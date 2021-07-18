<template>
  <div class="full-width">
    <q-select
        class="full-width"
        outlined
        :dense="true"
        v-model="selectedTag"
        :options="tagOptions"
        :option-value="opt => Object(opt) === opt && 'tagId' in opt ? opt.tagId : null"
        :option-label="opt => Object(opt) === opt && 'tagName' in opt ? opt.tagName : '- 未知 -'"
        emit-value
        map-options
        use-input
        :max-values="maxSize"
        :multiple="maxSize>1"
        :hint="selectedTag!=null?(selectedTag!=''?'TagId: '+selectedTag:''):''"
        input-debounce="250"
        label="标签"
        @filter="tagFilterFn"
        @filter-abort="tagAbortFilterFn"
        :rules="[ val => validTags(val)|| tagsWarning]"
        :lazy-rules="true"
    >
    </q-select>
  </div>
</template>

<script>
import {FUN} from "@/utils/julyCommon";
import {isNotEmpty} from "@/utils/validate";

export default {
  name: "SeriesSelector",
  props: {
    maxSize: {
      type: Number,
      require: false,
      default: 3,
    },
    tagType: {
      type: String,
      require: false,
      default: 'video',
    },
  },
  data () {
    return {
      hostInfo: this.$store.getters.hostInfo,
      stringTagOptions: [],
      selectedTag: null,
      tagOptions: [],
      tagsWarning: '请选择合适的标签',
    }
  },
  watch: {
    selectedTag: {
      immediate: true,
      handler(newVal, oldVal) {
        oldVal
        this.$emit('tagsChanged',newVal)
        // Fetch data about the movie
      },
      deep: true
    },
  },
  mounted() {
    console.log("SeriesSelector mounted")
    this.getAllTag()
    this.initMode()
  },
  methods:{
    initMode(){
      if(this.maxSize>1){
        this.selectedTag=[]
      }else{
        this.selectedTag=''
      }
    },
    validTags(val) {
      console.log('validTags'+val)
      if(!isNotEmpty(val)){
        this.tagsWarning = '必须选择至少一个标签'
        return false
      }
      if(val.length>3){
        this.tagsWarning ='不能选择超过三个'
        return false
      }
      return true
    },
    async getAllTag(){
      let _that=this
      if(isNotEmpty(this.hostInfo)){
        await this.$store.dispatch('tag/getAllTagByType',_that.tagType)
            .then((data) => {
              _that.stringTagOptions.splice(0,_that.stringTagOptions.length)
              for(let index in data){
                _that.stringTagOptions.push({tagId:data[index].tagId,tagName:(data[index].typeStr!=null?data[index].typeStr:'')+data[index].tagName})
              }
              _that.initMode()
            })
            .catch((msg) => {
              FUN.notify("服务器错误:"+msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
            })
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
  }
}
</script>

<style scoped>

</style>
