<template>
  <div class=" chat-root-container">
    <div class="row full-width no-wrap q-gutter-x-md">
        <contacts class="contacts-main-container" ref="contacts" v-if="contactsShow&&hostInfo!=null"  :host-info="hostInfo" :stomp-client="julyWebsocket.constant.stompClient" :connection-flag="this.julyWebsocket.variable.connectionFlag" :peer-id="chatPeerId" :peer-type="chatPeerType" v-on:changePeer="changeChatPeer"/>
        <div class="column">
          <q-slide-transition >
            <meeting class="q-mb-md" v-if="meetingWindowShow&&chatPeerId!=null&&chatPeerType!=null&&hostInfo!=null" :host-info="hostInfo" :session-id="callSessionId" :stomp-client="julyWebsocket.constant.stompClient" :peer-info="meetingPeerInfo" :connection-flag="this.julyWebsocket.variable.connectionFlag"></meeting>
          </q-slide-transition>
          <div class="col">
            <chat-window class="chat-window-main-container" v-if="chatWindowShow&&chatPeerId!=null&&chatPeerType!=null&&hostInfo!=null" :host-info="hostInfo" :stomp-client="julyWebsocket.constant.stompClient" :peer-id="chatPeerId" :peer-type="chatPeerType" :connection-flag="this.julyWebsocket.variable.connectionFlag" :message-window-height="messageWindowHeight" v-on:whenNewMsg="whenNewMsg" v-on:startOrEndMeeting="startOrEndMeeting"/>
          </div>
        </div>


    </div>
    <q-dialog v-model="callInDialog" persistent transition-show="scale" transition-hide="scale">
      <q-card  style="width: auto;min-width: 400px">
        <q-card-section class=" bg-teal text-white" style="height: 50px">
          <div class="text-h6">呼入请求</div>
        </q-card-section>

        <q-card-section class="full-width justify-center no-wrap q-gutter-lg">
          <q-chip class="inline" size="20px" color="warning">
            <q-avatar>
              <img :src="meetingPeerInfo!=null?meetingPeerInfo.avatar:''">
            </q-avatar>
            {{meetingPeerInfo!=null?(meetingPeerInfo.remarkName!=null?meetingPeerInfo.remarkName:meetingPeerInfo.userName):''}}{{meetingPeerInfo!=null?(meetingPeerInfo.remarkName!=null?'('+meetingPeerInfo.userName+')':''):''}}
          </q-chip>
          <q-icon name="eva-arrowhead-right-outline" style="font-size: 2em"  class="animate__animated animate__lightSpeedInLeft  animate__infinite"/>
          <!--          <q-spinner-infinity color="lime" />-->
          <q-chip class="inline" size="20px" color="positive">
            <q-avatar>
              <img :src="hostInfo!=null?hostInfo.avatar:''">
            </q-avatar>
            Me
          </q-chip>
        </q-card-section>

        <q-card-actions align="right" class="bg-white text-teal">
          <q-btn class="btn-small" flat round color="green" icon="videocam" @click="acceptCallIn"  v-close-popup/>
          <q-btn class="btn-small" flat round color="negative" icon="do_not_disturb_on"  @click="hangupCall" v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="callOutDialog" persistent transition-show="scale" transition-hide="scale">
      <q-card style="width: auto;min-width: 400px">
        <q-card-section class=" bg-teal text-white" style="height: 50px">
          <div class="text-h6">等待接受</div>
        </q-card-section>

        <q-card-section class="full-width justify-center no-wrap q-gutter-lg">
          <q-chip class="inline" size="20px" color="positive">
            <q-avatar>
              <img :src="hostInfo!=null?hostInfo.avatar:''">
            </q-avatar>
            Me
          </q-chip>
          <q-icon name="eva-arrowhead-right-outline" style="font-size: 2em"  class="animate__animated animate__lightSpeedInLeft  animate__infinite"/>
<!--          <q-spinner-infinity color="lime" />-->
          <q-chip class="inline" size="20px" color="warning">
            <q-avatar>
              <img :src="meetingPeerInfo!=null?meetingPeerInfo.avatar:''">
            </q-avatar>
            {{meetingPeerInfo!=null?(meetingPeerInfo.remarkName!=null?meetingPeerInfo.remarkName:meetingPeerInfo.userName):''}}{{meetingPeerInfo!=null?(meetingPeerInfo.remarkName!=null?'('+meetingPeerInfo.userName+')':''):''}}
          </q-chip>
        </q-card-section>
        <q-card-actions align="right" class="bg-white text-teal">
          <q-btn class="btn-small" flat round color="negative" icon="do_not_disturb_on" @click="hangupCall" v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import Contacts from "@/views/chat/contacts";
