package com.hly.july.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Linyuan Hou
 * @date 2021/3/9 11:17
 * # ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
 * # nacos-config-client-dev.yaml
 *
 * # nacos-config-client-test.yaml   ----> config.info
 */
@RestController
@RefreshScope
@Slf4j
public class ConfigClientController {
    @Value("${store.db.url:test}")
    private String configInfo;

    @GetMapping("/config/info")
    public String getConfigInfo() {
        log.info("new getconfig:{}",configInfo);
        return configInfo;
    }
}
