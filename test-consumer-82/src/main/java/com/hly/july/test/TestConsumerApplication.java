package com.hly.july.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Linyuan Hou
 * @date 2021/3/9 16:53
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class TestConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestConsumerApplication.class, args);
    }
}
