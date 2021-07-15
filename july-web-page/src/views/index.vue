<template>
  <q-layout view="lHh Lpr fff"  style="min-width: 0">
    <q-header elevated class="bg-white text-grey-8 bg-grey-1" height-hint="64">
      <q-toolbar class="GPL__toolbar" style="height: 64px">
        <q-btn
            flat
            dense
            round
            @click="toggleLeftDrawer"
            aria-label="Menu"
            icon="menu"
            class="q-mx-md"
        />

        <q-toolbar-title v-if="$q.screen.gt.sm" shrink class="row items-center no-wrap">
          <img src="">
          <span class="q-ml-sm">July</span>
        </q-toolbar-title>

        <q-space />

        <q-input class="GPL__toolbar-input" dense standout="bg-primary" v-model="search" placeholder="搜索">
          <template v-slot:prepend>
            <q-icon v-if="search === ''" name="search" />
            <q-icon v-else name="clear" class="cursor-pointer" @click="search = ''" />
          </template>
        </q-input>

<!--        <q-btn v-if="$q.screen.gt.xs" flat dense no-wrap color="primary" icon="add" no-caps label="Create" class="q-ml-sm q-px-md">-->
<!--          <q-menu anchor="top end" self="top end">-->
<!--            <q-list class="text-grey-8" style="min-width: 100px">-->
<!--              <q-item aria-hidden="true">-->
<!--                <q-item-section class="text-uppercase text-grey-7" style="font-size: 0.7rem">Create New</q-item-section>-->
<!--              </q-item>-->
<!--              <q-item v-for="menu in createMenu" :key="menu.text" clickable v-close-popup aria-hidden="true">-->
<!--                <q-item-section avatar>-->
<!--                  <q-icon :name="menu.icon" />-->
<!--                </q-item-section>-->
<!--                <q-item-section>{{ menu.text }}</q-item-section>-->
<!--              </q-item>-->
<!--            </q-list>-->
<!--          </q-menu>-->
<!--        </q-btn>-->

<!--        <q-btn v-if="$q.screen.gt.xs" flat dense no-wrap color="primary" icon="cloud_upload" no-caps label="Upload" class="q-ml-sm q-px-md" />-->

        <q-space />

        <div class="q-gutter-sm row items-center no-wrap">
          <q-btn round dense flat color="grey-8" icon="notifications">
            <q-badge v-if="notification.unRead!==null&&notification.unRead.count>0" color="red" text-color="white" floating>
              {{notification.unRead.count}}
            </q-badge>
            <q-tooltip v-if="notification.lastMsg!==null&&notification.unRead.count>0&&notification.lastMsg.peerId!=null">
              收到来自{{notification.lastMsg.remarkName!=null?notification.lastMsg.remarkName+'('+(notification.lastMsg.peerNickName!=null)?notification.lastMsg.peerNickName+'('+notification.lastMsg.peerUserName+')':notification.lastMsg.peerUserName+')':(notification.lastMsg.peerNickName!=null)?notification.lastMsg.peerNickName+'('+notification.lastMsg.peerUserName+')':notification.lastMsg.peerUserName}}的新消息
              <br> 对方系统角色:<strong class="text-deep-orange">{{'['+notification.lastMsg.peerRole+']'}}</strong>
              <br> 时间:{{notification.lastMsg.msgGMT}}</q-tooltip>
          </q-btn>
          <q-btn v-if="hostInfo!=null" round flat color="primary">
            <q-avatar size="26px">
              <img :src="hostInfo.avatar!=null?hostInfo.avatar:''">
            </q-avatar>
            <q-tooltip class="text-center">用户名: {{ (hostInfo.nickName!=null&&hostInfo.nickName!=='')?hostInfo.nickName+'('+hostInfo.userName+')':hostInfo.userName}}<br>
              系统角色:<strong class="text-deep-orange">{{'['+hostInfo.role+']'}}</strong></q-tooltip>
            <q-menu
                transition-show="jump-down"
                transition-hide="jump-up"
            >
              <q-list >
                <q-item clickable>
                  <q-item-section>Having fun</q-item-section>
                </q-item>
                <q-item clickable>
                  <q-item-section>Crazy for transitions</q-item-section>
                </q-item>
                <q-separator />
                <q-item clickable>
                  <q-item-section @click="logout">注销</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
          <q-btn v-else icon="login" color="positive"  @click="$router.push({ name: 'entry'});"  flat round dense>
            <q-tooltip>点击登录/注册</q-tooltip>
          </q-btn>
        </div>
      </q-toolbar>
    </q-header>

    <q-drawer
        v-model="leftDrawerOpen"
        bordered
        behavior="mobile"
        @click="leftDrawerOpen = false"
    >
      <q-scroll-area class="fit">
        <q-toolbar class="GPL__toolbar">
          <q-toolbar-title class="row items-center text-grey-8">
