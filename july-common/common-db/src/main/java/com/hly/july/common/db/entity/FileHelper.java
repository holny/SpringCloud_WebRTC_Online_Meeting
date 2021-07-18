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
@TableName("file_helper")
public class FileHelper implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("file_id")
    private String fileId;

    @TableField("uploader_id")
    private String uploaderId;

    @TableField("type")
    private Integer type;

    @TableField("save_path")
    private String savePath;

    @TableField("file_name")
    private String fileName;

    @TableField("file_suffix")
    private String fileSuffix;

    @TableField("gmt_create")
    private Date gmtCreate;

    @TableField("gmt_update")
    private Date gmtUpdate;

    @TableField("status")
    private Integer status;

    @TableField("server_key")
    private String serverKey;

    @TableField("server_path")
    private String serverPath;

    @TableField("file_size")
    private Long fileSize;

    @TableField("file_md5")
    private String fileMd5;


}
