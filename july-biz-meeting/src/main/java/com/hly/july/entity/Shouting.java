package com.hly.july.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName Shouting
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/8 18:09
 * @Version 1.0.0
 **/

public class Shouting {
    private String type;
    private Date gmtCreate;
    private String method;
    private String to;

    public Shouting() {
    }

    public Shouting(String to,String type, Date gmtCreate, String method) {
        this.to = to;
        this.type = type;
        this.gmtCreate = gmtCreate;
        this.method = method;
    }
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
