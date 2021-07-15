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
  { path: '/', redirect: '/video' },
  {path: '/entry', name: 'entry',meta: {
      title: '欢迎登陆',
    }, component: () => import('@/views/entry'), hidden: true},
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
        path: 'sorders',
        name: 'sellerOrders',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '卖家订单',
          component: 'sellerOrders'
        }
      },{
        path: 'tradingrecords',
        name: 'tradingRecords',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '交易流水',
          component: 'transactionRecords'
        }
      },{
        path: 'mysubscriptions',
        name: 'mySubscriptions',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '我的订阅',
          component: 'mySubscriptions'
        }
      },{
        path: 'myfavourites',
        name: 'myFavourites',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '我的收藏',
          component: 'myFavourites'
        }
      },{
        path: 'myvideos',
        name: 'myVideos',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '我的视频',
          component: 'myVideos'
        }
      },{
        path: 'myfollowers',
        name: 'myFollowers',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '关注我的',
          component: 'myFollowers'
        }
      },{
        path: 'myworkstat',
        name: 'myWorkStatistics',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '我的统计',
          component: 'myWorkStatistics'
        }
      },{
        path: 'newproduct',
        name: 'newProduct',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '新建作品',
          component: 'newProduct'
        }
      },{
        path: 'myinfo',
        name: 'myInfo',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '我的信息',
          component: 'myInfo'
        }
      },{
        path: 'paymentsettings',
        name: 'myPaymentSettings',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '支付设置',
          component: 'myPaymentSettings'
        }
      },{
        path: 'sellersettings',
        name: 'sellerSettings',
        components: {
          viewMain: MySetting,
        },
        meta: {
          title: '卖家设置',
          component: 'sellerSettings'
        }
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
        path: '/user/:uid',
        name: 'profile',
        components: {
          viewMain: Profile,
        },
        meta: {
          title: '用户信息',
          sideBarPosition: 'right'
        }
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
        path: '/video/:vid',
        name: 'video',
        components: {
          viewMain: Video,
        },
        meta: {
          title: '视频播放',
          sideBarPosition: 'right'
        }
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
    ],
    hidden: true}
  // { path: '/404', component: () => import('@/page/errorPage/404'), hidden: true },
  // { path: '/401', component: () => import('@/page/errorPage/401'), hidden: true },
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
