<template>
  <div class="q-pa-md">
    <div v-if="type==='dateTimeRange'" class="vertical-top">
      <!-- begin -->
      <q-input outlined v-model="startDateStr" class="inline-block"  label-slot  readonly borderless>
        <template v-slot:label>
          选择<strong class="text-deep-orange">起始</strong>日期时间
        </template>
        <template v-slot:prepend>
          <q-icon name="event" class="cursor-pointer">
            <q-popup-proxy transition-show="scale" transition-hide="scale">
              <q-date v-model="startDateStr" mask="YYYY-MM-DD HH:mm" :events="eventsFn" color="yellow" text-color="black" today-btn
                      :event-color="eventColor" :options="optionsFn">
                <div class="row items-center justify-end">
                  <q-btn v-close-popup label="Close" color="primary" flat />
                </div>
              </q-date>
            </q-popup-proxy>
          </q-icon>
        </template>

        <template v-slot:append>
          <q-icon name="access_time" class="cursor-pointer">
            <q-popup-proxy transition-show="scale" transition-hide="scale">
              <q-time v-model="startDateStr" mask="YYYY-MM-DD HH:mm"  :hour-options="limitTime" :minute-options="minuteOptionsTime" color="yellow" text-color="black" format24h>
                <div class="row items-center justify-end">
                  <q-btn v-close-popup label="Close" color="primary" flat />
                </div>
              </q-time>
            </q-popup-proxy>
          </q-icon>
        </template>
      </q-input>
      <div class="inline-block vertical-top q-ml-lg">
        <q-badge color="secondary">
          选择需要<strong class="text-deep-orange">持续</strong>多少小时 (最长{{sliderMax}}小时)
        </q-badge>

        <q-slider
            v-model="sliderValue"
            :min="0.5"
            :max="sliderMax"
            :step="0.5"
            label
            label-always
            color="light-green"
        />
      </div>
<!--      Date:{{startDate}} - {{endDate}}-->
    </div>
  </div>
</template>

<script>
import {getQuasarColorByRatio} from "@/utils/random";
import { date } from 'quasar'
import {CONSTANT} from "@/utils/constant";
export default {
  name: "RangeDateTimePicker",
  props: {
    type: {
      type: String,
      require: false,
      default: 'dateTimeRange',
    },
    busyDate: {
      type: Object,
      require: false,
      default: ()=>({'2021/07/03':1,'2021/07/04':0.9,'2021/07/05':0.8,'2021/07/08':0.7,'2021/07/09':0.6,'2021/07/12':0.5,'2021/07/13':0.4,'2021/07/14':0.3,'2021/07/20':0.2,'2021/07/22':0.1,'2021/07/23':0.0}),
    },
    limitDate: {
      type: Array,
      require: false,
      default: ()=>(['2021/07/03','2021/07/04','2021/07/05','2021/07/08','2021/07/09','2021/07/12','2021/07/13','2021/07/14','2021/07/20','2021/07/22','2021/07/23']),
    },
    limitTime: {
      type: Array,
      require: false,
      default: ()=>([0,1,2,7,8,11,12,13,14,15,16,17,18,19,20,21,22,23]),
    },
    maxRange: {
      type: Number,
      require: false,
      default: 20,
    },
    startDate: {
      type: Date,
      require: false,
      default: null,
    },
    endDate: {
      type: Date,
      require: false,
      default: null,
    },
    consultRangeTime: {
      type: Number,
      require: false,
      default: 0.5,
    },
  },
  data () {
    return {
      sliderMax:0,
      startDateStr:'',
      endDateStr:'',
      // hourOptionsTime1: [ 9, 10, 11, 13, 15 ],
      minuteOptionsTime: [ 0, 15, 30, 45 ],
      sliderValue: 0.5,
    }
  },
  watch: {
    // Whenever the movie prop changes, fetch new data
    startDateStr: {
      // Will fire as soon as the component is created
      immediate: false,
      handler(newVal, oldVal) {
        oldVal
        if (date.isValid(newVal)) {
          // console.log(newVal)
          // this.limitTime.sort(function(a,b){return a-b}) //从小到大排序
          // console.log(this.limitTime)
          let startDate = date.extractDate(newVal, CONSTANT.DATE_FORMAT_2)
          let tempInt = parseInt(date.formatDate(startDate,'HH').toString())
          let sliderMax = 0
          while(this.limitTime.indexOf(tempInt)>=0&&sliderMax<this.maxRange){
            startDate = date.addToDate(startDate , {minutes: 30})
            tempInt = parseInt(date.formatDate(startDate,'HH').toString())
            sliderMax+=0.5
            console.log(startDate.toString())
            console.log(sliderMax)
          }
          this.sliderMax = sliderMax
          startDate = date.extractDate(newVal, CONSTANT.DATE_FORMAT_2)
          this.$emit('update:startDate', startDate);
          if(this.sliderValue>=0.5){
            if(this.sliderValue>this.sliderMax){
              this.sliderValue = this.sliderMax
            }else{
              let addMinutes = 60*this.sliderValue
              let endDate = date.addToDate(startDate , {minutes: addMinutes})
              this.endDateStr =date.formatDate(endDate, CONSTANT.DATE_FORMAT_2)
              this.$emit('update:endDate', endDate);
            }
          }
        }
      }
      // deep: true
    },
    sliderValue: {
      // Will fire as soon as the component is created
      immediate: true,
      handler(newVal, oldVal) {
        oldVal
        if (date.isValid(this.startDateStr)) {
          let addMinutes = 60*newVal
          // console.log("add minutes:"+addMinutes)
          let startDate = date.extractDate(this.startDateStr, CONSTANT.DATE_FORMAT_2)
          let endDate = date.addToDate(startDate , {minutes: addMinutes})
          // console.log(startDate.toString())
          // console.log(endDate.toString())
          this.endDateStr =date.formatDate(endDate, CONSTANT.DATE_FORMAT_2)
          this.$emit('update:endDate', endDate);
          this.$emit('update:consultRangeTime', newVal);
        }
      }
      // deep: true
    },
  },
  mounted() {
    if (this.startDate!=null) {
      this.startDateStr = date.formatDate(this.startDate, CONSTANT.DATE_FORMAT_2)
    }
    if (this.consultRangeTime !=null,this.consultRangeTime>=0.5) {
      this.sliderValue = this.consultRangeTime
      // let diff = date.getDateDiff(this.endDate, this.startDate, 'minutes')
      // console.log("diff "+diff)
      // if(diff>=0){
      //   this.sliderValue = Math.floor(diff/30)/2
      //   console.log("sliderValue "+this.sliderValue)
      // }
    }
  },
  methods: {
    eventsFn (date) {
      // eslint-disable-next-line no-prototype-builtins
      if (this.busyDate.hasOwnProperty(date)){
        return true
      }
      return false
    },
    eventColor(date){
      let color;
      // eslint-disable-next-line no-prototype-builtins
      if (this.busyDate.hasOwnProperty(date)){
        color = getQuasarColorByRatio(this.busyDate[date])
      }else{
        color = getQuasarColorByRatio(0)
      }
      // console.log("eventColor date:"+date+"  color:"+color)
      return color.toString()
    },
    optionsFn (date) {
      if(this.limitDate.length>0){
        if(this.limitDate.indexOf(date)>=0){
          return true
        }else{
          return false
        }
      }else{
        return true
      }
    }
  }
}
</script>

<style scoped>

</style>
