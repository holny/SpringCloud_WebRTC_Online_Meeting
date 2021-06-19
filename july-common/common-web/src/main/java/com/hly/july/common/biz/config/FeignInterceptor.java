package com.hly.july.common.biz.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName FeignConfig
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/14 23:00
 * @Version 1.0.0
 **/
@Slf4j
@Configuration
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes!=null) {
            log.info("RequestInterceptor  attributes:{}", attributes.toString());
            HttpServletRequest request = attributes.getRequest();
            log.info("RequestInterceptor  request:{}", request.toString());
            //添加token
            requestTemplate.header("Authorization", request.getHeader("Authorization"));
        }
    }

}
