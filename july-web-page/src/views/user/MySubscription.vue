<template>
  <div class="justify-center">
    <q-tabs
        v-model="tab"
        narrow-indicator
        dense
        align="justify"
    >
      <q-tab class="text-purple" name="activity" icon="mail" label="动态" />
      <q-tab class="text-orange" name="subscription" icon="alarm" label="订阅" />
    </q-tabs>
    <q-separator />
    <q-tab-panels v-model="tab" animated>
      <q-tab-panel name="activity">
        <VideoList></VideoList>
      </q-tab-panel>

      <q-tab-panel name="subscription">
       <UserList></UserList>
      </q-tab-panel>
    </q-tab-panels>
  </div>
</template>

<script>
import VideoList from "@/views/video/VideoList";
import UserList from "@/views/user/UserList";
export default {
  name: "MySubscription",
  components:{
    VideoList,
    UserList
  },
  data () {
    return {
      tab: 'activity'
    }
  },
}
</script>

<style scoped>

</style>
