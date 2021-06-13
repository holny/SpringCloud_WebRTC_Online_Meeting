package com.hly.july.auth.service;


import com.hly.july.auth.service.impl.UserServiceImpl;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.constant.UserStatusEnum;
import com.hly.july.common.entity.LoginUser;
import com.hly.july.common.util.JulyAuthorityUtils;
import com.hly.july.common.util.WrappedBeanCopier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author Linyuan Hou
 * @date 2021/3/15 12:24
 */
@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("new user login:{}",userName);
        User user = userService.getUserByAccount(userName);
        if (user == null) {
            log.warn("This account not exists, account:"+userName);
            throw new BadCredentialsException("不存在此账号debug");
        }
        LoginUser loginUser = WrappedBeanCopier.copyProperties(user,LoginUser.class);
        if(UserStatusEnum.USER_STATUS_DELETE.getCode().equals(user.getStatus())){
            log.warn("This account was deleted, user:"+user.toString());
            loginUser.setIsEnabled(false);
        }
        if(UserStatusEnum.USER_STATUS_FORBIDDEN.getCode().equals(user.getStatus())){
            log.warn("This account was forbidden, user:"+user.toString());
            loginUser.setIsAccountNonLocked(false);
        }
        if(UserStatusEnum.USER_STATUS_LOCKED.getCode().equals(user.getStatus())){
            log.warn("This account was forbidden, user:"+user.toString());
            loginUser.setIsAccountNonLocked(false);
        }
        if(UserStatusEnum.USER_STATUS_EXPIRED.getCode().equals(user.getStatus())){
            log.warn("This account was expired, user:"+user.toString());
            loginUser.setIsAccountNonExpired(false);
        }

        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet();
        //查询用户具有的权限
        Set<String> authoritySet = JulyAuthorityUtils.authorityClassifyString2Set(user.getAuthority());
        if(!CollectionUtils.isEmpty(authoritySet)) {
            log.info("This userId:"+user.getUserId()+" has Authorities:{}", authoritySet.toString());
            authoritySet.stream().forEach(author -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(author.toUpperCase()));
            });
        }else{
            log.info("This userId:"+user.getUserId()+" has no Authority.");
        }
        Set<String> roleSet = JulyAuthorityUtils.roleClassifyString2Set(user.getRole());
        log.info("roleSet:{}",roleSet.toString());
        if(!ObjectUtils.isEmpty(roleSet)) {
            log.info("This userId:"+user.getUserId()+" has Roles:{}", authoritySet.toString());
            roleSet.stream().forEach(role -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.toUpperCase()));
            });
        }else{
            log.info("This userId:"+user.getUserId()+" has no Role.");
        }

        if (!loginUser.isEnabled()) {
            throw new DisabledException("此账号已被禁用debug");
        } else if (!loginUser.isAccountNonLocked()) {
            throw new LockedException("此账号已被锁定debug");
        } else if (!loginUser.isAccountNonExpired()) {
            throw new AccountExpiredException("此账号过期debug");
        }
        loginUser.setAuthorities(new ArrayList<SimpleGrantedAuthority>(grantedAuthorities));
//        loginUser.setUsername(user.getUserId().toString());   // 通过修改这个，设定资源器自动解析token的Principal
        log.info("Login successful loginUser:{}",loginUser.toString());
        return loginUser;
    }
}
