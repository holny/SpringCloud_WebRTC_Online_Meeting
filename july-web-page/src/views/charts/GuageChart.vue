<template>
    <q-card class="my-card bg-grey-1" flat bordered>
      <q-card-section class="text-h6">
        {{chartTitle}}

        <q-btn icon="fa fa-download" class="float-right" @click="SaveImage" flat dense>
          <q-tooltip>Download PNG</q-tooltip>
        </q-btn>
      </q-card-section>
      <q-card-section>
        <div ref="guagechart" id="guageChart" style="height: 250px;"></div>
      </q-card-section>
      <q-resize-observer @resize="onResize"/>
    </q-card>

</template>

<script>
export default {
  name: "GuageChart",
  props: {
    chartTitle:{
      type: String,
      require: true,
      default: 'Guage Chart'
    },
    dataSet:{
      type: Array,
      require: false,
      default: ()=>( [{
        value: 50,
        name: 'SCORE'
      }])
    }
  },
  data() {
    return {
      model: false,
      options: {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%'
        },
        series: [{
          name: 'Pressure',
          type: 'gauge',
          progress: {
            show: true
          },
          detail: {
            valueAnimation: true,
            formatter: '{value}'
          },
          data: this.dataSet
        }]
      },
      guage_chart: null
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
      const linkSource = this.guage_chart.getDataURL();
      const downloadLink = document.createElement('a');
      document.body.appendChild(downloadLink);
      downloadLink.href = linkSource;
      downloadLink.target = '_self';
      downloadLink.download = 'GuageChart.png';
      downloadLink.click();
    },
    init() {
      let guageChart = document.getElementById('guageChart');
      this.$echarts.dispose(guageChart);
      let theme = this.model ? 'dark' : 'light';
      this.guage_chart = this.$echarts.init(guageChart, theme);
      this.guage_chart.setOption(this.options)
    },
    onResize() {
      if (this.guage_chart) {
        this.guage_chart.resize();
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
