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

public enum TagTypeEnum {
    VIDEO(1,"video");

    private Integer code;
    private String desc;

    TagTypeEnum(Integer code, String desc) {
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
        list1.add(VIDEO.getCode());
        allTypeCodeList = Collections.unmodifiableList(list1);
    }

    public static List<Integer> getAllTypeCodeList(){
        return allTypeCodeList;
    }

    public static TagTypeEnum getEnumByCode(Integer code){
        TagTypeEnum[] list = TagTypeEnum.values();
        for (TagTypeEnum item : list) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code){
        TagTypeEnum[] list = TagTypeEnum.values();
        for (TagTypeEnum item : list) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }

    public static Integer getCodeByDesc(String desc){
        TagTypeEnum[] list = TagTypeEnum.values();
        for (TagTypeEnum item : list) {
            if (item.getDesc().equals(desc)) {
                return item.getCode();
            }
        }
        return null;
    }
}
