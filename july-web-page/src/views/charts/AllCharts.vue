<template>
  <q-page class="">
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
        <scatter-chart></scatter-chart>
      </div>
      <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
        <dotted-bar-chart></dotted-bar-chart>
      </div>
      <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
        <guage-chart></guage-chart>
      </div>
      <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
        <boxplot></boxplot>
      </div>
      <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
        <area-chart></area-chart>
      </div>
    </div>
  </q-page>
</template>

<script>
import LineChart from "@/views/charts/LineChart";
import BarChart from "@/views/charts/BarChart";
import PieChart from "@/views/charts/PieChart";
import ScatterChart from "@/views/charts/ScatterChart";
import DottedBarChart from "@/views/charts/DottedBarChart";
import GuageChart from "@/views/charts/GuageChart";
import Boxplot from "@/views/charts/BoxChart";
import AreaChart from "@/views/charts/AreaChart";
export default {
  name: 'PageIndex',
  components: {AreaChart, Boxplot, GuageChart, DottedBarChart, ScatterChart, PieChart, BarChart, LineChart}
}
</script>