<!--            <img class="q-pl-md" src="https://www.gstatic.com/images/branding/googlelogo/svg/googlelogo_clr_74x24px.svg">-->
            <span class="q-ml-sm">July</span>
          </q-toolbar-title>
        </q-toolbar>

        <q-list padding>
          <q-item v-for="link in links1" :key="link.text" clickable class="GPL__drawer-item">
            <q-item-section avatar>
              <q-icon :name="link.icon" @click="$router.push({ name: link.routerName});" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ link.text }}</q-item-label>
            </q-item-section>
          </q-item>

          <q-separator class="q-my-md" />

          <q-item v-for="link in links2" :key="link.text" clickable class="GPL__drawer-item">
            <q-item-section avatar>
              <q-icon :name="link.icon" @click="$router.push({ name: link.routerName});"/>
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ link.text }}</q-item-label>
            </q-item-section>
          </q-item>

          <q-separator class="q-my-md" />

          <q-item v-for="link in links3" :key="link.text" clickable class="GPL__drawer-item">
            <q-item-section avatar>
              <q-icon :name="link.icon" @click="$router.push({ name: link.routerName});" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ link.text }}</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </q-scroll-area>
    </q-drawer>

    <q-page-container class="GPL__page-container">
      <div class="full-width">
        <router-view class="q-mt-md no-wrap JULY__container" name="viewMain" style="margin: 0 auto;min-width: 1400px"></router-view>
      </div>

      <q-page-sticky v-if="$q.screen.gt.sm" expand position="left">
        <div class="fit q-pt-xl q-px-sm column">
          <q-btn round flat color="grey-8" stack no-caps size="26px" class="GPL__side-btn" @click="$router.push({ name: 'videoList'},onComplete => {},onAbort => {});">
            <q-icon size="22px" name="video_library" />
            <div class="GPL__side-btn__label">视频广场</div>
          </q-btn>

          <q-btn round flat color="grey-8" stack no-caps size="26px" class="GPL__side-btn" @click="$router.push({ name: 'userList'},onComplete => {},onAbort => {});" >
            <q-icon size="22px" name="people"/>
            <div class="GPL__side-btn__label">专家列表</div>
          </q-btn>


        </div>
      </q-page-sticky>
    </q-page-container>
  </q-layout>
</template>

<script>

