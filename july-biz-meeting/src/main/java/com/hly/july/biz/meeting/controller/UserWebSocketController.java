package com.hly.july.biz.meeting.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hly.july.biz.meeting.entity.Ack;
import com.hly.july.biz.meeting.service.impl.UserService;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

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

}