import ChatWindow from "@/views/chat/chatWindow";
import {getHostId} from "@/utils/auth";
import {initJulyWS, closeConnectionJuly} from "@/utils/socket";
import {JULY,FUN} from "@/utils/julyCommon";
import meeting from "@/views/meeting/meeting";
import {CONSTANT, EVENT_CODE} from "@/utils/constant";
import {isNotEmpty} from "@/utils/validate";
// import meeting from "@/views/meeting/meeting";
// import {date} from "quasar";
export default {
  name: "ChatLand",
  components: {
    Contacts,
    ChatWindow,
    meeting
  },
  data () {
    return {
      hostId: getHostId(),
      hostInfo: null,
      callInDialog:false,
      callOutDialog:false,
      callSessionId: null,
      hostStatusObject:{
        userId: getHostId(),
        status: CONSTANT.USER_ACTIVE_STATUS_VISIBLE,
      },
      contactsShow: false,
      chatWindowShow: false,
      meetingWindowShow: false,
      chatPeerId: null,
      chatPeerType: null,
      meetingPeerInfo: null,
      messageWindowHeight: '575px',
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
  computed: {
    receiveNotifyURI() {
      return '/user/topic/meeting/notify/' + this.hostInfo.userId
    },
    sendVideoCallURI() {
      return '/app/videoCall/' + this.meetingPeerInfo.userId
    },
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
        this.julyWebsocket.constant.stompClient = await initJulyWS(JULY.WEBSOCKET_URI_ENDPOINT, JULY.WEBSOCKET_URI_SEND_HEARTBEAT,JULY.WEBSOCKET_URI_SEND_HEARTBEAT_INTERVAL,this.hostStatusObject)
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
            _that.initEventNotifyWatcher()
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
    whenNewMsg(peerId,msgDate){
      console.log("ChatLand whenNewMsg")
      console.log(peerId)
      console.log(msgDate)
      this.$refs.contacts.updateOneRecentContactView(peerId,msgDate)
    },
    changeChatPeer(peerId,peerType){
      console.log("Chat land changeChatPeer")
      console.log(peerId)
      this.chatPeerId = peerId
      this.chatPeerType = peerType
      this.startOrEndMeeting(null,'end')
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
    acceptCallIn(){
      // 接受视频会议请求的话就先打开chat聊天框
      if(this.callSessionId!=null){
        this.chatPeerId = this.meetingPeerInfo.userId
        this.chatPeerType = this.meetingPeerInfo.type
        this.startOrEndMeeting(this.meetingPeerInfo,'start')
      }else {
        FUN.notify("没有找到会话,无法接受请求", FUN.NOTIFY_LEVEL_ERROR, FUN.NOTIFY_POSITION_TOP)
      }
    },
    hangupCall(){
      if( this.meetingWindowShow){
        // 如果meetingWindow是显示的，直接关闭window，meeting的destroy()会自动发送关闭session的请求
        this.startOrEndMeeting(null,'end')
      }else{
        if(this.callSessionId!=null){
          // 如果meetingWindow不是显示的,并且有sessionId，需自助关闭
          let init_connection_params = {
            'sessionId': this.callSessionId,
            'isAccept': false,
          }
          this.julyWebsocket.constant.stompClient.send(this.sendVideoCallURI, {},JSON.stringify(init_connection_params))
        }
      }
    },
    startOrEndMeeting(meetingPeerInfo,action){
      console.log('startOrEndMeeting')
      console.log(meetingPeerInfo)
      if(action==='start'){
        this.meetingWindowShow = true
        this.meetingPeerInfo = meetingPeerInfo
        this.messageWindowHeight = '275px'
      }else if(action==='end'){
        this.meetingWindowShow = false
        this.meetingPeerInfo = null
        this.callType = null
        this.messageWindowHeight = '575px'
        this.callInDialog= false
        this.callOutDialog= false
        this.callSessionId=null
      }
    },
    initEventNotifyWatcher(){
      console.log("initEventNotifyWatcher")
      let _that = this
      _that.julyWebsocket.constant.stompClient.subscribe(_that.receiveNotifyURI, (response) => {
        console.log("收到event")
        _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
        let feedback = JSON.parse(response.body)
        console.log(feedback)
        if (feedback.type=== CONSTANT.SHOUTING_EVENT){
          // type=== 'message' 说明是来了新事件
          // let feedbackTS = date.formatDate(date.extractDate(feedback.gmtCreate,CONSTANT.DATE_FORMAT) , 'x')
          if (feedback.code===EVENT_CODE.EVENT_CALL_RING){
            console.log("EVENT_CALL_RING  铃声Ing")
            _that.callInDialog= false
            _that.callOutDialog= true
            _that.callSessionId = feedback.data.sessionId
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_IN){
            console.log("EVENT_CALL_IN  有新呼叫进入")
            _that.callInDialog= true
            _that.callOutDialog= false
            _that.callSessionId = feedback.data.sessionId
            // 获取到PeerInfo
            _that.meetingPeerInfo = {}
            _that.meetingPeerInfo.userId = feedback.data.data.peerId
            _that.meetingPeerInfo.type = feedback.data.data.peerType
            _that.meetingPeerInfo.remarkName = feedback.data.data.remarkName
            _that.meetingPeerInfo.tag = feedback.data.data.tag
            _that.meetingPeerInfo.userName = feedback.data.data.peerUserName
            _that.meetingPeerInfo.avatar = feedback.data.data.peerAvatar
            if(isNotEmpty(feedback.data.data.gmtLastContact)){
              _that.meetingPeerInfo.gmtLastContact = feedback.data.data.gmtLastContact
            }else{
              _that.meetingPeerInfo.gmtLastContact = null
            }
            if(isNotEmpty(feedback.data.data.peerGmtLastLogin)){
              _that.meetingPeerInfo.peerGmtLastLogin = feedback.data.data.peerGmtLastLogin
            }else{
              _that.meetingPeerInfo.peerGmtLastLogin = null
            }
            _that.meetingPeerInfo.nickName = feedback.data.data.peerNickName
            _that.meetingPeerInfo.gender = FUN.convertPrintGender(feedback.data.data.peerGender)
            _that.meetingPeerInfo.role = FUN.filterPrintRole(feedback.data.data.peerRole)
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_FAIL){
            console.log("EVENT_CALL_FAIL  呼叫失败")
            FUN.notify(feedback.message,FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
            _that.startOrEndMeeting(null,'end')
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_BUSY){
            console.log("EVENT_CALL_BUSY  呼叫对方忙")
            FUN.notify(feedback.message,FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
            _that.startOrEndMeeting(null,'end')
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_OFFLINE){
            console.log("EVENT_CALL_OFFLINE  呼叫对方不在线")
            FUN.notify(feedback.message,FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
            _that.startOrEndMeeting(null,'end')
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_DENY){
            console.log("EVENT_CALL_DENY  呼叫对方拒绝")
            FUN.notify(feedback.message,FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
            _that.startOrEndMeeting(null,'end')
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_PRE_CONNECT){
            console.log("EVENT_CALL_PRE_CONNECT  呼叫预建立连接")
            _that.callInDialog= false
            _that.callOutDialog= false
            if(this.callSessionId===feedback.data.sessionId&&((this.meetingPeerInfo.userId===feedback.data.peerId&&this.hostId===feedback.data.requesterId)||(this.meetingPeerInfo.userId===feedback.data.requesterId&&this.hostId===feedback.data.peerId))){
              _that.startOrEndMeeting(_that.meetingPeerInfo,'start')
            }else{
              _that.startOrEndMeeting(_that.meetingPeerInfo,'end')
              FUN.notify('呼叫建立失败,已有当前会话不对',FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            }
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_CONNECTED){
            console.log("EVENT_CALL_CONNECTED  呼叫已建立连接")
            _that.callInDialog= false
            _that.callOutDialog= false
            if(this.callSessionId===feedback.data.sessionId&&((this.meetingPeerInfo.userId===feedback.data.peerId&&this.hostId===feedback.data.requesterId)||(this.meetingPeerInfo.userId===feedback.data.requesterId&&this.hostId===feedback.data.peerId))){
              _that.startOrEndMeeting(_that.meetingPeerInfo,'start')
            }else{
              _that.startOrEndMeeting(_that.meetingPeerInfo,'end')
              FUN.notify('呼叫建立失败,已有当前会话不对',FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            }
          } else if (feedback.code===EVENT_CODE.EVENT_CALL_HANGUP){
            console.log("EVENT_CALL_HANGUP  呼叫挂断")
            FUN.notify(feedback.message,FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
            _that.startOrEndMeeting(null,'end')
          }
        }
      })
    },

  }
}
</script>

<style scoped>
/*.col,.col-4{*/
/*  height: 400px;*/
/*  width: 400px*/
/*}*/
.chat-root-container{
  min-width: 100%;
  max-width: max-content;
  max-height: max-content;
}
.contacts-main-container {
  width: 30em;
  min-width: 30em;
  height: 100%;
}
.chat-window-main-container {
  width: 50em;
  min-width: 50em;
  height: 100%;
}
</style>
