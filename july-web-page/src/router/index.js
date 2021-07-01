import Vue from 'vue'
import VueRouter from 'vue-router'
import VideoList from "@/views/video/VideoList";
import Video from "@/views/video/Video";
import UserList from "@/views/user/UserList";
import Profile from "@/views/user/Profile";
import BookMark from "@/views/consult/BookMark";
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
  {path: '/videoitem', name: 'videoitem', component: () => import('@/views/video/VideoItem'), hidden: true},
  {path: '/useritem', name: 'useritem', component: () => import('@/views/user/UserItem'), hidden: true},
  // {path: '/videolist', name: 'videolist', component: () => import('@/views/video/VideoList'), hidden: true},
  {path: '/bookmarkcard', name: 'bookmarkcard', component: () => import('@/views/consult/BookMarkStep'), hidden: true},
  {path: '/sidebar', name: 'sidebar', component: () => import('@/views/common/UserInfoSideBar'), hidden: true},
  {path: '/datetimepicker', name: 'datetimepicker', component: () => import('@/views/common/RangeDateTimePicker'), hidden: true},
  {path: '/charts', name: 'charts', component: () => import('@/views/charts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('@/views/charts/AllCharts.vue') }
    ], hidden: true},
  {path: '/videoplayer', name: 'videoplayer', component: () => import('@/views/video/Video'), hidden: true},
  {path: '/index', name: 'index', component: () => import('@/views/index'), hidden: true},
  {path: '/bookmark',  component: () => import('@/views/index'),children: [
      {
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: '/bookmark/:uid',
        name: 'bookmark',
        components: {
          viewMain: BookMark,
        },
        meta: {
          title: '预约咨询',
          sideBarPosition: 'right'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      }
    ], hidden: true},
  {path: '/user',
    component: () => import('@/views/index'),
    children: [
      {
        // 当 /user/:id/profile 匹配成功，
        // UserProfile 会被渲染在 User 的 <router-view> 中
        path: '/',
        name: 'userList',
        components: {
          viewMain: UserList
          // viewSidebar: VideoListSideBar
        },
        meta: {
          title: '用户列表',
          sideBarPosition: 'left'
        }
      },
      {
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: '/user/:uid',
        name: 'profile',
        components: {
          viewMain: Profile,
        },
        meta: {
          title: '用户信息',
          sideBarPosition: 'right'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      }
    ],
    hidden: true},
  {path: '/video',
    component: () => import('@/views/index'),
    children: [
      {
        // 当 /user/:id/profile 匹配成功，
        // UserProfile 会被渲染在 User 的 <router-view> 中
        path: '/',
        name: 'videoList',
        components: {
          viewMain: VideoList
          // viewSidebar: VideoListSideBar
        },
        meta: {
          title: '视频列表',
          sideBarPosition: 'left'
        }
      },
      {
          // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: '/video/:vid',
        name: 'video',
        components: {
          viewMain: Video,
        },
        meta: {
          title: '视频播放',
          sideBarPosition: 'right'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      }
    ],
    hidden: true},
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
