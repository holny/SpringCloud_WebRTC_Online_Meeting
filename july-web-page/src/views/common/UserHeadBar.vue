<template>
  <q-responsive :ratio="4/3" class="col" style="max-height: 250px">
    <q-card class="column my-card bg-grey-1">
      <q-img class="col" src="https://cdn.quasar.dev/img/parallax1.jpg" style="max-height: 100px"/>

      <q-card-section horizontal class="q-pa-lg justify-between">
        <q-item>
          <q-item-section avatar class="items-center q-gutter-y-sm">
            <q-avatar size="60px">
              <img :src="hostInfo!=null?(hostInfo.avatar!=null?hostInfo.avatar:'https://cdn.quasar.dev/img/avatar2.jpg'):'https://cdn.quasar.dev/img/avatar2.jpg'">
              <q-badge v-if="hostInfo!=null&&hostInfo.identification!=null" rounded floating color="yellow-10">
                <q-icon name="done" color="white" />
                <q-tooltip>
                  <strong >已认证{{ (hostInfo!=null&&hostInfo.identInfo!=null)?': '+hostInfo.identInfo:''}}</strong>
                </q-tooltip>
              </q-badge>
            </q-avatar>
            <q-item-label clickable class="text-subtitle1 text-black items-center">
              <q-badge color="blue">
                {{ (hostInfo!=null&&hostInfo.level!=null)?'Lv.'+hostInfo.level:'Lv.0'}}
              </q-badge>
            </q-item-label>
          </q-item-section>
          <q-item-section>
            <div>
              <q-icon name="person" size="xs" class="q-mr-sm inline-block text-teal">
                <q-tooltip>
                  <strong >姓名</strong>
                </q-tooltip>
              </q-icon>
              <q-item-label class="text-h6 q-mb-sm inline-block">
                {{hostInfo!=null?((hostInfo.nickName!=null&&hostInfo.nickName!=='')?hostInfo.nickName+'('+hostInfo.userName+')':hostInfo.userName):'UnKnown'}}
              </q-item-label>
            </div>
            <div>
              <q-icon name="local_offer" size="xs"  class="q-mr-sm inline-block text-brown">
                <q-tooltip>
                  <strong >ID</strong>
                </q-tooltip>
              </q-icon>
              <q-item-label class="text-subtitle2  q-mb-sm inline-block">
                {{hostInfo!=null?(hostInfo.userId!=null?hostInfo.userId:'UnKnown'):'UnKnown'}}
              </q-item-label>
            </div>
            <div>
              <q-icon name="badge" size="xs"  class="q-mr-sm inline-block text-accent">
                <q-tooltip>
                  <strong >角色</strong>
                </q-tooltip>
              </q-icon>
              <q-item-label class="text-subtitle2  text-capitalize text-no-wrap text-orange  q-mb-sm inline-block">{{ (hostInfo!=null&&hostInfo.role!=null)?hostInfo.role:''}}</q-item-label>
            </div>
            <div>
              <q-icon name="tag" size="xs"  class="q-mr-sm inline-block text-orange">
                <q-tooltip>
                  <strong >擅长</strong>
                </q-tooltip>
              </q-icon>
              <q-item-label class="inline-block q-gutter-x-md  q-mb-sm"><q-badge outline color="primary" label="Outline" />
                <q-badge outline color="orange" label="Outline" />
                <q-badge outline color="secondary" label="Outline" /></q-item-label>
            </div>
          </q-item-section>
        </q-item>
        <q-item class="float-right">
          <q-card-actions  class="q-gutter-x-md">
            <div>
              <q-btn flat round color="red" :icon="isFollow ? 'favorite' : 'favorite_border'" @click="isFollow = !isFollow" ><q-tooltip>关注ta</q-tooltip></q-btn>
              <div class="text-subtitle2 text-center text-red" > 22k</div>
            </div>
            <div>
              <q-btn flat round color="accent" icon="bookmark" ><q-tooltip>预约咨询</q-tooltip></q-btn>
              <div class="text-subtitle2 text-center text-accent" >咨询</div>
            </div>
            <div>
              <q-btn flat round color="positive" icon="forum" ><q-tooltip>私聊</q-tooltip></q-btn>
              <div class="text-subtitle2 text-center text-positive" >私聊</div>
            </div>
            <div>
              <q-btn flat round color="primary" icon="share" ><q-tooltip>个人主页</q-tooltip></q-btn>
              <div class="text-subtitle2 text-center text-primary" >主页</div>
            </div>
          </q-card-actions>
        </q-item>
      </q-card-section>
    </q-card>
  </q-responsive>
</template>

<script>

import {FUN} from "@/utils/julyCommon";

export default {
  name: "UserHeadBar",
  data () {
    return {
      isFollow: false,
      hostInfo: this.$store.getters.hostInfo,
    }
  },
  async mounted() {
    console.log("UserHeadBar mounted")
    this.userInfo = await this.getUserInfo(this.userId)
  },
  methods:{
    async getUserInfo(search){
      let result
      console.log('getSearchUserInfo :' +search)
      await this.$store.dispatch('user/searchUserInfo', search)
          .then((data) => {
            console.log('getSearchUserInfo successful')
            console.log(data)
            result = data
          })
          .catch((error) => {
            console.log('getSearchUserInfo fail')
            FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            result = []
          })
      return result
    }
  }
}
</script>

<style scoped>

</style>
