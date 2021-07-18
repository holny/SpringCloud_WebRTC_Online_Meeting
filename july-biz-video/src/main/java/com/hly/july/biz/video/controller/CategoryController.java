package com.hly.july.biz.video.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hly.july.biz.video.service.impl.CategoryServiceImpl;
import com.hly.july.common.core.constant.CategoryTypeEnum;
import com.hly.july.common.core.constant.TagTypeEnum;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.WrappedBeanCopier;
import com.hly.july.common.core.validation.group.NewCategoryGroup;
import com.hly.july.common.core.validation.group.NewSeriesGroup;
import com.hly.july.common.db.entity.Category;
import com.hly.july.common.db.entity.Series;
import com.hly.july.common.web.vo.CategoryInfoVO;
import com.hly.july.common.web.vo.SeriesInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CategoryController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/17 18:10
 * @Version 1.0.0
 **/
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @PostMapping("/")
    public Result<CategoryInfoVO> postNewCategory(@Validated(NewCategoryGroup.class)@RequestBody CategoryInfoVO categoryInfoVO){
        log.info("postNewCategory categoryInfoVO:"+categoryInfoVO.toString());
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                if(StringUtils.isNotEmpty(categoryInfoVO.getTypeStr())){
                    Integer code = CategoryTypeEnum.getCodeByDesc(categoryInfoVO.getTypeStr());
                    if(code!=null){
                        categoryInfoVO.setType(code);
                    }
                }
                CategoryInfoVO category = categoryService.initNewCategory(categoryInfoVO);
                if(category!=null){
                    return Result.success(category);
                }else{
                    return Result.failure(ResultCode.API_DB_FAIL);
                }
            }catch (ServiceInternalException e){
                log.error("postNewSeries ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }

    }

    @GetMapping("/")
    public Result<List<CategoryInfoVO>> getMainCategoryList(@RequestParam(value = "type", required = false) String type) {
        log.info("getMainCategoryList");
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                List<Integer> categoryTypeCodeList = new ArrayList<>();
                if(StringUtils.isNotEmpty(type)){
                    Integer code = CategoryTypeEnum.getCodeByDesc(type.toLowerCase());
                    if(code!=null){
                        categoryTypeCodeList.add(code);
                    }
                }
                if(CollectionUtils.isNotEmpty(categoryTypeCodeList)){
                    categoryTypeCodeList=CategoryTypeEnum.getAllTypeCodeList();
                }
                List<Category> categoryList = categoryService.getAllParentCategoryList(categoryTypeCodeList);
                List<CategoryInfoVO> categoryInfoVOS = new ArrayList<>();
                for (Category category : categoryList) {
                    CategoryInfoVO tempCategoryVO = new CategoryInfoVO();
                    tempCategoryVO.setMainCategoryId(category.getCategoryId());
                    tempCategoryVO.setMainCategoryName(category.getCategoryName());
                    tempCategoryVO.setType(category.getType());
                    categoryInfoVOS.add(tempCategoryVO);
                }
                return Result.success(categoryInfoVOS);
            }catch (ServiceInternalException e){
                log.error("getMainCategoryList ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }


    @GetMapping("/{mainCategoryId}")
    public Result<List<CategoryInfoVO>> getSubCategoryList(@PathVariable String mainCategoryId) {
        log.info("getSubCategoryList mainCategoryId:{}",mainCategoryId);
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                List<CategoryInfoVO> categoryInfoVOS = categoryService.getAllChildCategoryListByPId(mainCategoryId);
                return Result.success(categoryInfoVOS);
            }catch (ServiceInternalException e){
                log.error("getSubCategoryList ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }

    @DeleteMapping("/{categoryId}")
    public Result<String> deleteCategory(@PathVariable String categoryId,@RequestParam(value = "level", required = true) Integer level) {
        log.info("deleteCategory categoryId:{} ,level:{}",categoryId,level);
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                boolean result =  categoryService.deleteCategory(categoryId,level);
                if(result){
                    return Result.success();
                }else{
                    return Result.failure(ResultCode.API_DB_FAIL);
                }
            }catch (ServiceInternalException e){
                log.error("deleteCategory ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }
}
