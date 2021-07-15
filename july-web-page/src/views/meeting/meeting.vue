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
        <q-item-section>
          <q-btn :disable="localRecordStatus!='ready'&&localRecordStatus!='recording'&&localRecordStatus!='done'"
                 v-if="localRecordStatus!=null" :icon="localRecordStatus==='ready'?'fiber_manual_record':(localRecordStatus==='recording'?'fiber_manual_record':(localRecordStatus==='done'?'file_download':'info'))"
                 :color="localRecordStatus==='ready'?'positive':(localRecordStatus==='recording'?'accent':(localRecordStatus==='done'?'info':'negative'))"
                 @click="localRecordStatusChange"
                 :class="localRecordStatus==='ready'?'float-right btn-small':(localRecordStatus==='recording'?'float-right btn-small animate__animated animate__flash animate__infinite':'float-right btn-small')" flat round >
            <q-tooltip>{{localRecordStatus==='ready'?'开始录像':(localRecordStatus==='recording'?'正在录像中，点击停止录像,已录制:'+localPlayer.record().getCurrentTime()+'秒':(localRecordStatus==='done'?'请先保存录像文件':'视频流停止,无法录像'))}}</q-tooltip>
          </q-btn>
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
        <q-item-section>
          <q-btn :disable="remoteRecordStatus!='ready'&&remoteRecordStatus!='recording'&&remoteRecordStatus!='done'"
                 v-if="remoteRecordStatus!=null" :icon="remoteRecordStatus==='ready'?'fiber_manual_record':(remoteRecordStatus==='recording'?'fiber_manual_record':(remoteRecordStatus==='done'?'file_download':'info'))"
                 :color="remoteRecordStatus==='ready'?'positive':(remoteRecordStatus==='recording'?'accent':(remoteRecordStatus==='done'?'info':'negative'))"
                 @click="remoteRecordStatusChange"
                 :class="remoteRecordStatus==='ready'?'float-right':(remoteRecordStatus==='recording'?'float-right animate__animated animate__flash animate__infinite':'float-right ')" flat>
            <q-tooltip>{{remoteRecordStatus==='ready'?'开始录像':(remoteRecordStatus==='recording'?'正在录像中，点击停止录像,已录制:'+remotePlayer.record().getCurrentTime()+'秒':(remoteRecordStatus==='done'?'请先保存录像文件':'视频流停止,无法录像'))}}</q-tooltip>
          </q-btn>
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

/* eslint-disable */
import 'video.js/dist/video-js.css'
import 'videojs-record/dist/css/videojs.record.css'
import '@ffmpeg/ffmpeg/dist/ffmpeg.min.js'
import RecordRTC from 'recordrtc'
import Record from 'videojs-record/dist/videojs.record.js'

