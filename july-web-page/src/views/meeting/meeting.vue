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
    }
  },
  data() {
    return {
      localPlayer: '',
      remotePlayer: '',
      julyWebsocket:{
        constant: {
          stompClient: this.stompClient,
        },
        variable: {
          connectionFlag: this.connectionFlag!=null?this.connectionFlag:{'websocket':{'name':'websocket',"status":false}}
        },
      },
      // startButtonDisable: false,
      // callButtonDisable: false,
      // hangupButtonDisable: false,
      srcObject: null,
      mediaStreamConstraints: {
        video: true,
        audio: true
      },
      offerOptions: {
        offerToReceiveVideo: 1,
      },
      localStream:null,
      startTime:null,
      localPeerConnection: null,
      remotePeerConnection: null,
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
      // videos
      localVideo: {},
      remoteVideo: {},
    }
  },
  computed: {
    sendVideoCallURI() {
      return '/app/videoCall/' + this.peerInfo.userId
    },
  },
  created() {
    console.log("meeting create")
  },
  mounted() {
    console.log("meeting mounted")
    this.initMedia()

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
    if(this.localPeerConnection){
      this.localPeerConnection.close();
    }
    if(this.remotePeerConnection){
      this.remotePeerConnection.close();
    }
    this.localPeerConnection = null;
    this.remotePeerConnection = null;
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
    initMedia(){
      console.log(adapter.browserDetails.browser);
      this.localPlayer = videojs(this.$refs.localVideo, this.playerOptions, function onPlayerReady() {
        console.log('onPlayerReady localPlayer', this);
      })
      this.remotePlayer = videojs(this.$refs.remoteVideo, this.playerOptions, function onPlayerReady() {
        console.log('onPlayerReady remotePlayer', this);
      })
      let _that =this
      this.localPlayer.on("loadedmetadata",function(event){
        console.log("localPlayer loadedmetadata ")
        _that.logVideoLoaded(event)
      })
      this.remotePlayer.on("loadedmetadata",function(event){
        console.log("remotePlayer loadedmetadata ")
        _that.logVideoLoaded(event)
      })
      this.remotePlayer.on("loadedmetadata",function(event){
        console.log("localPlayer loadedmetadata ")
        _that.logResizedVideo(event)
      })

      if (this.hasUserMedia()){
        navigator.mediaDevices.getUserMedia(this.mediaStreamConstraints)
            .then((mediaStream) => {
              this.$refs.localVideo.srcObject = mediaStream;
              this.localStream = mediaStream;
              console.log('Received local stream.');
              this.buildConnection()
            }).catch((error) => {
          FUN.notify(error.toString(),FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
          console.log(`navigator.getUserMedia error: ${error.toString()}.`);
        });
        console.log('Requesting local stream.');
      }else{
        FUN.notify("无法获取到媒体设备",FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
      }
    },
    buildConnection(){
      let init_connection_params ={}
      if(this.sessionId==null){
        // SessionId不存在，说明是己方是发起方
        init_connection_params = {
          'sessionId': null,
          'isAccept': true,
        }
      }else{
        // SessionId不存在，说明是己方是接受方
        // Todo:这里要附带自己的webrtc信息
        init_connection_params = {
          'sessionId': this.sessionId,
          'isAccept': true,
        }
      }
      this.julyWebsocket.constant.stompClient.send(this.sendVideoCallURI, {},JSON.stringify(init_connection_params))
    },

    // Handles call button action: creates peer connection.
    callAction() {
      this.callButtonDisable = true;
      this.hangupButtonDisable = false;

      console.log('Starting call.');
      this.startTime = window.performance.now();

      // Get local media stream tracks.
      const videoTracks = this.localStream.getVideoTracks();
      const audioTracks = this.localStream.getAudioTracks();
      if (videoTracks.length > 0) {
        console.log(`Using video device: ${videoTracks[0].label}.`);
      }
      if (audioTracks.length > 0) {
        console.log(`Using audio device: ${audioTracks[0].label}.`);
      }

      const servers = null;  // Allows for RTC server configuration.

      // Create peer connections and add behavior.
      this.localPeerConnection = new RTCPeerConnection(servers);
      console.log('Created local peer connection object localPeerConnection.');

      this.localPeerConnection.addEventListener('icecandidate', this.handleConnection);
      this.localPeerConnection.addEventListener(
          'iceconnectionstatechange', this.handleConnectionChange);

      this.remotePeerConnection = new RTCPeerConnection(servers);
      console.log('Created remote peer connection object remotePeerConnection.');

      this.remotePeerConnection.addEventListener('icecandidate', this.handleConnection);
      this.remotePeerConnection.addEventListener(
          'iceconnectionstatechange', this.handleConnectionChange);
      this. remotePeerConnection.addEventListener('addstream', this.gotRemoteMediaStream);

      // Add local stream to connection and create offer to connect.
      this.localPeerConnection.addStream(this.localStream);
      console.log('Added local stream to localPeerConnection.');

      console.log('localPeerConnection createOffer start.');
      this.localPeerConnection.createOffer(this.offerOptions)
          .then(this.createdOffer).catch(this.setSessionDescriptionError);
    },
    // Handles hangup action: ends up call, closes connections and resets peers.
    hangupAction() {
      this.localPeerConnection.close();
      this.remotePeerConnection.close();
      this.localPeerConnection = null;
      this.remotePeerConnection = null;
      this.hangupButtonDisable = true;
      this.callButtonDisable = false;
      console.log('Ending call.');
    },
    // Connects with new peer candidate.
    handleConnection(event) {
      const peerConnection = event.target;
      const iceCandidate = event.candidate;

      if (iceCandidate) {
        const newIceCandidate = new RTCIceCandidate(iceCandidate);
        const otherPeer = this.getOtherPeer(peerConnection);

        otherPeer.addIceCandidate(newIceCandidate)
            .then(() => {
              this.handleConnectionSuccess(peerConnection);
            }).catch((error) => {
          this.handleConnectionFailure(peerConnection, error);
        });

        console.log(`${this.getPeerName(peerConnection)} ICE candidate:\n` +
            `${event.candidate.candidate}.`);
      }
    },
    // Logs changes to the connection state.
    handleConnectionChange(event) {
      const peerConnection = event.target;
      console.log('ICE state change event: ', event);
      console.log(`${this.getPeerName(peerConnection)} ICE state: ` +
          `${peerConnection.iceConnectionState}.`);
    },
    // Gets the "other" peer connection.
    getOtherPeer(peerConnection) {
      return (peerConnection === this.localPeerConnection) ?
          this.remotePeerConnection : this.localPeerConnection;
    },
    // Logs that the connection succeeded.
    handleConnectionSuccess(peerConnection) {
      console.log(`${this.getPeerName(peerConnection)} addIceCandidate success.`);
    },
    // Logs that the connection failed.
    handleConnectionFailure(peerConnection, error) {
      console.log(`${this.getPeerName(peerConnection)} failed to add ICE Candidate:\n`+
          `${error.toString()}.`);
    },

    // Gets the name of a certain peer connection.
    getPeerName(peerConnection) {
      return (peerConnection === this.localPeerConnection) ?
          'localPeerConnection' : 'remotePeerConnection';
    },
    // Handles remote MediaStream success by adding it as the remoteVideo src.
    gotRemoteMediaStream(event) {
      const mediaStream = event.stream;
      this.$refs.remoteVideo.srcObject = mediaStream;
      this.remoteStream = mediaStream;
      console.log('Remote peer connection received remote stream.');
    },
    // Logs offer creation and sets peer connection session descriptions.
    createdOffer(description) {
      console.log(`Offer from localPeerConnection:\n${description.sdp}`);

      console.log('localPeerConnection setLocalDescription start.');
      this.localPeerConnection.setLocalDescription(description)
          .then(() => {
            this.setLocalDescriptionSuccess(this.localPeerConnection);
          }).catch(this.setSessionDescriptionError);

      console.log('remotePeerConnection setRemoteDescription start.');
      this.remotePeerConnection.setRemoteDescription(description)
          .then(() => {
            this.setRemoteDescriptionSuccess(this.remotePeerConnection);
          }).catch(this.setSessionDescriptionError);

      console.log('remotePeerConnection createAnswer start.');
      this.remotePeerConnection.createAnswer()
          .then(this.createdAnswer)
          .catch(this.setSessionDescriptionError);
    },
    // Logs answer to offer creation and sets peer connection session descriptions.
    createdAnswer(description) {
      console.log(`Answer from remotePeerConnection:\n${description.sdp}.`);

      console.log('remotePeerConnection setLocalDescription start.');
      this.remotePeerConnection.setLocalDescription(description)
          .then(() => {
            this.setLocalDescriptionSuccess(this.remotePeerConnection);
          }).catch(this.setSessionDescriptionError);

      console.log('localPeerConnection setRemoteDescription start.');
      this.localPeerConnection.setRemoteDescription(description)
          .then(() => {
            this.setRemoteDescriptionSuccess(this.localPeerConnection);
          }).catch(this.setSessionDescriptionError);
    },
    // Logs success when localDescription is set.
    setLocalDescriptionSuccess(peerConnection) {
      this.setDescriptionSuccess(peerConnection, 'setLocalDescription');
    },
    // Logs success when remoteDescription is set.
    setRemoteDescriptionSuccess(peerConnection) {
      this.setDescriptionSuccess(peerConnection, 'setRemoteDescription');
    },
    // Logs success when setting session description.
    setDescriptionSuccess(peerConnection, functionName) {
      const peerName =  this.getPeerName(peerConnection);
      console.log(`${peerName} ${functionName} complete.`);
    },
    // Logs error when setting session description fails.
    setSessionDescriptionError(error) {
      console.log(`Failed to create session description: ${error.toString()}.`);
    },
    // Add behavior for video streams.

    // Logs a message with the id and size of a video element.
    logVideoLoaded(event) {
      const video = event.target;
      console.log(`${video.id} videoWidth: ${video.videoWidth}px, ` +
          `videoHeight: ${video.videoHeight}px.`);
    },
    // Logs a message with the id and size of a video element.
    // This event is fired when video begins streaming.
    logResizedVideo(event) {
      this.logVideoLoaded(event);

      if (this.startTime) {
        const elapsedTime = window.performance.now() - this.startTime;
        this.startTime = null;
        console.log(`Setup time: ${elapsedTime.toFixed(3)}ms.`);
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
