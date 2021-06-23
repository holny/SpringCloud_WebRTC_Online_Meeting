package com.hly.july.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName SessionStatusEnum
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/23 10:44
 * @Version 1.0.0
 **/
public enum SessionStatusEnum {

    IN_CONFIRM(1, "in_confirm"),
    CONFIRM(2, "confirm"),
    IN_SIGNALING(3, "in_signaling"),
    IN_MEETING(4, "in_meeting"),
    PRE_CLOSE(5, "pre_close"),
    CLOSED(6, "closed"),
    FAIL(7, "fail");

    private Integer code;
    private String desc;

    SessionStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }


    public String getMsg() {
        return desc;
    }
    private static List<Integer> allActiveCodeList;

    static {
        List<Integer> list1 = new ArrayList<>();
        list1.add(IN_CONFIRM.getCode());
        list1.add(CONFIRM.getCode());
        list1.add(IN_SIGNALING.getCode());
        list1.add(IN_MEETING.getCode());
        list1.add(PRE_CLOSE.getCode());
        allActiveCodeList = Collections.unmodifiableList(list1);

    }

    public static List<Integer> getActiveCodeList(){
        return allActiveCodeList;
    }
}
