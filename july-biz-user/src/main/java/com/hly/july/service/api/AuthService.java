package com.hly.july.service.api;

import com.hly.july.common.constant.AuthConstants;
import com.hly.july.common.entity.Payment;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * @author Linyuan Hou
 * @date 2021/5/9 15:48
 */
@FeignClient(value = "july-auth")
public interface AuthService {

//    @Headers({"Content-Type: application/json;charset=utf8","Authorization: "+ AuthConstants.CLIENT_BASIC_SECRET})
//    @PostMapping(value = "/oauth/token" )
//    public Object postAccessToken(@RequestParam(value = "grant_type", required = true) String grant_type,
//                                                             @RequestParam(value = "username", required = true) String username,
//                                                             @RequestParam(value = "password", required = true) String password);
//
//    @Headers({"Content-Type: application/json;charset=utf8","Authorization: "+ AuthConstants.CLIENT_BASIC_SECRET})
    @RequestMapping(value = "/oauth/token", method=RequestMethod.POST,headers ={"Content-Type=application/json;charset=utf8","Authorization="+ AuthConstants.CLIENT_BASIC_SECRET} )
    public ResponseEntity<OAuth2AccessToken> postAccessToken(@RequestParam
            Map<String, String> parameters);
}
