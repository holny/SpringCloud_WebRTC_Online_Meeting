package com.hly.july.schedule;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hly.july.common.biz.constant.ContainerEnum;
import com.hly.july.common.biz.constant.UserStatusEnum;
import com.hly.july.common.biz.entity.Message;
import com.hly.july.common.biz.util.DateUtils;
import com.hly.july.common.biz.utils.MyBase64Utils;
import com.hly.july.common.biz.utils.RedisUtils;
import com.hly.july.common.biz.utils.SerializableUtils;
import com.hly.july.entity.MessageVO;
import com.hly.july.service.impl.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @ClassName PersistSchedule
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/16 16:25
 * @Version 1.0.0
 **/
@Slf4j
@Component
@EnableScheduling//可以在启动类上注解也可以在当前文件
public class PersistSchedule {
    @Resource
    private RedisUtils redisUtils;

    @Autowired
    private MessageServiceImpl messageService;
    // watcher只存在30s，过期说明客户端没在监听，离线了
    private int REDIS_WATCHER_EXPIRED = 30;
    private String REDIS_WATCHER = "chat_message_";

    /**
     * fixedDelay单位毫秒 1000=1秒
     */
//    @Scheduled(fixedDelay=600000)
//    private void saveLocal() throws UnsupportedEncodingException {
//        String msgCreate_start = DateUtils.getDayStartTime("yyyy-MM-dd", DateFormatUtils.format(DateUtils.getCurrentDateTime(),"yyyy-MM-dd HH:mm:ss"));
//        if(redisUtils.hasKey(REDIS_WATCHER+msgCreate_start)){
//            Map<String,List<MessageVO>> messageMap = (Map<String,List<MessageVO>>)redisUtils.get(REDIS_WATCHER+msgCreate_start);
//            Map<String, Message> labelMessageMap = new HashMap<>();
//            for (String peerId : messageMap.keySet()) {
//                List<MessageVO> messageVOList = messageMap.get(peerId);
//                for (MessageVO messageVO : messageVOList) {
//                    String label=null;
//                    if(Long.parseLong(messageVO.getFrom())>Long.parseLong(messageVO.getTo())){
//                        label= messageVO.getFrom()+"_"+messageVO.getTo();
//                    }else{
//                        label= messageVO.getTo()+"_"+messageVO.getFrom();
//                    }
//                    if(labelMessageMap.containsKey(label)){
//                        Message message = labelMessageMap.get(label);
//                        if(StringUtils.isNotEmpty(message.getMessage())){
//                            try {
//                                String messageBase64 = MyBase64Utils.springEncodeString(messageVO.getMessage());
//                                long sendTS = messageVO.getGmtCreate().getTime();
//                                String messageFrag = "";
//                                messageFrag="<"+messageVO.getFrom()+"_"+sendTS+":"+messageBase64+">";
//
//                            }catch (UnsupportedEncodingException e){
//
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//    }

    @Scheduled(fixedDelay=600000)
    private void saveMessageYesterdayLocal() {
        String msgCreate_start_yesterday = DateUtils.getBeforeDayOfDate(DateUtils.DATE_FORMAT,DateUtils.getCurrentTime(DateUtils.DATE_FORMAT),1);
        Date msgCreate_start_yesterday_date = DateUtils.str2Date(msgCreate_start_yesterday,DateUtils.DATE_FORMAT);
//        String msgCreate_start_today = DateUtils.getBeforeDayOfDate(DateUtils.DATE_FORMAT,DateUtils.getCurrentTime(DateUtils.DATE_FORMAT_DEFAULT),1);
//        Date msgCreate_start_today_date = DateUtils.str2Date(msgCreate_start_today,DateUtils.DATE_FORMAT);
        if(redisUtils.hasKey(REDIS_WATCHER+msgCreate_start_yesterday)){
            HashMap<String, ArrayList<MessageVO>> messageMap = (HashMap<String, ArrayList<MessageVO>>)redisUtils.get(REDIS_WATCHER+msgCreate_start_yesterday);
            HashMap<String, ArrayList<MessageVO>> labelMessageMap = new HashMap<>();
            for (String peerId : messageMap.keySet()) {
                ArrayList<MessageVO> messageVOList = messageMap.get(peerId);
                for (MessageVO messageVO : messageVOList) {
                    // 先把发送者和接收者的Id按照从小到大顺序组合成Label
                    String label=null;
                    if(Long.parseLong(messageVO.getFrom())>Long.parseLong(messageVO.getTo())){
                        label= messageVO.getFrom()+"_"+messageVO.getTo();
                    }else{
                        label= messageVO.getTo()+"_"+messageVO.getFrom();
                    }
                    //然后按照Label,发送时间，消息分类
                    ArrayList<MessageVO> tempMessageVOList;
                    if(labelMessageMap.containsKey(label)){
                        tempMessageVOList = labelMessageMap.get(label);
                    }else{
                        tempMessageVOList = new ArrayList<>();
                    }
                    tempMessageVOList.add(messageVO);
                    labelMessageMap.put(label,tempMessageVOList);
                }
            }
            //开始保存
            for (String label : labelMessageMap.keySet()) {
                ArrayList<MessageVO> messageVOList = labelMessageMap.get(label);
                Set<String> participantSet = new HashSet<>();
                String type = "";
                for (MessageVO messageVO : messageVOList) {
                    participantSet.add(messageVO.getFrom());
                    participantSet.add(messageVO.getTo());
                    if(StringUtils.isNotEmpty(messageVO.getPeerType())){
                        Integer code = ContainerEnum.getCodeByDesc(messageVO.getPeerType());
                        if(code!=null){
                            type = code.toString();
                        }
                    }
                }
                ArrayList<String> participantList = new ArrayList<>(participantSet);
                participantList.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o2.compareTo(o1);
                    }
                });
                String participantStr = "";
                for (String s : participantList) {
                    participantStr+=s+";";
                }
                List<Message> messageByLabelAndNearCreateTime = messageService.getMessageByLabelAndNearCreateTime(label, msgCreate_start_yesterday_date);
                Message existedMessage;
                if(messageByLabelAndNearCreateTime.size()>0){
                    existedMessage = messageByLabelAndNearCreateTime.get(0);
                    existedMessage.setLabel(null);
                    existedMessage.setParticipant(null);
                    existedMessage.setGmtStart(null);
                    existedMessage.setType(null);
                    existedMessage.setGmtEnd(DateUtils.getCurrentDateTime());
                    existedMessage.setStatus(null);
                }else{
                    existedMessage = new Message();
                    Long newId= IdWorker.getId();
                    existedMessage.setMsgId(newId.toString());
                    existedMessage.setLabel(label);
                    existedMessage.setParticipant(participantStr);
                    existedMessage.setGmtStart(msgCreate_start_yesterday_date);
                    existedMessage.setType(type);
                    existedMessage.setGmtEnd(DateUtils.getCurrentDateTime());
                    existedMessage.setStatus(UserStatusEnum.USER_STATUS_NORMAL.getCode().toString());
                }
                try {
                    byte[] bytes = SerializableUtils.obj2byte(messageVOList);
                    existedMessage.setMessage(bytes);
                    messageService.saveOrUpdate(existedMessage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
