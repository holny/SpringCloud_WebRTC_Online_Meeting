package com.hly.july.common.biz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hly.july.common.biz.constant.JulyConstants;
import com.hly.july.common.biz.constant.UserStatusEnum;
import com.hly.july.common.biz.entity.User;

import com.hly.july.common.biz.util.JulyAuthorityUtils;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author Linyuan Hou
 * @date 2021/5/11 17:33
 */
@Data
public class UserInfoVO  implements Serializable {

    private String userId;

    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String gender;

    private String phoneNumber;

    private String email;

    private String avatar;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtLastLogin;

    private String status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtBirthday;

    private String nickName;

    private Set<String> role;

    private Map<String,Set<String>> authority;

    private Map<String,Object> token;

    public UserInfoVO(User user){
        if(user != null) {
            this.userId = user.getUserId().toString();
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

    public UserInfoVO(User user,Map<String,Object> token){
        if(user != null) {
            this.userId = user.getUserId().toString();
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
