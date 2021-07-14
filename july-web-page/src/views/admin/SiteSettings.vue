<template>
  <q-card class="my-card bg-grey-1 no-wrap full-width" flat bordered style="min-width: 500px">
    <q-card-section>
      <div class="text-h5 q-mt-sm text-center">站点设置</div>
    </q-card-section>
    <q-card-section>
      <q-separator spaced />
      <q-item-label header>个性信息</q-item-label>
      <q-list>
        <q-item class="full-width">
          <q-item-section >
            <q-field hint="用户Id" :dense="true" style="width: 80%">
              <template v-slot:control>
                <div class="self-center full-width no-outline text-bold" tabindex="0">{{updateSellerInfo.userId}}</div>
              </template>
            </q-field>
          </q-item-section>
          <q-item-section >
            <q-toggle
                v-model="updateSellerInfo.isOpened"
                checked-icon="check"
                color="green"
                label="是否接受新咨询预约"
                unchecked-icon="clear"
            />
          </q-item-section>
        </q-item>
      </q-list>
    </q-card-section>
    <q-card-section>
      <q-separator spaced />
      <q-item-label header>卖家设置</q-item-label>
      <q-list>
        <q-item class="full-width">
          <q-item-section >
            <q-input :readonly="!updateSellerInfo.isOpened" class="col" v-model="updateSellerInfo.preNotify" ref="preNotify"  name="preNotify"
                     type="text"  debounce="500" style="width: 80%" counter hint="预约前说明"
                     autocomplete="on" autogrow
                     maxlength="150"   :dense="true" clearable outlined>
              <template v-slot:after>
                <q-btn round dense flat icon="eva-refresh-outline" >
                  <q-tooltip
                      transition-show="scale"
                      transition-hide="scale"
                  >
                    确认修改
                  </q-tooltip>
                </q-btn>
              </template>
            </q-input>
          </q-item-section>
          <q-item-section >
            <q-select
                :readonly="!updateSellerInfo.isOpened"
                class="full-width"
                outlined
                :dense="true"
                v-model="updateSellerInfo.tags"
                :options="consultTagsOption"
                :option-value="opt => Object(opt) === opt && 'code' in opt ? opt.code : null"
                :option-label="opt => Object(opt) === opt && 'desc' in opt ? opt.desc : '- 未知 -'"
                emit-value
                map-options
                use-chips
                multiple
                use-input
                max-values="3"
                :hint="updateSellerInfo.tags.length!=0?'标签名: '+sellerInfo.tags:''"
                input-debounce="0"
                @new-value="consultTagCreateValue"
                @filter="consultTagFilterFn"
                @filter-abort="consultTagAbortFilterFn"
            >
              <template v-slot:append>
                <q-icon v-show="tags_isCorrect!=null" :color="tags_isCorrect?'positive':'negative'" :name="tags_isCorrect?'check_circle':'cancel'" >
                  <q-tooltip
                      transition-show="scale"
                      transition-hide="scale"
                  >
                    标题需要符合要求
                  </q-tooltip>
                </q-icon>
              </template>
              <template v-slot:after>
                <q-btn round dense flat icon="eva-refresh-outline" >
                  <q-tooltip
                      transition-show="scale"
                      transition-hide="scale"
                  >
                    确认修改
                  </q-tooltip>
                </q-btn>
              </template>
            </q-select>
          </q-item-section>
        </q-item>
        <q-item class="full-width">
          <q-item-section  >
            <q-input :dense="true" type="number"  hint="输入每半小时咨询价格" maxlength="2"  suffix="豆" outlined v-model="updateSellerInfo.unitCost" style="width: 40%">
              <template v-slot:before>
                <q-icon name="sell" />
              </template>
              <template v-slot:after>
                <q-btn round dense flat icon="eva-refresh-outline" >
                  <q-tooltip
                      transition-show="scale"
                      transition-hide="scale"
                  >
                    确认修改
                  </q-tooltip>
                </q-btn>
              </template>
            </q-input>
          </q-item-section>
        </q-item>
      </q-list>
    </q-card-section>
    <q-card-section>
      <q-separator spaced />
      <q-item-label header>预约设置  <q-toggle
          v-model="acceptToggle"
          color="info"
          icon="eva-lock-outline"
          unchecked-icon="eva-unlock-outline"
          label="是否实行通用设置"
          @input="consultAcceptToggleSwitch"
      >
        <q-tooltip
            transition-show="scale"
            transition-hide="scale"
        >
          开启通用设置,卖家只需指定星期几接收预约,往后延续这种设置。<br>
          否则开启特殊指定，卖家需指定某天接收预约。
        </q-tooltip>
      </q-toggle>
        <q-btn round dense flat icon="eva-refresh-outline"  class="q-ml-lg">
          <q-tooltip
              transition-show="scale"
              transition-hide="scale"
          >
            确认修改
          </q-tooltip>
        </q-btn>
      </q-item-label>
      <q-list>
        <q-item class="full-width">
          <q-item-section >
            <q-option-group
                :disable="updateSellerInfo.acceptMode!='week'"
                name="accepted_genres"
                v-model="updateSellerInfo.acceptWeekDay"
                :options="weekDayOptions"
                type="checkbox"
                color="primary"
                inline
            />
          </q-item-section>
          <q-item-section >
            <div class="q-pb-sm">
              Model: {{ updateSellerInfo.acceptDate }}
            </div>

            <q-input :dense="true" outlined readonly v-model="updateSellerInfo.acceptDate"  label-slot>
              <template v-slot:label>
                选择最范围小于<strong class="text-deep-orange">{{consultAcceptSpecialMaxDays}}</strong>天
              </template>
              <template v-slot:append>
                <q-icon name="event" class="cursor-pointer">
                  <q-popup-proxy ref="qDateProxy" transition-show="scale" transition-hide="scale">
                    <q-date :disable="updateSellerInfo.acceptMode!='special'" v-model="updateSellerInfo.acceptDate" multiple mask="YYYY-MM-DD" :options="optionsFn" today-btn>
                      <div class="row items-center justify-end">
                        <q-btn v-close-popup label="Close" color="primary" flat />
                      </div>
                    </q-date>
                  </q-popup-proxy>
                </q-icon>
              </template>
            </q-input>
          </q-item-section>
        </q-item>
        <q-item class="full-width">
          <q-item-section avatar>
            <q-icon name="local_atm" >
              <q-tooltip
                  transition-show="scale"
                  transition-hide="scale"
              >
               选择每天接收预约的时间段,上面的通用设置和特殊设置都会使用此时间段
              </q-tooltip>
            </q-icon>
          </q-item-section>
          <q-item-section >
            <q-badge color="secondary" class="q-my-lg" style="height: 25px;font-size: 1em">
              选择每天接收预约的时间段: <strong class="text-amber">{{ updateSellerInfo.acceptTime.min }}时</strong> 至 <strong class="text-amber">{{ updateSellerInfo.acceptTime.max }}时</strong>
            </q-badge>
            <q-range
                v-model="updateSellerInfo.acceptTime"
                :min="0"
                :max="24"
                :step="0.5"
                label-always
                :left-label-value="updateSellerInfo.acceptTime.min + '时'"
                :right-label-value="updateSellerInfo.acceptTime.max + '时'"
                snap
                markers
                color="orange"
            />
          </q-item-section>
        </q-item>
      </q-list>
    </q-card-section>

  </q-card>
