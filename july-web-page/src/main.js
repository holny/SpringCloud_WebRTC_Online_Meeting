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
import {getHostInfo} from "@/utils/auth";

Vue.prototype.$echarts = echarts
Vue.prototype.$video = Video
Vue.prototype.instance = request
Vue.use(animated)
Vue.config.productionTip = false
// const whiteList = ['/entry', '/auth-redirect'] // no redirect whitelist

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  if (to.path === '/entry'&&!to.query.redirect) {
    to.query.redirect = from.fullPath
  }
  next()
  // determine whether the user has logged in
  const hostInfo = getHostInfo()

  if (hostInfo) {
    if (to.path === '/entry') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
    } else {
      // // determine whether the user has obtained his permission roles through getInfo
      // const hasRoles = store.getters.roles && store.getters.roles.length > 0
      // if (hasRoles) {
      //   next()
      // } else {
      //   try {
      //     // get user info
      //     // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
      //     const { roles } = await store.dispatch('user/getInfo')
      //
      //     // generate accessible routes map based on roles
      //     const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
      //
      //     // dynamically add accessible routes
      //     router.addRoutes(accessRoutes)
      //
      //     // hack method to ensure that addRoutes is complete
      //     // set the replace: true, so the navigation will not leave a history record
      //     next({ ...to, replace: true })
      //   } catch (error) {
      //     // remove token and go to login page to re-login
      //     await store.dispatch('user/resetToken')
      //     Message.error(error || 'Has Error')
      //     next(`/login?redirect=${to.path}`)
      //     NProgress.done()
      //   }
      // }
      next()
    }
  } else {
    /* has no token*/
    //
    // if (whiteList.indexOf(to.path) !== -1) {
    //   // in the free login whitelist, go directly
    //   next()
    // } else {
    //   // other pages that do not have permission to access are redirected to the login page.
    //   next(`/login?redirect=${to.path}`)
    // }
    next()
  }
})
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
