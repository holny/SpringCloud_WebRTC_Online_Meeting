<template>
  <div class="contacts justify-center">
  <q-card class="my-card" flat bordered>
    <q-card-section horizontal style="height: 100px">
      <q-img class="col" native-context-menu src="https://cdn.quasar.dev/img/mountains.jpg" >

        <div class="fit">
          <q-item>
            <q-item-section avatar>
              <q-avatar>
                <img :src="hostInfo!=null?hostInfo.avatar:''">
              </q-avatar>
            </q-item-section>
            <q-item-section>
              <q-item-label>{{hostInfo!=null?hostInfo.userName:'Unknown'}}{{hostInfo!=null?(hostInfo.nickName!=null?'('+hostInfo.nickName+')':''):''}}</q-item-label>
              <q-item-label caption class="text-white">{{hostInfo!=null?hostInfo.role:''}}</q-item-label>
            </q-item-section>
          </q-item>
          <q-icon v-if="julyWebsocket.variable.connectionFlag['websocket']['status']===true" class="absolute-top-right q-mr-md q-mt-xs" size="sm" color="green" name="link" >
            <q-tooltip>
              <strong >当前前WebSocket已连接</strong>
            </q-tooltip>
          </q-icon>
          <q-icon v-else class="absolute-top-right q-mr-md q-mt-xs" size="sm" color="negative" name="link_off" >
            <q-tooltip>
              <strong >当前前WebSocket未连接</strong>
            </q-tooltip>
          </q-icon>
        </div>
      </q-img>
    </q-card-section>
    <q-card-section class="q-pt-sm q-pb-sm" style="height: 50px">
      <q-select
          label="搜一搜"
          :dense="true"
          rounded outlined
          v-model="searchSelectUserInfo"
          use-input
          hide-dropdown-icon
          clearable
          input-debounce="500"
          :options="options"
          @filter="filterFn"
          style="width: 100%"
          :display-value="searchSelectUserInfo?searchInputDisplayVal:null"
      >
        <template v-slot:option="scope">
          <q-item @click.native="onClickSearchSelection"
              v-bind="scope.itemProps"
              v-on="scope.itemEvents"
          >
            <q-item-section avatar>
              <q-avatar>
                <img :src="scope.opt.avatar">
              </q-avatar>
            </q-item-section>
            <q-item-section>
              <q-item-label v-html="scope.opt.userName" />
              <q-item-label caption>{{ scope.opt.userId}}</q-item-label>
            </q-item-section>
          </q-item>
        </template>
      </q-select>
    </q-card-section>
    <q-card-section :style="{height: contactsWindowHeight}"  horizontal>

      <q-card-actions vertical class="justify-start">
        <q-btn flat round color="accent" icon="bookmark"  @click="contactType='recent'">
          <q-tooltip transition-show="scale" transition-hide="scale" >
            最近聊天
          </q-tooltip>
        </q-btn>
        <q-btn flat round color="red" icon="favorite"  @click="contactType='bookmark'">
          <q-tooltip transition-show="scale" transition-hide="scale" >
            联系人
          </q-tooltip>
        </q-btn>
        <q-btn flat round color="primary" icon="groups" @click="contactType='group'">
          <q-tooltip transition-show="scale" transition-hide="scale" >
            聊天室
          </q-tooltip>
        </q-btn>
      </q-card-actions>
      <q-separator color="orange"  vertical inset />
      <q-list v-if="contactType==='recent'" class="full-width">
        <q-scroll-area class="full-height full-width">
          <contact-item v-for="relationObject in contacts.variable.recent.array" :key="relationObject.peerId" :peer-object="relationObject" v-on:changePeer="changeChatPeer" v-on:addBookmarker="addBookMarkPeer" v-on:removeUserRelation="removeUserRelation" v-on:updateUserRelationType="updateUserRelationType"/>
        </q-scroll-area>
      </q-list>
      <q-list v-else-if="contactType==='bookmark'" class="full-width">
        <q-scroll-area class="full-height full-width">
          <contact-item v-for="relationObject in contacts.variable.bookMark.array" :key="relationObject.peerId" :peer-object="relationObject" v-on:changePeer="changeChatPeer" v-on:addBookmarker="addBookMarkPeer" v-on:removeUserRelation="removeUserRelation" v-on:updateUserRelationType="updateUserRelationType"/>
        </q-scroll-area>
      </q-list>
      <q-list v-else-if="contactType==='group'" class="full-width">
        <q-scroll-area class="full-height full-width">
          <contact-item v-for="relationObject in contacts.variable.group.array" :key="relationObject.peerId" :peer-object="relationObject" v-on:changePeer="changeChatPeer" v-on:addBookmarker="addBookMarkPeer" v-on:removeUserRelation="removeUserRelation" v-on:updateUserRelationType="updateUserRelationType"/>
        </q-scroll-area>
      </q-list>
      <q-space/>
    </q-card-section>
  </q-card>
    <q-dialog v-model="userInfoDialogShow" ref="userInfoDialog" @hide="userInfoDialogBeforeHide">
      <q-card class="my-card" style="height: auto">
        <q-card-section class="row items-center q-pb-none">
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>

        <q-item>
          <q-item-section avatar>
            <q-avatar>
              <img :src="candidateUserInfo.avatar">
            </q-avatar>
          </q-item-section>

          <q-item-section>
            <q-item-label>{{this.candidateUserInfo.userName}}{{this.candidateUserInfo.nickName!=null&&this.candidateUserInfo.nickName!=''?'('+this.candidateUserInfo.nickName+')':''}}</q-item-label>
            <q-item-label caption>
              {{this.candidateUserInfo.role}}
            </q-item-label>
          </q-item-section>
        </q-item>
        <q-separator />
        <q-card-section>
          <q-btn
              fab
              color="primary"
              icon="place"
              class="absolute"
              style="top: 0; right: 12px; transform: translateY(-50%);"
          />

          <div class="row no-wrap items-center">
            <div class="col text-h6 ellipsis">
              Cafe Basilico
            </div>
            <div class="col-auto text-grey text-caption q-pt-md row no-wrap items-center">
              <q-icon name="place" />
              250 ft
            </div>
          </div>

        </q-card-section>

        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">

          </div>
          <div class="text-caption text-grey">
            Small plates, salads & sandwiches in an intimate setting.
          </div>
        </q-card-section>

        <q-separator />

        <q-card-actions align="right">
          <q-btn v-close-popup flat color="primary" round icon="bookmark" @click="addBookMarkPeer(candidateUserInfo.userId,candidateUserInfo.peerType)"/>
          <q-btn v-close-popup flat color="green" round icon="chat" @click="changeChatPeer(candidateUserInfo.userId,candidateUserInfo.peerType,candidateUserInfo.category)" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import contactItem from '@/views/chat/contactItem'
