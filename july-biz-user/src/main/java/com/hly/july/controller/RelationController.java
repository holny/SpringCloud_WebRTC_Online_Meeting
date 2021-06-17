package com.hly.july.controller;

import com.hly.july.common.biz.constant.ContainerEnum;
import com.hly.july.common.biz.constant.UserStatusEnum;
import com.hly.july.common.biz.entity.Relation;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.vo.RelationVO;
import com.hly.july.common.biz.exception.ServiceInternalException;
import com.hly.july.common.biz.result.Result;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.common.biz.util.DateUtils;
import com.hly.july.entity.RecentVO;
import com.hly.july.service.impl.RelationServiceImpl;
import com.hly.july.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName RelationController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/12 14:55
 * @Version 1.0.0
 **/
@RestController
@RequestMapping("/relation")
@Slf4j
public class RelationController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RelationServiceImpl relationService;

    @GetMapping(value = "/{userId}")
    public Result<List<RelationVO>> getUserRelation(@PathVariable String userId, @RequestParam(value = "peerId", required = false) String peerId, @RequestParam(value = "category", required = false) String category){
        log.info("getUserRelation userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        if (!UserStatusEnum.getVisibleUserStatusCodeList().contains(user.getStatus())){
            return Result.failure(ResultCode.AUTH_ACCOUNT_INVALID);
        }
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
        if(!hostId.equals(user.getUserId())){
            return Result.failure(ResultCode.AUTH_UNAUTHORIZED);
        }
        try {
            List<RelationVO> relationVOList = relationService.getUserRelation(userId, peerId, category);
            return Result.success(relationVOList);
        }catch (ServiceInternalException e){
            return Result.failure(e.getResultCode(),e.getErrorMsg());
        }
    }

    @PostMapping(value = "/{userId}")
    public Result<List<RelationVO>> upInsertUserRelation(@PathVariable String userId, @RequestParam(value = "category", required = false) String category, @RequestBody Relation addRelation){
        log.info("upInsertUserRelation userId:{},category:{},addRelation:{}",userId,category,addRelation.toString());
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        if (!UserStatusEnum.getVisibleUserStatusCodeList().contains(user.getStatus())){
            return Result.failure(ResultCode.AUTH_ACCOUNT_INVALID);
        }
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
        if(!hostId.equals(user.getUserId())){
            return Result.failure(ResultCode.AUTH_UNAUTHORIZED);
        }
        try {
            addRelation.setUserId(userId);
            if(StringUtils.isNotEmpty(addRelation.getPeerType())){
                if(category!=null&&(category.equals("recent")||category.equals("bookmark"))){
                    if(!addRelation.getPeerType().equals(ContainerEnum.PERSON.getDesc())) {
                        return Result.failure(ResultCode.API_VALIDATION_ERROR,"参数中PeerType与category不对应");
                    }
                }else if(category!=null&&category.equals("group")){
                    if(!addRelation.getPeerType().equals(ContainerEnum.GROUP.getDesc())) {
                        return Result.failure(ResultCode.API_VALIDATION_ERROR,"参数中PeerType与category不对应");
                    }
                }
                Integer peerTypeCode = ContainerEnum.getCodeByDesc(addRelation.getPeerType());
                if(peerTypeCode!=null){
                    addRelation.setPeerType(peerTypeCode.toString());
                }
            }
            if(StringUtils.isEmpty(addRelation.getPeerType())) {
                if (category != null && (category.equals("recent") || category.equals("bookmark"))) {
                    addRelation.setPeerType(ContainerEnum.PERSON.getCode().toString());
                } else if (category != null && category.equals("group")) {
                    addRelation.setPeerType(ContainerEnum.GROUP.getCode().toString());
                } else {
                    return Result.failure(ResultCode.API_VALIDATION_ERROR);
                }
            }

            int result = relationService.upInsertRelationByUserIdAndPeerIdAndCategory(addRelation);
            if(result>=0){
                List<RelationVO> relationVOList = relationService.getUserRelation(userId, null, category);
                return Result.success(relationVOList);
            }else{
                return Result.failure(ResultCode.API_DB_FAIL);
            }
        }catch (ServiceInternalException e){
            return Result.failure(e.getResultCode(),e.getErrorMsg());
        }
    }

    @PostMapping(value = "/{userId}/recent")
    public Result<List<RelationVO>> upInsertRecentContact(@PathVariable String userId, @RequestBody RecentVO recentVO){
        log.info("upInsertRecentContact userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        if (!UserStatusEnum.getVisibleUserStatusCodeList().contains(user.getStatus())){
            return Result.failure(ResultCode.AUTH_ACCOUNT_INVALID);
        }
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
        if(!hostId.equals(user.getUserId())){
            return Result.failure(ResultCode.AUTH_UNAUTHORIZED);
        }
        try {
            Integer peerTypeCode = ContainerEnum.getCodeByDesc(recentVO.getPeerType());
            if(peerTypeCode!=null){
                recentVO.setPeerType(peerTypeCode.toString());
            }else{
                return Result.failure(ResultCode.API_VALIDATION_ERROR);
            }
            recentVO.setGmtLastContact(DateUtils.getCurrentDateTime());
            relationService.upInsertRecentList(recentVO);
            List<RelationVO> relationVOS  = relationService.getUserRecentContactRelationVO(userId);
            log.info("upInsertRecentContact  userId:{},recentVOs:{}",userId,relationVOS.toString());
            if(relationVOS!=null){
                return Result.success(relationVOS);
            } else{
                return Result.failure(ResultCode.API_DB_FAIL);
            }
        }catch (ServiceInternalException e){
            return Result.failure(e.getResultCode(),e.getErrorMsg());
        }
    }

    @DeleteMapping(value = "/{userId}/recent")
    public Result<List<RelationVO>> removeUserRecentRelation(@PathVariable String userId, @RequestBody RecentVO recentVO){
        log.info("removeUserRecentRelation userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        if (!UserStatusEnum.getVisibleUserStatusCodeList().contains(user.getStatus())){
            return Result.failure(ResultCode.AUTH_ACCOUNT_INVALID);
        }
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
        if(!hostId.equals(user.getUserId())){
            return Result.failure(ResultCode.AUTH_UNAUTHORIZED);
        }
        try {
            Integer peerTypeCode = ContainerEnum.getCodeByDesc(recentVO.getPeerType());
            if(peerTypeCode!=null){
                recentVO.setPeerType(peerTypeCode.toString());
            }else{
                return Result.failure(ResultCode.API_VALIDATION_ERROR);
            }
            relationService.deleteUserRecentRelation(recentVO);
            List<RelationVO> relationVOS  = relationService.getUserRecentContactRelationVO(recentVO.getUserId());
            log.info("upInsertRecentContact  userId:{},recentVOs:{}",userId,relationVOS.toString());
            if(relationVOS!=null){
                return Result.success(relationVOS);
            } else{
                return Result.failure(ResultCode.API_DB_FAIL);
            }
        }catch (ServiceInternalException e){
            return Result.failure(e.getResultCode(),e.getErrorMsg());
        }
    }

