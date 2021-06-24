package com.hly.july.controller;

import com.hly.july.common.biz.result.Result;
import com.hly.july.entity.UserActiveStatusEnum;
import com.hly.july.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName WebSocketController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/15 09:57
 * @Version 1.0.0
 **/
@RestController
@Slf4j
public class WebSocketController {
    @Autowired
    private UserService userService;
    @MessageMapping({"/heartbeat"})
    @SendToUser({"/topic/heartbeat"})
    public Result<String> subscribeHeartBeat(Map<String,String> data, OAuth2Authentication auth2Authentication) {
        Integer status = UserActiveStatusEnum.ACTIVE_ONLY_MESSAGE.getCode();
        if(data.containsKey("status")){
            status = Integer.parseInt(data.get("status"));
        }
        if (auth2Authentication!=null) {
            String hostId = auth2Authentication.getPrincipal().toString();
            if (StringUtils.isNotEmpty(hostId)){
                userService.updateUserActiveStatus(hostId,status);
            }
        }
        return Result.success("hello!");
    }



}
