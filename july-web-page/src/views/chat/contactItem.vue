<template>
  <q-item clickable>
    <q-item-section avatar>
      <q-avatar>
        <img :src="peerObject.peerAvatar">
        <q-badge v-if="peerObject.unReadMsgCount!=null&&peerObject.unReadMsgCount>0" color="orange" floating transparent>
          {{peerObject.unReadMsgCount}}
        </q-badge>
      </q-avatar>
    </q-item-section>

    <q-item-section>
      <q-item-label>{{peerObject.remarkName!=null?peerObject.remarkName:peerObject.peerUserName}}{{peerObject.remarkName!=null?'('+peerObject.peerUserName+')':''}}</q-item-label>
      <q-item-label caption>{{peerObject.category===CONTACTS_CATEGORY_RECENT?peerObject.gmtLastContact:peerObject.gmtCreate}}</q-item-label>
    </q-item-section>
    <q-item-section side>
      <q-icon name="chat_bubble" color="green" @click="$emit('changePeer',peerObject.peerId,peerObject.peerType,peerObject.category)">

      </q-icon>
    </q-item-section>
    <q-menu
        touch-position
        context-menu
    >

      <q-list dense style="min-width: 100px">
        <q-item clickable v-close-popup v-if="peerObject.category===CONTACTS_CATEGORY_RECENT">
          <q-item-section @click="$emit('addBookmarker',peerObject.peerId,peerObject.peerType)">标记</q-item-section>
        </q-item>
        <q-item clickable v-close-popup>
          <q-item-section  @click="$emit('removeUserRelation',peerObject.peerId,peerObject.peerType,peerObject.category)">删除</q-item-section>
        </q-item>
        <q-item clickable v-close-popup>
          <q-item-section v-if="peerObject.relTypeCode!==RELATION_TYPE_BLACK&&peerObject.relTypeCode!==RELATION_TYPE_IGNORE" @click="$emit('updateUserRelationType',peerObject.peerId,peerObject.peerType,RELATION_TYPE_IGNORE,'add')">屏蔽</q-item-section>
          <q-item-section v-if="peerObject.relTypeCode===RELATION_TYPE_IGNORE" @click="$emit('updateUserRelationType',peerObject.peerId,peerObject.peerType,RELATION_TYPE_IGNORE,'remove')">解除屏蔽</q-item-section>
        </q-item>
        <q-item clickable v-close-popup>
          <q-item-section v-if="peerObject.relTypeCode!==RELATION_TYPE_BLACK&&peerObject.relTypeCode!==RELATION_TYPE_IGNORE" @click="$emit('updateUserRelationType',peerObject.peerId,peerObject.peerType,RELATION_TYPE_BLACK,'add')">拉黑</q-item-section>
          <q-item-section v-if="peerObject.relTypeCode===RELATION_TYPE_BLACK" @click="$emit('updateUserRelationType',peerObject.peerId,peerObject.peerType,RELATION_TYPE_BLACK,'remove')">解除拉黑</q-item-section>
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
import {CONSTANT} from "@/utils/constant";

export default {
  name: "contactItem",
  props: {
    peerObject: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      CONTACTS_CATEGORY_RECENT: CONSTANT.CONTACTS_CATEGORY_RECENT,
      CONTACTS_CATEGORY_BOOKMARK:CONSTANT.CONTACTS_CATEGORY_BOOKMARK,
      RELATION_TYPE_FRIEND:CONSTANT.RELATION_TYPE_FRIEND,
      RELATION_TYPE_TEMP:CONSTANT.RELATION_TYPE_TEMP,
      RELATION_TYPE_BLACK:CONSTANT.RELATION_TYPE_BLACK,
      RELATION_TYPE_IGNORE:CONSTANT.RELATION_TYPE_IGNORE
    }
  }

}
</script>

<style scoped>

</style>
