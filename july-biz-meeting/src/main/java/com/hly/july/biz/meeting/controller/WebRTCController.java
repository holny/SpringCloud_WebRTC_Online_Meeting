package com.hly.july.biz.meeting.controller;

import com.hly.july.biz.meeting.entity.MeetingSession;
import com.hly.july.biz.meeting.service.impl.MeetingService;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
    public Result<MeetingSession> videoCallRequest(@DestinationVariable String peerId, Map<String,Object> data, OAuth2Authentication auth2Authentication) {
        String hostId = auth2Authentication.getPrincipal().toString();
        String sessionId = null;
        Boolean isAccept = true;
        Map<String,Object> signalingMap = new HashMap<>();

        if(data.containsKey("sessionId")){
            sessionId = (String)data.get("sessionId");
        }
        if(data.containsKey("isAccept")){
            if(data.get("isAccept").equals("false")){
                isAccept = false;
            }
        }
        if(data.containsKey("signalingMap")){
            signalingMap = (HashMap<String,Object>)data.get("signalingMap");
        }
        log.info("videoCallRequest hostId:{}, peerId:{}, sessionId:{}, isAccept:{}, signalingMap:{}",hostId,peerId,sessionId,isAccept,signalingMap.keySet().toString());
        if (StringUtils.isNotEmpty(hostId)){
            try {
                MeetingSession meetingSession = meetingService.preMeetingProcess(hostId,peerId,sessionId,isAccept,signalingMap);
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
