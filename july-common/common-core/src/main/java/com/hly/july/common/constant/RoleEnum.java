package com.hly.july.common.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Linyuan Hou
 * @date 2021/5/12 09:44
 */
public enum RoleEnum {
    ROLE_VISITOR("ROLE_VISITOR","role_visitor"),
    ROLE_USER("ROLE_USER","role_user"),
    ROLE_AUTHOR("ROLE_AUTHOR","role_author"),
    ROLE_EXPERT("ROLE_EXPERT","role_expert"),
    ROLE_ADMIN("ROLE_ADMIN","role_admin"),
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN","role_superAdmin");

    private String code;
    private String desc;

    RoleEnum(String code, String desc) {
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

    private static List<String> allRoleList;

    static {
        List<String> list1 = new ArrayList<>();
        list1.add(ROLE_VISITOR.getCode());
        list1.add(ROLE_USER.getCode());
        list1.add(ROLE_AUTHOR.getCode());
        list1.add(ROLE_EXPERT.getCode());
        list1.add(ROLE_ADMIN.getCode());
        list1.add(ROLE_SUPER_ADMIN.getCode());
        allRoleList = Collections.unmodifiableList(list1);
    }

    public static List<String> getAllRoleList(){
        return allRoleList;
    }

    public static RoleEnum getEnumByString(String str){
        RoleEnum[] list = RoleEnum.values();
        for (RoleEnum item : list) {
            if (item.getCode().equals(str)) {
                return item;
            }
        }
        return null;
    }
}
