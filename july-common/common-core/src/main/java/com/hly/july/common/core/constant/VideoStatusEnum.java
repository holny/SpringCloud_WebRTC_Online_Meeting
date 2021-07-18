package com.hly.july.common.core.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName VideoStatus
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/16 14:04
 * @Version 1.0.0
 **/

public enum VideoStatusEnum {
    DELETE(0,"VIDEO_STATUS_DELETE"),
    IN_PROCESS(1,"VIDEO_STATUS_IN_PROCESS"),
    PRIVATE(2,"VIDEO_STATUS_PRIVATE"),
    PUBLIC(3,"VIDEO_STATUS_PUBLIC"),
    LOCKED(4,"VIDEO_STATUS_LOCKED");

    private Integer code;
    private String desc;

    VideoStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private static List<Integer> allInvisibleCodeList;

    private static List<Integer> publicCodeList;

    static {
        List<Integer> list1 = new ArrayList<>();
        list1.add(PRIVATE.getCode());
        list1.add(PUBLIC.getCode());
        list1.add(LOCKED.getCode());
        allInvisibleCodeList = Collections.unmodifiableList(list1);

        List<Integer> list2 = new ArrayList<>();
        list2.add(PUBLIC.getCode());
        publicCodeList = Collections.unmodifiableList(list2);
    }

    public static List<Integer> getAllInvisibleCodeList(){
        return allInvisibleCodeList;
    }

    public static List<Integer> getPublicCodeList(){
        return publicCodeList;
    }

    public static VideoStatusEnum getEnumByCode(Integer code){
        VideoStatusEnum[] list = VideoStatusEnum.values();
        for (VideoStatusEnum item : list) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code){
        VideoStatusEnum[] list = VideoStatusEnum.values();
        for (VideoStatusEnum item : list) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }
}
