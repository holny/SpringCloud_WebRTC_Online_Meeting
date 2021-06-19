package com.hly.july.service.impl;

import com.hly.july.common.biz.exception.ServiceInternalException;
import com.hly.july.common.biz.utils.RedisUtils;
import com.hly.july.common.biz.result.Result;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.entity.Event;
import com.hly.july.entity.Shouting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName UserService
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/15 09:54
 * @Version 1.0.0
 **/
@Service
@Slf4j
public class UserService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public UserService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Resource
    private RedisUtils redisUtils;

    private String REDIS_PREFIX_ROOM_INFO = "meeting_room_info_";
    private String REDIS_PREFIX_ACK = "meeting_room_ack_";

    private String REDIS_USER_INFO = "meeting_room_user_";


    public Result<String> updateAck(String objectId, String userId, Date ackDate){
        if (objectId!=null&&userId!=null&&ackDate!=null) {
            if (redisUtils.hasKey(REDIS_PREFIX_ACK + userId)) {
                try {
                    Map<String,Date> ackMap = (Map<String,Date>) redisUtils.get(REDIS_PREFIX_ACK + userId);
//                    redisUtils.multi();
                    if(ackMap.containsKey(objectId)){
                        Date oldAckDate = ackMap.get(objectId);
                        if(oldAckDate.before(ackDate)){
                            ackMap.put(objectId,ackDate);
                            redisUtils.set(REDIS_PREFIX_ACK + userId,ackMap);
                            log.info("updateAck success, userId{}, objectId:{}, newAckDate",userId,objectId,ackDate);
                        }
                        return Result.success();
                    }else{
                        ackMap.put(objectId,ackDate);
                        redisUtils.set(REDIS_PREFIX_ACK + userId,ackMap);
                        log.info("updateAck success, userId{}, objectId:{}, newAckDate",userId,objectId,ackDate);
                        return Result.success();
                    }
//                    redisUtils.exec();

                } catch (Exception e) {
//                    redisUtils.discard();
                    log.error("updateAck error, userId{}, objectId:{}, newAckDate",userId,objectId,ackDate);
                    e.printStackTrace();
                    return Result.failure(ResultCode.WEBSOCKET_SERVER_ERROR);
                }
            }else{
                return Result.failure(ResultCode.WEBSOCKET_KEY_NOT_EXIST);
            }
        }else {
            return Result.failure(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }




    public Boolean sendPersonalEvent(String userId,Event event){
        log.info("sendPersonalEvent userId:{},event:{}",userId,event.toString());
        event.setType("event");
        sendPersonalShouting(event.getTo(),event);
        return true;
    }

    public void sendPersonalShouting(String userId, Shouting shouting) {
        log.info("sendPersonalShouting userId:{} shouting:{}",userId,shouting.toString());
        simpMessagingTemplate.convertAndSendToUser(
                userId,
                "/topic/notify/"+userId,
                shouting
        );
    }
}
