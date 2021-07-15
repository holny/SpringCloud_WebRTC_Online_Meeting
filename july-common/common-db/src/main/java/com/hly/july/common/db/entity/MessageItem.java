package com.hly.july.common.db.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName MessageItem
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/16 14:52
 * @Version 1.0.0
 **/
@Data
public class MessageItem implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    private String sender;
    private String message;
    private Date sendTime;
}
