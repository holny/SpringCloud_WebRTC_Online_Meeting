package com.hly.july.common.constant;

/**
 * @author Linyuan Hou
 * @date 2021/5/12 21:17
 */
public interface JulyConstants {
    Integer USER_GENDER_FEMALE = 0;
    Integer USER_GENDER_MALE = 1;




    Integer DEFAULT_USER_GENDER = USER_GENDER_MALE;
    String DEFAULT_USER_AVATAR = "/default_avatar.jpg";
    Integer DEFAULT_USER_STATUS = UserStatusEnum.USER_STATUS_NORMAL.getCode();
}
