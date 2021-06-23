<template>

    <video playsinline ref="videoPlayer" class="video-js  vjs-default-skin"></video>

</template>

<script>
import videojs from 'video.js';
export default {
  name: "VideoPlayer",
  props: {
    options: {
      type: Object,
      default() {
        return {};
      }
    },
    srcObject: {
      type: MediaStream,

    }
  },
  data() {
    return {
      player: null
    }
  },
  mounted() {
    this.player = videojs(this.$refs.videoPlayer, this.options, function onPlayerReady() {
      console.log('onPlayerReady', this);
    })
    this.player.srcObject = this.srcObject
  },
  beforeDestroy() {
    if (this.player) {
      this.player.dispose()
    }
  }
}
</script>

<style scoped>

</style>
