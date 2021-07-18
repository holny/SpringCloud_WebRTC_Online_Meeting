package com.hly.july.biz.video.controller;

import com.hly.july.biz.video.service.impl.SeriesServiceImpl;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.WrappedBeanCopier;
import com.hly.july.common.core.validation.group.NewSeriesGroup;
import com.hly.july.common.core.validation.group.NewVideoGroup;
import com.hly.july.common.db.entity.Series;
import com.hly.july.common.web.vo.SeriesInfoVO;
import com.hly.july.common.web.vo.VideoInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName SeriesAndTagController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/17 15:20
 * @Version 1.0.0
 **/
@RestController
@RequestMapping("/series")
@Slf4j
public class SeriesController {
    @Autowired
    private SeriesServiceImpl seriesService;

    @PostMapping("/")
    public Result<SeriesInfoVO> postNewSeries(@Validated(NewSeriesGroup.class)@RequestBody SeriesInfoVO seriesInfoVO){
        log.info("postNewSeries seriesInfoVO:"+seriesInfoVO.toString());
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                seriesInfoVO.setAuthorId(hostId);
                Series series = seriesService.initNewViewInfo(seriesInfoVO);
                return Result.success(WrappedBeanCopier.copyProperties(series,SeriesInfoVO.class));
            }catch (ServiceInternalException e){
                log.error("postNewSeries ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }

    @GetMapping("/")
    public Result<List<SeriesInfoVO>> getSeriesList() {
        log.info("getSeriesList");
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                List<Series> seriesList = seriesService.getSeriesListByAuthorId(hostId);
                return Result.success(WrappedBeanCopier.copyPropertiesOfList(seriesList,SeriesInfoVO.class));
            }catch (ServiceInternalException e){
                log.error("getSeriesList ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }

    @GetMapping("/{seriesId}")
    public Result<SeriesInfoVO> getSeries(@PathVariable String seriesId) {
        log.info("getSeries seriesId:{}",seriesId);
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                Series series = seriesService.getSeries(seriesId,hostId);
                return Result.success(WrappedBeanCopier.copyProperties(series,SeriesInfoVO.class));
            }catch (ServiceInternalException e){
                log.error("getSeries ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }

    @DeleteMapping("/{seriesId}")
    public Result<String> deleteSeries(@PathVariable String seriesId) {
        log.info("deleteSeries seriesId:{}",seriesId);
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                boolean result = seriesService.deleteSeries(seriesId);
                if(result){
                    return Result.success();
                }else{
                    return Result.failure(ResultCode.API_DB_FAIL);
                }
            }catch (ServiceInternalException e){
                log.error("deleteSeries ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }
}
