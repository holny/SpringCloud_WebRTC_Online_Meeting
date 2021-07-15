package com.hly.july.biz.user.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hly.july.biz.user.service.impl.AuthServiceImpl;
import com.hly.july.biz.user.service.impl.UserServiceImpl;
import com.hly.july.common.auth.util.EncryptUtils;
import com.hly.july.common.core.constant.JulyConstants;
import com.hly.july.common.core.constant.RoleEnum;
import com.hly.july.common.core.constant.UserStatusEnum;
import com.hly.july.common.core.exception.BizException;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.DateUtils;
import com.hly.july.common.core.util.JulyAuthorityUtils;
import com.hly.july.common.core.util.WrappedBeanCopier;
import com.hly.july.common.core.validation.group.LoginValidationGroup;
import com.hly.july.common.core.validation.group.RegisterValidationGroup;
import com.hly.july.common.db.entity.User;
import com.hly.july.common.web.util.GetRequestInfo;
import com.hly.july.common.web.vo.AuthUserVO;
import com.hly.july.common.web.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public Result<Map<String,Object>> login(HttpServletRequest request, @Validated(LoginValidationGroup.class) @RequestBody AuthUserVO authUserVO, HttpSession session){
        log.info("Login User:"+authUserVO.toString());
        try {
            String account;
            if(JulyConstants.DEFAULT_LOGIN_BY_ACCOUNT.equals("phoneNumber")){
                account = authUserVO.getPassword();
            }else{
                account = authUserVO.getEmail();
            }
            User loginUser = userService.getUserByAccount(account);
            if (loginUser!=null){
                // 这里先判断此用户存在与否，存在才去鉴权服务器取token，这里需要看情况，如果鉴权服务器有用户信息，但本地没有用户信息(也就是第一次登陆)，Todo 应该是引导用户先去获得token，再在本地保存用户信息
                Map<String,Object> token = authService.getTokenByAccount(account,authUserVO.getPassword());
                if(token!=null){
                    String ip = GetRequestInfo.getIpAddress(request);
                    User updateUser = new User();
                    if(StringUtils.isNotEmpty(ip)){
                        updateUser.setLastLoginIp(ip);
                        loginUser.setLastLoginIp(ip);
                    }
                    updateUser.setGmtLastLogin(DateUtils.getCurrentDateTime());
                    loginUser.setGmtLastLogin(DateUtils.getCurrentDateTime());
                    userService.updateUserByUserId(loginUser.getUserId(),updateUser);
                    UserInfoVO userInfoVO = new UserInfoVO(loginUser);
                    token.put("userInfo",userInfoVO);
                    return Result.success(token);
                }else{
                    log.error("Token is null, account:"+authUserVO.getEmail()+" password:"+authUserVO.getPassword());
                    return Result.failure(ResultCode.TOKEN_FAIL);
                }
            }else{
                log.error("Account not exist in DB, account:"+authUserVO.getEmail()+" password:"+authUserVO.getPassword());
                return Result.failure(ResultCode.AUTH_ACCOUNT_INVALID);
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
        newUser.setUserId(userId.toString());
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

    /**
     * 如果已经存在用户，返回fail，不存在用户则返回success
     * @param authUserVO
     * @return
     */
    @PostMapping(value = "/duplicate")
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
        if (!UserStatusEnum.getVisibleUserStatusCodeList().contains(user.getStatus())){
            return Result.failure(ResultCode.AUTH_ACCOUNT_INVALID);
        }
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
        UserInfoVO loginUser = null;
        if(StringUtils.isNotBlank(hostId)){
            User hostUser = userService.getById(hostId);
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

    @GetMapping(value = "/search")
    public Result<List<UserInfoVO>> getUserBySearch(@RequestParam(value = "search", required = false) String search){
        log.info("getUserBySearch , getUserBySearch:"+search);
        if (StringUtils.isNotEmpty(search)) {
            String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            log.info("Authentication:"+SecurityContextHolder.getContext().getAuthentication().toString());
            search = search.trim();
            List<Integer> visibleUserStatusList = UserStatusEnum.getAllUserStatusCodeList();
            List<User> userList = userService.getUserBySearch(search, 10, visibleUserStatusList);
            log.info("getUserBySearch , userList:{}",userList.toString());
            List<UserInfoVO> userInfoVOList = new ArrayList<>();

            User hoster = null;
            for (User user : userList) {
                UserInfoVO userInfoVO = new UserInfoVO(user);
                userInfoVOList.add(userInfoVO);
                if(hostId!=null&&user.getUserId().equals(hostId)){
                    hoster=user;
                }
            }
            if(hoster!=null){
                // 删除hoster自己
                userList.remove(hoster);
            }
            if (CollectionUtils.isNotEmpty(userInfoVOList)) {
                return Result.success(userInfoVOList);
            } else {
                return Result.success("未搜索到任何结果");
            }
        }else{
            return Result.failure(ResultCode.API_VALIDATION_ERROR,"搜索条件为空");
        }
    }

}
