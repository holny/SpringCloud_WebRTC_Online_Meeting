package com.hly.july.common.core.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName ContainerEnum
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/15 15:36
 * @Version 1.0.0
 **/
public enum ContainerEnum {
    PERSON(1,"person"),
    GROUP(2,"group"),
    MESSAGE(3,"message"),
    MAIN_CATEGORY(4,"mainCategory"),
    SUB_CATEGORY(5,"subCategory");


    private Integer code;
    private String desc;

    ContainerEnum(Integer code, String desc) {
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
    private static List<Integer> allCodeList;

    static {
        List<Integer> list1 = new ArrayList<>();
        list1.add(PERSON.getCode());
        list1.add(GROUP.getCode());
        list1.add(MESSAGE.getCode());
        list1.add(MAIN_CATEGORY.getCode());
        list1.add(SUB_CATEGORY.getCode());
        allCodeList = Collections.unmodifiableList(list1);
    }

    public static List<Integer> getAllCodeList(){
        return allCodeList;
    }

    public static String getDescByCode(Integer code){
        ContainerEnum[] list = ContainerEnum.values();
        for (ContainerEnum item : list) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }

    public static Integer getCodeByDesc(String desc){
        ContainerEnum[] list = ContainerEnum.values();
        for (ContainerEnum item : list) {
            if (item.getDesc().equals(desc)) {
                return item.getCode();
            }
        }
        return null;
    }
}
