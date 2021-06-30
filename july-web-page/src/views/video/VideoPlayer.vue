<template>
    <video playsinline ref="videoPlayer" :class="{'video-js':1===1,'vjs-big-play-centered':1===1,'vjs-default-skin':1===1,'JULY__video-player':autoSize===true}"></video>

</template>

<script>
import videojs from 'video.js';
import '@videojs/themes/dist/city/index.css';
export default {
  name: "VideoPlayer",
  props: {
    options: {
      type: Object,
      default() {
        return {};
      }
    },
    autoSize:{
      type: Boolean,
      default: true,
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

<style scoped lang="sass">
.JULY
  @media (min-width: 0px)
    &__video-player
      width: 445px
      height: 250px
  @media (min-width: 600px)
    &__video-player
      width: 640px
      height: 360px
  @media (min-width: 1024px)
    &__video-player
      width: 800px
      height: 450px
  @media (min-width: 1440px)
    &__video-player
      width: 1024px
      height: 575px
  @media (min-width: 1920px)
    &__video-player
      width: 1280px
      height: 720px
</style>