import { getHostInfo,getToken} from "@/utils/auth";
import {closeConnectionJuly, initJulyWS} from "@/utils/socket";
import {FUN, JULY} from "@/utils/julyCommon";
import {CONSTANT, EVENT_CODE,RESULT_CODE} from "@/utils/constant";
import { date } from 'quasar'
export default {
  name: 'GooglePhotosLayout',
  props: {
    redirectURI: {
      type: String,
      require: false,
      default: ''
    },
  },
  data () {
    return {
      hostInfo: getHostInfo(),
      leftDrawerOpen: false,
      search: '',
      links1: [
        { icon: 'video_library', text: '视频广场', routerName:'videoList' },

      ],
      links2: [
        { icon: 'people', text: '用户列表', routerName:'userList'  },
      ],
      links3: [
        { icon: 'question_answer', text: '在线会话' , routerName:'chat' },
      ],
      julyWebsocket:{
        constant: {
          stompClient: null
        },
        variable: {
          connectionFlag: {'websocket':{'name':'websocket',"status":false}}
        },
      },
      hostStatusObject:{
        userId: this.hostInfo!=null?this.hostInfo.userId:null,
        status: CONSTANT.USER_ACTIVE_STATUS_ONLY_MESSAGE,
      },
      notification:{
        unRead: {
          count:0,
          gmtCreat:null,
        },
        lastMsg:{
          peerId: null,
          peerType: null,
          peerUserName: null,
          remarkName: null,
          peerAvatar: null,
          peerNickName: null,
          peerRole: null,
          msgGMT: null,
        }
      },
      getAllUnreadCountTimer: null,

    }
  },
  computed: {
    sideBarPosition () {
      return this.$route.meta.sideBarPosition
    },
    receiveEventNotifyURI() {
      return '/user/topic/notify/' + this.hostInfo.userId
    },
    subscribeUnReadCountURI() {
      return '/app/unreadcount/' + this.hostInfo.userId
    },
  },
  async mounted() {
    if(this.hostInfo){
      console.log(this.hostInfo.userId)
      this.hostInfo.role = FUN.filterPrintRole(this.hostInfo.role)
      console.log(getToken())
      this.hostStatusObject.userId = this.hostInfo.userId
      await this.initWSEnv()
    }
  },
  destroyed() {
    closeConnectionJuly(this.julyWebsocket.constant.stompClient)
    clearInterval(this.getAllUnreadCountTimer)
  },
  methods: {
    toggleLeftDrawer () {
      this.leftDrawerOpen = !this.leftDrawerOpen
    },
    async initWSEnv(){
      if (this.julyWebsocket.constant.stompClient == null&&getToken()!==null) {
        this.julyWebsocket.constant.stompClient = await initJulyWS(JULY.WEBSOCKET_URI_ENDPOINT, JULY.WEBSOCKET_URI_SEND_HEARTBEAT,JULY.WEBSOCKET_URI_SEND_HEARTBEAT_INTERVAL,this.hostStatusObject)
      }
      this.initWSConnectionHeartBeatWatcher()
    },

    initWSConnectionHeartBeatWatcher(){
      let _that = this
      this.julyWebsocket.constant.stompClient.connect(
          JULY.WEBSOCKET_HEADERS,
          function connectCallback () {
            _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
            _that.isSocketReady = true
            _that.initNotificationWSListener()
            // 订阅stompClient心跳检测
            _that.julyWebsocket.constant.stompClient.subscribe(JULY.WEBSOCKET_URI_SUBSCRIBE_HEARTBEAT, () => {
              _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
            })
            _that.subscribeAllUnreadCount(_that)
          },
          function errorCallBack () {
            _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = false
            FUN.notify("初始连接websocket失败",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
          }
      )
    },
    subscribeAllUnreadCount(_that){
      console.log('subscribeAllUnreadCount')
      // 定时获取host当前未读消息总数
      clearInterval(_that.getAllUnreadCountTimer)
      _that.getAllUnreadCountTimer = setInterval(() => {
        _that.julyWebsocket.constant.stompClient.subscribe(_that.subscribeUnReadCountURI, (response) => {
          _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
          let feedback = JSON.parse(response.body)
          if (feedback.code=== RESULT_CODE.SUCCESS){
            let feedbackTS = date.formatDate(feedback.data.gmtCreate , 'x')
            if(_that.notification.unRead.gmtCreat!=null){
              if(feedbackTS<_that.notification.unRead.gmtCreat){
                return
              }
            }
            _that.notification.unRead.count=feedback.data.allUnreadCount
          }

        })
      }, JULY.WEBSOCKET_URI_GET_ALL_UNREAD_COUNT_INTERVAL)
    },
    initNotificationWSListener(){
      console.log("initEventNotifyWatcher")
      let _that = this
      _that.julyWebsocket.constant.stompClient.subscribe(_that.receiveEventNotifyURI, (response) => {
        console.log("收到event")
        _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
        let feedback = JSON.parse(response.body)
        console.log(feedback)
        if (feedback.type=== CONSTANT.SHOUTING_EVENT){
          if (feedback.code===EVENT_CODE.EVENT_NEW_MESSAGE_NOTIFY){
            let feedbackCreateTS = date.formatDate(feedback.data.gmtCreate , 'x')
            if(_that.notification.unRead.gmtCreat!=null){
              if(feedbackCreateTS>_that.notification.unRead.gmtCreat){
                _that.notification.unRead.count=feedback.data.allUnreadCount
              }
            }else{
              _that.notification.unRead.count=feedback.data.allUnreadCount
            }

            let feedbackMsgTS = date.formatDate(feedback.data.newMsg.msgGMT , 'x')
            console.log(feedbackMsgTS)
            let feedbackMsg = date.formatDate(feedback.data.newMsg.msgGMT , CONSTANT.DATE_FORMAT)
            if(_that.notification.lastMsg.msgGMT!=null){
              let comparerTS = date.formatDate(date.extractDate(_that.notification.lastMsg.msgGMT, CONSTANT.DATE_FORMAT) , 'x')
              console.log(comparerTS)
              if(feedbackMsgTS>comparerTS){
                _that.notification.lastMsg = feedback.data.newMsg
                _that.notification.lastMsg.msgGMT = feedbackMsg
                _that.notification.lastMsg.peerRole = FUN.filterPrintRole( _that.notification.lastMsg.peerRole)
                  // console.log( feedback.data.newMsg.msgGMT)
                  // console.log( feedback.data.newMsg.userName)
                  // console.log( feedback.data.newMsg.remarkName)
                  // console.log( feedback.data.newMsg.nickName)
                  // console.log(_that.notification.lastMsg.msgGMT)
                  // console.log(_that.notification.lastMsg.remarkName)
                  console.log(_that.notification.lastMsg.peerUserName)
                console.log(_that.notification.lastMsg.peerRole)
                  // console.log(_that.notification.lastMsg.peerNickName)
              }
            }else{
              _that.notification.lastMsg = feedback.data.newMsg
              _that.notification.lastMsg.msgGMT = feedbackMsg
            }
          }
        }
      })
    },
    async logout(){
      console.log("logout")
      await this.$store.dispatch('user/logout')
      this.hostInfo = null
      this.hostStatusObject.hostId = null
      this.notification.unRead = null
      this.notification.lastMsg = null
      closeConnectionJuly(this.julyWebsocket.constant.stompClient)
      clearInterval(this.getAllUnreadCountTimer)
    }
  }
}
</script>

<style lang="sass">
.JULY
  @media (min-width: 0px)
    &__container
      width: 100%
  @media (min-width: 600px)
    &__container
      width: 95%
  @media (min-width: 1024px)
    &__container
      width: 90%
  @media (min-width: 1440px)
    &__container
      width: 85%
  @media (min-width: 1920px)
    &__container
      width: 75%
.GPL
  &__toolbar
    height: 64px
  &__toolbar-input
    width: 35%
  &__drawer-item
    line-height: 24px
    border-radius: 0 24px 24px 0
    margin-right: 12px
    .q-item__section--avatar
      padding-left: 12px
      .q-icon
        color: #5f6368
    .q-item__label:not(.q-item__label--caption)
      color: #3c4043
      letter-spacing: .01785714em
      font-size: .875rem
      font-weight: 500
      line-height: 1.25rem
    &--storage
      border-radius: 0
      margin-right: 0
      padding-top: 24px
      padding-bottom: 24px
  &__side-btn
    &__label
      font-size: 12px
      line-height: 24px
      letter-spacing: .01785714em
      font-weight: 500
  @media (min-width: 1024px)
    &__page-container
      padding-left: 94px

</style>
