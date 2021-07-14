<template>
  <div class="justify-center">
    <q-tabs
        v-model="tab"
        narrow-indicator
        dense
        align="justify"
    >
      <q-tab class="text-purple" name="waiting" icon="mail" label="待开始" />
      <q-tab class="text-orange" name="inProgress" icon="alarm" label="进行中" />
      <q-tab class="text-teal" name="completed" icon="movie" label="已完成" />
    </q-tabs>
    <q-separator />
    <q-tab-panels v-model="tab" animated>
      <q-tab-panel name="waiting">
        <OrderItemList></OrderItemList>
      </q-tab-panel>

      <q-tab-panel name="inProgress">
        <OrderItemList></OrderItemList>
      </q-tab-panel>

      <q-tab-panel name="completed">
        <OrderItemList></OrderItemList>
      </q-tab-panel>
    </q-tab-panels>
  </div>
</template>

<script>
import OrderItemList from "@/views/trading/OrderItemList";

export default {
  name: "OrderItems",
  components:{
    OrderItemList
  },
  data () {
    return {
      numbers: [ 1, 2, 3, 4, 5, 6, 7, 8 , 9, 10, 11,12,13,14,15,16 ],
      current: 5,
      tab: 'waiting'
    }
  },
}
</script>

<style scoped>

</style>
