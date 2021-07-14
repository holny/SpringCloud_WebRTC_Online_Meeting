<template>
    <q-card class="my-card bg-grey-1" flat bordered>
      <q-card-section>
        <div class="text-h5 q-mt-sm">预约咨询</div>
      </q-card-section>
      <q-card-section>
        <q-stepper
            v-model="step"
            ref="stepper"
            color="primary"
            animated
            class="bg-grey-1 no-border-radius no-shadow"
        >
          <q-step
              :name="1"
              title="阅读说明"
              icon="settings"
              :done="step1Agree===true&&step>1"
              style="min-height: 200px;"
          >
            <div class="text-h6 text-green-9">请仔细阅读咨询说明</div>
            <div class="text-body2 text-black">&nbsp;&nbsp;请仔细阅读咨询说明请仔细阅读咨询说明请仔细阅读咨询说明请仔细阅读咨询说明请仔细阅读咨询说明</div>
            <q-toggle
                v-model="step1Agree"
                checked-icon="check"
                color="positive"
                label="同意以上说明"
                unchecked-icon="clear"
                class="q-my-md"
            />
            <q-stepper-navigation>
              <q-btn :disable="!step1Agree" @click="() => { done1 = true; $refs.stepper.next() }" color="primary" label="继续" />
            </q-stepper-navigation>
          </q-step>
          <q-step
              :name="2"
              title="许可"
              icon="settings"
              :done="(step > 2&&isPermitPass)||!isNeedPermit"
              style="min-height: 200px;"
              :disable="!isNeedPermit"
          >
            <div class="text-h6 text-green-9">输入许可密钥</div>
            <q-input outlined bottom-slots v-model="permitCode" label="输入许可密钥" counter maxlength="12" :dense="true">
              <template v-slot:append>
                <q-icon v-if="permitCode !== ''" name="close" @click="permitCode = ''" class="cursor-pointer" />
                <q-icon name="schedule" />
              </template>
              <template v-slot:hint>
                {{permitInputHint}}
              </template>
              <template v-slot:after>
                <q-btn round dense flat icon="explore" @click="verifyPermitCode"/>
              </template>
            </q-input>
<!--            <q-input outlined v-model="permitCode" label="输入许可密钥" stack-label :dense="true" />-->
            <q-stepper-navigation>
              <q-btn flat @click="$refs.stepper.previous()" color="primary" label="返回" class="q-ml-sm" />
              <q-btn :disable="!isPermitPass" @click="() => { done2 = true;$refs.stepper.next() }" color="primary" label="继续" />
            </q-stepper-navigation>
          </q-step>

          <q-step
              :name="3"
              title="领域"
              icon="settings"
              :done="consultFieldChipSelected.length > 0&&step > 3"
              style="min-height: 200px;"
          >
            <div class="text-h6 text-green-9">选择想要咨询的领域(最多3个)</div>

            <SelectChip ref="consultFieldChip" :selection-queue="consultFieldChipSelected"></SelectChip>
            <q-input
                v-model="consultLeaveNote"
                filled
                autogrow
                label="输入留言"
            />
            <div class="q-mt-sm">
              Queue: {{ consultFieldChipSelected }}
            </div>
            <q-stepper-navigation>
              <q-btn flat @click="$refs.stepper.previous()" color="primary" label="返回" class="q-ml-sm" />
              <q-btn :disable="consultFieldChipSelected.length === 0" @click="() => { done3 = true; $refs.stepper.next() }" color="primary" label="继续" />
            </q-stepper-navigation>
          </q-step>

          <q-step
              :name="4"
              title="时间"
              caption="Optional"
              icon="create_new_folder"
              :done="step > 4"
              style="min-height: 200px;"
          >
            <div class="q-mt-sm">
              <RangeDateTimePicker ref="consultRangeDTP" :start-date.sync="consultStartDate" :end-date.sync="consultEndDate" :consult-range-time.sync="consultRangeTime" :type="'dateTimeRange'" :max-range="5"></RangeDateTimePicker>
            </div>
            <div>
              <div class="text-overline text-orange-9">费用:</div>
              <q-item-label caption lines="1">
                单价: 16豆/30分钟，总计<strong class="text-deep-orange text-bold">{{consultRangeTime*16}}豆</strong>
              </q-item-label>
            </div>
            <q-stepper-navigation>
              <q-btn flat @click="$refs.stepper.previous()" color="primary" label="返回" class="q-ml-sm" />
              <q-btn :disable="consultStartDate===null || consultEndDate===null" @click="() => { done4 = true; $refs.stepper.next() }" color="primary" label="继续" />
            </q-stepper-navigation>
          </q-step>

          <q-step
              :name="5"
              title="确认"
              icon="assignment"
              :done="step > 5"
              style="min-height: 200px;"
          >
            <div class="q-mt-sm text-bold">
              咨询专家:<strong class="text-deep-orange">{{consultStartDateStr}} - {{consultEndDateStr}} ({{consultRangeTime}}小时)</strong>
            </div>
            <div class="q-mt-sm text-bold">
              已挑选的时间范围:<strong class="text-deep-orange">{{consultStartDateStr}} - {{consultEndDateStr}} ({{consultRangeTime}}小时)</strong>
            </div>
            <div class="q-mt-sm text-bold">
              需支付:<strong class="text-deep-orange text-bold">{{consultRangeTime*16}}豆</strong>
            </div>
            <q-stepper-navigation>
              <q-btn flat @click="$refs.stepper.previous()" color="primary" label="返回" class="q-ml-sm" />
              <q-btn @click="() => { done5 = true; $refs.stepper.next();showTextLoading() }" color="primary" label="确认" />
            </q-stepper-navigation>
          </q-step>

          <q-step
              :name="6"
              title="Create an ad"
              icon="add_comment"
              :done="step > 6"
              style="min-height: 200px;"
          >
            <transition
                appear
                enter-active-class="animated fadeIn"
                leave-active-class="animated fadeOut"
            >
              <div v-show="showFinalStepResult">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent vel magna eu risus laoreet tristique. Nulla ut fermentum elit, nec consequat augue. Morbi et dolor nec metus tincidunt pellentesque. Nullam non semper ante. Fusce pellentesque sagittis felis quis porta. Aenean condimentum neque sed erat suscipit malesuada. Nulla eget rhoncus enim. Duis dictum interdum eros.
              </div>
            </transition>
            <q-inner-loading :showing="finalStepLoadingVisible">
              <q-spinner-gears size="50px" color="primary" />
            </q-inner-loading>
          </q-step>

