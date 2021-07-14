<template>
  <div class="q-pa-md no-wrap" style="min-width: 250px">
    <q-list class="q-gutter-md">
      <q-expansion-item class="shadow-1 overflow-hidden"
                        style="border-radius: 30px" label="用户管理" header-class="bg-primary text-white"  expand-icon-class="text-white" group="somegroup"
                        :default-opened="nowOpened==='userManagement'|| nowOpened==='userStatistics'" icon="explore" caption="用户管理">
        <q-item clickable :active="nowOpened==='userManagement'" @click="onClickSettingBarMenu('userManagement')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>用户管理</q-item-section>
        </q-item>

        <q-item clickable  :active="nowOpened==='userStatistics'" @click="onClickSettingBarMenu('userStatistics')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>用户统计</q-item-section>
        </q-item>
      </q-expansion-item>


      <q-expansion-item class="shadow-1 overflow-hidden"
                        style="border-radius: 30px" label="视频管理"
                        :default-opened="nowOpened==='videoManagement' || nowOpened==='videoStatistics'"  header-class="bg-primary text-white"  expand-icon-class="text-white" group="somegroup" icon="explore" caption="视频管理">
        <q-item clickable :active="nowOpened==='videoManagement'" @click="onClickSettingBarMenu('videoManagement')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>视频管理</q-item-section>
        </q-item>
        <q-item clickable  :active="nowOpened==='videoStatistics'" @click="onClickSettingBarMenu('videoStatistics')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>视频统计</q-item-section>
        </q-item>
      </q-expansion-item>

      <q-expansion-item class="shadow-1 overflow-hidden"
                        style="border-radius: 30px" label="咨询管理"
                        :default-opened="nowOpened==='consultOrderManagement'  || nowOpened==='consultStatistics' " header-class="bg-primary text-white"  expand-icon-class="text-white" group="somegroup" icon="explore" caption="咨询管理">
        <q-item clickable :active="nowOpened==='consultOrderManagement'" @click="onClickSettingBarMenu('consultOrderManagement')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>咨询订单管理</q-item-section>
        </q-item>
        <q-item clickable  :active="nowOpened==='consultStatistics'" @click="onClickSettingBarMenu('consultStatistics')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>咨询统计</q-item-section>
        </q-item>

      </q-expansion-item>

      <q-expansion-item class="shadow-1 overflow-hidden"
                        style="border-radius: 30px" label="支付管理"
                        :default-opened="nowOpened==='paymentOrderManagement'  || nowOpened==='paymentStatistics' " header-class="bg-primary text-white"  expand-icon-class="text-white" group="somegroup" icon="explore" caption="支付管理">
        <q-item clickable :active="nowOpened==='paymentOrderManagement'" @click="onClickSettingBarMenu('paymentOrderManagement')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>支付订单管理</q-item-section>
        </q-item>
        <q-item clickable  :active="nowOpened==='paymentStatistics'" @click="onClickSettingBarMenu('paymentStatistics')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>支付统计</q-item-section>
        </q-item>

      </q-expansion-item>

      <q-expansion-item class="shadow-1 overflow-hidden"
                        style="border-radius: 30px" label="网站设置"
                        :default-opened="nowOpened==='siteManagement' || nowOpened==='siteStatistics' " header-class="bg-primary text-white"  expand-icon-class="text-white" group="somegroup" icon="explore" caption="网站设置">
        <q-item clickable :active="nowOpened==='siteManagement'" @click="onClickSettingBarMenu('siteManagement')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>网站管理</q-item-section>
        </q-item>

        <q-item clickable :active="nowOpened==='siteStatistics'" @click="onClickSettingBarMenu('siteStatistics')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>全站统计</q-item-section>
        </q-item>
      </q-expansion-item>
    </q-list>
  </div>
</template>

<script>
export default {
  name: "AdminSettingMenuSideBar",
  props: {
    defaultOpened:{
      type: String,
      require: false,
      default: 'userManagement'
    },
  },
  data () {
    return {
      nowOpened: this.defaultOpened,
      counter: 0,

    }
  },

  methods: {

    onClickSettingBarMenu(target){
      this.nowOpened=target
      if(target==='userManagement'){
        this.$emit('changeCom','userManagement')
      }else if(target==='userStatistics'){
        this.$emit('changeCom','userStatistics')
      }else if(target==='videoManagement'){
        this.$emit('changeCom','videoManagement')
      }else if(target==='videoStatistics'){
        this.$emit('changeCom','videoStatistics')
      }else if(target==='consultOrderManagement'){
        this.$emit('changeCom','consultOrderManagement')
      }else if(target==='consultStatistics'){
        this.$emit('changeCom','consultStatistics')
      }else if(target==='paymentOrderManagement'){
        this.$emit('changeCom','paymentOrderManagement')
      }else if(target==='paymentStatistics'){
        this.$emit('changeCom','paymentStatistics')
      }else if(target==='siteManagement'){
        this.$emit('changeCom','siteManagement')
      }else if(target==='siteStatistics'){
        this.$emit('changeCom','siteStatistics')
      }
    }
  },

}
</script>

<style lang="sass" scoped>
.my-menu-link
  color: black
  background:	#87CEFA
</style>
