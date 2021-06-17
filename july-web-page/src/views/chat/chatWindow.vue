<template>
  <div class="chat-window q-pa-md justify-center">
    <q-card class="my-card" flat bordered>

      <q-card-section horizontal style="height: 12%">
        <q-img class="col" native-context-menu src="https://cdn.quasar.dev/img/mountains.jpg" >

          <div class="fit">
            <q-item>
              <q-item-section avatar>
                <q-avatar>
                  <img :src="julyChat.variable.peerInfo.avatar">
                </q-avatar>
              </q-item-section>
              <q-item-section>
                <q-item-label class="text-h6 text-white">{{julyChat.variable.peerInfo.remarkName!=null?julyChat.variable.peerInfo.remarkName:julyChat.variable.peerInfo.userName}}{{julyChat.variable.peerInfo.remarkName!=null?'('+julyChat.variable.peerInfo.userName+')':''}}</q-item-label>
                <q-item-label class="text-caption text-white">{{julyChat.variable.peerInfo.role}}</q-item-label>
              </q-item-section>
            </q-item>
            <q-card-actions class="absolute-right justify-around">
              <q-btn class="btn-small" flat round color="green" icon="videocam" />
              <q-btn class="btn-small" flat round color="indigo" icon="mic" />
              <q-btn class="btn-small" flat round color="indigo" icon="star_outline" />
              <q-btn class="btn-small" v-show="julyChat.variable.peerType==='room'" @click="leaveChat" flat round color="negative" icon="fas fa-sign-out-alt" />
            </q-card-actions>
            <q-icon v-if="julyWebsocket.variable.connectionFlag['websocket']['status']===true&&connectionFlag==null" class="absolute-top-right q-mr-md q-mt-xs" size="sm" color="green" name="link" >
              <q-tooltip>
                <strong >当前前WebSocket已连接</strong>
              </q-tooltip>
            </q-icon>
            <q-icon v-else-if="julyWebsocket.variable.connectionFlag['websocket']['status']===false&&connectionFlag==null" class="absolute-top-right q-mr-md q-mt-xs" size="sm" color="negative" name="link_off" >
              <q-tooltip>
                <strong >当前前WebSocket未连接</strong>
              </q-tooltip>
            </q-icon>
          </div>
        </q-img>

<!--        <q-card-actions vertical class="justify-around">-->
<!--          <q-btn flat round color="green" icon="videocam" />-->
<!--          <q-btn flat round color="indigo" icon="mic" />-->
<!--        </q-card-actions>-->
      </q-card-section>

      <q-card-section style="height: 73%">
        <div id="chat-scroll-container" class="scroll inset-shadow-down shadow-10 shadow-box q-pb-md " style="overflow: auto;min-height:100%;max-height:100%">
          <q-infinite-scroll scroll-target="#chat-scroll-container"  :offset="250" :thumb-style="thumbStyle" :bar-style="barStyle" @load="onLoad" reverse ref="scrollArea">
<!--            <template slot="loading">-->
<!--              <div class="row justify-center q-my-md">-->
<!--                <q-spinner color="primary" name="dots" size="40px" />-->
<!--              </div>-->
<!--            </template>-->
            <q-scroll-observer @scroll="onChatViewScroll"  />
            <div v-for="(msgItem, index) in updateMsgItems.data"  :key="'updateId'+index" class="chat-main-message-area q-py-sm">
              <div>
                <q-chat-message
                    v-if="msgItem.sender === hostId"
                    :name="julyChat.variable.participantInfo.data.hasOwnProperty(msgItem.sender)?julyChat.variable.participantInfo.data[msgItem.sender].userName:'Unknown'"
                    :stamp="msgItem.stamp"
                    :text="msgItem.message"
                    sent
                    :size="msgItem.message.toString().length<5?'2':(msgItem.message.toString().length<20?'3':'4')"
                    text-color="white"
                    bg-color="primary"
                >
<!--                    <q-spinner-radio-->
<!--                        color="primary"-->
<!--                        size="2em"-->
<!--                    />-->


                  <template v-slot:avatar>
                    <img
                        class="q-message-avatar q-message-avatar--sent"
                        :src="julyChat.variable.participantInfo.data.hasOwnProperty(msgItem.sender)?julyChat.variable.participantInfo.data[msgItem.sender].avatar:'https://cdn.quasar.dev/img/avatar5.jpg'"
                    >
                  </template>
