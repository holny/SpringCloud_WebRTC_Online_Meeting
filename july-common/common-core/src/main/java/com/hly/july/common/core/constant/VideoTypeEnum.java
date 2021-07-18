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

public enum VideoTypeEnum {
    NORMAL(0,"VIDEO_TYPE_NORMAL"),
    CONSULT(1,"VIDEO_TYPE_CONSULT"),
    RECORD(2,"VIDEO_TYPE_RECORD");

    private Integer code;
    private String desc;

    VideoTypeEnum(Integer code, String desc) {
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
        list1.add(NORMAL.getCode());
        list1.add(CONSULT.getCode());
        list1.add(RECORD.getCode());
        allTypeCodeList = Collections.unmodifiableList(list1);
    }

    public static List<Integer> getAllTypeCodeList(){
        return allTypeCodeList;
    }

    public static VideoTypeEnum getEnumByCode(Integer code){
        VideoTypeEnum[] list = VideoTypeEnum.values();
        for (VideoTypeEnum item : list) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code){
        VideoTypeEnum[] list = VideoTypeEnum.values();
        for (VideoTypeEnum item : list) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }
}
