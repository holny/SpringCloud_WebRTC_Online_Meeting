<template>
  <q-card class="my-card bg-grey-1 no-wrap full-width" flat bordered style="min-width: 500px">
    <q-card-section>
      <div class="text-h5 q-mt-sm text-center">上传视频</div>
    </q-card-section>
    <q-form>
      <q-card-section>
        <q-separator spaced />
        <q-item-label header>视频信息</q-item-label>
        <q-item>
          <q-item-section style="width: 50%">
            <q-input class="full-width" outlined v-model="video.title"   label="视频标题"  counter maxlength="20" :dense="true">
              <template v-slot:append>
                <q-icon v-show="title_isCorrect!=null" :color="title_isCorrect?'positive':'negative'" :name="title_isCorrect?'check_circle':'cancel'" >
                  <q-tooltip
                      transition-show="scale"
                      transition-hide="scale"
                  >
                   标题需要符合要求
                  </q-tooltip>
                </q-icon>
              </template>
            </q-input>
          </q-item-section>
          <q-item-section>
            <div>
              <q-toggle
                  v-model="video.isNeedTop"
                  checked-icon="check"
                  color="green"
                  label="置顶视频"
                  unchecked-icon="clear"
              />
              <q-toggle
                  v-model="video.isNeedPush"
                  checked-icon="check"
                  color="green"
                  label="推送视频"
                  unchecked-icon="clear"
              />
            </div>
          </q-item-section>
        </q-item>
        <q-item>
          <q-item-section style="width: 50%">

            <div style="min-width: 250px;width: 100%">
              <q-badge color="secondary" class="q-mb-md">
                Model: {{ video.tag || '[]' }}
              </q-badge>

              <q-select
                  outlined
                  v-model="video.tag"
                  multiple
                  :options="videoTagOptions"
                  use-input
                  use-chips
                  input-debounce="0"
                  @filter="videoTagFilterFn"
                  :dense="true"
                  label="视频标签"
                  class="full-width"
                  max-values="3"
              />
            </div>
          </q-item-section>

          <q-item-section>
            <q-item-label>选择视频标签</q-item-label>
            <q-item-label caption>
              多选，选择合适视频标签
            </q-item-label>
          </q-item-section>
        </q-item>
        <q-item>
          <q-item-section style="width: 50%">
            <div class="full-width">
              <q-select
                  class="inline-block"
                  outlined
                  :dense="true"
                  v-model="video.priCategory"
                  :options="videoPriCategoryOptions"
                  use-input
                  input-debounce="0"
                  label="视频主类别"
                  @filter="videoPriCategoryFilterFn"
                  @filter-abort="videoPriCategoryAbortFilterFn"
                  style="width: 48%"
              />
              <q-select
                  class="inline-block float-right"
                  outlined
                  :dense="true"
                  v-model="video.subCategory"
                  use-input
                  label="视频子类别"
                  :options="videoSubCategoryOptions"
                  @filter="videoSubCategoryFilterFn"
                  @filter-abort="videoSubCategoryAbortFilterFn"
                  style="width: 48%"
              >
                <template v-slot:no-option>
                  <q-item>
                    <q-item-section class="text-grey">
                      No results
                    </q-item-section>
                  </q-item>
                </template>
              </q-select>
            </div>
          </q-item-section>

          <q-item-section>
            <q-item-label>视频类别</q-item-label>
            <q-item-label caption>
              先选择视频主类别，再选择子类别
            </q-item-label>
          </q-item-section>
        </q-item>
        <q-item>
          <q-item-section style="width: 50%">
            <div class="full-width">
              <q-select
                  class="full-width"
                  outlined
                  :dense="true"
                  v-model="video.series"
                  :options="videoSeriesOptions"
                  :option-value="opt => Object(opt) === opt && 'id' in opt ? opt.id : null"
                  :option-label="opt => Object(opt) === opt && 'name' in opt ? opt.name : '- 未知 -'"
                  emit-value
                  map-options
                  use-input
                  :hint="video.series!=null?(video.series!=''?'系列名: '+video.series:''):''"
                  input-debounce="0"
                  label="系列"
                  @new-value="videoSeriesCreateValue"
                  @filter="videoSeriesFilterFn"
                  @filter-abort="videoSeriesAbortFilterFn"
              >
                <template v-slot:append>
                  <q-icon v-show="series_isCorrect!=null" :color="series_isCorrect?'positive':'negative'" :name="series_isCorrect?'check_circle':'cancel'" >
                    <q-tooltip
                        transition-show="scale"
                        transition-hide="scale"
                    >
                      系列名称需要符合要求
                    </q-tooltip>
                  </q-icon>
                </template>
              </q-select>
            </div>
          </q-item-section>

          <q-item-section>
            <q-item-label>视频系列</q-item-label>
            <q-item-label caption>
              选择/新建一个视频系列，新视频会加入到这个系列中，
            </q-item-label>
          </q-item-section>
        </q-item>
        <q-item>
          <q-item-section style="width: 50%">
            <q-input class="full-width" outlined v-model="video.info" autogrow   label="视频说明"  counter maxlength="150" :dense="true">
              <template v-slot:append>
                <q-icon v-show="info_isCorrect!=null" :color="info_isCorrect?'positive':'negative'" :name="info_isCorrect?'check_circle':'cancel'" >
                  <q-tooltip
                      transition-show="scale"
                      transition-hide="scale"
                  >
                    说明文本需要符合要求
                  </q-tooltip>
                </q-icon>
              </template>
            </q-input>
          </q-item-section>

          <q-item-section>
            <q-item-label>视频说明</q-item-label>
            <q-item-label caption>
              提供一个对于此视频的说明，观看者可见
            </q-item-label>
          </q-item-section>
        </q-item>
        <q-separator spaced />
        <q-item-label header>权限控制</q-item-label>
        <q-item>
          <q-item-section style="width: 50%">
            <q-toggle
                v-model="isNeedPassword"
                color="pink"
                icon="eva-lock-outline"
                unchecked-icon="eva-unlock-outline"
                label="是否开启密码"
                @input="videoPasswordToggleSwitch"
            >
              <q-tooltip
                  transition-show="scale"
                  transition-hide="scale"
              >
                若开启密码，视频将默认保存为私密状态,但其他人密码验证通过也可观看
              </q-tooltip>
            </q-toggle>
          </q-item-section>
          <q-item-section >
            <q-input class="col" :v-model="video.password" ref="password" :readonly="!isNeedPassword"
                     :type="isShowPWD ? 'text':'password'" label="请输入密码" debounce="200"
                     autocomplete="off" @blur="isPWDCapsMode = false" @keyup.native="videoPasswordKeyUpEvent"
                     maxlength="25" placeholder="Password" :dense="true" lazy-rules outlined>
              <template v-slot:prepend>
                <q-icon name="password" />
              </template>
              <template v-slot:append>
                <q-icon
                    :name="isShowPWD ? 'visibility':'visibility_off'"
                    class="cursor-pointer"
                    @click="isShowPWD = !isShowPWD"
                />
                <q-icon v-show="isPWDCapsMode"
                        name="font_download"
                />
              </template>
            </q-input>
          </q-item-section>
        </q-item>
        <q-item>
          <q-item-section style="width: 50%">

            <div style="min-width: 250px;width: 100%">
              <q-badge color="secondary" class="q-mb-md">
                Model: {{ video.status }}
              </q-badge>

              <q-select
                  outlined
                  :readonly="isNeedPassword"
                  v-model="video.status"
                  :options="videoStatusOptions"
                  :option-value="opt => Object(opt) === opt && 'code' in opt ? opt.code : null"
                  :option-label="opt => Object(opt) === opt && 'desc' in opt ? opt.desc : '- 未知 -'"
                  :option-disable="opt => Object(opt) === opt ? opt.inactive === true : true"
                  ref="videoStatusSelect"
                  emit-value
                  map-options
                  input-debounce="0"
                  :dense="true"
                  label="视频状态"
                  class="full-width"
                  max-values="1"
              />
            </div>
          </q-item-section>

          <q-item-section>
            <q-item-label>选择视频状态</q-item-label>
            <q-item-label caption>
              选择视频是直接公开发布还是暂时私密
            </q-item-label>
          </q-item-section>
        </q-item>
        <q-separator spaced />
        <q-item-label header>上传资源</q-item-label>

        <q-item>
          <q-item-section style="width: 50%">
            <q-uploader
                url="http://localhost:4444/upload"
                label="上传视频资源"
                max-files="1"
                hide-upload-btn
                :headers="[{name: 'X-Custom-Timestamp', value: 1550240306080}]"
                accept=".mp4, .avi, video/*"
                @rejected="onUploadVideoRejected"
            />
          </q-item-section>
          <q-item-section >
            <q-uploader
                url="http://localhost:4444/upload"
                label="上传视频Poster画报"
                max-files="1"
                hide-upload-btn
                :headers="[{name: 'X-Custom-Timestamp', value: 1550240306080}]"
                accept=".png, .jpg, .jpeg, image/*"
                max-file-size="10485760"
                @rejected="onUploadVideoRejected"
            />
          </q-item-section>
        </q-item>
      </q-card-section>
      <q-card-section class="text-center">
        <q-btn color="primary" @click="createNewVideo">创建
        </q-btn>
      </q-card-section>
    </q-form>
    <q-inner-loading :showing="visible">
      <q-spinner-gears size="50px" color="primary" />
    </q-inner-loading>
  </q-card>
