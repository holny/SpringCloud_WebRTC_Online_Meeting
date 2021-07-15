package com.hly.july.biz.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

/**
 * @ClassName MeetingSession
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/23 10:21
 * @Version 1.0.0
 **/
@Data
public class MeetingSession<T> {
    private String requesterId;
    private String peerId;
    private String sessionId;
    private String meetingId;
//    private List<String> handShakeSeq;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtRequestCreate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtMeetingStart;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtMeetingEnd;
    private Integer status;
    private T data;
    private Map<String,Object> signalingMap;

    public void addSignalingItem(String key,Object value){
        if(this.signalingMap==null){
            this.signalingMap  = new HashMap<>();
        }
        this.signalingMap.put(key,value);
    }
}
