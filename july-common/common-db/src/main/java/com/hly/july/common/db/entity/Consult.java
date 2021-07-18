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
@TableName("consult")
public class Consult implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("consult_id")
    private String consultId;

    @TableField("customer_id")
    private String customerId;

    @TableField("expert_id")
    private String expertId;

    @TableField("tags")
    private String tags;

    @TableField("leave_message")
    private String leaveMessage;

    @TableField("gmt_create")
    private Date gmtCreate;

    @TableField("gmt_start")
    private Date gmtStart;

    @TableField("gmt_start_actual")
    private Date gmtStartActual;

    @TableField("gmt_stop_actual")
    private Date gmtStopActual;

    @TableField("duration")
    private Integer duration;

    @TableField("duration_actual")
    private Integer durationActual;

    @TableField("cost")
    private BigDecimal cost;

    @TableField("pay_id")
    private String payId;

    @TableField("status")
    private Integer status;


}
