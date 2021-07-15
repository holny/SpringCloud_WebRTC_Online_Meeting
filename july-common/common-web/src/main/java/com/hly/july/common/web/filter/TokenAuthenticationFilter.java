package com.hly.july.common.web.filter;

import com.hly.july.common.auth.entity.LoginUser;
import com.hly.july.common.auth.properties.RSAKeyProperties;
import com.hly.july.common.auth.util.JwtUtils;
import com.hly.july.common.core.util.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Linyuan Hou
 * @date 2021/3/16 15:34
 */
@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private RSAKeyProperties rsaKeyProp;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = JwtUtils.getTokenFromServletRequest(request);
        log.info("OncePerRequestFilter token:{}",token);
        log.info("RSA public key{}",rsaKeyProp.getPublicKey());
        if(!StringUtils.isEmpty(token)) {
            if(JwtUtils.validateToken(token, rsaKeyProp.getPublicKey())) {
                if (!JwtUtils.isTokenExpired(token, rsaKeyProp.getPublicKey())) {
                    try {
                        Claims claims = JwtUtils.getClaimsFromToken(token, rsaKeyProp.getPublicKey());
                        List<String> authorList = (List<String>) claims.get("authorities");
                        log.info("OncePerRequestFilter  claims:{} ", claims.getSubject());
                        log.info("OncePerRequestFilter  --- userId{}", claims.get("userId"));
                        log.info("OncePerRequestFilter  --- user_name{}", claims.get("userName"));
                        log.info("OncePerRequestFilter  --- createTime{}", claims.get("createTime"));
                        log.info("OncePerRequestFilter  --- scope{}", claims.get("scope"));
                        Date expiredDate = new Date(Long.parseLong(claims.get("exp").toString()) * 1000);
                        String expiredTime = DateUtils.format(expiredDate, DateUtils.DATE_FORMAT_DEFAULT);
                        log.info("OncePerRequestFilter  --- expiredTime{}", expiredTime);
                        log.info("OncePerRequestFilter  --- authorities{}", authorList);
                        log.info("OncePerRequestFilter  --- jti{}", claims.get("jti"));
                        // 如果可以正确的从JWT中提取用户信息，并且该用户未被授权
                        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet();
                        //查询用户具有的权限
                        if (!CollectionUtils.isEmpty(authorList)) {
                            log.info("This userId:" + claims.get("userId") + " has Authorities:{}", authorList.toString());
                            authorList.stream().forEach(author -> {
                                grantedAuthorities.add(new SimpleGrantedAuthority(author.toUpperCase()));
                            });
                        } else {
                            log.info("This userId:" + claims.get("userId") + " has no Authority.");
                        }
                        LoginUser loginUser = new LoginUser();
                        loginUser.setUserId(claims.get("userId").toString());
                        loginUser.setJti(claims.get("jti").toString());
                        loginUser.setStatus(Integer.parseInt(claims.get("status").toString()));
                        loginUser.setUsername(claims.get("account").toString());
                        loginUser.setAuthorities(new ArrayList<>(grantedAuthorities));
                        if (loginUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            // 给使用该JWT令牌的用户进行授权
                            log.info("This userId:" + claims.get("userId") + " start inject UsernamePasswordAuthenticationToken into security.");
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, token, loginUser.getAuthorities());
                            authenticationToken.setDetails(loginUser);
                            // 交给spring security管理,在之后的过滤器中不会再被拦截进行二次授权了
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            log.info("Done inject UsernamePasswordAuthenticationToken into security. authenticationToken:" + authenticationToken.toString());
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        logger.error("Unable to get username from JWT. " + e.getMessage());
                    } catch (ExpiredJwtException e) {
                        e.printStackTrace();
                        logger.warn("Expired JWT. " + e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Exception" + e.getMessage());
                    }
                } else {
                    log.warn("token is expired, token:{}", token);
                }
            }else{
                log.warn("token is invalid, token:{}", token);
            }

        }
        filterChain.doFilter(request, response);


    }

    protected String getJwtToken(HttpServletRequest request) {
        String authInfo = request.getHeader("Authorization");
        return StringUtils.removeStart(authInfo, "Bearer ");
    }
//
//    protected boolean requiresAuthentication(HttpServletRequest request,
//                                             HttpServletResponse response) {
//        return requiresAuthenticationRequestMatcher.matches(request);
//    }
}
