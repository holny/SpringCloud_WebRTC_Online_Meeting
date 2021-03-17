package com.hly.july.test.service;

import com.hly.july.common.entity.CommonResult;
import com.hly.july.common.entity.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Linyuan Hou
 * @date 2021/3/9 16:57
 */
@FeignClient(value = "nacos-provider")
public interface ProviderService {

    @GetMapping(value = "/provider/{id}")
    public CommonResult<Payment> payment(@PathVariable("id") Long id);
}
