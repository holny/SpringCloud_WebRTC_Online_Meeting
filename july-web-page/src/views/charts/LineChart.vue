<template>
    <q-card class="my-card bg-grey-1" flat bordered>
      <q-card-section class="text-h6">
        {{chartTitle}}
      </q-card-section>
      <q-card-section>
        <div ref="linechart" id="lineChart" style="height: 250px;"></div>
      </q-card-section>
      <q-resize-observer @resize="onResize"/>
    </q-card>
</template>

<script>
export default {
  name: "LineChart",
  props: {
    chartTitle:{
      type: String,
      require: true,
      default: 'Line Chart'
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
      color:{'0':{legendColor:'#80FFA5',areaColor:['rgba(128, 255, 165)','rgba(1, 191, 236)']},
        '1':{legendColor:'#00DDFF',areaColor:['rgba(0, 221, 255)','rgba(77, 119, 255)']},
        '2':{legendColor:'#37A2FF',areaColor:['rgba(55, 162, 255)','rgba(116, 21, 219)']},
        '3':{legendColor:'#FF0087',areaColor:['rgba(255, 0, 135)','rgba(135, 0, 157)']},
        '4':{legendColor:'#FFBF00',areaColor:['rgba(255, 191, 0)','rgba(224, 62, 76)']}},
      options: {
        color: [], // 放入数据种类颜色
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
          data: [], // 放入数据种类Label名称
          bottom: 10,
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '20%',
          top: '5%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: this.xAxis //放入横坐标名称
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: []
      },
      line_chart: null
    }
  },
  mounted() {
    this.initData()
    this.initView();
  },
  watch: {
    '$q.dark.isActive': function () {
      this.initView();
    }
  },
  methods: {
    initData() {
      let count = 0
      for(let legendKey in this.legendMap){
        // console.log(this.color)
        // console.log(count%Object.keys(this.color).length.toString())
        // console.log(this.color[count%Object.keys(this.color).length.toString()])
        this.options.color.push(this.color[count%Object.keys(this.color).length.toString()].legendColor)
        this.options.legend.data.push(legendKey)
        let item={
              name: legendKey,
              type: 'line',
              stack: 'Total',
              smooth: true,
              lineStyle: {
                width: 0
              },
              showSymbol: false,
              areaStyle: {
                opacity: 0.8,
                color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                  offset: 0,
                  color: this.color[count%Object.keys(this.color).length.toString()].areaColor[0]
                }, {
                  offset: 1,
                  color: this.color[count%Object.keys(this.color).length.toString()].areaColor[1]
                }])
              },
              emphasis: {
                focus: 'series'
              },
              data: this.legendMap[legendKey]
            }
        this.options.series.push(item)
        count++
      }
    },
    initView() {
      let lineChart = document.getElementById('lineChart');
      this.$echarts.dispose(lineChart);
      let theme = this.model ? 'dark' : 'light';
      this.line_chart = this.$echarts.init(lineChart, theme);
      this.line_chart.setOption(this.options)
    },
    onResize() {
      if (this.line_chart) {
        this.line_chart.resize();
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
