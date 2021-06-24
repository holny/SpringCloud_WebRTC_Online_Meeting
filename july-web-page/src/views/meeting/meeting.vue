<template>
  <q-card class="my-card">
    <div class="inline-block" style="width: 50%;">
      <q-item>
        <q-item-section avatar>
          <q-avatar>
            <img :src="hostInfo!=null?hostInfo.avatar:''">
          </q-avatar>
        </q-item-section>

        <q-item-section>
          <q-item-label>{{hostInfo!=null?hostInfo.userName:'Unknown'}}{{hostInfo!=null?(hostInfo.nickName!=null?'('+hostInfo.nickName+')':''):''}}</q-item-label>
          <q-item-label caption>Me</q-item-label>
        </q-item-section>
      </q-item>

      <video playsinline ref="localVideo"  class="video-js vjs-default-skin" style="overflow: hidden"></video>
    </div>
    <div class="inline-block" style="width: 50%;">
      <q-item>
        <q-item-section avatar>
          <q-avatar>
            <img :src="peerInfo!=null?peerInfo.avatar:''">
          </q-avatar>
        </q-item-section>

        <q-item-section>
          <q-item-label>{{peerInfo.remarkName!=null?peerInfo.remarkName:peerInfo.userName}}{{peerInfo.remarkName!=null?'('+peerInfo.userName+')':''}}</q-item-label>
          <q-item-label caption>{{peerInfo.gender=='1'?'Him':'Her'}}</q-item-label>
        </q-item-section>
      </q-item>

      <video playsinline ref="remoteVideo"  class="video-js  vjs-default-skin" style="overflow: hidden"></video>
    </div>
  </q-card>

</template>

<script>
import adapter from 'webrtc-adapter'
// import videoPlayer from '@/views/meeting/videoPlayer'
import videojs from "video.js";
import {FUN} from "@/utils/julyCommon";
import {isNotEmpty} from "@/utils/validate";

