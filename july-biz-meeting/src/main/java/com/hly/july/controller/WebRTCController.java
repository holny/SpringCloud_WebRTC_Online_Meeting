package com.hly.july.controller;

import com.hly.july.common.biz.exception.ServiceInternalException;
import com.hly.july.common.biz.result.Result;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.common.biz.util.DateUtils;
import com.hly.july.entity.MeetingSession;
import com.hly.july.entity.Watcher;
import com.hly.july.service.impl.MeetingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName WebRTCController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/22 16:36
 * @Version 1.0.0
 **/
@RestController
@Slf4j
public class WebRTCController {
    @Autowired
    private MeetingService meetingService;

    @MessageMapping({"/videoCall/{peerId}"})
    public Result<MeetingSession> videoCallRequest(@DestinationVariable String peerId, Map<String,String> data, OAuth2Authentication auth2Authentication) {
        String hostId = auth2Authentication.getPrincipal().toString();
        String sessionId = null;
        Boolean isAccept = true;
        if(data.containsKey("sessionId")){
            sessionId = data.get("sessionId");
        }
        if(data.containsKey("isAccept")){
            if(data.get("isAccept").equals("false")){
                isAccept = false;
            }
        }
        log.info("videoCallRequest hostId:{}, peerId:{}, sessionId:{}, isAccept:{}",hostId,peerId,sessionId,isAccept);
        if (StringUtils.isNotEmpty(hostId)){
            try {
                MeetingSession meetingSession = meetingService.preMeetingProcess(hostId,peerId,sessionId,isAccept);
                return Result.success(meetingSession);
            }catch (ServiceInternalException e){
                log.error("videoCallRequest  preMeetingProcess resultCode:{},resultMsg:{}",e.getResultCode().getCode(),e.getErrorMsg());
                return Result.failure(e.getResultCode());
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
        }

    }
}
