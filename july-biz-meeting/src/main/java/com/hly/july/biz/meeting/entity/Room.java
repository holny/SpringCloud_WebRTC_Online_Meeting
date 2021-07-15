package com.hly.july.biz.meeting.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @ClassName Room
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/7 21:36
 * @Version 1.0.0
 **/
@Data
public class Room implements Serializable {
    private String roomName;
    private String roomId;
    private String dominatorId;
    private String type;
    private String status;
    private Set<String> adminIdSet;
    private Set<String> memberIdSet;
    private Set<String> blackIdSet;
    private Integer maxMemberCount;;
    private Date gmtCreate;
}
