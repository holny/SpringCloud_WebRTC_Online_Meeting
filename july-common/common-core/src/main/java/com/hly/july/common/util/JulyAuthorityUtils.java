package com.hly.july.common.util;

import cn.hutool.core.util.EscapeUtil;
import com.hly.july.common.constant.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


/**
 * @author Linyuan Hou
 * @date 2021/5/11 21:03
 */
@Slf4j
public class JulyAuthorityUtils {

    public static String getRoleByRegisterCode(String registerCode) {
        RegisterCodeEnum registerCodeEnum = RegisterCodeEnum.getEnumByString(registerCode);
        if(registerCodeEnum!=null) {
            String role;
            switch (registerCodeEnum) {
                case CODE_SUPER_ADMIN:
                    role = RoleEnum.ROLE_SUPER_ADMIN.getCode();
                    break;
                case CODE_ADMIN:
                    role = RoleEnum.ROLE_ADMIN.getCode();
                    break;
                case CODE_AUTHOR:
                    role = RoleEnum.ROLE_AUTHOR.getCode();
                    break;
                case CODE_VISITOR:
                    role = RoleEnum.ROLE_VISITOR.getCode();
                    break;
                case CODE_EXPERT:
                    role = RoleEnum.ROLE_EXPERT.getCode();
                    break;
                case CODE_USER:
                    role = RoleEnum.ROLE_USER.getCode();
                    break;
                default:
                    role = null;
                    break;
            }
            return role;
        }else {
            return null;
        }

    }

    public static Set<String> getDefaultAuthorityByRole(String role) {
        Set<String> authoritySet = new HashSet<>();
        switch (RoleEnum.getEnumByString(role)){
            case ROLE_SUPER_ADMIN:
            case ROLE_ADMIN:
                authoritySet.add(AuthorityEnum.USER_CREATE.getCode());
                authoritySet.add(AuthorityEnum.USER_DELETE.getCode());

                authoritySet.add(AuthorityEnum.VIDEO_DELETE.getCode());

                authoritySet.add(AuthorityEnum.COMMENT_DELETE.getCode());

                authoritySet.add(AuthorityEnum.MEETING_DELETE.getCode());
            case ROLE_EXPERT:

                authoritySet.add(AuthorityEnum.MEETING_UPDATE.getCode());
                authoritySet.add(AuthorityEnum.MEETING_CREATE.getCode());
            case ROLE_AUTHOR:
                authoritySet.add(AuthorityEnum.VIDEO_UPDATE.getCode());
                authoritySet.add(AuthorityEnum.VIDEO_CREATE.getCode());
            case ROLE_USER:
                authoritySet.add(AuthorityEnum.USER_UPDATE.getCode());

                authoritySet.add(AuthorityEnum.COMMENT_UPDATE.getCode());
                authoritySet.add(AuthorityEnum.COMMENT_CREATE.getCode());
                //MEETING
                authoritySet.add(AuthorityEnum.MEETING_VIEW.getCode());
            case ROLE_VISITOR:
                //USER
                authoritySet.add(AuthorityEnum.USER_VIEW.getCode());
                //video
                authoritySet.add(AuthorityEnum.VIDEO_VIEW.getCode());
                //comment
                authoritySet.add(AuthorityEnum.COMMENT_VIEW.getCode());
                break;

        }
        return authoritySet;
    }

    /**
     *
     * @param authoritySet Set<Resource.Action>
     * @return USER_VIEW.CREATE;USER_VIEW.UPDATE;USER_VIEW.DELETE;RESOURCE_VIDEO.CREATE;RESOURCE_DELETE;
     */
    public static String authorityConcatSet2String(Set<String> authoritySet){
        if (authoritySet!=null) {
            StringBuffer sb = new StringBuffer();
            authoritySet.forEach(str -> {
                if (AuthorityEnum.getAllAuthorityList().contains(str)) {
                    sb.append(str).append(AuthConstants.AUTH_SEPARATOR);
                }
            });
            return sb.toString();
        }else{
            return null;
        }
    }