export default {
  name: "meeting",
  components:{
    // videoPlayer
  },
  props: {
    stompClient: {
      type: Object,
      required: true
    },
    connectionFlag: {
      type: Object,
      required: true
    },
    peerInfo: {
      type: Object,
      required: true
    },
    hostInfo: {
      peerType: Object,
      required: true
    },
    sessionId: {
      peerType: String,
      required: false,
      default: null,
    },
    inSignaling:{
      peerType: Boolean,
      required: false,
      default: false
    },
  },
  data() {
    return {
      localPlayer: '',
      remotePlayer: '',
      joinOrCreate: 'join',
      julyWebsocket:{
        constant: {
          stompClient: this.stompClient,
        },
        variable: {
          connectionFlag: this.connectionFlag!=null?this.connectionFlag:{'websocket':{'name':'websocket',"status":false}}
        },
      },
      mediaStreamConstraints: {
        video: { width: 480, height: 320 },
        audio: true,
      },
      offerOptions: {
        offerToReceiveAudio: true,
        offerToReceiveVideo: true
      },
      localStream:null,
      remoteStream:null,
      startTime:null,
      peerConnection: null,
      peerConnectionConfig:{
        iceServers: [{ urls: 'stun:stun.l.google.com:19302' }]
      },
      playerOptions: {
        // video.js options
        controls: true,
        bigPlayButton: false,
        autoplay: true,
        loop: false,
        fluid: false,
        width: 320,
        height: 240,
      },
    }
  },
  computed: {
    sendVideoCallURI() {
      return '/app/videoCall/' + this.peerInfo.userId
    },
  },
  watch: {
    inSignaling: {
      // Will fire as soon as the component is created
      immediate: true,
      handler(newVal, oldVal) {
        console.log('watch inSignaling changed')
        console.log('newVal:'+newVal)
        console.log('oldVal:'+oldVal)
        if(newVal){
          this.startSignaling()
        }else{
          FUN.notify("不能进行create PeerConnection, inSignaling:"+newVal+"  sessionId:"+this.sessionId,FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
        }
        // Fetch data about the movie
      },
      // deep: true
    },
  },
  created() {
    if(isNotEmpty(this.sessionId)){
      this.joinOrCreate='join'
    }else{
      this.joinOrCreate='create'
    }
    console.log("meeting create Im:"+this.joinOrCreate)
  },
  async mounted() {
    console.log("meeting mounted")
    await this.initMedia()
    console.log('mounted local stream.');
    console.log(this.localStream)
    // Get local media stream tracks.
    if(this.localStream!=null){
      const videoTracks = this.localStream.getVideoTracks();
      const audioTracks = this.localStream.getAudioTracks();
      if (videoTracks.length > 0) {
        console.log(`Using video device: ${videoTracks[0].label}.`);
      }
      if (audioTracks.length > 0) {
        console.log(`Using audio device: ${audioTracks[0].label}.`);
      }
    }
    // 建立peerConnection并根据当前是requester还是peer来发起请求/接受
    await this.createPeerConnection()
  },
  activated () {
    console.log("meeting activated")
  },
  beforeDestroy() {
    if (this.localPlayer) {
      this.localPlayer.dispose()
    }
    if (this.remotePlayer) {
      this.remotePlayer.dispose()
    }
    if(this.peerConnection){
      this.peerConnection.close();
    }
    this.peerConnection = null;
  },
  destroyed () {
    console.log("meeting destroyed")
    if(this.sessionId!=null){
      let init_connection_params = {
        'sessionId': this.sessionId,
        'isAccept': false,
      }
      this.julyWebsocket.constant.stompClient.send(this.sendVideoCallURI, {},JSON.stringify(init_connection_params))
    }
  },
  methods: {
    async initMedia(){
      console.log(`Browser ${adapter.browserDetails.browser} — version ${adapter.browserDetails.version}`)
      this.localPlayer = videojs(this.$refs.localVideo, this.playerOptions, function onPlayerReady() {
        console.log('onPlayerReady localPlayer', this);
      })
      this.remotePlayer = videojs(this.$refs.remoteVideo, this.playerOptions, function onPlayerReady() {
        console.log('onPlayerReady remotePlayer', this);
      })
      // init player listener
      this.localPlayer.on("loadedmetadata",function(event){
        console.log("localPlayer loadedmetadata ")
        const video = event.target;
        console.log(`${video.id} videoWidth: ${video.videoWidth}px, ` +
            `videoHeight: ${video.videoHeight}px.`);
      })
      this.remotePlayer.on("loadedmetadata",function(event){
        console.log("remotePlayer loadedmetadata ")
        const video = event.target;
        console.log(`${video.id} videoWidth: ${video.videoWidth}px, ` +
            `videoHeight: ${video.videoHeight}px.`);
      })
      this.remotePlayer.on("onresize",function(event){
        console.log("localPlayer onresize ")
        const video = event.target;
        console.log(`${video.id} videoWidth: ${video.videoWidth}px, ` +
            `videoHeight: ${video.videoHeight}px.`);
        if (this.startTime) {
          const elapsedTime = window.performance.now() - this.startTime;
          this.startTime = null;
          console.log(`Setup time: ${elapsedTime.toFixed(3)}ms.`);
        }
      })

      if (this.hasUserMedia()){
        await navigator.mediaDevices.getUserMedia(this.mediaStreamConstraints)
            .then((mediaStream) => {
              this.$refs.localVideo.srcObject = mediaStream;
              this.localStream = mediaStream;
              console.log('Received local stream.');
            }).catch((error) => {
          FUN.notify(error.toString(),FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
          console.log(`navigator.getUserMedia error: ${error.toString()}.`);
        });
        console.log('Requesting local stream.');
      }else{
        FUN.notify("无法获取到媒体设备",FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
      }
    },
    async createPeerConnection(){
      await this.localStream
      this.peerConnection = new RTCPeerConnection(this.peerConnectionConfig)
      // addLocalStream
      console.log(this.localStream)
      let _that =this
      this.peerConnection.addStream(this.localStream)
      console.log(this.peerConnection)
      this.peerConnection.addEventListener('icecandidate', (event)=>{
        // const peerConnection = event.target;
        const iceCandidate = event.candidate;
        console.log('handleConnection')
        // console.log(peerConnection)
        // console.log(iceCandidate)
        if (iceCandidate&&this.inSignaling) {
          let params = {
            'sessionId': _that.sessionId,
            'isAccept': true,
            'signalingMap':{
              'peerIcecandidate': iceCandidate
            }
          }
          if(_that.joinOrCreate === 'join'){
            params['signalingMap']['peerIcecandidate'] = iceCandidate
          }else{
            params['signalingMap']['requesterIcecandidate'] = iceCandidate
          }
          _that.julyWebsocket.constant.stompClient.send(_that.sendVideoCallURI, {},JSON.stringify(params))
          console.log(` ICE candidate:\n` +
              `${event.candidate.candidate}.`);
        }
      });
      this.peerConnection.addEventListener(
          'iceconnectionstatechange', (event) =>{
            const peerConnection = event.target
            console.log('ICE state change event: ', event)
            console.log(`ICE state: ` +`${peerConnection.iceConnectionState}.`)
          });
      this.peerConnection.addEventListener('addstream',(event) =>{
        if (event.stream) {
          _that.remoteStream = event.stream
          _that.$refs.remoteVideo.srcObject = _that.remoteStream
        }
      });
      this.peerConnection.addEventListener('removestream',(event) =>{
        console.log('Remote stream removed. Event: ', event)
      });
      // peerConnection建立完毕，并且listener建立完毕就可以根据当前是requester还是peer来发第一次连接请求
      this.initFirstRequest()
    },
    initFirstRequest(){
      if(this.joinOrCreate==='create'){
        // 如果初始Session为空，说明当前为requester会话发起方，需要发起会话
        if(this.peerInfo!=null){
          let params = {
            'isAccept': true,
          }
          this.julyWebsocket.constant.stompClient.send(this.sendVideoCallURI, {},JSON.stringify(params))
          FUN.notify("己方是会话发起方,发送会话",FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
        }
      }else if(this.joinOrCreate==='join'){
        // 如果初始Session不为空，说明当前为requester会话接受方，需要接受会话
        if(this.peerInfo!=null){
          let params = {
            'isAccept': true,
            'sessionId': this.sessionId
          }
          this.julyWebsocket.constant.stompClient.send(this.sendVideoCallURI, {},JSON.stringify(params))
          FUN.notify("己方是会话接受方,接受会话",FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
        }
      }
    },
    startSignaling(){
      if(this.joinOrCreate==='create'){
        this.createOffer()
      }
    },
    async createOffer(){
      if(this.joinOrCreate==='create'){
        await this.peerConnection
        this.peerConnection.createOffer((sessionDescription)=>{
          this.peerConnection.setLocalDescription(sessionDescription)
          // 发送自己的sessionDescription信息
          let params = {
            'sessionId': this.sessionId,
            'isAccept': true,
            'signalingMap':{
              'offer': sessionDescription
            }
          }
          this.julyWebsocket.constant.stompClient.send(this.sendVideoCallURI, {},JSON.stringify(params))
        },(event)=>{
          console.log('createOffer() error: ', event)
        })
      }
    },
    async handleOffer(offer){
      console.log('handleOffer')
      console.log(offer)
      if(this.joinOrCreate==='join') {
        await this.peerConnection
        console.log(this.peerConnection)
        // await this.peerConnection.setRemoteDescription(new RTCSessionDescription(offer))
        await this.peerConnection.setRemoteDescription(offer)
        console.log('handleOffer done')
        await this.peerConnection.createAnswer(answer => {
          this.peerConnection.setLocalDescription(answer);
          let params = {
            'sessionId': this.sessionId,
            'isAccept': true,
            'signalingMap': {
              'answer': answer
            }
          }
          console.log("generate answer and sent")
          console.log(params)
          this.julyWebsocket.constant.stompClient.send(this.sendVideoCallURI, {}, JSON.stringify(params))
        }, err => {
          console.err("Error creating an answer.", err);
        });
      }
    },
    async handleAnswer(answer){
      console.log('answer')
      console.log(answer)
      if(this.inSignaling&&this.sessionId!=null) {
        // await this.peerConnection.setRemoteDescription(new RTCSessionDescription(answer))
        await this.peerConnection.setRemoteDescription(answer)
        console.log('answer done')
      }
    },
    async handleIcecandidate(icecandidate){
      console.log('addIcecandidate')
      console.log(icecandidate)
      console.log(this.peerConnection)
      if(this.inSignaling&&this.sessionId!=null) {
        // new RTCIceCandidate(icecandidate)
        await this.peerConnection
        // await this.peerConnection.addIceCandidate(new RTCIceCandidate(icecandidate))
        await this.peerConnection.addIceCandidate(icecandidate)
        console.log('addIcecandidate done')
      }
    },

    hasUserMedia() {
      //check if the browser supports the WebRTC
      return !!(navigator.getUserMedia || navigator.webkitGetUserMedia ||
          navigator.mozGetUserMedia);
    },

  }
}
</script>

<style scoped>
.my-card {
  width: 100%;
  border-radius:10px;
  display: inline-block;
}

.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}
</style>
