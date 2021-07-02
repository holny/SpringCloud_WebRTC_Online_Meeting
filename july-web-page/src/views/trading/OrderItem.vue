<template>
  <q-card class="column my-card bg-grey-1 no-wrap" style="min-width: 1000px">
    <q-card-section class="q-pb-xs">
      <div class="text-subtitle1 inline-block text-bold">预约订单:&nbsp;<strong class="text-deep-orange">12312312312312312412312312</strong></div>
      <div class="text-subtitle1 float-right inline-block"><count-timer ></count-timer>
        <q-tooltip>
          <strong >coming soon</strong>
        </q-tooltip>
      </div>
    </q-card-section>
    <q-card-section class="q-pt-xs">
      <q-item class="column float-left" style="width: 500px">
        <q-item-label header class="full-width col" style="height: 50px">专家信息</q-item-label>
        <div class="full-width col justify-center">
          <q-item-section avatar class="items-center q-gutter-y-sm">
            <q-avatar size="60px">
              <img src="https://cdn.quasar.dev/img/avatar2.jpg">
              <q-badge rounded floating color="yellow-10">
                <q-icon name="done" color="white" />
                <q-tooltip>
                  <strong >已认证</strong>
                </q-tooltip>
              </q-badge>
            </q-avatar>
            <q-item-label clickable class="text-subtitle1 text-black items-center">
              <q-badge color="blue">
                Lv.12
              </q-badge>
            </q-item-label>
          </q-item-section>
          <q-item-section class="text-center">
            <div>
              <q-icon name="person" size="xs" class="q-mr-sm inline-block text-teal">
                <q-tooltip>
                  <strong >姓名</strong>
                </q-tooltip>
              </q-icon>
              <q-item-label class="text-h6 q-mb-sm inline-block">
                fasdawdawdfasdawd
              </q-item-label>
            </div>
            <div>
              <q-icon name="local_offer" size="xs"  class="q-mr-sm inline-block text-brown">
                <q-tooltip>
                  <strong >ID</strong>
                </q-tooltip>
              </q-icon>
              <q-item-label class="text-caption q-mb-sm inline-block">
                fawfawfawf
              </q-item-label>
            </div>
            <div>
              <q-icon name="tag" size="xs"  class="q-mr-sm inline-block text-orange">
                <q-tooltip>
                  <strong >擅长</strong>
                </q-tooltip>
              </q-icon>
              <q-item-label class="inline-block q-gutter-x-md  q-mb-sm"><q-badge outline color="primary" label="Outline" />
                <q-badge outline color="orange" label="Outline" />
                <q-badge outline color="secondary" label="Outline" /></q-item-label>
            </div>
          </q-item-section>
        </div>
      </q-item>
      <q-separator vertical color="orange" class="inline-block" style="height: 300px" />
      <q-item class="vertical-top inline-block">
        <q-item-label header>预约信息</q-item-label>
        <q-card-actions  class="q-gutter-x-md">
          <q-list>
            <q-item>
              <q-item-section>
                <q-item-label class="text-overline text-orange-9">选择领域:</q-item-label>
                <q-item-label caption lines="2">
                  <q-chip v-for="(value, key) in candidateMap" :key="key"  :color="getColor(key)" text-color="white" :dense="true" :icon="value.icon" >
                    {{value.name}}
                  </q-chip>
                </q-item-label>
              </q-item-section>
            </q-item>
            <q-separator spaced inset />
            <q-item>
              <q-item-section>
                <q-item-label class="text-overline text-orange-9">时间范围:</q-item-label>
                <q-item-label caption lines="1">
                  <strong class="text-deep-orange text-bold">2021-07-05 14:30:13 - 2021-07-05 15:30:13 &nbsp; (1小时)</strong>
                </q-item-label>
              </q-item-section>
            </q-item>
            <q-separator spaced inset />
            <q-item>
              <q-item-section>
                <q-item-label class="text-overline text-orange-9">费用:</q-item-label>
                <q-item-label caption lines="1">
                  单价: 16豆/30分钟，总计<strong class="text-deep-orange text-bold">202豆</strong>
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-list>

        </q-card-actions>
      </q-item>
    </q-card-section>
    <q-card-actions  class="q-gutter-x-md justify-end">
      <div class="text-center justify-center">
        <q-btn flat round color="positive" icon="paid" ><q-tooltip>支付费用</q-tooltip></q-btn>
        <div class="text-subtitle2" ><count-timer ></count-timer></div>
      </div>
      <div>
        <q-btn flat round color="accent" icon="bookmark" ><q-tooltip>预约咨询</q-tooltip></q-btn>
        <div class="text-subtitle2 text-center text-accent" >咨询</div>
      </div>
      <div>
        <q-btn flat round color="positive" icon="forum" ><q-tooltip>私聊</q-tooltip></q-btn>
        <div class="text-subtitle2 text-center text-positive" >私聊</div>
      </div>
      <div>
        <q-btn flat round color="primary" icon="share" ><q-tooltip>个人主页</q-tooltip></q-btn>
        <div class="text-subtitle2 text-center text-primary" >主页</div>
      </div>
    </q-card-actions>
  </q-card>
</template>

<script>
import CountTimer from "@/views/common/CountTimer";
import {randomColor} from "@/utils/random";

export default {
  name: "OrderItem",
  props: {
    candidateMap:{
      type: Object,
      require: false,
      default: () => ({'1':{name:'Ice cream',val:'ice',icon:'cake',selected: false},'2':{name:'Eclair',val:'eclair',icon:'cake',selected: false},'3':{name:'Cupcake',val:'cupcake',icon:'cake',selected: false},'4':{name:'Gingerbread',val:'gingerbread',icon:'cake',selected: false}}),
    },
  },
  components:{
    CountTimer
  },
  data () {
    return {
      timer: '',
    }
  },
  mounted() {
  },
  methods:{
    getColor(index){
      return randomColor(index,[]);
    },

  }
}
</script>

<style scoped>

</style>
