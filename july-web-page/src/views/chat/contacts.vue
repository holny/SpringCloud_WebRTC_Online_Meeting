<template>
  <div class="contacts q-pa-md justify-center">
  <q-card class="my-card" flat bordered>
    <q-card-section horizontal style="height: 12%">
      <q-img class="col" native-context-menu src="https://cdn.quasar.dev/img/mountains.jpg" >

        <div class="fit">
          <q-item>
            <q-item-section avatar>
              <q-avatar>
                <img src="https://cdn.quasar.dev/img/avatar2.jpg">
              </q-avatar>
            </q-item-section>
            <q-item-section>
              <q-item-label>{{this.searchSelectUserInfo}}</q-item-label>
              <q-item-label caption>Subhead</q-item-label>
            </q-item-section>
          </q-item>
        </div>
      </q-img>
    </q-card-section>
    <q-card-section class="q-pt-sm q-pb-sm" style="min-height: 4%">
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
    <q-card-section style="min-height: 78%"  horizontal>

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
          <contact-item v-for="socialObject in contacts.variable.recentArray" :key="socialObject.peerId" :peer-object="socialObject"/>
        </q-scroll-area>
      </q-list>
      <q-list v-else-if="contactType==='bookmark'" class="full-width">
        <q-scroll-area class="full-height full-width">
          <contact-item v-for="socialObject in contacts.variable.bookMarkArray" :key="socialObject.peerId" :peer-object="socialObject"/>
        </q-scroll-area>
      </q-list>
      <q-list v-else-if="contactType==='group'" class="full-width">
        <q-scroll-area class="full-height full-width">
          <contact-item v-for="socialObject in contacts.variable.groupArray" :key="socialObject.peerId" :peer-object="socialObject"/>
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
          <q-btn v-close-popup flat color="primary" round icon="bookmark" @click="$emit('bookMarkPeer',this.candidateUserInfo.userId)"/>
          <q-btn v-close-popup flat color="green" round icon="chat" @click="$emit('changeChatPeer',this.candidateUserInfo.userId)" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import {getHostId, getToken} from "@/utils/auth";