<!--          <template v-slot:navigation>-->
<!--            <q-stepper-navigation>-->
<!--              <q-btn @click="$refs.stepper.next()" color="primary" :label="step === 4 ? 'Finish' : 'Continue'" />-->
<!--              <q-btn v-if="step > 1" flat color="primary" @click="$refs.stepper.previous()" label="Back" class="q-ml-sm" />-->
<!--            </q-stepper-navigation>-->
<!--          </template>-->

          <template v-slot:message>
<!--            <q-banner v-if="step === 1" class="bg-purple-8 text-white q-px-lg">-->
<!--              必须选择想要咨询的领域-->
<!--            </q-banner>-->
            <q-banner v-if="step === 3" class="bg-orange-8 text-white q-px-lg">
              必须选择想要咨询的领域
            </q-banner>
            <q-banner v-else-if="step === 4" class="bg-green-8 text-white q-px-lg">
              The Ad template is disabled - this won't be displayed
            </q-banner>
<!--            <q-banner v-else class="bg-blue-8 text-white q-px-lg">-->
<!--              The final step is creating the ad...-->
<!--            </q-banner>-->
          </template>
        </q-stepper>
      </q-card-section>
    </q-card>
</template>

<script>
import SelectChip from "@/views/common/SelectChip";
import RangeDateTimePicker from "@/views/common/RangeDateTimePicker";
import {CONSTANT} from "@/utils/constant";
import { date } from 'quasar'
export default {
  name: "BookMarkStep",
  components:{
    SelectChip,
    RangeDateTimePicker
  },
  computed: {
    step1ButtonDisable () {
      if(this.consultFieldChipSelected.length>0){
        console.log("true")
        return false
      }
      console.log("false")
      return true
    }
  },
  data () {
    return {
      isNeedPermit: false,
      permitCode: '',
      permitInputHint:'输入许可密钥,验证通过后才可预约',
      isPermitPass: false,
      finalStepLoadingVisible: false,
      showFinalStepResult: false,
      step: 1,
      step1Agree: false,
      consultLeaveNote: '',
      consultFieldChipSelected:[],
      consultStartDate: null,
      consultEndDate: null,
      consultRangeTime: 0.5,
      consultStartDateStr: '',
      consultEndDateStr: '',
    }
  },
  watch: {
    // Whenever the movie prop changes, fetch new data
    consultStartDate: {
      // Will fire as soon as the component is created
      immediate: true,
      handler(newVal, oldVal) {
        oldVal
        if (date.isValid(newVal)) {
          this.consultStartDateStr = date.formatDate(newVal, CONSTANT.DATE_FORMAT)
        }
      }
      // deep: true
    },
    consultEndDate: {
      // Will fire as soon as the component is created
      immediate: true,
      handler(newVal, oldVal) {
        oldVal
        if (date.isValid(newVal)) {
          this.consultEndDateStr = date.formatDate(newVal, CONSTANT.DATE_FORMAT)
        }
      }
      // deep: true
    },
  },
  mounted() {

  },
  methods:{
    verifyPermitCode(){
      if(this.permitCode==="AAAA"){
        this.isPermitPass=true
        this.permitInputHint="验证通过"
      }else{
        this.isPermitPass=false
        this.permitInputHint="验证失败"
      }
    },
    showTextLoading () {
      this.finalStepLoadingVisible = true
      this.showFinalStepResult = false
      setTimeout(() => {
        this.finalStepLoadingVisible = false
        this.showFinalStepResult = true
      }, 3000)
    }
  }
}
</script>

<style scoped>
.my-card {
  transition: box-shadow .3s;
  height: 100%;
  border-radius:10px;
  width: 100%;
}
.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}
</style>
