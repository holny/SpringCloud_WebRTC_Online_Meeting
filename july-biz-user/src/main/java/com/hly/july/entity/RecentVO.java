package com.hly.july.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName RecentDO
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/15 16:07
 * @Version 1.0.0
 **/
@Data
public class RecentVO {
    private String userId;
    private String peerId;
    private String peerType;
    private Date gmtLastContact;
}
