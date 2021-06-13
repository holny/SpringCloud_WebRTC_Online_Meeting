<template>
  <div class="chat-window q-pa-md justify-center">
    <q-card class="my-card" flat bordered>

      <q-card-section horizontal style="height: 12%">
        <q-img class="col" native-context-menu src="https://cdn.quasar.dev/img/mountains.jpg" >

          <div class="fit">
            <q-item>
              <q-item-section avatar>
                <q-avatar>
                  <img src="https://cdn.quasar.dev/img/avatar2.jpg">
                </q-avatar>
              </q-item-section>
              <q-item-section>
                <q-item-label class="text-h6 text-white">{{ julyChat.variable.participantInfo.data.size}}</q-item-label>
                <q-item-label class="text-caption text-white">Subhead</q-item-label>
              </q-item-section>
            </q-item>
            <q-card-actions class="absolute-right justify-around">
              <q-btn class="btn-small" flat round color="green" icon="videocam" />
              <q-btn class="btn-small" flat round color="indigo" icon="mic" />
              <q-btn class="btn-small" flat round color="indigo" icon="star_outline" />
              <q-btn class="btn-small" v-show="julyChat.variable.chatType==='room'" @click="leaveRoomChat" flat round color="negative" icon="fas fa-sign-out-alt" />
            </q-card-actions>
            <q-icon v-if="julyWebsocket.variable.connectionFlag['websocket']['status']===true" class="absolute-top-right q-mr-md q-mt-xs" size="sm" color="green" name="link" >
              <q-tooltip>
                <strong >当前前WebSocket已连接</strong>
              </q-tooltip>
            </q-icon>
            <q-icon v-else class="absolute-top-right q-mr-md q-mt-xs" size="sm" color="negative" name="link_off" >
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
        <div id="chat-scroll-container" class="scroll inset-shadow-down shadow-10 shadow-box q-pb-md " style="overflow: auto;min-height:100%">
          <q-infinite-scroll scroll-target="#chat-scroll-container"  :offset="250" :thumb-style="thumbStyle" :bar-style="barStyle" @load="onLoad" reverse ref="scrollArea">