</template>

<script>
import {date} from "quasar";
import {CONSTANT} from "@/utils/constant";

let stringConsultTagsOptions =[
  {code:'100001',desc:'Google'}, {code:'100002',desc:'Facebook'}, {code:'100003',desc:'Twitter'}, {code:'100004',desc:'Apple'}, {code:'100005',desc:'Oracle'}
]
const stringWeekDayOptions =[ { label: '星期一', value: 1 }, { label: '星期二', value: 2 }, { label: '星期三', value: 4 }, { label: '星期四', value: 8}, { label: '星期五', value: 16 } , { label: '星期六', value: 32, disable: false } , { label: '星期日', value: 64, disable: false } ]
export default {
  name: "TradingSettings",
  data () {
    return {
      sellerInfo: {
        // constant
        userId: '100002',
        // could modify
        isOpened: true,
        preNotify: '',
        tags:[],
        acceptMode: 'week',
        acceptWeekDay:[1,2,4,8,64],
        acceptDate:['2021-07-13','2021-07-14','2021-07-22','2021-07-27'],
        acceptTime:{min: 8.5, max:22.5},
        unitCost:0,
      },
      updateSellerInfo:{
        // constant
        userId: null,
        // could modify
        isOpened: null,
        preNotify: null,
        tags:null,
        acceptMode: null,
        acceptWeekDay:null,
        acceptDate:null,
        acceptTime:null,
        unitCost:null,
      },
      acceptToggle: null,
      tags_isCorrect: null,
      consultTagsOption: stringConsultTagsOptions,
      weekDayOptions: stringWeekDayOptions,
      consultAcceptSpecialMaxDays:CONSTANT.CONSULT_SPECIAL_MAX_REACH_DAYS,

    }
  },
  created () {
    this.updateSellerInfo= JSON.parse(JSON.stringify(this.sellerInfo)) //深度复制
    this.acceptToggle=this.sellerInfo.acceptMode==='week'
  },
  methods:{
    consultTagCreateValue (val, done) {
      // Calling done(var) when new-value-mode is not set or "add", or done(var, "add") adds "var" content to the model
      // and it resets the input textbox to empty string
      // ----
      // Calling done(var) when new-value-mode is "add-unique", or done(var, "add-unique") adds "var" content to the model
      // only if is not already set
      // and it resets the input textbox to empty string
      // ----
      // Calling done(var) when new-value-mode is "toggle", or done(var, "toggle") toggles the model with "var" content
      // (adds to model if not already in the model, removes from model if already has it)
      // and it resets the input textbox to empty string
      // ----
      // If "var" content is undefined/null, then it doesn't tampers with the model
      // and only resets the input textbox to empty string

      if (val.length > 2) {
        // if (!stringOptions.includes(val)) {
        //   stringOptions.push(val)
        // }
        // done(val, 'toggle')
        if (!stringConsultTagsOptions.includes(val)) {
          console.log("videoSeriesCreateValue")
          console.log(val)
          console.log(this.consultTagsOption)
          if(val.length >=10){
            this.tags_isCorrect = false
          }else{
            this.tags_isCorrect = true
            this.consultTagsOption.push({code:'100006',desc:val})
            stringConsultTagsOptions.push({code:'100006',desc:val})
            done({code:'100006',desc:val}, 'add-unique')
          }
        }else{
          this.tags_isCorrect = null
        }
      }
    },
    consultTagFilterFn (val, update, abort) {
      abort
      if (this.consultTagsOption !== null) {
        update(() => {
          if (val === '') {
            this.consultTagsOption = stringConsultTagsOptions
          }
          else {
            const needle = val.toLowerCase()
            this.consultTagsOption = stringConsultTagsOptions.filter(
                v => v.desc.toLowerCase().indexOf(needle) > -1
            )
          }
        })
        return
      }

      setTimeout(() => {
        update(() => {
          this.consultTagsOption = stringConsultTagsOptions
        })
      }, 200)
    },

    consultTagAbortFilterFn () {
      // console.log('delayed filter aborted')
    },
    consultAcceptToggleSwitch(value){
      console.log("consultAcceptToggleSwitch")
      console.log(value)
      if(value===true){
        this.updateSellerInfo.acceptMode='week'
        this.updateSellerInfo.acceptDate =  JSON.parse(JSON.stringify(this.sellerInfo.acceptDate)) //深度复制
      }else{
        this.updateSellerInfo.acceptMode='special'
        this.updateSellerInfo.acceptWeekDay=JSON.parse(JSON.stringify(this.sellerInfo.acceptWeekDay)) //深度复制
      }
    },
    optionsFn (dateStr) {
      console.log(dateStr)
      let nowDate = new Date(Date.now())
      let tempDate = date.extractDate(dateStr, CONSTANT.DATE_FORMAT_4)
      let maxDate = date.addToDate(nowDate , {days: CONSTANT.CONSULT_SPECIAL_MAX_REACH_DAYS})
      if(tempDate>nowDate&&tempDate<maxDate){
       return true
      }else{
        return false
      }
    }
  }
}
</script>

<style scoped>

</style>
