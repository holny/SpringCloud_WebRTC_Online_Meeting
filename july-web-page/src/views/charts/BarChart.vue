<template>
    <q-card class="my-card bg-grey-1" flat bordered>
      <q-card-section class="text-h6">
        {{chartTitle}}
        <q-btn icon="fa fa-download" class="float-right" @click="SaveImage" flat dense>
          <q-tooltip>Download PNG</q-tooltip>
        </q-btn>
      </q-card-section>
      <q-card-section>
        <div ref="barchart" id="barChart" style="height: 250px;"></div>
      </q-card-section>
      <q-resize-observer @resize="onResize"/>
    </q-card>
</template>

<script>
export default {
  name: "BarChart",
  props: {
    chartTitle:{
      type: String,
      require: true,
      default: 'Bar Chart'
    },
    dataSet:{
      type: Array,
      require: false,
      default: ()=>([
        ['product', '2015', '2016', '2017'],
        ['Matcha Latte', 43.3, 85.8, 93.7],
        ['Milk Tea', 83.1, 73.4, 55.1],
        ['Cheese Cocoa', 86.4, 65.2, 82.5],
        ['Walnut Brownie', 72.4, 53.9, 39.1]
      ])
    }
  },
  data() {
    return {
      model: false,
      options: {
        legend: {
          bottom: 10,
        },
        tooltip: {},
        dataset: {
          source: this.dataSet
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '20%',
          top: '5%',
          containLabel: true
        },
        xAxis: {type: 'category'},
        yAxis: {},
        // Declare several bar series, each will be mapped
        // to a column of dataset.source by default.
        series: [
          {type: 'bar'},
          {type: 'bar'},
          {type: 'bar'}
        ]
      },
      bar_chart: null
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
    SaveImage() {
      const linkSource = this.bar_chart.getDataURL();
      const downloadLink = document.createElement('a');
      document.body.appendChild(downloadLink);
      downloadLink.href = linkSource;
      downloadLink.target = '_self';
      downloadLink.download = 'BarChart.png';
      downloadLink.click();
    },
    init() {
      let barChart = document.getElementById('barChart');
      this.$echarts.dispose(barChart);
      let theme = this.model ? 'dark' : 'light';
      this.bar_chart = this.$echarts.init(barChart, theme);
      this.bar_chart.setOption(this.options)
    },
    onResize() {
      if (this.bar_chart) {
        this.bar_chart.resize();
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
