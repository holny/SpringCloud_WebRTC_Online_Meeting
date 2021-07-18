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
 * @since 2021-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("tag_id")
    private String tagId;

    @TableField("tag_name")
    private String tagName;

    @TableField("tag_author_id")
    private String tagAuthorId;

    @TableField("gmt_create")
    private Date gmtCreate;

    @TableField("status")
    private Integer status;

    @TableField("type")
    private Integer type;


}
