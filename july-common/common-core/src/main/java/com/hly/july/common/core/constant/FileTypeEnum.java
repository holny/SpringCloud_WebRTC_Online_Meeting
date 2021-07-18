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

public enum FileTypeEnum {
    UNKNOWN(0,"VIDEO_TYPE_UNKNOWN"),
    VIDEO(1,"VIDEO_TYPE_VIDEO"),
    AUDIO(2,"VIDEO_TYPE_AUDIO"),
    IMAGE(3,"VIDEO_TYPE_IMAGE");

    private Integer code;
    private String desc;

    FileTypeEnum(Integer code, String desc) {
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


    private static List<Integer> allTypeCodeList;

    static {
        List<Integer> list1 = new ArrayList<>();
        list1.add(UNKNOWN.getCode());
        list1.add(VIDEO.getCode());
        list1.add(AUDIO.getCode());
        list1.add(IMAGE.getCode());
        allTypeCodeList = Collections.unmodifiableList(list1);
    }

    public static List<Integer> getAllTypeCodeList(){
        return allTypeCodeList;
    }

    public static FileTypeEnum getEnumByCode(Integer code){
        FileTypeEnum[] list = FileTypeEnum.values();
        for (FileTypeEnum item : list) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code){
        FileTypeEnum[] list = FileTypeEnum.values();
        for (FileTypeEnum item : list) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }
}
