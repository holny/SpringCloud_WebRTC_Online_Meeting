<template>
  <q-card class="my-card bg-grey-1 no-wrap full-width" flat bordered style="min-width: 500px">
    <q-card-section>
      <div class="text-h5 q-mt-sm text-center">站点设置</div>
    </q-card-section>
    <q-card-section>
      <q-separator spaced />
      <q-item-label header>业务信息</q-item-label>
      <q-list>
        <q-item>
          <q-item-section style="width: 50%">
            <div class="full-width">
              <q-select
                  class="full-width"
                  outlined
                  :dense="true"
                  v-model="removeTagId"
                  :options="tagOptions"
                  :option-value="opt => Object(opt) === opt && 'tagId' in opt ? opt.tagId : null"
                  :option-label="opt => Object(opt) === opt && 'tagName' in opt ? opt.tagName : '- 未知 -'"
                  emit-value
                  map-options
                  use-input
                  max-values="1"
                  :hint="removeTagId!=null?(removeTagId!=''?'TagId: '+removeTagId:''):''"
                  input-debounce="0"
                  label="服务器当前所有可见标签"
                  @new-value="tagCreateValue"
                  @filter="tagFilterFn"
                  @filter-abort="tagAbortFilterFn"
              >
                <template v-slot:after>
                  <q-btn v-show="removeTagId!=null" round dense flat icon="eva-trash-2-outline" @click="deleteTag">
                    <q-tooltip
                        transition-show="scale"
                        transition-hide="scale"
                    >
                      确认删除TagId:{{removeTagId}}
                    </q-tooltip>
                  </q-btn>
                </template>
              </q-select>
            </div>
          </q-item-section>

          <q-item-section>
            <q-item-label>全站所有标签</q-item-label>
            <q-item-label caption>
              删除/新建一个标签，
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
                  v-model="removeMainCategoryId"
                  :options="mainCategoryOptions"
                  :option-value="opt => Object(opt) === opt && 'mainCategoryId' in opt ? opt.mainCategoryId : null"
                  :option-label="opt => Object(opt) === opt && 'mainCategoryName' in opt ? opt.mainCategoryName : '- 未知 -'"
                  emit-value
                  map-options
                  use-input
                  max-values="1"
                  :hint="removeMainCategoryId!=null?(removeMainCategoryId!=''?'MainCategoryId: '+removeMainCategoryId:''):''"
                  input-debounce="0"
                  label="服务器当前所有主类别"
                  @new-value="mainCategoryCreateValue"
                  @filter="mainCategoryFilterFn"
                  @filter-abort="mainCategoryAbortFilterFn"
              >
                <template v-slot:after>
                  <q-btn v-show="removeMainCategoryId!=null" round dense flat icon="eva-trash-2-outline" @click="deleteMainCategory">
                    <q-tooltip
                        transition-show="scale"
                        transition-hide="scale"
                    >
                      确认删除MainCategoryId:{{removeMainCategoryId}}
                    </q-tooltip>
                  </q-btn>
                </template>
              </q-select>
            </div>
          </q-item-section>

          <q-item-section>
            <div class="full-width">
              <q-select
                  class="full-width"
                  outlined
                  :dense="true"
                  v-model="removeSubCategoryId"
                  :options="subCategoryOptions"
                  :option-value="opt => Object(opt) === opt && 'subCategoryId' in opt ? opt.subCategoryId : null"
                  :option-label="opt => Object(opt) === opt && 'subCategoryName' in opt ? opt.subCategoryName : '- 未知 -'"
                  emit-value
                  map-options
                  use-input
                  max-values="1"
                  :hint="removeSubCategoryId!=null?(removeSubCategoryId!=''?'SubCategoryId: '+removeSubCategoryId:''):''"
                  input-debounce="0"
                  label="服务器当前主类别对应的子类别"
                  @new-value="subCategoryCreateValue"
                  @filter="subCategoryFilterFn"
                  @filter-abort="subCategoryAbortFilterFn"
              >
                <template v-slot:after>
                  <q-btn v-show="removeSubCategoryId!=null" round dense flat icon="eva-trash-2-outline" @click="deleteSubCategory">
                    <q-tooltip
                        transition-show="scale"
                        transition-hide="scale"
                    >
                      确认删除SubCategoryId:{{removeSubCategoryId}}
                    </q-tooltip>
                  </q-btn>
                </template>
              </q-select>
            </div>
          </q-item-section>
        </q-item>
      </q-list>
    </q-card-section>
  </q-card>
