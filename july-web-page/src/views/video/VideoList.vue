<template>
  <div class="justify-center">
    <q-btn-dropdown flat color="primary" label="按 流浪量排序">
      <q-list>
        <q-item clickable v-close-popup @click="changeOrder('view',false)">
          <q-item-section>
            <q-item-label>浏览量升序</q-item-label>
          </q-item-section>
        </q-item>
        <q-item clickable v-close-popup @click="changeOrder('view',true)">
          <q-item-section>
            <q-item-label>浏览量降序</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="changeOrder('gmt_update',false)">
          <q-item-section>
            <q-item-label>上传时间升序</q-item-label>
          </q-item-section>
        </q-item>
        <q-item clickable v-close-popup @click="changeOrder('gmt_update',true)">
          <q-item-section>
            <q-item-label>上传时间降序</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="changeOrder('video_time_duration',false)">
          <q-item-section>
            <q-item-label>时间长度升序</q-item-label>
          </q-item-section>
        </q-item>
        <q-item clickable v-close-popup @click="changeOrder('video_time_duration',true)">
          <q-item-section>
            <q-item-label>时间长度降序</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-btn-dropdown>

  <div class="row q-pt-xs q-pb-md justify-around">
    <video-item class="q-ma-md" v-for="item in videoInfoList" :key="item.videoId" :video-info="item"/>
  </div>
    <q-pagination
        size="1.2em"
        padding="md"
        v-model="toPageNum"
        :max="pages"
        :value="nowPageNum"
        :max-pages="search.pageLimit"
        direction-links
        unelevated
        color="black"
        active-color="yellow"
        active-text-color="black"
        class="justify-center"
    />
  </div>
</template>

<script>
import VideoItem from '@/views/video/VideoItem'
import {FUN} from "@/utils/julyCommon";
import {isNotEmpty} from "@/utils/validate";

const searchConstant = {
  videoId:null,authorId:null,seriesId:null,categoryMainId:null,categorySubId:null,isTop:null,search:null,gmtCreate:null,gmtUpdate:null,orderByClause:null,isDesc:null,currentPageNum:1,pageLimit:10,
}
export default {
  name: "VideoList",
  components:{
    VideoItem
  },
  data () {
    return {
      hostInfo: this.$store.getters.hostInfo,
      videoInfoList:[],
      nowPageNum:1,
      toPageNum:1,
      pages:0,
      search: searchConstant,
    }
  },

  watch: {
    toPageNum: {
      immediate: true,
      handler(newVal, oldVal) {
        oldVal
        this.search.currentPageNum=newVal
        this.getVideoListBySearch(this.search)
        // Fetch data about the movie
      },
      deep: true
    },
  },
  mounted() {
    console.log("VideoList mounted")
    this.search = FUN.deepCopy(searchConstant)
    // this.video.title="mounted"

  },
  methods: {
    changeOrder(order,isDesc){
      this.search.orderByClause=order
      this.search.isDesc=isDesc
      this.getVideoListBySearch(this.search)
    },
    getVideoListBySearch(search){
      if(isNotEmpty(search)){
        let _that = this
        this.$store.dispatch('video/getVideoInfosBySearch', FUN.deepCopy(search))
            .then((data) => {
              console.log(data)
              _that.videoInfoList = data.records
              _that.search.currentPageNum=data.current
              _that.search.pageLimit=data.size
              _that.pages=data.pages+1
              _that.nowPageNum=data.current
            })
            .catch(() => {
              FUN.notify("无法获取视频列表",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)

            })
      }
    }
  }
}
</script>

<style scoped>

</style>
