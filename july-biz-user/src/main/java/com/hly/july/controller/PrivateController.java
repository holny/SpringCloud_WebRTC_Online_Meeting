package com.hly.july.controller;

import com.hly.july.common.biz.constant.ContainerEnum;
import com.hly.july.common.biz.constant.UserStatusEnum;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.exception.ServiceInternalException;
import com.hly.july.common.biz.result.Result;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.common.biz.util.DateUtils;
import com.hly.july.common.biz.vo.RecentVO;
import com.hly.july.common.biz.vo.RelationVO;
import com.hly.july.common.biz.vo.UserInfoVO;
import com.hly.july.service.impl.RelationServiceImpl;
import com.hly.july.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ClassName InternalController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/18 11:34
 * @Version 1.0.0
 **/
@RestController
@RequestMapping("/private")
@Slf4j
public class PrivateController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RelationServiceImpl relationService;

    @GetMapping(value = "/relation/{userId}")
    public Result<List<RelationVO>> getUserRelation(@PathVariable String userId, @RequestParam(value = "peerId", required = false) String peerId, @RequestParam(value = "peerType", required = false) Integer peerType, @RequestParam(value = "relTypeList", required = false) List<Integer> relTypeList){
        log.info("getUserRelation userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }

        try {
            if(relTypeList!=null&&relTypeList.size()==0){
                relTypeList = null;
            }
            List<RelationVO> relationVOList = relationService.getUserRelationByIdAndType(userId, peerId, peerType, relTypeList);
            return Result.success(relationVOList);
        }catch (ServiceInternalException e){
            return Result.failure(e.getResultCode(),e.getErrorMsg());
        }
    }

    @GetMapping(value = "/user/{userId}")
    public Result<UserInfoVO> getUser(@PathVariable String userId){
        log.info("get a userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        UserInfoVO userInfoVO = new UserInfoVO(user);
        return Result.success("get success", userInfoVO);
    }

    @PostMapping(value = "/relation/{userId}/recent")
    public Result<List<RelationVO>> upInsertRecentContact(@PathVariable String userId, @RequestBody RecentVO recentVO){
        log.info("upInsertRecentContact userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        try {
            Integer peerTypeCode = ContainerEnum.getCodeByDesc(recentVO.getPeerType());
            if(peerTypeCode!=null){
                recentVO.setPeerType(peerTypeCode.toString());
            }else{
                return Result.failure(ResultCode.API_VALIDATION_ERROR);
            }
            if(recentVO.getGmtLastContact()==null){
                recentVO.setGmtLastContact(DateUtils.getCurrentDateTime());
            }
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

    @GetMapping(value = "/relation/{userId}/recent")
    public Result<List<RelationVO>> getUserRecentRelation(@PathVariable String userId){
        log.info("getUserRecentRelation userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        List<RelationVO> relationVOList = relationService.getUserRecentContactRelationVO(userId);
        if(relationVOList!=null){
            return Result.success(relationVOList);
        }else{
            return Result.failure(ResultCode.API_VALIDATION_ERROR);
        }
    }
}
