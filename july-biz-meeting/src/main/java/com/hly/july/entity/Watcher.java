package com.hly.july.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName Watcher
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/16 10:21
 * @Version 1.0.0
 **/
@Data
public class Watcher {
    private String watcherId;
    private String peerId;
    private String peerType;
    private String action;
    private Date gmtWatch;
}
