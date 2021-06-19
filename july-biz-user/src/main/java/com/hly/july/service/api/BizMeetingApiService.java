package com.hly.july.service.api;

import com.hly.july.common.biz.config.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import java.util.Map;

/**
 * @author Linyuan Hou
 * @date 2021/5/9 15:48
 */
@FeignClient(value = "meeting-service", configuration = FeignInterceptor.class)
public interface BizMeetingApiService {
}
