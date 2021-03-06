package com.hly.july.biz.meeting.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hly.july.biz.meeting.entity.Ack;
import com.hly.july.biz.meeting.entity.Room;
import com.hly.july.biz.meeting.service.impl.UserService;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName UserWebSocketController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/15 09:56
 * @Version 1.0.0
 **/
@RestController
@Slf4j
public class UserWebSocketController {
    @Autowired
    private UserService userService;


    @MessageMapping({"/room/{roomId}/ack"})
    public Result<String> ackRoom(@DestinationVariable String roomId, Ack ack) {
        log.info("ackRoom roomId:{}, ack:{}",roomId,ack.toString());
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (StringUtils.isNotBlank(hostId)){
            return  userService.updateAck(roomId,hostId, DateUtils.getCurrentDateTime());
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
        }

    }


    @MessageMapping({"/personal/{userId}/ack"})
    public Result<String> ackPersonal(@DestinationVariable String userId, Ack ack) {
        log.info("ackPersonal userId:{}, ack:{}",userId,ack.toString());
        String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (StringUtils.isNotBlank(hostId)){
            return userService.updateAck(userId,hostId,DateUtils.getCurrentDateTime());
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
        }

    }

    @SubscribeMapping({"/unreadcount/{userId}"})
    public Result<Map<String,Object>> subscribeNotify(@DestinationVariable String userId, OAuth2Authentication auth2Authentication) {
//        log.info("subscribeNotify userId:{}",userId);
        if (auth2Authentication!=null){
            String hostId =auth2Authentication.getPrincipal().toString();
//            log.info("subscribeNotify hostId:{}",hostId);
            if (StringUtils.isNotBlank(hostId)&&hostId.equals(userId)){
                Map<String,Object> result =  userService.getNotification(hostId);
                if(ObjectUtils.isNotEmpty(result)){
                    return Result.success(result);
                }else{
                    return Result.failure(ResultCode.WEBSOCKET_SERVER_ERROR);
                }
            }else{
                return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
        }
    }

}
