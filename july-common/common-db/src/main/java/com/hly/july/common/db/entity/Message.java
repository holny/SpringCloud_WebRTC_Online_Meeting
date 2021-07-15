package com.hly.july.common.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author Linyuan Hou
 * @since 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("msg_id")
    private String msgId;

    @TableField("label")
    private String label;

    @TableId("gmt_start")
    private Date gmtStart;

    @TableField("gmt_end")
    private Date gmtEnd;

    @TableField("message")
    private byte[] message;

    @TableField("participant")
    private String participant;

    @TableField("type")
    private String type;

    @TableField("status")
    private String status;


}
