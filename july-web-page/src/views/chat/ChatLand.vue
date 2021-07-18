<template>
  <div class=" chat-root-container">
    <div class="row full-width no-wrap q-gutter-x-md">
        <contacts class="contacts-main-container" ref="contacts" v-if="contactsWindowShow"  :host-info="hostInfo" :stomp-client="julyWebsocket.constant.stompClient" :connection-flag="this.julyWebsocket.variable.connectionFlag" :peer-id="chatPeerId" :peer-type="chatPeerType" v-on:changePeer="changeChatPeer"/>
        <div class="column">
          <q-slide-transition >
            <meeting ref="meeting" class="q-mb-md" v-if="meetingWindowShow" :in-signaling="inSignaling" :host-info="hostInfo" :session-id="callSessionId" :stomp-client="julyWebsocket.constant.stompClient" :peer-info="meetingPeerInfo" :connection-flag="this.julyWebsocket.variable.connectionFlag"></meeting>
          </q-slide-transition>
          <div class="col">
            <chat-window class="chat-window-main-container" v-if="chatWindowShow" :host-info="hostInfo" :stomp-client="julyWebsocket.constant.stompClient" :peer-id="chatPeerId" :peer-type="chatPeerType" :connection-flag="this.julyWebsocket.variable.connectionFlag" :message-window-height="messageWindowHeight" v-on:whenNewMsg="whenNewMsg" v-on:startCallOut="startCallOut"/>
          </div>
        </div>


    </div>
    <q-dialog v-model="callInDialog" persistent transition-show="scale" transition-hide="scale">
      <q-card  style="width: auto;min-width: 400px">
        <q-card-section class=" bg-teal text-white" style="height: 50px">
          <div class="text-h6">会话请求</div>
        </q-card-section>

        <q-card-section class="full-width justify-center no-wrap q-gutter-lg">
          <q-chip class="inline" size="20px" color="warning">
            <q-avatar>
              <img :src="callInTempPeerInfo!=null?callInTempPeerInfo.avatar:''">
            </q-avatar>
            {{callInTempPeerInfo!=null?(callInTempPeerInfo.remarkName!=null?callInTempPeerInfo.remarkName:callInTempPeerInfo.userName):''}}{{callInTempPeerInfo!=null?(callInTempPeerInfo.remarkName!=null?'('+callInTempPeerInfo.userName+')':''):''}}
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
          <q-btn class="btn-small" flat round color="green" icon="videocam" @click="acceptCallIn(callInTempPeerInfo,callInTempSessionId)"  v-close-popup/>
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
import {initJulyWS, closeConnectionJuly} from "@/utils/socket";
import {JULY,FUN} from "@/utils/julyCommon";
import meeting from "@/views/meeting/meeting";
import {CONSTANT, EVENT_CODE} from "@/utils/constant";
import {isNotEmpty} from "@/utils/validate";
import {getToken} from "@/utils/auth";
// import meeting from "@/views/meeting/meeting";
// import {date} from "quasar";
export default {
  name: "ChatLand",
  components: {
    Contacts,
    ChatWindow,
    meeting
  },
  // props: {
  //   julyWebsocket: {
  //     type: Object,
  //     required: false,
  //     default: ()=>({
  //       constant: {
  //         stompClient: null
  //       },
  //       variable: {
  //         connectionFlag: {'websocket':{'name':'websocket',"status":false}}
  //         },
  //     })
  //   },
  // },
  data () {
    return {
      hostId: null,
      hostInfo: this.$store.getters.hostInfo,
      isSocketReady: false,
      isContactsWindowReady: true,
      isChatWindowReady: false,
      isMeetingWindowReady: false,
      callInDialog:false,
      callInTempSessionId: null,
      callInTempPeerInfo: {},
      callOutDialog:false,
      callSessionId: null, // meetingWindow根据初始时有无sessionId来判断是callIn还是callOut，如果没有sessionId,说明是callOut
      // hostStatusObject:{
      //   userId: null,
      //   status: CONSTANT.USER_ACTIVE_STATUS_VISIBLE,
      // },
      chatPeerId: null,
      chatPeerType: null,
      meetingPeerInfo: null,
      inSignaling: false,
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
    contactsWindowShow(){
      return this.isContactsWindowReady&&this.isSocketReady&&this.hostInfo!=null
    },
    chatWindowShow(){
      return this.isChatWindowReady&&this.isSocketReady&&this.chatPeerId!=null&&this.chatPeerType!=null&&this.hostInfo!=null
    },
    meetingWindowShow(){
      return this.isMeetingWindowReady&&this.chatWindowShow
    },
    messageWindowHeight(){
      if(this.meetingWindowShow){
        return '275px'
      }else{
        return '575px'
      }
    }
  },
  watch: {
    julyWebsocket: {
      immediate: false,
      handler(newVal,oldVal) {
        oldVal
        console.log("julyWebsocket changed")
        console.log(newVal)
        console.log(oldVal)
      },
      deep: true
    },
  },
  async created() {
    console.log("chatLand created")
    this.$emit('changeHostStatus',CONSTANT.USER_ACTIVE_STATUS_VISIBLE)
  },
  async mounted () {
    console.log("chatLand mounted")
    // this.hostInfo = await FUN.getFormatHostInfo()
    if(this.hostInfo){
      this.hostId = this.hostInfo.userId
      // this.hostStatusObject.userId = this.hostInfo.userId
      console.log("ChatLand mounted")
      console.log(this.hostInfo)
      console.log(this.julyWebsocket)
      await this.initWSEnv()
    }
  },
  // keep-alive会缓存组件状态，activated()和 deactivated() 这两个钩子函数。activated()是keep-alive 组件激活时调用，而 deactivated() 是 keep-alive 组件停用时调用。activated ()替换mounted()
  activated () {
    console.log("chatLand activated")
  },
  destroyed() {
    console.log("chatLand destroyed")
    this.$emit('changeHostStatus',CONSTANT.USER_ACTIVE_STATUS_ONLY_MESSAGE)
    closeConnectionJuly(this.julyWebsocket.constant.stompClient)

  },
  methods: {
    async initWSEnv() {
      if (this.julyWebsocket.constant.stompClient == null) {
        console.log("start init chatLand websocket")
        this.julyWebsocket.constant.stompClient = await initJulyWS(JULY.WEBSOCKET_URI_ENDPOINT+getToken(), JULY.WEBSOCKET_URI_SEND_HEARTBEAT,JULY.WEBSOCKET_URI_SEND_HEARTBEAT_INTERVAL)
      }
      this.initWSConnectionHeartBeatWatcher()
    },
    initWSConnectionHeartBeatWatcher(){
      let _that = this
      this.julyWebsocket.constant.stompClient.connect(
          JULY.WEBSOCKET_HEADERS,
          function connectCallback () {
            _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
            _that.isSocketReady = true
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
      this.isChatWindowReady = true
    },
    hangupCall(){
      if( this.meetingWindowShow){
        // 如果meetingWindow是显示的，直接关闭window，meeting的destroy()会自动发送关闭session的请求
        this.isMeetingWindowReady = false
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
      this.meetingPeerInfo = {}
      this.callInTempPeerInfo = {}
      this.callInTempSessionId=null
      this.callSessionId=null
      this.inSignaling = false
    },
    acceptCallIn(meetingPeerInfo,sessionId){
      console.log('acceptCallIn')
      console.log(meetingPeerInfo)
      console.log(sessionId)
      // 接受视频会议请求的话就先打开chat聊天框
      if(sessionId!=null){
        this.changeChatPeer(meetingPeerInfo.userId,meetingPeerInfo.type)
        this.meetingPeerInfo = meetingPeerInfo
        this.callSessionId = sessionId
        this.isMeetingWindowReady = true
      }else {
        FUN.notify("没有找到会话,无法接受请求", FUN.NOTIFY_LEVEL_ERROR, FUN.NOTIFY_POSITION_TOP)
      }
    },
    startCallOut(meetingPeerInfo){
      console.log('startCallOut')
      console.log(meetingPeerInfo)
      this.changeChatPeer(meetingPeerInfo.userId,meetingPeerInfo.type)
      this.meetingPeerInfo = meetingPeerInfo

      this.isMeetingWindowReady = true
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
            _that.callOutDialog= true
            _that.callSessionId = feedback.data.sessionId
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_IN){
            console.log("EVENT_CALL_IN  有新呼叫进入")
            _that.callInDialog= true
            _that.callInTempSessionId = feedback.data.sessionId
            // 获取到PeerInfo
            _that.callInTempPeerInfo = {}
            _that.callInTempPeerInfo.userId = feedback.data.data.peerId
            _that.callInTempPeerInfo.type = feedback.data.data.peerType
            _that.callInTempPeerInfo.remarkName = feedback.data.data.remarkName
            _that.callInTempPeerInfo.tag = feedback.data.data.tag
            _that.callInTempPeerInfo.userName = feedback.data.data.peerUserName
            _that.callInTempPeerInfo.avatar = feedback.data.data.peerAvatar
            if(isNotEmpty(feedback.data.data.gmtLastContact)){
              _that.callInTempPeerInfo.gmtLastContact = feedback.data.data.gmtLastContact
            }else{
              _that.callInTempPeerInfo.gmtLastContact = null
            }
            if(isNotEmpty(feedback.data.data.peerGmtLastLogin)){
              _that.callInTempPeerInfo.peerGmtLastLogin = feedback.data.data.peerGmtLastLogin
            }else{
              _that.callInTempPeerInfo.peerGmtLastLogin = null
            }
            _that.callInTempPeerInfo.nickName = feedback.data.data.peerNickName
            _that.callInTempPeerInfo.gender = FUN.convertPrintGender(feedback.data.data.peerGender)
            _that.callInTempPeerInfo.role = FUN.filterPrintRole(feedback.data.data.peerRole)
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_PRE_CONNECT){
            console.log("EVENT_CALL_PRE_CONNECT  呼叫预建立连接")
            if(this.callSessionId===feedback.data.sessionId&&((this.meetingPeerInfo.userId===feedback.data.peerId&&this.hostId===feedback.data.requesterId)||(this.meetingPeerInfo.userId===feedback.data.requesterId&&this.hostId===feedback.data.peerId))){
              if(!this.meetingWindowShow){
                FUN.notify('会话建立失败,会议窗口未打开',FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
                _that.hangupCall()
              }
              if(feedback.data.status === CONSTANT.MEETING_SESSION_STATUS_IN_SIGNALING){
                if(_that.meetingPeerInfo.userId===feedback.data.peerId&&this.hostId===feedback.data.requesterId){
                  // 说明当前为发起人requester，requester在IN SIGNALING阶段只发offer，并接受answer
                  _that.callOutDialog= false
                  if(!isNotEmpty(feedback.data.signalingMap)){
                    // 如果signalingMap为空，说明现在只是signaling第一步，需要requester(host)发出offer
                    _that.inSignaling = true
                  }else{
                    // 如果signalingMap不为空，说明现在只是signaling第三步，需要requester(host)接受peer发出的answer
                    if(feedback.data.signalingMap.peerIcecandidate){
                      _that.$refs.meeting.handleIcecandidate(feedback.data.signalingMap.peerIcecandidate)
                    }
                    if(feedback.data.signalingMap.answer){
                      _that.$refs.meeting.handleAnswer(feedback.data.signalingMap.answer)
                    }
                  }
                }else{
                  // 说明当前为接受者peer，peer在IN SIGNALING阶段接受requester发出的offer,处理并发出answer
                  this.inSignaling = true
                  _that.callInDialog= false
                  if(!isNotEmpty(feedback.data.signalingMap)){
                    // 如果signalingMap为空，说明现在只是signaling第一步，需要requester(host)发出offer
                    FUN.notify('己方为接受方,但接受的offer为空',FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
                  }else{
                    // 如果signalingMap不为空，说明现在只是signaling第三步，需要requester(host)接受peer发出的answer
                    if(feedback.data.signalingMap.requesterIcecandidate){
                      _that.$refs.meeting.handleIcecandidate(feedback.data.signalingMap.requesterIcecandidate)
                    }
                    if(feedback.data.signalingMap.offer){
                      _that.$refs.meeting.handleOffer(feedback.data.signalingMap.offer)
                    }
                  }
                }
              }
            }else{
              // _that.startOrEndMeeting(_that.meetingPeerInfo,'end')
              FUN.notify('会话建立失败,已有当前会话Id不正确',FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            }
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_CONNECTED){
            console.log("EVENT_CALL_CONNECTED  会话已建立连接")
            _that.callInDialog= false
            _that.callOutDialog= false
            if(_that.callSessionId===feedback.data.sessionId&&((_that.meetingPeerInfo.userId===feedback.data.peerId&&_that.hostId===feedback.data.requesterId)||(_that.meetingPeerInfo.userId===feedback.data.requesterId&&_that.hostId===feedback.data.peerId))){
              if(!_that.meetingWindowShow){
                FUN.notify('会话建立失败,会议窗口未打开',FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
                _that.hangupCall()
              }else{
                FUN.notify('会话建立成功',FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
              }
            }else{
              FUN.notify('会话建立失败,已有当前会话Id不正确',FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            }
          } else if (feedback.code===EVENT_CODE.EVENT_CALL_HANGUP){
            console.log("EVENT_CALL_HANGUP  会话挂断")
            FUN.notify(feedback.message,FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
            _that.hangupCall()
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_FAIL){
            console.log("EVENT_CALL_FAIL  呼叫失败")
            FUN.notify(feedback.message,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            _that.hangupCall()
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_BUSY){
            console.log("EVENT_CALL_BUSY  呼叫对方忙")
            FUN.notify(feedback.message,FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
            _that.hangupCall()
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_OFFLINE){
            console.log("EVENT_CALL_OFFLINE  呼叫对方不在线")
            FUN.notify(feedback.message,FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
            _that.hangupCall()
          }else if (feedback.code===EVENT_CODE.EVENT_CALL_DENY){
            console.log("EVENT_CALL_DENY  呼叫对方拒绝")
            FUN.notify(feedback.message,FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
            _that.hangupCall()
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
  margin-top: 100px !important;
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
