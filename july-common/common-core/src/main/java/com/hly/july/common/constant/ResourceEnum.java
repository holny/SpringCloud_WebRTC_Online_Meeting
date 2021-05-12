package com.hly.july.common.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Linyuan Hou
 * @date 2021/5/12 09:44
 */
public enum ResourceEnum {
    RESOURCE_USER("RESOURCE_USER","resource_user"),
    RESOURCE_VIDEO("RESOURCE_VIDEO","resource_video"),
    RESOURCE_COMMENT("RESOURCE_COMMENT","resource_comment"),
    RESOURCE_MEETING("RESOURCE_MEETING","resource_meeting");

    private String code;
    private String desc;

    ResourceEnum(String code, String desc) {
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

    private static List<String> allResourceList;

    static {
        List<String> list1 = new ArrayList<>();
        list1.add(RESOURCE_USER.getCode());
        list1.add(RESOURCE_VIDEO.getCode());
        list1.add(RESOURCE_COMMENT.getCode());
        list1.add(RESOURCE_MEETING.getCode());
        allResourceList = Collections.unmodifiableList(list1);
    }

    public static List<String> getAllResourceList(){
        return allResourceList;
    }

    public static ResourceEnum getEnumByString(String str){
        ResourceEnum[] list = ResourceEnum.values();
        for (ResourceEnum item : list) {
            if (item.getCode().equals(str)) {
                return item;
            }
        }
        return null;
    }
}
