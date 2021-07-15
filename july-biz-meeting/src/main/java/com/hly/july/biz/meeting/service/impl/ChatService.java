package com.hly.july.biz.meeting.service.impl;

import com.hly.july.biz.meeting.entity.*;
import com.hly.july.biz.meeting.service.api.BizUserApiService;
import com.hly.july.common.core.constant.ContainerEnum;
import com.hly.july.common.core.constant.JulyConstants;
import com.hly.july.common.core.constant.RelationTypeEnum;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.db.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Autowired
    private AsyncJobService asyncJobService;

    @Resource
    private BizUserApiService bizUserApiService;
    // watcher只存在30s，过期说明客户端没在监听，离线了
    private int REDIS_WATCHER_EXPIRED = JulyConstants.REDIS_WATCHER_EXPIRED;
    private String REDIS_WATCHER = "chat_watcher_";

    private String REDIS_UNREAD_COUNT = "chat_unread_count_";

    private String USER_RELATION_FILTER = "user_relation_filter_";

    public Boolean upInsertWatcher(Watcher watcher){
        if(watcher!=null&&watcher.getWatcherId()!=null&&watcher.getPeerId()!=null){
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
            Integer filterRelTypeCode = getRelationFilterRelTypeCode(peerId,senderId);
            if(filterRelTypeCode!=null&& RelationTypeEnum.getNegativeCodeList().contains(filterRelTypeCode)){
                if(RelationTypeEnum.BLACK.getCode()==filterRelTypeCode){
                    throw new ServiceInternalException(ResultCode.USER_SOCIAL_BE_BLACKED);
                }else if(RelationTypeEnum.IGNORE.getCode()==filterRelTypeCode){
                    throw new ServiceInternalException(ResultCode.USER_SOCIAL_BE_IGNORED);
                }
            }
            boolean result = messageService.insertTodayMessage(message);
            if(result){
                if(isWatchingMe(senderId,message.getTo(), ContainerEnum.PERSON.getDesc())){
                    sendPersonalShouting(message.getTo(),message);
                    if(ContainerEnum.PERSON.getDesc().equals(message.getPeerType())){
                        sendPersonalShouting(message.getFrom(),message);
                    }
                    asyncJobService.sendMsgSubProcess(message.getFrom(),ContainerEnum.PERSON.getCode(),message.getTo(), ContainerEnum.getCodeByDesc(message.getPeerType()),message.getGmtCreate(),false);
                    return true;
                }else{
                    if(ContainerEnum.PERSON.getDesc().equals(message.getPeerType())){
                        sendPersonalShouting(message.getFrom(),message);
                    }
                    asyncJobService.sendMsgSubProcess(message.getFrom(),ContainerEnum.PERSON.getCode(),message.getTo(),ContainerEnum.getCodeByDesc(message.getPeerType()),message.getGmtCreate(),true);
                    throw new ServiceInternalException("对方不在线");
                }
            }else{
                throw new ServiceInternalException("保存消息失败");
            }

        }else{
            throw new ServiceInternalException(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }

    public Integer changeUnRead(String senderId,Integer senderType,String receiverId,String action){
        if(senderId!=null&&senderType!=null&&receiverId!=null){
            Boolean result = null;
            if("update".equals(action)){
                result = upInsertUnRead(senderId,senderType,receiverId);
            }else if("clear".equals(action)){
                result = clearUnRead(senderId,senderType,receiverId);
            }
            if(result!=null&&result){
                Integer count = 0;
                if (redisUtils.hHasKey(REDIS_UNREAD_COUNT + receiverId,senderId)){
                    count = (Integer)redisUtils.hGet(REDIS_UNREAD_COUNT + receiverId,senderId);
                }
                return count;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

    private Boolean upInsertUnRead(String senderId,Integer senderType,String receiverId){
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

    private Boolean clearUnRead(String senderId,Integer senderType,String receiverId){
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

    public Integer getUnRead(String senderId,Integer senderType,String receiverId){
        if(senderId!=null&&senderType!=null&&receiverId!=null){
            if (redisUtils.hHasKey(REDIS_UNREAD_COUNT + receiverId,senderId)){
                return (Integer)redisUtils.hGet(REDIS_UNREAD_COUNT + receiverId,senderId);
            }else{
                redisUtils.hSet(REDIS_UNREAD_COUNT + receiverId,senderId,0);
                return 0;
            }
        }else{
            return 0;
        }
    }

    public Integer getAllUnRead(String receiverId){
        if(receiverId!=null){
            if(redisUtils.hasKey(REDIS_UNREAD_COUNT + receiverId)){
                Integer sum = new Integer(0);
                Map<Object,Object> hm = redisUtils.hmGet(REDIS_UNREAD_COUNT + receiverId);
                if(ObjectUtils.isNotEmpty(hm)){
                    for (Map.Entry<Object,Object> entry : hm.entrySet()) {
                        Integer count = (Integer) entry.getValue();
                        if(count>0){
                            if(count>=99){
                                sum +=99;
                            }else{
                                sum +=count;
                            }
                        }
                    }
                }
                return sum;
            }else{
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

    public Integer getRelationFilterRelTypeCode(String userId,String peerId){
        if(StringUtils.isNotEmpty(userId)&&StringUtils.isNotEmpty(peerId)) {
            Integer relTypeCode = null;
            if(redisUtils.hHasKey(USER_RELATION_FILTER + userId,peerId)){
                relTypeCode = (Integer) redisUtils.hGet(USER_RELATION_FILTER + userId,peerId);
            }
            return relTypeCode;
        }else{
            return null;
        }
    }

    public Boolean upInsertRelationFilterRelTypeCode(String userId,String peerId,Integer relTypeCode){
        if(StringUtils.isNotEmpty(userId)&&StringUtils.isNotEmpty(peerId)&&relTypeCode!=null) {
            redisUtils.hSet(USER_RELATION_FILTER + userId,peerId,relTypeCode);
            return true;
        }else{
            return false;
        }
    }

    public Boolean removeRelationFilterRelTypeCode(String userId,String peerId,Integer relTypeCode){
        log.info("removeRelationFilterRelTypeCode userId:{},peerId:{},relTypeCode:{}",userId,peerId,relTypeCode);
        if(StringUtils.isNotEmpty(userId)&&StringUtils.isNotEmpty(peerId)&&relTypeCode!=null) {
            Integer existedCode = getRelationFilterRelTypeCode(userId,peerId);
            if(relTypeCode.equals(existedCode)){
                redisUtils.hDel(USER_RELATION_FILTER + userId,peerId);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


}
