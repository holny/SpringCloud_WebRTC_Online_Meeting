package com.hly.july.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hly.july.common.biz.constant.ContainerEnum;
import com.hly.july.common.biz.constant.JulyConstants;
import com.hly.july.common.biz.entity.Container;
import com.hly.july.common.biz.exception.ServiceInternalException;
import com.hly.july.common.biz.result.Result;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.common.biz.util.DateUtils;
import com.hly.july.common.biz.utils.RedisUtils;
import com.hly.july.entity.Event;
import com.hly.july.entity.MessageVO;
import com.hly.july.entity.Shouting;
import com.hly.july.entity.Watcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ChatService
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/16 10:24
 * @Version 1.0.0
 **/
@Service
@Slf4j
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    @Resource
    private RedisUtils redisUtils;

    @Autowired
    private MessageServiceImpl messageService;
    // watcher只存在30s，过期说明客户端没在监听，离线了
    private int REDIS_WATCHER_EXPIRED = JulyConstants.REDIS_WATCHER_EXPIRED;
    private String REDIS_WATCHER = "chat_watcher_";

    public Boolean upInsertWatcher(Watcher watcher){
        if(watcher!=null&&watcher.getWatcherId()!=null&&watcher.getPeerId()!=null){
            log.info("upInsertWatcher watcher:{}",watcher.toString());
            if(watcher.getAction()!=null&&"entry".equals(watcher.getAction())) {
                redisUtils.set(REDIS_WATCHER + watcher.getWatcherId(), watcher, REDIS_WATCHER_EXPIRED);
                return true;
            }else{
                if (redisUtils.hasKey(REDIS_WATCHER +  watcher.getWatcherId())){
                    Watcher existedWatcher = (Watcher) redisUtils.get(REDIS_WATCHER + watcher.getWatcherId());
                    if(existedWatcher!=null&& watcher.getPeerId().equals(existedWatcher.getPeerId())){
                        log.info("remove watcher:{}",watcher.toString());
                        return redisUtils.delete(REDIS_WATCHER + watcher.getWatcherId());
                    }else{
                        return true;
                    }
                }else{
                    return true;
                }
            }
        }else{
            return false;
        }
    }

    public Boolean isWatchingMe(String myId,String peerId,String peerType){
        if(myId!=null&&peerId!=null&&peerId!=null){
            if (redisUtils.hasKey(REDIS_WATCHER + peerId)){
                Watcher watcher = (Watcher) redisUtils.get(REDIS_WATCHER + peerId);
                if(watcher!=null&& myId.equals(watcher.getPeerId())){
                    log.info("isWatchingMe myId:{},peerId:{}",myId,peerId);
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean sendPersonalMessage(String peerId,String senderId, MessageVO message) throws ServiceInternalException {
        log.info("sendPersonalMessage peerId:{},senderId:{}, message:{}",peerId,senderId,message.toString());
        if (peerId!=null&&message!=null) {
            // Todo:要检查对方是否存在，并且是否黑名单
            boolean result = messageService.insertTodayMessage(message);
            if(result){
                if(isWatchingMe(senderId,message.getTo(), ContainerEnum.PERSON.getDesc())){
                    sendPersonalShouting(message.getTo(),message);
                    if(ContainerEnum.PERSON.getDesc().equals(message.getPeerType())){
                        sendPersonalShouting(message.getFrom(),message);
                    }
                    return true;
                }else{
                    if(ContainerEnum.PERSON.getDesc().equals(message.getPeerType())){
                        sendPersonalShouting(message.getFrom(),message);
                    }
                    throw new ServiceInternalException("对方不在线");
                }
            }else{
                throw new ServiceInternalException("保存消息失败");
            }

        }else{
            throw new ServiceInternalException(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }

    public Boolean sendPersonalEvent(String userId,String hostId, Event event)  throws ServiceInternalException{
        log.info("sendPersonalEvent userId:{},hostId:{}, event:{}",userId,hostId,event.toString());
        event.setType("event");
        if (userId!=null&&event!=null) {
            sendPersonalShouting(event.getTo(),event);
            return true;
        }else{
            throw new ServiceInternalException(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }



    public void sendPersonalShouting(String userId, Shouting shouting)  throws ServiceInternalException{
        log.info("sendPersonalShouting userId:{} shouting:{}",userId,shouting.toString());
        shouting.setType("message");
        simpMessagingTemplate.convertAndSendToUser(
                userId,
                "/topic/personal/"+userId,
                shouting
        );
    }

}
