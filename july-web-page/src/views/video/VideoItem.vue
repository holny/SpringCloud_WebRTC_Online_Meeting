<template>
  <q-card class="my-card no-wrap" flat bordered style="width: 300px;">

    <q-img :src="posterBaseURL" style="cursor: pointer;height: 200px" @click="$router.push({ name: 'video', params: { videoId: videoInfo.videoId }})">
      <div class="absolute-bottom">
        <div class="text-h6">{{videoInfo.title}}</div>
        <div class="text-subtitle2">by  {{videoInfo.authorNickName!=null?(videoInfo.authorNickName+'('+videoInfo.authorName+')'):videoInfo.authorName}}</div>
      </div>
    </q-img>


    <q-card-section class="q-gutter-sm q-pa-sm">
      <div class="row no-wrap justify-between">
        <div class="no-wrap items-center">
          <q-rating size="18px" v-model="stars" :max="5" color="primary"  icon-half="star_half"  readonly />
          <span class="text-caption text-grey q-ml-sm">{{stars.toFixed(1)}}</span>
        </div>
        <div class="no-wrap items-center">
          <q-icon class="text-caption text-weight-thin" color="grey-6" name="eva-eye-outline" >
            <q-tooltip>
              <strong >浏览量</strong>
            </q-tooltip>
          </q-icon>
          <span class="text-caption text-grey q-ml-sm">{{videoInfo.videoView!=null?(videoInfo.videoView>10000?videoInfo.videoView%10000+'K':videoInfo.videoView):0}}</span>
        </div>
      </div>
      <div class="row no-wrap justify-between">
        <div class="no-wrap items-center">
          <q-icon class="text-caption text-weight-thin" color="grey-6" name="eva-calendar-outline" >
            <q-tooltip>
              <strong >发布日期</strong>
            </q-tooltip>
          </q-icon>
          <span class="text-  caption text-grey q-ml-sm"> {{videoInfo.gmtUpdate!=null?videoInfo.gmtUpdate!=null:'XXXX-XX-XX'}}</span>
        </div>
        <div class="no-wrap items-center">
          <q-icon class="text-caption text-weight-thin" color="grey-6" name="thumb_up" >
            <q-tooltip>
              <strong >点赞数</strong>
            </q-tooltip>
          </q-icon>
          <span class="text-  caption text-grey q-ml-sm">{{videoInfo.videoThumb!=null?(videoInfo.videoThumb>10000?videoInfo.videoThumb%10000+'K':videoInfo.videoThumb):0}}</span>
        </div>
      </div>
    </q-card-section>
  </q-card>

</template>

<script>
import {getToken} from "@/utils/auth";
import {isNotEmpty} from "@/utils/validate";

export default {
  name: "VideoItem",
  props: {
    videoInfo: {
      type: Object,
      required: true
    },
  },
  computed: {
    posterBaseURL() {
      if(this.videoInfo.videoId){
        if(isNotEmpty(getToken())){
          return  process.env.VUE_APP_PUBLIC_PATH+'video/'+this.videoInfo.videoId+ '/poster?Authorization=' + getToken()
        }else{
          return  process.env.VUE_APP_PUBLIC_PATH+'video/'+this.videoInfo.videoId+ '/poster'
        }

      }else{
        return 'https://cdn.quasar.dev/img/parallax2.jpg'
      }
    },
  },
  data () {
    return {
      stars: this.videoInfo.videoScore!=null?this.videoInfo.videoScore!=null:0,
    }
  },

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
