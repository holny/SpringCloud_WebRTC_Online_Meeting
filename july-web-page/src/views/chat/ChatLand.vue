<template>
  <div class="q-pa-md chat-root-container">
    <div class="row justify-center">
      <div class="col-2 contacts-main-container float-left">
        <contacts v-if="contactsShow" :stomp-client="julyWebsocket.constant.stompClient" :connection-flag="this.julyWebsocket.variable.connectionFlag"/>
      </div>
      <div class="col-4 chat-window-main-container">
        <chat-window v-if="chatWindowShow" :stomp-client="julyWebsocket.constant.stompClient" :peer-id="chatPeerId" :connection-flag="this.julyWebsocket.variable.connectionFlag"/>
      </div>
    </div>
  </div>
</template>

<script>
import contacts from "@/views/chat/contacts";
import chatWindow from "@/views/chat/chatWindow";
import {initJulyWS, closeConnectionJuly} from "@/utils/socket";
import {getToken} from "@/utils/auth";
// import {date} from "quasar";
export default {
  name: "ChatLand",
  components: {
    contacts,
    chatWindow
  },
  data () {
    return {
      contactsShow: false,
      chatWindowShow: false,
      chatPeerId: '',
      julyWebsocket:{
        constant: {
          WSEndPointURI:  'http://localhost:80/meeting/endpointWS?Authorization=' + getToken(),
          sendWSHeartBeatURI: '/app/heartbeat',
          subWSHeartBeatURI: '/user/topic/heartbeat',
          stompClient: null
        },
        variable: {
          connectionFlag: {'websocket':{'name':'websocket',"status":false}},
          headers: {'Authorization': 'Bearer ' + getToken()}
        },
      },
    }
  },
  created() {
    this.initWSEnv()
  },
  mounted () {
    this.initWSWatcher()
  },
  // keep-alive会缓存组件状态，activated()和 deactivated() 这两个钩子函数。activated()是keep-alive 组件激活时调用，而 deactivated() 是 keep-alive 组件停用时调用。activated ()替换mounted()
  activated () {
  },
  destroyed() {
    closeConnectionJuly(this.julyWebsocket.constant.stompClient)
  },
  methods: {
    initWSEnv() {
      if (this.julyWebsocket.constant.stompClient == null) {
        console.log("start init chatLand websocket")
        this.julyWebsocket.constant.stompClient = initJulyWS(this.julyWebsocket.constant.WSEndPointURI, this.julyWebsocket.constant.sendWSHeartBeatURI)
      }
    },
    initWSWatcher(){
      let _that = this
      this.julyWebsocket.constant.stompClient.connect(
          _that.julyWebsocket.variable.headers,
          function connectCallback () {
            _that.contactsShow = true
            _that.chatWindowShow = true
            // 订阅stompClient心跳检测
            _that.julyWebsocket.constant.stompClient.subscribe(_that.julyWebsocket.constant.subWSHeartBeatURI, () => {
              _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
            })
          },
          function errorCallBack (error) {
            _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = false
            console.log('订阅失败:' + error)
          }
      )
    },
    changeChatPeer(peerInfo){
      console.log('changeChatPeer')
      console.log(peerInfo)
    }
    // bookMarkPeer(peerId){
    //   console.log('bookMarkPeer:'+peerId)
    //   this.chatPeerId = peerId
    // },
    // startChatPeer(peerId){
    //   console.log('startChatPeer:'+peerId)
    //   this.chatPeerId = peerId
    // },
  }
}
</script>

<style scoped>
.col,.col-4{
  height: 900px;
}
.chat-root-container{
  min-width: 100%;
  max-width: max-content;
  max-height: max-content;
}
.contacts-main-container {
  min-width: 400px;
  max-height: max-content;
}
.chat-window-main-container {
  min-width: 700px;
}
</style>
