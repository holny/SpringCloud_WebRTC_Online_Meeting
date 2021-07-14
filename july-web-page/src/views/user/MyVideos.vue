<template>
  <div class="justify-center">
    <q-tabs
        v-model="tab"
        narrow-indicator
        dense
        align="justify"
    >
      <q-tab class="text-purple" name="public" icon="mail" label="已发布" />
      <q-tab class="text-orange" name="private" icon="alarm" label="私有" />
    </q-tabs>
    <q-separator />
    <q-tab-panels v-model="tab" animated>
      <q-tab-panel name="public">
        <VideoList></VideoList>
      </q-tab-panel>

      <q-tab-panel name="private">
        <VideoList></VideoList>
      </q-tab-panel>
    </q-tab-panels>
  </div>
</template>

<script>
import VideoList from "@/views/video/VideoList";

export default {
  name: "MyVideos",
  components:{
    VideoList
  },
  data () {
    return {
      tab: 'public'
    }
  },
}
</script>

<style scoped>

</style>
