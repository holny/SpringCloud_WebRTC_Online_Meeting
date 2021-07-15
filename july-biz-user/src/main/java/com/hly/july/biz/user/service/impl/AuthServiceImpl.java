package com.hly.july.biz.user.service.impl;

import com.hly.july.biz.user.service.IAuthService;
import com.hly.july.biz.user.service.api.AuthApiService;
import com.hly.july.common.core.exception.BizException;
import com.hly.july.common.core.result.ResultCode;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AuthServiceImpl
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/5/13 20:56
 * @Version 1.0.0
 **/
@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {
    @Resource
    private AuthApiService authApiService;

    public Map<String,Object> getTokenByAccount(String account, String password) throws BizException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("grant_type","password");
        parameters.put("username",account);
        parameters.put("password",password);
        Map<String,Object> token = null;
        try {
            ResponseEntity<Object> tokenResponseEntity = authApiService.postAccessToken(parameters);
            Object body = tokenResponseEntity.getBody();
            log.info("getTokenByAccount token:"+body.toString()+" type:"+body.getClass().toString());
            if (body!=null){
                if(body instanceof OAuth2AccessToken){
                    token = (Map<String,Object>)body;
                    log.info("getTokenByAccount token:"+token.toString());
                    return token;
                }else if(body instanceof OAuth2Exception){
                    OAuth2Exception exception = (OAuth2Exception)body;
                    log.error("OAuth2Exception account:"+account+" password:"+password+" ,exception:"+exception.getMessage());
                    throw new BizException(ResultCode.TOKEN_FAIL,exception.getMessage());
                }else if(body.toString().contains("error_description")){
                    String errorStr= body.toString().split("error_description")[1].split("}")[0].replace("\"","").replace(":","").replace("=","");
                    log.error("Exception splited by error_description,account:"+account+" password:"+password+" ,e"+errorStr);
                    throw new BizException(ResultCode.TOKEN_FAIL,errorStr);
                }else{
                    token = (Map<String,Object>)body;
                    log.info("getTokenByAccount not OAuth2AccessToken token:"+token.toString());
                }
            }else{
                log.error("response body is null, account:"+account+" password:"+password);
                throw new BizException(ResultCode.TOKEN_FAIL);
            }
        }catch (FeignException e){
            String errorStr= e.getMessage();
            log.info("getTokenByAccount FeignException1 account:"+account+" password:"+password +"error:"+errorStr);
            if(errorStr.contains("error_description")){
                errorStr = errorStr.split("error_description")[1].split("}")[0].replace("\"","").replace(":","");
            }
            log.error("getTokenByAccount FeignException2 account:"+account+" password:"+password +"error:"+errorStr);
            throw new BizException(ResultCode.TOKEN_FAIL,errorStr);
        }
        return token;
    }
}
