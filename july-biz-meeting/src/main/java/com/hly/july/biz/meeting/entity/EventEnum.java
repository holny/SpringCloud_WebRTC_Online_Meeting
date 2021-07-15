package com.hly.july.biz.meeting.entity;

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
    EVENT_CHAT_MESSAGE("E1004", "新消息"),
    EVENT_RECENT_CHANGED("E1005", "最近联系人列表改变"),
    EVENT_UNREAD_CHANGED("E1006", "未读信息计数改变"),
    EVENT_CONSPICUOUS_NOTIFY("E1007", "显示消息"),
    EVENT_CALL_RING("E2001", "呼叫响铃"),
    EVENT_CALL_IN("E2002", "有新呼叫"),
    EVENT_CALL_FAIL("E2003", "呼叫失败"),
    EVENT_CALL_BUSY("E2004", "对方忙"),
    EVENT_CALL_OFFLINE("E2005", "对方不在线"),
    EVENT_CALL_DENY("E2006", "呼叫被拒绝"),
    EVENT_CALL_PRE_CONNECT("E2007", "会话连接建立中"),
    EVENT_CALL_CONNECTED("E2008", "呼叫双方通话中"),
    EVENT_CALL_HANGUP("E2009", "呼叫对放挂断");

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
