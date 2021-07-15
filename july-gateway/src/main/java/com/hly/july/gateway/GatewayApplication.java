package com.hly.july.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author Linyuan Hou
 * @date 2021/3/11 20:24
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.hly.july.common.db.mapper"})
@ComponentScan(basePackages = {"com.hly.july.gateway","com.hly.july.common.core","com.hly.july.common.auth","com.hly.july.common.db"})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
