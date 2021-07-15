package com.hly.july.biz.video;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = {"com.hly.july.common.db.mapper"})
@ComponentScan(basePackages = {"com.hly.july.biz.video","com.hly.july.common.core","com.hly.july.common.auth","com.hly.july.common.db","com.hly.july.common.web"})
public class BizVideoApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizVideoApplication.class, args);
    }
}
