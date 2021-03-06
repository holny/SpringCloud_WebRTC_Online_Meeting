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
      title: '????????????',
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
            // ??? /user/:id/posts ????????????
            // UserPosts ??????????????? User ??? <router-view> ???
            path: 'borders',
            name: 'buyerOrders',
            components: {
              settingsComponent: OrderItems,
            },
            meta: {
              title: '????????????',
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
              title: '????????????',
              component: 'sellerOrders'
            }
          },{
            path: 'tradingrecords',
            name: 'tradingRecords',
            components: {
              settingsComponent: TradingRecords,
            },
            meta: {
              title: '????????????',
              component: 'transactionRecords'
            }
          },{
            path: 'mysubscriptions',
            name: 'mySubscriptions',
            components: {
              settingsComponent: MySubscription,
            },
            meta: {
              title: '????????????',
              component: 'mySubscriptions'
            }
          },{
            path: 'myfavourites',
            name: 'myFavourites',
            components: {
              settingsComponent: MyFavourite,
            },
            meta: {
              title: '????????????',
              component: 'myFavourites'
            }
          },{
            path: 'myvideos',
            name: 'myVideos',
            components: {
              settingsComponent: MyVideos,
            },
            meta: {
              title: '????????????',
              component: 'myVideos'
            },
          },{
            path: 'myfollowers',
            name: 'myFollowers',
            components: {
              settingsComponent: MyFollower,
            },
            meta: {
              title: '????????????',
              component: 'myFollowers'
            }
          },{
            path: 'myworkstat',
            name: 'myWorkStatistics',
            components: {
              settingsComponent: MyWorkStatistics,
            },
            meta: {
              title: '????????????',
              component: 'myWorkStatistics'
            }
          },{
            path: 'newproduct',
            name: 'newProduct',
            components: {
              settingsComponent: InitNewProduct,
            },
            meta: {
              title: '????????????',
              component: 'newProduct'
            },
          },{
            path: 'myinfo',
            name: 'myInfo',
            components: {
              settingsComponent: MyInfoSettings,
            },
            meta: {
              title: '????????????',
              component: 'myInfo'
            }
          },{
            path: 'paymentsettings',
            name: 'myPaymentSettings',
            components: {
              settingsComponent: PaymentSettings,
            },
            meta: {
              title: '????????????',
              component: 'myPaymentSettings'
            }
          },{
            path: 'sellersettings',
            name: 'sellerSettings',
            components: {
              settingsComponent: TradingSettings,
            },
            meta: {
              title: '????????????',
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
              title: 'Admin-????????????',
              component: 'userManagement'
            }
          }, {
            path: 'userstatistics',
            name: 'userStatistics',
            components: {
              adminSettingsComponent: UserStatistics,
            },
            meta: {
              title: 'Admin-??????????????????',
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
              title: 'Admin-????????????',
              component: 'videoManagement'
            }
          }, {
            path: 'videostatistics',
            name: 'videoStatistics',
            components: {
              adminSettingsComponent: VideoStatistics,
            },
            meta: {
              title: 'Admin-??????????????????',
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
              title: 'Admin-????????????',
              component: 'consultOrderManagement'
            }
          }, {
            path: 'consultstatistics',
            name: 'consultStatistics',
            components: {
              adminSettingsComponent: ConsultStatistics,
            },
            meta: {
              title: 'Admin-??????????????????',
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
              title: 'Admin-????????????',
              component: 'paymentOrderManagement'
            }
          }, {
            path: 'paymentstatistics',
            name: 'paymentStatistics',
            components: {
              adminSettingsComponent: PaymentStatistics,
            },
            meta: {
              title: 'Admin-??????????????????',
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
              title: 'Admin-????????????',
              component: 'siteManagement'
            }
          }, {
            path: 'sitestatistics',
            name: 'siteStatistics',
            components: {
              adminSettingsComponent: SiteStatistics,
            },
            meta: {
              title: 'Admin-??????????????????',
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
            // ??? /user/:id/posts ????????????
            // UserPosts ??????????????? User ??? <router-view> ???
            path: ':uid',
            name: 'bookmark',
            components: {
              bookmarkComponent: BookMarkStep,
            },
            meta: {
              title: '????????????',
              sideBarPosition: 'right'
            }
            // props: {
            //   viewMain: true,
            //   viewSidebar: false
            // }
          }
        ]},
      {
        // ??? /user/:id/profile ???????????????
        // UserProfile ??????????????? User ??? <router-view> ???
        path: 'user',
        name: 'userList',
        components: {
          viewMain: UserList
          // viewSidebar: VideoListSideBar
        },
        meta: {
          title: '????????????',
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
          title: '????????????',
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
          title: '????????????',
          // sideBarPosition: 'left'
        }
      }
    ]
  },

  {path: '/video',
    component: () => import('@/views/index'),
    children: [
      {
        // ??? /user/:id/profile ???????????????
        // UserProfile ??????????????? User ??? <router-view> ???
        path: '/',
        name: 'videoList',
        components: {
          viewMain: VideoList
          // viewSidebar: VideoListSideBar
        },
        meta: {
          title: '????????????',
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
          title: '????????????',
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
  //       // ??? /user/:id/profile ???????????????
  //       // UserProfile ??????????????? User ??? <router-view> ???
  //       path: '/',
  //       name: 'chat',
  //       // components: {
  //       //   viewMain: VideoList,
  //       //   viewSidebar: VideoListSideBar
  //       // },
  //       meta: {
  //         title: '????????????',
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

//????????????????????????push??????
const originalPush = VueRouter.prototype.push
//????????????????????????push??????
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)

}
export default router
