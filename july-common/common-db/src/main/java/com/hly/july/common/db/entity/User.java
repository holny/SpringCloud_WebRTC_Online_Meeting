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
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("user_id")
    private String userId;

    @TableField("user_name")
    private String userName;

    @TableField("password")
    private String password;

    @TableField("gender")
    private Integer gender;

    @TableField("phone_number")
    private String phoneNumber;

    @TableField("email")
    private String email;

    @TableField("avatar")
    private String avatar;

    @TableField("gmt_create")
    private Date gmtCreate;

    @TableField("gmt_update")
    private Date gmtUpdate;

    @TableField("gmt_last_login")
    private Date gmtLastLogin;

    @TableField("status")
    private Integer status;

    @TableField("gmt_birthday")
    private Date gmtBirthday;

    @TableField("nick_name")
    private String nickName;

    @TableField("role")
    private String role;

    @TableField("authority")
    private String authority;

    @TableField("exp")
    private Long exp;

    @TableField("last_login_ip")
    private String lastLoginIp;

    @TableField("identification")
    private String identification;

    @TableField("ident_info")
    private String identInfo;

    @TableField("balance")
    private BigDecimal balance;


}
