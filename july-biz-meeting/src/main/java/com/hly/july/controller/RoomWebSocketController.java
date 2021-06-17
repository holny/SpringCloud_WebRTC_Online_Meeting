package com.hly.july.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hly.july.common.biz.result.Result;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.common.biz.util.DateUtils;
import com.hly.july.entity.*;
import com.hly.july.service.impl.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RestController;

/**
 * STOMP 的消息根据前缀的不同分为三种。如下，以 /app 开头的消息都会被路由到带有@MessageMapping 或 @SubscribeMapping 注解的方法中；以/topic 或 /queue 开头的消息都会发送到STOMP代理中，根据你所选择的STOMP代理不同，目的地的可选前缀也会有所限制；以/user开头的消息会将消息重路由到某个用户独有的目的地上。
 * @ClassName WebSocketController
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/5/29 17:25
 * @Version 1.0.0
 **/
@RestController
@Slf4j
public class RoomWebSocketController {
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private RoomService roomService;

    @SubscribeMapping({"/room/order/{type}"})
    public Result<Room> subscribeCreateRoom(@DestinationVariable String type, OAuth2Authentication auth2Authentication) {
        log.info("subscribeCreateRoom type:{}",type);
        if (auth2Authentication!=null){
            String hostId =auth2Authentication.getPrincipal().toString();
            if (StringUtils.isNotBlank(hostId)){
                log.info("subscribeCreateRoom hostId:{}",hostId);
                return roomService.createRoom(hostId,null,type);
            }else{
                return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
        }
    }

    @SubscribeMapping({"/room/{roomId}/join"})
    public Result<Room> requestJoinRoom(@DestinationVariable String roomId, OAuth2Authentication auth2Authentication) {
        log.info("requestJoinRoom roomId:{}",roomId);
        if (auth2Authentication!=null){
            String hostId =auth2Authentication.getPrincipal().toString();
            if (StringUtils.isNotBlank(hostId)){
                log.info("requestJoinRoom hostId:{}",hostId);
                Result<Room> result = roomService.addMemberIntoRoom(roomId,hostId);
                if (result.getCode()==ResultCode.SUCCESS.getCode()){
                    roomService.sendRoomEvent(result.getData().getRoomId(),Event.buildBroadCast(EventEnum.EVENT_CHAT_JOIN,result.getData().getRoomId(),result.getData()));
                }else if(result.getCode()==ResultCode.WEBSOCKET_USER_EXIST.getCode()){
                    result.setCode(ResultCode.SUCCESS.getCode());
                }
                log.info("requestJoinRoom result:{}",result.toString());
                return result;
            }else{
                return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
        }

    }

    @SubscribeMapping({"/room/{roomId}/leave"})
    public Result<Room> requestLeaveRoom(@DestinationVariable String roomId, OAuth2Authentication auth2Authentication) {
        log.info("requestLeaveRoom roomId:{}",roomId);
        if (auth2Authentication!=null){
            String hostId =auth2Authentication.getPrincipal().toString();
            if (StringUtils.isNotBlank(hostId)){
                log.info("requestLeaveRoom hostId:{}",hostId);
                Result<Room> result = roomService.deleteMemberFromRoom(roomId,hostId);
                if (result.getCode()==ResultCode.SUCCESS.getCode()){
                    roomService.sendRoomEvent(result.getData().getRoomId(),Event.buildBroadCast(EventEnum.EVENT_CHAT_LEAVE,result.getData().getRoomId(),result.getData()));
                }
                return result;
            }else{
                return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
        }

    }

    @MessageMapping({"/room/{roomId}/sendmsg"})
    public Result<String> sendRoomMessage(@DestinationVariable String roomId,MessageVO message, OAuth2Authentication auth2Authentication) {
        log.info("sendRoomMessage roomId:{}, message:{}",roomId,message.toString());
        if (auth2Authentication!=null) {
            String hostId = auth2Authentication.getPrincipal().toString();
//            String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            if (StringUtils.isNotBlank(hostId)){
                log.info("sendRoomMessage hostId:{}",hostId);
                message.setFrom(hostId);
                message.setGmtCreate(DateUtils.getCurrentDateTime());

                return roomService.sendRoomMessage(roomId,message);
            }else{
                return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
            }
        }else{
            return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
        }

    }

//    @MessageMapping({"/personal/{userId}/sendmsg"})
//    public Result<String> sendPersonalMessage(@DestinationVariable String userId,Message message, OAuth2Authentication auth2Authentication) {
//        log.info("sendPersonalMessage userId:{}, message:{}",userId,message.toString());
//        if (auth2Authentication!=null) {
//            String hostId = auth2Authentication.getPrincipal().toString();
////            String hostId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//            if (StringUtils.isNotBlank(hostId)&&StringUtils.isNotBlank(userId)){
//                log.info("sendPersonalMessage hostId:{}",hostId);
//                message.setFrom(hostId);
//                message.setTo(userId);
//                message.setMethod("personal");
//                // Todo:要确定对方是否存在
//                message.setGmtCreate(DateUtils.getCurrentDateTime());
//                return roomService.sendPersonalMessage(userId,hostId,message);
//            }else{
//                return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
//            }
//        }else{
//            return Result.failure(ResultCode.AUTH_NEED_LOGIN,"未登录");
//        }
//
//    }
//
//    /**
//     * @MessageMapping 指定目的地是“/app/marco”（“/app”前缀是隐含的，因为我们将其配置为应用的目的地前缀）。
//     * @param incoming
//     * @return
//     */
//    @MessageMapping("/room/{roomId}") // @MessageMapping 和 @RequestMapping 功能类似，浏览器向服务器发起消息，映射到该地址。  表示服务端可以接收客户端通过向地址“/hello”发送过来的消息。
////    @SendTo("/topic/room/{roomId}")  // 如果服务器接受到了消息，就会对订阅了 @SendTo 括号中的地址的浏览器发送消息。@SendTo表示此方法会向订阅”/topic/hello”的用户广播message消息。@SendTo("/topic/hello")注解等同于使用SimpMessagingTemplate.convertAndSend("/topic/hello", new Response("你好" ));客户端通过stomp.subscribe("/topic/hello", handleFunction);方法订阅的地方都能收到消息。
//    public Result<String> roomChat(Message message) {
//        log.info("roomChat message:{}",message.toString());
//        return Result.success("SendTo服务端接收到你发的："+incoming.toString());
//    }
//
//    /**
//     * 用的是@SendToUser，这就是发送给单一客户端的标志. 本例中，客户端接收一对一消息的主题应该是“/user/message” ,”/user/”是固定的搭配，服务器会自动识别。
//     * @SendToUser("/message") 等同于使用SimpMessagingTemplate.convertAndSendToUser(Key,"/message", "新消息");
//     * 客户端通过stomp.subscribe("/user/topic/msg", handleFunction);方法订阅的并且注册时返回的username=Key时才能收到消息。
//     */
//    @MessageMapping("/message")
//    @SendToUser("/topic/msg")
//    public  Result<String> userMessage(Shout incoming) throws Exception {
//        log.info("userMessage incoming:{}",incoming.toString());
//        return Result.success("SendToUser服务端接收到你发的："+incoming.toString());
//    }
//
//    @GetMapping("/sendMsgToUser")
//    public Result<String> sendMsgByUser(@RequestBody Shout incoming) {
//        log.info("sendMsgByUser incoming:{}",incoming.toString());
//        // /user/{name}/hello
//        simpMessagingTemplate.convertAndSendToUser("zhangsan", "/hello", incoming.toString());
//        return Result.success();
//    }
//
//    @GetMapping("/sendMsgToAll")
//    public Result<String> sendMsgByAll(@RequestBody Shout incoming) {
//        log.info("sendMsgByAll incoming:{}",incoming);
//        // /topic/info/{classId}
//        simpMessagingTemplate.convertAndSend("/topic/info/");
//        return Result.success();
//    }
//
//    @MessageExceptionHandler(Exception.class)
//    @SendToUser("/queue/errors")
//    public Exception handleExceptions(Exception t){
//        log.info("handleExceptions t:{}",t.getMessage());
//        return t;
//    }

}
