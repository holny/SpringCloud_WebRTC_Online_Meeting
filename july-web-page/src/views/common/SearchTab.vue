<template>
  <q-card>
    <div class="row justify-center q-pb-md">
      <q-input dense standout="bg-primary" v-model="search"  placeholder="搜索"  style="width: 40%">
        <template v-slot:prepend>
          <q-icon v-if="search === ''" name="search" />
          <q-icon v-else name="clear" class="cursor-pointer" @click="search = ''" />
        </template>
      </q-input>
    </div>
    <q-tabs
        v-model="tab"
        dense
        class="text-grey q-header--bordered"
        active-color="primary"
        indicator-color="primary"
        align="justify"
        narrow-indicator
        style="height: 50px"
    >
      <q-tab class="text-purple" name="videos" label="视频" />
      <q-tab class="text-orange" name="users" label="用户" />
      <q-tab class="text-teal" name="movies" label="专家" />
    </q-tabs>

    <q-separator />

    <q-tab-panels v-model="tab" animated>
      <q-tab-panel name="videos">
        <video-list></video-list>
      </q-tab-panel>

      <q-tab-panel name="users">
        <user-list></user-list>
      </q-tab-panel>

      <q-tab-panel name="movies">
        <div class="text-h6">Movies</div>
        Lorem ipsum dolor sit amet consectetur adipisicing elit.
      </q-tab-panel>
    </q-tab-panels>
  </q-card>
</template>

<script>
import VideoList from "@/views/video/VideoList";
import UserList from "@/views/user/UserList";
export default {
  name: "SearchTab",
  components:{
    VideoList,
    UserList
  },
  data () {
    return {
      tab: 'videos',
      search : '',
    }
  }
}
</script>

<style scoped>

</style>
