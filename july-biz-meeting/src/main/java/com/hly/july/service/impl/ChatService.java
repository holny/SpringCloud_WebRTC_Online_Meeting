package com.hly.july.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hly.july.common.biz.utils.RedisUtils;
import com.hly.july.common.result.Result;
import com.hly.july.common.result.ResultCode;
import com.hly.july.common.util.DateUtils;
import com.hly.july.entity.*;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName ChatService
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/8 12:39
 * @Version 1.0.0
 **/

@Service
@Slf4j
public class ChatService {
    @Resource
    private RedisUtils redisUtils;

    private String REDIS_PREFIX_ROOM_INFO = "room_info_";
    private String REDIS_PREFIX_ROOM_MEMBER = "room_member_";
    private String REDIS_PREFIX_ACK = "room_ack_";

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public Result<Room> createRoom(String hostId,String roomId,String type) {
        if (hostId!=null){
            if(roomId==null){
                roomId = String.valueOf(IdWorker.getId());
            }else if (redisUtils.hasKey(REDIS_PREFIX_ROOM_INFO + roomId)){
                return Result.failure(ResultCode.WEBSOCKET_ROOM_DUPLICATE);
            }
            log.info("createRoom hostId:{}, roomId:{}, type:{}",hostId,roomId,type);
            Room chatRoom = new Room();
            chatRoom.setRoomId(roomId);
            chatRoom.setDominatorId(hostId);
            Set<String> adminIdSet = new HashSet<>();
            adminIdSet.add(hostId);
            Date createTime = DateUtils.getCurrentDateTime();
            chatRoom.setRoomName(hostId+"'s room");
            chatRoom.setAdminIdSet(adminIdSet);
            chatRoom.setGmtCreate(createTime);
            chatRoom.setMaxMemberCount(5);
            chatRoom.setType(type);
            Set<String> memberIdSet = new HashSet<>();
            memberIdSet.add(hostId);
            chatRoom.setMemberIdSet(memberIdSet);
            redisUtils.set(REDIS_PREFIX_ROOM_INFO+roomId,chatRoom);
            redisUtils.set(REDIS_PREFIX_ROOM_MEMBER+roomId,memberIdSet);
            return Result.success(chatRoom);
        }else{
            return Result.failure(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }


    public Room getRoomByRoomId(String roomId) {
        if(redisUtils.hasKey(REDIS_PREFIX_ROOM_INFO+roomId)){
            Room chatRoom = (Room)redisUtils.get(REDIS_PREFIX_ROOM_INFO+roomId);
            Set<String> memberIdSet = (Set<String>)redisUtils.get(REDIS_PREFIX_ROOM_MEMBER+roomId);
            chatRoom.setMemberIdSet(memberIdSet);
            return chatRoom;
        }else{
            log.info("getRoomByRoomId , roomId not exist in redis, roomId{}",roomId);
            return null;
        }
    }

    public Result<Room> addMemberIntoRoom(String roomId,String userId) {
        if (roomId!=null&&userId!=null) {
            log.info("addMemberIntoRoom , roomId{}, new memberId:{}",roomId,userId);
            Room chatRoom;
            log.info("addMemberIntoRoom ,to  create a new");
            if (!redisUtils.hasKey(REDIS_PREFIX_ROOM_INFO + roomId)) {
                log.info("addMemberIntoRoom ,to  create a new");
                Result<Room> result = createRoom(userId,roomId,"group");
                log.info("addMemberIntoRoom , create a new ROOM{}",result.toString());
                if (result.getCode()==ResultCode.SUCCESS.getCode()){
                    chatRoom = result.getData();
                }else{
                    return result;
                }
            }else{
                log.info("addMemberIntoRoom ,room exists");
                chatRoom = (Room) redisUtils.get(REDIS_PREFIX_ROOM_INFO + roomId);
            }
            try {
                Set<String> memberIdSet = (Set<String>) redisUtils.get(REDIS_PREFIX_ROOM_MEMBER + roomId);
                chatRoom = (Room) redisUtils.get(REDIS_PREFIX_ROOM_INFO + roomId);
                log.info("addMemberIntoRoom , chatRoom:{}",chatRoom.toString());
                log.info("addMemberIntoRoom , memberIdSet:{}",memberIdSet.toString());
//                redisUtils.multi();
                log.info("addMemberIntoRoom , entry multi");
                if(chatRoom.getMaxMemberCount()>memberIdSet.size()&&!memberIdSet.contains(userId)){
                    if(!memberIdSet.contains(userId)){
                        memberIdSet.add(userId);
                        chatRoom.setMemberIdSet(memberIdSet);
                        redisUtils.set(REDIS_PREFIX_ROOM_INFO + roomId, chatRoom);
                        redisUtils.set(REDIS_PREFIX_ROOM_MEMBER+roomId,memberIdSet);
                    }
//                    redisUtils.exec();
                    log.info("addMemberIntoRoom , add success, roomId{}, new memberId:{}",roomId,userId);
                    return Result.success(chatRoom);
                }else if(memberIdSet.contains(userId)){
                    log.info("addMemberIntoRoom , user already exist, roomId{}, new memberId:{}",roomId,userId);
                    return Result.failure(ResultCode.WEBSOCKET_USER_EXIST,chatRoom);
                }else{
//                    redisUtils.exec();
                    return Result.failure(ResultCode.WEBSOCKET_ROOM_OCCUPIED);
                }
            } catch (Exception e) {
//                redisUtils.discard();
                log.error("addMemberIntoRoom , error\n:{}", e.getMessage());
                e.printStackTrace();
                return Result.failure(ResultCode.WEBSOCKET_SERVER_ERROR);
            }
//            } else {
//                log.info("addMemberIntoRoom , roomId not exist in redis, roomId{}, new memberId:{}",roomId,userId);
//                return Result.failure(ResultCode.WEBSOCKET_ROOM_NOT_FOUND);
//            }
        }else{
            return Result.failure(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }

    public Result<Room> deleteMemberFromRoom(String roomId,String userId) {
        if (roomId!=null&&userId!=null) {
            if (redisUtils.hasKey(REDIS_PREFIX_ROOM_INFO + roomId)) {
                Room chatRoom = (Room) redisUtils.get(REDIS_PREFIX_ROOM_INFO + roomId);
                Set<String> memberIdSet = (HashSet<String>) redisUtils.get(REDIS_PREFIX_ROOM_MEMBER + roomId);
                try {
                    redisUtils.multi();
                    Set<String> adminIdSet = chatRoom.getAdminIdSet();
                    if (memberIdSet.contains(userId)){
                        memberIdSet.remove(userId);
                    }
                    if (adminIdSet.contains(userId)){
                        adminIdSet.remove(userId);
                    }
                    // 如果当前删除的UserId是房主，需要找一个替代用户
                    if (chatRoom.getDominatorId().equals(userId)){
                        ArrayList<String> adminIdList = new ArrayList<>(adminIdSet);
                        if(adminIdList.size()>0){
                            String newDominatorId = adminIdList.get(0);
                            chatRoom.setDominatorId(newDominatorId);
                        }else{
                            ArrayList<String> memberIdList = new ArrayList<>(memberIdSet);
                            if(memberIdList.size()>0){
                                String newDominatorId = memberIdList.get(0);
                                chatRoom.setDominatorId(newDominatorId);
                                adminIdSet.add(newDominatorId);
                                chatRoom.setAdminIdSet(adminIdSet);
                            }else{
                                log.info("No member exist");
                            }
                        }
                    }
                    chatRoom.setMemberIdSet(memberIdSet);
                    redisUtils.set(REDIS_PREFIX_ROOM_INFO + roomId, chatRoom);
                    redisUtils.set(REDIS_PREFIX_ROOM_MEMBER + roomId, memberIdSet);
                    redisUtils.exec();
                    log.info("deleteMemberFromRoom , delete success, roomId{}, new memberId:{}",roomId,userId);
                    return Result.success(chatRoom);
                } catch (Exception e) {
                    redisUtils.discard();
                    log.error("deleteMemberFromRoom , error\n:{}", e.getMessage());
                    e.printStackTrace();
                    return Result.failure(ResultCode.WEBSOCKET_SERVER_ERROR);
                }
            } else {
                log.info("deleteMemberFromRoom , roomId not exist in redis, roomId{}, new memberId:{}",roomId,userId);
                return Result.failure(ResultCode.WEBSOCKET_ROOM_NOT_FOUND);
            }
        }else{
            return Result.failure(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }

    public Result<String> sendRoomMessage(String roomId, Message message) {
        log.info("sendRoomMessage room:{}, message:{}",roomId,message.toString());
        if (roomId!=null&&message!=null) {
            if (redisUtils.hasKey(REDIS_PREFIX_ROOM_INFO + roomId)) {
                Room chatRoom = (Room) redisUtils.get(REDIS_PREFIX_ROOM_INFO + roomId);
                if(message.getMethod().equals("broadcast")){
                    broadcastRoomShouting(chatRoom,message);
                }else if(message.getMethod().equals("personal")&&message.getTo()!=null){
                    sendRoomShouting(message.getTo(),chatRoom.getRoomId(),message);
                }
                return Result.success();
            }else{
                return Result.failure(ResultCode.WEBSOCKET_ROOM_NOT_FOUND);
            }
        }else{
            return Result.failure(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }

    public void sendRoomEvent(String roomId, Event event) {
        log.info("sendEvent room:{}, event:{}",roomId,event.toString());
        if (roomId!=null&&event!=null) {
            if (redisUtils.hasKey(REDIS_PREFIX_ROOM_INFO + roomId)) {
                Room chatRoom = (Room) redisUtils.get(REDIS_PREFIX_ROOM_INFO + roomId);
                if(event.getMethod().equals("broadcast")){
                    broadcastRoomShouting(chatRoom,event);
                }else if(event.getMethod().equals("personal")&&event.getTo()!=null){
                    sendRoomShouting(event.getTo(),roomId,event);
                }
            }
        }
    }

    public void broadcastRoomShouting(Room room, Shouting shouting) {
        log.info("broadcastRoomShouting room:{}, shouting:{}",room.toString(),shouting.toString());
        Set<String> memberIdSet = room.getMemberIdSet();
        Iterator it = memberIdSet.iterator();

        while(it.hasNext()) {
            sendRoomShouting((String)it.next(),room.getRoomId(),shouting);

        }
    }

    public void sendRoomShouting(String userId,String roomId, Shouting shouting) {
        log.info("sendPersonalShouting userId:{}, shouting:{}",userId,shouting.toString());
        simpMessagingTemplate.convertAndSendToUser(
                userId,
                "/topic/room/"+roomId,
                shouting
        );
    }

    public Result<String> sendPersonalMessage(String userId,String hostId, Message message) {
        log.info("sendPersonalMessage userId:{},hostId:{}, message:{}",userId,hostId,message.toString());
        if (userId!=null&&message!=null) {
            sendPersonalShouting(message.getTo(),message);
            return Result.success();
        }else{
            return Result.failure(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }

    public Result<String> sendPersonalEvent(String userId,String hostId, Event event) {
        log.info("sendPersonalEvent userId:{},hostId:{}, event:{}",userId,hostId,event.toString());
        if (userId!=null&&event!=null) {
            sendPersonalShouting(event.getTo(),event);
            return Result.success();
        }else{
            return Result.failure(ResultCode.WEBSOCKET_REQUEST_ERROR);
        }
    }



    public void sendPersonalShouting(String userId, Shouting shouting) {
        log.info("sendPersonalShouting userId:{} shouting:{}",userId,shouting.toString());
        simpMessagingTemplate.convertAndSendToUser(
                userId,
                "/topic/personal/"+userId,
                shouting
        );
    }



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

}
