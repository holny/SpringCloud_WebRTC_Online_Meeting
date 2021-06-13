package com.hly.july.common.biz.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName SocialVO
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/13 18:56
 * @Version 1.0.0
 **/
@Data
public class SocialVO {

    private String socialId;

    private String userId;

    private String peerId;

    private String socialType;

    @JSONField(serialize = false)
    private Date gmtCreate;

    private Date gmtLastContact;

    private String remarkName;

    private String tag;

    private String peerUserName;

    private String peerGender;

    @JSONField(serialize = false)
    private String peerPhoneNumber;

    @JSONField(serialize = false)
    private String peerEmail;

    private String peerAvatar;

    @JSONField(serialize = false)
    private Date peerGmtLastLogin;

    private String peerStatus;

    @JSONField(serialize = false)
    private Date peerGmtBirthday;

    private String peerNickName;

    private Set<String> peerRole;

    @JSONField(serialize = false)
    private Map<String,Set<String>> peerAuthority;
}
