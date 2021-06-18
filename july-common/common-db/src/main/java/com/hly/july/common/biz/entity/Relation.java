package com.hly.july.common.biz.entity;

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
 * @since 2021-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("relation")
public class Relation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("rel_id")
    private String relId;

    @TableId("user_id")
    private String userId;

    @TableField("peer_id")
    private String peerId;

    @TableField("remark_name")
    private String remarkName;

    @TableField("peer_type")
    private String peerType;

    @TableField("gmt_create")
    private Date gmtCreate;

    @TableField("tag")
    private String tag;

    @TableField("rel_type")
    private Integer relType;


}
