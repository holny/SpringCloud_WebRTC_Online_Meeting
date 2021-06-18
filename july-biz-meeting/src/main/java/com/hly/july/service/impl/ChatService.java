package com.hly.july.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hly.july.common.biz.constant.ContainerEnum;
import com.hly.july.common.biz.constant.JulyConstants;
import com.hly.july.common.biz.constant.RelationTypeEnum;
import com.hly.july.common.biz.exception.ServiceInternalException;
import com.hly.july.common.biz.result.Result;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.common.biz.utils.RedisUtils;
import com.hly.july.common.biz.vo.RelationVO;
import com.hly.july.entity.*;
import com.hly.july.service.api.BizUserApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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


    private BizUserApiService bizUserApiService;
    // watcher只存在30s，过期说明客户端没在监听，离线了
    private int REDIS_WATCHER_EXPIRED = JulyConstants.REDIS_WATCHER_EXPIRED;
    private String REDIS_WATCHER = "chat_watcher_";

    private String REDIS_UNREAD_INFO = "chat_unread_info_";
    private String REDIS_UNREAD_COUNT = "chat_unread_count_";

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
            Result<List<RelationVO>> relationVOListResult = bizUserApiService.getUserRelation(peerId,senderId,ContainerEnum.PERSON.getCode(), RelationTypeEnum.getNegativeCodeList());
            if(relationVOListResult.getCode()==ResultCode.SUCCESS.getCode()){
                List<RelationVO> relationVOS = relationVOListResult.getData();
                if(CollectionUtils.isNotEmpty(relationVOS)){
                    for (RelationVO relationVO : relationVOS) {
                        if(peerId.equals(relationVO.getUserId())&&senderId.equals(relationVO.getPeerId())&&RelationTypeEnum.getNegativeCodeList().contains(relationVO.getRelTypeCode())){
                            throw new ServiceInternalException(ResultCode.USER_SOCIAL_BE_BLACKED);
                        }
                    }
                }
            }
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
                    upInsertUnRead(senderId,ContainerEnum.getCodeByDesc(message.getPeerType()),peerId);
                    throw new ServiceInternalException("对方不在线");
                }
            }else{
                throw new ServiceInternalException("保存消息失败");
            }

        }else{
            throw new ServiceInternalException(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }

    public Boolean upInsertUnRead(String senderId,Integer senderType,String receiverId){
        if(senderId!=null&&senderType!=null&&receiverId!=null){
            if (redisUtils.hHasKey(REDIS_UNREAD_COUNT + receiverId,senderId)){
                redisUtils.hashIncr(REDIS_UNREAD_COUNT + receiverId,senderId,1);
            }else{
                redisUtils.hSet(REDIS_UNREAD_COUNT + receiverId,senderId,0);
            }
            return true;
        }else{
            return false;
        }
    }

    public Boolean clearUnRead(String senderId,Integer senderType,String receiverId){
        if(senderId!=null&&senderType!=null&&receiverId!=null){
            if (redisUtils.hHasKey(REDIS_UNREAD_COUNT + receiverId,senderId)){
                redisUtils.hSet(REDIS_UNREAD_COUNT + receiverId,senderId,0);
            }else{
                redisUtils.hSet(REDIS_UNREAD_COUNT + receiverId,senderId,0);
            }
            return true;
        }else{
            return false;
        }
    }

    public long getUnRead(String senderId,Integer senderType,String receiverId){
        if(senderId!=null&&senderType!=null&&receiverId!=null){
            if (redisUtils.hHasKey(REDIS_UNREAD_COUNT + receiverId,senderId)){
                return (Long)redisUtils.hGet(REDIS_UNREAD_COUNT + receiverId,senderId);
            }else{
                redisUtils.hSet(REDIS_UNREAD_COUNT + receiverId,senderId,0);
                return 0;
            }
        }else{
            return 0;
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
