package com.hly.july.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hly.july.common.biz.constant.ContainerEnum;
import com.hly.july.common.biz.constant.JulyConstants;
import com.hly.july.common.biz.entity.Message;
import com.hly.july.common.biz.entity.MessageItem;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.exception.ServiceInternalException;
import com.hly.july.common.biz.mapper.MessageMapper;
import com.hly.july.common.biz.mapper.UserMapper;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.common.biz.util.DateUtils;
import com.hly.july.common.biz.utils.MyBase64Utils;
import com.hly.july.common.biz.utils.RedisUtils;
import com.hly.july.common.biz.utils.SerializableUtils;
import com.hly.july.common.biz.vo.RelationVO;
import com.hly.july.entity.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.*;

/**
 * @ClassName MessageService
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/16 11:19
 * @Version 1.0.0
 **/
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IService<Message> {
    @Resource
    private RedisUtils redisUtils;
    // watcher只存在30s，过期说明客户端没在监听，离线了
    private int REDIS_WATCHER_EXPIRED = 30;
    private String REDIS_WATCHER = "chat_watcher_";
    private String REDIS_CHAT_MESSAGE = "chat_message_";
//    public Boolean upInsertMessage(MessageVO messageVO)  throws ServiceInternalException {
//        if(messageVO!=null&&messageVO.getFrom()!=null&&messageVO.getTo()!=null&&messageVO.getGmtCreate()!=null&& StringUtils.isNumeric(messageVO.getFrom())&& StringUtils.isNumeric(messageVO.getTo())){
//            String msgCreate_start = DateUtils.getDayStartTime("yyyy-MM-dd HH:mm:ss",DateFormatUtils.format(messageVO.getGmtCreate(),"yyyy-MM-dd HH:mm:ss"));
//            String label=null;
//            if(Long.parseLong(messageVO.getFrom())>Long.parseLong(messageVO.getTo())){
//                label= messageVO.getFrom()+"_"+messageVO.getTo();
//            }else{
//                label= messageVO.getTo()+"_"+messageVO.getFrom();
//            }
//            QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("label",label).apply("UNIX_TIMESTAMP(gmt_start) == UNIX_TIMESTAMP('" + msgCreate_start + "') limit 1");
//            List<Message> messageList = baseMapper.selectList(queryWrapper);
//            try {
//                if(messageList.size()>0){
//                    Message message = messageList.get(0);
//                    List<MessageItem> messageItems = (List<MessageItem>)SerializableUtils.byte2obj(message.getMessage().getBytes(1,(int)message.getMessage().length()));
//                    MessageItem
//                }
//            }catch (SQLException e){
//                e.printStackTrace();
//                throw new ServiceInternalException("转换聊天信息失败");
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new ServiceInternalException("获取聊天信息失败");
//            }
//
//
//        }
//    }

    public List<Message> getMessageByLabelAndNearCreateTime(String label, Date gmtCreate){
        String msgCreate_start = DateUtils.getDayStartTime("yyyy-MM-dd HH:mm:ss",DateFormatUtils.format(gmtCreate,"yyyy-MM-dd HH:mm:ss"));
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label",label).apply("UNIX_TIMESTAMP(gmt_start) == UNIX_TIMESTAMP('" + msgCreate_start + "') limit 1");
        List<Message> messageList = baseMapper.selectList(queryWrapper);
        return messageList;
    }

    public Boolean insertTodayMessage(MessageVO messageVO) throws ServiceInternalException{
        if(messageVO!=null&&messageVO.getFrom()!=null&&messageVO.getTo()!=null&&messageVO.getGmtCreate()!=null&&StringUtils.isNumeric(messageVO.getFrom())&&StringUtils.isNumeric(messageVO.getTo())) {
            String label=null;
            if (ContainerEnum.GROUP.getDesc().equals(messageVO.getPeerType())) {
                label = messageVO.getTo();
            }else{
                if (Long.parseLong(messageVO.getFrom()) > Long.parseLong(messageVO.getTo())) {
                    label = messageVO.getFrom() + "_" + messageVO.getTo();
                } else {
                    label = messageVO.getTo() + "_" + messageVO.getFrom();
                }
            }
            Date current_date = DateUtils.getCurrentDateTime();
            // 只接受当前30分钟以内的消息
            Date allow_date = DateUtils.addMinutes(current_date, -JulyConstants.CHAT_MESSAGE_INSERT_ALLOW);
            if (messageVO.getGmtCreate().before(allow_date)) {
                throw new ServiceInternalException("不接受" + JulyConstants.CHAT_MESSAGE_INSERT_ALLOW + "分钟之前的消息");
            }
            // 获取发送时间当天起始时间
            String messageStartDay = DateUtils.date2Str(messageVO.getGmtCreate(), DateUtils.DATE_FORMAT);
            log.info("insertTodayMessage messageStartDay:{} ", messageStartDay);
            if (redisUtils.hasKey(REDIS_CHAT_MESSAGE + messageStartDay)) {
                HashMap<String, ArrayList<MessageVO>> messageMap = (HashMap<String, ArrayList<MessageVO>>) redisUtils.get(REDIS_CHAT_MESSAGE + messageStartDay);
                ArrayList<MessageVO> messageVOList;
                if (messageMap.containsKey(label)){
                    messageVOList = messageMap.get(label);
                }else{
                    messageVOList = new ArrayList<>();
                }
                boolean isExisted = false;
                for (MessageVO vo : messageVOList) {
                    if(vo.equals(messageVO)){
                        isExisted = true;
                        break;
                    }
                }
                if(!isExisted){
                    messageVOList.add(messageVO);
                }
                messageMap.put(label,messageVOList);
                redisUtils.set(REDIS_CHAT_MESSAGE + messageStartDay,messageMap,JulyConstants.CHAT_MESSAGE_ALIVE);
            }else{
                Map<String, List<MessageVO>> messageMap = new HashMap<>();
                List<MessageVO> messageVOList = new ArrayList<>();
                messageVOList.add(messageVO);
                messageMap.put(label,messageVOList);
                redisUtils.set(REDIS_CHAT_MESSAGE + messageStartDay,messageMap,JulyConstants.CHAT_MESSAGE_ALIVE);
            }
            return true;
        }else{
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
    }

    public List<MessageVO> getMessageVOByIdAndDayCount(String userId,String peerId,Integer count,String type){
        if(StringUtils.isNumeric(userId)&&StringUtils.isNumeric(peerId)&&count!=null&&type!=null) {
            String label=null;
            if (ContainerEnum.GROUP.getDesc().equals(type)) {
                label = peerId;
            }else{
                if (Long.parseLong(userId) > Long.parseLong(peerId)) {
                    label = userId + "_" + peerId;
                } else {
                    label = peerId + "_" + userId;
                }
            }
            Date startDate = DateUtils.getDayStartTime(DateUtils.getCurrentDateTime());
            List<MessageVO> messageVOList = new ArrayList<>();
            int allowDay = JulyConstants.CHAT_MESSAGE_QUERY_ALLOW;
            if(count>allowDay){
                count=allowDay;
            }
            while (allowDay>=count&&count>0){
                // 获取发送时间当天起始时间
                String messageStartDay = DateUtils.date2Str(startDate,DateUtils.DATE_FORMAT);
                if (redisUtils.hasKey(REDIS_CHAT_MESSAGE + messageStartDay)) {
                    HashMap<String, ArrayList<MessageVO>> messageMap = (HashMap<String, ArrayList<MessageVO>>) redisUtils.get(REDIS_CHAT_MESSAGE + messageStartDay);
                    if (messageMap.containsKey(label)) {
                        messageVOList.addAll(messageMap.get(label));
                    }
                }else{
                    //Todo:redis没有，说明在数据库中，要从数据库中获取。
                }
                startDate = DateUtils.addMinutes(startDate,-1440l);
                count--;
            }
            // 降序排列，时间越近的，下标越小
            messageVOList.sort(new Comparator<MessageVO>() {
                @Override
                public int compare(MessageVO o1, MessageVO o2) {
                    if (o2.getGmtCreate().before(o1.getGmtCreate())){
                        return -1;
                    }else if (o2.getGmtCreate().equals(o1.getGmtCreate())){
                        return 0;
                    }else{
                        return 1;
                    }
                }
            });
            return messageVOList;
        }else {
            return null;
        }
    }
}
