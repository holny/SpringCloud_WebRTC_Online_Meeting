<template>
  <div class="chat-main-container q-pa-md justify-center">
    <q-card class="my-card" flat bordered>
      <q-card-section horizontal style="height: 100px">
        <q-img class="col" native-context-menu src="https://cdn.quasar.dev/img/mountains.jpg" >
          <div class="fit">
            <q-item>
              <q-item-section avatar>
                <q-avatar>
                  <img src="https://cdn.quasar.dev/img/avatar2.jpg">
                </q-avatar>
              </q-item-section>
              <q-item-section>
                <q-item-label class="text-h6 text-white">Title</q-item-label>
                <q-item-label class="text-caption text-white">Subhead</q-item-label>
              </q-item-section>
            </q-item>
            <q-card-actions class="absolute-right justify-around">
              <q-btn class="btn-small" flat round color="green" icon="videocam" />
              <q-btn class="btn-small" flat round color="indigo" icon="mic" />
              <q-btn class="btn-small" flat round color="indigo" icon="star_outline" />
            </q-card-actions>
          </div>
        </q-img>

        <!--        <q-card-actions vertical class="justify-around">-->
        <!--          <q-btn flat round color="green" icon="videocam" />-->
        <!--          <q-btn flat round color="indigo" icon="mic" />-->
        <!--        </q-card-actions>-->
      </q-card-section>

      <q-card-section>
        <q-scroll-area ref="scrollArea" class="inset-shadow-down shadow-10 shadow-box q-pb-md "
                       :thumb-style="thumbStyle"  @scroll="onScrollMessageView"
                       :bar-style="barStyle" style="height: 800px">
          <q-infinite-scroll @load="onLoad" reverse>
            <template slot="loading">
              <div class="row justify-center q-my-md">
                <q-spinner color="primary" name="dots" size="40px" />
              </div>
            </template>
            <div v-for="(msgItem, index) in updateMsgItems.data"  :key="'updateId'+index" class="chat-main-message-area q-py-sm">
              <div>
                <q-chat-message
                    v-if="msgItem.sender === hostId"
                    name="me"
                    :stamp="msgItem.stamp"
                    :text="msgItem.message"
                    sent
                    text-color="white"
                    bg-color="primary"
                >
                  <template v-slot:avatar>
                    <img
                        class="q-message-avatar q-message-avatar--sent"
                        :src="participantInfo[msgItem.sender].avatar"
                    >
                  </template>
                </q-chat-message>

                <q-chat-message
                    v-if="msgItem.sender !== hostId"
                    :name="participantInfo[msgItem.sender].userName"
                    :text="msgItem.message"
                    :stamp="msgItem.stamp"
                    bg-color="amber"
                >
                  <template v-slot:avatar>
                    <img
                        class="q-message-avatar q-message-avatar--received"
                        :src="participantInfo[msgItem.sender].avatar"
                    >
                  </template>

                  <!--                  <q-spinner-dots size="2rem" />-->
                </q-chat-message>
              </div>
            </div>
          </q-infinite-scroll>
        </q-scroll-area>
      </q-card-section>
      <q-card-actions  class="column">
        <div class="col full-width">
          <q-input  outlined clearable label="输入信息" :dense="dense" :loading="inputMessageLoading" @keyup.enter.native="sendMessage"
                    v-model="inputMessage" counter maxlength="255" type="textarea" style="width: 100%"
                    autogrow
          />
        </div>
        <div class="col  full-width">
          <div class="q-pa-sm q-gutter-x-sm justify-between float-left">
            <q-btn round color="amber" icon="emoji_emotions" />
            <q-btn round color="info" icon="folder" />
          </div>
          <div class="q-pa-sm q-gutter-x-sm justify-between float-right">
            <q-btn color="positive" icon="send" @click.native="sendMessage"  />
          </div>
        </div>
      </q-card-actions>
    </q-card>
  </div>

</template>

