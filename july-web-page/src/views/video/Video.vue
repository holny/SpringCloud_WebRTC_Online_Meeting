<template>
  <div class="row  q-col-gutter-xl">
    <div class="">
      <div class="text-h5 text-weight-bold text-center q-mb-md">尚硅谷SpringCloud(H版&alibaba)框架开发教程(大牛讲授spring cloud)</div>
      <div class="text-subtitle2 text-grey-7 text-center q-mb-md">27K播放量 &nbsp;  • &nbsp;  2012-02-05 12:05:22</div>
      <div class="row justify-center">
        <VideoPlayer :options="playerOptions" :auto-size="true"></VideoPlayer>
      </div>
      <div class="row no-wrap  justify-between">
        <div class="no-wrap items-center q-mt-lg ">
          <q-rating size="md" v-model="stars" :max="5" color="primary"  icon-half="star_half"  >
            <template v-slot:tip-1>
              <q-tooltip>非常差</q-tooltip>
            </template>
            <template v-slot:tip-2>
              <q-tooltip>比较差</q-tooltip>
            </template>
            <template v-slot:tip-3>
              <q-tooltip>一般般</q-tooltip>
            </template>
            <template v-slot:tip-4>
              <q-tooltip>还不错</q-tooltip>
            </template>
            <template v-slot:tip-5>
              <q-tooltip>很棒!</q-tooltip>
            </template>
          </q-rating>
        </div>
        <div class="no-wrap items-center q-mt-lg q-ml-md">
          <q-icon style="cursor: pointer;" size="sm" class="text-weight-thin q-ml-lg" color="grey-6" name="thumb_up" >
          </q-icon>
          <q-icon style="cursor: pointer;" size="sm" class="text-weight-thin q-ml-sm" color="grey-6" name="thumb_down" >
          </q-icon>

          <q-icon style="cursor: pointer;" size="md" class="text-weight-thin q-ml-lg" color="grey-6" name="star_border" >
            <q-tooltip>收藏</q-tooltip>
          </q-icon>
        </div>
      </div>
      <q-separator class="q-my-md" />
      <div class="text-overline  q-mt-md">作者说明     <q-btn
          color="grey"
          round
          flat
          dense
          :icon="expanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
          @click="expanded = !expanded"
      /></div>
      <q-slide-transition>
        <div v-show="expanded">
          <div class="row justify-start text-body1">
            fawfawfawfawfawfwafawf fawfawfawfawfawfwafawf fawfawfawfawfawfwafawf fawfawfawfawfawfwafawf fawfawfawfawfawfwafawf fawfawfawfawfawfwafawf fawfawfawfawfawfwafawf fawfawfawfawfawfwafawf fawfawfawfawfawfwafawf fawfawfawfawfawfwafawf
          </div>

        </div>
      </q-slide-transition>

      <div class="text-overline  q-mt-md">视频标签</div>
      <div class="row justify-start q-gutter-x-md">
        <q-badge color="primary" label="Outline" />
        <q-badge color="orange" label="Outline" />
        <q-badge color="secondary" label="Outline" />
      </div>
      <q-separator class="q-my-md" />
      <div class="row  q-mt-md justify-between q-gutter-x-md full-width">
          <div class="col-1">
            <q-avatar size="50px">
              <img src="https://cdn.quasar.dev/img/avatar2.jpg">
            </q-avatar>
          </div>
          <div class="col-8">
            <q-input
                v-model="text"
                filled
                autogrow
                label="在这里输入评论"
                class="full-width"
            />
          </div>
          <div class="col-2 self-center">
            <q-btn color="primary" size="md" icon-right="send" label="发送" />
          </div>
      </div>

<!--      {{this.$route.params.vid}}奥术大师-->
    </div>
    <div class="">
      <user-info-side-bar class="float-right"></user-info-side-bar>
    </div>
  </div>
</template>

<script>
import VideoPlayer from "@/views/video/VideoPlayer"
import UserInfoSideBar from "@/views/common/UserInfoSideBar";
export default {
  name: "Video",
  components:{
    VideoPlayer,
    UserInfoSideBar
  },
  // props: {
  //   vid: {
  //     type: String,
  //     required: true
  //   }
  // },
  data() {
    return {
      stars: 4.5,
      expanded: false,
      text:'',
      playerOptions: {
        // video.js options
        controls: true,
        bigPlayButton: true,
        autoplay: false,
        loop: false,
        fluid: false,
        poster: require('@/assets/videos/anime-watch.jpg'),

        sources: [
          {
            src: require('@/assets/videos/1.mp4'),
            type: "video/mp4"
          }
        ]
      },
    }
  },
}
</script>

<style scoped>

</style>
