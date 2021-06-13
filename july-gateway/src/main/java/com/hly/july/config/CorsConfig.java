package com.hly.july.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

/**
 * @ClassName CorsConfig
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/5/24 20:05
 * @Version 1.0.0
 **/
@Configuration
public class CorsConfig {
    /**
     * 跨域处理方式一
     * @return
     */
//    @Bean
//    public CorsWebFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedMethod("*");
////        config.addAllowedOrigin("*");
////        config.addAllowedHeader("*");
////        config.addAllowedMethod("GET,PUT,POST,DELETE,OPTIONS");
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsWebFilter(source);
//    }

    /**
     * 跨域处理方式二
     * @return
     */
//    @Bean
//    public WebFilter corsFilter() {
//        return (ServerWebExchange ctx, WebFilterChain chain) -> {
//            ServerHttpRequest request = ctx.getRequest();
//            if (CorsUtils.isCorsRequest(request)) {
//                HttpHeaders requestHeaders = request.getHeaders();
//                ServerHttpResponse response = ctx.getResponse();
//                HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
//                HttpHeaders headers = response.getHeaders();
//                headers.setAccessControlAllowOrigin(requestHeaders.getOrigin());
//                headers.setAccessControlAllowHeaders(requestHeaders.getAccessControlRequestHeaders());
//                if (requestMethod != null) {
//                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
//                }
//                headers.setAccessControlAllowCredentials(true);
//                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
////                if (request.getMethod() == HttpMethod.OPTIONS) {
////                    response.setStatusCode(HttpStatus.OK);
////                    return Mono.empty();
////                }
//            }
//            return chain.filter(ctx);
//        };
//    }

}
