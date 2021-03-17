package com.hly.july.test.controller;


import com.hly.july.common.entity.CommonResult;
import com.hly.july.common.entity.Payment;
import com.hly.july.test.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Linyuan Hou
 * @date 2021/3/9 19:08
 */
@RestController
@Slf4j
public class ConsumerController {
    @Resource
    private ProviderService providerService;

    @Value("${server.port}")
    private String serverPort;

    @PreAuthorize("hasAnyAuthority('UPDATE')")
    @GetMapping(value="/consumer/payment/{id}")
    public CommonResult<Payment> payment(@PathVariable("id") Long id){
        CommonResult<Payment> payment = providerService.payment(id);
        log.info("Consumer payment:{} serverPort:{}",payment,serverPort);
        payment.setMessage("consumer port:"+serverPort+" -- "+payment.getMessage());
        return payment;
    }

    @PreAuthorize("hasAnyAuthority('DELETE')")
    @GetMapping(value="/consumer/test/{id}")
    public CommonResult<Payment> test(@PathVariable("id") Long id){
        CommonResult<Payment> payment = providerService.payment(id);
        log.info("Consumer test:{} serverPort:{}",payment,serverPort);
        payment.setMessage("test port:"+serverPort+" -- "+payment.getMessage());
        return payment;
    }
}
