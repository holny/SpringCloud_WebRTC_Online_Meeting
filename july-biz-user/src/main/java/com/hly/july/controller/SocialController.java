package com.hly.july.controller;

import com.hly.july.common.biz.entity.Social;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.vo.SocialVO;
import com.hly.july.common.biz.vo.UserInfoVO;
import com.hly.july.common.exception.ServiceInternalException;
import com.hly.july.common.result.Result;
import com.hly.july.common.result.ResultCode;
import com.hly.july.service.impl.SocialServiceImpl;
import com.hly.july.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SocialController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/12 14:55
 * @Version 1.0.0
 **/
@RestController
@RequestMapping("/social")
@Slf4j
public class SocialController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SocialServiceImpl socialService;

    @GetMapping(value = "/{userId}")
    public Result<List<SocialVO>> getUserSocial(@PathVariable String userId, @RequestParam(value = "type", required = false) String type){
        log.info("getUserSocial userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
        if(!hostId.equals(user.getUserId())){
            return Result.failure(ResultCode.AUTH_UNAUTHORIZED);
        }
        List<SocialVO> socialVOList = socialService.getUserSocialVOByUserIdAndType(userId,type);
        return Result.success(socialVOList);
    }

    @PostMapping(value = "/{userId}")
    public Result<String> addUserSocial(@PathVariable String userId, @RequestParam(value = "type", required = true) String type, @RequestParam(value = "peerId", required = true) String peerId){
        log.info("addUserSocial userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
        if(!hostId.equals(user.getUserId())){
            return Result.failure(ResultCode.AUTH_UNAUTHORIZED);
        }
        try {
            Boolean result = socialService.addUserSocialByIdAndType(userId,peerId,type);
            if(result){
                return Result.success();
            }else{
                return Result.failure(ResultCode.API_DB_FAIL);
            }
        }catch (ServiceInternalException e){
            return Result.failure(e.getResultCode(),e.getErrorMsg());
        }
    }

    @DeleteMapping(value = "/{userId}")
    public Result<String> removeUserSocial(@PathVariable String userId, @RequestParam(value = "type", required = true) String type, @RequestParam(value = "peerId", required = true) String peerId){
        log.info("removeUserSocial userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
        if(!hostId.equals(user.getUserId())){
            return Result.failure(ResultCode.AUTH_UNAUTHORIZED);
        }
        Boolean result = socialService.deleteUserSocialByIdAndType(userId,peerId,type);
        if(result){
            return Result.success();
        }else{
            return Result.failure(ResultCode.API_DB_FAIL);
        }
    }
}
