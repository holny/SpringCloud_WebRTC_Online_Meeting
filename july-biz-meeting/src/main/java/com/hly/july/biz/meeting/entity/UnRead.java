package com.hly.july.biz.meeting.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName UnRead
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/18 15:33
 * @Version 1.0.0
 **/
@Data
public class UnRead {
    private String senderId;
    private String senderType;
    private String receiverId;
    private Long unReadCount;
    private Date gmtLastRead;
    private Date gmtUpdate;
}
