<template>
  <div class="q-pa-md">
    <div class="row">
      <UserHeadBar></UserHeadBar>
    </div>
    <div class="row no-wrap q-mt-md q-gutter-x-lg">
      <div class="column q-gutter-y-lg float-left">
        <div>
          <ConsultSideBar :self-introduction="true" :bar-width="'450'"></ConsultSideBar>
        </div>
      </div>
      <keep-alive>
        <router-view name="bookmarkComponent"></router-view>
      </keep-alive>
<!--      <BookMarkStep></BookMarkStep>-->
    </div>
  </div>
</template>

<script>
import ConsultSideBar from "@/views/common/ConsultSideBar";
import UserHeadBar from "@/views/common/UserHeadBar";
// import BookMarkStep from "@/views/consult/BookMarkStep";
export default {
  name: "BookMark",
  components:{
    ConsultSideBar,
    UserHeadBar,
    // BookMarkStep
  },

}
</script>

<style scoped>

</style>
