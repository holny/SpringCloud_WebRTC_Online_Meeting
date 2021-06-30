<template>
  <div class="justify-center">
    <q-btn-dropdown flat color="primary" label="按 浏览量排序">
      <q-list>
        <q-item clickable v-close-popup @click="onItemClick">
          <q-item-section>
            <q-item-label>浏览量</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="onItemClick">
          <q-item-section>
            <q-item-label>上传时间</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="onItemClick">
          <q-item-section>
            <q-item-label>时间长度</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-btn-dropdown>

    <div class="row q-pt-xs q-pb-md justify-around">
      <user-item class="q-ma-md" v-for="key in numbers" :key="key"/>
    </div>
    <q-pagination
        size="1.2em"
        padding="md"
        v-model="current"
        :max="5"
        direction-links
        unelevated
        color="black"
        active-color="yellow"
        active-text-color="black"
        class="justify-center"
    />
  </div>
</template>

<script>
import UserItem from '@/views/user/UserItem'
export default {
  name: "UserList",
  components:{
    UserItem
  },
  data () {
    return {
      numbers: [ 1, 2, 3, 4, 5, 6, 7, 8 , 9, 10, 11,12,13,14,15,16 ],
      current: 5,
    }
  },
  methods: {
    onItemClick () {
      // console.log('Clicked on an Item')
    }
  }
}
</script>

<style scoped>

</style>
