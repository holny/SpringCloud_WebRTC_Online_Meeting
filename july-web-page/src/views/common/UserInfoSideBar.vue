<template>
  <q-card class="my-card no-wrap  bg-grey-1" flat bordered>
    <q-card-section horizontal style="height: 200px">
      <q-card-section class="q-pt-xs">
        <div class="text-overline">个人信息</div>
        <div class="text-h5 q-mt-sm q-mb-xs">{{userInfo!=null?((userInfo.nickName!=null&&userInfo.nickName!=='')?userInfo.nickName+'('+userInfo.userName+')':userInfo.userName):'UnKnown'}}</div>
        <div class="text-caption text-grey">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
        </div>
      </q-card-section>

      <q-card-section class="col-5 flex flex-center">
        <q-item>
          <q-item-section avatar>
            <q-avatar size="60px">
              <img :src="userInfo!=null?(userInfo.avatar!=null?userInfo.avatar:'https://cdn.quasar.dev/img/avatar2.jpg'):'https://cdn.quasar.dev/img/avatar2.jpg'">
              <q-badge v-if="userInfo!=null&&userInfo.identification!=null" rounded floating color="yellow-10">
                <q-icon name="done" color="white" />
                <q-tooltip>
                  <strong >已认证{{ (userInfo!=null&&userInfo.identInfo!=null)?': '+userInfo.identInfo:''}}</strong>
                </q-tooltip>
              </q-badge>
            </q-avatar>
          </q-item-section>
          <q-item-section>
            <q-item-label clickable class="text-subtitle1 text-black text-center">
              <q-badge color="blue">
                {{ (userInfo!=null&&userInfo.level!=null)?'Lv.'+userInfo.level:'Lv.0'}}
              </q-badge>
            </q-item-label>
            <q-item-label class="text-caption text-bold text-capitalize text-no-wrap text-orange"> {{ (userInfo!=null&&userInfo.role!=null)?userInfo.role:''}}</q-item-label>
          </q-item-section>
        </q-item>
      </q-card-section>
    </q-card-section>
    <q-separator />
    <q-card-actions  class="justify-around">
      <div>
        <q-btn flat round color="red" :icon="isFollow ? 'favorite' : 'favorite_border'" @click="isFollow = !isFollow"><q-tooltip>关注ta</q-tooltip></q-btn>
        <div class="text-subtitle2 text-center text-red" > 22k</div>
      </div>
      <div>
        <q-btn flat round color="accent" icon="bookmark" ><q-tooltip>预约咨询</q-tooltip></q-btn>
        <div class="text-subtitle2 text-center text-accent" >咨询</div>
      </div>
      <div>
        <q-btn flat round color="accent" icon="forum" ><q-tooltip>私聊</q-tooltip></q-btn>
        <div class="text-subtitle2 text-center text-accent" >私聊</div>
      </div>
      <div>
        <q-btn flat round color="primary" icon="share" ><q-tooltip>个人主页</q-tooltip></q-btn>
        <div class="text-subtitle2 text-center text-primary" >主页</div>
      </div>
    </q-card-actions>
    <q-separator />

    <q-card-section class="q-gutter-sm q-pa-md">
      <div class="text-overline">擅长领域</div>
      <q-badge outline color="primary" label="Outline" />
      <q-badge outline color="orange" label="Outline" />
      <q-badge outline color="secondary" label="Outline" />
    </q-card-section>

    <q-card-section class="q-gutter-sm">
      <q-tabs
          v-model="tab"
          class="bg-grey-1 full-width"
          dense
          align="justify"
      >
        <q-tab class="text-orange" name="mails" icon="slideshow" label="视频" />
        <q-tab class="text-cyan" name="alarms" icon="playlist_play" label="系列" />
        <q-tab class="text-red" name="movies" icon="record_voice_over" label="评价" />
      </q-tabs>
      <div class="q-gutter-y-sm">
        <q-tab-panels
            v-model="tab"
            animated
            transition-prev="jump-up"
            transition-next="jump-down"
            class="text-center bg-grey-1"
        >
          <q-tab-panel name="mails" class="text-orange">
            <div class="text-h6">视频</div>
            <div v-for="(item, index) in items" :key="index" class="q-mb-sm text-left">
              <q-badge color="secondary">
                {{ items.length - index }}
              </q-badge>
              Lorem ipsum dolor sit amet,
            </div>
          </q-tab-panel>

          <q-tab-panel name="alarms" class="text-cyan">
            <div class="text-h6">系列</div>
            <div v-for="(item, index) in items" :key="index" class="q-mb-sm text-left">
              <q-badge color="secondary">
                {{ items.length - index }}
              </q-badge>
              Lorem ipsum dolor sit amet,
            </div>
          </q-tab-panel>

          <q-tab-panel name="movies" class="text-red">
            <div class="text-h6">评价</div>
            <div v-for="(item, index) in items" :key="index" class="q-mb-sm text-left">
              <q-badge color="secondary">
                {{ items.length - index }}
              </q-badge>
              Lorem ipsum dolor sit amet,
            </div>
          </q-tab-panel>
        </q-tab-panels>
      </div>
    </q-card-section>
  </q-card>
</template>

<script>
import {isNotEmpty} from "@/utils/validate";
import {FUN} from "@/utils/julyCommon";

export default {
  name: "UserInfoSideBar",
  props: {
    userId: {
      type: String,
      require: true,
    },
  },
  data () {
    return {
      userInfo:null,
      tab: 'mails',
      isFollow: false,
      items: [ {}, {}, {}, {}, {}, {}, {}, {}, {} ]
    }
  },
  async mounted() {
    console.log("UserInfoSideBar mounted")
    this.userInfo = await this.getUserInfo(this.userId)
  },
  methods:{
    async getUserInfo(userId){
      let result=null
      if(isNotEmpty(userId)){
        await this.$store.dispatch('user/getUserInfo', userId)
            .then((data) => {
              result = data
            })
            .catch((msg) => {
              FUN.notify(msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
              result = null
            })
      }
      return result
    }
  }
}
</script>

<style scoped>
.my-card {
  transition: box-shadow .3s;
  height: 780px;
  border-radius:10px;
  width: 360px;

}
.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}

</style>
