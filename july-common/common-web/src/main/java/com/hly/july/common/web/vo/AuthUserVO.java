package com.hly.july.common.web.vo;

import com.hly.july.common.core.validation.group.LoginValidationGroup;
import com.hly.july.common.core.validation.group.RegisterValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Linyuan Hou
 * @date 2021/5/7 18:03
 */
@Data
public class AuthUserVO  implements Serializable {
    @NotBlank(message = "用户名不能为空",groups = {RegisterValidationGroup.class})
    @Size(max=10, message="用户名长度最大10")
    @Pattern(groups = {RegisterValidationGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\.-]+){5,10}$)|(^([\\u4e00-\\u9fa5\\.]+){2,10}$)"
            ,message = "用户名必须是字母或者汉字组成(最大10位)")
    private String userName;

    @Size(max=10, message="用户别名长度最大10")
    @Pattern(groups = {RegisterValidationGroup.class, LoginValidationGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\.-]+){5,10}$)|(^([\\u4e00-\\u9fa5\\.]+){2,10}$)"
            ,message = "用户名必须是字母或者汉字组成(最大10位)")
    private String nickName;

    @NotBlank(message = "手机号码不能为空",groups = {RegisterValidationGroup.class})
    @Pattern(groups = {RegisterValidationGroup.class},regexp = "(^\\s?)|(^1[3456789]\\d{9}$)"
            ,message = "手机号码不符合规范(11位)")
    private String phoneNumber;

    @NotBlank(message = "邮箱地址不能为空",groups = {RegisterValidationGroup.class})
//    @Pattern(groups = {RegisterValidationGroup.class, LoginValidationGroup.class},regexp = "/^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$/"
//            ,message = "邮箱地址不符合规范")
    private String email;

    @NotBlank(message = "密码不能为空",groups = {RegisterValidationGroup.class, LoginValidationGroup.class})
    @Size(max=15, message="密码长度最大15")
    @Pattern(groups = {RegisterValidationGroup.class, LoginValidationGroup.class},regexp = "(^\\s?)|(^[a-zA-Z0-9_-]{0,15}$)"
            ,message = "密码不符合规范")
    private String password;

    private Boolean rememberMe;

    @Size(max=5, message="注册码长度最大5")
    @Pattern(regexp = "(^\\s?)|(^[a-zA-Z0-9_-]{0,5}$)"
            ,message = "注册码不符合规范")
    private String registerCode;
}
