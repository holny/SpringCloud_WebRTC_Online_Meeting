<template>
  <q-card class="my-card bg-grey-1" flat bordered :style="{width: barWidth+'px'}">
    <VideoPlayer v-if="selfIntroduction" :options="playerOptions" :auto-size="false" style="overflow: hidden"></VideoPlayer>
    <q-card-section>
      <div class="text-h5 q-mt-sm q-mb-xs">咨询</div>
      <div class="text-overline text-orange-9">咨询说明</div>
      <div class="text-caption text-grey">
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
      </div>
    </q-card-section>
    <q-separator />
    <q-card-section>
      <div class="text-overline text-orange-9 q-mb-md">繁忙程度</div>
      <q-badge color="positive">
        近1天
      </q-badge>
      <q-linear-progress :value="progress" color="positive" class="q-mb-md"/>
      <q-badge color="accent">
        近7天
      </q-badge>
      <q-linear-progress :value="progress" color="accent" class="q-mb-md"/>
      <q-badge color="warning">
        近1个月
      </q-badge>
      <q-linear-progress :value="progress" color="warning" class="q-mb-md"/>
    </q-card-section>
    <q-separator />
    <q-card-section>
      <div>
        <div class="text-overline text-orange-9 q-mb-md inline-block">近期统计</div>
        <q-btn-dropdown flat color="primary" label="近 1 天" class="inline-block float-right">
          <q-list>
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
      </div>
      <q-badge color="orange">
        准时率
      </q-badge>
      <q-circular-progress
          show-value
          font-size="12px"
          :value="value"
          size="50px"
          :thickness="0.22"
          color="orange"
          track-color="grey-3"
          class="q-ma-md"
      >
        {{ value }}%
      </q-circular-progress>

      <q-badge color="blue">
        好评率
      </q-badge>
      <q-circular-progress
          show-value
          font-size="12px"
          :value="value"
          size="50px"
          :thickness="0.22"
          color="blue"
          track-color="grey-3"
          class="q-ma-md"
      >
        {{ value }}%
      </q-circular-progress>

      <q-badge color="teal">
        续订率
      </q-badge>
      <q-circular-progress
          show-value
          font-size="12px"
          :value="value"
          size="50px"
          :thickness="0.22"
          color="teal"
          track-color="grey-3"
          class="q-ma-md"
      >
        {{ value }}%
      </q-circular-progress>
    </q-card-section>
    <q-separator />
    <q-card-section>
      <div class="text-overline text-orange-9 q-mb-md">咨询类型</div>
      <PieChart></PieChart>
    </q-card-section>
    <q-separator />
    <q-card-section>
      <div class="text-overline text-orange-9 q-mb-md">最近评价</div>
      <q-timeline layout="dense" side="left" color="secondary">
        <q-timeline-entry
            subtitle="February 22, 1986"
            side="left"
            avatar="https://cdn.quasar.dev/img/avatar3.jpg"
        >
          <div>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusm
          </div>
        </q-timeline-entry>

        <q-timeline-entry
            subtitle="February 21, 1986"
            side="right"
            avatar="https://cdn.quasar.dev/img/avatar3.jpg"
        >
          <div>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusm
          </div>
        </q-timeline-entry>

        <q-timeline-entry
            subtitle="February 22, 1986"
            side="left"
            avatar="https://cdn.quasar.dev/img/avatar3.jpg"
        >
          <div>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusm
          </div>
        </q-timeline-entry>

        <q-timeline-entry
            subtitle="February 22, 1986"
            side="right"
            avatar="https://cdn.quasar.dev/img/avatar3.jpg"
        >
          <div>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusm
          </div>
        </q-timeline-entry>

        <q-timeline-entry
            subtitle="February 22, 1986"
            side="left"
            avatar="https://cdn.quasar.dev/img/avatar3.jpg"
        >
          <div>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusm
          </div>
        </q-timeline-entry>
      </q-timeline>
    </q-card-section>
  </q-card>
</template>

<script>
import PieChart from "@/views/charts/PieChart";
import VideoPlayer from "@/views/video/VideoPlayer";
export default {
  name: "ConsultSideBar",
  components:{
    PieChart,
    VideoPlayer
  },
  props: {
    selfIntroduction: {
      type: Boolean,
      required: false,
      default: true,
    },
    barWidth:{
      type: String,
      required: false,
      default: '430',
    }
  },
  data () {
    return {
      progress: 0.4,
      value: 81,
      playerOptions: {
        // video.js options
        controls: true,
        bigPlayButton: true,
        autoplay: false,
        loop: false,
        fluid: false,
        poster: require('@/assets/videos/anime-watch.jpg'),
        width: this.barWidth,
        height: this.barWidth*0.558,
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
.my-card {
  transition: box-shadow .3s;
  height: 100%;
  border-radius:10px;

}
.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}
</style>
