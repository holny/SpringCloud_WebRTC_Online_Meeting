package com.hly.july.common.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Linyuan Hou
 * @date 2021/5/12 09:44
 */
public enum RegisterCodeEnum{
    CODE_SUPER_ADMIN("SUPERADMIN", "superAdmin"),
    CODE_ADMIN("ADMIN", "admin"),
    CODE_VISITOR("VISITOR", "visitor"),
    CODE_EXPERT("EXPERT", "expert"),
    CODE_AUTHOR("AUTHOR", "author"),
    CODE_USER("USER", "user");


    private String code;
    private String desc;

    RegisterCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static List<String> allCodeList;


    static {
        List<String> list1 = new ArrayList<>();
        list1.add(CODE_SUPER_ADMIN.getCode());
        list1.add(CODE_ADMIN.getCode());
        list1.add(CODE_VISITOR.getCode());
        list1.add(CODE_EXPERT.getCode());
        list1.add(CODE_AUTHOR.getCode());
        list1.add(CODE_USER.getCode());
        allCodeList = Collections.unmodifiableList(list1);
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

    public static List<String> getAllCodeList(){
        return allCodeList;
    }

    public static RegisterCodeEnum getEnumByString(String str){
        RegisterCodeEnum[] list = RegisterCodeEnum.values();
        for (RegisterCodeEnum item : list) {
            if (item.getCode().equals(str)) {
                return item;
            }
        }
        return null;
    }
}
