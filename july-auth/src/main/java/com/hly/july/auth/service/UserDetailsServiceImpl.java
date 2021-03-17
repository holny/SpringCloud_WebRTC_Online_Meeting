package com.hly.july.auth.service;

import com.hly.july.common.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Linyuan Hou
 * @date 2021/3/15 12:24
 */
@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    public static HashMap<String, LoginUser> userHashMap = new HashMap<>();
    static
    {
        //54321
        userHashMap.put("tttt",new LoginUser(1L,"Tom","$2a$10$Rk9.n5LiG1DLX6s1FSrJIOr4q8rP5XlVwHoB6dM.kmoKun98xu6Vq",true));
        //6543
        userHashMap.put("jjjj",new LoginUser(2L,"Jim","$2a$10$wh1BZ/e5eAjeVeJtMf8qWuD/VFp/EqSIHwZ9WZRmZGpsELOg/HOcu",true));
        //1234
        userHashMap.put("ffff",new LoginUser(3L,"Fury","$2a$10$Kuq9IXomzoju6mpEqE.1v.6foyVVywImgUPLuj6IemSD51b5m7SQm",true));
    }

    public static HashMap<String, List<String>> roleHashMap = new HashMap<>();
    static
    {
        roleHashMap.put("tttt",new ArrayList<String>(Arrays.asList("ROLE_ADMIN","ROLE_USER")));
        roleHashMap.put("jjjj",new ArrayList<String>(Arrays.asList("ROLE_PRO","ROLE_USER")));
        roleHashMap.put("ffff",new ArrayList<String>(Arrays.asList("ROLE_USER")));
    }

    public static HashMap<String, List<String>> authorityHashMap = new HashMap<>();
    static
    {
        authorityHashMap.put("tttt",new ArrayList<String>(Arrays.asList("CREATE","UPDATE","INSERT","DELETE")));
        authorityHashMap.put("jjjj",new ArrayList<String>(Arrays.asList("CREATE","UPDATE","INSERT")));
        authorityHashMap.put("ffff",new ArrayList<String>(Arrays.asList("CREATE","UPDATE")));
    }
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("new user login:{}",s);
        LoginUser loginUser = userHashMap.get(s);
        if (ObjectUtils.isEmpty(loginUser)) {
            throw new UsernameNotFoundException("不存在此用户");
        }
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet();
        //查询用户具有的权限
        List<String> authorityList = authorityHashMap.get(s);
        if(!ObjectUtils.isEmpty(authorityList)) {
            log.info("user authorityList:{}", authorityList.toString());
            authorityList.stream().forEach(author -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(author.toUpperCase()));
            });
        }else{
            log.info("user no authority, all:{}",authorityHashMap.toString());
        }
        List<String> roleList = roleHashMap.get(s);
        if(!ObjectUtils.isEmpty(roleList)) {
            log.info("user roleList:{}", roleList.toString());
            roleList.stream().forEach(role -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.toUpperCase()));
            });
        }else{
            log.info("user no role, all:{}",authorityHashMap.toString());
        }

        if (!loginUser.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!loginUser.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!loginUser.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        loginUser.setAuthorities(new ArrayList<SimpleGrantedAuthority>(grantedAuthorities));
        log.info("login successful login:{}",loginUser.toString());
        return loginUser;
    }
}
