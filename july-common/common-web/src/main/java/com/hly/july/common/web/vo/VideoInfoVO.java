package com.hly.july.common.web.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hly.july.common.core.constant.JulyConstants;
import com.hly.july.common.core.constant.UserStatusEnum;
import com.hly.july.common.core.util.JulyAuthorityUtils;
import com.hly.july.common.core.validation.group.NewVideoGroup;
import com.hly.july.common.core.validation.group.RegisterValidationGroup;
import com.hly.july.common.db.entity.Tag;
import com.hly.july.common.db.entity.User;
import com.hly.july.common.db.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

/**
 * @ClassName VideoUpload
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/16 14:32
 * @Version 1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfoVO implements Serializable {

    private String videoId;

    private String authorId;

    private String authorName;

    private String authorNickName;

    private String authorAvatar;

    private String authorRoleStr;

    private Set<String> authorRole;

    private Long authorExp;

    private String authorIdentification;

    private String authorIdentInfo;

    private Integer authorGenderCode;

    private String authorGender;

    private String seriesId;

    private String seriesTitle;

    private String seriesName;

    private String seriesInfo;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date seriesGmtCreate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date seriesGmtUpdate;


    @NotBlank(message = "标题不能为空",groups = {NewVideoGroup.class})
    @Size(max=25, message="标题长度最大25位",groups = {NewVideoGroup.class})
    @Pattern(groups = {NewVideoGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\u4e00-\\u9fa5\\.-]+){1,25}$)"
            ,message = "标题必须是字母或者汉字组成(最大25位)")
    private String title;

    private List<String> tags;
    private String tagStr;

    private List<Tag> tagList;

    private String categoryMain;

    private String categoryMainName;

    private String categorySub;

    private String categorySubName;

    private Boolean isNeedTop;

    private Boolean isNeedPush;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtUpdate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtPush;

    @Size(max=150, message="说明长度最大150位",groups = {NewVideoGroup.class})
    @Pattern(groups = {NewVideoGroup.class},regexp = "(^\\s?)|(^([a-zA-Z0-9_\\u4e00-\\u9fa5\\.-]+){1,150}$)"
            ,message = "标题必须是字母或者汉字组成(最大150位)")
    private String info;

    private Integer status;

    private Integer videoStatus;
    private Integer seriesStatus;

    private Integer authorStatusCode;
    private String authorStatus;

    private Integer type;

    private boolean isNeedPassword;

    private String password;

    private Long videoTimeDuration;

    private String videoPosterId;

    private String videoFileId;

    private String videoFormat;

    private String videoMeta;

    private String videoRatio;

    private Long videoThumb;

    private Float videoScore;

    private Long videoView;



    public VideoInfoVO(Video video){
        if(video != null) {
            this.videoId = video.getVideoId();
            this.authorId = video.getAuthorId();
            this.seriesId = video.getSeriesId();
            this.title = video.getTitle();
            if(StringUtils.isNotEmpty(video.getTags())){
                this.tags = Arrays.asList(video.getTags().split(","));
            }
            this.categoryMain = video.getCategoryMain();
            this.categorySub = video.getCategorySub();
            if(video.getIsTop()==1){
                this.isNeedTop = true;
            }else{
                this.isNeedTop = false;
            }
            this.gmtCreate = video.getGmtCreate();
            this.gmtUpdate = video.getGmtUpdate();
            this.gmtPush = video.getGmtPush();
            this.info = video.getInfo();
            this.status = video.getStatus();
            this.type = video.getType();
            if(StringUtils.isNotEmpty(video.getPassword())){
                this.isNeedPassword = true;
            }else{
                this.isNeedPassword = false;
            }
            this.videoTimeDuration = video.getVideoTimeDuration();
            this.videoPosterId = video.getVideoPosterId();
            this.videoFileId = video.getVideoFileId();
            this.videoMeta = video.getVideoMeta();
            this.videoRatio = video.getVideoRatio();
            this.videoThumb = video.getThumb();
            this.videoScore = video.getScore();
            this.videoView = video.getView();
        }
    }

    public VideoInfoVO(Video video,UserInfoVO userInfoVO){
        if(video != null) {
            this.videoId = video.getVideoId();
            this.authorId = video.getAuthorId();
            this.seriesId = video.getSeriesId();
            this.title = video.getTitle();
            if(StringUtils.isNotEmpty(video.getTags())){
                this.tags = Arrays.asList(video.getTags().split(","));
            }
            this.categoryMain = video.getCategoryMain();
            this.categorySub = video.getCategorySub();
            if(video.getIsTop()==1){
                this.isNeedTop = true;
            }else{
                this.isNeedTop = false;
            }
            this.gmtCreate = video.getGmtCreate();
            this.gmtUpdate = video.getGmtUpdate();
            this.gmtPush = video.getGmtPush();
            this.info = video.getInfo();
            this.status = video.getStatus();
            this.type = video.getType();
            if(StringUtils.isNotEmpty(video.getPassword())){
                this.isNeedPassword = true;
            }else{
                this.isNeedPassword = false;
            }
            this.videoTimeDuration = video.getVideoTimeDuration();
            this.videoPosterId = video.getVideoPosterId();
            this.videoFileId = video.getVideoFileId();
            this.videoMeta = video.getVideoMeta();
            this.videoRatio = video.getVideoRatio();
            this.videoThumb = video.getThumb();
            this.videoScore = video.getScore();
            this.videoView = video.getView();
            if(userInfoVO!=null){
                this.authorName=userInfoVO.getUserName();
                this.authorNickName = userInfoVO.getNickName();
                this.authorAvatar=userInfoVO.getAvatar();
            }
        }
    }

}
