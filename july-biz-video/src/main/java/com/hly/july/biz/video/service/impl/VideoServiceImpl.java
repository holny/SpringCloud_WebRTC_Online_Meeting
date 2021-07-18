package com.hly.july.biz.video.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.hly.july.biz.video.entity.VideoSearchVO;
import com.hly.july.biz.video.mapper.VideoInfoMapper;
import com.hly.july.biz.video.service.api.BizUserApiService;
import com.hly.july.common.core.constant.*;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.DateUtils;
import com.hly.july.common.core.util.JulyAuthorityUtils;
import com.hly.july.common.db.entity.FileHelper;
import com.hly.july.common.db.entity.Tag;
import com.hly.july.common.db.entity.User;
import com.hly.july.common.db.entity.Video;
import com.hly.july.common.db.mapper.UserMapper;
import com.hly.july.common.db.mapper.VideoMapper;
import com.hly.july.common.db.service.IVideoService;
import com.hly.july.common.web.util.FileHelperUtil;
import com.hly.july.common.web.vo.RelationVO;
import com.hly.july.common.web.vo.UserInfoVO;
import com.hly.july.common.web.vo.VideoInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @ClassName VideoSerivceImpl
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/16 14:59
 * @Version 1.0.0
 **/
@Slf4j
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IService<Video> {
    @Resource
    private BizUserApiService bizUserApiService;

    @Autowired
    private FileHelperServiceImpl fileHelperService;

    @Resource
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private TagServiceImpl tagService;

    @Autowired
    private AsyncJobService asyncJobService;

    public VideoInfoVO initNewViewInfo(VideoInfoVO video) throws ServiceInternalException {
        if(ObjectUtils.isNotEmpty(video)){
            if(StringUtils.isEmpty(video.getAuthorId())){
                log.warn("initNewViewInfo authorId is null, video:{}",video.toString());
                throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
            }
            Result<UserInfoVO> authorInfoResult;
            try {
                authorInfoResult =  bizUserApiService.getUser(video.getAuthorId());
            }catch (Exception e){
                e.printStackTrace();
                log.warn("initNewViewInfo get authorInfo fail, video:{}",video.toString());
                throw new ServiceInternalException(ResultCode.API_FAIL_500);
            }
            if(authorInfoResult!=null&&(ResultCode.SUCCESS.getCode()==authorInfoResult.getCode())){
                // 说明作者存在
                Video saveVideo = new Video();
                saveVideo.setVideoId(IdWorker.getIdStr());
                saveVideo.setAuthorId(video.getAuthorId());
                if(StringUtils.isNotEmpty(video.getTitle())){
                    saveVideo.setTitle(StringEscapeUtils.escapeSql(video.getTitle()));
                }
                if(StringUtils.isNotEmpty(video.getInfo())){
                    saveVideo.setInfo(StringEscapeUtils.escapeSql(video.getInfo()));
                }
                if(StringUtils.isNotEmpty(video.getPassword())){
                    saveVideo.setPassword(video.getPassword());
                }
                if(video.getIsNeedTop()!=null&&video.getIsNeedTop()){
                    saveVideo.setIsTop(new Integer(1));
                }else{
                    saveVideo.setIsTop(new Integer(0));
                }
                if(CollectionUtils.isNotEmpty(video.getTags())){
                    String result = Joiner.on(",").join(video.getTags());
                    saveVideo.setTags(result);
                }
                saveVideo.setCategoryMain(video.getCategoryMain());
                saveVideo.setCategorySub(video.getCategorySub());
                saveVideo.setSeriesId(video.getSeriesId());
                saveVideo.setStatus(VideoStatusEnum.IN_PROCESS.getCode());
                if(video.getType()==null||!VideoTypeEnum.getAllTypeCodeList().contains(video.getType())){
                    saveVideo.setType(VideoTypeEnum.NORMAL.getCode());
                }else{
                    saveVideo.setType(video.getType());
                }
                boolean reulst = save(saveVideo);
                if(!reulst){
                    throw new ServiceInternalException(ResultCode.API_DB_FAIL);
                }
                VideoInfoVO videoInfoVO = new VideoInfoVO(saveVideo,authorInfoResult.getData());
                return videoInfoVO;
            }else{
                throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
            }

        }else{
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
    }

    public VideoInfoVO saveVideoFile(MultipartFile file,String videoId,String hostId) throws ServiceInternalException {
        log.info("saveVideoFile videoId:{} hostId:{}",videoId,hostId);
        if(file==null||StringUtils.isEmpty(videoId)||StringUtils.isEmpty(hostId)){
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        Video video = getById(videoId);
        if(video==null){
            throw new ServiceInternalException(ResultCode.VIDEO_ID_NOT_EXISTS);
        }
        if(!hostId.equals(video.getAuthorId())){
            throw new ServiceInternalException(ResultCode.AUTH_UNAUTHORIZED);
        }
        if(!VideoStatusEnum.IN_PROCESS.getCode().equals(video.getStatus())){
            throw new ServiceInternalException(ResultCode.API_CAN_NOT_EDIT);
        }
        FileHelper fileHelper = fileHelperService.saveMultipartFile(file,video.getAuthorId(), FileTypeEnum.VIDEO.getCode());
        if(fileHelper!=null){
            log.info("saveVideoFile fileHelper:{}",fileHelper.toString());
            Video updater = new Video();
            updater.setVideoId(video.getVideoId());
            updater.setVideoFileId(fileHelper.getFileId());
            updater.setGmtUpdate(DateUtils.getCurrentDateTime());
            // Todo: 视频文件以及保存，这里暂时先把文件设置为Pubic状态，正式需设置为private
            updater.setStatus(VideoStatusEnum.PUBLIC.getCode());
            if(StringUtils.isNotEmpty(fileHelper.getFileSuffix())){
                if(fileHelper.getFileSuffix().equals("mp4")){
                    updater.setVideoFormat(VideoConstants.VIDEO_FORMAT_MP4);
                }else if(fileHelper.getFileSuffix().equals("webm")){
                    updater.setVideoFormat(VideoConstants.VIDEO_FORM_WEBM);
                }
            }
            updateById(updater);

            video.setVideoFileId(updater.getVideoFileId());
            video.setGmtUpdate(updater.getGmtUpdate());
            video.setStatus(updater.getStatus());

            Result<UserInfoVO> authorInfoResult;
            try {
                authorInfoResult =  bizUserApiService.getUser(video.getAuthorId());
            }catch (Exception e){
                e.printStackTrace();
                log.warn("saveVideoFile get authorInfo fail, video:{}",video.toString());
                throw new ServiceInternalException(ResultCode.API_FAIL_500);
            }
            if(authorInfoResult!=null&&(ResultCode.SUCCESS.getCode()==authorInfoResult.getCode())){
                // 说明作者存在
                VideoInfoVO videoInfoVO = new VideoInfoVO(video,authorInfoResult.getData());
                return videoInfoVO;
            }else{
                throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
            }

        }else{
            throw new ServiceInternalException(ResultCode.FILE_SAVE_ERROR);
        }
    }

    public Page<VideoInfoVO> getVideoInfoVOPage(Page page, VideoSearchVO videoSearchVO) throws ServiceInternalException {
        if(ObjectUtils.isEmpty(videoSearchVO)){
            log.info("getVideoInfoVOPage videoSearchVO is empty ,ideoSearchVO:{}",videoSearchVO.toString());
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        if(StringUtils.isNotEmpty(videoSearchVO.getOrderByClause())){
            if(videoSearchVO.getIsDesc()!=null&&!videoSearchVO.getIsDesc()){
                videoSearchVO.setOrderByClause(videoSearchVO.getOrderByClause().trim()+" desc");
            }else{
                videoSearchVO.setOrderByClause(videoSearchVO.getOrderByClause().trim()+" asc");
            }
        }
        if(StringUtils.isNotEmpty(videoSearchVO.getSearch())){
            videoSearchVO.setSearch(StringEscapeUtils.escapeSql(videoSearchVO.getSearch()));
        }
        videoSearchVO.setVideoStatusList(VideoStatusEnum.getPublicCodeList());
        videoSearchVO.setVideoTypeList(VideoTypeEnum.getAllTypeCodeList());
        videoSearchVO.setSeriesStatusList(ContainerStatusEnum.getPublicStatusCodeList());
        Page<VideoInfoVO> videoInfoVOIPage =  videoInfoMapper.selectVideoVoInfoPageBySearch(page,videoSearchVO);
        List<VideoInfoVO> videoInfoVOS =  videoInfoVOIPage.getRecords();
        videoInfoVOS.forEach(videoInfoVO -> {
            if(StringUtils.isNotEmpty(videoInfoVO.getTagStr())){
                List<String> tags = Arrays.asList(videoInfoVO.getTagStr().split(","));
                if(CollectionUtils.isNotEmpty(tags)){
                    try {
                        List<Tag> tagList = tagService.getTags(tags);
                        videoInfoVO.setTagList(tagList);
                    }catch (ServiceInternalException e){
                        log.error("getVideoInfoByVideoId ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                    }
                }
            }
            if(videoInfoVO.getAuthorGenderCode()!=null) {
                if (JulyConstants.USER_GENDER_FEMALE.equals(videoInfoVO.getAuthorGenderCode())) {
                    videoInfoVO.setAuthorGender("female");
                } else if (JulyConstants.USER_GENDER_MALE.equals(videoInfoVO.getAuthorGenderCode())) {
                    videoInfoVO.setAuthorGender("male");
                } else {
                    videoInfoVO.setAuthorGender("male");
                }
            }
            videoInfoVO.setAuthorStatus( UserStatusEnum.getDescByCode(videoInfoVO.getAuthorStatusCode()));
            if(StringUtils.isNotEmpty(videoInfoVO.getAuthorRoleStr())) {
                videoInfoVO.setAuthorRole(JulyAuthorityUtils.roleClassifyString2Set(videoInfoVO.getAuthorRoleStr()));
            }
        });

        return videoInfoVOIPage;
    }

    public VideoInfoVO getVideoInfoByVideoId(String videId) throws ServiceInternalException {
        if(ObjectUtils.isEmpty(videId)){
            log.info("getVideoInfoByVideoIf videId is null ");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        VideoSearchVO searchVO = new VideoSearchVO();
        searchVO.setVideoId(videId);
        searchVO.setVideoStatusList(VideoStatusEnum.getPublicCodeList());
        searchVO.setVideoTypeList(VideoTypeEnum.getAllTypeCodeList());
        searchVO.setSeriesStatusList(ContainerStatusEnum.getPublicStatusCodeList());
        log.info("start searchVO:{}",searchVO.toString());
        List<VideoInfoVO> videoInfoVOs =  videoInfoMapper.selectVideoVoInfoBySearch(searchVO);
        log.info("start videoInfoVOs:{}",videoInfoVOs.toString());
        if(CollectionUtils.isNotEmpty(videoInfoVOs)){
            VideoInfoVO videoInfoVO = videoInfoVOs.get(0);
            if(StringUtils.isNotEmpty(videoInfoVO.getTagStr())){
                List<String> tags = Arrays.asList(videoInfoVO.getTagStr().split(","));
                if(CollectionUtils.isNotEmpty(tags)){
                    try {
                        List<Tag> tagList = tagService.getTags(tags);
                        videoInfoVO.setTagList(tagList);
                    }catch (ServiceInternalException e){
                        log.error("getVideoInfoByVideoId ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                    }
                }
            }
            if(videoInfoVO.getAuthorGenderCode()!=null) {
                if (JulyConstants.USER_GENDER_FEMALE.equals(videoInfoVO.getAuthorGenderCode())) {
                    videoInfoVO.setAuthorGender("female");
                } else if (JulyConstants.USER_GENDER_MALE.equals(videoInfoVO.getAuthorGenderCode())) {
                    videoInfoVO.setAuthorGender("male");
                } else {
                    videoInfoVO.setAuthorGender("male");
                }
            }
            videoInfoVO.setAuthorStatus( UserStatusEnum.getDescByCode(videoInfoVO.getAuthorStatusCode()));
            if(StringUtils.isNotEmpty(videoInfoVO.getAuthorRoleStr())) {
                videoInfoVO.setAuthorRole(JulyAuthorityUtils.roleClassifyString2Set(videoInfoVO.getAuthorRoleStr()));
            }
            return videoInfoVOs.get(0);
        }else{
            return null;
        }
    }

    public File fetchVideoFileByVideoId(String videoId,String hostId) throws ServiceInternalException{
        if(ObjectUtils.isEmpty(videoId)){
            log.info("fetchVideoFileByVideoId videoId is null ");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        Video video = getById(videoId);
        if(VideoStatusEnum.getPublicCodeList().contains(video.getStatus())||(VideoStatusEnum.getAllInvisibleCodeList().contains(video.getStatus())&&StringUtils.isNotEmpty(hostId)&&video.getAuthorId().equals(hostId))){
            File videoFile = fileHelperService.fetchFileByFileId(video.getVideoFileId(),FileStatusEnum.getAllPublicCodeList());
            return videoFile;
        }else{
            log.info("video not exists ,video:{}",video.toString());
            throw new ServiceInternalException(ResultCode.VIDEO_ID_NOT_EXISTS);
        }
    }

    public File fetchPosterFileByVideoId(String videoId,String hostId) throws ServiceInternalException{
        if(ObjectUtils.isEmpty(videoId)){
            log.info("fetchPosterFileByVideoId videoId is null ");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        Video video = getById(videoId);
        if(VideoStatusEnum.getPublicCodeList().contains(video.getStatus())||(VideoStatusEnum.getAllInvisibleCodeList().contains(video.getStatus())&&StringUtils.isNotEmpty(hostId)&&video.getAuthorId().equals(hostId))){
            File videoFile = fileHelperService.fetchFileByFileId(video.getVideoPosterId(),FileStatusEnum.getAllPublicCodeList());
            return videoFile;
        }else{
            log.info("poster not exists ,video:{}",video.toString());
            throw new ServiceInternalException(ResultCode.VIDEO_ID_NOT_EXISTS);
        }
    }

}
