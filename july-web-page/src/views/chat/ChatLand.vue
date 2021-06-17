<template>
  <div class="q-pa-md chat-root-container">
    <div class="row justify-center">
      <div class="col-2 contacts-main-container float-left">
        <contacts v-if="contactsShow&&hostInfo!=null"  :host-info="hostInfo" :stomp-client="julyWebsocket.constant.stompClient" :connection-flag="this.julyWebsocket.variable.connectionFlag" v-on:changePeer="changeChatPeer"/>
      </div>
      <div class="col-4 chat-window-main-container">
        <chat-window v-if="chatWindowShow&&chatPeerId!=null&&chatPeerType!=null&&hostInfo!=null" :host-info="hostInfo" :stomp-client="julyWebsocket.constant.stompClient" :peer-id="chatPeerId" :peer-type="chatPeerType" :connection-flag="this.julyWebsocket.variable.connectionFlag"/>
      </div>
    </div>
  </div>
</template>

<script>
import contacts from "@/views/chat/contacts";
import chatWindow from "@/views/chat/chatWindow";
import {getHostId} from "@/utils/auth";
import {initJulyWS, closeConnectionJuly} from "@/utils/socket";
import {JULY,FUN} from "@/utils/julyCommon";
// import {date} from "quasar";
export default {
  name: "ChatLand",
  components: {
    contacts,
    chatWindow
  },
  data () {
    return {
      hostId: getHostId(),
      hostInfo: null,
      contactsShow: false,
      chatWindowShow: false,
      chatPeerId: null,
      chatPeerType: null,
      julyWebsocket:{
        constant: {
          stompClient: null
        },
        variable: {
          connectionFlag: {'websocket':{'name':'websocket',"status":false}}
        },
      },
    }
  },
  async created() {
    this.hostInfo = await this.getUserInfo(this.hostId)
    await this.initWSEnv()
  },
  mounted () {

  },
  // keep-alive会缓存组件状态，activated()和 deactivated() 这两个钩子函数。activated()是keep-alive 组件激活时调用，而 deactivated() 是 keep-alive 组件停用时调用。activated ()替换mounted()
  activated () {
  },
  destroyed() {
    closeConnectionJuly(this.julyWebsocket.constant.stompClient)
  },
  methods: {
    async initWSEnv() {
      if (this.julyWebsocket.constant.stompClient == null) {
        console.log("start init chatLand websocket")
        this.julyWebsocket.constant.stompClient = await initJulyWS(JULY.WEBSOCKET_URI_ENDPOINT, JULY.WEBSOCKET_URI_SEND_HEARTBEAT,JULY.WEBSOCKET_URI_SEND_HEARTBEAT_INTERVAL)
      }
      this.initWSConnectionHeartBeatWatcher()
    },
    initWSConnectionHeartBeatWatcher(){
      let _that = this
      this.julyWebsocket.constant.stompClient.connect(
          JULY.WEBSOCKET_HEADERS,
          function connectCallback () {
            _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
            _that.contactsShow = true
            _that.chatWindowShow = true
            // 订阅stompClient心跳检测
            _that.julyWebsocket.constant.stompClient.subscribe(JULY.WEBSOCKET_URI_SUBSCRIBE_HEARTBEAT, () => {
              _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
            })
          },
          function errorCallBack () {
            _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = false
            FUN.notify("初始连接websocket失败",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
          }
      )
    },
    changeChatPeer(peerId,peerType){
      console.log("Chat land changeChatPeer")
      console.log(peerId)
      this.chatPeerId = peerId
      this.chatPeerType = peerType
    },
    async getUserInfo (userId) {
      let result=null
      await this.$store.dispatch('user/getUserInfo', userId)
          .then((data) => {
            // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
            console.log('got user info successful')
            data.gender = FUN.convertPrintGender(data.peerGender)
            data.role = FUN.filterPrintRole(data.peerRole)
            result = data
          })
          .catch(() => {
            console.log('got user info fail')
            FUN.notify("无法获取当前用户信息",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            result = null
          })
      return result
    },
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
