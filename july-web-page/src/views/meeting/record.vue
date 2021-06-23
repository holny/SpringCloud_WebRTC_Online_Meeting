<template>
  <div class="record-page">
    <div style="margin-bottom: 15px;">
      <q-btn  @click="startRecording" :disabled="videoStart" >start recording</q-btn >
      <q-btn  @click="stopRecording" :disabled="!videoStart"  id="btn-stop-recording">stop recording</q-btn>
    </div>
    <video-player :options="videoOptions"/>
  </div>
</template>

<script>
import RecordRTC from 'recordrtc';
import videoPlayer from '@/views/meeting/videoPlayer';
export default {
  name: "meeting",
  components: {
    videoPlayer
  },
  data() {
    return {
      video: null,
      videoStart: false,
      recorder: null,
      videoOptions: {
        controls: true,
        bigPlayButton: false,
        width: 320,
        height: 240,
        fluid: false,
        autoplay: true
      }
    }
  },
  created() {
    if (!navigator.getDisplayMedia && !navigator.mediaDevices.getDisplayMedia) {
      let error = 'Your browser does NOT support the getDisplayMedia API.';
      throw new Error(error);
    }

    function hasUserMedia() {
      //check if the browser supports the WebRTC
      return !!(navigator.getUserMedia || navigator.webkitGetUserMedia ||
          navigator.mozGetUserMedia);
    }

    if (hasUserMedia()) {
      navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia
          || navigator.mozGetUserMedia;

      //enabling video and audio channels
      navigator.getUserMedia({ video: true, audio: true }, function (stream) {
        var video = document.querySelector("video");

        //inserting our stream to the video tag
        video.src = window.URL.createObjectURL(stream);
      }, function (err) {});
    } else {
      alert("WebRTC is not supported");
    }
  },
  mounted() {
    this.video = document.querySelector('video');
  },
  methods: {
    invokeGetDisplayMedia(success, error) {
      let displaymediastreamconstraints = {
        video: {
          displaySurface: 'monitor', // monitor, window, application, browser
          logicalSurface: true,
          cursor: 'always' // never, always, motion
        }
      };
      // above constraints are NOT supported YET
      // that's why overridnig them
      displaymediastreamconstraints = {
        video: true
      };
      if (navigator.mediaDevices.getDisplayMedia) {
        navigator.mediaDevices.getDisplayMedia(displaymediastreamconstraints).then(success).catch(error);
      }
      else {
        navigator.getDisplayMedia(displaymediastreamconstraints).then(success).catch(error);
      }
    },
    captureScreen(callback) {
      this.invokeGetDisplayMedia((screen) => {
        this.addStreamStopListener(screen, () => {
          //
        });
        callback(screen);
      }, function (error) {
        console.error(error);
        alert('Unable to capture your screen. Please check console logs.\n' + error);
      });
    },
    addStreamStopListener(stream, callback) {
      stream.addEventListener('ended', function () {
        callback();
        callback = function () { };
      }, false);
      stream.addEventListener('inactive', function () {
        callback();
        callback = function () { };
      }, false);
      stream.getTracks().forEach(function (track) {
        track.addEventListener('ended', function () {
          callback();
          callback = function () { };
        }, false);
        track.addEventListener('inactive', function () {
          callback();
          callback = function () { };
        }, false);
      });
    },
    startRecording() {
      this.captureScreen(screen=>{
        this.video.srcObject = screen;
        this.recorder = RecordRTC(screen, {
          type: 'video'
        });
        this.recorder.startRecording();
        // release screen on stopRecording
        this.recorder.screen = screen;
        this.videoStart = true;
      });
    },
    stopRecordingCallback() {
      this.video.src = this.video.srcObject = null;
      this.video.src = URL.createObjectURL(this.recorder.getBlob());

      this.recorder.screen.stop();
      this.recorder.destroy();
      this.recorder = null;
      this.videoStart = false;
    },
    stopRecording() {
      this.recorder.stopRecording(this.stopRecordingCallback);
    }
  }
}
</script>

<style scoped>

</style>
