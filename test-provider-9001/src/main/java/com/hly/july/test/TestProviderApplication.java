package com.hly.july.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Linyuan Hou
 * @date 2021/3/9 16:48
 */
@EnableDiscoveryClient
@SpringBootApplication
public class TestProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestProviderApplication.class, args);
    }
}
