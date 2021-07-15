package com.hly.july.common.core.constant;

/**
 * @author Linyuan Hou
 * @date 2021/5/12 21:17
 */
public interface JulyConstants {
    Integer USER_GENDER_FEMALE = 0;
    Integer USER_GENDER_MALE = 1;

    Integer DEFAULT_USER_GENDER = USER_GENDER_MALE;
    String DEFAULT_USER_AVATAR = "/default.jpeg";
    Integer DEFAULT_USER_STATUS = UserStatusEnum.USER_STATUS_NORMAL.getCode();

    String DEFAULT_LOGIN_BY_ACCOUNT = "email";

    long CHAT_MESSAGE_INSERT_ALLOW = 30L; //只接受前30分钟以内的message
    long CHAT_MESSAGE_ALIVE = 172900L; //设置每天的消息队列多久没更新后就会redis过期，48h

    int CHAT_MESSAGE_QUERY_ALLOW = 3; // 允许用户最多获取前三天的聊天记录

    int REDIS_WATCHER_EXPIRED = 30; // 用户在对话聊天框必须最多每个一段时间发送watch消息，说明自己在准备接收当前对话框的message
}