<script>
import { date } from 'quasar'
import {isNotEmpty} from "@/utils/validate";
export default {
  name: "chat",
  data () {
    return {
      dense: true,
      lastUpdateTime: '',
      combineMsgGap:10,
      updateViewInterval:2000,
      getRemoteMessagesInterval:5000,
      hostId: '10001',
      inputMessage: '',
      inputMessageLoading: false,
      msgViewVerticalPercentage: 1, // 当前消息浏览界面滚动条所处百分比，1=100%，说明滚动条在最下面
      participantInfo: {'10001':{'userName':'Tome','avatar':'https://cdn.quasar.dev/img/avatar4.jpg'},'10002':{'userName':'July','avatar':'https://cdn.quasar.dev/img/avatar2.jpg'}},
      // 注意：以下Items默认按时间，越近的下标越大
      // testMsgItems :[ {'sender':'10001','startTS': '1622862079780','endTS': '1622862086781','stamp':'','message':['new hello1','new hello2']},
      //   {'sender':'10002','startTS': '1622862090781','endTS': '1622862095781','stamp':'','message':['new hello3']},
      //   {'sender':'10001','startTS': '1622862095781','endTS': '1622862102781','stamp':'','message':['new hello5','new hello6']},
      //   {'sender':'10001','startTS': '1622862106782','endTS': '1622862110785','stamp':'','message':['new hello7','new hello8']},
      //   {'sender':'10002','startTS': '1622862114785','endTS': '1622862119781','stamp':'','message':['new hello9','new hello10']}],
      testMsgItems :[ {'sender':'10001','startTS': '1328165532000','endTS': '1328165592000','stamp':'','message':['old hello1','old hello2']},
        {'sender':'10002','startTS': '1328082792000','endTS': '1328082912000','stamp':'','message':['old hello3']},
        {'sender':'10001','startTS': '1327697712000','endTS': '1327728132000','stamp':'','message':['old hello5','old hello6']},
        {'sender':'10001','startTS': '1327674132000','endTS': '1327674252000','stamp':'','message':['old hello7','old hello8']},
        {'sender':'10002','startTS': '1327429452000','endTS': '1327431312000','stamp':'','message':['old hello9','old hello10']}],
      // expired 代表无限等待锁的释放
      candidateMsgItems: {'lockObject':{'name':'candidateMsgItemsLock','lock':false,'expired':-1},'data':[]},
      // lockObject 自旋锁，false=没有在更新(改变数组)，true=有在更新
      updateMsgItems: {'lockObject':{'name':'updateMsgItemsLock','lock':false,'expired':1},'data':
            [ {'sender':'10001','startTS': '1330842681000','endTS': '1330842691000','stamp':'2012-3-4 14:31:31','message':['luck1','luck2']},
              {'sender':'10002','startTS': '1330842764000','endTS': '1330842824000','stamp':'2012-3-4 14:33:44','message':['luck3']},
              {'sender':'10001','startTS': '1330842944000','endTS': '1330842964000','stamp':'2012-3-4 14:36:04','message':['luck4','luck5']},
              {'sender':'10001','startTS': '1330843364000','endTS': '1330843435000','stamp':'2012-3-4 14:43:55','message':['luck6','luck7']},
              {'sender':'10002','startTS': '1330843904000','endTS': '1330843932000','stamp':'2012-3-4 14:52:12','message':['luck8','luck9']},
              {'sender':'10001','startTS': '1622788281000','endTS': '1622788291000','stamp':'','message':['hello1','hello2']},
              {'sender':'10002','startTS': '1622788424000','endTS': '1622788424000','stamp':'','message':['hello3']},
              {'sender':'10001','startTS': '1622788544000','endTS': '1622788564000','stamp':'','message':['hello5','hello6']},
              {'sender':'10001','startTS': '1622788964000','endTS': '1622789035000','stamp':'','message':['hello7','hello8']},
              {'sender':'10002','startTS': '1622789504000','endTS': '1622789532000','stamp':'','message':['hello9','hello10']}]},
      // previousMsgItems: {'lockObject':{'name':'previousMsgItemsLock','lock':false,'expired':1},'data':
      //   [ {'sender':'10001','startTS': '1330842681000','endTS': '1330842691000','stamp':'2012-3-4 14:31:31','message':['luck1','luck2']},
      //   {'sender':'10002','startTS': '1330842764000','endTS': '1330842824000','stamp':'2012-3-4 14:33:44','message':['luck3']},
      //   {'sender':'10001','startTS': '1330842944000','endTS': '1330842964000','stamp':'2012-3-4 14:36:04','message':['luck4','luck5']},
      //   {'sender':'10001','startTS': '1330843364000','endTS': '1330843435000','stamp':'2012-3-4 14:43:55','message':['luck6','luck7']},
      //   {'sender':'10002','startTS': '1330843904000','endTS': '1330843932000','stamp':'2012-3-4 14:52:12','message':['luck8','luck9']}]},
      thumbStyle: {
        right: '4px',
        borderRadius: '5px',
        backgroundColor: '#027be3',
        width: '5px',
        opacity: 0.75
      },
      barStyle: {
        right: '2px',
        borderRadius: '9px',
        backgroundColor: '#027be3',
        width: '9px',
        opacity: 0.2
      }
    }
  },
  // computed: {
  //   testMsgItems () {
  //     return [ {'sender':'10001','startTS': '1622814681000','endTS': '1622814741000','stamp':'','message':['hello1','hello2']},
  //       {'sender':'10002','startTS': '1622814801000','endTS': '1622814861000','stamp':'','message':['hello3']},
  //       {'sender':'10001','startTS': '1622814933000','endTS': '1622814955000','stamp':'','message':['hello5','hello6']},
  //       {'sender':'10001','startTS': '1622815015000','endTS': '1622815065000','stamp':'','message':['hello7','hello8']},
  //       {'sender':'10002','startTS': '1622815545000','endTS': '1622815605000','stamp':'','message':['hello9','hello10']}]
  //   },
  //   candidateMsgItems () {
  //     return []
  //   },
  //   updateMsgItems () {
  //     return [ {'sender':'10001','startTS': '1622788281000','endTS': '1622788291000','stamp':'','message':['hello1','hello2']},
  //       {'sender':'10002','startTS': '1622788424000','endTS': '1622788424000','stamp':'','message':['hello3']},
  //       {'sender':'10001','startTS': '1622788544000','endTS': '1622788564000','stamp':'','message':['hello5','hello6']},
  //       {'sender':'10001','startTS': '1622788964000','endTS': '1622789035000','stamp':'','message':['hello7','hello8']},
  //       {'sender':'10002','startTS': '1622789504000','endTS': '1622789532000','stamp':'','message':['hello9','hello10']}]
  //   },
  //   previousMsgItems () {
  //     return [ {'sender':'10001','startTS': '1330842681000','endTS': '1330842691000','stamp':'2012-3-4 14:31:31','message':['luck1','luck2']},
  //       {'sender':'10002','startTS': '1330842764000','endTS': '1330842824000','stamp':'2012-3-4 14:33:44','message':['luck3']},
  //       {'sender':'10001','startTS': '1330842944000','endTS': '1330842964000','stamp':'2012-3-4 14:36:04','message':['luck4','luck5']},
  //       {'sender':'10001','startTS': '1330843364000','endTS': '1330843435000','stamp':'2012-3-4 14:43:55','message':['luck6','luck7']},
  //       {'sender':'10002','startTS': '1330843904000','endTS': '1330843932000','stamp':'2012-3-4 14:52:12','message':['luck8','luck9']}]
  //   }
  // },
  mounted () {
    // 先立即执行一次更新View
    this.updateView()
    setInterval(this.updateView, this.updateViewInterval)
    // setInterval(this.getRemoteMessages, this.getRemoteMessagesInterval)
  },
  // keep-alive会缓存组件状态，activated()和 deactivated() 这两个钩子函数。activated()是keep-alive 组件激活时调用，而 deactivated() 是 keep-alive 组件停用时调用。activated ()替换mounted()
  activated () {
  },
  destroyed() {
    clearInterval(this.updateView)
    // clearInterval(this.getRemoteMessages)
  },
  methods: {
    init(){
    },
    getRemoteMessages(){
      if (this.testMsgItems.length>0){
        this.candidateMsgItems.data.push(this.testMsgItems.shift())
      }
      if (this.candidateMsgItems.data.length > 0) {
        // 如果candidateMsgItems存在数据了说明接收到了新数据
        // 首先获取candidateMsgItems的lock，把candidateMsgItems的数据Push到updateMsgItems尾部，并且清空candidateMsgItems，最后释放candidateMsgItems lock
        if (!this.getLock(this.candidateMsgItems.lockObject)) {
          console.log('got candidateMsgItems lock out deadline, Fail, give up this job')
          this.releaseLock(this.candidateMsgItems.lockObject)
          return
        }
        try{
          for (let index in this.candidateMsgItems.data) {
            this.insertMessage(this.candidateMsgItems.data[index])
          }
          this.candidateMsgItems.data.splice(0, this.candidateMsgItems.data.length)
        }catch (error){
          console.error('updateRecentMessageState -candidateMsgItems error:'+error)
        }finally {
          this.releaseLock(this.candidateMsgItems.lockObject)
        }
      }

    },
    updateView() {
      let beforeState = this.isChatViewObservedBottom
      this.updateRecentMessageState()
      if (beforeState){
        this.scrollToBottom()
      }
    },
    // 这里周期循环运行，目的是更新已插入消息的stamp或者状态
    updateRecentMessageState(){
      if (!this.getLock(this.updateMsgItems.lockObject)) {
        console.log('got updateMsgItems lock out deadline, Fail, give up this job')
        return
      }
      try {
        this.lastUpdateTime = this.convertStamp(this.updateMsgItems.data)
      } catch (error){
        console.error('updateRecentMessageState error:' + error)
      }  finally    {
        this.releaseLock(this.updateMsgItems.lockObject)
      }
    },
    // 根据当前时间，更新每一个Stamp
    convertStamp(msgItems){
      let lastUpdateTime = new Date(Date.now())  // 记录现在更新时间
      // console.log("updateMessageState -- test lastUpdateTime " + typeof (this.lastUpdateTime))
      let offsetOneDay = date.formatDate(date.subtractFromDate(lastUpdateTime, {days: 1}), 'x')
      let offsetOneHour = date.formatDate(date.subtractFromDate(lastUpdateTime, {hours: 1}), 'x')
      let offsetOneMinute = date.formatDate(date.subtractFromDate(lastUpdateTime, {minutes: 1}), 'x')
      if (msgItems.length>0) {
        for (let index = msgItems.length - 1; index >= 0; index--) {
          if (offsetOneMinute < msgItems[index].endTS) {
            // 说明消息是最近一分钟的,所以计算秒差，更新stamp
            let diff = date.getDateDiff(lastUpdateTime, date.extractDate(msgItems[index].endTS, 'x'), 'seconds')
            if (diff <= 10) {
              msgItems[index].stamp = 'just now'
            } else {
              msgItems[index].stamp = diff + ' seconds ago'
            }
          } else if (offsetOneHour < msgItems[index].endTS) {
            // 说明消息是最近一小时的,所以计算分钟差，更新stamp
            let diff = date.getDateDiff(lastUpdateTime, date.extractDate(msgItems[index].endTS, 'x'), 'minutes')
            msgItems[index].stamp = diff + ' minutes ago'
          } else if (offsetOneDay < msgItems[index].endTS) {
            // 说明消息是最近一天的(但是大于小时),所以计算小时差，更新stamp
            let diff = date.getDateDiff(lastUpdateTime, date.extractDate(msgItems[index].endTS, 'x'), 'hours')
            msgItems[index].stamp = diff + ' hours ago'
          } else {
            if (date.isValid(msgItems[index].stamp)) {
              // 因为数组排列是按发送时间从近到远排列，筛选到这的endTS都是离当前时间大于24h的，并且stamp是时间格式的，所以后面的元素都不用再转换stamp
              break
            } else {
              msgItems[index].stamp = date.formatDate(date.extractDate(msgItems[index].endTS, 'x'), 'YYYY-MM-DD HH:mm:ss')
            }
          }
        }
      }
      return lastUpdateTime
    },
    sendMessage(){
      let msgText = this.inputMessage
      if(isNotEmpty(msgText)){
        this.inputMessage = ''
        console.log('sendMessage '+msgText)
        this.inputMessageLoading = true
        let nowTS = date.formatDate( new Date(Date.now()),'x')
        let newMsg = {'sender':this.hostId,'startTS': nowTS,'endTS': nowTS,'stamp':date.formatDate(date.extractDate(nowTS, 'x'), 'YYYY-MM-DD HH:mm:ss'),'message':[msgText]}
        this.insertMessage(newMsg)
        // this.$refs.scrollArea.setScrollPosition(this.$refs.scrollArea.getScrollTarget().scrollHeight,100)
        // Todo: 传递消息到服务器
        this.inputMessageLoading = false
      }
    },
    // 在当前聊天界面立即插入消息
    insertMessage(msgItem){
      if (!this.getLock(this.updateMsgItems.lockObject)) {
        console.log('got updateMsgItems lock out deadline, Fail, give up this job')
        return
      }
      this.convertStamp([msgItem])
      try {
        console.log('sendMessage ' + msgItem)
        console.log('sendMessage  updateMsgItems.data.length:' + this.updateMsgItems.data.length)
        if (this.updateMsgItems.data.length>0){
          let insertPosition = this.updateMsgItems.data.length
          for (let index= this.updateMsgItems.data.length;index>0;index--){
            if (msgItem.endTS<=this.updateMsgItems.data[index-1].startTS){
              insertPosition = index-1
            }else{
              break
            }
          }
          console.log('sendMessage  insertPosition:' + insertPosition)
          console.log('sendMessage  item' + this.updateMsgItems.data[insertPosition])
          let nowDate = new Date(Date.now())
          // 看所插入位置前面一位的endTS，来决定是否合并
          if (insertPosition>0&&this.updateMsgItems.data[insertPosition-1].sender == msgItem.sender) {
            console.log('insertMessage  nowDate:'+date.formatDate(nowDate, 'YYYY-MM-DD HH:mm:ss'))
            console.log('insertMessage  nowDate:'+date.formatDate(date.extractDate(this.updateMsgItems.data[insertPosition-1].endTS, 'x'), 'YYYY-MM-DD HH:mm:ss'))
            let diffSeconds = date.getDateDiff(nowDate, date.extractDate(this.updateMsgItems.data[insertPosition-1].endTS, 'x'), 'seconds')
            console.log('insertMessage  diffSeconds:'+diffSeconds)
            if (diffSeconds <= this.combineMsgGap) {
              for(let index in  msgItem.message){
                this.updateMsgItems.data[insertPosition-1].message.push(msgItem.message[index])
              }
              this.updateMsgItems.data[insertPosition-1].endTS = msgItem.endTS
            } else {
              this.updateMsgItems.data.splice(insertPosition,0,msgItem)
            }
          } else {
            this.updateMsgItems.data.splice(insertPosition,0,msgItem)
          }
        }else{
          this.updateMsgItems.data.splice(0,0,msgItem)
        }

      }catch (error){
        console.error('insertMessage error:' +error.stack)

      }finally {
        this.releaseLock(this.updateMsgItems.lockObject)
        console.log('insertMessage done, has released lock.. lock new status:' + this.updateMsgItems.lockObject.lock)
      }
    },
    getLock(lockObject){
      let entranceTS = new Date(Date.now())
      // 设置要先获取updateMsgItems的锁，这里形成自选，并且判断等待锁的时间，如果超过循环任务的时间间隔一半就放弃更新
      // console.log('start got lock:'+lockObject.name)
      // console.log('this lock expired time:'+lockObject.expired)
      while (lockObject.lock){
        let probeTS = new Date(Date.now())
        let waitingDiff = date.getDateDiff(probeTS,entranceTS,'seconds')
        if(lockObject.expired>0&&waitingDiff>=lockObject.expired){
          console.log('waiting for release lock too long time, out deadline')
          return false
        }
      }
      lockObject.lock = true
      return true
    },
    releaseLock(lockObject){
      lockObject.lock = false
    },
    onLoad (index, done) {
      setTimeout(() => {
        console.log('onLoad '+index)
        // setTimeout(() => {
        //   if (this.items) {
        //
        //     done()
        //   }
        // }, 2000)
        if (this.testMsgItems.length>0){
          this.candidateMsgItems.data.push(this.testMsgItems.shift())
        }
        done()
      }, 2000)
    }
  }
}
</script>

<style scoped>
.chat-main-container {
  min-width: 700px;
  max-width: max-content;
  max-height: max-content;
}
.chat-main-message-area {
  padding: 5px 3%;
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
