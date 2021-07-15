package com.hly.july.auth.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.hly.july.common.auth.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author Linyuan Hou
 * @date 2021/3/16 14:58
 */
@Slf4j
public class JwtTokenEnhancer implements TokenEnhancer {
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = Maps.newHashMap();
        if(authentication.getPrincipal() instanceof String){
            // 如果不是密码模式登陆的没有用户信息 所以不用定制token
            return accessToken;
        }
        // 给/oauth/token接口加属性roles,author
        LoginUser user = (LoginUser) authentication.getPrincipal();
        List<SimpleGrantedAuthority> authorities = user.getAuthorities();
        List<String> permissions = Lists.newArrayList();
        for (SimpleGrantedAuthority authority : authorities) {
            permissions.add(authority.getAuthority());
        }
//        additionalInfo.put("permissions", authorities.stream().map(GrantedAuthority::getAuthority).collect(toList()));
        log.info("JwtTokenEnhancer  loginUser:"+user.toString());
        additionalInfo.put("userId", user.getUserId());
        additionalInfo.put("account", user.getAccount()); //设置account
        additionalInfo.put("status", user.getStatus());
        additionalInfo.put("createTime", df.format(LocalDateTime.now()));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;

//        Map<String, Object> map = new HashMap<>(8);
//        LoginUser user = (LoginUser) authentication.getUserAuthentication().getPrincipal();
//        map.put("userId", user.getId());
//        map.put("userName",user.getUsername());
//        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
//        return accessToken;
    }
}
