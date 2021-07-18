<template>
  <div class="q-pa-md no-wrap">
    <div class="row no-wrap q-mt-md q-gutter-x-lg full-width">
      <div class="col-2">
          <AdminSettingMenuSideBar @changeCom="changeCom($event)" :default-opened="settingsName"></AdminSettingMenuSideBar>
      </div>
      <div class="col">
<!--      <component :is="settingsComName!=null?settingsComName:''"></component>-->
        <router-view name="adminSettingsComponent"></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import AdminSettingMenuSideBar from "@/views/admin/AdminSettingMenuSideBar";
// import UserManagement from "@/views/admin/UserManagement";
// import UserStatistics from "@/views/admin/UserStatistics";
// import VideoManagement from "@/views/admin/VideoManagement";
// import VideoStatistics from "@/views/admin/VideoStatistics";
// import ConsultOrderManagement from "@/views/admin/ConsultOrderManagement";
// import ConsultStatistics from "@/views/admin/ConsultStatistics";
// import PaymentOrderManagement from "@/views/admin/PaymentOrderManagement";
// import PaymentStatistics from "@/views/admin/PaymentStatistics";
// import SiteSettings from "@/views/admin/SiteSettings";
// import SiteStatistics from "@/views/admin/SiteStatistics";
export default {
  name: "AdminMain",
  components:{
    AdminSettingMenuSideBar,
    // UserManagement,
    // UserStatistics,
    // VideoManagement,
    // VideoStatistics,
    // ConsultOrderManagement,
    // ConsultStatistics,
    // PaymentOrderManagement,
    // PaymentStatistics,
    // SiteSettings,
    // SiteStatistics

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
        // if(this.settingsName==='userManagement'){
        //   this.settingsComName = 'UserManagement'
        // }else if(this.settingsName==='userStatistics'){
        //   this.settingsComName = 'UserStatistics'
        // }else if(this.settingsName==='videoManagement'){
        //   this.settingsComName = 'VideoManagement'
        // }else if(this.settingsName==='videoStatistics'){
        //   this.settingsComName = 'VideoStatistics'
        // }else if(this.settingsName==='consultOrderManagement'){
        //   this.settingsComName = 'ConsultOrderManagement'
        // }else if(this.settingsName==='consultStatistics'){
        //   this.settingsComName = 'ConsultStatistics'
        // }else if(this.settingsName==='paymentOrderManagement'){
        //   this.settingsComName = 'PaymentOrderManagement'
        // }else if(this.settingsName==='paymentStatistics'){
        //   this.settingsComName = 'PaymentStatistics'
        // }else if(this.settingsName==='siteManagement'){
        //   this.settingsComName = 'SiteSettings'
        // }else if(this.settingsName==='siteStatistics'){
        //   this.settingsComName = 'SiteStatistics'
        // }
      },
      immediate: true
    }
  },
  created() {
    console.log(this.$route.meta.component)
    this.settingsName = this.$route.meta.component
    // if(this.settingsName==='userManagement'){
    //   this.settingsComName = 'UserManagement'
    // }else if(this.settingsName==='userStatistics'){
    //   this.settingsComName = 'UserStatistics'
    // }else if(this.settingsName==='videoManagement'){
    //   this.settingsComName = 'VideoManagement'
    // }else if(this.settingsName==='videoStatistics'){
    //   this.settingsComName = 'VideoStatistics'
    // }else if(this.settingsName==='consultOrderManagement'){
    //   this.settingsComName = 'ConsultOrderManagement'
    // }else if(this.settingsName==='consultStatistics'){
    //   this.settingsComName = 'ConsultStatistics'
    // }else if(this.settingsName==='paymentOrderManagement'){
    //   this.settingsComName = 'PaymentOrderManagement'
    // }else if(this.settingsName==='paymentStatistics'){
    //   this.settingsComName = 'PaymentStatistics'
    // }else if(this.settingsName==='siteManagement'){
    //   this.settingsComName = 'SiteSettings'
    // }else if(this.settingsName==='siteStatistics'){
    //   this.settingsComName = 'SiteStatistics'
    // }
  },
  methods: {
    changeCom: function (event) {
      console.log(event)
      if (event === 'userManagement') {
        this.$router.push({name: 'userManagement'});
        // this.$router.push({ name: 'buyerOrders', params: { vid: item.toString() }});
      }else if (event === 'userStatistics') {
        this.$router.push({name: 'userStatistics'});
        // this.$router.push({ name: 'buyerOrders', params: { vid: item.toString() }});
      }else if (event === 'videoManagement') {
        this.$router.push({name: 'videoManagement'});
      }else if (event === 'videoStatistics') {
        this.$router.push({name: 'videoStatistics'});
      }else if (event === 'consultOrderManagement') {
        this.$router.push({name: 'consultOrderManagement'});
      }else if (event === 'consultStatistics') {
        this.$router.push({name: 'consultStatistics'});
      }else if (event === 'paymentOrderManagement') {
        this.$router.push({name: 'paymentOrderManagement'});
      }else if (event === 'paymentStatistics') {
        this.$router.push({name: 'paymentStatistics'});
      }else if (event === 'siteManagement') {
        this.$router.push({name: 'siteManagement'});
      }else if (event === 'siteStatistics') {
        this.$router.push({name: 'siteStatistics'});
      }
    }
  }
}
</script>

<style scoped>

</style>
