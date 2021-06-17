import SockJS from "sockjs-client";
import Stomp from "stompjs";

export async function initJulyWS (wsEndpointURI,sendWSHeartBeatURI,interval) {
    let stompClient = await initWSConnection(wsEndpointURI)
    // 断开重连机制
    setInterval(() => {
        try {
            stompClient.send(sendWSHeartBeatURI, {},"testMsg")
        } catch (e) {
            console.log('WebSocket connection interrupt: ' + e)
            initWSConnection()
        }
    }, interval)
    return stompClient
}
function initWSConnection (wsEndpointURI) {
    // 建立连接对象
    // 连接服务端提供的通信接口，连接以后才可以订阅广播消息和个人消息
    let socket = null
    let stompClient
    socket = new SockJS(wsEndpointURI)// 后端提供协议字段
    stompClient = Stomp.over(socket)
    // socket.on('connection', (socket) => {
    //     console.log("connection")
    //     console.log(socket)
    // })
    socket.onerror = function(event) {
        console.error("WebSocket error observed:", event);
    };
    // socket.on('disconnect', (socket) => {
    //     console.log("disconnect")
    //     console.log(socket)
    // })
    // let socket = new SockJS('http://localhost:80/user/' + this.$store.getters.hostId+'?Authorization=' + this.$store.getters.hostToken)// 后端提供协议字段
    // stompClient.onopen = () => {
    //     console.log('localWebsocket打开')
    // }
    // stompClient.onerror = () => {
    //     console.log('localWebsocket错误')
    //     // 重连？
    // }
    // stompClient.onclose = (e) => {
    //     console.log('localWebsocket关闭' + e)
    // }
    // 获取STOMP子协议的客户端对象
    // this.stompClient.heartbeat.outgoing = 20000;  // client will send heartbeats every 20000ms
    // this.stompClient.heartbeat.incoming = 0;      // client does not want to receive heartbeats from the server  0表示客户端不从服务端接收心跳包
    // The heart-beating is using window.setInterval() to regularly send heart-beats and/or check server heart-beats
    // 拦截输出的一大堆垃圾信息
    // stompClient.debug = function (str) {
    //
    //     console.log(str)
    // }
    return stompClient
}

export async function subscribeJuly(stompClient,subscribeURI,headers){
    // let headers = {'Authorization': 'Bearer ' + this.$store.getters.hostToken}
    console.log('subscribe')
    stompClient.connect(
      headers, // headers
      function connectCallback () {
        // 订阅服务端消息 subscribe(destination url, callback[, headers])
          stompClient.subscribe(subscribeURI, (response) => { // 后端提供订阅地址
              console.log('获取到消息:' + response)
              return response
          })

        // __this.stompClient.subscribe('/topic/broadcast', (response) => { // 后端提供订阅地址
        //   console.log(response)// 接收后端response数据
        //   this.testText = response
        // })
      },
      function errorCallBack (error) {
          console.log('连接失败:' + error)
          return error
      }
    )
}

export function closeConnectionJuly(stompClient){
    console.log('closeConnectionJuly')
    if (stompClient != null) {
      stompClient.disconnect()
    }
}