<!--                  <q-spinner-dots size="2rem" />-->
                </q-chat-message>

                <q-chat-message
                    v-if="msgItem.sender !== hostId"
                    :name="julyChat.variable.participantInfo.data.hasOwnProperty(msgItem.sender)?julyChat.variable.participantInfo.data[msgItem.sender].userName:'Unknown'"
                    :text="msgItem.message"
                    :stamp="msgItem.stamp"
                    :size="msgItem.message.toString().length<5?'2':(msgItem.message.toString().length<20?'3':'4')"
                    bg-color="amber"
                >
                  <template v-slot:avatar>
                    <img
                        class="q-message-avatar q-message-avatar--received"
                        :src="julyChat.variable.participantInfo.data.hasOwnProperty(msgItem.sender)?julyChat.variable.participantInfo.data[msgItem.sender].avatar:'https://cdn.quasar.dev/img/avatar5.jpg'"
                    >
                  </template>

                  <!--                  <q-spinner-dots size="2rem" />-->
                </q-chat-message>
              </div>
            </div>
            <!-- 使用`v-scroll`指令的例子 -->
            <div v-intersection="onObservedElement"></div>
          </q-infinite-scroll>
        </div>
      </q-card-section>
      <q-card-actions  class="column" style="height: 15%">
        <div class="col full-width">
        <q-input  outlined clearable label="输入信息" :dense="dense" :loading="julyChat.variable.inputMessageLoading" @keyup.enter.native="sendMessage"
                  v-model="julyChat.variable.inputMessage" counter maxlength="255" type="textarea" style="width: 100%"
                  autogrow
        />
        </div>
        <div class="col  full-width">
          <div class="q-pa-sm q-gutter-x-sm justify-between float-left">
            <q-btn round color="amber" icon="emoji_emotions" />
            <q-btn round color="info" icon="folder" />
          </div>
          <div class="q-pa-sm q-gutter-x-sm justify-between float-right">
            <q-btn color="positive" icon="send" :loading="julyChat.variable.inputMessageLoading"  @click.native="sendMessage"  />
          </div>
        </div>
      </q-card-actions>
    </q-card>
  </div>

</template>

