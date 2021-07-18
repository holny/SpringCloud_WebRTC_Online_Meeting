package com.hly.july.common.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hly.july.common.core.validation.group.NewSeriesGroup;
import com.hly.july.common.core.validation.group.NewVideoGroup;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @ClassName SeriesInfoVO
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/17 15:18
 * @Version 1.0.0
 **/
@Data
public class SeriesInfoVO {

    private String seriesId;

    private String authorId;

    @NotBlank(message = "系列标题不能为空",groups = {NewSeriesGroup.class})
    @Size(max=25, message="系列标题长度最大25位",groups = {NewSeriesGroup.class})
    @Pattern(groups = {NewSeriesGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\u4e00-\\u9fa5\\.-]+){1,25}$)"
            ,message = "系列标题必须是字母或者汉字组成(最大25位)")
    private String seriesTitle;

    @Size(max=15, message="系列名称长度最大15位",groups = {NewSeriesGroup.class})
    @Pattern(groups = {NewSeriesGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\u4e00-\\u9fa5\\.-]+){1,15}$)"
            ,message = "系列名称必须是字母或者汉字组成(最大15位)")
    private String seriesName;


    @Size(max=150, message="系列标题长度最大150位",groups = {NewSeriesGroup.class})
    @Pattern(groups = {NewSeriesGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\u4e00-\\u9fa5\\.-]+){1,150}$)"
            ,message = "系列标题必须是字母或者汉字组成(最大150位)")
    private String seriesInfo;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtUpdate;

    private Integer status;

}
