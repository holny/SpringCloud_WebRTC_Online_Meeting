package com.hly.july.common.core.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Linyuan Hou
 * @date 2021/5/12 10:07
 */

public enum AuthorityEnum {
    USER_VIEW(ResourceEnum.RESOURCE_USER.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.VIEW.getCode(), "USER_VIEW"),
    USER_CREATE(ResourceEnum.RESOURCE_USER.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.CREATE.getCode(), "USER_CREATE"),
    USER_UPDATE(ResourceEnum.RESOURCE_USER.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.UPDATE.getCode(), "USER_UPDATE"),
    USER_DELETE(ResourceEnum.RESOURCE_USER.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.DELETE.getCode(), "USER_DELETE"),

    VIDEO_VIEW(ResourceEnum.RESOURCE_VIDEO.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.VIEW.getCode(), "VIDEO_VIEW"),
    VIDEO_CREATE(ResourceEnum.RESOURCE_VIDEO.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.CREATE.getCode(), "VIDEO_CREATE"),
    VIDEO_UPDATE(ResourceEnum.RESOURCE_VIDEO.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.UPDATE.getCode(), "VIDEO_UPDATE"),
    VIDEO_DELETE(ResourceEnum.RESOURCE_VIDEO.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.DELETE.getCode(), "VIDEO_DELETE"),

    COMMENT_VIEW(ResourceEnum.RESOURCE_COMMENT.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.VIEW.getCode(), "COMMENT_VIEW"),
    COMMENT_CREATE(ResourceEnum.RESOURCE_COMMENT.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.CREATE.getCode(), "COMMENT_CREATE"),
    COMMENT_UPDATE(ResourceEnum.RESOURCE_COMMENT.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.UPDATE.getCode(), "COMMENT_UPDATE"),
    COMMENT_DELETE(ResourceEnum.RESOURCE_COMMENT.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.DELETE.getCode(), "COMMENT_DELETE"),

    MEETING_VIEW(ResourceEnum.RESOURCE_MEETING.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.VIEW.getCode(), "MEETING_VIEW"),
    MEETING_CREATE(ResourceEnum.RESOURCE_MEETING.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.CREATE.getCode(), "MEETING_CREATE"),
    MEETING_UPDATE(ResourceEnum.RESOURCE_MEETING.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.UPDATE.getCode(), "MEETING_UPDATE"),
    MEETING_DELETE(ResourceEnum.RESOURCE_MEETING.getCode()+AuthConstants.AUTHORITY_SEPARATOR+ActionEnum.DELETE.getCode(), "MEETING_DELETE");

    private String code;
    private String desc;

    AuthorityEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    private static List<String> allAuthorityList;

    private static List<String> userAllAuthorityList;

    private static List<String> videoAllAuthorityList;

    private static List<String> commentAllAuthorityList;

    private static List<String> meetingAllAuthorityList;

    static {
        List<String> list1 = new ArrayList<>();
        list1.add(USER_VIEW.getCode());
        list1.add(USER_CREATE.getCode());
        list1.add(USER_UPDATE.getCode());
        list1.add(USER_DELETE.getCode());
        userAllAuthorityList = Collections.unmodifiableList(list1);

        List<String> list2 = new ArrayList<>();
        list2.add(VIDEO_VIEW.getCode());
        list2.add(VIDEO_CREATE.getCode());
        list2.add(VIDEO_UPDATE.getCode());
        list2.add(VIDEO_DELETE.getCode());
        videoAllAuthorityList = Collections.unmodifiableList(list2);

        List<String> list3 = new ArrayList<>();
        list3.add(COMMENT_VIEW.getCode());
        list3.add(COMMENT_CREATE.getCode());
        list3.add(COMMENT_UPDATE.getCode());
        list3.add(COMMENT_DELETE.getCode());
        commentAllAuthorityList = Collections.unmodifiableList(list3);

        List<String> list4 = new ArrayList<>();
        list4.add(MEETING_VIEW.getCode());
        list4.add(MEETING_CREATE.getCode());
        list4.add(MEETING_UPDATE.getCode());
        list4.add(MEETING_DELETE.getCode());
        meetingAllAuthorityList = Collections.unmodifiableList(list4);

        List<String> list5 = new ArrayList<>();

        list5.add(USER_VIEW.getCode());
        list5.add(USER_CREATE.getCode());
        list5.add(USER_UPDATE.getCode());
        list5.add(USER_DELETE.getCode());

        list5.add(VIDEO_VIEW.getCode());
        list5.add(VIDEO_CREATE.getCode());
        list5.add(VIDEO_UPDATE.getCode());
        list5.add(VIDEO_DELETE.getCode());

        list5.add(COMMENT_VIEW.getCode());
        list5.add(COMMENT_CREATE.getCode());
        list5.add(COMMENT_UPDATE.getCode());
        list5.add(COMMENT_DELETE.getCode());

        list5.add(MEETING_VIEW.getCode());
        list5.add(MEETING_CREATE.getCode());
        list5.add(MEETING_UPDATE.getCode());
        list5.add(MEETING_DELETE.getCode());
        allAuthorityList = Collections.unmodifiableList(list5);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static List<String> getUserAllAuthorityList(){
        return userAllAuthorityList;
    }

    public static List<String> getVideoAllAuthorityList(){
        return videoAllAuthorityList;
    }

    public static List<String> getCommentAllAuthorityList(){
        return commentAllAuthorityList;
    }

    public static List<String> getMeetingAllAuthorityList(){
        return meetingAllAuthorityList;
    }

    public static List<String> getAllAuthorityList(){
        return allAuthorityList;
    }

    public static AuthorityEnum getEnumByString(String str){
        AuthorityEnum[] list = AuthorityEnum.values();
        for (AuthorityEnum item : list) {
            if (item.getCode().equals(str)) {
                return item;
            }
        }
        return null;
    }

}
