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
              <q-btn :disable="!step1Agree" @click="() => { done1 = true; step = 2 }" color="primary" label="继续" />
            </q-stepper-navigation>
          </q-step>
          <q-step
              :name="2"
              title="领域"
              icon="settings"
              :done="consultFieldChipSelected.length > 0&&step > 2"
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
              <q-btn flat @click="step = 1" color="primary" label="返回" class="q-ml-sm" />
              <q-btn :disable="consultFieldChipSelected.length === 0" @click="() => { done2 = true; step = 3 }" color="primary" label="继续" />
            </q-stepper-navigation>
          </q-step>

          <q-step
              :name="3"
              title="时间"
              caption="Optional"
              icon="create_new_folder"
              :done="step > 3"
              style="min-height: 200px;"
          >
            <div class="q-mt-sm">
              <RangeDateTimePicker ref="consultRangeDTP" :start-date.sync="consultStartDate" :end-date.sync="consultEndDate" :consult-range-time.sync="consultRangeTime" :type="'dateTimeRange'" :max-range="5"></RangeDateTimePicker>
            </div>
            <q-stepper-navigation>
              <q-btn flat @click="step = 2" color="primary" label="返回" class="q-ml-sm" />
              <q-btn :disable="consultStartDate===null || consultEndDate===null" @click="() => { done3 = true; step = 4 }" color="primary" label="继续" />
            </q-stepper-navigation>
          </q-step>

          <q-step
              :name="4"
              title="确认"
              icon="assignment"
              :done="step > 4"
              style="min-height: 200px;"
          >
            <div class="q-mt-sm text-bold">
              咨询专家:<strong class="text-deep-orange">{{consultStartDateStr}} - {{consultEndDateStr}} ({{consultRangeTime}}小时)</strong>
            </div>
            <div class="q-mt-sm text-bold">
              已挑选的时间范围:<strong class="text-deep-orange">{{consultStartDateStr}} - {{consultEndDateStr}} ({{consultRangeTime}}小时)</strong>
            </div>
            <q-stepper-navigation>
              <q-btn flat @click="step = 3" color="primary" label="返回" class="q-ml-sm" />
              <q-btn @click="() => { done4 = true; step = 5 }" color="primary" label="继续" />
            </q-stepper-navigation>
          </q-step>

          <q-step
              :name="5"
              title="Create an ad"
              icon="add_comment"
              :done="step > 5"
              style="min-height: 200px;"
          >
            Try out different ad text to see what brings in the most customers, and learn how to
            enhance your ads using features like ad extensions. If you run into any problems with
            your ads, find out how to tell if they're running and how to resolve approval issues.
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
            <q-banner v-if="step === 2" class="bg-orange-8 text-white q-px-lg">
              必须选择想要咨询的领域
            </q-banner>
            <q-banner v-else-if="step === 3" class="bg-green-8 text-white q-px-lg">
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
