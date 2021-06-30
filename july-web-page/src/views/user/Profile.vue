<template>
  <div class="q-pa-md">
    <div class="row">
      <UserHeadBar></UserHeadBar>
    </div>
    <div class="row no-wrap q-mt-md">
      <VideoList></VideoList>
      <div class="column q-gutter-y-lg"  style="width: 1500px">
        <div>
          <q-card class="my-card  bg-grey-1" flat bordered>
    <!--        <q-img-->
    <!--            src="https://cdn.quasar.dev/img/parallax2.jpg"-->
    <!--        />-->
            <VideoPlayer :options="playerOptions" :auto-size="false" style="overflow: hidden"></VideoPlayer>
            <q-card-section>
              <div class="text-overline text-orange-9">个人说明</div>
              <div class="text-caption text-grey">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
              </div>
            </q-card-section>
            <q-separator />
            <q-card-actions>
              <q-btn flat color="dark" label="视频" @click="tab='videos'"/>
              <q-btn flat color="primary" label="系列" @click="tab='series'"/>
              <q-space />
              <q-btn-dropdown flat color="primary" label="近 1 天">
                <q-list>
                  <q-item clickable v-close-popup>
                    <q-item-section>
                      <q-item-label>最近更新</q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item clickable v-close-popup>
                    <q-item-section>
                      <q-item-label>近1天</q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item clickable v-close-popup>
                    <q-item-section>
                      <q-item-label>近7天</q-item-label>
                    </q-item-section>
                  </q-item>

                  <q-item clickable v-close-popup>
                    <q-item-section>
                      <q-item-label>近1个月</q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </q-btn-dropdown>
            </q-card-actions>
            <div class="q-gutter-y-sm">
              <q-tab-panels
                  v-model="tab"
                  animated
                  transition-prev="fade"
                  transition-next="fade"
                  class="text-center bg-grey-1"
              >
                <q-tab-panel name="videos" class="text-orange">
                  <div v-for="(item, index) in items" :key="index" class="q-mb-sm text-left" style="cursor: pointer">
                    <q-badge color="orange" class="q-mr-md">
                      {{ items.length - index }}
                    </q-badge>
                    Lorem ipsum dolor sit amet,
                  </div>
                </q-tab-panel>

                <q-tab-panel name="series" class="text-cyan">
                  <div v-for="(item, index) in items" :key="index" class="q-mb-sm text-left" style="cursor: pointer">
                    <q-badge color="cyan" class="q-mr-md">
                      {{ items.length - index }}
                    </q-badge>
                    Lorem ipsum dolor sit amet,
                  </div>
                </q-tab-panel>
              </q-tab-panels>
            </div>
          </q-card>
        </div>
        <div >
          <ConsultSideBar :self-introduction="false"></ConsultSideBar>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import VideoList from "@/views/video/VideoList";
import VideoPlayer from "@/views/video/VideoPlayer";
import ConsultSideBar from "@/views/common/ConsultSideBar";
import UserHeadBar from "@/views/common/UserHeadBar";
export default {
  name: "Profile",
  components:{
    VideoList,
    VideoPlayer,
    ConsultSideBar,
    UserHeadBar
  },
  data () {
    return {
      expanded: false,
      tab: 'videos',
      items: [ {}, {}, {}, {}, {}, {}, {}, {}, {} ],
      lorem: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.',
      playerOptions: {
        // video.js options
        controls: true,
        bigPlayButton: true,
        autoplay: false,
        loop: false,
        fluid: false,
        poster: require('@/assets/videos/anime-watch.jpg'),
        width: 430,
        height: 240,
        sources: [
          {
            src: require('@/assets/videos/1.mp4'),
            type: "video/mp4"
          }
        ]
      },
    }
  }
}
</script>

<style scoped>
.my-card {
  transition: box-shadow .3s;
  width: 100%;
  height: 100%;
  border-radius:10px;

}
.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}
</style>
