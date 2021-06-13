package com.hly.july;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName BizMeetingApplication
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/5/29 15:48
 * @Version 1.0.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = {"com.hly.july.common.biz.mapper", "com.hly.july.mapper"})
@ComponentScan(basePackages = {"com.hly.july.common.biz","com.hly.july.common", "com.hly.july"})
public class BizMeetingApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizMeetingApplication.class, args);
    }
}
