package com.hly.july.entity;

import com.hly.july.common.biz.vo.RelationVO;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
    private Date gmtRequestCreate;
    private Date gmtMeetingStart;
    private Date gmtMeetingEnd;
    private Integer status;
    private T data;
}
