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
@TableName("series")
public class Series implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("series_id")
    private String seriesId;

    @TableField("author_id")
    private String authorId;

    @TableField("series_title")
    private String seriesTitle;

    @TableField("series_name")
    private String seriesName;

    @TableField("series_info")
    private String seriesInfo;

    @TableField("gmt_create")
    private Date gmtCreate;

    @TableField("gmt_update")
    private Date gmtUpdate;

    @TableField("status")
    private Integer status;


}
