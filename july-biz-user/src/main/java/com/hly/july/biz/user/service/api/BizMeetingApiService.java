package com.hly.july.biz.user.service.api;

import com.hly.july.common.web.config.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Linyuan Hou
 * @date 2021/5/9 15:48
 */
@FeignClient(value = "meeting-service", configuration = FeignInterceptor.class)
public interface BizMeetingApiService {
}
