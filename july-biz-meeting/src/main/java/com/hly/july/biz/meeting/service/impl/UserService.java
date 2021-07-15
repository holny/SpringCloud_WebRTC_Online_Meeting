package com.hly.july.biz.meeting.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hly.july.biz.meeting.entity.Event;
import com.hly.july.biz.meeting.entity.Shouting;
import com.hly.july.biz.meeting.entity.UserActiveStatusEnum;
import com.hly.july.biz.meeting.service.api.BizUserApiService;
import com.hly.july.common.core.constant.ContainerEnum;
import com.hly.july.common.core.constant.RelationTypeEnum;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.db.utils.RedisUtils;
import com.hly.july.common.web.vo.RelationVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Resource
    private BizUserApiService bizUserApiService;


    private String REDIS_PREFIX_ROOM_INFO = "meeting_room_info_";
    private String REDIS_PREFIX_ACK = "meeting_room_ack_";

    private String USER_ACTIVE_STATUS = "user_active_status";

    private long USER_ACTIVE_STATUS_TIME = 30;


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

    public Boolean updateUserActiveStatus(String userId,Integer status){
        if (StringUtils.isNotEmpty(userId)) {
            if(!UserActiveStatusEnum.getAllCodeList().contains(status)){
                status = UserActiveStatusEnum.ACTIVE_VISIBLE.getCode();
            }
            redisUtils.hSet(USER_ACTIVE_STATUS,userId,status,USER_ACTIVE_STATUS_TIME);
            return true;
        }else{
            return false;
        }
    }

    public Integer getUserActiveStatus(String userId){
        if (StringUtils.isNotEmpty(userId)) {
            if(redisUtils.hHasKey(USER_ACTIVE_STATUS,userId)){
                Integer status = (Integer)redisUtils.hGet(USER_ACTIVE_STATUS,userId);
                log.info("getUserActiveStatus userId:{},status:{}",userId,status);
                return status;
            }else{
                return UserActiveStatusEnum.OFFLINE.getCode();
            }
        }else{
            return UserActiveStatusEnum.OFFLINE.getCode();
        }
    }

    public Boolean isInPeerBookMark(String userId,String peerId){
        if (StringUtils.isNotEmpty(userId)&&StringUtils.isNotEmpty(peerId)) {
            List<Integer> relTypeList = new ArrayList<>();
            relTypeList.add(RelationTypeEnum.FRIEND.getCode());
            Result<List<RelationVO>> result = bizUserApiService.getUserRelation(peerId,null, ContainerEnum.PERSON.getCode(), relTypeList);
            log.info("isInPeerBookMark  getUserRelation result:{}",result.toString());
            if(result.getCode()==ResultCode.SUCCESS.getCode()){
                List<RelationVO> relationVOList = result.getData();
                if(CollectionUtils.isNotEmpty(relationVOList)){
                    if(relationVOList.contains(userId)){
                        return true;
                    }
                }
            }
        }
        return false;
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