// import {date} from "quasar";
import {initJulyWS} from "@/utils/socket";
import {isNotEmpty} from "@/utils/validate";
import {JULY,FUN} from "@/utils/julyCommon";
import {CONSTANT, EVENT_CODE} from "@/utils/constant";
import {date} from "quasar";
import {getToken} from "@/utils/auth";

export default {
  name: "Contacts",
  props: {
    stompClient: {
      type: Object,
      required: true
    },
    connectionFlag: {
      type: Object,
      required: false
    },
    peerId: {
      type: String,
      required: false
    },
    peerType: {
      peerType: String,
      required: false
    },
    hostInfo: {
      peerType: Object,
      required: false
    },
    contactsWindowHeight: {
      peerType: String,
      required: false,
      default: '650px'
    }
  },
  components: {
    contactItem
  },
  data () {
    return {
      contactType: 'recent',
      hostId: this.hostInfo!=null?this.hostInfo.userId:null,
      userInfoDialogShow: false,
      searchInputDisplayVal:'',
      options:[],
      candidateOptions: [],
      searchSelectUserInfo:null,
      candidateUserInfo:{
        userId:'',
        userName:'',
        nickName:'',
        gender:'',
        lastLoginTime:'',
        role:'',
        avatar:''
      },
      contacts:{
        variable:{
          recent:{updateTS:null,array:[]},
          bookMark:{updateTS:null,array:[]},
          group:{updateTS:null,array:[]},
        }

      },
      julyWebsocket:{
        constant: {
          stompClient: this.stompClient
        },
        variable: {
          connectionFlag: this.connectionFlag!=null?this.connectionFlag:{'websocket':{'name':'websocket',"status":false}}
        },
      },
    }
  },
  computed: {
    chatEventNotifyURI() {
      if(this.hostId!=null){
        return '/user/topic/notify/' + this.hostId
      }else{
        return null
      }
    }
  },
  mounted () {
    this.initContacts(this.hostId)
    this.initEventNotifyWatcher()
    // this.initWSEnv()
    // if(this.stompClient==null){
    //   this.initWSWatcher()
    // }
  },
  methods: {
    initWSEnv(){
      if (this.julyWebsocket.constant.stompClient==null){
        console.log("contacts websocket not init , so start init by self")
        this.julyWebsocket.constant.stompClient = initJulyWS(JULY.WEBSOCKET_URI_ENDPOINT+getToken(),JULY.WEBSOCKET_URI_SEND_HEARTBEAT,JULY.WEBSOCKET_URI_SEND_HEARTBEAT_INTERVAL)
      }
    },
    initWSWatcher(){
      console.log("contacts websocket watcher not init , so start init by self")
      let _that = this
      this.julyWebsocket.constant.stompClient.connect(
          JULY.WEBSOCKET_HEADERS,
          function connectCallback () {
            // 订阅stompClient心跳检测
            _that.julyWebsocket.constant.stompClient.subscribe(JULY.WEBSOCKET_URI_SUBSCRIBE_HEARTBEAT, () => {
              _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
            })
          },
          function errorCallBack (error) {
            _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = false
            FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
          }
      )
    },
    initEventNotifyWatcher(){
      console.log("initEventNotifyWatcher")
      let _that = this
      _that.julyWebsocket.constant.stompClient.subscribe(_that.chatEventNotifyURI, (response) => {
        console.log("收到event")
        _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
        let feedback = JSON.parse(response.body)
        console.log(feedback)
        if (feedback.type=== CONSTANT.SHOUTING_EVENT){
          // type=== 'message' 说明是来了新事件
          let feedbackTS = date.formatDate(date.extractDate(feedback.gmtCreate,CONSTANT.DATE_FORMAT) , 'x')
          if (feedback.code===EVENT_CODE.RECENT_CHANGED){ // E1005 最近联系人列表改变
            if(!isNotEmpty(_that.contacts.variable.recent.updateTS) || feedbackTS>_that.contacts.variable.recent.updateTS){
              _that.updateRecentContactView(feedback.data,feedbackTS)
            }
          }else  if (feedback.code===EVENT_CODE.CONSPICUOUS_NOTIFY){ // E1007 显示消息
            FUN.notify(feedback.message, FUN.NOTIFY_LEVEL_WARNING, FUN.NOTIFY_POSITION_TOP)
          }
        }
      })
    },
    initContacts(hostId){
      if (isNotEmpty(hostId)) {
        let _that =this
        this.$store.dispatch('user/getUserRelation', {userId: hostId, peerId: null, category: null})
            .then((data) => {
              // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
              console.log('initContacts getRelation successful')
              console.log(data)
              _that.contacts.variable.recent.array=[]
              _that.contacts.variable.recent.updateTS = null
              _that.contacts.variable.bookMark.array=[]
              _that.contacts.variable.bookMark.updateTS = null
              for (let index in data) {
                if (data[index].category === CONSTANT.CONTACTS_CATEGORY_RECENT) {
                  let newRelation = {}
                  newRelation['peerId'] = data[index].peerId
                  newRelation['peerType'] = data[index].peerType
                  newRelation['remarkName'] = data[index].remarkName
                  newRelation['tag'] = data[index].tag
                  newRelation['peerUserName'] = data[index].peerUserName
                  newRelation['peerAvatar'] = data[index].peerAvatar
                  if (isNotEmpty(data[index].gmtCreate)) {
                    newRelation['gmtCreate'] = data[index].gmtCreate
                  } else {
                    newRelation['gmtCreate'] = null
                  }
                  if (isNotEmpty(data[index].gmtLastContact)) {
                    newRelation['gmtLastContact'] = data[index].gmtLastContact
                  } else {
                    newRelation['gmtLastContact'] = null
                  }
                  newRelation['peerNickName'] = data[index].peerNickName
                  newRelation['peerGender'] = FUN.convertPrintGender(data[index].peerGender)
                  newRelation['peerRole'] = FUN.filterPrintRole(data[index].peerRole)
                  newRelation['category'] = data[index].category
                  newRelation['unReadMsgCount'] = data[index].unReadMsgCount
                  newRelation['relTypeCode'] = data[index].relTypeCode
                  _that.contacts.variable.recent.array.push(newRelation)
                  _that.contacts.variable.recent.updateTS = date.formatDate(new Date(Date.now()) , 'x') // 记录现在更新时间
                } else if (data[index].category === CONSTANT.CONTACTS_CATEGORY_BOOKMARK) {
                  let newRelation = {}
                  newRelation['peerId'] = data[index].peerId
                  newRelation['peerType'] = data[index].peerType
                  newRelation['remarkName'] = data[index].remarkName
                  newRelation['tag'] = data[index].tag
                  newRelation['peerUserName'] = data[index].peerUserName
                  newRelation['peerAvatar'] = data[index].peerAvatar
                  if (isNotEmpty(data[index].gmtCreate)) {
                    newRelation['gmtCreate'] = data[index].gmtCreate
                  } else {
                    newRelation['gmtCreate'] = null
                  }
                  if (isNotEmpty(data[index].gmtLastContact)) {
                    newRelation['gmtLastContact'] = data[index].gmtLastContact
                  } else {
                    newRelation['gmtLastContact'] = null
                  }
                  newRelation['peerNickName'] = data[index].peerNickName
                  newRelation['peerGender'] = FUN.convertPrintGender(data[index].peerGender)
                  newRelation['peerRole'] = FUN.filterPrintRole(data[index].peerRole)
                  newRelation['category'] = data[index].category
                  newRelation['unReadMsgCount'] = data[index].unReadMsgCount
                  newRelation['relTypeCode'] = data[index].relTypeCode
                  _that.contacts.variable.bookMark.array.push(newRelation)
                  _that.contacts.variable.bookMark.updateTS = date.formatDate(new Date(Date.now()) , 'x') // 记录现在更新时间
                }
              }
            })
            .catch((error) => {
              console.log('initContacts getRelation fail')
              console.log(error)
              FUN.notify(error, FUN.NOTIFY_LEVEL_ERROR, FUN.NOTIFY_POSITION_TOP)
            })
      }else{
        FUN.notify("没有当前用户信息，无法获取用户的联系人", FUN.NOTIFY_LEVEL_ERROR, FUN.NOTIFY_POSITION_TOP)
      }
    },
    async filterFn (val, update, abort) {
      if (val.length < 1) {
        abort()
        return
      }
      let _that = this
      _that.candidateOptions.length=0
      console.log(val.trim())
      let candidateUserInfoArray = await this.getSearchUserInfo(val.trim())
      console.log("getSearchUserInfo")
      console.log(candidateUserInfoArray)
      for (let index in candidateUserInfoArray){
        let userInfo = {}
        userInfo['userId'] = candidateUserInfoArray[index].userId
        userInfo['nickName'] = candidateUserInfoArray[index].nickName
        userInfo['userName'] = candidateUserInfoArray[index].userName
        userInfo['avatar'] = candidateUserInfoArray[index].avatar
        userInfo['gender'] = candidateUserInfoArray[index].gender
        userInfo['role'] = candidateUserInfoArray[index].role
        _that.candidateOptions.push(userInfo)
      }
        update(() => {
          this.options = this.candidateOptions
        })
    },
    async getSearchUserInfo (search) {
      let result
      console.log('getSearchUserInfo :' +search)
      await this.$store.dispatch('user/searchUserInfo', search)
          .then((data) => {
            console.log('getSearchUserInfo successful')
            console.log(data)
            result = data
          })
          .catch((error) => {
            console.log('getSearchUserInfo fail')
            FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            result = []
          })
      return result
    },
    userInfoDialogBeforeHide(){
      this.searchInputDisplayVal = null
      this.searchSelectUserInfo = null
    },
    onClickSearchSelection(){
      this.searchInputDisplayVal = this.searchSelectUserInfo.userName
      this.userInfoDialogShow = true
      this.candidateUserInfo.userId = this.searchSelectUserInfo.userId
      this.candidateUserInfo.userName = this.searchSelectUserInfo.userName
      this.candidateUserInfo.nickName = this.searchSelectUserInfo.nickName
      this.candidateUserInfo.userId = this.searchSelectUserInfo.userId
      this.candidateUserInfo.avatar = this.searchSelectUserInfo.avatar
      this.candidateUserInfo.role = this.searchSelectUserInfo.role
      this.candidateUserInfo.peerType = CONSTANT.CONTAINER_PERSON
      this.candidateUserInfo.cateGory = CONSTANT.CONTACTS_CATEGORY_RECENT
    },
    changeChatPeer(peerId,peerType,category){
      console.log('contacts changeChatPeer')
      console.log(peerId)
      console.log(this.peerId)
      console.log(peerType)
      console.log(category)
      // let _that = this
      if (peerId!=this.peerId){
        this.$emit('changePeer',peerId,peerType)
      //   let data ={userId:this.hostId,peerId:peerId,peerType:peerType}
      //   this.$store.dispatch('user/upInsertUserRecentContact', {userId: this.hostId, data: JSON.stringify(data)})
      //       .then((data) => {
      //         // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
      //         console.log('upInsertRecentContact successful')
      //         console.log(data)
      //         _that.updateRecentContactView(data,null)
      //       })
      //       .catch((error) => {
      //         console.log('upInsertRecentContact fail')
      //         FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
      //
      //       })
      }

    },
    updateOneRecentContactView(peerId,msgDate){
      console.log("updateOneRecentContactView")
      console.log(peerId)
      console.log(msgDate)
      let needUpdatedItemIndex =null;
      for (let index=0;index<this.contacts.variable.recent.array.length;index++){
        if(this.contacts.variable.recent.array[index].peerId===peerId){
          let oldCreatedTS = date.formatDate(date.extractDate(this.contacts.variable.recent.array[index].gmtCreate,CONSTANT.DATE_FORMAT) , 'x')
          console.log("oldCreatedTS -"+oldCreatedTS)
          if(oldCreatedTS<msgDate){
            needUpdatedItemIndex = index;
            break;
          }
        }
      }
      if (needUpdatedItemIndex!=null&&needUpdatedItemIndex<this.contacts.variable.recent.array.length){
        let needUpdatedItem = this.contacts.variable.recent.array[needUpdatedItemIndex]
        needUpdatedItem.gmtLastContact = date.formatDate(date.extractDate(msgDate,'x') , CONSTANT.DATE_FORMAT)
        this.contacts.variable.recent.array.splice(needUpdatedItemIndex,1)
        this.contacts.variable.recent.array.splice(0,0,needUpdatedItem)
      }else {
          this.initContacts(this.hostId)
      }
    },
    updateRecentContactView(data,updateTS){
      console.log("updateRecentContactView")
      console.log(data)
      console.log(updateTS)
      this.contacts.variable.recent.array=[]
      this.contacts.variable.recent.updateTS=null
      for (let index in data){
        if(data[index].category===CONSTANT.CONTACTS_CATEGORY_RECENT) {
          let newRelation = {}
          newRelation['peerId'] = data[index].peerId
          newRelation['peerType'] = data[index].peerType
          newRelation['remarkName'] = data[index].remarkName
          newRelation['tag'] = data[index].tag
          newRelation['peerUserName'] = data[index].peerUserName
          newRelation['peerAvatar'] = data[index].peerAvatar
          if (isNotEmpty(data[index].gmtCreate)) {
            newRelation['gmtCreate'] = data[index].gmtCreate
          } else {
            newRelation['gmtCreate'] = null
          }
          if (isNotEmpty(data[index].gmtLastContact)) {
            newRelation['gmtLastContact'] = data[index].gmtLastContact
          } else {
            newRelation['gmtLastContact'] = null
          }
          newRelation['peerNickName'] = data[index].peerNickName
          newRelation['peerGender'] = FUN.convertPrintGender(data[index].peerGender)
          newRelation['peerRole'] = FUN.filterPrintRole(data[index].peerRole)
          newRelation['category'] = data[index].category
          newRelation['unReadMsgCount'] = data[index].unReadMsgCount
          newRelation['relTypeCode'] = data[index].relTypeCode
          this.contacts.variable.recent.array.push(newRelation)
        }
      }
      if(isNotEmpty(updateTS)){
        this.contacts.variable.recent.updateTS=updateTS // 记录现在更新时间
      }else{
        this.contacts.variable.recent.updateTS=date.formatDate(new Date(Date.now()) , 'x') // 记录现在更新时间
      }
    },
    updateBookmarkContactView(data,updateTS){
      this.contacts.variable.bookMark.array=[]
      this.contacts.variable.bookMark.updateTS=null
      for (let index in data){
        if(data[index].category===CONSTANT.CONTACTS_CATEGORY_BOOKMARK) {
          let newRelation = {}
          newRelation['peerId'] = data[index].peerId
          newRelation['peerType'] = data[index].peerType
          newRelation['remarkName'] = data[index].remarkName
          newRelation['tag'] = data[index].tag
          newRelation['peerUserName'] = data[index].peerUserName
          newRelation['peerAvatar'] = data[index].peerAvatar
          if (isNotEmpty(data[index].gmtCreate)) {
            newRelation['gmtCreate'] = data[index].gmtCreate
          } else {
            newRelation['gmtCreate'] = null
          }
          if (isNotEmpty(data[index].gmtLastContact)) {
            newRelation['gmtLastContact'] = data[index].gmtLastContact
          } else {
            newRelation['gmtLastContact'] = null
          }
          newRelation['peerNickName'] = data[index].peerNickName
          newRelation['peerGender'] = FUN.convertPrintGender(data[index].peerGender)
          newRelation['peerRole'] = FUN.filterPrintRole(data[index].peerRole)
          newRelation['category'] = data[index].category
          newRelation['unReadMsgCount'] = data[index].unReadMsgCount
          newRelation['relTypeCode'] = data[index].relTypeCode
          this.contacts.variable.bookMark.array.push(newRelation)
        }
      }
      if(isNotEmpty(updateTS)){
        this.contacts.variable.bookMark.updateTS=updateTS // 记录现在更新时间
      }else{
        this.contacts.variable.bookMark.updateTS=date.formatDate(new Date(Date.now()) , 'x') // 记录现在更新时间
      }
    },
    removeUserRelation(peerId,peerType,category){
      console.log('contacts removeUserRelation')
      console.log(peerId)
      console.log(peerType)
      console.log(category)
      if(isNotEmpty(peerId)&&isNotEmpty(peerType)&&isNotEmpty(category)){
        let _that = this
        if(category===CONSTANT.CONTACTS_CATEGORY_RECENT){
          let data ={userId:this.hostId,peerId:peerId,peerType:peerType}
          this.$store.dispatch('user/removeUserRecentContact',{userId: this.hostId, data: JSON.stringify(data)})
              .then((data) => {
                // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
                console.log('removeUserRecentContact successful')
                console.log(data)
                FUN.notify("删除最近联系人成功",FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
                _that.updateRecentContactView(data,null)
              })
              .catch((error) => {
                console.log('removeUserRecentContact fail')
                console.log(error)
                FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
              })
        }else if(category===CONSTANT.CONTACTS_CATEGORY_BOOKMARK){
          let data ={userId: this.hostId, peerId:peerId,peerType:peerType}
          this.$store.dispatch('user/removeUserRelation',data)
              .then((data) => {
                // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
                console.log('removeUserRelation successful')
                console.log(data)
                FUN.notify("删除收藏联系人成功",FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
                _that.updateBookmarkContactView(data,null)
              })
              .catch((error) => {
                console.log('removeUserRelation fail')
                console.log(error)
                FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
              })
        }
      }else{
        console.log("removeUserRelation 参数不正确")
      }
    },
    updateUserRelationType(peerId,peerType,relTypeCode,action){
      console.log("updateUserRelationType")
      console.log(peerId)
      console.log(peerType)
      console.log(relTypeCode)
      console.log(action)
      if(isNotEmpty(peerId)&&isNotEmpty(relTypeCode)&&isNotEmpty(action)){
        let actionMsg = ''
        let _that = this
        if(relTypeCode===CONSTANT.RELATION_TYPE_IGNORE){
          actionMsg = '屏蔽'
        }else if(relTypeCode===CONSTANT.RELATION_TYPE_BLACK){
          actionMsg = '拉黑'
        }
        if('add'===action){
          actionMsg = '添加'+actionMsg
          let data = {userId:this.hostId,peerId:peerId,peerType:peerType,relType:relTypeCode}
          this.$store.dispatch('user/upInsertUserRelation',{userId:this.hostId,category:null,data:JSON.stringify(data)})
              .then((data) => {
                // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
                console.log('updateUserRelationType add successful')
                console.log(data)
                _that.updateRecentContactView(data,null)
                _that.updateBookmarkContactView(data,null)
                FUN.notify(actionMsg+"成功",FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
              })
              .catch((error) => {
                console.log(error)
                FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
              })

        }else if('remove'===action){
          actionMsg = '取消'+actionMsg
          let data ={userId: this.hostId, peerId:peerId,peerType:peerType,relType:relTypeCode}
          this.$store.dispatch('user/removeUserRelation',data)
              .then((data) => {
                // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
                console.log('updateUserRelationType successful')
                console.log(data)
                FUN.notify(actionMsg+"成功",FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
                _that.updateRecentContactView(data,null)
                _that.updateBookmarkContactView(data,null)
              })
              .catch((error) => {
                console.log('updateUserRelationType fail')
                console.log(error)
                FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
              })
        }

      }
    },
    addBookMarkPeer(peerId,peerType){
      console.log('bookMarkPeer:'+peerId)
      console.log('bookMarkPeer:'+peerType)
      let _that = this
      let data = {userId:this.hostId,peerId:peerId,peerType:peerType,relType:CONSTANT.RELATION_TYPE_FRIEND}
      this.$store.dispatch('user/upInsertUserRelation',{userId:this.hostId,category:CONSTANT.CONTACTS_CATEGORY_BOOKMARK,data:JSON.stringify(data)})
          .then((data) => {
            // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
            console.log('addBookMarkPeer successful')
            console.log(data)
            _that.updateBookmarkContactView(data,null)
            FUN.notify("添加成功",FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
          })
          .catch((error) => {
            console.log('addBookMarkPeer fail')
            console.log(error)
            FUN.notify(error,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
          })
    },

  }
}
</script>

<style scoped>
.contacts {
  width: 100%;
  height: 100%;
  /*max-width: max-content;*/
  /*max-height: max-content;*/
}

.shadow-box{
  border: 1px solid #eee;
  border-radius: 5px;
}
.my-card {
  transition: box-shadow .3s;
  width: 100%;
  height: 100%;
  border-radius:10px;

}
.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}
</style>
