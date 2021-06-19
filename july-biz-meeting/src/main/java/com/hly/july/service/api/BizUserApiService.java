package com.hly.july.service.api;

import com.hly.july.common.biz.config.FeignInterceptor;
import com.hly.july.common.biz.vo.RecentVO;
import com.hly.july.common.biz.vo.RelationVO;
import com.hly.july.common.biz.vo.UserInfoVO;
import com.hly.july.common.biz.constant.AuthConstants;
import com.hly.july.common.biz.result.Result;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Linyuan Hou
 * @date 2021/5/9 15:48
 */
@FeignClient(value = "user-manage-service", configuration = FeignInterceptor.class)
public interface BizUserApiService {

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

    @GetMapping(value = "/private/relation/{userId}")
    public Result<List<RelationVO>> getUserRelation(@PathVariable String userId, @RequestParam(value = "peerId", required = false) String peerId, @RequestParam(value = "peerType", required = false) Integer peerType, @RequestParam(value = "relType", required = false) List<Integer> relType);

    @GetMapping(value = "/private/user/{userId}")
    public Result<UserInfoVO> getUser(@PathVariable String userId);

    @PostMapping(value = "/private/relation/{userId}/recent")
    public Result<List<RelationVO>> upInsertRecentContact(@PathVariable String userId, @RequestBody RecentVO recentVO);

    @GetMapping(value = "/private/relation/{userId}/recent")
    public Result<List<RelationVO>> getUserRecentRelation(@PathVariable String userId);
}
