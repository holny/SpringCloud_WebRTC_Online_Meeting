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

public enum FileStatusEnum {
    DELETE(0,"FILE_STATUS_DELETE"),
    IN_PROCESS(1,"FILE_STATUS_IN_PROCESS"),
    PRIVATE(2,"FILE_STATUS_PRIVATE"),
    PUBLIC(3,"FILE_STATUS_PUBLIC"),
    LOCKED(4,"FILE_STATUS_LOCKED"),
    NOTFOUND(5,"FILE_STATUS_NOTFOUND");

    private Integer code;
    private String desc;

    FileStatusEnum(Integer code, String desc) {
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

    private static List<Integer> allExistsCodeList;

    private static List<Integer> allInvisibleCodeList;

    private static List<Integer> allPublicCodeList;

    static {
        List<Integer> list1 = new ArrayList<>();
        list1.add(IN_PROCESS.getCode());
        list1.add(PUBLIC.getCode());
        allExistsCodeList = Collections.unmodifiableList(list1);

        List<Integer> list2 = new ArrayList<>();
        list2.add(PRIVATE.getCode());
        list2.add(PUBLIC.getCode());
        list2.add(LOCKED.getCode());
        allInvisibleCodeList = Collections.unmodifiableList(list2);

        List<Integer> list3 = new ArrayList<>();
        list3.add(PUBLIC.getCode());
        allPublicCodeList = Collections.unmodifiableList(list3);
    }

    public static List<Integer> getAllExistsCodeList(){
        return allExistsCodeList;
    }

    public static List<Integer> getAllInvisibleCodeList(){
        return allInvisibleCodeList;
    }
    public static List<Integer> getAllPublicCodeList(){
        return allPublicCodeList;
    }

    public static FileStatusEnum getEnumByCode(Integer code){
        FileStatusEnum[] list = FileStatusEnum.values();
        for (FileStatusEnum item : list) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code){
        FileStatusEnum[] list = FileStatusEnum.values();
        for (FileStatusEnum item : list) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }
}
