<template>
  <div class="row  q-col-gutter-xl">
    <div class="">
      <div class="text-h5 text-weight-bold text-center q-mb-md">{{videoInfo.title}}</div>
      <div class="text-subtitle2 text-grey-7 text-center q-mb-md">{{videoInfo.videoView!=null?(videoInfo.videoView>10000?videoInfo.videoView%10000+'K':videoInfo.videoView):0}}播放量 &nbsp;  • &nbsp;  {{videoInfo.gmtUpdate!=null?videoInfo.gmtUpdate:'XXXX-XX-XX XX:XX:XX'}}</div>
      <div class="row justify-center">
        <VideoPlayer v-if="videoSrcReady" :options="playerOptions" :auto-size="true"></VideoPlayer>
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
                  {{videoInfo.info!=null?videoInfo.info:'作者没有什么想说的.'}}
          </div>

        </div>
      </q-slide-transition>

      <div class="text-overline  q-mt-md">视频标签</div>
      <div class="row justify-start q-gutter-x-md">
        <q-badge :v-if="videoInfo.tagList!=null" v-for="(item,index) in videoInfo.tagList" :key="item.tagId" :color="getColor(index)" :label="item.tagName" />
      </div>
      <q-separator class="q-my-md" />
      <div class="row  q-mt-md justify-between q-gutter-x-md full-width">
          <div class="col-1">
            <q-avatar size="50px">
              <img :src="videoInfo.authorAvatar!=null?videoInfo.authorAvatar:'https://cdn.quasar.dev/img/avatar2.jpg'">
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
      <user-info-side-bar class="float-right" v-if="videoInfo.authorId!=null" :user-id="videoInfo.authorId"></user-info-side-bar>
    </div>
  </div>
</template>

<script>
import VideoPlayer from "@/views/video/VideoPlayer"
import UserInfoSideBar from "@/views/common/UserInfoSideBar";
import {FUN} from "@/utils/julyCommon";
import {isNotEmpty} from "@/utils/validate";
import {randomColor} from "@/utils/random";
import {getToken} from "@/utils/auth";
export default {
  name: "Video",
  components:{
    VideoPlayer,
    UserInfoSideBar
  },
  data() {
    return {
      videoId:this.$route.params.videoId,
      videoSrcReady:false,
      videoInfo: {
        videoId: null,
        authorId:null,
        authorName:null,
        authorNickName:null,
        authorAvatar:null,
        authorRoleStr:null,
        authorRole:[],
        authorExp:null,
        authorIdentification:null,
        authorIdentInfo:null,
        authorGenderCode:null,
        authorGender:null,
        seriesId:null,
        seriesTitle:null,
        seriesName:null,
        seriesInfo:null,
        seriesGmtCreate:null,
        seriesGmtUpdate:null,
        title:null,
        tags:null,
        tagStr:null,
        tagList:null,
        categoryMain:null,
        categoryMainName:null,
        categorySub:null,
        categorySubName:null,
        isNeedTop:null,
        isNeedPush:null,
        gmtCreate:null,
        gmtUpdate:null,
        gmtPush:null,
        info:null,
        status:null,
        videoStatus:null,
        seriesStatus:null,
        authorStatusCode:null,
        authorStatus:null,
        type:null,
        password:null,
        videoFormat:null,
        videoTimeDuration:null,
        videoPosterId:null,
        videoFileId:null,
        videoMeta:null,
        videoRatio:null,
        videoThumb:null,
        videoScore:null,
        videoView:null,
        needPassword:null
      },
      stars: this.videoInfo!=null?(this.videoInfo.videoScore!=null?this.videoInfo.videoScore!=null:0):0,
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
            src: require('@/assets/videos/remote-video-file_1626327965727.webm'),
            type: "video/mp4"
          }
        ]
      },
    }
  },
  mounted() {
    console.log("Video mounted")
    console.log(this.$route.params.videoId)
    this.getVideoInfo(this.videoId)

  },
  methods: {
    getVideoInfo(videoId){
      if(isNotEmpty(videoId)){
        let _that = this
        this.$store.dispatch('video/getVideoInfoById',videoId)
            .then((data) => {
              console.log(data)
              _that.videoInfo = data
              if(isNotEmpty(_that.videoInfo.videoPosterId)){
                if(isNotEmpty(getToken())){
                  _that.playerOptions.poster =  process.env.VUE_APP_PUBLIC_PATH+'video/'+_that.videoInfo.videoId+ '/poster?Authorization=' + getToken()
                }else{
                  _that.playerOptions.poster =  process.env.VUE_APP_PUBLIC_PATH+'video/'+_that.videoInfo.videoId+ '/poster'
                }
              }else{
                _that.playerOptions.poster ='https://cdn.quasar.dev/img/parallax2.jpg'
              }
              if(isNotEmpty(_that.videoInfo.videoFileId)){
                if(isNotEmpty(getToken())){
                  _that.playerOptions.sources[0].src =  process.env.VUE_APP_PUBLIC_PATH+'video/'+_that.videoInfo.videoId+ '/player?Authorization=' + getToken()
                }else{
                  _that.playerOptions.sources[0].src =  process.env.VUE_APP_PUBLIC_PATH+'video/'+_that.videoInfo.videoId+ '/player'
                }
                if(isNotEmpty(_that.videoInfo.videoFormat)){
                  _that.playerOptions.sources[0].type = _that.videoInfo.videoFormat
                }else{
                  _that.playerOptions.sources[0].type = 'video/mp4'
                }
                console.log(_that.playerOptions.sources[0].src)
                console.log(_that.playerOptions.sources[0].type)
                _that.videoSrcReady=true
              }else{
                FUN.notify("无视频文件",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
              }

            })
            .catch(() => {
              FUN.notify("无法获取视频列表",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)

            })
      }
    },
    getColor(index){
      console.log('getColor')
      console.log(index)
      return randomColor(index,[]);
    },

  }
}
</script>

<style scoped>

</style>