<script>
import { date,scroll } from 'quasar'
const { getScrollTarget, setScrollPosition, getScrollHeight } = scroll
import {isNotEmpty} from "@/utils/validate";
import {initJulyWS, closeConnectionJuly} from "@/utils/socket";
import {CONSTANT,RESULT_CODE,EVENT_CODE} from "@/utils/constant";
import {JULY,FUN} from "@/utils/julyCommon";
export default {
  name: "chatWindow",
  props: {
    stompClient: {
      type: Object,
      required: false
    },
    connectionFlag: {
      type: Object,
      required: false
    },
    peerId: {
      type: String,
      required: true
    },
    peerType: {
      peerType: String,
      required: true
    },
    hostInfo: {
      peerType: Object,
      required: false
    }
  },
  data () {
    return {
      dense: true,
      hostId: this.hostInfo!=null?this.hostInfo.userId:null,
      // 注意：以下Items默认按时间，越近的下标越大
      // testMsgItems: [],
      testMsgItems: [],
      // expired 代表无限等待锁的释放
      // candidateMsgItems: {'lockObject':{'name':'candidateMsgItemsLock','lock':false,'expired':-1},'data':[]},
      candidateMsgItems: {'lockObject':{'name':'candidateMsgItemsLock','lock':false,'expired':-1},'data':[]},
      // lockObject 自旋锁，false=没有在更新(改变数组)，true=有在更新
      // updateMsgItems: {'lockObject':{'name':'updateMsgItemsLock','lock':false,'expired':1},'data':[]}
      updateMsgItems: {'lockObject':{'name':'updateMsgItemsLock','lock':false,'expired':1},'data':[]},
      julyWebsocket:{
        constant: {
          stompClient: this.stompClient,
        },
        variable: {
          connectionFlag: this.connectionFlag!=null?this.connectionFlag:{'websocket':{'name':'websocket',"status":false}}
        },
      },
      julyChat: {
        constant: {
          combineMsgGap:10,
          updateViewInterval:2000, //ms , 每隔一段时间更新时间戳显示
          getRemoteMessagesInterval:5000
        },
        variable: {
          peerType: null, // personal or room
          peerId: null, // 如果聊天是私人聊天，roomId就是对方userId，如果是公共的，就是roomId
          lastUpdateTime: '',
          chatViewScrollEleTarget: '',
          isChatViewObservedBottom: true, // true: 代表聊天界面的scroll bar拉倒最底，false则不是最底
          inputMessage: '',
          inputMessageLoading: false,
          // participantInfo: {'updateDate':null,'data':new Map()},
          participantInfo: {'updateDate':null,'data':new Map()},
          peerInfo: null,
          historyMessageDayCount:2
        }
      },
      thumbStyle: {
        right: '4px',
        borderRadius: '5px',
        backgroundColor: '#027be3',
        width: '5px',
        opacity: 0.75
      },
      barStyle: {
        right: '2px',
        borderRadius: '9px',
        backgroundColor: '#027be3',
        width: '9px',
        opacity: 0.2
      }
    }
  },
  computed: {
     receiverURI() {
       if (this.julyChat.variable.peerType===CONSTANT.CONTAINER_GROUP){
         return '/user/topic/room/'+this.julyChat.variable.peerId
       }else if (this.julyChat.variable.peerType===CONSTANT.CONTAINER_PERSON){
         return '/user/topic/personal/'+this.hostId
       }else{
         return null
       }
    },
    sendMsgURI() {
      if (this.julyChat.variable.peerType===CONSTANT.CONTAINER_GROUP){
        return '/app/room/'+this.julyChat.variable.peerId+'/sendmsg'
      }else if (this.julyChat.variable.peerType===CONSTANT.CONTAINER_PERSON){
        return '/app/personal/'+this.julyChat.variable.peerId+'/sendmsg'
      }else{
        return null
      }
    },
    subscribeHistoryMessageURI() {
      return '/app/historyMessage/'+this.julyChat.variable.peerId+"/"+this.julyChat.variable.historyMessageDayCount
    },
    sendChatSessionWatchURI() {
      if (this.julyChat.variable.peerType===CONSTANT.CONTAINER_GROUP){
        return '/app/watch/'+this.julyChat.variable.peerId
      }else if (this.julyChat.variable.peerType===CONSTANT.CONTAINER_PERSON){
        return '/app/watch/'+this.julyChat.variable.peerId
      }else{
        return null
      }
    },
    ackURI() {
      if (this.julyChat.variable.peerType===CONSTANT.CONTAINER_GROUP){
        return '/app/room/'+this.julyChat.variable.peerId+'/ack'
      }else if (this.julyChat.variable.peerType===CONSTANT.CONTAINER_PERSON){
        return '/app/personal/'+this.hostId+'/ack'
      }else{
        return null
      }
    },
    joinRoomURI() {
      if (this.julyChat.variable.peerType===CONSTANT.CONTAINER_GROUP){
        return '/app/room/'+this.julyChat.variable.peerId+'/join'
      }else{
        return null
      }
    },
    leaveRoomURI() {
      if (this.julyChat.variable.peerType===CONSTANT.CONTAINER_GROUP){
        return '/app/room/'+this.julyChat.variable.peerId+'/leave'
      }else{
        return null
      }
    },
    chatViewScrollEleTarget(){
       return getScrollTarget(document.getElementById('chat-scroll-container'))
    }
  },
  watch: {
    // Whenever the movie prop changes, fetch new data
    peerId: {
      // Will fire as soon as the component is created
      immediate: true,
      handler(peerId){
        this.resetData(peerId,this.peerType)
        // Fetch data about the movie
      }
      // deep: true
    },
    // Whenever the movie prop changes, fetch new data
    candidateMsgItems: {
      // Will fire as soon as the component is created
      // immediate: true,
      handler(){
        let entranceTS = new Date(Date.now())
        while (this.candidateMsgItems.lockObject.lock){
          let probeTS = new Date(Date.now())
          let waitingDiff = Math.abs(date.getDateDiff(probeTS,entranceTS,'seconds'))
          if(this.candidateMsgItems.lockObject.expired>0&&waitingDiff>=this.candidateMsgItems.lockObject.expired){
            console.log('waiting for release lock too long time, out deadline')
            return false
          }
        }
        let messageArray = JSON.parse(JSON.stringify(this.candidateMsgItems.data)) //深度复制
        this.decorateMessageArray(messageArray)
      },
      deep: true
    },
  },
  mounted () {
    console.log("chatWindow")
    this.initChatWSConnection(this.julyChat.variable.peerType,this.julyWebsocket.constant.stompClient)
    this.initView()
  },
  // keep-alive会缓存组件状态，activated()和 deactivated() 这两个钩子函数。activated()是keep-alive 组件激活时调用，而 deactivated() 是 keep-alive 组件停用时调用。activated ()替换mounted()
  activated () {
  },
  destroyed() {
    this.leaveChat()
    this.destroyChat()
    closeConnectionJuly(this.julyWebsocket.constant.stompClient)
  },
  methods: {
    initWSEnv(){
      if (this.julyWebsocket.constant.stompClient==null){
        console.log("chatWindow websocket not init , so start init by self")
        this.julyWebsocket.constant.stompClient = initJulyWS(JULY.WEBSOCKET_URI_ENDPOINT,JULY.WEBSOCKET_URI_SEND_HEARTBEAT,JULY.WEBSOCKET_URI_SEND_HEARTBEAT_INTERVAL)
      }
    },
    initWSWatcher(){
      console.log("chatWindow websocket watcher not init , so start init by self")
      let _that = this
      this.julyWebsocket.constant.stompClient.connect(
          JULY.WEBSOCKET_HEADERS,
          function connectCallback () {
            // 订阅stompClient心跳检测
            _that.julyWebsocket.constant.stompClient.subscribe(JULY.WEBSOCKET_URI_SUBSCRIBE_HEARTBEAT, () => {
              _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
            })
          },
          function errorCallBack (error) {
            _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = false
            FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
          }
      )
    },
    initChatWSConnection(type,stompClient){
      if (type===CONSTANT.CONTAINER_GROUP){
        this.initRoomChat(stompClient,this.julyWebsocket.variable.connectionFlag,this.joinRoomURI,this.receiverURI)
      }else if (type===CONSTANT.CONTAINER_PERSON){
        this.initPersonalChat(stompClient,JULY.WEBSOCKET_HEADERS,this.julyWebsocket.variable.connectionFlag,this.receiverURI)
      }
      // 发送冒泡消息告诉后端我在，并且还在接收当前peerId的消息
      this.initChatSessionWatcher(stompClient)
      this.sendChatSessionWatchMsg(stompClient)
    },
    initChatSessionWatcher(stompClient){
      let _that = this
      setInterval(() => {
        try {
          _that.sendChatSessionWatchMsg(stompClient)
        } catch (e) {
          console.log('WebSocket connection interrupt: ' + e)
          FUN.notify('会话冒泡连接失败',FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
        }
      }, JULY.CHAT_WATCH_SEND_INTERVAL)
    },
    sendChatSessionWatchMsg(stompClient){
      stompClient.send(this.sendChatSessionWatchURI, {},JSON.stringify({watcherId:this.hostId,peerId:this.julyChat.variable.peerId,peerType:this.julyChat.variable.peerType,action:CONSTANT.CHAT_WATCH_ACTION_ENTRY}))
    },
    async initPersonalChat(stompClient,headers,connectionFlag,receiverURI){
      console.log("initPersonalChat")
      console.log(receiverURI)
      console.log(stompClient)
      // 初始化1v1聊天
      let _that = this
      // 订阅接收消息(message/event)
      stompClient.subscribe(receiverURI, (response) => {
        let feedback = JSON.parse(response.body)
        console.log(feedback)
        if (feedback.type=== CONSTANT.SHOUTING_MESSAGE){
          connectionFlag['websocket']['status'] = true
          let feedbackTS = date.formatDate(date.extractDate(feedback.gmtCreate,CONSTANT.DATE_FORMAT) , 'x')
          let newMsg = {'sender':feedback.from,'startTS': feedbackTS,'endTS': feedbackTS,'stamp':feedback.gmtCreate,'message':[feedback.message]}
          _that.insertMessageArray([newMsg])
        }
      })
    },
    async initRoomChat(stompClient,connectionFlag,joinRoomURI,receiverURI){
      console.log("initRoomChat")
      console.log(stompClient)
      console.log(joinRoomURI)
      // 初始化1vN聊天室
      this.julyChat.variable.inputMessageLoading = true
      let _that = this
      stompClient.subscribe(joinRoomURI, (response) => { // 后端提供订阅地址
        connectionFlag['websocket']['status'] = true
        let feedback = JSON.parse(response.body)
        if (feedback.code===RESULT_CODE.SUCCESS){
          console.log("已进入房间")
          FUN.notify('已进入房间',FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
          let roomInfo = feedback.data
          let nowTS = date.formatDate(new Date(Date.now()) , 'x')
          // 新进入房间要更新房间内用户的信息
          _that.updateRoomParticipant(roomInfo.memberIdSet,nowTS)
        }else{
          console.log("进入房间失败 error:"+feedback.msg)
          FUN.notify("进入房间失败 error:"+feedback.msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
        }
        _that.julyChat.variable.inputMessageLoading = false
      })
      stompClient.subscribe(receiverURI, (response) => { // 后端提供订阅地址
        connectionFlag['websocket']['status'] = true
        let feedback = JSON.parse(response.body)
        console.log(feedback)
        if (feedback.type=== CONSTANT.SHOUTING_MESSAGE){
          // type=== 'message' 说明是来了新聊天消息
          let feedbackTS = date.formatDate(date.extractDate(feedback.gmtCreate,CONSTANT.DATE_FORMAT) , 'x')
          let newMsg = {'sender':feedback.from,'startTS': feedbackTS,'endTS': feedbackTS,'stamp':feedback.gmtCreate,'message':[feedback.message]}
          _that.insertMessageArray([newMsg])
        }else if (feedback.type=== CONSTANT.SHOUTING_EVENT){
          // type=== 'message' 说明是来了新事件
          let feedbackTS = date.formatDate(date.extractDate(feedback.gmtCreate,CONSTANT.DATE_FORMAT) , 'x')
          if (feedback.code===EVENT_CODE.NEW_JOINER){ // E1001 有新人进入事件
            _that.updateRoomParticipant(feedback.data.memberIdSet,feedbackTS)
            FUN.notify('有新用户加入房间',FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
          }
        }
        return response
      })
    },
    initView(){
      this.scrollToBottom() // 开局首先scrollbar保持底部
      // 先立即执行一次更新View(更新历史消息状态)
      this.updateView()
      setInterval(this.updateView, this.julyChat.constant.updateViewInterval)
      // setInterval(this.getRemoteMessages, this.julyChat.constant.getRemoteMessagesInterval)
    },
    async resetData(peerId,peerType){
      console.log("chat window resetData")
      console.log(peerId)
      console.log(peerType)
      this.julyChat.variable.lastUpdateTime = null
      this.julyChat.variable.inputMessage=''
      this.julyChat.variable.participantInfo = {'updateDate':null,'data':new Map()}
      this.testMsgItems=[]
      this.candidateMsgItems =  {'lockObject':{'name':'candidateMsgItemsLock','lock':false,'expired':-1},'data':[]}
      this.updateMsgItems =   {'lockObject':{'name':'updateMsgItemsLock','lock':false,'expired':1},'data':[]}
      this.julyChat.variable.peerInfo={
        userId:null,
        type:null,
        remarkName:"请选择用户",
        tag:null,
        userName:'',
        avatar:"",
        gmtLastContact:null,
        peerGmtLastLogin:null,
        nickName:"请选择用户",
        gender:null,
        role:''
      }
      if(isNotEmpty(peerId)&&isNotEmpty(peerType)){
        this.julyChat.variable.peerType = peerType
        this.julyChat.variable.peerId = peerId
        await this.updateHostInfo()

        let peerInfo = await this.updatePeerInfo(peerId)
        console.log("got updatePeerInfo")
        console.log(peerInfo)
        if(peerInfo !=null){
          this.julyChat.variable.peerInfo={
            userId:peerInfo.userId,
            type:peerInfo.type,
            remarkName:peerInfo.remarkName,
            tag:peerInfo.tag,
            userName:peerInfo.userName,
            avatar:peerInfo.avatar,
            gmtLastContact:peerInfo.gmtLastContact,
            peerGmtLastLogin:peerInfo.peerGmtLastLogin,
            nickName:peerInfo.nickName,
            gender:peerInfo.gender,
            role:peerInfo.role
          }
        }
        this.getHistoryMessages(peerId,peerType)
      }
    },
    updateRoomParticipant(participantIds,updateDate){
      // 更新当前聊天中参与者信息，participantIds是后端传来的Room现有参与者userId列表，updateDate是后端传来的更新时间，
      console.log('updateParticipant participantIds:'+participantIds)
      console.log('updateParticipant updateDate:'+updateDate)
      // 确保updateDate是更新的才进行更新
      if(isNotEmpty(updateDate)&&(!isNotEmpty(this.julyChat.variable.participantInfo.updateDate) || this.julyChat.variable.participantInfo.updateDate<updateDate)){
        // 解析获取新加入的用户Id
        let joinParticipantIds = []
        // 是当前已有用户Id(是上一次更新的)
        let completeParticipantIds = Object.keys(this.julyChat.variable.participantInfo.data)
        console.log('updateParticipant completeParticipantIds:'+completeParticipantIds)
        for(let i=0;i<participantIds.length;i++){
          if(completeParticipantIds.indexOf(participantIds[i])<0){
            joinParticipantIds.push(participantIds[i])
          }
        }
        console.log('updateParticipant joinParticipantIds:'+joinParticipantIds)
        // 对于新参与者要获取其用户信息
        for(let i=0;i<joinParticipantIds.length;i++){
          this.updateUserInfo(joinParticipantIds[i])
        }
        // 解析获取离开的用户Id 列表
        let leaveParticipantIds = []
        for(let i=0;i<completeParticipantIds.length;i++){
          if(participantIds.indexOf(completeParticipantIds[i])<0){
            leaveParticipantIds.push(completeParticipantIds[i])
            // 顺便删除已离开的用户信息
            delete this.julyChat.variable.participantInfo.data[completeParticipantIds[i]]
          }
        }
        console.log('updateParticipant leaveParticipantIds:'+leaveParticipantIds)
        // 更新这次的更新时间
        this.julyChat.variable.participantInfo.updateDate = updateDate
      }
    },
    getHistoryMessages(){
      let _that = this
      this.julyWebsocket.constant.stompClient.subscribe(this.subscribeHistoryMessageURI, (response) => { // 后端提供订阅地址
        let feedback = JSON.parse(response.body)
        _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
        if (feedback.code===RESULT_CODE.SUCCESS){
          let messageArray = feedback.data
          console.log("获取到历史消息")
          console.log(feedback)
          let tempMessageArray = []
          for(let index = 0;index<messageArray.length; index++){
            if (messageArray[index].type=== CONSTANT.SHOUTING_EVENT) {
              FUN.notify('事件:(code:'+messageArray[index].code+"|"+"msg:"+messageArray[index].message+")",FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
            }else{
              let feedbackTS = date.formatDate(date.extractDate(messageArray[index].gmtCreate, CONSTANT.DATE_FORMAT), 'x')
              let newMsg = {
                'sender': messageArray[index].from,
                'startTS': feedbackTS,
                'endTS': feedbackTS,
                'stamp': messageArray[index].gmtCreate,
                'message': [messageArray[index].message]
              }
              tempMessageArray.push(newMsg)
            }
          }
          if(tempMessageArray.length>0){
            this.insertMessageArray(tempMessageArray)
            this.scrollToBottom()
            FUN.notify('获取到历史消息'+tempMessageArray.length+"条",FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
          }

          // if (feedback.type=== CONSTANT.SHOUTING_MESSAGE){
          //   connectionFlag['websocket']['status'] = true
          //   let feedbackTS = date.formatDate(date.extractDate(feedback.gmtCreate,CONSTANT.DATE_FORMAT) , 'x')
          //   let newMsg = {'sender':feedback.from,'startTS': feedbackTS,'endTS': feedbackTS,'stamp':feedback.gmtCreate,'message':[feedback.message]}
          //   _that.insertMessage(newMsg)
          // }
        }else{
          console.log("进入房间失败 error:"+feedback.msg)
          FUN.notify("获取历史消息失败 error:"+feedback.msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
        }
        _that.julyChat.variable.inputMessageLoading = false
      })
    },
    updateView() {
      // let beforeState = this.julyChat.variable.isChatViewObservedBottom
      this.updateRecentMessageState()
      // if (beforeState){
      //   this.scrollToBottom()
      // }
    },
    // 这里周期循环运行，目的是更新已插入消息的stamp或者状态
    updateRecentMessageState(){
      if (!this.getLock(this.updateMsgItems.lockObject)) {
        console.log('got updateMsgItems lock out deadline, Fail, give up this job')
        return
      }
      try {
        this.julyChat.variable.lastUpdateTime = this.convertStamp(this.updateMsgItems.data)
      } catch (error){
        console.error('updateRecentMessageState error:' + error)
        FUN.notify('周期更新消息时间戳失败',FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
      }  finally    {
        this.releaseLock(this.updateMsgItems.lockObject)
      }
    },
    convertStamp(msgItems){
      // 根据当前时间，更新每一个消息的Stamp
      let lastUpdateTime = new Date(Date.now())  // 记录现在更新时间
      // 获取当前时间前一分钟，前一小时，前一天的时间戳
      let offsetOneDay = date.formatDate(date.subtractFromDate(lastUpdateTime, {days: 1}), 'x')
      let offsetOneHour = date.formatDate(date.subtractFromDate(lastUpdateTime, {hours: 1}), 'x')
      let offsetOneMinute = date.formatDate(date.subtractFromDate(lastUpdateTime, {minutes: 1}), 'x')
      if (msgItems.length>0) {
        for (let index = msgItems.length - 1; index >= 0; index--) {
          if (offsetOneMinute < msgItems[index].endTS) {
            // 说明消息是最近一分钟的,所以计算秒差，更新stamp
            let diff = date.getDateDiff(lastUpdateTime, date.extractDate(msgItems[index].endTS, 'x'), 'seconds')
            if (diff <= 10) {
              msgItems[index].stamp = 'just now    '
            } else {
              msgItems[index].stamp = diff + ' seconds ago'
            }
          } else if (offsetOneHour < msgItems[index].endTS) {
            // 说明消息是最近一小时的,所以计算分钟差，更新stamp
            let diff = date.getDateDiff(lastUpdateTime, date.extractDate(msgItems[index].endTS, 'x'), 'minutes')
            msgItems[index].stamp = diff + ' minutes ago'
          } else if (offsetOneDay < msgItems[index].endTS) {
            // 说明消息是最近一天的(但是大于小时),所以计算小时差，更新stamp
            let diff = date.getDateDiff(lastUpdateTime, date.extractDate(msgItems[index].endTS, 'x'), 'hours')
            msgItems[index].stamp = diff + ' hours ago'
          } else {
            if (date.isValid(msgItems[index].stamp)) {
              // 因为数组排列是按发送时间从近到远排列，筛选到这的endTS都是离当前时间大于24h的，并且stamp是时间格式的，所以后面的元素都不用再转换stamp
              break
            } else {
              msgItems[index].stamp = date.formatDate(date.extractDate(msgItems[index].endTS, 'x'), CONSTANT.DATE_FORMAT)
            }
          }
        }
      }
      return lastUpdateTime
    },
    sendMessage(){
      // 发送消息，这里逻辑是不直接更新当前view，而是消息WS发送到后端服务器后，服务器经过处理会发送到前端(Room会发送给所有参与者，包括发送消息者，PersonalChat是发送给聊天双方)，待WS订阅消息收到消息后更新View
      let msgText = this.julyChat.variable.inputMessage
      if(isNotEmpty(msgText)&&this.julyChat.variable.inputMessageLoading===false){
        this.julyChat.variable.inputMessage = ''
        this.julyChat.variable.inputMessageLoading = true
        //
        // let nowTS = date.formatDate( new Date(Date.now()),'x')
        // let newMsg = {'sender':this.hostId,'startTS': nowTS,'endTS': nowTS,'stamp':date.formatDate(date.extractDate(nowTS, 'x'), 'YYYY-MM-DD HH:mm:ss'),'message':[msgText]}
        // this.insertMessage(newMsg)
        let server_message = {}
        if(this.julyChat.variable.peerType===CONSTANT.CONTAINER_GROUP){
          server_message = {
            'from':this.hostId,
            'to':'all',
            'message':msgText,
            'method': CONSTANT.WS_METHOD_BROADCAST,
            "peerType": CONSTANT.CONTAINER_GROUP,
            "type":CONSTANT.SHOUTING_MESSAGE
          }
        }else{
          server_message = {
            'from':this.hostId,
            'to':this.julyChat.variable.peerType,
            'message':msgText,
            'method':CONSTANT.WS_METHOD_PERSONAL,
            "peerType": CONSTANT.CONTAINER_PERSON,
            "type":CONSTANT.SHOUTING_MESSAGE
          }
        }
        this.julyWebsocket.constant.stompClient.send(this.sendMsgURI, {},JSON.stringify(server_message))
        this.julyChat.variable.inputMessageLoading = false
      }
    },
    insertMessageArray(messageArray){
      // candidateMsgItems 是有没有合并的数据，每一个item就是一条message
      // 首先获取candidateMsgItems的lock，把candidateMsgItems的数据Push到updateMsgItems尾部，并且清空candidateMsgItems，最后释放candidateMsgItems lock
      if (!this.getLock(this.candidateMsgItems.lockObject)) {
        console.log('got candidateMsgItems lock out deadline, Fail, give up this job')
        this.releaseLock(this.candidateMsgItems.lockObject)
        return
      }
      try{
        for (let index = 0; index< messageArray.length;index++) {
          let msgItem = messageArray[index]
          if(typeof (msgItem.message)==='string'){
            msgItem.message = [msgItem.message]
          }
          if (this.candidateMsgItems.data.length>0){
            let insertPosition = this.candidateMsgItems.data.length
            for (let index= this.candidateMsgItems.data.length-1;index>=0;index--){
              // 要根据endTS消息发出时间比较，找到合适的地方插入
              if (msgItem.startTS<=this.candidateMsgItems.data[index].startTS){
                insertPosition = index
              }else{
                break
              }
            }
            console.log('candidateMsgItems position:' + (insertPosition))
            // 看所插入位置前面一位的endTS，来决定是否合并
            if (insertPosition>=0) {
              this.candidateMsgItems.data.splice(insertPosition,0,msgItem)
            } else {
              this.candidateMsgItems.data.splice(0, 0, msgItem)
            }
          }else{
            // 如果是新消息(第一条消息)，直接插入到updateMsgItems即可
            this.candidateMsgItems.data.splice(0,0,msgItem)
          }
        }
      }catch (error){
        console.error('andidateMsgItems error:'+error)
        FUN.notify("更新消息列表失败-1/2",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
      }finally {
        this.releaseLock(this.candidateMsgItems.lockObject)
      }
    },
    decorateMessageArray(messageArray){
      // 在当前聊天界面立即插入消息
      if (!this.getLock(this.updateMsgItems.lockObject)) {
        console.log('got updateMsgItems lock out deadline, Fail, give up this job')
        return
      }
      let beforeState = this.julyChat.variable.isChatViewObservedBottom
      // 对于新来的聊天消息要更新时间戳，不直接显示具体时间，而显示xxx ago
      // this.convertStamp(messageArray)
      try {
        console.log('decorateMessageArray  updateMsgItems.data.length:' + this.updateMsgItems.data.length)
        console.log(messageArray)
        if (isNotEmpty(messageArray)&&messageArray.length>0) {
          let tempMessageArray = []
          for (let index = messageArray.length-1; index>0;index--) {
            if (index>0&&(messageArray[index].sender === messageArray[index-1].sender)) {
              let diffSeconds = Math.abs(date.getDateDiff(date.extractDate(messageArray[index].startTS, 'x'), date.extractDate(messageArray[index-1].endTS, 'x'), 'seconds'))

              if (diffSeconds <= this.julyChat.constant.combineMsgGap) {
                for(let j=0; j<messageArray[index].message.length;j++){
                  messageArray[index-1].message.push(messageArray[index].message[j])
                }
                messageArray[index-1].endTS = messageArray[index].endTS
              } else {
                tempMessageArray.splice(0,0,messageArray[index])
              }
            }else{
              tempMessageArray.splice(0,0,messageArray[index])
            }
          }
          tempMessageArray.splice(0,0,messageArray[0])
          this.convertStamp(tempMessageArray)
          this.updateMsgItems.data = tempMessageArray
        }else{
          this.updateMsgItems.data = []
        }

      }catch (error){
        console.error('insertMessage error:' +error.stack)
        FUN.notify("更新消息列表失败-2/2",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
      }finally {
        this.releaseLock(this.updateMsgItems.lockObject)
        if (beforeState){
          // 插入之前scrollbar在最底，插入消息完成后，scrollbar还要保持最底
          this.scrollToBottom()
        }
      }
    },
    scrollToBottom(){
      // 设置滚动到底部:
      setScrollPosition (this.chatViewScrollEleTarget, getScrollHeight(this.chatViewScrollEleTarget), 500)
    },
    async updatePeerInfo (userId) {
      console.log(userId)
      let data = await this.getUserRelation(this.hostId,userId,null)
      console.log("got getUserRelation")
      console.log(data)
      if(isNotEmpty(data)&&data.length>0){
        let newRelation = {}
        newRelation['userId'] = data[0].peerId
        newRelation['type'] = data[0].peerType
        newRelation['remarkName'] = data[0].remarkName
        newRelation['tag'] = data[0].tag
        newRelation['userName'] = data[0].peerUserName
        newRelation['avatar'] = data[0].peerAvatar
        if(isNotEmpty(data[0].gmtLastContact)){
          newRelation['gmtLastContact'] = data[0].gmtLastContact
        }else{
          newRelation['gmtLastContact'] = null
        }
        if(isNotEmpty(data[0].peerGmtLastLogin)){
          newRelation['peerGmtLastLogin'] = data[0].peerGmtLastLogin
        }else{
          newRelation['peerGmtLastLogin'] = null
        }
        newRelation['nickName'] = data[0].peerNickName
        newRelation['gender'] = FUN.convertPrintGender(data[0].peerGender)
        newRelation['role'] = FUN.filterPrintRole(data[0].peerRole)

        this.julyChat.variable.participantInfo.data[newRelation.userId] = newRelation
        console.log(this.julyChat.variable.participantInfo.data)
        return newRelation
      }else{
        return null
      }
    },
    async updateHostInfo () {
      if(this.hostInfo==null){
        this.hostInfo = await this.getUserInfo(this.hostId)
      }
      if(isNotEmpty(this.hostInfo)){
        let newRelation = {}
        newRelation['userId'] = this.hostInfo.userId
        newRelation['type'] = 'host'
        newRelation['remarkName'] = 'Me'
        newRelation['tag'] = null
        newRelation['userName'] = this.hostInfo.userName
        newRelation['avatar'] = this.hostInfo.avatar
        newRelation['gmtLastContact'] = null
        if(isNotEmpty(this.hostInfo .gmtLastLogin)){
          newRelation['gmtLastLogin'] = this.hostInfo.gmtLastLogin
        }else{
          newRelation['gmtLastLogin'] = null
        }
        newRelation['nickName'] = this.hostInfo.nickName
        newRelation['gender'] = this.hostInfo.gender
        newRelation['role'] = this.hostInfo.role

        this.julyChat.variable.participantInfo.data[newRelation.userId] = newRelation
        console.log(this.julyChat.variable.participantInfo.data)
        return this.hostInfo
      }else{
        return null
      }

    },
    async getUserInfo (userId) {
      let result=null
      await this.$store.dispatch('user/getUserInfo', userId)
          .then((data) => {
            // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
            console.log('got user info successful')
            result = data
          })
          .catch((error) => {
            console.log('got user info fail')
            FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            result = null
          })
      return result
    },
    async getUserRelation (userId,peerId,category) {
      let result=null
      await this.$store.dispatch('user/getUserRelation', {userId:userId,peerId:peerId,category:category})
          .then((data) => {
            // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
            console.log('getUserRelation successful')
            result = data
          })
          .catch((error) => {
            console.log('getUserRelation fail')
            FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            result = null
          })
      return result
    },
    onObservedElement(entry){
      this.julyChat.variable.isChatViewObservedBottom = entry.isIntersecting
    },
    leaveChat(){
      this.julyChat.variable.inputMessageLoading = true
      this.julyWebsocket.constant.stompClient.send(this.sendChatSessionWatchURI, {},JSON.stringify({watcherId:this.hostId,peerId:this.julyChat.variable.peerId,peerType:this.julyChat.variable.peerType,action:CONSTANT.CHAT_WATCH_ACTION_LEAVE}))
    },
    getLock(lockObject){
      let entranceTS = new Date(Date.now())
      // 设置要先获取updateMsgItems的锁，这里形成自选，并且判断等待锁的时间，如果超过循环任务的时间间隔一半就放弃更新
      while (lockObject.lock){
        let probeTS = new Date(Date.now())
        let waitingDiff = date.getDateDiff(probeTS,entranceTS,'seconds')
        if(lockObject.expired>0&&waitingDiff>=lockObject.expired){
          console.log('waiting for release lock too long time, out deadline')
          return false
        }
      }
      lockObject.lock = true
      return true
    },
    releaseLock(lockObject){
      lockObject.lock = false
    },
    onChatViewScroll (info){
      info
      // console.log('onChatViewScroll')
      // console.log(info)
    },
    onLoad (index, done) {
      setTimeout(() => {
        // console.log('onLoad '+index)
        // setTimeout(() => {
        //   if (this.items) {
        //
        //     done()
        //   }
        // }, 2000)
        // if (this.testMsgItems.length>0){
        //   this.candidateMsgItems.data.push(this.testMsgItems.shift())
        // }
        done()
      }, 2000)
    },
    destroyChat(){
      clearInterval(this.updateView)
      // clearInterval(this.getRemoteMessages)
    }
  }
}
</script>

<style scoped>
.chat-window {
  height: 100%;

}
.chat-main-message-area {
  padding: 5px 3%;
}

.shadow-box{
  border: 1px solid #eee;
  border-radius: 5px;
}
.my-card {
  transition: box-shadow .3s;
  width: 100%;
  height: 100%;
  border-radius:10px;

}
.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}

</style>
