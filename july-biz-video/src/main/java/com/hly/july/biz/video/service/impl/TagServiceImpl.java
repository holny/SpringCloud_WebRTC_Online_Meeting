package com.hly.july.biz.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.core.constant.ContainerStatusEnum;
import com.hly.july.common.core.constant.RelationTypeEnum;
import com.hly.july.common.core.constant.TagTypeEnum;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.DateUtils;
import com.hly.july.common.core.util.WrappedBeanCopier;
import com.hly.july.common.db.entity.Relation;
import com.hly.july.common.db.entity.Tag;
import com.hly.july.common.db.mapper.TagMapper;

import com.hly.july.common.web.vo.TagInfoVO;
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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements IService<Tag> {
    public Tag initNewTag(TagInfoVO tagInfoVO) throws ServiceInternalException {
        if(ObjectUtils.isNotEmpty(tagInfoVO)){
            if(StringUtils.isEmpty(tagInfoVO.getTagAuthorId())||StringUtils.isEmpty(tagInfoVO.getTagName())){
                log.warn("initNewViewInfo authorId is null, tagInfoVO:{}",tagInfoVO.toString());
                throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
            }
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tag_name", tagInfoVO.getTagName()).in("status",ContainerStatusEnum.getVisibleStatusCodeList()).orderByDesc("gmt_create");
            List<Tag> tempTagList = baseMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(tempTagList)){
                throw new ServiceInternalException(ResultCode.API_DUPLICATE_DATA);
            }
            Tag saver = WrappedBeanCopier.copyProperties(tagInfoVO,Tag.class);
            saver.setTagName(StringEscapeUtils.escapeSql(tagInfoVO.getTagName()));
            saver.setType(TagTypeEnum.VIDEO.getCode());
            saver.setStatus(ContainerStatusEnum.NORMAL.getCode());
            saver.setGmtCreate(DateUtils.getCurrentDateTime());
            saver.setTagId(IdWorker.getIdStr());
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

    public List<Tag> getTagListByAuthorId(String authorId,Integer tagType) throws ServiceInternalException{
        if(StringUtils.isEmpty(authorId)){
            log.warn("getTagListByAuthorId authorId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_author_id", authorId).eq(tagType!=null,"type", tagType).in("status",ContainerStatusEnum.getVisibleStatusCodeList()).orderByDesc("gmt_create");
        List<Tag> tagList = baseMapper.selectList(queryWrapper);
        return tagList;
    }

    public List<Tag> getTagByStatusAndType(List<Integer> tagStatusCodeList,List<Integer> tagTypeCodeList) throws ServiceInternalException{
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(tagStatusCodeList),"status",tagStatusCodeList).in(CollectionUtils.isNotEmpty(tagTypeCodeList),"type",tagTypeCodeList).orderByDesc("gmt_create");
        List<Tag> tagList = baseMapper.selectList(queryWrapper);
        return tagList;
    }

    public Tag getTag(String tagId) throws ServiceInternalException{
        if(StringUtils.isEmpty(tagId)){
            log.warn("getTag tagId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_author_id", tagId).in("status",ContainerStatusEnum.getVisibleStatusCodeList()).orderByDesc("gmt_create");
        List<Tag> tagList = baseMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(tagList)){
            return tagList.get(0);
        }else{
            return null;
        }
    }

    public List<Tag> getTags(List<String> tagIds) throws ServiceInternalException{
        if(CollectionUtils.isEmpty(tagIds)){
            log.warn("getTags tagIds is empty");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("tag_id", tagIds).in("status",ContainerStatusEnum.getVisibleStatusCodeList());
        List<Tag> tagList = baseMapper.selectList(queryWrapper);
        return tagList;
    }

    public Boolean deleteTag(String tagId) throws ServiceInternalException{
        if(StringUtils.isEmpty(tagId)){
            log.warn("deleteTag tagId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        UpdateWrapper<Tag> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", ContainerStatusEnum.DELETE.getCode()).eq("tag_id", tagId).in("status",ContainerStatusEnum.getVisibleStatusCodeList());
        return super.update(updateWrapper);
    }
}
