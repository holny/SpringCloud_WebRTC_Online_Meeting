package com.hly.july.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 检测权限异常
 * 后到ResourceServerSecurityConfigurer资源服务中配置此异常处理类
 * @author Linyuan Hou
 * @date 2021/3/16 13:39
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.error(e.getMessage());

        //response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
//
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().print(JsonUtil.obj2Json(Result.failure(ResponseCode.FORBIDDEN, "权限不足无法访问")));

        ObjectMapper objectMapper = new ObjectMapper();
        Map map = new HashMap(2);
        map.put("code", 4000);
        map.put("msg", "权限不足无法访问");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control","no-cache");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}
