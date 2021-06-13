<template>
  <div class="entry-container">
    <div style="display: flex">
      <transition :enter-active-class="entryComName==='LoginCard'?'animate__animated animate__fadeInDown animate__faster':'animate__animated animate__fadeInUp animate__faster'"
                  :leave-active-class="entryComName==='LoginCard'?'animate__animated animate__fadeOutDown animate__faster':'animate__animated animate__fadeOutUp animate__faster'"  mode='out-in'>
        <keep-alive>
    <!--      <login-card  v-if="show"></login-card>-->
    <!--      <register-card  v-else></register-card>-->
        <component @changeCom="changeCom($event)" :is="entryComName"></component>
        </keep-alive>
      </transition>
    </div>
  </div>
</template>

<script>
import LoginCard from '@/views/login'
import RegisterCard from '@/views/register'
export default {
  name: 'entry',
  components: {
    LoginCard,
    RegisterCard
  },
  data () {
    return {
      entryComName: 'LoginCard',
      show: true
    }
  },
  methods: {
    changeCom: function (event) {
      if (event === 'login') {
        this.entryComName = 'LoginCard'
      } else {
        this.entryComName = 'RegisterCard'
      }
    }
  }
}
</script>

<style lang="scss" scoped>

.entry-container {
  min-height: 100%;
  width: 100%;
  display: flex;
  align-items: center; /*定义body的元素垂直居中*/
  justify-content: center; /*定义body的里的元素水平居中*/
}

</style>
