package com.hly.july.config;


import com.hly.july.handler.CustomHandshakeHandler;
import com.hly.july.interceptor.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
/**
 * STOMP: stomp是WebSocket的一个子协议,这种方式功能更加强大，可以使用消息代理，对于发送的消息可以使用类似springMvc的处理方式，同时消息的发送变成了订阅的模式，可以很方便的进行群发。
 * https://www.jianshu.com/p/57fbfadacfeb
 * @ClassName WebSocketConfig
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/5/29 16:59
 * @Version 1.0.0
 **/
@Configuration
@EnableWebSocketMessageBroker // 注解表明： 这个配置类不仅配置了 WebSocket，还配置了基于代理的 STOMP消息；
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private CustomHandshakeHandler customHandshakeHandler;

    @Autowired
    private WebSocketInterceptor webSocketInterceptor;
    /**
     * 复写了 registerStompEndpoints() 方法：添加一个服务端点，来接收客户端的连接。将 "/endpointChat" 路径注册为 STOMP 端点。
     * 这个路径与发送和接收消息的目的路径有所不同， 这是一个端点，客户端在订阅或发布消息到目的地址前，要连接该端点，
     * 即用户发送请求 ：url="/127.0.0.1:8080/endpointChat" 与 STOMP server 进行连接，之后再转发到订阅url；
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 配置客户端尝试连接地址
        registry.
                addEndpoint("/meeting/endpointWS").     // 设置连接节点，前端请求的建立连接的地址就是 http://ip:端口/ws
                addInterceptors(webSocketInterceptor).     // 设置握手拦截器
                setAllowedOrigins("*").     // 配置跨域
                setHandshakeHandler(customHandshakeHandler).
                withSockJS();       // 开启sockJS支持，这里可以对不支持stomp的浏览器进行兼容。
    }

    /**
     * 复写了 configureMessageBroker() 方法：
     * 配置了一个 简单的消息代理，通俗一点讲就是设置消息连接请求的各种规范信息。
     * 发送应用程序的消息将会带有 “/app” 前缀。
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 消息代理,这里配置自带的消息代理，也可以配置其它的消息代理
        // 一定要注意这里的参数，可以理解为开启通道,后面如果使用了"/XXX"来作为前缀，这里就要配置,同时这里的"/topic"是默认群发消息的前缀,前端在订阅群发地址的时候需要加上"/topic"
        //定义了一个（或多个）客户端订阅地址的前缀信息，也就是客户端接收服务端发送消息的前缀信息
        registry.enableSimpleBroker("/queue","/topic");
        // 客户端向服务端发送消息需有的前缀,需要什么样的前缀在这里配置,但是不建议使用，这里跟下面首次订阅返回会有点冲突，如果不需要首次订阅返回消息，也可以加上消息前缀
        //定义了服务端接收地址的前缀，也即客户端给服务端发消息的地址前缀
        // 将应用的前缀设置为“/app”。所有目的地以“/app”打头的消息都将会路由到带有@MessageMapping @SubscribeMapping注解的方法中，而不会发布到代理队列或主题中。
         registry.setApplicationDestinationPrefixes("/app");
        // 配置单发消息的前缀 /user，前端订阅一对一通信的地址需要加上"/user"前缀，不设置的话，默认也是/user/
        registry.setUserDestinationPrefix("/user");
    }

    @Bean
    public WebSocketInterceptor getWebSocketInterceptor() {
        return new WebSocketInterceptor();
    }
}
