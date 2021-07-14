<template>
  <div class="q-pa-md no-wrap" style="min-width: 250px">
    <q-list class="q-gutter-md">
      <q-expansion-item class="shadow-1 overflow-hidden"
                        style="border-radius: 30px" label="订单与支付"
                        @show="startCounting"
                        @hide="stopCounting"    header-class="bg-primary text-white"  expand-icon-class="text-white" group="somegroup"
                        :default-opened="nowOpened==='buyerOrders' || nowOpened==='sellerOrders'|| nowOpened==='transactionRecords'" icon="explore" caption="买家与卖家">
        <q-item clickable :active="nowOpened==='buyerOrders'" @click="onClickSettingBarMenu('buyerOrders')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>买家订单</q-item-section>
        </q-item>

        <q-item clickable :active="nowOpened==='sellerOrders'"  @click="onClickSettingBarMenu('sellerOrders')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>卖家订单</q-item-section>
        </q-item>
        <q-item clickable :active="nowOpened==='transactionRecords'"  @click="onClickSettingBarMenu('transactionRecords')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>交易流水</q-item-section>
        </q-item>

      </q-expansion-item>


      <q-expansion-item class="shadow-1 overflow-hidden"
                        style="border-radius: 30px" label="订阅与收藏"
                        @show="startCounting"
                        @hide="stopCounting"
                        :default-opened="nowOpened==='mySubscriptions' || nowOpened==='myFavourites'"  header-class="bg-primary text-white"  expand-icon-class="text-white" group="somegroup" icon="explore" caption="买家与卖家">
        <q-item clickable :active="nowOpened==='mySubscriptions'" @click="onClickSettingBarMenu('mySubscriptions')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>我的订阅</q-item-section>
        </q-item>

        <q-item clickable :active="nowOpened==='myFavourites'" @click="onClickSettingBarMenu('myFavourites')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>我的收藏</q-item-section>
        </q-item>

      </q-expansion-item>

      <q-expansion-item class="shadow-1 overflow-hidden"
                        style="border-radius: 30px" label="创作中心"
                        @show="startCounting"
                        @hide="stopCounting"
                        :default-opened="nowOpened==='myVideos'  || nowOpened==='newProduct' || nowOpened==='myFollowers' || nowOpened==='myWorkStatistics'" header-class="bg-primary text-white"  expand-icon-class="text-white" group="somegroup" icon="explore" caption="买家与卖家">
        <q-item clickable :active="nowOpened==='myVideos'" @click="onClickSettingBarMenu('myVideos')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>我的视频</q-item-section>
        </q-item>
        <q-item clickable :active="nowOpened==='newProduct'" @click="onClickSettingBarMenu('newProduct')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>创作作品</q-item-section>
        </q-item>

        <q-item clickable :active="nowOpened==='myFollowers'" @click="onClickSettingBarMenu('myFollowers')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>关注我的</q-item-section>
        </q-item>
        <q-item clickable  :active="nowOpened==='myWorkStatistics'" @click="onClickSettingBarMenu('myWorkStatistics')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>统计</q-item-section>
        </q-item>

      </q-expansion-item>

      <q-expansion-item class="shadow-1 overflow-hidden"
                        style="border-radius: 30px" label="个人设置"
                        @show="startCounting"
                        @hide="stopCounting"
                        :default-opened="nowOpened==='myInfo' || nowOpened==='myPaymentSettings' || nowOpened==='sellerSettings'" header-class="bg-primary text-white"  expand-icon-class="text-white" group="somegroup" icon="explore" caption="买家与卖家">
        <q-item clickable :active="nowOpened==='myInfo'" @click="onClickSettingBarMenu('myInfo')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>个人信息</q-item-section>
        </q-item>

        <q-item clickable :active="nowOpened==='myPaymentSettings'" @click="onClickSettingBarMenu('myPaymentSettings')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>支付设置</q-item-section>
        </q-item>
        <q-item clickable :active="nowOpened==='sellerSettings'" @click="onClickSettingBarMenu('sellerSettings')" active-class="my-menu-link" v-ripple>
          <q-item-section avatar>
            <q-icon color="primary" name="bluetooth" />
          </q-item-section>

          <q-item-section>卖家设置</q-item-section>
        </q-item>

      </q-expansion-item>
    </q-list>
  </div>
</template>

<script>
export default {
  name: "MySettingMenuSideBar",
  props: {
    defaultOpened:{
      type: String,
      require: false,
      default: 'myWorkStatistics'
    },
  },
  data () {
    return {
      nowOpened: this.defaultOpened,
      counter: 0,

    }
  },
  // watch: {
  //   // Whenever the movie prop changes, fetch new data
  //   nowOpened: {
  //     // Will fire as soon as the component is created
  //     immediate: true,
  //     handler(newVal,oldVal) {
  //       console.log("new val:"+newVal)
  //       console.log("old val:"+oldVal)
  //       if(newVal==='buyerOrder'){
  //         this.$emit('changeCom','buyerOrder')
  //       }
  //     }
  //     // deep: true
  //   },
  // },
  methods: {
    startCounting () {
      this.timer = setInterval(() => {
        this.counter++
      }, 1000)
    },

    stopCounting () {
      clearInterval(this.timer)
    },
    onClickSettingBarMenu(target){
      this.nowOpened=target
      if(target==='buyerOrders'){
        this.$emit('changeCom','buyerOrders')
      }else if(target==='sellerOrders'){
        this.$emit('changeCom','sellerOrders')
      }else if(target==='transactionRecords'){
        this.$emit('changeCom','transactionRecords')
      }else if(target==='mySubscriptions'){
        this.$emit('changeCom','mySubscriptions')
      }else if(target==='myFavourites'){
        this.$emit('changeCom','myFavourites')
      }else if(target==='myVideos'){
        this.$emit('changeCom','myVideos')
      }else if(target==='myFollowers'){
        this.$emit('changeCom','myFollowers')
      }else if(target==='myWorkStatistics'){
        this.$emit('changeCom','myWorkStatistics')
      }else if(target==='newProduct'){
        this.$emit('changeCom','newProduct')
      }else if(target==='myInfo'){
        this.$emit('changeCom','myInfo')
      }else if(target==='myPaymentSettings'){
        this.$emit('changeCom','myPaymentSettings')
      }else if(target==='sellerSettings'){
        this.$emit('changeCom','sellerSettings')
      }
    }
  },

  beforeDestroy () {
    this.stopCounting()
  }
}
</script>

<style lang="sass" scoped>
.my-menu-link
  color: black
  background:	#87CEFA
</style>
