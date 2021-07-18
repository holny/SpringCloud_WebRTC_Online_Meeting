package com.hly.july.biz.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.core.constant.ContainerStatusEnum;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.DateUtils;
import com.hly.july.common.core.util.WrappedBeanCopier;
import com.hly.july.common.db.entity.Series;
import com.hly.july.common.db.entity.Tag;
import com.hly.july.common.db.entity.User;
import com.hly.july.common.db.mapper.SeriesMapper;
import com.hly.july.common.web.vo.SeriesInfoVO;
import com.hly.july.common.web.vo.VideoInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SeriesServiceImpl
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/17 15:25
 * @Version 1.0.0
 **/
@Slf4j
@Service
public class SeriesServiceImpl extends ServiceImpl<SeriesMapper, Series> implements IService<Series> {
    public Series initNewViewInfo(SeriesInfoVO seriesInfoVO) throws ServiceInternalException {
        if(ObjectUtils.isNotEmpty(seriesInfoVO)){
            if(StringUtils.isEmpty(seriesInfoVO.getAuthorId())||StringUtils.isEmpty(seriesInfoVO.getSeriesTitle())){
                log.warn("initNewViewInfo authorId is null, seriesInfoVO:{}",seriesInfoVO.toString());
                throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
            }
            QueryWrapper<Series> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("series_title", seriesInfoVO.getSeriesTitle()).in("status",ContainerStatusEnum.getVisibleStatusCodeList()).orderByDesc("gmt_create");
            List<Series> tempSeriesList = baseMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(tempSeriesList)){
                throw new ServiceInternalException(ResultCode.API_DUPLICATE_DATA);
            }
            Series saver = WrappedBeanCopier.copyProperties(seriesInfoVO,Series.class);
            saver.setSeriesTitle(StringEscapeUtils.escapeSql(seriesInfoVO.getSeriesTitle()));
            saver.setSeriesName(StringEscapeUtils.escapeSql(seriesInfoVO.getSeriesName()));
            saver.setSeriesInfo(StringEscapeUtils.escapeSql(seriesInfoVO.getSeriesInfo()));
            saver.setSeriesId(IdWorker.getIdStr());
            saver.setGmtCreate(DateUtils.getCurrentDateTime());
            saver.setGmtUpdate(saver.getGmtCreate());
            saver.setStatus(ContainerStatusEnum.NORMAL.getCode());
            boolean result = save(saver);
            if(result){
                return saver;
            }else{
                throw new ServiceInternalException(ResultCode.API_DB_FAIL);
            }
        }else {
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
    }

    public List<Series> getSeriesListByAuthorId(String authorId) throws ServiceInternalException{
        if(StringUtils.isEmpty(authorId)){
            log.warn("getSeriesListByAuthorId authorId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Series> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", authorId).in("status",ContainerStatusEnum.getVisibleStatusCodeList()).orderByDesc("gmt_update");
        List<Series> seriesList = baseMapper.selectList(queryWrapper);
        return seriesList;
    }

    public Series getSeries(String seriesId,String hostId) throws ServiceInternalException{
        if(StringUtils.isEmpty(seriesId)){
            log.warn("getSeries seriesId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Series> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("series_id", seriesId).eq(StringUtils.isNotEmpty(hostId),"author_id",hostId).in("status",ContainerStatusEnum.getVisibleStatusCodeList()).orderByDesc("gmt_update");
        List<Series> seriesList = baseMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(seriesList)){
            return seriesList.get(0);
        }else{
            return null;
        }
    }

    public Boolean deleteSeries(String seriesId) throws ServiceInternalException{
        if(StringUtils.isEmpty(seriesId)){
            log.warn("deleteTag seriesId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        UpdateWrapper<Series> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", ContainerStatusEnum.DELETE.getCode()).set("gmt_update", DateUtils.getCurrentDateTime()).eq("series_id", seriesId).in("status",ContainerStatusEnum.getVisibleStatusCodeList());
        return super.update(updateWrapper);
    }

}
