package com.hly.july.entity;

import com.hly.july.common.biz.util.DateUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Message
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/7 19:57
 * @Version 1.0.0
 **/
@Data
public class Event<T> extends Shouting implements Serializable {
    private String code;
    private T data;
    private String message;

    public Event(String to,String method, String code, String message,T data) {
        super(to,"event", DateUtils.getCurrentDateTime(),method);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Event<T> build(EventEnum eventType,String peerId,String method,T data) {
        Event event = new Event(peerId,method,eventType.getCode(),eventType.getMsg(),data);
        return event;
    }

    public static <T> Event<T> buildBroadCast(EventEnum eventType,String roomId,T data) {
        Event event = new Event(roomId,"broadcast",eventType.getCode(),eventType.getMsg(),data);
        return event;
    }

    public static <T> Event<T> buildPersonal(EventEnum eventType,String peerId,T data) {
        Event event = new Event(peerId,"personal",eventType.getCode(),eventType.getMsg(),data);
        return event;
    }
}
