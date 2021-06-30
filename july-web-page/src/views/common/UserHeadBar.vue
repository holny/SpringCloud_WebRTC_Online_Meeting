<template>
  <q-responsive :ratio="4/3" class="col" style="max-height: 250px">
    <q-card class="column my-card bg-grey-1">
      <q-img class="col" src="https://cdn.quasar.dev/img/parallax1.jpg" style="max-height: 100px"/>

      <q-card-section horizontal class="q-pa-lg justify-between">
        <q-item>
          <q-item-section avatar class="items-center q-gutter-y-sm">
            <q-avatar size="60px">
              <img src="https://cdn.quasar.dev/img/avatar2.jpg">
              <q-badge rounded floating color="yellow-10">
                <q-icon name="done" color="white" />
                <q-tooltip>
                  <strong >已认证</strong>
                </q-tooltip>
              </q-badge>
            </q-avatar>
            <q-item-label clickable class="text-subtitle1 text-black items-center">
              <q-badge color="blue">
                Lv.12
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
                fasdawdawdfasdawdawdfasdawdawdfasdawdawdfasdawdawd
              </q-item-label>
            </div>
            <div>
              <q-icon name="local_offer" size="xs"  class="q-mr-sm inline-block text-brown">
                <q-tooltip>
                  <strong >ID</strong>
                </q-tooltip>
              </q-icon>
              <q-item-label class="text-caption q-mb-sm inline-block">
                fawfawfawf
              </q-item-label>
            </div>
            <div>
              <q-icon name="badge" size="xs"  class="q-mr-sm inline-block text-accent">
                <q-tooltip>
                  <strong >角色</strong>
                </q-tooltip>
              </q-icon>
              <q-item-label class="text-subtitle2 text-italic text-capitalize  q-mb-sm inline-block">Admin</q-item-label>
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
export default {
  name: "UserHeadBar",
  data () {
    return {
      isFollow: false,
    }
  }
}
</script>

<style scoped>

</style>
