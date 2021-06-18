package com.hly.july.common.biz.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName RelationType
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/18 10:48
 * @Version 1.0.0
 **/
public enum RelationTypeEnum {
    FRIEND(1,"FRIEND"),
    BLACK(2,"BLACK"),
    IGNORE(3,"IGNORE");

    private Integer code;
    private String desc;

    RelationTypeEnum(Integer code, String desc) {
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

    private static List<Integer> allNegativeCodeList;

    static {
        List<Integer> list1 = new ArrayList<>();
        list1.add(IGNORE.getCode());
        list1.add(BLACK.getCode());
        list1.add(FRIEND.getCode());
        allCodeList = Collections.unmodifiableList(list1);

        List<Integer> list2 = new ArrayList<>();
        list2.add(IGNORE.getCode());
        list2.add(BLACK.getCode());
        allNegativeCodeList = Collections.unmodifiableList(list2);
    }

    public static List<Integer> getAllCodeList(){
        return allCodeList;
    }

    public static List<Integer> getNegativeCodeList(){
        return allNegativeCodeList;
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
