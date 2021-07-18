package com.hly.july.common.db.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("expert")
public class Expert implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("expert_id")
    private String expertId;

    @TableField("pre_note")
    private String preNote;

    @TableField("status")
    private Integer status;

    @TableField("accept_date")
    private String acceptDate;

    @TableField("accept_time")
    private String acceptTime;

    @TableField("unit_cost")
    private BigDecimal unitCost;

    @TableField("type")
    private Integer type;

    @TableField("tags")
    private String tags;

    @TableField("category_main")
    private String categoryMain;

    @TableField("category_sub")
    private String categorySub;

    @TableField("password")
    private String password;


}
