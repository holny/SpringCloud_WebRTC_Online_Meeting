package com.hly.july.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Linyuan Hou
 * @date 2021/3/12 18:34
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = {"com.hly.july.common.db.mapper"})
@ComponentScan(basePackages = {"com.hly.july.auth","com.hly.july.common.core","com.hly.july.common.auth","com.hly.july.common.db","com.hly.july.common.web"})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
