<template>
  <div class="q-pa-md no-wrap">
    <div class="row">
      <UserHeadBar></UserHeadBar>
    </div>
    <div class="row no-wrap q-mt-md q-gutter-x-lg full-width">
      <div class="col-2">
          <MySettingMenuSideBar @changeCom="changeCom($event)" :default-opened="settingsName"></MySettingMenuSideBar>
      </div>
      <div class="col">
      <component :is="settingsComName!=null?settingsComName:''"></component>
      </div>
    </div>
  </div>
</template>

<script>
import UserHeadBar from "@/views/common/UserHeadBar";
import MySettingMenuSideBar from "@/views/common/MySettingMenuSideBar";
import OrderItems from "@/views/trading/OrderItems";
import TradingRecords from "@/views/trading/TradingRecords";
import MySubscription from "@/views/user/MySubscription";
import MyFavourite from "@/views/user/MyFavourite";
import MyVideos from "@/views/user/MyVideos";
import NewVideo from "@/views/video/NewVideo";
import MyFollower from "@/views/user/MyFollower";
import MyWorkStatistics from "@/views/user/MyWorkStatistics";
import MyInfoSettings from "@/views/user/MyInfoSettings";
import PaymentSettings from "@/views/trading/PaymentSettings";
import TradingSettings from "@/views/trading/TradingSettings";
export default {
  name: "MySetting",
  components:{
    UserHeadBar,
    MySettingMenuSideBar,
    OrderItems,
    TradingRecords,
    MySubscription,
    MyFavourite,
    MyVideos,
    MyFollower,
    MyWorkStatistics,
    NewVideo,
    MyInfoSettings,
    PaymentSettings,
    TradingSettings
  },
  data () {
    return {
      settingsName: null,
      settingsComName: null,
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        const query = route.query
        console.log(route)
        console.log("query "+query)
        this.settingsName = route.meta.component
        if(this.settingsName==='buyerOrders'){
          this.settingsComName = 'OrderItems'
        }else if(this.settingsName==='sellerOrders'){
          this.settingsComName = ''
        }else if(this.settingsName==='transactionRecords'){
          this.settingsComName = 'TradingRecords'
        }else if(this.settingsName==='mySubscriptions'){
          this.settingsComName = 'MySubscription'
        }else if(this.settingsName==='myFavourites'){
          this.settingsComName = 'MyFavourite'
        }else if(this.settingsName==='myVideos'){
          this.settingsComName = 'MyVideos'
        }else if(this.settingsName==='myFollowers'){
          this.settingsComName = 'MyFollower'
        }else if(this.settingsName==='myWorkStatistics'){
          this.settingsComName = 'MyWorkStatistics'
        }else if(this.settingsName==='newProduct'){
          this.settingsComName = 'NewVideo'
        }else if(this.settingsName==='myInfo'){
          this.settingsComName = 'MyInfoSettings'
        }else if(this.settingsName==='myPaymentSettings'){
          this.settingsComName = 'PaymentSettings'
        }else if(this.settingsName==='sellerSettings'){
          this.settingsComName = 'TradingSettings'
        }
        // this.$router.go(0);
        // if (query) {
        //   console.log('watch route handler query:' + JSON.stringify(query))
        //   this.redirect = query.redirect
        //   this.otherQuery = this.getOtherQuery(query)
        // }
      },
      immediate: true
    }
  },
  created() {
    console.log("MySettings created")
    console.log(this.$route.meta.component)
    this.settingsName = this.$route.meta.component
    if(this.settingsName==='buyerOrders'){
      this.settingsComName = 'OrderItems'
    }else if(this.settingsName==='sellerOrders'){
      this.settingsComName = ''
    }else if(this.settingsName==='transactionRecords'){
      this.settingsComName = 'TradingRecords'
    }else if(this.settingsName==='mySubscriptions'){
      this.settingsComName = 'MySubscription'
    }else if(this.settingsName==='myFavourites'){
      this.settingsComName = 'MyFavourite'
    }else if(this.settingsName==='myVideos'){
      this.settingsComName = 'MyVideos'
    }else if(this.settingsName==='myFollowers'){
      this.settingsComName = 'MyFollower'
    }else if(this.settingsName==='myWorkStatistics'){
      this.settingsComName = 'MyWorkStatistics'
    }else if(this.settingsName==='newProduct'){
      this.settingsComName = 'NewVideo'
    }else if(this.settingsName==='myInfo'){
      this.settingsComName = 'MyInfoSettings'
    }else if(this.settingsName==='myPaymentSettings'){
      this.settingsComName = 'PaymentSettings'
    }else if(this.settingsName==='sellerSettings'){
      this.settingsComName = 'TradingSettings'
    }
  },
  methods: {
    changeCom: function (event) {
      console.log(event)
      if (event === 'buyerOrders') {
        console.log("change com buyerOrder")
        this.$router.push({name: 'buyerOrders'});
        // this.$router.push({ name: 'buyerOrders', params: { vid: item.toString() }});
      }else if (event === 'sellerOrders') {
        console.log("change com buyerOrder")
        this.$router.push({ name: 'sellerOrders'});
        // this.$router.push({ name: 'sellerOrders', params: { vid: item.toString() }});
      }else if (event === 'transactionRecords') {
        this.$router.push({ name: 'tradingRecords'});
        // this.$router.push({ name: 'sellerOrders', params: { vid: item.toString() }});
      }else if(event==='mySubscriptions'){
        this.$router.push({ name: 'mySubscriptions'});
      }else if(event==='myFavourites'){
        this.$router.push({ name: 'myFavourites'});
      }else if(event==='myVideos'){
        this.$router.push({ name: 'myVideos'});
      }else if(event==='myFollowers'){
        this.$router.push({ name: 'myFollowers'});
      }else if(event==='myWorkStatistics'){
        this.$router.push({ name: 'myWorkStatistics'});
      }else if(event==='newProduct'){
        this.$router.push({ name: 'newProduct'});
      }else if(event==='myInfo'){
        this.$router.push({ name: 'myInfo'});
      }else if(event==='myPaymentSettings'){
        this.$router.push({ name: 'myPaymentSettings'});
      }else if(event==='sellerSettings'){
        this.$router.push({ name: 'sellerSettings'});
      }
    }
  }
}
</script>

<style scoped>

</style>
