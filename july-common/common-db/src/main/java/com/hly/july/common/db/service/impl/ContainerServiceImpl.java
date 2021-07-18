package com.hly.july.common.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.core.constant.ContainerEnum;
import com.hly.july.common.core.constant.ContainerStatusEnum;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.db.entity.Category;
import com.hly.july.common.db.entity.Container;
import com.hly.july.common.db.mapper.CategoryMapper;
import com.hly.july.common.db.mapper.ContainerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ContainerServiceImpl
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/17 18:35
 * @Version 1.0.0
 **/
@Slf4j
@Service
public class ContainerServiceImpl  extends ServiceImpl<ContainerMapper, Container> implements IService<Container> {
    public Boolean saveNewContainer(Container container) throws ServiceInternalException{
        if(ObjectUtils.isNotEmpty(container)) {
            if (StringUtils.isEmpty(container.getContainerId())||StringUtils.isEmpty(container.getContentId())||
                    container.getContainerType()==null||container.getContentType()==null ||
            !ContainerEnum.getAllCodeList().contains(container.getContainerType())||!ContainerEnum.getAllCodeList().contains(container.getContentType())) {
                log.warn("saveNewContainer some vale is wrong, container:{}", container.toString());
                throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
            }
            container.setStatus(ContainerStatusEnum.NORMAL.getCode());
            return save(container);
        }else{
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }

    }

    public List<String> getAllParentIdByPTypeAndCId(String childId,Integer parentType) throws ServiceInternalException{
        if (StringUtils.isEmpty(childId)) {
            log.warn("getAllParentIdByPTypeAndChildId ,childId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id", childId).in(parentType!=null,"container_type",parentType).in("status",ContainerStatusEnum.getVisibleStatusCodeList());
        List<Container> tempContainerList = baseMapper.selectList(queryWrapper);
        List<String> parentIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(tempContainerList)){
            for (Container container : tempContainerList) {
                parentIdList.add(container.getContainerId());
            }
        }
        return parentIdList;
    }

    public Boolean deleteByParentId(String parentId,Integer childType) throws ServiceInternalException{
        if (StringUtils.isEmpty(parentId)) {
            log.warn("deleteAllChildByParentId ,parentId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("container_id", parentId).in(childType!=null,"content_type",childType);
        return super.remove(queryWrapper);
    }

    public List<String> getAllChildIdByCTypeAndPId(String parentId,Integer childType) throws ServiceInternalException{
        if (StringUtils.isEmpty(parentId)) {
            log.warn("getAllChildIdByCTypeAndPId ,parentId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("container_id", parentId).eq(childType!=null,"content_type",childType).in("status",ContainerStatusEnum.getVisibleStatusCodeList());
        List<Container> tempContainerList = baseMapper.selectList(queryWrapper);
        List<String> childIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(tempContainerList)){
            for (Container container : tempContainerList) {
                childIdList.add(container.getContentId());
            }
        }
        return childIdList;
    }

    public Boolean deleteByChildId(String childId,Integer parentType) throws ServiceInternalException{
        if (StringUtils.isEmpty(childId)) {
            log.warn("deleteAllParentByChildId ,childId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id", childId).in(parentType!=null,"container_type",parentType);
        return super.remove(queryWrapper);
    }

    public List<Container> getContainer(String parentId,String childId,List<Integer> containerStatus) throws ServiceInternalException{
        if (StringUtils.isEmpty(parentId)||StringUtils.isEmpty(childId)) {
            log.warn("getContainer ,parentId or childId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("container_id", parentId).eq("content_id", childId).in(CollectionUtils.isNotEmpty(containerStatus),"status",containerStatus);
        return baseMapper.selectList(queryWrapper);
    }


    public Boolean deleteContainer(String parentId,String childId) throws ServiceInternalException{
        if (StringUtils.isEmpty(parentId)||StringUtils.isEmpty(childId)) {
            log.warn("deleteContainer ,parentId or childId is null");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("container_id", parentId).eq("content_id", childId);
        return super.remove(queryWrapper);
    }
}
