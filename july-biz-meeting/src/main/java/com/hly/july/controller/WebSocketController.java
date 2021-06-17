package com.hly.july.controller;

import com.hly.july.common.biz.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RestController;

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

    @MessageMapping({"/heartbeat"})
    @SendToUser({"/topic/heartbeat"})
    public Result<String> subscribeHeartBeat(String msg, OAuth2Authentication auth2Authentication) {
        log.info("subscribeHeartBeat "+msg);

        log.info("subscribeHeartBeat host");
        return Result.success("hello!");
    }



}
