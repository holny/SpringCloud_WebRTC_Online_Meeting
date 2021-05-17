package com.hly.july.controller;

import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hly.july.common.biz.vo.AuthUserVO;
import com.hly.july.common.biz.vo.UserInfoVO;
import com.hly.july.common.constant.JulyConstants;
import com.hly.july.common.constant.RoleEnum;
import com.hly.july.common.entity.LoginUser;
import com.hly.july.common.exception.BizException;
import com.hly.july.common.result.Result;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.result.ResultCode;
import com.hly.july.common.util.DateUtils;
import com.hly.july.common.util.EncryptUtils;
import com.hly.july.common.util.JulyAuthorityUtils;
import com.hly.july.common.util.WrappedBeanCopier;
import com.hly.july.common.validation.group.LoginValidationGroup;
import com.hly.july.common.validation.group.RegisterValidationGroup;
import com.hly.july.service.api.AuthApiService;
import com.hly.july.service.impl.AuthServiceImpl;
import com.hly.july.service.impl.UserServiceImpl;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.util.*;

/**
 * @author Linyuan Hou
 * @date 2021/4/24 15:33
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private AuthServiceImpl authService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/login")
    public Result<Map<String,Object>> login(@Validated(LoginValidationGroup.class) @RequestBody AuthUserVO authUserVO){
        log.info("Login User:"+authUserVO.toString());
        try {
            Map<String,Object> token = authService.getTokenByAccount(authUserVO.getEmail(),authUserVO.getPassword());
            if(token!=null){
                return Result.success(token);
            }else{
                log.error("Token is null, account:"+authUserVO.getEmail()+" password:"+authUserVO.getPassword());
                return Result.failure(ResultCode.TOKEN_FAIL);
            }
        }catch (BizException e){
            log.error("Login BizException error:"+e.toString()+", account:"+authUserVO.getEmail()+" password:"+authUserVO.getPassword());
            return Result.failure(e.getResultCode(),e.getErrorMsg());
        }catch (Exception e){
            log.error("Login Exception error:"+e.toString()+", account:"+authUserVO.getEmail()+" password:"+authUserVO.getPassword());
            return Result.failure(ResultCode.TOKEN_FAIL);
        }
    }

    @PostMapping(value = "/register")
    public Result<UserInfoVO> register(@Validated(RegisterValidationGroup.class)@RequestBody AuthUserVO authUserVO){
        log.info("Register User:"+authUserVO.toString());
        Long userId= IdWorker.getId();
        User newUser = WrappedBeanCopier.copyProperties(authUserVO, User.class);
        newUser.setUserId(userId);
        String role = RoleEnum.ROLE_USER.getCode();
        Set<String> authoritySet = JulyAuthorityUtils.getDefaultAuthorityByRole(role);
        if (StringUtils.isNotBlank(authUserVO.getRegisterCode())) {
            role = JulyAuthorityUtils.getRoleByRegisterCode(authUserVO.getRegisterCode().trim());
            if (StringUtils.isNotBlank(role)){
                authoritySet = JulyAuthorityUtils.getDefaultAuthorityByRole(role);
            }else{
                role = RoleEnum.ROLE_USER.getCode();
            }
        }
        newUser.setRole(role);
        newUser.setAuthority(JulyAuthorityUtils.authorityConcatSet2String(authoritySet));
        if(StringUtils.isBlank(newUser.getPassword())){
            return Result.failure(ResultCode.API_VALIDATION_ERROR,"password不符合规范");
        }
        Date now = DateUtils.getCurrentDateTime();
        // Encrypt password
        String encryptPassword = EncryptUtils.PasswordEncryptByBCrypt(newUser.getPassword());
        newUser.setPassword(encryptPassword);
        newUser.setGender(JulyConstants.DEFAULT_USER_GENDER);
        newUser.setAvatar(JulyConstants.DEFAULT_USER_AVATAR);
        newUser.setStatus(JulyConstants.DEFAULT_USER_STATUS);
        newUser.setGmtBirthday(now);
        newUser.setGmtCreate(now);
        newUser.setGmtUpdate(now);
        log.info("Start save new register:"+newUser.toString());
        if (userService.save(newUser)){
            Map<String,Object> token = null;
            try {
                token = authService.getTokenByAccount(authUserVO.getEmail(),authUserVO.getPassword());
                if(token==null){
                    log.error("Register ,Token is null, account:"+authUserVO.getEmail()+" password:"+authUserVO.getPassword());
                    return Result.failure(ResultCode.TOKEN_FAIL);
                }
            }catch (BizException e){
                log.error("Register BizException error:"+e.toString()+", account:"+authUserVO.getEmail()+" password:"+authUserVO.getPassword());
                return Result.failure(e.getResultCode(),e.getErrorMsg());
            }catch (Exception e){
                log.error("Register Exception error:"+e.toString()+", account:"+authUserVO.getEmail()+" password:"+authUserVO.getPassword());
                return Result.failure(ResultCode.TOKEN_FAIL,e.getMessage());
            }
            UserInfoVO userInfoVO = new UserInfoVO(newUser,token);
            return Result.success(userInfoVO);
        }else{
            return Result.failure(ResultCode.API_DB_FAIL,"保存新用户数据失败");
        }
    }

    @PostMapping(value = "/validate")
    public Result<Object> validate(@RequestBody AuthUserVO authUserVO){
        log.info("Validate User:"+authUserVO.toString());
        User user = WrappedBeanCopier.copyProperties(authUserVO, User.class);
        if (userService.isDuplicateUser(user)){
            return Result.failure(ResultCode.API_DUPLICATE_DATA);
        }
        return Result.success();
    }

    @GetMapping(value = "/{userId}")
    public Result<UserInfoVO> getUser(@PathVariable String userId, HttpSession session){
        log.info("get a userId:"+userId);
        User user = userService.getById(userId);
        if (user ==null){
            return Result.failure(ResultCode.API_DB_FAIL);
        }
        String host = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserInfoVO loginUser = null;
        if(StringUtils.isNotBlank(host)){
            User hostUser = userService.getUserByAccount(host);
            if(hostUser!=null) {
                loginUser = new UserInfoVO(hostUser);
            }
        }
        log.info("loginUser:"+loginUser.toString());

        if(loginUser == null){
            log.warn("getUser-host is Null, get brief UserId:"+userId);
            UserInfoVO userInfoVO = new UserInfoVO(user);
            userInfoVO.setPhoneNumber(null);
            userInfoVO.setEmail(null);
            userInfoVO.setGmtCreate(null);
            userInfoVO.setGmtBirthday(null);
            return Result.success("get success",userInfoVO);
        }else {
            log.info("loginUser:"+loginUser.toString());
            String hostUserId = loginUser.getUserId();
            log.info("hostUserId:"+hostUserId);
            if(user.getUserId().equals(hostUserId)) {
                log.info("host get himself Info Successful, user:" + user.toString());
                UserInfoVO userInfoVO = new UserInfoVO(user);
                return Result.success("get success", userInfoVO);
            } else {
                log.info("host get not himself Info Successful, hostId:" + hostUserId+" , wanna get user:"+user.toString());
                UserInfoVO userInfoVO = new UserInfoVO(user);
                return Result.success("get success", userInfoVO);
            }
        }

    }

}
