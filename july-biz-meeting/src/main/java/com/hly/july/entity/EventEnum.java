package com.hly.july.entity;

import lombok.Data;

/**
 * @ClassName EventEnum
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/8 15:54
 * @Version 1.0.0
 **/
public enum EventEnum {

    EVENT_CHAT_JOIN("E1001", "新人加入"),
    EVENT_CHAT_LEAVE("E1002", "有人离开"),
    EVENT_CHAT_CHANGED("E1003", "Room被改变"),
    EVENT_CHAT_MESSAGE("E1004", "新消息");

    private String code;
    private String desc;

    EventEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }


    public String getMsg() {
        return desc;
    }
}