</template>

<script>
import {FUN} from "@/utils/julyCommon";
import {isNotEmpty} from "@/utils/validate";

export default {
  name: "SiteSettings",
  data () {
    return {
      hostInfo: this.$store.getters.hostInfo,
      removeTagId: null,
      stringTagOptions: [],
      tagOptions: [],
      stringMainCategoryOptions: [],
      mainCategoryOptions: [],
      removeMainCategoryId: null,
      stringSubCategoryOptions: [],
      subCategoryOptions: [],
      removeSubCategoryId: null,

    }
  },
  watch: {
    // Whenever the movie prop changes, fetch new data
    removeMainCategoryId: {
      // Will fire as soon as the component is created
      immediate: false,
      handler(newVal,oldVal) {
        oldVal
        if(isNotEmpty(newVal)){
          this.getAllSubCategory(newVal)
        }
        // Fetch data about the movie
      }
      // deep: true
    },
  },
  mounted() {
    console.log("SiteSettings mounted")
    this.getAllTag()
    this.getAllMainCategory()
    this.tagOptions = FUN.deepCopy(this.stringTagOptions)
  },
  methods:{
    async getAllTag(){
      let _that=this
      if(isNotEmpty(this.hostInfo)){
        await this.$store.dispatch('tag/getAllTag')
            .then((data) => {
              _that.stringTagOptions.splice(0,_that.stringTagOptions.length)
              for(let index in data){
                _that.stringTagOptions.push({tagId:data[index].tagId,tagName:(data[index].typeStr!=null?data[index].typeStr:'')+data[index].tagName})
              }
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
      }
    },
    async tagCreateValue (val, done) {
      if (val.length >= 1) {
        if (!this.stringTagOptions.includes(val)) {
          console.log("tagCreateValue")
          console.log(val)
          if(val.length >=8){
            FUN.notify("标签长度不能大于8位",FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
          }else{
            let newTag = await this.createNewTag(val)
            if(isNotEmpty(newTag)){
              this.tagOptions.push({tagId:newTag.tagId,tagName:(newTag.typeStr!=null?newTag.typeStr:'')+newTag.tagName})
              this.stringTagOptions.push({tagId:newTag.tagId,tagName:(newTag.typeStr!=null?newTag.typeStr:'')+newTag.tagName})
              done({tagId:newTag.tagId,tagName:(newTag.typeStr!=null?newTag.typeStr:'')+newTag.tagName}, 'add-unique')
            }else{
              FUN.notify("标签新建失败",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            }

          }
        }else{
          console.log("tagCreateValue  tag already exits")
        }
      }
    },
    async createNewTag(tagName){
      if(isNotEmpty(tagName)){
        let newTag = null
        let newTagInfo = {
          tagName: tagName
        }
        await this.$store.dispatch('tag/newTag', newTagInfo)
            .then((data) => {
              newTag = data
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
        return newTag
      }else{
        return null
      }
    },
    async deleteTag(){
      let _that= this
      if(isNotEmpty(_that.removeTagId)){
        await this.$store.dispatch('tag/deleteTagByTagId', _that.removeTagId)
            .then(() => {
              FUN.notify("删除Tag成功! TagId:"+_that.removeTagId,FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
              let deleteIndex =-1
              for(let index in _that.stringTagOptions){
                if(_that.stringTagOptions[index].tagId===_that.removeTagId){
                  deleteIndex = index
                }
              }
              if(deleteIndex>=0){
                _that.stringTagOptions.splice(deleteIndex,1)
                _that.tagOptions = FUN.deepCopy(_that.stringTagOptions)
              }
              _that.removeTagId=null
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
      }else{
        console.log("删除tag 失败, tagId为空")
      }
    },
    tagFilterFn (val, update, abort) {
      abort
      if (this.tagOptions !== null) {
        update(() => {
          if (val === '') {
            this.tagOptions = FUN.deepCopy(this.stringTagOptions)
          }
          else {
            const needle = val.toLowerCase()
            this.tagOptions = FUN.deepCopy(this.stringTagOptions.filter(
                v => v.tagName.toLowerCase().indexOf(needle) > -1
            ))
          }
        })
        return
      }

      setTimeout(() => {
        update(() => {
          this.tagOptions = FUN.deepCopy(this.stringTagOptions)
        })
      }, 200)
    },
    tagAbortFilterFn () {
      // console.log('delayed filter aborted')
    },

    // **************MainCategory
    async getAllMainCategory(){
      let _that=this
      if(isNotEmpty(this.hostInfo)){
        await this.$store.dispatch('category/getAllMainCategory')
            .then((data) => {
              _that.stringMainCategoryOptions.splice(0,_that.stringMainCategoryOptions.length)
              for(let index in data){
                _that.stringMainCategoryOptions.push({mainCategoryId:data[index].mainCategoryId,mainCategoryName:data[index].mainCategoryName})
              }
              _that.mainCategoryOptions = FUN.deepCopy(_that.stringMainCategoryOptions)
              _that.removeMainCategoryId = null
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
      }
    },
    async mainCategoryCreateValue (val, done) {
      if (val.length >= 1) {
        if (!this.stringMainCategoryOptions.includes(val)) {
          console.log("mainCategoryCreateValue")
          console.log(val)
          if(val.length >7){
            FUN.notify("主类别长度不能大于7位",FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
          }else{
            let category = {
              mainCategoryName:val,
              typeStr: 'video',
            }
            let newCategory = await this.createNewCategory(category)
            if(isNotEmpty(newCategory)){
              this.mainCategoryOptions.push({mainCategoryId:newCategory.mainCategoryId,mainCategoryName:newCategory.mainCategoryName})
              this.stringMainCategoryOptions.push({mainCategoryId:newCategory.mainCategoryId,mainCategoryName:newCategory.mainCategoryName})
              done({mainCategoryId:newCategory.mainCategoryId,mainCategoryName:newCategory.mainCategoryName}, 'add-unique')
            }else{
              FUN.notify("新建主类别失败",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            }

          }
        }else{
          console.log("mainCategoryCreateValue  mainCategory already exits")
        }
      }
    },
    async createNewCategory(category){
      if(isNotEmpty(category)){
        let newTag = null

        await this.$store.dispatch('category/newCategory', category)
            .then((data) => {
              newTag = data
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
        return newTag
      }else{
        return null
      }
    },
    async deleteMainCategory(){
      let _that= this
      if(isNotEmpty(_that.removeMainCategoryId)){
        const data = {
          categoryId:_that.removeMainCategoryId,
          level:1
        }
        await this.$store.dispatch('category/deleteCategoryByIdAndLevel', data)
            .then(() => {
              FUN.notify("删除主类别成功成功! CategoryId:"+_that.removeMainCategoryId,FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
              let deleteIndex =-1
              for(let index in _that.stringMainCategoryOptions){
                if(_that.stringMainCategoryOptions[index].categoryId===_that.removeMainCategoryId){
                  deleteIndex = index
                }
              }
              if(deleteIndex>=0){
                _that.stringMainCategoryOptions.splice(deleteIndex,1)
                _that.mainCategoryOptions = FUN.deepCopy(_that.stringMainCategoryOptions)
              }
              _that.removeMainCategoryId=null
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
      }else{
        console.log("删除主类别 失败, categoryId为空")
      }
    },
    mainCategoryFilterFn (val, update, abort) {
      abort
      if (this.mainCategoryOptions !== null) {
        update(() => {
          if (val === '') {
            this.mainCategoryOptions = FUN.deepCopy(this.stringMainCategoryOptions)
          }
          else {
            const needle = val.toLowerCase()
            this.mainCategoryOptions = FUN.deepCopy(this.stringMainCategoryOptions.filter(
                v => v.mainCategoryName.toLowerCase().indexOf(needle) > -1
            ))
          }
        })
        return
      }

      setTimeout(() => {
        update(() => {
          this.mainCategoryOptions = FUN.deepCopy(this.stringMainCategoryOptions)
        })
      }, 200)
    },
    mainCategoryAbortFilterFn () {
      // console.log('delayed filter aborted')
    },

    // **************SubCategory
    async getAllSubCategory(mainCategoryId){
      let _that=this
      if(isNotEmpty(mainCategoryId)){
        await this.$store.dispatch('category/getAllSubCategory',mainCategoryId)
            .then((data) => {
              _that.stringSubCategoryOptions.splice(0,_that.stringSubCategoryOptions.length)
              for(let index in data){
                _that.stringSubCategoryOptions.push({subCategoryId:data[index].subCategoryId,subCategoryName:data[index].subCategoryName})
              }
              _that.subCategoryOptions = FUN.deepCopy(_that.stringSubCategoryOptions)
              _that.removeSubCategoryId = null
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
      }
    },
    async subCategoryCreateValue (val, done) {
      if (val.length >= 1) {
        if (!this.stringSubCategoryOptions.includes(val)) {
          console.log("subCategoryCreateValue")
          console.log(val)
          if(val.length >7){
            FUN.notify("子类别长度不能大于7位",FUN.NOTIFY_LEVEL_WARNING,FUN.NOTIFY_POSITION_TOP)
          }else{
            let category = {
              mainCategoryId: this.removeMainCategoryId,
              subCategoryName:val,
              typeStr: 'video',
            }
            let newCategory = await this.createNewCategory(category)
            if(isNotEmpty(newCategory)){
              this.subCategoryOptions.push({subCategoryId:newCategory.subCategoryId,subCategoryName:newCategory.subCategoryName})
              this.stringSubCategoryOptions.push({subCategoryId:newCategory.subCategoryId,subCategoryName:newCategory.subCategoryName})
              done({subCategoryId:newCategory.subCategoryId,subCategoryName:newCategory.subCategoryName}, 'add-unique')
            }else{
              FUN.notify("新建子类别失败",FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            }

          }
        }else{
          console.log("subCategoryCreateValue  subCategory already exits")
        }
      }
    },

    async deleteSubCategory(){
      let _that= this
      if(isNotEmpty(_that.removeSubCategoryId)){
        const data = {
          categoryId:_that.removeSubCategoryId,
          level:2
        }
        await this.$store.dispatch('category/deleteCategoryByIdAndLevel', data)
            .then(() => {
              FUN.notify("删除子类别成功成功! CategoryId:"+_that.removeSubCategoryId,FUN.NOTIFY_LEVEL_INFO,FUN.NOTIFY_POSITION_TOP)
              let deleteIndex =-1
              for(let index in _that.stringSubCategoryOptions){
                if(_that.stringSubCategoryOptions[index].categoryId===_that.removeSubCategoryId){
                  deleteIndex = index
                }
              }
              if(deleteIndex>=0){
                _that.stringSubCategoryOptions.splice(deleteIndex,1)
                _that.subCategoryOptions = FUN.deepCopy(_that.stringSubCategoryOptions)
              }
              _that.removeSubCategoryId=null
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
      }else{
        console.log("删除子类别 失败, categoryId为空")
      }
    },
    subCategoryFilterFn (val, update, abort) {
      abort
      if (this.subCategoryOptions !== null) {
        update(() => {
          if (val === '') {
            this.subCategoryOptions = FUN.deepCopy(this.stringSubCategoryOptions)
          }
          else {
            const needle = val.toLowerCase()
            this.subCategoryOptions = FUN.deepCopy(this.stringSubCategoryOptions.filter(
                v => v.subCategoryName.toLowerCase().indexOf(needle) > -1
            ))
          }
        })
        return
      }

      setTimeout(() => {
        update(() => {
          this.subCategoryOptions = FUN.deepCopy(this.stringSubCategoryOptions)
        })
      }, 200)
    },
    subCategoryAbortFilterFn () {
      // console.log('delayed filter aborted')
    },
  }
}
</script>

<style scoped>

</style>
