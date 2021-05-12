package com.hly.july;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @author Linyuan Hou
 * @date 2021/3/11 20:24
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.hly.july.common.biz.mapper", "com.hly.july.mapper"})
@ComponentScan(basePackages = {"com.hly.july.common.biz","com.hly.july.common", "com.hly.july"})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