import {filterPrintRole,convertPrintGender} from "@/utils/convert";
import contactItem from '@/views/chat/contactItem'
import {date} from "quasar";
import {initJulyWS} from "@/utils/socket";

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
  },
  components: {
    contactItem
  },
  data () {
    return {
      contactType: 'recent',
      hostId: getHostId(),
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
          recentArray:[],
          bookMarkArray:[],
          groupArray:[],
        }

      },
      julyWebsocket:{
        constant: {
          WSEndPointURI:  'http://localhost:80/meeting/endpointWS?Authorization=' + getToken(),
          sendWSHeartBeatURI: '/app/heartbeat',
          subWSHeartBeatURI: '/user/topic/heartbeat',
          stompClient: this.stompClient
        },
        variable: {
          connectionFlag: this.connectionFlag!=null?this.connectionFlag:{'websocket':{'name':'websocket',"status":false}},
          headers: {'Authorization': 'Bearer ' + getToken()}
        },
      },
    }
  },
  mounted () {
    this.initContacts(this.hostId)
    // this.initWSEnv()
    // if(this.stompClient==null){
    //   this.initWSWatcher()
    // }
  },
  methods: {
    initWSEnv(){
      if (this.julyWebsocket.constant.stompClient==null){
        console.log("contacts websocket not init , so start init by self")
        this.julyWebsocket.constant.stompClient = initJulyWS(this.julyWebsocket.constant.WSEndPointURI,this.julyWebsocket.constant.sendWSHeartBeatURI)
      }
    },
    initWSWatcher(){
      console.log("contacts websocket watcher not init , so start init by self")
      let _that = this
      this.julyWebsocket.constant.stompClient.connect(
          _that.julyWebsocket.variable.headers,
          function connectCallback () {
            // 订阅stompClient心跳检测
            _that.julyWebsocket.constant.stompClient.subscribe(_that.julyWebsocket.constant.subWSHeartBeatURI, () => {
              _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = true
            })
          },
          function errorCallBack (error) {
            _that.julyWebsocket.variable.connectionFlag['websocket']['status'] = false
            console.log('订阅失败:' + error)
          }
      )
    },
    initContacts(hostId){
      let _that =this
      this.$store.dispatch('user/getUserSocial', hostId)
          .then((data) => {
            // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
            console.log('initContacts getUserSocial successful')
            console.log(data)
            for(let index in data){
              if (data[index].socialType==='recent'){
                let newSocial = {}
                newSocial['peerId'] = data[index].peerId
                newSocial['socialType'] = data[index].socialType
                newSocial['remarkName'] = data[index].remarkName
                newSocial['tag'] = data[index].tag
                newSocial['peerUserName'] = data[index].peerUserName
                newSocial['peerAvatar'] = data[index].peerAvatar
                newSocial['gmtLastContact'] = date.formatDate(date.extractDate(data[index].gmtLastContact, 'x'), 'YYYY-MM-DD HH:mm:ss')
                newSocial['peerNickName'] = data[index].peerNickName
                newSocial['peerGender'] = convertPrintGender(data[index].peerGender)
                newSocial['peerRole'] = filterPrintRole(data[index].peerRole)
                _that.contacts.variable.recentArray.push(newSocial)
              }else if (data[index].socialType==='bookmark'){
                let newSocial = {}
                newSocial['peerId'] = data[index].peerId
                newSocial['socialType'] = data[index].socialType
                newSocial['remarkName'] = data[index].remarkName
                newSocial['tag'] = data[index].tag
                newSocial['peerUserName'] = data[index].peerUserName
                newSocial['peerAvatar'] = data[index].peerAvatar
                newSocial['gmtLastContact'] = date.formatDate(date.extractDate(data[index].gmtLastContact, 'x'), 'YYYY-MM-DD HH:mm:ss')
                newSocial['peerNickName'] = data[index].peerNickName
                newSocial['peerGender'] = convertPrintGender(data[index].peerGender)
                newSocial['peerRole'] = filterPrintRole(data[index].peerRole)
                _that.contacts.variable.bookMarkArray.push(newSocial)
              }
            }
          })
          .catch((error) => {
            console.log('initContacts getUserSocial fail')
            console.log(error)
            this.$q.notify({
              type: 'negative',
              message: error.toString(),
              position: 'top',
              timeout: 5000
            })
          })
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
        userInfo['gender'] = convertPrintGender(candidateUserInfoArray[index].gender)
        userInfo['role'] = filterPrintRole(candidateUserInfoArray[index].role)
        _that.candidateOptions.push(userInfo)
      }
        update(() => {
          this.options = this.candidateOptions
        })
      // setTimeout(() => {
      //   update(() => {
      //     if (val === '') {
      //       this.options = this.candidateOptions
      //     }
      //     else {
      //       const needle = val.toLowerCase()
      //       this.options = this.candidateOptions.filter(v => v.label.toLowerCase().indexOf(needle) > -1)
      //     }
      //   })
      // }, 1500)
    },
    async getSearchUserInfo (search) {
      let result
      console.log('getSearchUserInfo :' +search)
      await this.$store.dispatch('user/searchUserInfo', search)
          .then((data) => {
            // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
            console.log('getSearchUserInfo successful')
            console.log(data)
            result = data
          })
          .catch((error) => {
            console.log('getSearchUserInfo fail')
            console.log(error)
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
    },
    addBookMarkPeer(peerId){
      console.log('bookMarkPeer:'+peerId)
      this.$store.dispatch('user/addUserSocial', {userId:this.hostId,peerId:peerId,type:'bookmark'})
          .then((data) => {
            // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
            console.log('addBookMarkPeer successful')
            console.log(data)
          })
          .catch((error) => {
            console.log('addBookMarkPeer fail')
            console.log(error)

          })
    },
    addRecentPeer(peerId){
      console.log('startChatPeer:'+peerId)
      this.chatPeerId = peerId
      this.$store.dispatch('user/addUserSocial', {userId:this.hostId,peerId:peerId,type:'recent'})
          .then((data) => {
            // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
            console.log('addBookMarkPeer successful')
            console.log(data)
          })
          .catch((error) => {
            console.log('addBookMarkPeer fail')
            console.log(error)

          })
    },
  }
}
</script>

<style scoped>
  .contacts {
    min-width: 400px;
    height: 100%;
    max-width: max-content;
    max-height: max-content;
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
