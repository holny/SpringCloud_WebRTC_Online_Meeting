package com.hly.july.filter;

import cn.hutool.json.JSONObject;
import com.hly.july.common.biz.utils.RedisUtils;
import com.hly.july.common.constant.AuthConstants;
import com.hly.july.common.properties.RSAKeyProperties;
import com.hly.july.common.result.ResultCode;
import com.hly.july.common.util.DateUtils;
import com.hly.july.common.util.JwtUtils;
import com.hly.july.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Linyuan Hou
 * @date 2021/3/23 18:45
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {
    @Resource
    private RSAKeyProperties rsaKeyProp;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String ip = "";
        log.info("Gateway AuthFilter");
        try {
            ip = request.getLocalAddress().toString();//返回发出请求的IP地址
            log.info("Gateway AuthFilter, ip:{}",ip);
        }catch (Exception e){
            log.error("Gateway AuthFilter, e:{}",e.getMessage());
        }

        String token = JwtUtils.getTokenFromHttpRequest(request);
        // 如果有token并且没有过期就直接放行，否则进入下面
        if(!StringUtils.isEmpty(token)){
            if(!JwtUtils.isTokenExpired(token,rsaKeyProp.getPublicKey())){
                JSONObject objectFromToken = JwtUtils.getJSONObjectFromToken(token);
                if(ObjectUtils.isNotEmpty(objectFromToken)){
                    log.info("Token parse success, objectFromToken:{}",objectFromToken.toString());
                    String jti = objectFromToken.getStr(AuthConstants.TOKEN_JTI_KEYWORD,null);
                    log.info("Token parse success, jti:{}",jti);
                    if(StringUtils.isNotEmpty(jti)){
                        // TODO: 对jtl在redis里的是否过期和是否黑名单进行判断
                        log.info("Token parse jtl success, token:{},\n jtl:{}",token,jti);
                        Date now =  DateUtils.getCurrentDateTime();
                        redisUtils.set(jti,now.toString());
                        return chain.filter(exchange);
                    }else{
                        return WebUtils.writeFailedToResponse(response, ResultCode.TOKEN_INVALID);
                    }
                }
            }else{
                log.info("Token is expired, token:{}",token);
                return WebUtils.writeFailedToResponse(response, ResultCode.TOKEN_EXPIRED);
            }
        }
        log.info("No token");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
