package com.hly.july.service.impl;

import cn.hutool.core.io.unit.DataUnit;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hly.july.common.biz.constant.ContainerEnum;
import com.hly.july.common.biz.exception.ServiceInternalException;
import com.hly.july.common.biz.result.Result;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.common.biz.util.DateUtils;
import com.hly.july.common.biz.utils.RedisUtils;
import com.hly.july.common.biz.vo.RelationVO;
import com.hly.july.entity.*;
import com.hly.july.service.api.BizUserApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName MeetingService
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/23 09:51
 * @Version 1.0.0
 **/
@Service
@Slf4j
public class MeetingService {
    @Autowired
    private UserService userService;
    @Resource
    private BizUserApiService bizUserApiService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MeetingService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    @Resource
    private RedisUtils redisUtils;

    private String MEETING_USER_ACTIVITY = "meeting_user_activity";
    private long MEETING_USER_ACTIVITY_TIME = 30;
    private String MEETING_SESSION = "meeting_session";


    /**
     * hostId 不一定是发起会话的发起人，得由ack查看
     * @param hostId
     * @param peerId
     * @return
     * @throws ServiceInternalException
     */
    public MeetingSession preMeetingProcess(String hostId,String peerId,String sessionId,boolean isAccept) throws ServiceInternalException {
        if(StringUtils.isNumeric(hostId)&&StringUtils.isNumeric(peerId)) {
            if(StringUtils.isEmpty(sessionId)){ // 如果sessionId为空，说明是第一次请求，HostId为发起人
                // 先检查自己的在线状态
                if(!UserActiveStatusEnum.getAllOnLineCodeList().contains(userService.getUserActiveStatus(hostId))){
                    // 说明自己就是不在线的状态，无法给任何发出meeting请求
                    Event<MeetingSession> meetingSessionEventToHost = Event.buildPersonal(EventEnum.EVENT_CALL_FAIL,"自己需保持在线状态",hostId,null);
                    sendPersonalEvent(hostId,meetingSessionEventToHost);
                    throw new ServiceInternalException("自己必须在线状态");
                }
                sessionId = IdWorker.getIdStr();
                // 先把双方置为in meeting
                MeetingSession meetingSession = new MeetingSession();
                meetingSession.setSessionId(sessionId);
                meetingSession.setRequesterId(hostId);
                meetingSession.setPeerId(peerId);
                meetingSession.setGmtRequestCreate(DateUtils.getCurrentDateTime());
                if (redisUtils.hHasKey(MEETING_USER_ACTIVITY,peerId)) {    // 对方有meeting session，说明对方在忙
                    meetingSession.setStatus(SessionStatusEnum.FAIL.getCode());
                    Event<MeetingSession> meetingSessionEventToHost = Event.buildPersonal(EventEnum.EVENT_CALL_BUSY,hostId,meetingSession);
                    sendPersonalEvent(hostId,meetingSessionEventToHost);
                    throw new ServiceInternalException(ResultCode.USER_ACTIVITY_PEER_BUSY);
                }else{
                    // 再检查对方的在线状态
                    if(userService.getUserActiveStatus(peerId).equals(UserActiveStatusEnum.ACTIVE_VISIBLE.getCode())||(userService.getUserActiveStatus(peerId).equals(UserActiveStatusEnum.ACTIVE_ONLY_CATEGORY_BOOKMARK.getCode())&& userService.isInPeerBookMark(hostId,peerId))){
                        // 第一步-requester(Host)发起请求
                        // 首先要给peer获取关于host的relation信息
                        Result<List<RelationVO>> result = bizUserApiService.getUserRelation(peerId,hostId,ContainerEnum.PERSON.getCode(),null);
                        if(result.getCode()!=ResultCode.SUCCESS.getCode()||result.getData()==null||result.getData().size()==0){
                            // 如果获取不到对方关系信息，说明对方不存在或者账号异常
                            meetingSession.setStatus(SessionStatusEnum.FAIL.getCode());
                            Event<MeetingSession> meetingSessionEventToHost = Event.buildPersonal(EventEnum.EVENT_CALL_FAIL,"无法获取对方信息",hostId,meetingSession);
                            sendPersonalEvent(hostId,meetingSessionEventToHost);
                            throw new ServiceInternalException("无法获取对方信息");
                        }

                        redisUtils.hSet(MEETING_USER_ACTIVITY ,peerId,sessionId,MEETING_USER_ACTIVITY_TIME);
                        redisUtils.hSet(MEETING_USER_ACTIVITY ,hostId,sessionId,MEETING_USER_ACTIVITY_TIME);
                        meetingSession.setStatus(SessionStatusEnum.IN_CONFIRM.getCode());
                        redisUtils.hSet(MEETING_SESSION,sessionId, meetingSession);
                        Event<MeetingSession> meetingSessionEventToHost = Event.buildPersonal(EventEnum.EVENT_CALL_RING,hostId,meetingSession);
                        sendPersonalEvent(hostId,meetingSessionEventToHost);
                        RelationVO relationVO = result.getData().get(0);
                        meetingSession.setData(relationVO);
                        Event<MeetingSession> meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_IN,peerId,meetingSession);
                        sendPersonalEvent(peerId,meetingSessionEventToPeer);
                        return meetingSession;
                    }else{
                        // 如果对方不在线，或者对方设置为只允许Bookmark，就返回Offline消息
                        meetingSession.setStatus(SessionStatusEnum.FAIL.getCode());
                        Event<MeetingSession> meetingSessionEventToHost = Event.buildPersonal(EventEnum.EVENT_CALL_OFFLINE,hostId,meetingSession);
                        sendPersonalEvent(hostId,meetingSessionEventToHost);
                        throw new ServiceInternalException(ResultCode.USER_ACTIVITY_PEER_OFFLINE);
                    }
                }
            }else{ // 如果sessionId不为为空
                MeetingSession meetingSession = checkSessionStatusActive(hostId,sessionId);
                if(!isAccept){
                    // 除了第一步，无论哪步，都可以直接取消Session
                    Event<MeetingSession> meetingSessionEventToRequester;
                    Event<MeetingSession> meetingSessionEventToPeer;
                    if(!meetingSession.getStatus().equals(SessionStatusEnum.IN_MEETING.getCode())){
                        // 如果还没有进入In meeting阶段，置为fail
                        meetingSession.setStatus(SessionStatusEnum.FAIL.getCode());
                        if(hostId.equals(meetingSession.getRequesterId())){
                            meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_FAIL,meetingSession.getRequesterId(),null);
                            meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_DENY,meetingSession.getPeerId(),null);

                        }else{
                            meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_DENY,meetingSession.getRequesterId(),null);
                            meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_FAIL,meetingSession.getPeerId(),null);
                        }
                    }else{
                        // 如果已进入IN MEETING阶段，置为closed
                        meetingSession.setStatus(SessionStatusEnum.CLOSED.getCode());
                        if(hostId.equals(meetingSession.getRequesterId())){
                            meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_HANGUP,meetingSession.getRequesterId(),null);
                            meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_HANGUP,meetingSession.getPeerId(),null);

                        }else{
                            meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_HANGUP,meetingSession.getRequesterId(),null);
                            meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_HANGUP,meetingSession.getPeerId(),null);
                        }
                    }
                    meetingSession.setGmtMeetingEnd(DateUtils.getCurrentDateTime());
                    redisUtils.hSet(MEETING_SESSION,sessionId,meetingSession); // 更新Session
                    meetingSessionEventToRequester.setData(meetingSession);
                    meetingSessionEventToPeer.setData(meetingSession);
                    sendPersonalEvent(meetingSession.getRequesterId(),meetingSessionEventToRequester);
                    sendPersonalEvent(meetingSession.getPeerId(),meetingSessionEventToPeer);
                    redisUtils.hDel(MEETING_USER_ACTIVITY ,meetingSession.getRequesterId());
                    redisUtils.hDel(MEETING_USER_ACTIVITY ,meetingSession.getPeerId());
                    throw new ServiceInternalException(ResultCode.MEETING_SESSION_CLOSED);
                }
                if(meetingSession.getStatus()==SessionStatusEnum.IN_CONFIRM.getCode()){
                    // 第二步-peer(host)接受，说明现在是HandShake阶段
                    if(!hostId.equals(meetingSession.getPeerId())){
                        // 携带了sessionId, host应该是接收者，但是Peer不为host，说明host是发起者(checkSessionStatusActive检验过host必为发起者或者接收者),说明host发出了无效请求。
                        throw new ServiceInternalException(ResultCode.MEETING_REQUEST_DUPLICATE);
                    }
                    // 到这说明当前host是接受者，并且session检查完毕，可以给requester发送peer接受请求的消息。
                    // Todo: HandShake握手阶段完毕, 这里peer得附带webrtc的信息给requester,
                    // IN_CONFIRM(HandSake)阶段完毕，进入Signaling阶段
                    meetingSession.setStatus(SessionStatusEnum.IN_SIGNALING.getCode());
                    redisUtils.hSet(MEETING_SESSION,sessionId,meetingSession); // 更新Session
                    Event<MeetingSession> meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_PRE_CONNECT,meetingSession.getRequesterId(),meetingSession);
                    sendPersonalEvent(meetingSession.getRequesterId(),meetingSessionEventToRequester);

                    Event<MeetingSession> meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_PRE_CONNECT,meetingSession.getPeerId(),meetingSession);
                    sendPersonalEvent(meetingSession.getPeerId(),meetingSessionEventToPeer);

                    redisUtils.hSet(MEETING_USER_ACTIVITY ,hostId,sessionId,MEETING_USER_ACTIVITY_TIME);
                }else if(meetingSession.getStatus()==SessionStatusEnum.IN_SIGNALING.getCode()){
                    // 第三步-requester(host)接受，说明现在已经进入signaling阶段
                    if(!hostId.equals(meetingSession.getRequesterId())){
                        // 携带了sessionId, host应该是接收者，但是Peer不为host，说明host是发起者(checkSessionStatusActive检验过host必为发起者或者接收者),说明host发出了无效请求。
                        throw new ServiceInternalException(ResultCode.MEETING_REQUEST_DUPLICATE);
                    }
                    // 到这说明当前host是requester发起者，并且session检查完毕,requester应当已经接收到了peer的webrtc信息，在这里requester要把自己的webrtc信息发送给peer。
                    meetingSession.setStatus(SessionStatusEnum.IN_MEETING.getCode());
                    meetingSession.setGmtMeetingStart(DateUtils.getCurrentDateTime());
                    redisUtils.hSet(MEETING_SESSION,sessionId,meetingSession); // 更新Session
                    Event<MeetingSession> meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_CONNECTED,meetingSession.getRequesterId(),meetingSession);
                    sendPersonalEvent(meetingSession.getRequesterId(),meetingSessionEventToRequester);

                    Event<MeetingSession> meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_CONNECTED,meetingSession.getPeerId(),meetingSession);
                    sendPersonalEvent(meetingSession.getPeerId(),meetingSessionEventToPeer);

                    redisUtils.hSet(MEETING_USER_ACTIVITY ,hostId,sessionId,MEETING_USER_ACTIVITY_TIME);
                }else if(meetingSession.getStatus()==SessionStatusEnum.IN_MEETING.getCode()){
                    // 已经进入IN MEETING阶段，说明已经完成以上三步，这里是更新host的session activity状态(证明自己在在线)，以免过期，并且获取对方是否在线状态
                    boolean isHangup = false;
                    if(hostId.equals(meetingSession.getRequesterId())){
                        if(!redisUtils.hHasKey(MEETING_USER_ACTIVITY,meetingSession.getPeerId())){
                            // host是requester，但是peer不在线了
                            isHangup = true;
                        }
                    }else if(hostId.equals(meetingSession.getPeerId())){
                        if(!redisUtils.hHasKey(MEETING_USER_ACTIVITY,meetingSession.getRequesterId())){
                            // host是peer，但是requester不在线了
                            isHangup = true;
                        }
                    }
                    if(isHangup){
                        meetingSession.setStatus(SessionStatusEnum.CLOSED.getCode());
                        meetingSession.setGmtMeetingEnd(DateUtils.getCurrentDateTime());
                        redisUtils.hSet(MEETING_SESSION,sessionId,meetingSession); // 更新Session
                        Event<MeetingSession> meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_HANGUP,meetingSession.getRequesterId(),meetingSession);
                        sendPersonalEvent(meetingSession.getRequesterId(),meetingSessionEventToRequester);

                        Event<MeetingSession> meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_HANGUP,meetingSession.getPeerId(),meetingSession);
                        sendPersonalEvent(meetingSession.getPeerId(),meetingSessionEventToPeer);

                        redisUtils.hDel(MEETING_USER_ACTIVITY ,meetingSession.getRequesterId());
                        redisUtils.hDel(MEETING_USER_ACTIVITY ,meetingSession.getPeerId());
                        throw new ServiceInternalException(ResultCode.MEETING_SESSION_CLOSED);
                    }else{
                        redisUtils.hSet(MEETING_USER_ACTIVITY ,hostId,sessionId,MEETING_USER_ACTIVITY_TIME);
                    }
                }
                return meetingSession;
            }
        }else{
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
    }

    public MeetingSession checkSessionStatusActive(String hostId,String sessionId) throws ServiceInternalException{
        if(StringUtils.isEmpty(sessionId)||!redisUtils.hHasKey(MEETING_SESSION,sessionId)){
            throw new ServiceInternalException(ResultCode.MEETING_SESSION_ERROR,"Session不存在1");
        }
        MeetingSession meetingSession = (MeetingSession) redisUtils.hGet(MEETING_SESSION,sessionId);
        if(StringUtils.isNotEmpty(hostId)&&!meetingSession.getRequesterId().equals(hostId)&&!meetingSession.getPeerId().equals(hostId)){
            // 虽然SessionId存在，但不属于当前host的
            throw new ServiceInternalException(ResultCode.MEETING_SESSION_ERROR,"Session不存在2");
        }
        if(!SessionStatusEnum.getActiveCodeList().contains(meetingSession.getStatus())){
            // Session已结束
            throw new ServiceInternalException(ResultCode.MEETING_SESSION_CLOSED);
        }
        // 检查MEETING_USER_ACTIVITY，看双方的Session
        if (!redisUtils.hHasKey(MEETING_USER_ACTIVITY,meetingSession.getRequesterId())) {    // 如果requester active不存在了，说了当前seesionId不存在或者过期了
            meetingSession.setStatus(SessionStatusEnum.FAIL.getCode());
            meetingSession.setGmtMeetingEnd(DateUtils.getCurrentDateTime());
            redisUtils.hSet(MEETING_SESSION,sessionId,meetingSession); // 更新Session

            Event<MeetingSession> meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_FAIL,"你的当前Session不存在",meetingSession.getRequesterId(),meetingSession);
            sendPersonalEvent(meetingSession.getRequesterId(),meetingSessionEventToRequester);

            // 通知Requester，对方状态
            Event<MeetingSession> meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_OFFLINE,meetingSession.getPeerId(),meetingSession);
            sendPersonalEvent(meetingSession.getPeerId(),meetingSessionEventToPeer);

            redisUtils.hDel(MEETING_USER_ACTIVITY ,meetingSession.getPeerId());
            throw new ServiceInternalException(ResultCode.MEETING_SESSION_GIVE_UP, "发起人放弃此Session");
        }else if (!redisUtils.hHasKey(MEETING_USER_ACTIVITY,meetingSession.getPeerId())) {    // 如果peer active不存在了，说了当前seesionId不存在或者过期了
            meetingSession.setStatus(SessionStatusEnum.FAIL.getCode());
            meetingSession.setGmtMeetingEnd(DateUtils.getCurrentDateTime());
            redisUtils.hSet(MEETING_SESSION,sessionId,meetingSession); // 更新Session

            // 通知Requester，对方状态
            Event<MeetingSession> meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_OFFLINE,meetingSession.getRequesterId(),meetingSession);
            sendPersonalEvent(meetingSession.getRequesterId(),meetingSessionEventToRequester);
            redisUtils.hDel(MEETING_USER_ACTIVITY ,meetingSession.getRequesterId());

            // 通知Requester，对方状态
            Event<MeetingSession> meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_FAIL,"你的当前Session不存在",meetingSession.getPeerId(),meetingSession);
            sendPersonalEvent(meetingSession.getPeerId(),meetingSessionEventToPeer);
            throw new ServiceInternalException(ResultCode.MEETING_SESSION_GIVE_UP,"接收人放弃此Session");
        }

        if (!sessionId.equals(redisUtils.hGet(MEETING_USER_ACTIVITY,meetingSession.getRequesterId()))||!sessionId.equals(redisUtils.hGet(MEETING_USER_ACTIVITY,meetingSession.getPeerId()))) {
            meetingSession.setStatus(SessionStatusEnum.FAIL.getCode());
            meetingSession.setGmtMeetingEnd(DateUtils.getCurrentDateTime());
            redisUtils.hSet(MEETING_SESSION,sessionId,meetingSession); // 更新Session

            // 如果双方active存在，但是双方存储的sessionId不是同一个sessionId，就说嘛对方忙
            Event<MeetingSession> meetingSessionEventToHost = Event.buildPersonal(EventEnum.EVENT_CALL_BUSY,meetingSession.getRequesterId(),meetingSession);
            sendPersonalEvent(hostId,meetingSessionEventToHost);
            redisUtils.hDel(MEETING_USER_ACTIVITY ,hostId);
            throw new ServiceInternalException(ResultCode.USER_ACTIVITY_PEER_BUSY);
        }
        // 到这说明session存在，并且双方都共同保存这个session
        // 先检查requester状态
        if(!UserActiveStatusEnum.getAllOnLineCodeList().contains(userService.getUserActiveStatus(meetingSession.getRequesterId()))){
            // 说明Requester就是不在线的状态，无法给任何发出meeting请求
            meetingSession.setStatus(SessionStatusEnum.FAIL.getCode());
            meetingSession.setGmtMeetingEnd(DateUtils.getCurrentDateTime());
            redisUtils.hSet(MEETING_SESSION,sessionId,meetingSession); // 更新Session

            Event<MeetingSession> meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_FAIL,meetingSession.getRequesterId(),meetingSession);
            sendPersonalEvent(meetingSession.getRequesterId(),meetingSessionEventToRequester);

            Event<MeetingSession> meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_OFFLINE,meetingSession.getPeerId(),meetingSession);
            sendPersonalEvent(meetingSession.getPeerId(),meetingSessionEventToPeer);

            redisUtils.hDel(MEETING_USER_ACTIVITY ,meetingSession.getRequesterId());
            redisUtils.hDel(MEETING_USER_ACTIVITY ,meetingSession.getPeerId());
            throw new ServiceInternalException("自己必须在线状态");
        }
        // 再检查对方的状态
        if(!UserActiveStatusEnum.getAllOnLineCodeList().contains(userService.getUserActiveStatus(meetingSession.getPeerId()))){
            // 说明对方就是不在线的状态，对方无法接受
            meetingSession.setStatus(SessionStatusEnum.FAIL.getCode());
            meetingSession.setGmtMeetingEnd(DateUtils.getCurrentDateTime());
            redisUtils.hSet(MEETING_SESSION,sessionId,meetingSession); // 更新Session

            Event<MeetingSession> meetingSessionEventToRequester = Event.buildPersonal(EventEnum.EVENT_CALL_OFFLINE,meetingSession.getRequesterId(),meetingSession);
            sendPersonalEvent(meetingSession.getRequesterId(),meetingSessionEventToRequester);

            Event<MeetingSession> meetingSessionEventToPeer = Event.buildPersonal(EventEnum.EVENT_CALL_FAIL,meetingSession.getPeerId(),meetingSession);
            sendPersonalEvent(meetingSession.getPeerId(),meetingSessionEventToPeer);

            redisUtils.hDel(MEETING_USER_ACTIVITY ,meetingSession.getRequesterId());
            redisUtils.hDel(MEETING_USER_ACTIVITY ,meetingSession.getPeerId());
            throw new ServiceInternalException("对方必须在线状态");
        }
        return meetingSession;
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
                "/topic/meeting/notify/"+userId,
                shouting
        );
    }

}
