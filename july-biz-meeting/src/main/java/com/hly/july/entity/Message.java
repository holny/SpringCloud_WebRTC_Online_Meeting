package com.hly.july.entity;

import com.hly.july.common.util.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Message
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/7 19:57
 * @Version 1.0.0
 **/

@Data
public class Message extends Shouting implements Serializable {
    private String from;
    private String message;

    public Message(String from, String to, String message, String method) {
        super(to,"message", DateUtils.getCurrentDateTime(),method);
        this.from = from;
        this.message = message;
    }

}
