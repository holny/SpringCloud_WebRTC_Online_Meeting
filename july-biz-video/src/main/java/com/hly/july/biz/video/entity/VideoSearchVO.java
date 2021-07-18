package com.hly.july.biz.video.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hly.july.common.core.validation.group.NewVideoGroup;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @ClassName VideoSearchVO
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/18 00:23
 * @Version 1.0.0
 **/
@Data
public class VideoSearchVO {
    private String videoId;
    private String authorId;
    private String seriesId;
    private String categoryMainId;
    private String categorySubId;
    private Integer isTop;
    private List<Integer> videoStatusList;
    private List<Integer> videoTypeList;
    private List<Integer> seriesStatusList;
    @Size(max=20, message="搜索长度最大150位",groups = {NewVideoGroup.class})
    @Pattern(groups = {NewVideoGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\u4e00-\\u9fa5\\.-]+){1,20}$)"
            ,message = "搜索必须是字母或者汉字组成(最大20位)")
    private String search;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtUpdate;
    private String orderByClause;
    private Boolean isDesc;
    private Long currentPageNum;
    private Long pageLimit;
}
