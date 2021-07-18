package com.hly.july.biz.video.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hly.july.biz.video.service.impl.TagServiceImpl;
import com.hly.july.common.core.constant.ContainerStatusEnum;
import com.hly.july.common.core.constant.TagTypeEnum;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.WrappedBeanCopier;
import com.hly.july.common.core.validation.group.NewTagGroup;
import com.hly.july.common.db.entity.Tag;
import com.hly.july.common.web.vo.TagInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TagController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/17 16:34
 * @Version 1.0.0
 **/
@RestController
@RequestMapping("/tag")
@Slf4j
public class TagController {
    @Autowired
    private TagServiceImpl tagService;

    @PostMapping("/")
    public Result<TagInfoVO> postNewTag(@Validated(NewTagGroup.class)@RequestBody TagInfoVO tagInfoVO){
        log.info("postNewTag tagInfoVO:"+tagInfoVO.toString());
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                tagInfoVO.setTagAuthorId(hostId);
                Tag tag = tagService.initNewTag(tagInfoVO);
                return Result.success(WrappedBeanCopier.copyProperties(tag,TagInfoVO.class));
            }catch (ServiceInternalException e){
                log.error("postNewVideo ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }

    @GetMapping("/")
    public Result<List<TagInfoVO>> getTagList( @RequestParam(value = "type", required = false) String type) {
        log.info("getTagList");
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                List<Integer> tagTypeList= new ArrayList<>();;
                if(StringUtils.isNotEmpty(type)){
                    Integer code = TagTypeEnum.getCodeByDesc(type.toLowerCase());
                    if(code!=null){
                        tagTypeList.add(code);
                    }
                }
                if(CollectionUtils.isNotEmpty(tagTypeList)){
                    tagTypeList=TagTypeEnum.getAllTypeCodeList();
                }
                List<Tag> tagList = tagService.getTagByStatusAndType(ContainerStatusEnum.getVisibleStatusCodeList(),tagTypeList);
                List<TagInfoVO> tagInfoVOS =  WrappedBeanCopier.copyPropertiesOfList(tagList,TagInfoVO.class);
                for (TagInfoVO tagInfoVO : tagInfoVOS) {
                    tagInfoVO.setTypeStr(TagTypeEnum.getDescByCode(tagInfoVO.getType()));
                }
                return Result.success(tagInfoVOS);
            }catch (ServiceInternalException e){
                log.error("getTagList ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }

    @GetMapping("/{tagId}")
    public Result<TagInfoVO> getTag(@PathVariable String tagId) {
        log.info("getTag tagId:{}",tagId);
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            Tag tag = tagService.getById(tagId);
            return Result.success(WrappedBeanCopier.copyProperties(tag,TagInfoVO.class));
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }

    @DeleteMapping("/{tagId}")
    public Result<String> deleteTag(@PathVariable String tagId) {
        log.info("deleteTag tagId:{}",tagId);
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (StringUtils.isNotEmpty(hostId)) {
            try {
                boolean result = tagService.deleteTag(tagId);
                if(result){
                    return Result.success();
                }else{
                    return Result.failure(ResultCode.API_DB_FAIL);
                }
            }catch (ServiceInternalException e){
                log.error("deleteTag ServiceInternalException resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN);
        }
    }
}
