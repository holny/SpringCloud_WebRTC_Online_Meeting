<template>
  <div class="column">
    <div class="col">
      <div class="text-bold text-h5 text-center q-mb-lg">视频数据统计</div>
    </div>
    <div class="col">
      <div class="row q-col-gutter-sm q-ml-xs q-mr-sm q-py-sm">
        <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
          <line-chart></line-chart>
        </div>
        <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
          <bar-chart></bar-chart>
        </div>
        <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
          <pie-chart></pie-chart>
        </div>
        <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
          <guage-chart></guage-chart>
        </div>
        <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
          <area-chart></area-chart>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import LineChart from "@/views/charts/LineChart";
import BarChart from "@/views/charts/BarChart";
import PieChart from "@/views/charts/PieChart";
import GuageChart from "@/views/charts/GuageChart";
import AreaChart from "@/views/charts/AreaChart";
export default {
  name: "VideoStatistics",
  components: {AreaChart, GuageChart, PieChart, BarChart, LineChart}
}
</script>

<style scoped>

</style>
