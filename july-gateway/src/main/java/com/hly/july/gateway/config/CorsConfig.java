package com.hly.july.gateway.config;

import org.springframework.context.annotation.Configuration;

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
