package com.hly.july.biz.meeting.entity;

import com.hly.july.common.core.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Message
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/7 19:57
 * @Version 1.0.0
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageVO extends Shouting implements Serializable {
    private String from;
    private String message;
    private String status;

    public MessageVO(String from, String to, String message, String method) {
        super(to,"message", DateUtils.getCurrentDateTime(),method);
        this.from = from;
        this.message = message;
    }

    public boolean equals(MessageVO other){
        if(other!=null){
            if(this.getFrom()!=null&&other.getFrom()!=null&&this.getFrom().equals(other.getFrom())){
                if(this.getTo()!=null&&other.getTo()!=null&&this.getTo().equals(other.getTo())){
                    if(this.getGmtCreate()!=null&&other.getGmtCreate()!=null&&this.getGmtCreate().equals(other.getGmtCreate())){
                        if(this.getMessage()!=null&&other.getMessage()!=null&&this.getMessage().trim().equals(other.getMessage().trim())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
