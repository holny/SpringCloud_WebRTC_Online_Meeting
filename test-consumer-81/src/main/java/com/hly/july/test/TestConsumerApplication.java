package com.hly.july.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Linyuan Hou
 * @date 2021/3/9 16:53
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.hly.july.common", "com.hly.july.test"})
public class TestConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestConsumerApplication.class, args);
    }
}
