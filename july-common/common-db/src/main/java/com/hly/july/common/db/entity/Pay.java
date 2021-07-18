package com.hly.july.common.db.entity;

import java.math.BigDecimal;
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
 * @since 2021-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pay")
public class Pay implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("pay_id")
    private String payId;

    @TableField("from_id")
    private String fromId;

    @TableField("to_id")
    private String toId;

    @TableField("from_type")
    private Integer fromType;

    @TableField("to_type")
    private Integer toType;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("gmt_create")
    private Date gmtCreate;

    @TableField("gmt_done")
    private Date gmtDone;

    @TableField("status")
    private Integer status;

    @TableField("type")
    private Integer type;

    @TableField("leave_message")
    private String leaveMessage;


}
