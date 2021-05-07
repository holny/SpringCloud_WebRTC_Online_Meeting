package com.hly.july.filter;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hly.july.common.properties.RSAKeyProperties;
import com.hly.july.common.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
        log.info("linyhou token:{}",token);

        if(!StringUtils.isEmpty(token)) {
            JSONObject jsonObject = JwtUtils.getJSONObjectFromToken(token);
            log.info("linyhou username:{}  author:{}",jsonObject.getStr("userName",""),jsonObject.get("authorities"));
            if(!JwtUtils.isTokenExpired(token,rsaKeyProp.getPublicKey())) {
                List<String> authorList = JSONUtil.toList(jsonObject.getJSONArray("authorities"),String.class);
                log.info("linyhou authorList:{}",authorList.toString());
//                            UsernamePasswordAuthenticationToken authentication = new
//                    UsernamePasswordAuthenticationToken(
//                    user, null, AuthorityUtils.createAuthorityList(authorities));
//                // 给使用该JWT令牌的用户进行授权
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                // 交给spring security管理,在之后的过滤器中不会再被拦截进行二次授权了
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

//            Claims claims = jwtUtils.getClaimsFromToken(token, rsaKeyProp.getPublicKey());
//            log.info("OncePerRequestFilter  claims:{}  --- {}",claims.getSubject(),claims.get("authorities"));
//            // 如果可以正确的从JWT中提取用户信息，并且该用户未被授权
//            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                if(jwtTokenUtil.validateToken(token, userDetails, rsaKeyProp.getPublicKey())) {
//                    // 给使用该JWT令牌的用户进行授权
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    // 交给spring security管理,在之后的过滤器中不会再被拦截进行二次授权了
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                }
//            }
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
