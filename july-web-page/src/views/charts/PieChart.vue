<template>
  <q-card class="my-card bg-grey-1" flat bordered>
    <q-card-section class="text-h6">
      {{chartTitle}}
<!--      <q-btn icon="fa fa-download" class="float-right" @click="SaveImage" flat dense>-->
<!--        <q-tooltip>Download PNG</q-tooltip>-->
<!--      </q-btn>-->
    </q-card-section>
    <q-card-section>
      <div ref="piechart" id="pieChart" style="height: 250px;"></div>
    </q-card-section>
    <q-resize-observer @resize="onResize"/>
  </q-card>
</template>

<script>
export default {
  name: "PieChart",
  props: {
    chartTitle:{
      type: String,
      require: true,
      default: 'Pie Chart'
    },
    dataSet:{
      type: Array,
      require: false,
      default: ()=>( [
        {value: 1048, name: 'Search Engine'},
        {value: 735, name: 'Direct access'},
        {value: 580, name: 'Email marketing'},
        {value: 484, name: 'Affiliate Advertising'},
        {value: 300, name: 'Video ad'}
      ])
    }
  },
  data() {
    return {
      model: false,
      options: {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          top: 'bottom',
          bottom: '5%',
          left: 'center'
        },
        series: [
          {
            name: '数据',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '35%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: this.dataSet
          }
        ]
      },
      pie_chart: null
    }
  },
  mounted() {
    this.init();
  },
  watch: {
    '$q.dark.isActive': function () {
      this.init();
    }
  },
  methods: {
    init() {
      let pieChart = document.getElementById('pieChart');
      this.$echarts.dispose(pieChart);
      let theme = this.model ? 'dark' : 'light';
      this.pie_chart = this.$echarts.init(pieChart, theme);
      this.pie_chart.setOption(this.options)
    },
    onResize() {
      if (this.pie_chart) {
        this.pie_chart.resize();
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
