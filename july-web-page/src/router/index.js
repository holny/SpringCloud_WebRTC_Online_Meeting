import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)


export const constantRouterMap = [
  // {
  //   path: '',
  //   component: Layout,
  //   redirect: '/index/index',
  //   hidden:true
  // },
  {path: '/login', name: 'login', component: () => import('@/views/login'), hidden: true},
  {path: '/register', name: 'register', component: () => import('@/views/register'), hidden: true},
  {path: '/entry', name: 'entry', component: () => import('@/views/entry'), hidden: true},
  {path: '/wstest', name: 'wstest', component: () => import('@/views/meeting/wstest'), hidden: true},
  {path: '/chatWindow', name: 'chatMessage', component: () => import('@/views/chat/chatWindow'), hidden: true},
  {path: '/contacts', name: 'contacts', component: () => import('@/views/chat/contacts'), hidden: true},
  {path: '/meeting', name: 'meeting', component: () => import('@/views/meeting/meeting'), hidden: true},
  // {path: '/home', name: 'home', component: () => import('@/views/home'), hidden: true},
  {path: '/chat',
    component: () => import('@/views/chat/ChatLand'),
    children: [
      {
        // 当 /user/:id/profile 匹配成功，
        // UserProfile 会被渲染在 User 的 <router-view> 中
        path: '/',
        name: 'chat',
        // components: {
        //   viewMain: VideoList,
        //   viewSidebar: VideoListSideBar
        // },
        meta: {
          title: '在线聊天',
          // sideBarPosition: 'left'
        }
      },
      // {
      //   // 当 /user/:id/posts 匹配成功
      //   // UserPosts 会被渲染在 User 的 <router-view> 中
      //   path: ':vid',
      //   name: 'video',
      //   components: {
      //     viewMain: VideoContent,
      //     viewSidebar: VideoListSideBar
      //   },
      //   meta: {
      //     title: '视频播放',
      //     sideBarPosition: 'right'
      //   },
      //   props: {
      //     viewMain: true,
      //     viewSidebar: false
      //   }
      // }
    ],
    hidden: true}
  // { path: '/404', component: () => import('@/page/errorPage/404'), hidden: true },
  // { path: '/401', component: () => import('@/page/errorPage/401'), hidden: true },
  // {
  //   path: '/index',
  //   name: 'index',
  //   component:Layout,
  //   meta:{
  //     title:'首页',
  //     icon: 'icondashboard',
  //   },
  //   noDropdown:true,
  //   children:[
  //     {
  //       path:'index',
  //       meta:{
  //         title:'首页',
  //         icon:'icondashboard',
  //         routerType:'leftmenu'
  //       },
  //       component: () => import('@/page/index/index'),
  //     }
  //   ]
  // }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: constantRouterMap
})

export default router