    /**
     *
     * @param authorityMap Map<Resource,Set<Action>>
     * @return USER_VIEW.CREATE;USER_VIEW.UPDATE;USER_VIEW.DELETE;RESOURCE_VIDEO.CREATE;RESOURCE_DELETE;
     */
    public static String authorityConcatMap2String(Map<String,Set<String>> authorityMap){
        if (authorityMap!=null) {
            StringBuffer sb = new StringBuffer();
            authorityMap.forEach((key, set) -> {
                set.forEach(str -> {
                    if (ResourceEnum.getAllResourceList().contains(key) && ActionEnum.getAllActionList().contains(str)) {
                        sb.append(key).append(AuthConstants.AUTHORITY_SEPARATOR).append(str).append(AuthConstants.AUTH_SEPARATOR);
                    }
                });
            });
            return sb.toString();
        }else{
            return null;
        }
    }

    /**
     *
     * @param authorityString USER_VIEW.CREATE;USER_VIEW.UPDATE;USER_VIEW.DELETE;RESOURCE_VIDEO.CREATE;RESOURCE_DELETE;
     * @return Set<Resource.Action>
     */
    public static Set<String> authorityClassifyString2Set(String authorityString){
        if (authorityString!=null) {
            List<String> authorityList = Arrays.asList(authorityString.split(AuthConstants.AUTH_SEPARATOR));
            Set<String> authoritySet = new HashSet<>();
            authorityList.forEach(str -> {
                if (str.contains(AuthConstants.AUTHORITY_SEPARATOR) && AuthorityEnum.getAllAuthorityList().contains(str)) {
                    authoritySet.add(str.trim());
                }
            });
            return authoritySet;
        }else{
            return null;
        }
    }

    /**
     *
     * @param authorityString USER_VIEW.CREATE;USER_VIEW.UPDATE;USER_VIEW.DELETE;RESOURCE_VIDEO.CREATE;RESOURCE_DELETE;
     * @return Map<Resource,Set<Action>>
     */
    public static Map<String,Set<String>> authorityClassifyString2Map(String authorityString){
        if (authorityString!=null) {
            List<String> authorityList = Arrays.asList(authorityString.split(AuthConstants.AUTH_SEPARATOR));
            Map<String, Set<String>> authorityMap = new HashMap<>();
            authorityList.forEach(str -> {
                if (AuthorityEnum.getAllAuthorityList().contains(str)) {
                    String[] authorityStringG = str.split(AuthConstants.AUTHORITY_SEPARATOR_ESCAPE);
                    List<String> authorityPair = Arrays.asList(authorityStringG);
                    if (authorityPair.size() == 2) {
                        Set<String> authoritySet;
                        if (authorityMap.containsKey(authorityPair.get(0).trim())) {
                            authoritySet = authorityMap.get(authorityPair.get(0).trim());
                        } else {
                            authoritySet = new HashSet<>();
                        }
                        authoritySet.add(authorityPair.get(1).trim());
                        authorityMap.put(authorityPair.get(0).trim(), authoritySet);
                    }
                }
            });
            return authorityMap;
        }else{
            return null;
        }
    }

    /**
     *
     * @param roleSet Set<Role>
     * @return ROLE_VISITOR;ROLE_USER;ROLE_ADMIN;
     */
    public static String roleConcatSet2String(Set<String> roleSet){
        if (roleSet!=null) {
            StringBuffer sb = new StringBuffer();
            roleSet.forEach(str -> {
                if (RoleEnum.getAllRoleList().contains(str)) {
                    sb.append(str).append(AuthConstants.AUTH_SEPARATOR);
                }
            });
            return sb.toString();
        }else{
            return null;
        }
    }

    /**
     *
     * @param roleString USER_VIEW.CREATE;USER_VIEW.UPDATE;USER_VIEW.DELETE;RESOURCE_VIDEO.CREATE;RESOURCE_DELETE;
     * @return Set<Resource.Action>
     */
    public static Set<String> roleClassifyString2Set(String roleString){
        if (roleString!=null) {
            List<String> roleList = Arrays.asList(roleString.split(AuthConstants.AUTH_SEPARATOR));
            Set<String> roleSet = new HashSet<>();
            roleList.forEach(str -> {
                if (RoleEnum.getAllRoleList().contains(str)) {
                    roleSet.add(str);
                }
            });
            return roleSet;
        }else{
            return null;
        }
    }


}
