<!--<template>-->
<!--  <div></div>-->
<!--</template>-->
<!--<script>-->
<!--import SockJS from 'sockjs-client'-->
<!--import Stomp from 'stompjs'-->
<!--import {getToken,getHostId} from "@/utils/auth";-->
<!--export default {-->
<!--  name: 'wstest',-->
<!--  data () {-->
<!--    const wsHeartBeatURI = '/app/message'-->
<!--    const wsHeartBeatURIInterval = 1000-->
<!--    const wsEndpoint = 'http://localhost:80/meeting/endpointChat?Authorization=' + getToken()-->
<!--    return {-->
<!--      wsHeartBeatURI: wsHeartBeatURI,-->
<!--      wsHeartBeatURIInterval: wsHeartBeatURIInterval,-->
<!--      wsEndpoint: wsEndpoint,-->
<!--      stompClient: '',-->
<!--      timer: '',-->
<!--      responseText: '',-->
<!--      inputValue: '',-->
<!--      isAudioOnly: true,-->
<!--      ws: null,-->
<!--      count: 0,-->
<!--      list: [], // 聊天记录的数组-->
<!--      contentText: '' // input输入的值-->
<!--    }-->
<!--  },-->
<!--  computed: {-->
<!--    hostId () {-->
<!--      return getHostId()-->
<!--    }-->
<!--  },-->
<!--  async mounted () {-->
<!--    this.initHost()-->
<!--    try {-->
<!--      await this.initWS()-->
<!--    } catch (e) {-->
<!--      console.log('websocket错误:' + e.message)-->
<!--    }-->
<!--  },-->
<!--  beforeDestroy () {-->
<!--    // 如果跳转别的页面的时候不仍保持websocket通信，此步可注释，否则定时器要清除-->
<!--    // clearInterval(this.timer)-->
<!--    this.disconnect()-->
<!--  },-->
<!--  created () {-->
<!--  },-->
<!--  methods: {-->

<!--    initHost () {-->
<!--      this.$store.dispatch('user/getUserInfo', this.hostId)-->
<!--        .then((data) => {-->
<!--          // this.$router.push({path: this.redirect || '/', query: this.otherQuery})-->
<!--          console.log('got host info successful')-->
<!--          console.log(data)-->
<!--        })-->
<!--        .catch((error) => {-->
<!--          console.log('got host info fail')-->
<!--          console.log(error)-->
<!--        })-->
<!--    },-->
<!--    async initWS () {-->
<!--      this.initWSConnection()-->
<!--      // 断开重连机制-->
<!--      this.timer = setInterval(() => {-->
<!--        try {-->
<!--          this.stompClient.send(this.wsHeartBeatURI, JSON.stringify({'message': 'test'}))-->
<!--        } catch (e) {-->
<!--          console.log('WebSocket connection interrupt: ' + e)-->
<!--          this.initWSConnection()-->
<!--        }-->
<!--      }, this.wsHeartBeatURIInterval)-->
<!--    },-->
<!--    initWSConnection () {-->
<!--      // 建立连接对象-->
<!--      // 连接服务端提供的通信接口，连接以后才可以订阅广播消息和个人消息-->
<!--      let socket = new SockJS(this.wsEndpoint)// 后端提供协议字段-->
<!--      // let socket = new SockJS('http://localhost:80/user/' + this.$store.getters.hostId+'?Authorization=' + this.$store.getters.hostToken)// 后端提供协议字段-->
<!--      socket.onopen = () => {-->
<!--        console.log('localWebsocket打开')-->
<!--      }-->
<!--      socket.onerror = () => {-->
<!--        console.log('localWebsocket错误')-->
<!--        // 重连？-->
<!--      }-->
<!--      socket.onclose = (e) => {-->
<!--        console.log('localWebsocket关闭' + e)-->
<!--      }-->
<!--      // 获取STOMP子协议的客户端对象-->
<!--      this.stompClient = Stomp.over(socket)-->
<!--      // this.stompClient.heartbeat.outgoing = 20000;  // client will send heartbeats every 20000ms-->
<!--      // this.stompClient.heartbeat.incoming = 0;      // client does not want to receive heartbeats from the server  0表示客户端不从服务端接收心跳包-->
<!--      // The heart-beating is using window.setInterval() to regularly send heart-beats and/or check server heart-beats-->
<!--      // 拦截输出的一大堆垃圾信息-->
<!--      this.stompClient.debug = function (str) {-->
<!--        console.log(str)-->
<!--      }-->
<!--      // let headers = {'Authorization': 'Bearer ' + this.$store.getters.hostToken}-->
<!--      // console.log(headers)-->
<!--      // this.stompClient.connect(-->
<!--      //   headers, // headers-->
<!--      //   function connectCallback () {-->
<!--      //     // 订阅服务端消息 subscribe(destination url, callback[, headers])-->
<!--      //     __this.stompClient.subscribe('/user/topic/msg', (response) => { // 后端提供订阅地址-->
<!--      //       console.log(response)// 接收后端response数据-->
<!--      //       this.testText = response-->
<!--      //     })-->
<!--      //-->
<!--      //     // __this.stompClient.subscribe('/topic/broadcast', (response) => { // 后端提供订阅地址-->
<!--      //     //   console.log(response)// 接收后端response数据-->
<!--      //     //   this.testText = response-->
<!--      //     // })-->
<!--      //   },-->
<!--      //   function errorCallBack (error) {-->
<!--      //     console.log('连接失败:' + error)-->
<!--      //   }-->
<!--      // )-->
<!--    },-->

<!--    // onConnected (globalWebsocketUrl) {-->
<!--    //   let _that = this-->
<!--    //   _that.stompClient = Stomp.client(globalWebsocketUrl, this.responseCallback(), this.onFailed())-->
<!--    //   console.log('连接已建立&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;')-->
<!--    //   // 向服务器发起websocket连接-->
<!--    //   _that.stompClient.connect({}, (frame) => {-->
<!--    //     _that.stompClient.subscribe('/user/' + 'zhangsan' + '/handsUp', (msg) => {-->
<!--    //       console.log('msg-&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;', msg)-->
<!--    //     })-->
<!--    //   }, (err) => {-->
<!--    //     console.log(err)-->
<!--    //   })-->
<!--    //   // 断开连接-->
<!--    //   _that.disconnect = () => {-->
<!--    //     if (_that.stompClient != null) {-->
<!--    //       _that.stompClient.disconnect()-->
<!--    //     }-->
<!--    //   }-->
<!--    // },-->
<!--    onFailed (frame) {-->
<!--      console.log('Failed: ' + frame)-->
<!--    },-->
<!--    responseCallback (frame) {-->
<!--      console.log('responseCallback msg=>' + frame)-->
<!--    },-->
<!--    // 关闭连接-->
<!--    closeWebSocket () {-->
<!--      this.disconnect()-->
<!--    },-->
<!--    meeting () {-->
<!--      // // 判断是否有 navigator.mediaDevices，没有赋成空对象-->
<!--      // if (navigator.mediaDevices === undefined) {-->
<!--      //   navigator.mediaDevices = {}-->
<!--      // }-->
<!--      //-->
<!--      // // 继续判断是否有 navigator.mediaDevices.getUserMedia，没有就采用 navigator.getUserMedia-->
<!--      // if (navigator.mediaDevices.getUserMedia) {-->
<!--      //   navigator.mediaDevices.getUserMedia = function (prams) {-->
<!--      //     let getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia-->
<!--      //     // 兼容获取-->
<!--      //     if (!getUserMedia) {-->
<!--      //       return Promise.reject(new Error('getUserMedia is not implemented in this browser'))-->
<!--      //     }-->
<!--      //     return new Promise(function (resolve, reject) {-->
<!--      //       getUserMedia.call(navigator, prams, resolve, reject)-->
<!--      //     })-->
<!--      //   }-->
<!--      // }-->
<!--      // navigator.mediaDevices.getUserMedia(constraints)-->
<!--      //   .then(stream => {-->
<!--      //     let video = document.querySelector('#Rtc')-->
<!--      //     if ('srcObject' in video) { // 判断是否支持 srcObject 属性-->
<!--      //       video.srcObject = stream-->
<!--      //     } else {-->
<!--      //       video.src = window.URL.createObjectURL(stream)-->
<!--      //     }-->
<!--      //     video.onloadedmetadata = function (e) {-->
<!--      //       video.play()-->
<!--      //     }-->
<!--      //   })-->
<!--      //   .catch((err) => { // 捕获错误-->
<!--      //     console.error(err.name + ': ' + err.message)-->
<!--      //   })-->
<!--      //-->
<!--      // var self = this-->
<!--      // self.pc = new RTCPeerConnection(iceServer)-->
<!--      //-->
<!--      // navigator.mediaDevices-->
<!--      //   .getUserMedia({ audio: true, video: false })// 音频通话，如果为视频通信把video后面的false改为true即可-->
<!--      //   .then(function (stream) {-->
<!--      //     // 绑定本地音频流'LocalAudio'-->
<!--      //     self.thisvideo = document.getElementById('LocalAudio')-->
<!--      //     self.addVideoURL('LocalAudio', stream)// 自己写的函数-->
<!--      //     self.thisvideo.muted = true-->
<!--      //     self.localStream = stream-->
<!--      //     self.pc.addStream(stream)-->
<!--      //     if (data) {-->
<!--      //       self.sendOffer()// 自己写的函数-->
<!--      //     } else {-->
<!--      //       self.handleOffer(self.offer)// 自己写的函数-->
<!--      //       self.sendAnswer()// 自己写的函数-->
<!--      //     }-->
<!--      //   })-->
<!--      //   .catch(function (err) {-->
<!--      //     console.log(err.name + ': ' + err.message)-->
<!--      //   })-->
<!--    },-->
<!--    // addVideoURL (elementId, stream) {-->
<!--    //   var video = document.getElementById(elementId)-->
<!--    //   // Old brower may have no srcObject-->
<!--    //   if ('srcObject' in video) {-->
<!--    //     video.srcObject = stream-->
<!--    //   } else {-->
<!--    //     // 防止在新的浏览器里使用它，应为它已经不再支持了-->
<!--    //     video.src = window.URL.createObjectURL(stream)-->
<!--    //   }-->
<!--    // },-->
<!--    // initiateCall (data) {-->
<!--    //   console.log('initiateCall:' + data)-->
<!--    //   let iceServer = {-->
<!--    //     'iceServers': [{-->
<!--    //       'url': 'stun:stun.l.google.com:19302'-->
<!--    //     }]-->
<!--    //   }-->
<!--    //   this.pc = new RTCPeerConnection(iceServer)-->
<!--    //-->
<!--    //   this.pc.onicecandidate = function (event) {-->
<!--    //     console.log('send ice')-->
<!--    //     let message = {-->
<!--    //       candidate: event.candidate,-->
<!--    //       to: this.choiceUser[0].userId-->
<!--    //     }-->
<!--    //     self.msgtype = 9-->
<!--    //     self.messageValue = JSON.stringify(message)-->
<!--    //     console.log(this.messageValue)-->
<!--    //     self.send()-->
<!--    //   }-->
<!--    //   this.pc.onaddstream = function (e) {-->
<!--    //     console.log('onaddstream')-->
<!--    //     if (this.isAudioOnly) {-->
<!--    //       var video = document.getElementById('RemoteAudio')-->
<!--    //       this.addVideoURL('RemoteAudio', e.stream)-->
<!--    //     } else {-->
<!--    //       var video = document.getElementById('RemoteVideo')-->
<!--    //       this.addVideoURL('RemoteVideo', e.stream)-->
<!--    //     }-->
<!--    //   }-->
<!--    //-->
<!--    //   if (this.isAudioOnly) {-->
<!--    //     console.log('初始化音频')-->
<!--    //     navigator.mediaDevices-->
<!--    //       .getUserMedia({audio: true, video: false})-->
<!--    //       .then(function (stream) {-->
<!--    //         this.thisvideo = document.getElementById('LocalAudio')-->
<!--    //         this.addVideoURL('LocalAudio', stream)-->
<!--    //         this.thisvideo.muted = true-->
<!--    //         this.localStream = stream-->
<!--    //         this.pc.addStream(stream)-->
<!--    //         if (data) {-->
<!--    //           self.sendOffer()-->
<!--    //         } else {-->
<!--    //           self.handleOffer(self.offer)-->
<!--    //           self.sendAnswer()-->
<!--    //         }-->
<!--    //       })-->
<!--    //       .catch(function (err) {-->
<!--    //         console.log(err.name + ': ' + err.message)-->
<!--    //       })-->
<!--    //   } else {-->
<!--    //     console.log('初始化视频')-->
<!--    //     navigator.mediaDevices-->
<!--    //       .getUserMedia({audio: true, video: true})-->
<!--    //       .then(function (stream) {-->
<!--    //         self.thisvideo = document.getElementById('LocalVideo')-->
<!--    //         self.addVideoURL('LocalVideo', stream)-->
<!--    //         self.thisvideo.muted = true-->
<!--    //         self.localStream = stream-->
<!--    //         self.pc.addStream(stream)-->
<!--    //         if (data) {-->
<!--    //           self.sendOffer()-->
<!--    //         } else {-->
<!--    //           self.handleOffer(self.offer)-->
<!--    //           self.sendAnswer()-->
<!--    //         }-->
<!--    //       })-->
<!--    //       .catch(function (err) {-->
<!--    //         console.log(err.name + ': ' + err.message)-->
<!--    //       })-->
<!--    //   }-->
<!--    // },-->
<!--    // sendOffer () {-->
<!--    //   let self = this-->
<!--    //   self.pc.createOffer(-->
<!--    //     offer => {-->
<!--    //       console.log('send offer')-->
<!--    //       let message = {-->
<!--    //         offer: offer,-->
<!--    //         to: self.choiceUser[0].userId-->
<!--    //       }-->
<!--    //       self.msgtype = 10-->
<!--    //       self.messageValue = JSON.stringify(message)-->
<!--    //       // console.log(this.messageValue);-->
<!--    //       self.send()-->
<!--    //       self.pc.setLocalDescription(offer)-->
<!--    //     },-->
<!--    //     error => {-->
<!--    //       alert('Error when creating an offer')-->
<!--    //     }-->
<!--    //   )-->
<!--    // },-->
<!--    sendAnswer () {-->
<!--      let self = this-->
<!--      self.pc.createAnswer(-->
<!--        answer => {-->
<!--          console.log('send answer')-->
<!--          self.pc.setLocalDescription(answer)-->
<!--          let message = {-->
<!--            answer: answer,-->
<!--            to: self.choiceUser[0].userId-->
<!--          }-->
<!--          self.msgtype = 11-->
<!--          self.messageValue = JSON.stringify(message)-->
<!--          // console.log(this.messageValue);-->
<!--          self.send()-->
<!--        },-->
<!--        error => {-->
<!--          alert('Error when creating an answer')-->
<!--          console.log(error)-->
<!--        }-->
<!--      )-->
<!--    },-->
<!--    handleAnswer (data) {-->
<!--      let self = this-->
<!--      self.pc.setRemoteDescription(new RTCSessionDescription(data)).catch(error => {-->
<!--        console.log(error)-->
<!--      })-->
<!--      console.log('handleAnswer ok')-->
<!--    },-->
<!--    handleOffer (data) {-->
<!--      let self = this-->
<!--      self.pc.setRemoteDescription(new RTCSessionDescription(data)).catch(error => {-->
<!--        console.log(error)-->
<!--      })-->
<!--      console.log('handleOffer ok')-->
<!--    },-->
<!--    handleCandidate (data) {-->
<!--      let self = this-->
<!--      self.pc.addIceCandidate(new RTCIceCandidate(data))-->
<!--      console.log('handleCandidate')-->
<!--    }-->

<!--  }-->
<!--}-->
<!--</script>-->
<!--<style lang="scss" scoped>-->
<!--.chat-box {-->
<!--  margin: 0 auto;-->
<!--  background: #fafafa;-->
<!--  position: absolute;-->
<!--  height: 100%;-->
<!--  width: 100%;-->
<!--  max-width: 700px;-->
<!--  header {-->
<!--    position: fixed;-->
<!--    width: 100%;-->
<!--    height: 3rem;-->
<!--    background: #409eff;-->
<!--    max-width: 700px;-->
<!--    display: flex;-->
<!--    justify-content: center;-->
<!--    align-items: center;-->
<!--    font-weight: bold;-->
<!--    color: white;-->
<!--    font-size: 1rem;-->
<!--  }-->
<!--  .msg-box {-->
<!--    position: absolute;-->
<!--    height: calc(100% - 6.5rem);-->
<!--    width: 100%;-->
<!--    margin-top: 3rem;-->
<!--    overflow-y: scroll;-->
<!--    .msg {-->
<!--      width: 95%;-->
<!--      min-height: 2.5rem;-->
<!--      margin: 1rem 0.5rem;-->
<!--      position: relative;-->
<!--      display: flex;-->
<!--      justify-content: flex-start !important;-->
<!--      .user-head {-->
<!--        min-width: 2.5rem;-->
<!--        width: 20%;-->
<!--        width: 2.5rem;-->
<!--        height: 2.5rem;-->
<!--        border-radius: 50%;-->
<!--        background: #f1f1f1;-->
<!--        display: flex;-->
<!--        justify-content: center;-->
<!--        align-items: center;-->
<!--        .head {-->
<!--          width: 1.2rem;-->
<!--          height: 1.2rem;-->
<!--        }-->
<!--        // position: absolute;-->
<!--      }-->
<!--      .user-msg {-->
<!--        width: 80%;-->
<!--        // position: absolute;-->
<!--        word-break: break-all;-->
<!--        position: relative;-->
<!--        z-index: 5;-->
<!--        span {-->
<!--          display: inline-block;-->
<!--          padding: 0.5rem 0.7rem;-->
<!--          border-radius: 0.5rem;-->
<!--          margin-top: 0.2rem;-->
<!--          font-size: 0.88rem;-->
<!--        }-->
<!--        .left {-->
<!--          background: white;-->
<!--          animation: toLeft 0.5s ease both 1;-->
<!--        }-->
<!--        .right {-->
<!--          background: #53a8ff;-->
<!--          color: white;-->
<!--          animation: toright 0.5s ease both 1;-->
<!--        }-->
<!--        @keyframes toLeft {-->
<!--          0% {-->
<!--            opacity: 0;-->
<!--            transform: translateX(-10px);-->
<!--          }-->
<!--          100% {-->
<!--            opacity: 1;-->
<!--            transform: translateX(0px);-->
<!--          }-->
<!--        }-->
<!--        @keyframes toright {-->
<!--          0% {-->
<!--            opacity: 0;-->
<!--            transform: translateX(10px);-->
<!--          }-->
<!--          100% {-->
<!--            opacity: 1;-->
<!--            transform: translateX(0px);-->
<!--          }-->
<!--        }-->
<!--      }-->
<!--    }-->
<!--  }-->
<!--  .input-box {-->
<!--    padding: 0 0.5rem;-->
<!--    position: absolute;-->
<!--    bottom: 0;-->
<!--    width: 100%;-->
<!--    height: 3.5rem;-->
<!--    background: #fafafa;-->
<!--    box-shadow: 0 0 5px #ccc;-->
<!--    display: flex;-->
<!--    justify-content: space-between;-->
<!--    align-items: center;-->
<!--    input {-->
<!--      height: 2.3rem;-->
<!--      display: inline-block;-->
<!--      width: 100%;-->
<!--      padding: 0.5rem;-->
<!--      border: none;-->
<!--      border-radius: 0.2rem;-->
<!--      font-size: 0.88rem;-->
<!--    }-->
<!--    .btn {-->
<!--      height: 2.3rem;-->
<!--      min-width: 4rem;-->
<!--      background: #e0e0e0;-->
<!--      padding: 0.5rem;-->
<!--      font-size: 0.88rem;-->
<!--      color: white;-->
<!--      text-align: center;-->
<!--      border-radius: 0.2rem;-->
<!--      margin-left: 0.5rem;-->
<!--      transition: 0.5s;-->
<!--    }-->
<!--    .btn-active {-->
<!--      background: #409eff;-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</style>-->
