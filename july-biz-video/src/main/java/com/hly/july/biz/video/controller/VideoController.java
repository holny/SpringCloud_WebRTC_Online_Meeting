package com.hly.july.biz.video.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hly.july.biz.video.entity.VideoSearchVO;
import com.hly.july.biz.video.service.impl.AsyncJobService;
import com.hly.july.biz.video.service.impl.VideoServiceImpl;
import com.hly.july.common.core.constant.JulyConstants;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.validation.group.NewVideoGroup;
import com.hly.july.common.web.vo.VideoInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @ClassName VideoController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/16 14:25
 * @Version 1.0.0
 **/
@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {
    @Autowired
    private VideoServiceImpl videoService;

    @Autowired
    private AsyncJobService asyncJobService;

    @PostMapping("/")
    public Result<VideoInfoVO> postNewVideo(@Validated(NewVideoGroup.class)@RequestBody VideoInfoVO video) throws IOException {
        log.info("postNewVideo video:"+video.toString());
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                video.setAuthorId(hostId);
                VideoInfoVO videoInfoVO = videoService.initNewViewInfo(video);
                return Result.success(videoInfoVO);
            }catch (ServiceInternalException e){
                log.error("postNewVideo ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }

    @PostMapping("/{videoId}/uploadVideo")
    public Result<VideoInfoVO> handleFileUpload(@PathVariable String videoId , @RequestHeader(value = "upload-file-type", required = false) String type, @RequestPart(value = "file") final MultipartFile uploadfile) throws IOException {
        log.info("handleFileUpload videoId:{}, type:{}",videoId,type);
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (StringUtils.isNotEmpty(hostId)) {
            try {
                if(JulyConstants.UPLOAD_TYPE_VIDEO.equals(type)){
                    VideoInfoVO videoInfoVO = videoService.saveVideoFile(uploadfile,videoId,hostId);
                    // 开启异步任务，获取Poster并保存
                    asyncJobService.getPosterFromVideFile(videoInfoVO.getVideoId());
                    return Result.success(videoInfoVO);
                }
                return Result.failure(ResultCode.API_VALIDATION_ERROR);
            }catch (ServiceInternalException e){
                log.error("postNewVideo ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }


    @PostMapping("/search")
    public Result<Page<VideoInfoVO>> getVideoBySearch(@Validated(NewVideoGroup.class)@RequestBody VideoSearchVO videoSearchVO) {
        log.info("getVideoBySearch videoSearchVO:{}",videoSearchVO);
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Long current = new Long(0);
        Long limit = new Long(0);
        if(videoSearchVO.getCurrentPageNum()!=null){
            current = videoSearchVO.getCurrentPageNum();
        }
        if(videoSearchVO.getPageLimit()!=null){
            limit = videoSearchVO.getPageLimit();
        }
        //创建Page对象
        Page<VideoInfoVO> videoInfoPage = new Page<>(current,limit);
        try {
            Page<VideoInfoVO> categoryInfoVOS = videoService.getVideoInfoVOPage(videoInfoPage,videoSearchVO);
            return Result.success(categoryInfoVOS);
        }catch (ServiceInternalException e){
            log.error("getVideoBySearch ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
            return Result.failure(e.getResultCode());
        }
    }

    @GetMapping("/{videoId}")
    public Result<VideoInfoVO> getVideoInfoVO(@PathVariable String videoId ) {
        log.info("getVideoInfoVO videoId:{}",videoId);
        try {
            VideoInfoVO videoInfoVO = videoService.getVideoInfoByVideoId(videoId);
            return Result.success(videoInfoVO);
        }catch (ServiceInternalException e){
            log.error("getVideoInfoVO ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
            return Result.failure(e.getResultCode());
        }
    }

    @GetMapping(value = "/{videoId}/player")
    public Result<String> getVideoFile(@PathVariable("videoId") String videoId, HttpServletRequest request, HttpServletResponse response) {
        log.debug("getVideoFile videoId:{}",videoId);
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        try {
            File videoFile = videoService.fetchVideoFileByVideoId(videoId,hostId);
            responseFile(response, videoFile);
            return Result.success();
        }catch (ServiceInternalException e){
            log.error("getVideoInfoVO ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
            return Result.failure(e.getResultCode());
        }
    }

    @GetMapping(value = "/{videoId}/poster")
    public Result<String> getPosterFile(@PathVariable("videoId") String videoId, HttpServletRequest request, HttpServletResponse response) {
        log.debug("getPosterFile videoId:{}",videoId);
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        try {
            File posterFile = videoService.fetchPosterFileByVideoId(videoId,hostId);
            responseFile(response, posterFile);
            return Result.success();
        }catch (ServiceInternalException e){
            log.error("getPosterFile ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
            return Result.failure(e.getResultCode());
        }
    }

    private void responseFile(HttpServletResponse response, File file) {
        try (InputStream is = new FileInputStream(file);
             OutputStream os = response.getOutputStream();) {
            byte[] buffer = new byte[1024]; // 文件流缓存池
            while (is.read(buffer) != -1) {
                os.write(buffer);
            }
            os.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


}