</template>

<script>
import {FUN} from "@/utils/julyCommon";

const stringVideoTagOptions = ['Google', 'Facebook', 'Twitter', 'Apple', 'Oracle']
const stringVideoPriCategoryOptions = [
  'Google', 'Facebook', 'Twitter', 'Apple', 'Oracle'
]
const stringVideoSubCategoryOptions = [
  'Google', 'Facebook', 'Twitter', 'Apple', 'Oracle'
]
let stringVideoSeriesOptions = [
  {id:'100001',name:'Google'}, {id:'100002',name:'Facebook'}, {id:'100003',name:'Twitter'}, {id:'100004',name:'Apple'}, {id:'100005',name:'Oracle'}
]
export default {
  name: "NewVideo",
  data () {
    return {
      visible: false,
      title_isCorrect: null,
      info_isCorrect: null,
      series_isCorrect: null,
      isNeedPassword: false,
      isShowPWD: false,
      isPWDCapsMode: false,
      video:{
        title: '',
        tag:[],
        info:'',
        priCategory: null,
        subCategory:null,
        series: null,
        isNeedTop: false,
        isNeedPush: false,
        password:'',
        status: null,
      },
      videoTagOptions: stringVideoTagOptions,
      videoPriCategoryOptions: stringVideoSubCategoryOptions,
      videoSubCategoryOptions: stringVideoSubCategoryOptions,
      videoSeriesOptions: stringVideoSeriesOptions,
      videoStatusOptions:[ {code:'public',desc:'公开发布',inactive:false},  {code:'private',desc:'私密'}]
    }
  },

  methods: {
    createNewVideo(){
      this.visible = true

      setTimeout(() => {
        this.visible = false
      }, 3000)
    },

    videoTagFilterFn (val, update) {
      update(() => {
        if (val === '') {
          this.videoTagOptions = stringVideoTagOptions
        }
        else {
          const needle = val.toLowerCase()
          this.videoTagOptions = stringVideoTagOptions.filter(
              v => v.toLowerCase().indexOf(needle) > -1
          )
        }
      })
    },
    videoPriCategoryFilterFn (val, update, abort) {
      abort
      if (this.videoPriCategoryOptions !== null) {
        update(() => {
          if (val === '') {
            this.videoPriCategoryOptions = stringVideoPriCategoryOptions
          }
          else {
            const needle = val.toLowerCase()
            this.videoPriCategoryOptions = stringVideoPriCategoryOptions.filter(
                v => v.toLowerCase().indexOf(needle) > -1
            )
          }
        })
        return
      }

      setTimeout(() => {
        update(() => {
          this.videoPriCategoryOptions = stringVideoPriCategoryOptions
        })
      }, 2000)
    },

    videoPriCategoryAbortFilterFn () {
      // console.log('delayed filter aborted')
    },
    videoSubCategoryFilterFn (val, update, abort) {
      abort
      if (this.videoSubCategoryOptions !== null) {
        update(() => {
          if (val === '') {
            this.videoSubCategoryOptions = stringVideoSubCategoryOptions
          }
          else {
            const needle = val.toLowerCase()
            this.videoSubCategoryOptions = stringVideoSubCategoryOptions.filter(
                v => v.toLowerCase().indexOf(needle) > -1
            )
          }
        })
        return
      }

      setTimeout(() => {
        update(() => {
          this.videoSubCategoryOptions = stringVideoSubCategoryOptions
        })
      }, 2000)
    },

    videoSubCategoryAbortFilterFn () {
      // console.log('delayed filter aborted')
    },
    videoSeriesCreateValue (val, done) {
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
        if (!stringVideoSeriesOptions.includes(val)) {
          console.log("videoSeriesCreateValue")
          console.log(val)
          if(val.length >=10){
            this.series_isCorrect = false
          }else{
            this.series_isCorrect = true
            this.videoSeriesOptions.push({id:'100006',name:val})
            stringVideoSeriesOptions.push({id:'100006',name:val})
            done({id:'100006',name:val}, 'add-unique')
          }
        }else{
          this.series_isCorrect = null
        }
      }
    },
    videoSeriesFilterFn (val, update, abort) {
      abort
      if (this.videoSeriesOptions !== null) {
        update(() => {
          if (val === '') {
            this.videoSeriesOptions = stringVideoSeriesOptions
          }
          else {
            const needle = val.toLowerCase()
            this.videoSeriesOptions = stringVideoSeriesOptions.filter(
                v => v.name.toLowerCase().indexOf(needle) > -1
            )
          }
        })
        return
      }

      setTimeout(() => {
        update(() => {
          this.videoSeriesOptions = stringVideoSeriesOptions
        })
      }, 200)
    },

    videoSeriesAbortFilterFn () {
      // console.log('delayed filter aborted')
    },
    onUploadVideoRejected (rejectedEntries) {
      // Notify plugin needs to be installed
      // https://quasar.dev/quasar-plugins/notify#Installation
      FUN.notify(`${rejectedEntries.length} file(s) did not pass validation constraints`,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
    },
    onUploadVideoPosterRejected (rejectedEntries) {
      // Notify plugin needs to be installed
      // https://quasar.dev/quasar-plugins/notify#Installation
      FUN.notify(`${rejectedEntries.length} file(s) did not pass validation constraints`,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
    },
    videoPasswordKeyUpEvent(e){
      const {key} = e
      this.isPWDCapsMode = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    videoPasswordToggleSwitch(value){
      console.log("videoPasswordToggleSwitch")
      console.log(value)
      if(value===true){
        // this.$refs.videoStatusSelect.setOptionIndex('prvate')
        this.video.status='private'
      }else{
        this.video.password=''
      }
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
