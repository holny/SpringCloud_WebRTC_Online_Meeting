package com.hly.july.common.core.constant;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Linyuan Hou
 * @date 2021/5/12 09:44
 */
@Slf4j
public enum RoleEnum {
    ROLE_VISITOR("ROLE_VISITOR","VISITOR"),
    ROLE_USER("ROLE_USER","USER"),
    ROLE_AUTHOR("ROLE_AUTHOR","AUTHOR"),
    ROLE_EXPERT("ROLE_EXPERT","EXPERT"),
    ROLE_ADMIN("ROLE_ADMIN","ADMIN"),
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN","SUPER_ADMIN");

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
        log.info("getEnumByString:{}",list.toString());
        for (RoleEnum item : list) {
            if (item.getCode().equals(str)) {
                return item;
            }
        }
        return null;
    }
}
