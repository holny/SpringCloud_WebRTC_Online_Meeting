import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './quasar'
import request from '@/utils/request'
import animated from 'animate.css'
import Video from 'video.js'
import 'video.js/dist/video-js.css'
import * as echarts from 'echarts'
Vue.prototype.$echarts = echarts
Vue.prototype.$video = Video
Vue.prototype.instance = request
Vue.use(animated)
Vue.config.productionTip = false
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
