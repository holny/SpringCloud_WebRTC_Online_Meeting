package com.hly.july.service.api;

import com.hly.july.common.biz.vo.UserInfoVO;
import com.hly.july.common.biz.constant.AuthConstants;
import com.hly.july.common.biz.result.Result;
import com.hly.july.config.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Linyuan Hou
 * @date 2021/5/9 15:48
 */
@FeignClient(value = "user-manage-service", configuration = FeignInterceptor.class)
public interface UserApiService {

//    @Headers({"Content-Type: application/json;charset=utf8","Authorization: "+ AuthConstants.CLIENT_BASIC_SECRET})
//    @PostMapping(value = "/oauth/token" )
//    public Object postAccessToken(@RequestParam(value = "grant_type", required = true) String grant_type,
//                                                             @RequestParam(value = "username", required = true) String username,
//                                                             @RequestParam(value = "password", required = true) String password);
//
//    @Headers({"Content-Type: application/json;charset=utf8","Authorization: "+ AuthConstants.CLIENT_BASIC_SECRET})
    @RequestMapping(value = "/oauth/token", method=RequestMethod.POST,headers ={"Content-Type=application/json;charset=utf8","Authorization="+ AuthConstants.CLIENT_BASIC_SECRET} )
    public ResponseEntity<Object> postAccessToken(@RequestParam
            Map<String, String> parameters);

    @GetMapping(value = "/user/{userId}")
    public Result<UserInfoVO> getUser(@PathVariable String userId);
}