//    @PutMapping(value = "/{userId}")
//    public Result<String> updateUserRelation(@PathVariable String userId,  @RequestParam(value = "category", required = false) String category,@RequestBody Relation updateRelation){
//        log.info("updateUserRelation updateRelation:"+updateRelation);
//        User user = userService.getById(userId);
//        if (user ==null){
//            return Result.failure(ResultCode.API_DB_FAIL);
//        }
//        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//        log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
//        if(!hostId.equals(user.getUserId())){
//            return Result.failure(ResultCode.AUTH_UNAUTHORIZED);
//        }
//        try {
//            updateRelation.setUserId(userId);
//            int result = RelationService.upInsertRelationByUserIdAndPeerIdAndCategory(updateRelation,category);
//            if(result>=0){
//                return Result.success();
//            }else{
//                return Result.failure(ResultCode.API_DB_FAIL);
//            }
//        }catch (ServiceInternalException e){
//            return Result.failure(e.getResultCode(),e.getErrorMsg());
//        }
//    }

    @DeleteMapping(value = "/{userId}")
    public Result<List<RelationVO>> removeUserRelation(@PathVariable String userId, @RequestParam(value = "peerType", required = true) String peerType, @RequestParam(value = "peerId", required = true) String peerId){
        log.info("removeUserRelation userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        if (!UserStatusEnum.getVisibleUserStatusCodeList().contains(user.getStatus())){
            return Result.failure(ResultCode.AUTH_ACCOUNT_INVALID);
        }
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
        if(!hostId.equals(user.getUserId())){
            return Result.failure(ResultCode.AUTH_UNAUTHORIZED);
        }
        Integer peerTypeCode = ContainerEnum.getCodeByDesc(peerType);
        if(peerTypeCode==null){
            return Result.failure(ResultCode.API_VALIDATION_ERROR);
        }
        Boolean result = relationService.deleteUserRelationByIdAndType(userId,peerId,peerTypeCode.toString());
        if(result){
            try {
                List<RelationVO> relationVOList = relationService.getUserRelation(userId, null, "bookmark");
                return Result.success(relationVOList);
            }catch (ServiceInternalException e){
                return Result.success("删除成功,但获取更新后列表失败");
            }

        }else{
            return Result.failure(ResultCode.API_DB_FAIL);
        }
    }
}
