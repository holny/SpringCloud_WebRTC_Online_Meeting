package com.hly.july.biz.meeting.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName UserActiveStatus
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/23 11:00
 * @Version 1.0.0
 **/
public enum UserActiveStatusEnum {

    OFFLINE(1, "offline"),
    ACTIVE_INVISIBLE(2, "active_invisible"),
    ACTIVE_VISIBLE(3, "active_visible"),
    ACTIVE_ONLY_CATEGORY_BOOKMARK(4, "active_only_category_bookmark"),
    ACTIVE_ONLY_MESSAGE(5, "active_only_message");

    private Integer code;
    private String desc;

    UserActiveStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }


    public String getMsg() {
        return desc;
    }

    private static List<Integer> allStatusList;

    private static List<Integer> allOnLineList;

    static {
        List<Integer> list1 = new ArrayList<>();
        list1.add(OFFLINE.getCode());
        list1.add(ACTIVE_VISIBLE.getCode());
        list1.add(ACTIVE_INVISIBLE.getCode());
        list1.add(ACTIVE_ONLY_CATEGORY_BOOKMARK.getCode());
        list1.add(ACTIVE_ONLY_MESSAGE.getCode());
        allStatusList = Collections.unmodifiableList(list1);

        List<Integer> list2 = new ArrayList<>();
        list2.add(ACTIVE_VISIBLE.getCode());
        list2.add(ACTIVE_INVISIBLE.getCode());
        list2.add(ACTIVE_ONLY_CATEGORY_BOOKMARK.getCode());
        list2.add(ACTIVE_ONLY_MESSAGE.getCode());
        allOnLineList = Collections.unmodifiableList(list2);
    }

    public static List<Integer> getAllCodeList(){
        return allStatusList;
    }
    public static List<Integer> getAllOnLineCodeList(){
        return allOnLineList;
    }
}
