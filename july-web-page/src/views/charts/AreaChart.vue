<template>
    <q-card class="my-card bg-grey-1" flat bordered>
      <q-card-section class="text-h6">
        {{chartTitle}}
      </q-card-section>
      <q-card-section>
        <div ref="areachart" id="areaChart" style="height: 250px;"></div>
      </q-card-section>
      <q-resize-observer @resize="onResize"/>
    </q-card>
</template>

<script>
export default {
  name: "AreaChart",
  props: {
    chartTitle:{
      type: String,
      require: true,
      default: 'Stacked Area Chart'
    },legendMap:{
      type: Object,
      require: false,
      default: ()=>({'Line 1':[140, 232, 101, 264, 90, 340, 250],
        'Line 2':[120, 282, 111, 234, 220, 340, 310],
        'Line 3':[320, 132, 201, 334, 190, 130, 220],
        'Line 4':[220, 402, 231, 134, 190, 230, 120],
        'Line 5':[220, 302, 181, 234, 210, 290, 150]})
    },xAxis:{
      type: Array,
      require: false,
      default: ()=>(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'])
    }
  },
  data() {
    return {
      model: false,
      options: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          data:[],
          bottom: 10,
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15%',
          top:'5%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: this.xAxis
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: []
      },
      area_chart: null
    }
  },
  mounted() {
    this.initData()
    this.init();
  },
  watch: {
    '$q.dark.isActive': function () {
      this.init();
    }
  },
  methods: {
    initData() {

      for(let legendKey in this.legendMap){
        // console.log(this.color)
        // console.log(count%Object.keys(this.color).length.toString())
        // console.log(this.color[count%Object.keys(this.color).length.toString()])
        // this.options.color.push(this.color[count%Object.keys(this.color).length.toString()].legendColor)
        this.options.legend.data.push(legendKey)
        let item={
              name: legendKey,
              type: 'line',
              stack: 'Total',
              areaStyle: {},
              emphasis: {
                focus: 'series'
              },
           data: this.legendMap[legendKey]
        }
        this.options.series.push(item)
      }
    },
    init() {
      let areaChart = document.getElementById('areaChart');
      this.$echarts.dispose(areaChart);
      let theme = this.model ? 'dark' : 'light';
      this.area_chart = this.$echarts.init(areaChart, theme);
      this.area_chart.setOption(this.options)
    },
    onResize() {
      if (this.area_chart) {
        this.area_chart.resize();
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
