package com.hly.july.biz.video.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hly.july.biz.video.utils.VideoUtil;
import com.hly.july.common.core.constant.*;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.DateUtils;
import com.hly.july.common.db.entity.FileHelper;
import com.hly.july.common.db.entity.Video;
import com.hly.july.common.web.util.FileHelperUtil;
import com.hly.july.common.web.vo.RecentVO;
import com.hly.july.common.web.vo.RelationVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AsyncJobService
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/18 14:50
 * @Version 1.0.0
 **/
@Service
@Slf4j
public class AsyncJobService {
    @Autowired
    private VideoServiceImpl videoService;

    @Autowired
    private FileHelperServiceImpl fileHelperService;

    @Resource
    private FileHelperUtil fileHelperUtil;

    @Value("${file.upload.imagePath}")
    private String imageSavePath;

    @Async("taskExecutor")
    public Boolean getPosterFromVideFile(String videoId) {
        log.info("getPosterFromVideFile videoId:{}",videoId);
        if(StringUtils.isEmpty(videoId)){
            log.warn("getPosterFromVideFile videoId is empty");
            return false;
        }
        Video video = videoService.getById(videoId);
        if(video==null|| !VideoStatusEnum.getAllInvisibleCodeList().contains(video.getStatus())|| StringUtils.isEmpty(video.getVideoFileId())){
            log.warn("getPosterFromVideFile video is wrong, video:{}",video.toString());
            return false;
        }
        try {
            File videoFile = fileHelperService.fetchFileByFileId(video.getVideoFileId(),FileStatusEnum.getAllInvisibleCodeList());
            if(videoFile!=null){
                String fileId = IdWorker.getIdStr();
                String fileSuffix = VideoConstants.POSTER_FORMAT;
                String midPath = DateUtils.getCurrentTime("yyyy-MM-dd");
                String saveFileName = fileId+"."+fileSuffix;
                String endPath = Paths.get(midPath,saveFileName).toString();
                String frontPath  = Paths.get(imageSavePath,midPath).toString();
                try {
                    File posterFile = VideoUtil.fetchFrame(videoFile.getAbsolutePath(),frontPath,saveFileName);
                    boolean isExists = fileHelperService.isExistsByPath(endPath,FileTypeEnum.IMAGE.getCode());
                    if(isExists){
                        String md5 = fileHelperUtil.getMD5(posterFile);
                        FileHelper fileHelperSaver = new FileHelper();
                        fileHelperSaver.setFileId(fileId);
                        fileHelperSaver.setFileName(fileId);
                        fileHelperSaver.setFileSuffix(fileSuffix.toLowerCase());
                        fileHelperSaver.setUploaderId(video.getAuthorId());
                        fileHelperSaver.setType(FileTypeEnum.IMAGE.getCode());
                        fileHelperSaver.setFileMd5(md5);
                        fileHelperSaver.setStatus(FileStatusEnum.PUBLIC.getCode());
                        fileHelperSaver.setGmtCreate(DateUtils.getCurrentDateTime());
                        fileHelperSaver.setGmtUpdate(fileHelperSaver.getGmtCreate());
                        fileHelperSaver.setSavePath(endPath);
                        fileHelperSaver.setFileSize(posterFile.length()); //单位B
                        boolean fileResult = fileHelperService.save(fileHelperSaver);
                        if(fileResult){
                            Video videoUpdater = new Video();
                            videoUpdater.setVideoId(video.getVideoId());
                            videoUpdater.setVideoPosterId(fileHelperSaver.getFileId());
                            videoUpdater.setGmtUpdate(DateUtils.getCurrentDateTime());
                            return videoService.updateById(videoUpdater);
                        }else{
                            log.warn("getPosterFromVideFile save fileHelper fail, fileHelperSaver:{}",fileHelperSaver.toString());
                            return false;
                        }
                    }else{
                        log.error("getPosterFromVideFile save poster file fail, video:{}",video.toString());
                        return false;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("getPosterFromVideFile get frame error, video:{}",video.toString());
                    return false;
                }
            }else{
                log.error("getPosterFromVideFile videoFile not exists, video:{}",video.toString());
                return false;
            }
        }catch (ServiceInternalException e){
            log.error("getPosterFromVideFile ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
            return false;
        }

    }


}
