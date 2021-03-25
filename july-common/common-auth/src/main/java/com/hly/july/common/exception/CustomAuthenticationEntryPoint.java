package com.hly.july.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Linyuan Hou
 * @date 2021/3/16 13:51
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error(e.getMessage());
        Map map = new HashMap(2);

        Throwable cause = e.getCause();
        if (cause instanceof InvalidTokenException) {
            map.put("code", 10001);
            map.put("msg", "无效的 Access Token");
        } else if (cause instanceof InvalidGrantException) {
            map.put("code", 10002);
            map.put("msg", "无效的 Refresh Token");
        } else if (cause instanceof AccessDeniedException) {
            map.put("code", 10002);
            map.put("msg", "权限不足无法访问");
        } else {
            map.put("code", 10002);
            map.put("msg", "尚未认证无法访问");
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control","no-cache");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), map);
        } catch (Exception ec) {
            throw new ServletException(ec.getMessage());
        }


//
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        response.setContentType("application/json;charset=UTF-8");

//        Throwable cause = e.getCause();
//        if (cause instanceof InvalidTokenException) {
//            response.getWriter().print(JsonUtil.obj2Json(Result.failure(ResponseCode.UNAUTHORIZED, "无效的 Access Token")));
//        } else if (cause instanceof InvalidGrantException) {
//            response.getWriter().print(JsonUtil.obj2Json(Result.failure(ResponseCode.UNAUTHORIZED, "无效的 Refresh Token")));
//        } else if (cause instanceof AccessDeniedException) {
//            response.getWriter().print(JsonUtil.obj2Json(Result.failure(ResponseCode.FORBIDDEN, "权限不足无法访问")));
//        } else {
//            response.getWriter().print(JsonUtil.obj2Json(Result.failure(ResponseCode.UNAUTHORIZED, "尚未认证无法访问")));
//        }

        /*
        if (isAjaxRequest(request)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
        } else {
            response.sendRedirect("/login");
        }
        */
    }

     /*
    private static boolean isAjaxRequest(HttpServletRequest request) {
        if (request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").equals("XMLHttpRequest"))) {
            return true;
        }
        return false;
    }
    */
}