import WebmWasmEngine from 'videojs-record/dist/plugins/videojs.record.webm-wasm.js';
import TsEBMLEngine from 'videojs-record/dist/plugins/videojs.record.ts-ebml.js';
import {FUN} from "@/utils/julyCommon";
import {isNotEmpty} from "@/utils/validate";
import {getBrowserType} from "@/utils/adapter";

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
      localRecordStatus: null,
      remoteRecordStatus: null,
      startTime:null,
      peerConnection: null,
      peerConnectionConfig:{
        iceServers: [{ urls: 'stun:stun.l.google.com:19302' }]
      },
      playerOptions: {
        // video.js options
        controls: true,
        bigPlayButton: false,
        controlBar: {
          // hide fullscreen and volume controls
          deviceButton: false,
          recordToggle: false,
        },
        autoplay: true,
        loop: false,
        fluid: false,
        width: 320,
        height: 240,
        plugins: {
          // videojs-record plugin options
          record: {
            image: false,
            audio: true,
            video: true,
            displayMilliseconds: true,
            debug: true,
            maxLength: 60*60,
            audioMimeType: 'audio/wav',
            videoMimeType: "video/webm;codecs=vp8,opus",
            // enable ts-ebml plugin
            convertEngine: 'ts-ebml'
            // enable ffmpeg.wasm plugin
            // convertEngine: 'ffmpeg.wasm',
            // convertWorkerURL:'../../node_modules/@ffmpeg/core/dist/ffmpeg-core.js',
            // // convert recorded data to MP4 (and copy over audio data without encoding)
            // convertOptions: ['-c:v', 'libx264', '-preset', 'slow', '-crf', '22', '-c:a', 'copy', '-f', 'mp4'],
            // // specify output mime-type
            // pluginLibraryOptions: {
            //   outputType: 'video/mp4'
            // }
          }
        }
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
      immediate: false,
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
    localRecordStatus: {
      // Will fire as soon as the component is created
      immediate: true,
      handler(newVal, oldVal) {
        console.log('watch localRecordStatus changed')
        console.log('newVal:'+newVal)
        console.log('oldVal:'+oldVal)
        // if(newVal==='ready'&&oldVal!='done'){
        //   this.localPlayer.record().getDevice()
        // }
        // Fetch data about the movie
      },
      // deep: true
    },
    remoteRecordStatus: {
      // Will fire as soon as the component is created
      immediate: true,
      handler(newVal, oldVal) {
        console.log('watch remoteRecordStatus changed')
        console.log('newVal:'+newVal)
        console.log('oldVal:'+oldVal)
        // if(newVal==='ready'&&oldVal!='done'){
        //   this.remotePlayer.record().getDevice()
        // }
        // Fetch data about the movie
      },
      // deep: true
    },
  },
  created() {
    // let ffmpegPath = require('@/utils/ffmpeg-core')
    // console.log(ffmpegPath)
    let browserType = getBrowserType()
    console.log("Browser type is:"+browserType)
    // if (browserType==='Chrome'){
    //   this.playerOptions.plugins.record.videoMimeType="video/webm;codecs=H264"
    // }else{
    //   this.playerOptions.plugins.record.videoMimeType="video/webm"
    // }

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
    console.log("meeting destroy")
    if (this.localPlayer) {
      this.localPlayer.dispose()
    }
    if (this.remotePlayer) {
      this.remotePlayer.dispose()
    }
    if (this.localStream) {
      this.localStream.stop()
      this.localRecordStatus = 'stop'
    }
    if (this.remoteStream) {
      this.remoteStream.stop()
      this.remoteRecordStatus = 'stop'
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
      this.localPlayer = videojs(this.$refs.localVideo, this.playerOptions, () => {
        // print version information at startup
        let msg = 'localPlayer using video.js ' + videojs.VERSION +
            ' with videojs-record ' + videojs.getPluginVersion('record') +
            ' and recordrtc ' + RecordRTC.version;
        videojs.log(msg);
      })
      this.remotePlayer = videojs(this.$refs.remoteVideo, this.playerOptions, () => {
        // print version information at startup
        let msg = ' remotePlayer using video.js ' + videojs.VERSION +
            ' with videojs-record ' + videojs.getPluginVersion('record') +
            ' and recordrtc ' + RecordRTC.version;
        videojs.log(msg);
      })
      // device is ready
      this.localPlayer.on('deviceReady', () => {
        console.log('device is ready!');
        this.localPlayer.record().start();
      });

      // user clicked the record button and started recording
      this.localPlayer.on('startRecord', () => {
        console.log('started recording!');
      });
      // user completed recording and stream is available
      this.localPlayer.on('finishRecord', () => {
        // the blob object contains the recorded data that
        // can be downloaded by the user, stored on server etc.
        console.log('localPlayer finished recording: ', this.localPlayer.recordedData);
        let now = Date.now()
        // this.localPlayer.record().saveAs({'video': 'local-video-file_'+now+'.webm'});
      });
      // converter completed and stream is available
      this.localPlayer.on('finishConvert', () => {
        console.log('localPlayer finished converting: ', this.localPlayer.convertedData)
      })
      // error handling
      this.localPlayer.on('error', (element, error) => {
        console.warn(error);
      });

      this.localPlayer.on('deviceError', () => {
        console.error('device error:', this.localPlayer.deviceErrorCode);
      });
      // device is ready
      this.remotePlayer.on('deviceReady', () => {
        console.log('device is ready!');
        this.remotePlayer.record().start();
      });

      // user clicked the record button and started recording
      this.remotePlayer.on('startRecord', () => {
        console.log('started recording!');
      });
      // user completed recording and stream is available
      this.remotePlayer.on('finishRecord', () => {
        // the blob object contains the recorded data that
        // can be downloaded by the user, stored on server etc.
        console.log('remotePlayer finished recording: ', this.remotePlayer.recordedData);
      });
      // converter completed and stream is available
      this.remotePlayer.on('finishConvert', () => {
        console.log('remotePlayer finished converting: ', this.remotePlayer.convertedData)
      })
      // error handling
      this.remotePlayer.on('error', (element, error) => {
        console.warn(error);
      });

      this.remotePlayer.on('deviceError', () => {
        console.error('device error:', this.remotePlayer.deviceErrorCode);
      });


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
              this.localRecordStatus = 'ready'
              console.log('Received local stream.');
            }).catch((error) => {
          FUN.notify("无法获取到媒体设备,请确认摄像头是否开启",FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
              console.log(error)
          console.log(`navigator.getUserMedia error: ${error.toString()}.`);
        });
        console.log('Requesting local stream.');
      }else{
        FUN.notify("不存在媒体设备",FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
      }
    },
    async createPeerConnection(){
      await this.localStream
      this.peerConnection = new RTCPeerConnection(this.peerConnectionConfig)
      // addLocalStream
      console.log(this.localStream)
      let _that =this
      if(this.localStream){
        this.peerConnection.addStream(this.localStream)
      }else{
        FUN.notify("本地视频流无效,无法给RTC加入本地流",FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
      }
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
          _that.remoteRecordStatus = 'ready'
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
    async localRecordStatusChange(){
      if(this.localRecordStatus==='ready'){
        this.localRecordStatus='recording'
        if(this.localPlayer.record().isRecording()){
          console.log("local player is already recording")
          return
        }else{
          this.localPlayer.record().getDevice()
          // this.localPlayer.record().start()
          return
        }
      }else if(this.localRecordStatus==='recording'){
        this.localRecordStatus='done'
        if(!this.localPlayer.record().isRecording()){
          console.log("local player is already stop recording")
          return
        }else{
          this.localPlayer.record().stopDevice()
          return
        }
      }else if(this.localRecordStatus==='done'){
        if(this.localPlayer.record().getDuration()>0){
          let now = Date.now()
          this.localPlayer.record().saveAs({'video': 'local-video-file_'+now+'.webm'},'convert');
          // this.localPlayer.record().saveAs({'video': 'local-video-file_'+now+'.mp4'}, 'convert');
        }else{
          console.log("local player has no recording ")
        }
        this.localRecordStatus='ready'
      }
    },
    async remoteRecordStatusChange(){
      if(this.remoteRecordStatus==='ready'){
        this.remoteRecordStatus='recording'
        if(this.remotePlayer.record().isRecording()){
          console.log("remote player is already recording")
          return
        }else{
          this.remotePlayer.record().getDevice()
          // this.remotePlayer.record().start()
          return
        }
      }else if(this.remoteRecordStatus==='recording'){
        this.remoteRecordStatus='done'
        if(!this.remotePlayer.record().isRecording()){
          console.log("remote player is already stop recording")
          return
        }else{
          this.remotePlayer.record().stopDevice()
          return
        }
      }else if(this.remoteRecordStatus==='done'){
        if(this.remotePlayer.record().getDuration()>0){
          let now = Date.now()
          this.remotePlayer.record().saveAs({'video': 'remote-video-file_'+now+'.webm'} ,'convert');
          // this.remotePlayer.record().saveAs({'video': 'remote-video-file_'+now+'.mp4'}, 'convert');
        }else{
          console.log("remote player has no recording ")
        }
        this.remoteRecordStatus='ready'
        return;
      }
    }
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
