import Vue from 'vue'
import VueRouter from 'vue-router'
import VideoList from "@/views/video/VideoList";
import Video from "@/views/video/Video";
import UserList from "@/views/user/UserList";
import Profile from "@/views/user/Profile";
import BookMark from "@/views/consult/BookMark";
import MySetting from "@/views/user/MySetting";
import AdminMain from "@/views/admin/AdminMain";
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
  {path: '/countdown', name: 'countdown', component: () => import('@/views/common/CountTimer'), hidden: true},
  {path: '/sidebar', name: 'sidebar', component: () => import('@/views/common/UserInfoSideBar'), hidden: true},
  {path: '/datetimepicker', name: 'datetimepicker', component: () => import('@/views/common/RangeDateTimePicker'), hidden: true},
  {path: '/settingmenu', name: 'settingmenu', component: () => import('@/views/common/MySettingMenuSideBar'), hidden: true},
  {path: '/orderitem', name: 'orderitem', component: () => import('@/views/trading/OrderItem'), hidden: true},
  {path: '/test', name: 'test', component: () => import('@/views/admin/Test'), hidden: true},
  {path: '/mysetting', name: 'mysetting', component: () => import('@/views/user/MySetting'), hidden: true},
  {path: '/charts', name: 'charts', component: () => import('@/views/charts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('@/views/charts/AllCharts.vue') }
    ], hidden: true},
  {path: '/videoplayer', name: 'videoplayer', component: () => import('@/views/video/Video'), hidden: true},
  {path: '/index', name: 'index', component: () => import('@/views/index'), hidden: true},
  {path: '/my',  component: () => import('@/views/index'),children: [
      {
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'borders',
        name: 'buyerOrders',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '买家订单',
          component: 'buyerOrders'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'sorders',
        name: 'sellerOrders',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '卖家订单',
          component: 'sellerOrders'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'tradingrecords',
        name: 'tradingRecords',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '交易流水',
          component: 'transactionRecords'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'mysubscriptions',
        name: 'mySubscriptions',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '我的订阅',
          component: 'mySubscriptions'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'myfavourites',
        name: 'myFavourites',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '我的收藏',
          component: 'myFavourites'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'myvideos',
        name: 'myVideos',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '我的视频',
          component: 'myVideos'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'myfollowers',
        name: 'myFollowers',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '关注我的',
          component: 'myFollowers'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'myworkstat',
        name: 'myWorkStatistics',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '我的统计',
          component: 'myWorkStatistics'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'newproduct',
        name: 'newProduct',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '新建作品',
          component: 'newProduct'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'myinfo',
        name: 'myInfo',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '我的信息',
          component: 'myInfo'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'paymentsettings',
        name: 'myPaymentSettings',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '支付设置',
          component: 'myPaymentSettings'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      },{
        // 当 /user/:id/posts 匹配成功
        // UserPosts 会被渲染在 User 的 <router-view> 中
        path: 'sellersettings',
        name: 'sellerSettings',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '卖家设置',
          component: 'sellerSettings'
        }
        // props: {
        //   viewMain: true,
        //   viewSidebar: false
        // }
      }
    ], hidden: true},
  {path: '/admin',  component: () => import('@/views/index'),children: [
      {
        path: 'usermanagement',
        name: 'userManagement',
        components: {
          viewMain: AdminMain,
        },
        meta: {
          title: 'Admin-用户管理',
          component: 'userManagement'
        }
      }, {
        path: 'userstatistics',
        name: 'userStatistics',
        components: {
          viewMain: AdminMain,
        },
        meta: {
          title: 'Admin-用户数据统计',
          component: 'userStatistics'
        }
      },
      {
        path: 'videomanagement',
        name: 'videoManagement',
        components: {
          viewMain: AdminMain,
        },
        meta: {
          title: 'Admin-视频管理',
          component: 'videoManagement'
        }
      }, {
        path: 'videostatistics',
        name: 'videoStatistics',
        components: {
          viewMain: AdminMain,
        },
        meta: {
          title: 'Admin-视频数据统计',
          component: 'videoStatistics'
        }
      },
      {
        path: 'consultmanagement',
        name: 'consultOrderManagement',
        components: {
          viewMain: AdminMain,
        },
        meta: {
          title: 'Admin-咨询管理',
          component: 'consultOrderManagement'
        }
      }, {
        path: 'consultstatistics',
        name: 'consultStatistics',
        components: {
          viewMain: AdminMain,
        },
        meta: {
          title: 'Admin-咨询数据统计',
          component: 'consultStatistics'
        }
      },
      {
        path: 'payordermanagement',
        name: 'paymentOrderManagement',
        components: {
          viewMain: AdminMain,
        },
        meta: {
          title: 'Admin-支付管理',
          component: 'paymentOrderManagement'
        }
      }, {
        path: 'paymentstatistics',
        name: 'paymentStatistics',
        components: {
          viewMain: AdminMain,
        },
        meta: {
          title: 'Admin-支付数据统计',
          component: 'paymentStatistics'
        }
      },
      {
        path: 'sitemanagement',
        name: 'siteManagement',
        components: {
          viewMain: AdminMain,
        },
        meta: {
          title: 'Admin-网站管理',
          component: 'siteManagement'
        }
      }, {
        path: 'sitestatistics',
        name: 'siteStatistics',
        components: {
          viewMain: AdminMain,
        },
        meta: {
          title: 'Admin-网站数据统计',
          component: 'siteStatistics'
        }
      }
    ], hidden: true},
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

//获取原型对象上的push函数
const originalPush = VueRouter.prototype.push
//修改原型对象中的push方法
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)

}
export default router
