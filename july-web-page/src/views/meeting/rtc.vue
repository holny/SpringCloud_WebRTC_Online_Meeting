<template>
  <div class="record-page">
    <div style="margin-bottom: 15px;" class="q-gutter-lg">
      <q-btn  :dense="true" ref="startButton" color="primary" label="Start" @click="startAction" :disabled="startButtonDisable"/>
      <q-btn  :dense="true" ref="callButton" color="primary" label="Call" @click="callAction" :disabled="callButtonDisable"/>
      <q-btn  :dense="true" ref="hangupButton" color="primary" label="Hangup" @click="hangupAction" :disabled="hangupButtonDisable"/>
    </div>

<!--    <video-player :options="playerOptions" :src-object="srcObject" />-->
    <video playsinline ref="localVideo"  class="video-js  vjs-default-skin"></video>
    <video playsinline ref="remoteVideo"  class="video-js  vjs-default-skin"></video>
  </div>
</template>

<script>
import adapter from 'webrtc-adapter'
// import videoPlayer from '@/views/meeting/videoPlayer'
import videojs from "video.js";

export default {
  name: "meeting",
  components:{
    // videoPlayer
  },
  data() {
    return {
      localPlayer: '',
      remotePlayer: '',
      startButtonDisable: false,
      callButtonDisable: false,
      hangupButtonDisable: false,
      startButtonTextDisable: false,
      callButtonTextDisable: false,
      hangupButtonTextDisable: false,
      srcObject: null,
      mediaStreamConstraints: {
        video: true,
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
        // bigPlayButton: false,
        autoplay: true,
        loop: false,
        fluid: false,
        width: 320,
        height: 240,
        // sources: [
        //   {
        //     src: "",
        //     type: "rtmp/flv"
        //   }
        // ]
        // plugins: {
        //   // videojs-record plugin options
        //   record: {
        //     image: false,
        //     audio: false,
        //     video: true,
        //     maxLength: 5,
        //     displayMilliseconds: true,
        //     debug: true
        //   }
        // }
      },
      // videos
      localVideo: {},
      remoteVideo: {},
    }
  },

  created() {
    // this.localVideo = document.getElementById("localVideo");
    // this.remoteVideo = document.getElementById("remoteVideo");
  },
  mounted() {
    this.callButtonDisable=true;
    this.hangupButtonDisable=true;
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
    // this.initMedia()
  },
  methods: {
    hasUserMedia() {
      //check if the browser supports the WebRTC
      return !!(navigator.getUserMedia || navigator.webkitGetUserMedia ||
          navigator.mozGetUserMedia);
    },
    initMedia(){
      let _that = this
      if (this.hasUserMedia()) {
        navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia
            || navigator.mozGetUserMedia;

        //enabling video and audio channels
        navigator.getUserMedia({ video: true, audio: true }, function (stream) {
          //inserting our stream to the video tag
          // _that.playerOptions.sources[0].src = stream;
          console.log(stream.getAudioTracks())
          console.log(stream.getVideoTracks())
          _that.$refs.localVideo.srcObject = stream
          // document.getElementById('localVideo').getElementsByTagName('video')[0].srcObject = stream
          // _that.player.src(stream)
          // _that.player.onloadedmetadata = function (e) {
          //   _that.player.play();
          //   console.log(e)
          // };
        }, function (err) {
          console.log(err)
        });
      } else {
        alert("WebRTC is not supported");
      }
    },
    // Handles start button action: creates local MediaStream.
    startAction() {
      this.startButtonDisable = true;
      navigator.mediaDevices.getUserMedia(this.mediaStreamConstraints)
          .then(this.gotLocalMediaStream).catch(this.handleLocalMediaStreamError);
      console.log('Requesting local stream.');
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
    // Handles error by logging a message to the console.
    handleLocalMediaStreamError(error) {
      console.log(`navigator.getUserMedia error: ${error.toString()}.`);
    },
    // Gets the name of a certain peer connection.
    getPeerName(peerConnection) {
      return (peerConnection === this.localPeerConnection) ?
          'localPeerConnection' : 'remotePeerConnection';
    },
    // Sets the MediaStream as the video element src.
    gotLocalMediaStream(mediaStream) {
      this.$refs.localVideo.srcObject = mediaStream;
      this.localStream = mediaStream;
      console.log('Received local stream.');
      this.callButtonDisable = false;  // Enable call button.
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
    }

  }
}
</script>

<style scoped>

</style>
