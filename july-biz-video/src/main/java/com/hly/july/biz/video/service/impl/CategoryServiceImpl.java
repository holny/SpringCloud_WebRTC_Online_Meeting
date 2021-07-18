package com.hly.july.biz.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.core.constant.CategoryTypeEnum;
import com.hly.july.common.core.constant.ContainerEnum;
import com.hly.july.common.core.constant.ContainerStatusEnum;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.db.entity.Category;
import com.hly.july.common.db.entity.Container;
import com.hly.july.common.db.entity.Series;
import com.hly.july.common.db.mapper.CategoryMapper;
import com.hly.july.common.db.mapper.SeriesMapper;
import com.hly.july.common.db.service.impl.ContainerServiceImpl;
import com.hly.july.common.web.vo.CategoryInfoVO;
import com.hly.july.common.web.vo.SeriesInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/17 18:15
 * @Version 1.0.0
 **/
@Slf4j
@Service
public class CategoryServiceImpl  extends ServiceImpl<CategoryMapper, Category> implements IService<Category> {
    @Autowired
    private ContainerServiceImpl containerService;

    public CategoryInfoVO initNewCategory(CategoryInfoVO categoryInfoVO) throws ServiceInternalException {
        if(ObjectUtils.isNotEmpty(categoryInfoVO)) {
            if (StringUtils.isEmpty(categoryInfoVO.getMainCategoryName())&&StringUtils.isEmpty(categoryInfoVO.getMainCategoryId())
                    ||categoryInfoVO.getType()==null||!CategoryTypeEnum.getAllTypeCodeList().contains(categoryInfoVO.getType())) {
                log.warn("initNewCategory authorId is null, seriesInfoVO:{}", categoryInfoVO.toString());
                throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
            }
            Category mainCategory = null;
            if(StringUtils.isNotEmpty(categoryInfoVO.getMainCategoryId())){
                QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("category_id", categoryInfoVO.getMainCategoryId()).eq("level", 1).eq("type", categoryInfoVO.getType());
                List<Category> tempCategoryList = baseMapper.selectList(queryWrapper);
                if(CollectionUtils.isNotEmpty(tempCategoryList)){
                   mainCategory = tempCategoryList.get(0);
                }
            }else{
                QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("category_name", categoryInfoVO.getMainCategoryName()).eq("level", 1).eq("type", categoryInfoVO.getType());
                List<Category> tempCategoryList = baseMapper.selectList(queryWrapper);
                if(CollectionUtils.isNotEmpty(tempCategoryList)){
                    mainCategory = tempCategoryList.get(0);
                }else{
                    Category saver = new Category();
                    saver.setCategoryId(IdWorker.getIdStr());
                    saver.setType(categoryInfoVO.getType());
                    saver.setCategoryName(StringEscapeUtils.escapeSql(categoryInfoVO.getMainCategoryName().trim()));
                    saver.setLevel(1);
                    boolean result = save(saver);
                    if(result){
                        mainCategory = saver;
                    }else{
                        log.warn("initNewCategory mainCategory save fail, categoryInfoVO:{}", categoryInfoVO.toString());
                        throw new ServiceInternalException(ResultCode.API_DB_FAIL);
                    }
                }
            }
            if(mainCategory==null){
                log.warn("initNewCategory mainCategory is null, categoryInfoVO:{}", categoryInfoVO.toString());
                throw new ServiceInternalException(ResultCode.API_DB_FAIL);
            }
            if (StringUtils.isEmpty(categoryInfoVO.getSubCategoryName())) {
               // subCategory为空，说明无需新建subCategory
                CategoryInfoVO resultCategoryInfoVO  = new CategoryInfoVO();
                resultCategoryInfoVO.setMainCategoryId(mainCategory.getCategoryId());
                resultCategoryInfoVO.setMainCategoryName(mainCategory.getCategoryName());
                resultCategoryInfoVO.setType(categoryInfoVO.getType());
                return resultCategoryInfoVO;
            }
            Category subCategory = null;
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_name", categoryInfoVO.getSubCategoryName()).eq("level", 2).eq("type", categoryInfoVO.getType());
            List<Category> tempCategoryList = baseMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(tempCategoryList)){
                subCategory = tempCategoryList.get(0);
                List<Container> containers = containerService.getContainer(mainCategory.getCategoryId(),subCategory.getCategoryId(),ContainerStatusEnum.getVisibleStatusCodeList());
                if(CollectionUtils.isNotEmpty(containers)){
                    log.warn("initNewCategory ,category group already exists  categoryInfoVO:{}", categoryInfoVO.toString());
                    throw new ServiceInternalException(ResultCode.API_DUPLICATE_DATA);
                }else{
                    // 如果主次category name 都存在，但是不存在包含关系，就加上包含关系
                    Container newContainer = new Container();
                    newContainer.setContainerId(mainCategory.getCategoryId());
                    newContainer.setContainerType(ContainerEnum.MAIN_CATEGORY.getCode());
                    newContainer.setContentId(subCategory.getCategoryId());
                    newContainer.setContentType(ContainerEnum.SUB_CATEGORY.getCode());
                    boolean result = containerService.saveNewContainer(newContainer);
                    if(result){
                        CategoryInfoVO resultCategoryInfoVO  = new CategoryInfoVO();
                        resultCategoryInfoVO.setMainCategoryId(mainCategory.getCategoryId());
                        resultCategoryInfoVO.setMainCategoryName(mainCategory.getCategoryName());
                        resultCategoryInfoVO.setSubCategoryId(subCategory.getCategoryId());
                        resultCategoryInfoVO.setSubCategoryName(subCategory.getCategoryName());
                        resultCategoryInfoVO.setType(categoryInfoVO.getType());
                        return resultCategoryInfoVO;
                    }else{
                        log.warn("initNewCategory container 1 save fail, categoryInfoVO:{}", categoryInfoVO.toString());
                        throw new ServiceInternalException(ResultCode.API_DB_FAIL);
                    }
                }

            }else{
                Category saver = new Category();
                saver.setCategoryId(IdWorker.getIdStr());
                saver.setType(categoryInfoVO.getType());
                saver.setCategoryName(StringEscapeUtils.escapeSql(categoryInfoVO.getSubCategoryName().trim()));
                saver.setLevel(2);
                boolean result = save(saver);
                if(!result){
                    log.warn("initNewCategory subCategory save fail, categoryInfoVO:{}", categoryInfoVO.toString());
                    throw new ServiceInternalException(ResultCode.API_DB_FAIL);
                }
                subCategory = saver;
                Container newContainer = new Container();
                newContainer.setContainerId(mainCategory.getCategoryId());
                newContainer.setContainerType(ContainerEnum.MAIN_CATEGORY.getCode());
                newContainer.setContentId(subCategory.getCategoryId());
                newContainer.setContentType(ContainerEnum.SUB_CATEGORY.getCode());
                boolean resultContainer = containerService.saveNewContainer(newContainer);
                if(resultContainer){
                    CategoryInfoVO resultCategoryInfoVO  = new CategoryInfoVO();
                    resultCategoryInfoVO.setMainCategoryId(mainCategory.getCategoryId());
                    resultCategoryInfoVO.setMainCategoryName(mainCategory.getCategoryName());
                    resultCategoryInfoVO.setSubCategoryId(subCategory.getCategoryId());
                    resultCategoryInfoVO.setSubCategoryName(subCategory.getCategoryName());
                    resultCategoryInfoVO.setType(categoryInfoVO.getType());
                    return resultCategoryInfoVO;
                }else{
                    log.warn("initNewCategory container 2 save fail, categoryInfoVO:{}", categoryInfoVO.toString());
                    throw new ServiceInternalException(ResultCode.API_DB_FAIL);
                }
            }
        }else{
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
    }

