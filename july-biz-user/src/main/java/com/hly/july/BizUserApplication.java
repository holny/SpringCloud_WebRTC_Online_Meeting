package com.hly.july;

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
@MapperScan(basePackages = {"com.hly.july.common.biz.mapper", "com.hly.july.mapper"})
@ComponentScan(basePackages = {"com.hly.july.common.biz","com.hly.july.common", "com.hly.july"})
public class BizUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizUserApplication.class, args);
    }
}
