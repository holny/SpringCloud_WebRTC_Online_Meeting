package com.hly.july.biz.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Linyuan Hou
 * @date 2021/4/24 16:34
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = {"com.hly.july.common.db.mapper","com.hly.july.biz.user.mapper"})
@ComponentScan(basePackages = {"com.hly.july.biz.user","com.hly.july.common.core","com.hly.july.common.auth","com.hly.july.common.db","com.hly.july.common.web"})
public class BizUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizUserApplication.class, args);
    }
}
