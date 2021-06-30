<template>
  <q-card class="my-card no-wrap" flat bordered style="width: 300px;">

    <q-img src="https://cdn.quasar.dev/img/parallax2.jpg" style="cursor: pointer;height: 200px">
      <div class="absolute-bottom">
        <div class="text-h6">Our Changing Planet</div>
        <div class="text-subtitle2">by John Doe</div>
      </div>
    </q-img>


    <q-card-section class="q-gutter-sm q-pa-sm">
      <div class="row no-wrap justify-between">
        <div class="no-wrap items-center">
          <q-rating size="18px" v-model="stars" :max="5" color="primary"  icon-half="star_half"  readonly />
          <span class="text-caption text-grey q-ml-sm">4.2 (551)</span>
        </div>
        <div class="no-wrap items-center">
          <q-icon class="text-caption text-weight-thin" color="grey-6" name="eva-eye-outline" >
            <q-tooltip>
              <strong >浏览量</strong>
            </q-tooltip>
          </q-icon>
          <span class="text-caption text-grey q-ml-sm">24K</span>
        </div>
      </div>
      <div class="row no-wrap justify-between">
        <div class="no-wrap items-center">
          <q-icon class="text-caption text-weight-thin" color="grey-6" name="eva-calendar-outline" >
            <q-tooltip>
              <strong >发布日期</strong>
            </q-tooltip>
          </q-icon>
          <span class="text-  caption text-grey q-ml-sm"> 2012-02-03</span>
        </div>
        <div class="no-wrap items-center">
          <q-icon class="text-caption text-weight-thin" color="grey-6" name="eva-person-outline" >
            <q-tooltip>
              <strong >作者</strong>
            </q-tooltip>
          </q-icon>
          <span class="text-  caption text-grey q-ml-sm">Tom</span>
        </div>
      </div>
    </q-card-section>
  </q-card>

</template>

<script>
export default {
  name: "VideoItem",
  data () {
    return {
      stars: 4.5,
    }
  }
}
</script>

<style scoped>
.my-card {
  width: 100%;
  border-radius:10px;
  display: inline-block;
}

.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}
</style>
