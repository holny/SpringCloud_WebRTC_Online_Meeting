package com.hly.july.common.db.entity;

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
 * @since 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("container")
public class Container implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("container_id")
    private String containerId;

    @TableField("container_type")
    private Integer containerType;

    @TableField("content_id")
    private String contentId;

    @TableField("content_type")
    private Integer contentType;

    @TableField("authority")
    private String authority;

    @TableField("status")
    private String status;


}
