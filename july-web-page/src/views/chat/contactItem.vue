<template>
  <q-item clickable>
    <q-item-section avatar>
      <q-avatar>
        <img :src="peerObject.peerAvatar">
      </q-avatar>
    </q-item-section>

    <q-item-section>
      <q-item-label>{{peerObject.remarkName!=null?peerObject.remarkName:peerObject.peerUserName}}{{peerObject.remarkName!=null?'('+peerObject.peerUserName+')':''}}</q-item-label>
      <q-item-label caption>{{peerObject.gmtLastContact!=null?peerObject.gmtLastContact:'未知'}}</q-item-label>
    </q-item-section>
    <q-item-section side>
      <q-icon name="chat_bubble" color="green" @click="$emit('changeChatPeer',peerObject)"/>
    </q-item-section>
    <q-menu
        touch-position
        context-menu
    >

      <q-list dense style="min-width: 100px">
        <q-item clickable v-close-popup v-if="peerObject.socialType==='recent'">
          <q-item-section>标记</q-item-section>
        </q-item>
        <q-item clickable v-close-popup>
          <q-item-section>删除</q-item-section>
        </q-item>
        <q-item clickable v-close-popup>
          <q-item-section>屏蔽</q-item-section>
        </q-item>
        <q-separator />
        <q-item clickable>
          <q-item-section>Preferences</q-item-section>
          <q-item-section side>
            <q-icon name="keyboard_arrow_right" />
          </q-item-section>

          <q-menu anchor="top end" self="top start">
            <q-list>
              <q-item
                  v-for="n in 3"
                  :key="n"
                  dense
                  clickable
              >
                <q-item-section>Submenu Label</q-item-section>
                <q-item-section side>
                  <q-icon name="keyboard_arrow_right" />
                </q-item-section>
                <q-menu auto-close anchor="top end" self="top start">
                  <q-list>
                    <q-item
                        v-for="n in 3"
                        :key="n"
                        dense
                        clickable
                    >
                      <q-item-section>3rd level Label</q-item-section>
                    </q-item>
                  </q-list>
                </q-menu>
              </q-item>
            </q-list>
          </q-menu>

        </q-item>
        <q-separator />
        <q-item clickable v-close-popup>
          <q-item-section>Quit</q-item-section>
        </q-item>
      </q-list>

    </q-menu>
  </q-item>

</template>

<script>
export default {
  name: "contactItem",
  props: {
    peerObject: {
      type: Object,
      required: true
    }
  },
}
</script>

<style scoped>

</style>
