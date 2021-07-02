<template>
  <div class="text-positive text-bold" ref="countObject"><q-icon name="alarm" class="q-ml-lg"/>&nbsp; {{hour? hourString+':'+minuteString+':'+secondString : minuteString+':'+secondString}}</div>
</template>

<script>
import { date } from 'quasar'
import {CONSTANT} from "@/utils/constant";

export default {
  name: "CountTimer",
  props: {
    remainTime: {    // 倒计时间总秒数
      type: Number,
      require: false,
      default: 30,
    },
    endDate: {    // 倒计时间总秒数
      type: Date,
      require: false,
      default: ()=>(date.extractDate('2021-07-02 13:35:44', CONSTANT.DATE_FORMAT)),
    },
    downOrUp: {
      type: String,
      require: false,
      default: 'down',
    }
  },
  data () {
    return {
      hour: '00',
      minute: '00',
      second: '00',
      promiseTimer: '',
      warningLeve1: 0,
      warningLeve2: 0,
      warningLeve3: 0,
      nowWarningLevel: -1,
    }
  },
  computed: {
    hourString () {
      return this.formatNum(this.hour)
    },
    minuteString () {
      return this.formatNum(this.minute)
    },
    secondString () {
      return this.formatNum(this.second)
    }
  },
  mounted () {
    if(this.downOrUp==='down'){
      if (this.remainTime > 0) {
        this.initWarningLevel(this.remainTime)
        this.hour = Math.floor((this.remainTime / 3600) % 24)
        this.minute = Math.floor((this.remainTime / 60) % 60)
        this.second = Math.floor(this.remainTime % 60)
        this.countDown()
      }
    }else if(this.downOrUp==='up'){
      this.hour = 0
      this.minute = 0
      this.second = 0
      this.countUp()
    }else if(this.downOrUp==='before'){
      let nowDate = new Date(Date.now())
      if(nowDate< this.endDate){
        this.hour = 0
        this.minute = 0
        this.second = 0
        this.countDownBeforeDate(this.endDate)
        let diff = date.getDateDiff(this.endDate,nowDate, 'seconds')
        this.initWarningLevel(diff)
      }
    }
  },
  methods: {
    countDownBeforeDate (endDate) {
      let self = this
      clearInterval(this.promiseTimer)
      this.promiseTimer = setInterval(function () {
        let nowDate = new Date(Date.now())
        if(nowDate> endDate){
          self.$emit('countBeforeEnd', true)
          clearInterval(self.promiseTimer)
        }
        let diff = date.getDateDiff(endDate,nowDate, 'seconds')
        self.second2Format(diff)
        self.updateAnimate(diff)
      }, 1000)
    },
    countDown () {
      let self = this
      clearInterval(this.promiseTimer)
      this.promiseTimer = setInterval(function () {
        if (self.hour === 0) {
          if (self.minute !== 0 && self.second === 0) {
            self.second = 59
            self.minute -= 1
          } else if (self.minute === 0 && self.second === 0) {
            self.second = 0
            self.$emit('countDownEnd', true)
            clearInterval(self.promiseTimer)
          } else {
            self.second -= 1
          }
        } else {
          if (self.minute !== 0 && self.second === 0) {
            self.second = 59
            self.minute -= 1
          } else if (self.minute === 0 && self.second === 0) {
            self.hour -= 1
            self.minute = 59
            self.second = 59
          } else {
            self.second -= 1
          }
        }
        self.updateAnimate(self.second+self.minute*60+self.hour*3600)
      }, 1000)
    },
    countUp () {
      let self = this
      clearInterval(this.promiseTimer)
      this.promiseTimer = setInterval(function () {
        if(self.second===59){
          self.second = 0
          if(self.minute===59){
            self.minute =0
            self.hour+=1
          }else{
            self.minute+=1
          }
        }else{
          self.second+=1
        }
        if(self.second+self.minute*60+self.hour*3600>=self.remainTime){
          self.$emit('countUpEnd', true)
          clearInterval(self.promiseTimer)
        }
      }, 1000)
    },
    initWarningLevel(time){
      if(time>=1700){ //大于30分钟
        this.warningLeve1 = 60
        this.warningLeve2 = 180
        this.warningLeve3 = 600
      }else if(time>=800){ //大于15分钟
        this.warningLeve1 = 60
        this.warningLeve2 = 120 // 2分钟
        this.warningLeve3 = 480 // 8分钟
      }else if(time>=500){ //大于10分钟
        this.warningLeve1 = 60
        this.warningLeve2 = 90 // 1分半分钟
        this.warningLeve3 = 300 // 5分钟
      }else if(time>=250){ //大于5分钟
        this.warningLeve1 = 30
        this.warningLeve2 = 60 // 1分钟
        this.warningLeve3 = 180 // 3分钟
      }else if(time>=170){
        this.warningLeve1 = 30
        this.warningLeve2 = 60
        this.warningLeve3 = 90 // 1.5分钟
      }else if(time>=90){
        this.warningLeve1 = 15
        this.warningLeve2 = 30
        this.warningLeve3 = 45 //
      }else if(time>=60){
        this.warningLeve1 = 10
        this.warningLeve2 = 20
        this.warningLeve3 = 30 //
      }else if(time>=45){
        this.warningLeve1 = 10
        this.warningLeve2 = 15
        this.warningLeve3 = 25 //
      }else if(time>=30){
        this.warningLeve1 = 5
        this.warningLeve2 = 10
        this.warningLeve3 = 20 //
      }else if(time>=20){
        this.warningLeve1 = 5
        this.warningLeve2 = 7
        this.warningLeve3 = 15 //
      }else if(time>=15){
        this.warningLeve1 = 5
        this.warningLeve2 = 7
        this.warningLeve3 = 10 //
      }else if(time>=10){
        this.warningLeve1 = 3
        this.warningLeve2 = 5
        this.warningLeve3 = 7 //
      }else if(time>=5){
        this.warningLeve1 = 3
        this.warningLeve2 = 3
        this.warningLeve3 = 3 //
      }else{
        this.warningLeve1 = 3
        this.warningLeve2 = 3
        this.warningLeve3 = 3 //
      }
    },
    updateAnimate(remainTime){
      //animate__animated animate__flash animate__infinite
      if(remainTime<=this.warningLeve1){
        if(this.nowWarningLevel!==1){
          this.$refs.countObject.setAttribute("class","animate__animated animate__flash animate__infinite text-red text-bold")
          console.log("set level 1")
          this.nowWarningLevel = 1
        }
      }else if(remainTime<=this.warningLeve2){
        if(this.nowWarningLevel!==2) {
          this.$refs.countObject.setAttribute("class", "animate__animated animate__headShake animate__infinite text-orange text-bold")
          console.log("set level 2")
          this.nowWarningLevel = 2
        }
      }else if(remainTime<=this.warningLeve3){
        if(this.nowWarningLevel!==3) {
          this.$refs.countObject.setAttribute("class", "animate__animated animate__pulse animate__infinite text-amber text-bold")
          console.log("set level 3")
          this.nowWarningLevel = 3
        }
      }else if(remainTime>this.warningLeve3&&this.nowWarningLevel!==0){
        this.$refs.countObject.setAttribute("class","text-positive text-bold")
        console.log("set level 0")
        this.nowWarningLevel = 0
      }
    },
    second2Format(second){
      console.log(second)
      // let day = Math.floor(sencond / (24 * 3600));
      let leve1 = second % (24 * 3600)
      this.hour = Math.floor(leve1 / (3600));
      let level2 = leve1 % (3600)
      this.minute = Math.floor(level2 / (60));
      let level3 = level2 % (60);
      this.second = level3;
    },
    formatNum (num) {
      return num < 10 ? '0' + num : '' + num
    }
  }
}
</script>
