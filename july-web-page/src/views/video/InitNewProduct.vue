<template>
  <q-card class="my-card bg-grey-1 no-wrap full-width" flat bordered style="min-width: 500px">
        <q-card-section>
          <div class="text-h5 q-mt-sm text-center">上传视频</div>
        </q-card-section>
        <q-form ref="videoForm">
          <q-card-section>
            <q-separator spaced />
            <q-item-label header>视频信息</q-item-label>
            <q-item>
              <q-item-section style="width: 50%">
                <q-input class="full-width" outlined v-model="video.title"   label="视频标题"  counter maxlength="20" :rules="[ val => validTitle(val)|| titleWarning]" :dense="true">
    <!--              <template v-slot:append>-->
    <!--                <q-icon v-show="title_isCorrect!=null" :color="title_isCorrect?'positive':'negative'" :name="title_isCorrect?'check_circle':'cancel'" >-->
    <!--                  <q-tooltip-->
    <!--                      transition-show="scale"-->
    <!--                      transition-hide="scale"-->
    <!--                  >-->
    <!--                   标题需要符合要求-->
    <!--                  </q-tooltip>-->
    <!--                </q-icon>-->
    <!--              </template>-->
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
                    Tag: {{ video.tags || '[]' }}
                  </q-badge>
                  <TagSelector :max-size="3" :tag-type="'video'"  v-on:tagsChanged="updateVideoTag"></TagSelector>
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
                <q-badge color="secondary" class="q-mb-md">
                  Category: {{ video.categoryMain || 'null' }} - {{ video.categorySub || 'null' }}
                </q-badge>
                <CategoryGroupSelector :category-type="'video'" v-on:mainCategoryChanged="updateMainCategoryId" v-on:subCategoryChanged="updateSubCategoryId"></CategoryGroupSelector>
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
                      v-model="video.seriesId"
                      :options="videoSeriesOptions"
                      :option-value="opt => Object(opt) === opt && 'seriesId' in opt ? opt.seriesId : null"
                      :option-label="opt => Object(opt) === opt && 'seriesTitle' in opt ? opt.seriesTitle : '- 未知 -'"
                      emit-value
                      map-options
                      use-input
                      :hint="video.seriesId!=null?(video.seriesId!=''?'系列名: '+video.seriesId:''):''"
                      input-debounce="0"
                      label="系列"
                      @new-value="videoSeriesCreateValue"
                      @filter="videoSeriesFilterFn"
                      @filter-abort="videoSeriesAbortFilterFn"
                      :rules="[ val => validSeries(val)|| seriesWarning]"
                  >

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
                <q-input class="full-width" outlined v-model="video.info" autogrow   label="视频说明"  counter maxlength="150" :rules="[ val => validInfo(val)|| infoWarning]" :dense="true">

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
                         :type="isShowPWD ? 'text':'password'" label="请输入密码" debounce="200" :rules="[ val => validPassword(val)|| passwordWarning]"
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
                      :rules="[ val => validStatus(val)|| statusWarning]"
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
                    :url="uploadURL"
                    label="上传视频资源"
                    max-files="1"
                    ref="videoUploader"
                    :auto-upload="false"
                    field-name="file"
                    :headers="videoUploaderHeader"
                    accept=".mkv .webm .mp4, .avi, video/*"
                    @rejected="onUploadVideoRejected"

                />
              </q-item-section>
              <q-item-section >
                <q-uploader
                    :url="uploadURL"
                    label="上传视频Poster画报"
                    max-files="1"
                    hide-upload-btn
                    ref="posterUploader"
                    :auto-upload="false"
                    field-name="file"
                    :headers="posterUploaderHeader"
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
import {CONSTANT} from "@/utils/constant";
import {isNotEmpty, validateText, isPassword} from "@/utils/validate";
import {getToken} from "@/utils/auth";
import TagSelector from "@/views/common/TagSelector";
import CategoryGroupSelector from "@/views/common/CategoryGroupSelector";
export default {
  name: "InitNewProduct",
  components: {
    TagSelector,
    CategoryGroupSelector
  },
  data () {
    return {
      hostInfo: this.$store.getters.hostInfo,
      visible: false,
      titleWarning: '请输入符合格式的姓名',
      infoWarning: '请输入符合格式的说明',
      categoryMainWarning: '请选择属于合适的主类别',
      categorySubWarning: '请选择属于合适的子类别',
      seriesWarning: '请选择属于合适的系列',
      passwordWarning: '请输入合适的密码',
      statusWarning: '请选择合适的状态',
      isNeedPassword: false,
      isShowPWD: false,
      isPWDCapsMode: false,
      videoUploaderHeader:[{name: 'upload-file-type', value: 'video'},{name:'Authorization',value:'Bearer '+getToken()}],
      posterUploaderHeader:[{name: 'upload-file-type', value: 'poster'},{name:'Authorization',value:'Bearer '+getToken()}],
      video:{
        videoId:null,
        title: '',
        tags: null,
        info:'',
        categoryMain: null,
        categorySub:null,
        seriesId: null,
        isNeedTop: false,
        isNeedPush: false,
        password:'',
        status: null,
      },
      stringVideoCategoryMainOptions: [
        'Google', 'Facebook', 'Twitter', 'Apple', 'Oracle'
      ],
      stringVideoCategorySubOptions: [
        'Google', 'Facebook', 'Twitter', 'Apple', 'Oracle'
      ],
      stringVideoSeriesOptions: [
        {seriesId:'100001',seriesTitle:'Google'}, {seriesId:'100002',seriesTitle:'Facebook'}, {seriesId:'100003',seriesTitle:'Twitter'}, {seriesId:'100004',seriesTitle:'Apple'}, {seriesId:'100005',seriesTitle:'Oracle'}
      ],
      videoCategoryMainOptions: [],
      videoCategorySubOptions:[],
      videoSeriesOptions: [],
      videoStatusOptions:[ {code:CONSTANT.VIDEO_STATUS_LOCKED,desc:'锁定',inactive:false}, {code:CONSTANT.VIDEO_STATUS_PRIVATE,desc:'隐藏',inactive:false}, {code:CONSTANT.VIDEO_STATUS_PUBLIC,desc:'公开',inactive:false}]
    }
  },
  activated() {
    console.log("NewVideo activated")
    // this.video.title="activated"
    console.log(process.env.NODE_ENV)
    console.log(process.env.VUE_APP_PUBLIC_PATH)
    console.log(process.env.BASE_URL)
    console.log(process.env.VUE_APP_BACK_END_API)
    console.log(process.env.VUE_APP_PORT)

  },
  created() {
    console.log("NewVideo created")
    // this.video.title="created"
  },
  mounted() {
    console.log("NewVideo mounted")
    // this.video.title="mounted"
    this.getMyAllSeries()
    this.videoCategoryMainOptions = FUN.deepCopy(this.stringVideoCategoryMainOptions)
    this.videoCategorySubOptions = FUN.deepCopy(this.stringVideoCategorySubOptions)
    this.videoSeriesOptions = FUN.deepCopy(this.stringVideoSeriesOptions)
  },
  computed: {
    uploadURL() {
      if(this.video.videoId){
        return  process.env.VUE_APP_PUBLIC_PATH+'video/'+this.video.videoId+ '/uploadVideo'
      }else{
        return null
      }
    },
  },

  methods: {
    updateVideoTag(tags){
      this.video.tags=tags
    },
    updateMainCategoryId(val){
      this.video.categoryMain=val
    },
    updateSubCategoryId(val){
      this.video.categorySub=val
    },
    validTitle(val) {
      console.log('validTitle:'+val)
      if(!isNotEmpty(val)){
        this.titleWarning = '标题不能为空'
        return false
      }
      if(val.length>20){
        this.titleWarning ='标题字数超出限制'
        return false
      }
      if (validateText(val)) {
        return true
      } else {
        this.titleWarning ='请输入符合格式的姓名'
        return false
      }
    },
    validCategoryMain(val) {
      console.log('validCategoryMain'+val)
      if(!isNotEmpty(val)){
        return false
      }
      return true
    },
    validCategorySub(val) {
      console.log('validCategorySub'+val)
      if(!isNotEmpty(val)){
        return false
      }
      if (this.video.categoryMain !== null) {
        this.$refs.categoryMain.validate()
      }
      return true
    },
    validSeries(val) {
      console.log('validSeries'+val)
      if(!isNotEmpty(val)){
        return false
      }
      return true
    },
    validInfo(val) {
      console.log('validInfo'+val)
      if(!isNotEmpty(val)){
        this.infoWarning = '说明不能为空'
        return false
      }
      if(val.length>150){
        this.infoWarning ='说明字数超出限制'
        return false
      }
      if (validateText(val)) {
        return true
      } else {
        this.infoWarning ='请输入符合格式的说明'
        return false
      }
    },
    validPassword(val) {
      console.log('validPassword')
      if(!this.isNeedPassword){
        return true
      }
      if (!isNotEmpty(val) || val.length < 4 || !isPassword(val) ) {
        return false
      } else {
        return true
      }
    },
    validStatus(val) {
      console.log('validStatus'+val)
      if(!isNotEmpty(val)){
        return false
      }
      return true
    },
    async createNewVideo(){
      this.$refs.videoForm.validate().then(valid =>{
        console.log('videoForm')
        console.log(this.video)
        if (valid) {
          this.visible = true
          let _that = this
          this.$store.dispatch('video/newVideo', _that.video)
              .then((data) => {
                console.log('got host info successful')
                _that.video.videoId = data.videoId
                // _that.$refs.videoUploader.upload()
                _that.visible = false
              })
              .catch(() => {
                console.log('got host info fail')
                FUN.notify("无法获取当前用户信息",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
                _that.visible = false
              })
        }else{
          FUN.notify("视频信息填写校验错误", FUN.NOTIFY_LEVEL_ERROR, FUN.NOTIFY_POSITION_TOP)
        }
      })


    },
    videoCategoryMainFilterFn (val, update, abort) {
      abort
      if (this.videocategoryMainOptions !== null) {
        update(() => {
          if (val === '') {
            this.videocategoryMainOptions = FUN.deepCopy(this.stringVideoCategoryMainOptions)
          }
          else {
            const needle = val.toLowerCase()
            this.videocategoryMainOptions = FUN.deepCopy(this.stringVideoCategoryMainOptions.filter(
                v => v.toLowerCase().indexOf(needle) > -1
            ))
          }
        })
        return
      }

      setTimeout(() => {
        update(() => {
          this.videocategoryMainOptions = FUN.deepCopy(this.stringVideoCategoryMainOptions)
        })
      }, 2000)
    },

    videoCategoryMainAbortFilterFn () {
      // console.log('delayed filter aborted')
    },
    videoCategorySubFilterFn (val, update, abort) {
      abort
      if (this.videocategorySubOptions !== null) {
        update(() => {
          if (val === '') {
            this.videocategorySubOptions = FUN.deepCopy(this.stringVideoCategorySubOptions)
          }
          else {
            const needle = val.toLowerCase()
            this.videocategorySubOptions = FUN.deepCopy(this.stringVideoCategorySubOptions.filter(
                v => v.toLowerCase().indexOf(needle) > -1
            ))
          }
        })
        return
      }

      setTimeout(() => {
        update(() => {
          this.videocategorySubOptions = FUN.deepCopy(this.stringVideoCategorySubOptions)
        })
      }, 2000)
    },

    videoCategorySubAbortFilterFn () {
      // console.log('delayed filter aborted')
    },
    async videoSeriesCreateValue (val, done) {
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
        if (!this.stringVideoSeriesOptions.includes(val)) {
          console.log("videoSeriesCreateValue")
          console.log(val)
          if(val.length >=25){
            FUN.notify("系列名称长度不能大于25位",FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
          }else{
            let newSeriesId = await this.createNewSeries(val)
            if(isNotEmpty(newSeriesId)){
              this.videoSeriesOptions.push({seriesId:newSeriesId,seriesTitle:val})
              this.stringVideoSeriesOptions.push({seriesId:newSeriesId,seriesTitle:val})
              done({seriesId:newSeriesId,seriesTitle:val}, 'add-unique')
            }else{
              FUN.notify("系列名称新建失败",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            }

          }
        }else{
          console.log("videoSeriesCreateValue already exits")
        }
      }
    },
    async createNewSeries(seriesTitle){
      if(isNotEmpty(seriesTitle)){
        let seriesId = null
        let newSeriesInfo = {
          seriesTitle: seriesTitle
        }
        await this.$store.dispatch('series/newSeries', newSeriesInfo)
            .then((data) => {
              seriesId = data.seriesId
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
        return seriesId
      }else{
        return null
      }
    },
    async getMyAllSeries(){
      let _that=this
      if(isNotEmpty(this.hostInfo)){
        await this.$store.dispatch('series/myAllSeries', this.hostInfo.userId)
            .then((data) => {
              _that.stringVideoSeriesOptions.splice(0,_that.stringVideoSeriesOptions.length)
              for(let index in data){
                _that.stringVideoSeriesOptions.push({seriesId:data[index].seriesId,seriesTitle:data[index].seriesTitle})
              }
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
      }
    },
    videoSeriesFilterFn (val, update, abort) {
      abort
      if (this.videoSeriesOptions !== null) {
        update(() => {
          if (val === '') {
            this.videoSeriesOptions = FUN.deepCopy(this.stringVideoSeriesOptions)
          }
          else {
            const needle = val.toLowerCase()
            this.videoSeriesOptions = FUN.deepCopy(this.stringVideoSeriesOptions.filter(
                v => v.seriesTitle.toLowerCase().indexOf(needle) > -1
            ))
          }
        })
        return
      }

      setTimeout(() => {
        update(() => {
          this.videoSeriesOptions = FUN.deepCopy(this.stringVideoSeriesOptions)
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
