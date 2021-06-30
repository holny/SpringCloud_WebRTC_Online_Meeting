<template>
  <q-card class="my-card no-wrap" flat bordered style="width: 400px;">
    <q-card-section horizontal style="height:100px">
      <q-img class="col" native-context-menu src="https://cdn.quasar.dev/img/mountains.jpg" >

        <div class="fit">
          <q-item>
            <q-item-section avatar>
              <q-avatar size="50px">
                <img src="https://cdn.quasar.dev/img/avatar2.jpg">
                <q-badge rounded floating color="yellow-10">
                  <q-icon name="done" color="white" />
                  <q-tooltip>
                    <strong >已认证</strong>
                  </q-tooltip>
                </q-badge>
              </q-avatar>
            </q-item-section>
            <q-item-section>
              <q-item-label clickable class="text-h6 text-white" style="cursor: pointer;">Tom
              <q-badge color="blue">
                Lv.12
              </q-badge>
              </q-item-label>
              <q-item-label class="text-caption text-italic text-white text-capitalize">vsidasdasd</q-item-label>
            </q-item-section>
          </q-item>
          <q-card-actions class="absolute-right justify-around">
            <q-btn class="btn-small" flat round color="positive" icon="chat" />
            <q-btn class="btn-small" flat round color="info" icon="bookmark" />
            <q-btn class="btn-small" flat round color="negative" icon="star_border" />
          </q-card-actions>
        </div>
      </q-img>

    </q-card-section>

    <q-card-section class="q-gutter-sm q-pa-md">
      <q-badge outline color="primary" label="Outline" />
      <q-badge outline color="orange" label="Outline" />
      <q-badge outline color="secondary" label="Outline" />
    </q-card-section>


    <q-card-section class="q-gutter-sm q-pa-sm">
      <div class="row no-wrap justify-between">
        <div class="no-wrap items-center">
          <q-rating size="18px" v-model="stars" :max="5" color="primary"  icon-half="star_half"  readonly />
          <span class="text-caption text-grey q-ml-sm">4.2 (551)</span>
        </div>
        <div class="no-wrap items-center">
          <q-icon class="text-caption text-weight-thin" color="grey-6" name="eva-star" >
            <q-tooltip>
              <strong >订阅量</strong>
            </q-tooltip>
          </q-icon>
          <span class="text-caption text-grey q-ml-sm">24K</span>
        </div>
      </div>
      <div class="row no-wrap justify-between">
        <div class="no-wrap items-center">
          <q-icon class="text-caption text-weight-thin" color="grey-6" name="eva-eye-outline" >
            <q-tooltip>
              <strong >浏览量</strong>
            </q-tooltip>
          </q-icon>
          <span class="text-caption text-grey q-ml-sm">24K</span>
        </div>
        <div class="no-wrap items-center">
          <q-icon class="text-caption text-weight-thin" color="grey-6" name="eva-person-outline" >
            <q-tooltip>
              <strong >作者</strong>
            </q-tooltip>
          </q-icon>
          <span class="text-  caption text-grey q-ml-sm">Admin</span>
        </div>
      </div>
    </q-card-section>
  </q-card>

</template>

<script>
export default {
  name: "UserItem",
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
