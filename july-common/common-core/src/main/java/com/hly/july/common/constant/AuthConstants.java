package com.hly.july.common.constant;

/**
 * @author Linyuan Hou
 * @date 2021/3/12 20:23
 */
public interface AuthConstants {

    String TOKEN_HEADER_KEYWORD="Authorization";
    String TOKEN_PREFIX_KEYWORD="Bearer ";
    Long TOKEN_EXPIRATION=1000000L;

    String TOKEN_JTI_KEYWORD = "jti";

}
