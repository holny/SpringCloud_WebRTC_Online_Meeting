package com.hly.july.common.biz.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Linyuan Hou
 * @date 2021/5/12 21:23
 */
public enum UserStatusEnum {
    USER_STATUS_DELETE(0,"USER_STATUS_DELETE"),
    USER_STATUS_NORMAL(1,"USER_STATUS_NORMAL"),
    USER_STATUS_FORBIDDEN(2,"USER_STATUS_FORBIDDEN"),
    USER_STATUS_LOCKED(3,"USER_STATUS_LOCKED"),
    USER_STATUS_EXPIRED(4,"USER_STATUS_EXPIRED");

    private Integer code;
    private String desc;

    UserStatusEnum(Integer code, String desc) {
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

    private static List<Integer> allUserStatusCodeList;

    private static List<Integer> visibleUserStatusCodeList;

    static {
        List<Integer> list1 = new ArrayList<>();
        list1.add(USER_STATUS_DELETE.getCode());
        list1.add(USER_STATUS_NORMAL.getCode());
        list1.add(USER_STATUS_FORBIDDEN.getCode());
        list1.add(USER_STATUS_LOCKED.getCode());
        list1.add(USER_STATUS_EXPIRED.getCode());
        allUserStatusCodeList = Collections.unmodifiableList(list1);

        List<Integer> list2 = new ArrayList<>();
        list2.add(USER_STATUS_NORMAL.getCode());
        list2.add(USER_STATUS_LOCKED.getCode());
        list2.add(USER_STATUS_EXPIRED.getCode());
        visibleUserStatusCodeList = Collections.unmodifiableList(list2);
    }

    public static List<Integer> getAllUserStatusCodeList(){
        return allUserStatusCodeList;
    }

    public static List<Integer> getVisibleUserStatusCodeList(){
        return visibleUserStatusCodeList;
    }

    public static RoleEnum getEnumByCode(Integer code){
        RoleEnum[] list = RoleEnum.values();
        for (RoleEnum item : list) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code){
        RoleEnum[] list = RoleEnum.values();
        for (RoleEnum item : list) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }
}
