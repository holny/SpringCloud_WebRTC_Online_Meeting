package com.hly.july.common.biz.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.constant.JulyConstants;
import com.hly.july.common.constant.RoleEnum;
import com.hly.july.common.constant.UserStatusEnum;
import com.hly.july.common.util.JulyAuthorityUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Linyuan Hou
 * @date 2021/5/11 17:33
 */
@Data
public class UserInfoVO {

    private Long userId;

    private String userName;

    @JSONField(serialize = false)
    private String password;

    private String gender;

    private String phoneNumber;

    private String email;

    private String avatar;

    private Date gmtCreate;

    private Date gmtLastLogin;

    private String status;

    private Date gmtBirthday;

    private String nickName;

    private Set<String> role;

    private Map<String,Set<String>> authority;

    private Object token;

    public UserInfoVO(User user){
        if(user != null) {
            this.userId = user.getUserId();
            this.userName = user.getUserName();
            this.password = user.getPassword();
            if(JulyConstants.USER_GENDER_FEMALE.equals(user.getGender())){
                this.gender = "female";
            }else if(JulyConstants.USER_GENDER_MALE.equals(user.getGender())){
                this.gender = "male";
            }else{
                this.gender = "male";
            }
            this.phoneNumber = user.getPhoneNumber();
            this.email = user.getEmail();
            this.avatar = user.getAvatar();
            this.gmtCreate = user.getGmtCreate();
            this.gmtLastLogin = user.getGmtUpdate();
            this.status = UserStatusEnum.getDescByCode(user.getStatus());
            this.gmtBirthday = user.getGmtBirthday();
            this.nickName = user.getNickName();
            this.role = JulyAuthorityUtils.roleClassifyString2Set(user.getRole());
            this.authority = JulyAuthorityUtils.authorityClassifyString2Map(user.getAuthority());
        }
    }

    public UserInfoVO(User user,Object token){
        if(user != null) {
            this.userId = user.getUserId();
            this.userName = user.getUserName();
            this.password = user.getPassword();
            if(JulyConstants.USER_GENDER_FEMALE.equals(user.getGender())){
                this.gender = "female";
            }else if(JulyConstants.USER_GENDER_MALE.equals(user.getGender())){
                this.gender = "male";
            }else{
                this.gender = "male";
            }
            this.phoneNumber = user.getPhoneNumber();
            this.email = user.getEmail();
            this.avatar = user.getAvatar();
            this.gmtCreate = user.getGmtCreate();
            this.gmtLastLogin = user.getGmtUpdate();
            this.status = UserStatusEnum.getDescByCode(user.getStatus());
            this.gmtBirthday = user.getGmtBirthday();
            this.nickName = user.getNickName();
            this.role = JulyAuthorityUtils.roleClassifyString2Set(user.getRole());
            this.authority = JulyAuthorityUtils.authorityClassifyString2Map(user.getAuthority());
        }
        if(token!=null){
            this.token = token;
        }
    }
}
