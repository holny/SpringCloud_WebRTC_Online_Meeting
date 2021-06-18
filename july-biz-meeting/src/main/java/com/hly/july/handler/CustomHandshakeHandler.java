package com.hly.july.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * @ClassName CustomHandshakeHandler
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/5/31 20:28
 * @Version 1.0.0
 **/
@Component
@Slf4j
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    // Custom class for storing principal
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
//        // Generate principal with UUID as name
//        HttpSession session = SpringContextUtils.getSession();
//        User loginUser = (User) session.getAttribute(Constants.SESSION_USER);
//
//        if(loginUser != null){
//            logger.debug(MessageFormat.format("WebSocket连接开始创建Principal，用户：{0}", loginUser.getUsername()));
//            return new MyPrincipal(loginUser.getUsername());
//        }else{
//            logger.error("未登录系统，禁止连接WebSocket");
//            return null;
//        }
        log.info("---------determineUser---------principal:{}", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        return SecurityContextHolder.getContext().getAuthentication();
    }
}
