package com.hly.july.common.web.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hly.july.common.core.validation.group.NewSeriesGroup;
import com.hly.july.common.core.validation.group.NewTagGroup;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @ClassName TagInfoVO
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/17 16:35
 * @Version 1.0.0
 **/
@Data
public class TagInfoVO {
    private static final long serialVersionUID = 1L;

    private String tagId;

    @NotBlank(message = "标签名不能为空",groups = {NewTagGroup.class})
    @Size(max=8, message="标签名长度最大8位",groups = {NewTagGroup.class})
    @Pattern(groups = {NewTagGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\u4e00-\\u9fa5\\.-]+){1,8}$)"
            ,message = "标签名必须是字母或者汉字组成(最大8位)")
    private String tagName;

    private String tagAuthorId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    private Integer status;

    private Integer type;

    private String typeStr;
}
