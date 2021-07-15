package com.hly.july.biz.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Shouting
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/8 18:09
 * @Version 1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shouting implements Serializable {
    private String type;
    private String peerType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    private String method;
    private String to;

    public Shouting(String to,String type, Date gmtCreate, String method) {
        this.to = to;
        this.type = type;
        this.gmtCreate = gmtCreate;
        this.method = method;
    }
    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeerType() {
        return this.peerType;
    }

    public void setPeerType(String peerType) {
        this.peerType = peerType;
    }


    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
