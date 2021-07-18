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
@TableName("video")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("video_id")
    private String videoId;

    @TableField("author_id")
    private String authorId;

    @TableField("series_id")
    private String seriesId;

    @TableField("title")
    private String title;

    @TableField("tags")
    private String tags;

    @TableField("category_main")
    private String categoryMain;

    @TableField("category_sub")
    private String categorySub;

    @TableField("is_top")
    private Integer isTop;

    @TableField("gmt_create")
    private Date gmtCreate;

    @TableField("gmt_update")
    private Date gmtUpdate;

    @TableField("gmt_push")
    private Date gmtPush;

    @TableField("info")
    private String info;

    @TableField("status")
    private Integer status;

    @TableField("type")
    private Integer type;

    @TableField("password")
    private String password;

    @TableField("video_time_duration")
    private Long videoTimeDuration;

    @TableField("video_poster_id")
    private String videoPosterId;

    @TableField("video_file_id")
    private String videoFileId;

    @TableField("video_meta")
    private String videoMeta;

    @TableField("video_ratio")
    private String videoRatio;

    @TableField("thumb")
    private Long thumb;

    @TableField("score")
    private Float score;

    @TableField("view")
    private Long view;

    @TableField("video_format")
    private String videoFormat;


}
