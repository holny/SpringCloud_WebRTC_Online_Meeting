import Vue from 'vue'
import VueRouter from 'vue-router'
import VideoList from "@/views/video/VideoList";
import Video from "@/views/video/Video";
import UserList from "@/views/user/UserList";
import Profile from "@/views/user/Profile";
import BookMark from "@/views/consult/BookMark";
import MySetting from "@/views/user/MySetting";
import AdminMain from "@/views/admin/AdminMain";
import ChatLand from "@/views/chat/ChatLand";

import OrderItems from "@/views/trading/OrderItems";
import TradingRecords from "@/views/trading/TradingRecords";
import MySubscription from "@/views/user/MySubscription";
import MyFavourite from "@/views/user/MyFavourite";
import MyVideos from "@/views/user/MyVideos";
// import NewVideo from "@/views/video/NewVideo";
import MyFollower from "@/views/user/MyFollower";
import MyWorkStatistics from "@/views/user/MyWorkStatistics";
import MyInfoSettings from "@/views/user/MyInfoSettings";
import PaymentSettings from "@/views/trading/PaymentSettings";
import TradingSettings from "@/views/trading/TradingSettings";
import UserManagement from "@/views/admin/UserManagement";
import UserStatistics from "@/views/admin/UserStatistics";
import VideoManagement from "@/views/admin/VideoManagement";
import VideoStatistics from "@/views/admin/VideoStatistics";
import ConsultOrderManagement from "@/views/admin/ConsultOrderManagement";
import ConsultStatistics from "@/views/admin/ConsultStatistics";
import PaymentOrderManagement from "@/views/admin/PaymentOrderManagement";
import PaymentStatistics from "@/views/admin/PaymentStatistics";
import SiteSettings from "@/views/admin/SiteSettings";
import SiteStatistics from "@/views/admin/SiteStatistics";
import InitNewProduct from "@/views/video/InitNewProduct";
import BookMarkStep from "@/views/consult/BookMarkStep";
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
  { path: '/',
    component:() => import('@/views/index'),
    hidden:false,
    children: [
      {
        path: 'my',
        hidden:false,
        components:{ viewMain: MySetting},
        children: [
          {
            // 当 /user/:id/posts 匹配成功
            // UserPosts 会被渲染在 User 的 <router-view> 中
            path: 'borders',
            name: 'buyerOrders',
            components: {
              settingsComponent: OrderItems,
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
              settingsComponent: OrderItems,
            },
            meta: {
              title: '卖家订单',
              component: 'sellerOrders'
            }
          },{
            path: 'tradingrecords',
            name: 'tradingRecords',
            components: {
              settingsComponent: TradingRecords,
            },
            meta: {
              title: '交易流水',
              component: 'transactionRecords'
            }
          },{
            path: 'mysubscriptions',
            name: 'mySubscriptions',
            components: {
              settingsComponent: MySubscription,
            },
            meta: {
              title: '我的订阅',
              component: 'mySubscriptions'
            }
          },{
            path: 'myfavourites',
            name: 'myFavourites',
            components: {
              settingsComponent: MyFavourite,
            },
            meta: {
              title: '我的收藏',
              component: 'myFavourites'
            }
          },{
            path: 'myvideos',
            name: 'myVideos',
            components: {
              settingsComponent: MyVideos,
            },
            meta: {
              title: '我的视频',
              component: 'myVideos'
            },
          },{
            path: 'myfollowers',
            name: 'myFollowers',
            components: {
              settingsComponent: MyFollower,
            },
            meta: {
              title: '关注我的',
              component: 'myFollowers'
            }
          },{
            path: 'myworkstat',
            name: 'myWorkStatistics',
            components: {
              settingsComponent: MyWorkStatistics,
            },
            meta: {
              title: '我的统计',
              component: 'myWorkStatistics'
            }
          },{
            path: 'newproduct',
            name: 'newProduct',
            components: {
              settingsComponent: InitNewProduct,
            },
            meta: {
              title: '新建作品',
              component: 'newProduct'
            },
          },{
            path: 'myinfo',
            name: 'myInfo',
            components: {
              settingsComponent: MyInfoSettings,
            },
            meta: {
              title: '我的信息',
              component: 'myInfo'
            }
          },{
            path: 'paymentsettings',
            name: 'myPaymentSettings',
            components: {
              settingsComponent: PaymentSettings,
            },
            meta: {
              title: '支付设置',
              component: 'myPaymentSettings'
            }
          },{
            path: 'sellersettings',
            name: 'sellerSettings',
            components: {
              settingsComponent: TradingSettings,
            },
            meta: {
              title: '卖家设置',
              component: 'sellerSettings'
            }
          }
        ]
      },
      {
        path: 'admin',
        components:{ viewMain: AdminMain},
        hidden:false,
        children: [
          {
            path: 'usermanagement',
            name: 'userManagement',
            components: {
              adminSettingsComponent: UserManagement,
            },
            meta: {
              title: 'Admin-用户管理',
              component: 'userManagement'
            }
          }, {
            path: 'userstatistics',
            name: 'userStatistics',
            components: {
              adminSettingsComponent: UserStatistics,
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
              adminSettingsComponent: VideoManagement,
            },
            meta: {
              title: 'Admin-视频管理',
              component: 'videoManagement'
            }
          }, {
            path: 'videostatistics',
            name: 'videoStatistics',
            components: {
              adminSettingsComponent: VideoStatistics,
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
              adminSettingsComponent: ConsultOrderManagement,
            },
            meta: {
              title: 'Admin-咨询管理',
              component: 'consultOrderManagement'
            }
          }, {
            path: 'consultstatistics',
            name: 'consultStatistics',
            components: {
              adminSettingsComponent: ConsultStatistics,
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
              adminSettingsComponent: PaymentOrderManagement,
            },
            meta: {
              title: 'Admin-支付管理',
              component: 'paymentOrderManagement'
            }
          }, {
            path: 'paymentstatistics',
            name: 'paymentStatistics',
            components: {
              adminSettingsComponent: PaymentStatistics,
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
              adminSettingsComponent: SiteSettings,
            },
            meta: {
              title: 'Admin-网站管理',
              component: 'siteManagement'
            }
          }, {
            path: 'sitestatistics',
            name: 'siteStatistics',
            components: {
              adminSettingsComponent: SiteStatistics,
            },
            meta: {
              title: 'Admin-网站数据统计',
              component: 'siteStatistics'
            }
          }
        ]
      },
      {
        path: 'bookmark',
        components: {
          viewMain: BookMark,
        },
        hidden: true,
        children: [
          {
            // 当 /user/:id/posts 匹配成功
            // UserPosts 会被渲染在 User 的 <router-view> 中
            path: ':uid',
            name: 'bookmark',
            components: {
              bookmarkComponent: BookMarkStep,
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
        ]},
      {
        // 当 /user/:id/profile 匹配成功，
        // UserProfile 会被渲染在 User 的 <router-view> 中
        path: 'user',
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
        path: 'user/:uid',
        name: 'profile',
        components: {
          viewMain: Profile,
        },
        meta: {
          title: '用户信息',
          sideBarPosition: 'right'
        }
      },
      {
        path: 'chat',
        name: 'chat',
        components: {
          viewMain: ChatLand,
        },
        hidden: true,
        meta: {
          title: '在线聊天',
          // sideBarPosition: 'left'
        }
      }
    ]
  },

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
        path: ':videoId',
        name: 'video',
        components: {
          viewMain: Video,
        },
        meta: {
          title: '视频播放',
          sideBarPosition: 'right'
        },
        props: true
      }
    ],
    hidden: true},
  // {path: '/home', name: 'home', component: () => import('@/views/home'), hidden: true},
  // {path: '/chat',
  //   component: () => import('@/views/chat/ChatLand'),
  //   children: [
  //     {
  //       // 当 /user/:id/profile 匹配成功，
  //       // UserProfile 会被渲染在 User 的 <router-view> 中
  //       path: '/',
  //       name: 'chat',
  //       // components: {
  //       //   viewMain: VideoList,
  //       //   viewSidebar: VideoListSideBar
  //       // },
  //       meta: {
  //         title: '在线聊天',
  //         // sideBarPosition: 'left'
  //       }
  //     },
  //   ],
  //   hidden: true}
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
