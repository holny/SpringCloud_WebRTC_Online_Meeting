package com.hly.july.biz.meeting.service.impl;

import com.hly.july.biz.meeting.entity.Event;
import com.hly.july.biz.meeting.entity.EventEnum;
import com.hly.july.biz.meeting.service.api.BizUserApiService;

import com.hly.july.common.core.constant.ContainerEnum;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.web.vo.RecentVO;
import com.hly.july.common.web.vo.RelationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName AsyncJobService
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/18 14:50
 * @Version 1.0.0
 **/
@Service
@Slf4j
public class AsyncJobService {
    @Resource
    private BizUserApiService bizUserApiService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    /**
     * 用户发送消息后续处理动作：
     * 1.更新双方最近联系人状态(最后联系时间)。
     * 2.更新收信人的未读消息数
     * 3.整合以上信息发送notify给发信人和收信人
     * @param senderId
     * @param senderType
     * @param receiverId
     * @return
     */
    @Async("taskExecutor")
    public Boolean sendMsgSubProcess(String senderId, Integer senderType, String receiverId,Integer receiverType, Date gmtMsgDate,boolean isNotify){
        log.info("sendMsgSubProcess sender:{},senderType:{},receiver:{},receiverType:{},date:{},isNotify:{}",senderId,senderType,receiverId,receiverType,gmtMsgDate,isNotify);
        if(isNotify) {
            // 获取收件人的未读信息数
            chatService.changeUnRead(senderId, senderType, receiverId, "update");
        }
        // 发件人的最近联系人
        RecentVO recentSender = new RecentVO();
        recentSender.setUserId(senderId);
        recentSender.setPeerId(receiverId);
        recentSender.setPeerType(ContainerEnum.getDescByCode(receiverType));
        recentSender.setGmtLastContact(gmtMsgDate);
        Result<List<RelationVO>> senderResult =  bizUserApiService.upInsertRecentContact(senderId,recentSender);
        if (senderResult.getCode() != ResultCode.SUCCESS.getCode()) {
            log.warn("changeUnReadAndNotify senderResult fail:{}", senderResult.toString());
        }
        // 收件人的最近联系人
        RecentVO recentReceiver = new RecentVO();
        recentReceiver.setUserId(receiverId);
        recentReceiver.setPeerId(senderId);
        recentReceiver.setPeerType(ContainerEnum.getDescByCode(senderType));
        recentReceiver.setGmtLastContact(gmtMsgDate);
        Result<List<RelationVO>> receiverResult =  bizUserApiService.upInsertRecentContact(receiverId,recentReceiver);
        if(isNotify) {
            if (receiverResult.getCode() == ResultCode.SUCCESS.getCode()) {
                List<RelationVO> receiverRelationVOList = receiverResult.getData();
                Event<List<RelationVO>> event = Event.buildPersonal(EventEnum.EVENT_RECENT_CHANGED, receiverId, receiverRelationVOList);
                event.setGmtCreate(gmtMsgDate);
                userService.sendPersonalEvent(receiverId, event);
                return true;

            } else {
                log.warn("changeUnReadAndNotify receiverResult fail:{}", receiverResult.toString());
                return false;
            }
        }else{
            log.info("process done ,not notify receiver:{}",receiverId);
            return true;
        }
    }

    @Async("taskExecutor")
    public Boolean sendRecentListNotify(String userId, Date gmtMsgDate){
        Result<List<RelationVO>> receiverResult =  bizUserApiService.getUserRecentRelation(userId);
        if(receiverResult!=null&&receiverResult.getCode()==ResultCode.SUCCESS.getCode()){
            List<RelationVO> receiverRelationVOList = receiverResult.getData();
            Event<List<RelationVO>> event = Event.buildPersonal(EventEnum.EVENT_RECENT_CHANGED, userId, receiverRelationVOList);
            event.setGmtCreate(gmtMsgDate);
            userService.sendPersonalEvent(userId, event);
            return true;
        }else{
            log.info("process done ,not notify receiver:{}",userId);
            return true;
        }
    }

}
