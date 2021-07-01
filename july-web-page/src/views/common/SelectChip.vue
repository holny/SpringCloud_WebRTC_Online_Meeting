<template>
  <div class="q-pa-md">
    <div class="q-gutter-xs">
      <q-chip v-for="(value, key) in candidateMap" :key="key"  :selected.sync="value.selected" :color="getColor(key)" text-color="white" :icon="value.icon" @click="onClick(key)">
        {{value.name}}
      </q-chip>

    </div>

<!--    <div class="q-mt-sm">-->
<!--      Your pick: {{ selection }}-->
<!--    </div>-->
<!--    <div class="q-mt-sm">-->
<!--      Queue: {{ selectionQueue }}-->
<!--    </div>-->
  </div>
</template>

<script>
import {randomColor} from "@/utils/random";

export default {
  name: "SelectChip",
  props: {
    limit: {
      type: Number,
      require: false,
      default: 3,
    },
    candidateMap:{
      type: Object,
      require: false,
      default: () => ({'1':{name:'Ice cream',val:'ice',icon:'cake',selected: false},'2':{name:'Eclair',val:'eclair',icon:'cake',selected: false},'3':{name:'Cupcake',val:'cupcake',icon:'cake',selected: false},'4':{name:'Gingerbread',val:'gingerbread',icon:'cake',selected: false}}),
    },
    selectionQueue:{
      type: Array,
      require: false,
      default: ()=> ([]),
    }
  },
  data () {
    return {
    }
  },

  computed: {
    // selection () {
    //   return Object.keys(this.candidateMap)
    //       .filter(key => this.candidateMap[key].selected === true)
    //       .join(', ')
    // }
  },
  mounted() {
    console.log("chip mounted" + this.selectionQueue)
    for(let index in this.selectionQueue){
      // eslint-disable-next-line no-prototype-builtins
      if(this.candidateMap.hasOwnProperty(this.selectionQueue[index])){
        this.candidateMap[this.selectionQueue[index]].selected = true
      }
    }
  },
  activated() {
    console.log("chip activated")
  },
  methods:{
    getColor(index){
      return randomColor(index,[]);
    },
    onClick(key){
      if(this.candidateMap[key].selected===true){
        if(this.selectionQueue.indexOf(key)<0) {
          if (this.limit > 0 && this.selectionQueue.length >= this.limit) {
            let deletedKey = this.selectionQueue.shift()
            this.candidateMap[deletedKey].selected = false
          }
          this.selectionQueue.push(key);
        }
      }else{
        if(this.selectionQueue.indexOf(key)>=0) {
          this.selectionQueue.splice(this.selectionQueue.indexOf(key),1)
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
