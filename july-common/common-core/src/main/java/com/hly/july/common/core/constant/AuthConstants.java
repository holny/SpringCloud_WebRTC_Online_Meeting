package com.hly.july.common.core.constant;

/**
 * @author Linyuan Hou
 * @date 2021/3/12 20:23
 */
public interface AuthConstants {

    String TOKEN_HEADER_KEYWORD="Authorization";
    String TOKEN_PREFIX_KEYWORD="Bearer ";
    Long TOKEN_EXPIRATION=1000000L;
    String CLIENT_BASIC_SECRET = "Basic anVseV9jbGllbnQ6aGx5NDMyMQ==";

    String TOKEN_JTI_KEYWORD = "jti";
//
//    String CODE_SUPER_ADMIN = "SUPERADMIN";
//    String CODE_ADMIN = "ADMIN";
//    String CODE_VISITOR = "VISITOR";
//    String CODE_EXPERT = "EXPERT";
//    String CODE_AUTHOR = "AUTHOR";
//    String CODE_USER = "USER";
//
//    String ROLE_VISITOR = "ROLE_VISITOR";
//    String ROLE_USER = "ROLE_USER";
//    String ROLE_AUTHOR = "ROLE_AUTHOR";
//    String ROLE_EXPERT = "ROLE_EXPERT";
//    String ROLE_ADMIN = "ROLE_ADMIN";
//    String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
//
//    String RESOURCE_USER = "RESOURCE_USER";
//    String RESOURCE_VIDEO = "RESOURCE_VIDEO";
//    String RESOURCE_COMMENT = "RESOURCE_COMMENT";
//    String RESOURCE_MEETING = "RESOURCE_MEETING";
//
//    String ACTION_CREATE = "CREATE";
//    String ACTION_UPDATE = "UPDATE";
//    String ACTION_DELETE = "DELETE";
//    String ACTION_VIEW = "VIEW";

    String AUTH_SEPARATOR = ";";
    String AUTHORITY_SEPARATOR = ".";
    String AUTHORITY_SEPARATOR_ESCAPE = "\\.";

}