    public List<Category> getAllParentCategoryList(List<Integer> categoryTypeList) throws ServiceInternalException {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(categoryTypeList),"type", categoryTypeList).eq("level", 1);
        return baseMapper.selectList(queryWrapper);
    }

    public List<CategoryInfoVO> getAllChildCategoryListByPId(String mainCategoryId) throws ServiceInternalException {
        if (StringUtils.isEmpty(mainCategoryId)) {
            log.warn("getAllChildCategoryListByPId mainCategoryId is null, categoryId:{}",mainCategoryId);
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        Category mainCateGory = null;
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", mainCategoryId).eq("level", 1);
        List<Category> tempMainCategoryList = baseMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(tempMainCategoryList)){
            mainCateGory = tempMainCategoryList.get(0);
            List<String> containers = containerService.getAllChildIdByCTypeAndPId(mainCateGory.getCategoryId(),ContainerEnum.SUB_CATEGORY.getCode());
            List<CategoryInfoVO> categoryInfoVOS = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(containers)){
                QueryWrapper<Category> querySubWrapper = new QueryWrapper<>();
                querySubWrapper.in("category_id", containers).eq("level", 2).eq("type",mainCateGory.getType());
                List<Category> tempSubCategoryList = baseMapper.selectList(querySubWrapper);
                if(CollectionUtils.isNotEmpty(tempSubCategoryList)){
                    for (Category category : tempSubCategoryList) {
                        CategoryInfoVO tempCategoryVO = new CategoryInfoVO();
                        tempCategoryVO.setMainCategoryId(mainCateGory.getCategoryId());
                        tempCategoryVO.setMainCategoryName(mainCateGory.getCategoryName());
                        tempCategoryVO.setSubCategoryId(category.getCategoryId());
                        tempCategoryVO.setSubCategoryName(category.getCategoryName());
                        tempCategoryVO.setType(mainCateGory.getType());
                        categoryInfoVOS.add(tempCategoryVO);
                    }
                }
            }
            return categoryInfoVOS;
        }else{
            log.warn("getAllChildCategoryListByPId mainCategory not exits, mainCategoryId:{}", mainCategoryId);
            throw new ServiceInternalException(ResultCode.API_NOT_FOUND);
        }
    }

    public Boolean deleteCategory(String categoryId,Integer level) throws ServiceInternalException {
        if (StringUtils.isEmpty(categoryId)||level==null) {
            log.warn("deleteCategory categoryId or level is null, categoryId:{}, level:{}",categoryId,level);
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        if(level==1){
            boolean result =  containerService.deleteByParentId(categoryId,ContainerEnum.SUB_CATEGORY.getCode());
            if(!result){
                log.warn("deleteCategory  delete container fail, categoryId:{}, level:{}", categoryId,level);
                throw new ServiceInternalException(ResultCode.API_DB_FAIL);
            }
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_id", categoryId).eq("level", level);
            return super.remove(queryWrapper);
        }else if(level==2){
            boolean result =  containerService.deleteByChildId(categoryId,ContainerEnum.MAIN_CATEGORY.getCode());
            if(!result){
                log.warn("deleteCategory  delete container fail, categoryId:{}, level:{}", categoryId,level);
                throw new ServiceInternalException(ResultCode.API_DB_FAIL);
            }else{
                return true;
            }
        }else{
            log.warn("deleteCategory level is wrong, categoryId:{}, level:{}",categoryId,level);
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
    }
}
