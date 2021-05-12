package com.hly.july.common.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Linyuan Hou
 * @date 2021/5/12 10:06
 */
public enum ActionEnum {
    CREATE("CREATE","create"),
    UPDATE("UPDATE","update"),
    DELETE("DELETE","delete"),
    VIEW("VIEW","view");

    private String code;
    private String desc;

    ActionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
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

    private static List<String> allActionList;

    static {
        List<String> list1 = new ArrayList<>();
        list1.add(CREATE.getCode());
        list1.add(UPDATE.getCode());
        list1.add(DELETE.getCode());
        list1.add(VIEW.getCode());
        allActionList = Collections.unmodifiableList(list1);
    }
    public static List<String> getAllActionList(){
        return allActionList;
    }

    public static ActionEnum getEnumByString(String str){
        ActionEnum[] list = ActionEnum.values();
        for (ActionEnum item : list) {
            if (item.getCode().equals(str)) {
                return item;
            }
        }
        return null;
    }
}
