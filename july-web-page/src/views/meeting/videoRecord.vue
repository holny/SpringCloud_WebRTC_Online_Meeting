<template>
  <div class="record-page">
    <div style="margin-bottom: 15px;">
    </div>

    <video id="myVideo" class="video-js vjs-default-skin" playsinline></video>
  </div>
</template>

<script>
/* eslint-disable */
import 'video.js/dist/video-js.css'
import 'videojs-record/dist/css/videojs.record.css'
import videojs from 'video.js'

import 'webrtc-adapter'
import RecordRTC from 'recordrtc'
import Record from 'videojs-record/dist/videojs.record.js'
import videoPlayer from '@/views/meeting/videoPlayer'
export default {
  name: "meeting",
  components:{
    videoPlayer
  },
  data() {
    return {
      player: '',
      playerOptions: {
        // video.js options
        controls: true,
        bigPlayButton: false,
        autoplay: false,
        loop: false,
        fluid: false,
        width: 320,
        height: 240,
        controlBar: {
          volumePanel: false
        },
        // sources: [
        //   {
        //     src: "//vjs.zencdn.net/v/oceans.mp4",
        //     type: "video/mp4"
        //   }
        // ]
        plugins: {
          // videojs-record plugin options
          record: {
            image: false,
            audio: false,
            video: true,
            displayMilliseconds: true,
            debug: true,
            videoMimeType: "video/webm;codecs=H264"
          }
        }
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
    this.initMedia()
  },
  methods: {
    hasUserMedia() {
      //check if the browser supports the WebRTC
      return !!(navigator.getUserMedia || navigator.webkitGetUserMedia ||
          navigator.mozGetUserMedia);
    },
    initMedia(){
      /* eslint-disable no-console */
      this.player = videojs('#myVideo', this.playerOptions, () => {
        // print version information at startup
        let msg = 'Using video.js ' + videojs.VERSION +
            ' with videojs-record ' + videojs.getPluginVersion('record') +
            ' and recordrtc ' + RecordRTC.version;
        videojs.log(msg);
      });

      // device is ready
      this.player.on('deviceReady', () => {
        console.log('device is ready!');
      });

      // user clicked the record button and started recording
      this.player.on('startRecord', () => {
        console.log('started recording!');
      });

      // user completed recording and stream is available
      this.player.on('finishRecord', () => {
        // the blob object contains the recorded data that
        // can be downloaded by the user, stored on server etc.
        console.log('finished recording: ', this.player.recordedData);
      });

      // error handling
      this.player.on('error', (element, error) => {
        console.warn(error);
      });

      this.player.on('deviceError', () => {
        console.error('device error:', this.player.deviceErrorCode);
      });
      // let _that = this
      // if (this.hasUserMedia()) {
      //   navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia
      //       || navigator.mozGetUserMedia;
      //
      //   //enabling video and audio channels
      //   navigator.getUserMedia({ video: true, audio: true }, function (stream) {
      //
      //     //inserting our stream to the video tag
      //     _that.playerOptions.sources[0].src = window.URL.createObjectURL(stream);
      //   }, function (err) {
      //     console.log(err)
      //   });
      // } else {
      //   alert("WebRTC is not supported");
      // }
    },
    beforeDestroy() {
      if (this.player) {
        this.player.dispose();
      }
    }

  }
}
</script>

<style scoped>

</style>
