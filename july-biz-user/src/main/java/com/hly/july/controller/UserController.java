package com.hly.july.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hly.july.common.biz.vo.AuthUserVO;
import com.hly.july.common.biz.vo.UserInfoVO;
import com.hly.july.common.constant.JulyConstants;
import com.hly.july.common.constant.RoleEnum;
import com.hly.july.common.entity.LoginUser;
import com.hly.july.common.result.Result;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.service.IUserService;
import com.hly.july.common.result.ResultCode;
import com.hly.july.common.util.DateUtils;
import com.hly.july.common.util.EncryptUtils;
import com.hly.july.common.util.JulyAuthorityUtils;
import com.hly.july.common.util.WrappedBeanCopier;
import com.hly.july.common.validation.group.LoginValidationGroup;
import com.hly.july.common.validation.group.RegisterValidationGroup;
import com.hly.july.service.api.AuthService;
import com.hly.july.service.impl.UserServiceImpl;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Linyuan Hou
 * @date 2021/4/24 15:33
 */
@RestController
@RequestMapping("/user")
@RestControllerAdvice
@Slf4j
public class UserController {
    @Resource
    private AuthService authService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/login")
    public Result<Object> login(@Validated(LoginValidationGroup.class) @RequestBody AuthUserVO authUserVO){
        log.info("Login User:"+authUserVO.toString());
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("grant_type","password");
        parameters.put("username",authUserVO.getEmail());
        parameters.put("password",authUserVO.getPassword());
        OAuth2AccessToken token;
        try {
            ResponseEntity<OAuth2AccessToken>  tokenResponseEntity = authService.postAccessToken(parameters);
            token = tokenResponseEntity.getBody();
            log.info("Login token:"+token.toString());
        }catch (FeignException e){
            String errorStr= e.toString();
            if(errorStr.contains("error_description")){
                errorStr = errorStr.split("error_description")[1].split("}")[0].replace("\"","").replace(":","");
            }
            log.error("Login FeignException error:"+errorStr);
            return Result.failure(ResultCode.AUTH_FAIL,errorStr);
        }catch (Exception e){
            log.error("Login Exception error:"+e.toString());
            return Result.failure(ResultCode.TOKEN_FAIL,e.getMessage());
        }
        return Result.success(token);
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
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("grant_type","password");
            parameters.put("username",authUserVO.getEmail());
            parameters.put("password",authUserVO.getPassword());
            OAuth2AccessToken token;
            try {
                ResponseEntity<OAuth2AccessToken>  tokenResponseEntity = authService.postAccessToken(parameters);
                token = tokenResponseEntity.getBody();
                log.info("Register token:"+token.toString());
            }catch (FeignException e){
                String errorStr= e.toString();
                if(errorStr.contains("error_description")){
                    errorStr = errorStr.split("error_description")[1].split("}")[0].replace("\"","").replace(":","");
                }
                log.error("Register FeignException error:"+errorStr);
                return Result.failure(ResultCode.AUTH_FAIL,errorStr);
            }catch (Exception e){
                log.error("Register Exception error:"+e.toString());
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
    public Result<User> getUser(@PathVariable String userId){
        log.info("get a userId:"+userId);
        User user = userService.getById(userId);
        return Result.success("get success",user);
    }

}