<!--            <template slot="loading">-->
<!--              <div class="row justify-center q-my-md">-->
<!--                <q-spinner color="primary" name="dots" size="40px" />-->
<!--              </div>-->
<!--            </template>-->
            <q-scroll-observer @scroll="onChatViewScroll"  />
            <div v-for="(msgItem, index) in julyChat.data.updateMsgItems.data"  :key="'updateId'+index" class="chat-main-message-area q-py-sm">
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
import {getHostId, getToken} from "@/utils/auth";
import {initJulyWS, closeConnectionJuly} from "@/utils/socket";
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
    }
  },
  data () {
    return {
      dense: true,
      hostId: getHostId(),
      julyChat: {
        constant: {
          combineMsgGap:10,
          updateViewInterval:2000, //ms , 每隔一段时间更新时间戳显示
          getRemoteMessagesInterval:5000
        },
        variable: {
          chatType:'room', // personal or room
          objectId:'10002', // 如果聊天是私人聊天，roomId就是对方userId，如果是公共的，就是roomId
          lastUpdateTime: '',
          chatViewScrollEleTarget: '',
          isChatViewObservedBottom: true, // true: 代表聊天界面的scroll bar拉倒最底，false则不是最底
          inputMessage: '',
          inputMessageLoading: false,
          participantInfo: {'updateDate':null,'data':new Map()},
        },
        data: {
          // 注意：以下Items默认按时间，越近的下标越大
          testMsgItems: [],
          // expired 代表无限等待锁的释放
          candidateMsgItems: {'lockObject':{'name':'candidateMsgItemsLock','lock':false,'expired':-1},'data':[]},
          // lockObject 自旋锁，false=没有在更新(改变数组)，true=有在更新
          updateMsgItems: {'lockObject':{'name':'updateMsgItemsLock','lock':false,'expired':1},'data':[]}
        }
      },
      julyWebsocket:{
        constant: {
          WSEndPointURI:  'http://localhost:80/meeting/endpointWS?Authorization=' + getToken(),
          sendWSHeartBeatURI: '/app/heartbeat',
          subWSHeartBeatURI: '/user/topic/heartbeat',
          stompClient: this.stompClient
        },
        variable: {
          connectionFlag: this.connectionFlag!=null?this.connectionFlag:{'websocket':{'name':'websocket',"status":false}},
          headers: {'Authorization': 'Bearer ' + getToken()}
        },
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
       if (this.julyChat.variable.chatType==='room'){
         return '/user/topic/room/'+this.julyChat.variable.objectId
       }else if (this.julyChat.variable.chatType==='personal'){
         return '/user/topic/personal/'+this.hostId
       }else{
         return null
       }
    },
    sendMsgURI() {
      if (this.julyChat.variable.chatType==='room'){
        return '/app/room/'+this.julyChat.variable.objectId+'/sendmsg'
      }else if (this.julyChat.variable.chatType==='personal'){
        return '/app/personal/'+this.julyChat.variable.objectId+'/sendmsg'
      }else{
        return null
      }
    },
    ackURI() {
      if (this.julyChat.variable.chatType==='room'){
        return '/app/room/'+this.julyChat.variable.objectId+'/ack'
      }else if (this.julyChat.variable.chatType==='personal'){
        return '/app/personal/'+this.hostId+'/ack'
      }else{
        return null
      }
    },
    joinRoomURI() {
      if (this.julyChat.variable.chatType==='room'){
        return '/app/room/'+this.julyChat.variable.objectId+'/join'
      }else{
        return null
      }
    },
    leaveRoomURI() {
      if (this.julyChat.variable.chatType==='room'){
        return '/app/room/'+this.julyChat.variable.objectId+'/leave'
      }else{
        return null
      }
    },
    chatViewScrollEleTarget(){
       return getScrollTarget(document.getElementById('chat-scroll-container'))
    }
  },
  mounted () {
    this.updateUserInfo(this.hostId)
    // this.initWSEnv()
    console.log("chatWindow")
    console.log(this.julyWebsocket.constant.stompClient)
    this.initChatWSConnection(this.julyChat.variable.chatType,this.julyWebsocket.constant.stompClient,this.julyWebsocket.variable.headers)
    // if(this.stompClient==null){
    //   this.initWSWatcher()
    // }
    // this.initView()
  },
  // keep-alive会缓存组件状态，activated()和 deactivated() 这两个钩子函数。activated()是keep-alive 组件激活时调用，而 deactivated() 是 keep-alive 组件停用时调用。activated ()替换mounted()
  activated () {
  },
  destroyed() {
    this.destoryChat()
    // this.leaveRoomChat()
    closeConnectionJuly(this.julyWebsocket.constant.stompClient)
  },
  methods: {
    initWSEnv(){
      if (this.julyWebsocket.constant.stompClient==null){
        console.log("chatWindow websocket not init , so start init by self")
        this.julyWebsocket.constant.stompClient = initJulyWS(this.julyWebsocket.constant.WSEndPointURI,this.julyWebsocket.constant.sendWSHeartBeatURI)
      }
    },
    initWSWatcher(){
      console.log("chatWindow websocket watcher not init , so start init by self")
      let _that = this
      this.julyWebsocket.constant.stompClient.connect(
          _that.julyWebsocket.variable.headers,
          function connectCallback () {
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
    initChatWSConnection(type,stompClient,headers){
      if (type==='room'){
        this.initRoomChat(stompClient,headers,this.julyWebsocket.variable.connectionFlag,this.joinRoomURI,this.receiverURI)
      }else if (type==='personal'){
        this.initPersonalChat(stompClient,headers,this.julyWebsocket.variable.connectionFlag,this.receiverURI)
      }
    },
    async initPersonalChat(stompClient,headers,connectionFlag,receiverURI){
      // 初始化1v1聊天
      let _that = this
      stompClient.connect(
          headers,
          function connectCallback () {
            // 订阅接收消息(message/event)
            stompClient.subscribe(receiverURI, (response) => {
              let feedback = JSON.parse(response.body)
              console.log(feedback)
              if (feedback.type=== 'message'){
                let feedbackDate = date.extractDate(feedback.gmtCreate)
                console.log("feedbackDate +"+feedbackDate)
                let nowTS = date.formatDate( feedbackDate,'x')
                let newMsg = {'sender':feedback.from,'startTS': nowTS,'endTS': nowTS,'stamp':date.formatDate(date.extractDate(nowTS, 'x'), 'YYYY-MM-DD HH:mm:ss'),'message':[feedback.message]}
                _that.insertMessage(newMsg)
              }
            })
          },
          function errorCallBack (error) {
            connectionFlag['websocket']['status'] = false
            console.log('订阅失败:' + error)
          }
      )
    },
    async initRoomChat(stompClient,headers,connectionFlag,joinRoomURI,receiverURI){
      console.log("initRoomChat")
      console.log(stompClient)
      console.log(joinRoomURI)
      // 初始化1vN聊天室
      this.julyChat.variable.inputMessageLoading = true
      let _that = this
      stompClient.subscribe(joinRoomURI, (response) => { // 后端提供订阅地址
        let feedback = JSON.parse(response.body)
        if (feedback.code===10001){
          console.log("已进入房间")
          let roomInfo = feedback.data
          let nowTS = date.formatDate(new Date(Date.now()) , 'x')
          // 新进入房间要更新房间内用户的信息
          connectionFlag['websocket']['status'] = true
          _that.updateRoomParticipant(roomInfo.memberIdSet,nowTS)
        }else{
          console.log("进入房间失败 error:"+feedback.msg)
        }
        _that.julyChat.variable.inputMessageLoading = false
      })
      stompClient.subscribe(receiverURI, (response) => { // 后端提供订阅地址
        let feedback = JSON.parse(response.body)
        console.log(feedback)
        if (feedback.type=== 'message'){
          // type=== 'message' 说明是来了新聊天消息
          let feedbackDate = date.extractDate(feedback.gmtCreate)
          console.log("feedbackDate +"+feedbackDate)
          let nowTS = date.formatDate( feedbackDate,'x')
          let newMsg = {'sender':feedback.from,'startTS': nowTS,'endTS': nowTS,'stamp':date.formatDate(date.extractDate(nowTS, 'x'), 'YYYY-MM-DD HH:mm:ss'),'message':[feedback.message]}
          _that.insertMessage(newMsg)
        }else if (feedback.type=== 'event'){
          // type=== 'message' 说明是来了新事件
          let feedbackDate = date.extractDate(feedback.gmtCreate)
          console.log("feedbackDate +"+feedbackDate)
          let feedbackDateTS = date.formatDate( feedbackDate,'x')
          if (feedback.code==='E1001'){ // E1001 有新人进入事件
            _that.updateRoomParticipant(feedback.data.memberIdSet,feedbackDateTS)
          }
        }
        return response
      })
      // stompClient.connect(
      //     headers,
      //     function connectCallback () {
      //       // 初始要订阅进入Room，后端返回room信息
      //       stompClient.subscribe(joinRoomURI, (response) => { // 后端提供订阅地址
      //         let feedback = JSON.parse(response.body)
      //         if (feedback.code===10001){
      //           console.log("已进入房间")
      //           let roomInfo = feedback.data
      //           let nowTS = date.formatDate(new Date(Date.now()) , 'x')
      //           // 新进入房间要更新房间内用户的信息
      //           connectionFlag['websocket']['status'] = true
      //           _that.updateRoomParticipant(roomInfo.memberIdSet,nowTS)
      //         }else{
      //           console.log("进入房间失败 error:"+feedback.msg)
      //         }
      //         _that.julyChat.variable.inputMessageLoading = false
      //       })
      //       // 订阅接收消息(message/event)
      //       stompClient.subscribe(receiverURI, (response) => { // 后端提供订阅地址
      //         let feedback = JSON.parse(response.body)
      //         console.log(feedback)
      //         if (feedback.type=== 'message'){
      //           // type=== 'message' 说明是来了新聊天消息
      //           let feedbackDate = date.extractDate(feedback.gmtCreate)
      //           console.log("feedbackDate +"+feedbackDate)
      //           let nowTS = date.formatDate( feedbackDate,'x')
      //           let newMsg = {'sender':feedback.from,'startTS': nowTS,'endTS': nowTS,'stamp':date.formatDate(date.extractDate(nowTS, 'x'), 'YYYY-MM-DD HH:mm:ss'),'message':[feedback.message]}
      //           _that.insertMessage(newMsg)
      //         }else if (feedback.type=== 'event'){
      //           // type=== 'message' 说明是来了新事件
      //           let feedbackDate = date.extractDate(feedback.gmtCreate)
      //           console.log("feedbackDate +"+feedbackDate)
      //           let feedbackDateTS = date.formatDate( feedbackDate,'x')
      //           if (feedback.code==='E1001'){ // E1001 有新人进入事件
      //             _that.updateRoomParticipant(feedback.data.memberIdSet,feedbackDateTS)
      //           }
      //         }
      //         return response
      //       })
      //     },
      //     function errorCallBack (error) {
      //       connectionFlag['websocket']['status'] = false
      //       console.log('进入到房间 失败:' + error)
      //       return error
      //     }
      // )
      //
    },
    initView(){
      this.scrollToBottom() // 开局首先scrollbar保持底部
      // 先立即执行一次更新View(更新历史消息状态)
      this.updateView()
      setInterval(this.updateView, this.julyChat.constant.updateViewInterval)
      // setInterval(this.getRemoteMessages, this.julyChat.constant.getRemoteMessagesInterval)
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
        console.log(participantIds)
        console.log(completeParticipantIds)
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
    getRemoteMessages(){
      if (this.julyChat.data.testMsgItems.length>0){
        this.julyChat.data.candidateMsgItems.data.push(this.julyChat.data.testMsgItems.shift())
      }
      if (this.julyChat.data.candidateMsgItems.data.length > 0) {
        // 如果candidateMsgItems存在数据了说明接收到了新数据
        // 首先获取candidateMsgItems的lock，把candidateMsgItems的数据Push到updateMsgItems尾部，并且清空candidateMsgItems，最后释放candidateMsgItems lock
        if (!this.getLock(this.julyChat.data.candidateMsgItems.lockObject)) {
          console.log('got candidateMsgItems lock out deadline, Fail, give up this job')
          this.releaseLock(this.julyChat.data.candidateMsgItems.lockObject)
          return
        }
        try{
          for (let index in this.julyChat.data.candidateMsgItems.data) {
            this.insertMessage(this.julyChat.data.candidateMsgItems.data[index])
          }
          this.julyChat.data.candidateMsgItems.data.splice(0, this.julyChat.data.candidateMsgItems.data.length)
        }catch (error){
          console.error('updateRecentMessageState -candidateMsgItems error:'+error)
        }finally {
          this.releaseLock(this.julyChat.data.candidateMsgItems.lockObject)
        }
      }

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
      if (!this.getLock(this.julyChat.data.updateMsgItems.lockObject)) {
        console.log('got updateMsgItems lock out deadline, Fail, give up this job')
        return
      }
      try {
        this.julyChat.variable.lastUpdateTime = this.convertStamp(this.julyChat.data.updateMsgItems.data)
      } catch (error){
        console.error('updateRecentMessageState error:' + error)
      }  finally    {
        this.releaseLock(this.julyChat.data.updateMsgItems.lockObject)
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
              msgItems[index].stamp = date.formatDate(date.extractDate(msgItems[index].endTS, 'x'), 'YYYY-MM-DD HH:mm:ss')
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

        let server_message = {
          'from':this.hostId,
          'to':'all',
          'message':msgText,
          'method':'broadcast',
        }
        this.julyWebsocket.constant.stompClient.send(this.sendMsgURI, {},JSON.stringify(server_message))
        // this.julyWebsocket.constant.stompClient.connect(
        //     this.julyWebsocket.variable.headers, // headers
        //     function connectCallback () {
        //       // 订阅服务端消息 subscribe(destination url, callback[, headers])
        //       _that.julyWebsocket.constant.stompClient.send(_that.julyWebsocket.variable.sendMessageURI, JSON.stringify(server_message))
        //     },
        //     function errorCallBack (error) {
        //       console.log('发送 连接 失败:' + error)
        //       return error
        //     }
        // )
        this.julyChat.variable.inputMessageLoading = false
      }
    },
    insertMessage(msgItem){
      // 在当前聊天界面立即插入消息
      if (!this.getLock(this.julyChat.data.updateMsgItems.lockObject)) {
        console.log('got updateMsgItems lock out deadline, Fail, give up this job')
        return
      }
      let beforeState = this.julyChat.variable.isChatViewObservedBottom
      // 对于新来的聊天消息要更新时间戳，不直接显示具体时间，而显示xxx ago
      this.convertStamp([msgItem])
      try {
        console.log('sendMessage  updateMsgItems.data.length:' + this.julyChat.data.updateMsgItems.data.length)
        if (this.julyChat.data.updateMsgItems.data.length>0){
          // 如果当前消息界面已有遗留消息显示(当前新插入消息不是第一条)，需要合并新消息到旧消息上(Quasar消息需要这样做才好看，其它的随意)
          // 默认插入位置是updateMsgItems最后一位(如果是最新消息)，因为updateMsgItems按时间从远到近排列
          let insertPosition = this.julyChat.data.updateMsgItems.data.length
          for (let index= this.julyChat.data.updateMsgItems.data.length;index>0;index--){
            // 要根据endTS消息发出时间比较，找到合适的地方插入
            if (msgItem.endTS<=this.julyChat.data.updateMsgItems.data[index-1].startTS){
              insertPosition = index-1
            }else{
              break
            }
          }
          let nowDate = new Date(Date.now())
          // 看所插入位置前面一位的endTS，来决定是否合并
          if (insertPosition>0&&this.julyChat.data.updateMsgItems.data[insertPosition-1].sender === msgItem.sender) {
            let diffSeconds = date.getDateDiff(nowDate, date.extractDate(this.julyChat.data.updateMsgItems.data[insertPosition-1].endTS, 'x'), 'seconds')
            // console.log('insertMessage  diffSeconds:'+diffSeconds)
            if (diffSeconds <= this.julyChat.constant.combineMsgGap) {
              for(let index in  msgItem.message){
                this.julyChat.data.updateMsgItems.data[insertPosition-1].message.push(msgItem.message[index])
              }
              this.julyChat.data.updateMsgItems.data[insertPosition-1].endTS = msgItem.endTS
            } else {
              this.julyChat.data.updateMsgItems.data.splice(insertPosition,0,msgItem)
            }
          } else {
            this.julyChat.data.updateMsgItems.data.splice(insertPosition,0,msgItem)
          }
        }else{
          // 如果是新消息(第一条消息)，直接插入到updateMsgItems即可
          this.julyChat.data.updateMsgItems.data.splice(0,0,msgItem)
        }

      }catch (error){
        console.error('insertMessage error:' +error.stack)

      }finally {
        this.releaseLock(this.julyChat.data.updateMsgItems.lockObject)
        if (beforeState){
          // 插入之前scrollbar在最底，插入消息完成后，scrollbar还要保持最底
          this.scrollToBottom()
        }
      }
    },
    scrollToBottom(){
      // 设置滚动到底部:
      setScrollPosition (this.chatViewScrollEleTarget, getScrollHeight(this.chatViewScrollEleTarget), 1000)
    },
    updateUserInfo (userId) {
      this.$store.dispatch('user/getUserInfo', userId)
          .then((data) => {
            // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
            console.log('got user info successful')
            console.log(data)
            this.julyChat.variable.participantInfo.data[data.userId] = {'userName':data.userName,'avatar':data.avatar}
            console.log(this.julyChat.variable.participantInfo.data)
          })
          .catch((error) => {
            console.log('got user info fail')
            console.log(error)
          })
    },
    onObservedElement(entry){
      // console.log(entry)
      this.julyChat.variable.isChatViewObservedBottom = entry.isIntersecting
    },
    leaveRoomChat(){
      let _that = this
      _that.julyChat.variable.inputMessageLoading = true
      // this.julyWebsocket.constant.stompClient.send(leaveRoomURI, {},'leave room')
      // this.julyWebsocket.constant.stompClient.connect(
      //     this.julyWebsocket.variable.headers,
      //     function connectCallback () {
      //       // 发送离开room请求
      //       _that.julyWebsocket.constant.stompClient.subscribe(_that.leaveRoomURI, (response) => {
      //         let feedback = JSON.parse(response.body)
      //         console.log(feedback)
      //         if (feedback.code===10001){
      //           let roomInfo = feedback.data
      //           let nowTS = date.formatDate(new Date(Date.now()) , 'x')
      //           // 新进入房间要更新房间内用户的信息
      //           _that.updateRoomParticipant(roomInfo.memberIdSet,nowTS)
      //           if(roomInfo.memberIdSet.indexOf(_that.hostId)<0){
      //             console.log("离开房间成功")
      //           }
      //         }else{
      //           console.log("离开房间失败 error:"+feedback.msg)
      //         }
      //       })
      //     },
      //     function errorCallBack (error) {
      //       _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = false
      //       console.log('订阅失败:' + error)
      //     }
      // )

    },
    getLock(lockObject){
      let entranceTS = new Date(Date.now())
      // 设置要先获取updateMsgItems的锁，这里形成自选，并且判断等待锁的时间，如果超过循环任务的时间间隔一半就放弃更新
      // console.log('start got lock:'+lockObject.name)
      // console.log('this lock expired time:'+lockObject.expired)
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
        //   this.julyChat.data.candidateMsgItems.data.push(this.julyChat.data.testMsgItems.shift())
        // }
        done()
      }, 2000)
    },
    destoryChat(){
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
