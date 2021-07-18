<template>
  <div class="full-width">
    <q-select
        class="inline-block"
        outlined
        :dense="true"
        v-model="selectedMainCategoryId"
        :options="mainCategoryOptions"
        :option-value="opt => Object(opt) === opt && 'mainCategoryId' in opt ? opt.mainCategoryId : null"
        :option-label="opt => Object(opt) === opt && 'mainCategoryName' in opt ? opt.mainCategoryName : '- 未知 -'"
        emit-value
        map-options
        use-input
        input-debounce="0"
        label="主类别"
        @filter="mainCategoryFilterFn"
        @filter-abort="mainCategoryAbortFilterFn"
        style="width: 48%"
        ref="categoryMain"
        :rules="[ val => validCategoryMain(val)|| categoryMainWarning]"
    />
    <q-select
        class="inline-block float-right"
        outlined
        :dense="true"
        v-model="selectedSubCategoryId"
        emit-value
        map-options
        use-input
        label="子类别"
        :options="subCategoryOptions"
        :option-value="opt => Object(opt) === opt && 'subCategoryId' in opt ? opt.subCategoryId : null"
        :option-label="opt => Object(opt) === opt && 'subCategoryName' in opt ? opt.subCategoryName : '- 未知 -'"
        @filter="subCategoryFilterFn"
        @filter-abort="subCategoryAbortFilterFn"
        style="width: 48%"
        ref="categorySub"
        :rules="[ val => validCategorySub(val)|| categorySubWarning]"
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
</template>

<script>
import {isNotEmpty} from "@/utils/validate";
import {FUN} from "@/utils/julyCommon";

export default {
  name: "CategoryGroupSelector",
  props: {
    categoryType: {
      type: String,
      require: false,
      default: 'video',
    },
  },
  data () {
    return {
      hostInfo: this.$store.getters.hostInfo,
      stringMainCategoryOptions: [],
      mainCategoryOptions: [],
      selectedMainCategoryId: null,
      stringSubCategoryOptions: [],
      subCategoryOptions: [],
      selectedSubCategoryId: null,
      categoryMainWarning: '请选择属于合适的主类别',
      categorySubWarning: '请选择属于合适的子类别',
    }
  },
  watch: {
    selectedMainCategoryId: {
      immediate: true,
      handler(newVal, oldVal) {
        oldVal
        this.$emit('mainCategoryChanged',newVal)
        if(isNotEmpty(newVal)){
          this.getAllSubCategory(newVal)
        }
      },
      deep: true
    },
    selectedSubCategoryId: {
      immediate: true,
      handler(newVal, oldVal) {
        oldVal
        this.$emit('subCategoryChanged',newVal)
      },
      deep: true
    },
  },
  mounted() {
    console.log("CategoryGroupSelector mounted")
    this.getAllMainCategory(this.categoryType)
  },
  methods:{
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
      if (this.selectedMainCategoryId !== null) {
        this.$refs.categoryMain.validate()
      }
      return true
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

    async getAllMainCategory(categoryType){
      let _that=this
      if(isNotEmpty(this.hostInfo)){
        await this.$store.dispatch('category/getMainCategoryByType',categoryType)
            .then((data) => {
              _that.stringMainCategoryOptions.splice(0,_that.stringMainCategoryOptions.length)
              for(let index in data){
                _that.stringMainCategoryOptions.push({mainCategoryId:data[index].mainCategoryId,mainCategoryName:data[index].mainCategoryName})
              }
              _that.mainCategoryOptions = FUN.deepCopy(_that.stringMainCategoryOptions)
              _that.selectedMainCategoryId = null
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
      }
    },
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
              _that.selectedSubCategoryId = null
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
      }
    },
  }
}
</script>

<style scoped>

</style>
