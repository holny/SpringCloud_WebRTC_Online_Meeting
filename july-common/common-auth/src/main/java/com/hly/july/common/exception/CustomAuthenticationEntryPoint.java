package com.hly.july.common.exception;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hly.july.common.result.Result;
import com.hly.july.common.result.ResultCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 检测Token异常
 * 后到ResourceServerSecurityConfigurer资源服务中配置此异常处理类
 * @author Linyuan Hou
 * @date 2021/3/16 13:51
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error(e.getMessage());
        Result<String> result ;
        Throwable cause = e.getCause();
        log.error("get Throwable case class:{}",cause.getClass());
        log.error("get Throwable case:{}",cause.getStackTrace());

        if (cause instanceof InvalidTokenException) {
            result = Result.failure(ResultCode.TOKEN_INVALID);
        } else if (cause instanceof InvalidGrantException) {
            result = Result.failure(ResultCode.TOKEN_INVALID_REFRESH);
        } else if (cause instanceof AccessDeniedException) {
            result = Result.failure(ResultCode.TOKEN_ACCESS_FORBIDDEN);
        } else if (cause instanceof ExpiredJwtException)  {
            result = Result.failure(ResultCode.TOKEN_EXPIRED);
        }else{
            result =  Result.failure(ResultCode.TOKEN_INVALID);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), result);
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
