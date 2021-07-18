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

public enum ContainerStatusEnum {
    DELETE(0,"DELETE"),
    NORMAL(1,"NORMAL"),
    PRIVATE(2,"PRIVATE"),
    LOCKED(3,"LOCKED");

    private Integer code;
    private String desc;

    ContainerStatusEnum(Integer code, String desc) {
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

    private static List<Integer> allVisibleStatusCodeList;

    private static List<Integer> allPublicStatusCodeList;

    static {
        List<Integer> list1 = new ArrayList<>();
        list1.add(NORMAL.getCode());
        list1.add(PRIVATE.getCode());
        allVisibleStatusCodeList = Collections.unmodifiableList(list1);

        List<Integer> list2 = new ArrayList<>();
        list2.add(NORMAL.getCode());
        allPublicStatusCodeList = Collections.unmodifiableList(list2);
    }

    public static List<Integer> getVisibleStatusCodeList(){
        return allVisibleStatusCodeList;
    }

    public static List<Integer> getPublicStatusCodeList(){
        return allPublicStatusCodeList;
    }

    public static ContainerStatusEnum getEnumByCode(Integer code){
        ContainerStatusEnum[] list = ContainerStatusEnum.values();
        for (ContainerStatusEnum item : list) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code){
        ContainerStatusEnum[] list = ContainerStatusEnum.values();
        for (ContainerStatusEnum item : list) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }
}
