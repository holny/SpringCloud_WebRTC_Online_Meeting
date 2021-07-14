<template>
  <q-card class="my-card bg-grey-1 no-wrap full-width" flat bordered style="min-width: 500px">
    <q-card-section>
      <div class="text-h5 q-mt-sm text-center">支付设置</div>
    </q-card-section>
    <q-card-section>
      <q-separator spaced />
      <q-item-label header>账户信息</q-item-label>
      <q-list>
        <q-item class="full-width">
          <q-item-section >
            <q-field hint="用户Id" :dense="true" style="width: 80%">
              <template v-slot:control>
                <div class="self-center full-width no-outline text-bold" tabindex="0">{{userPaymentInfo.userId}}</div>
              </template>
            </q-field>
          </q-item-section>
          <q-item-section >
            <q-field hint="账户余额" :dense="true" style="width: 80%">
              <template v-slot:control>
                <div class="self-center full-width no-outline text-bold" tabindex="0">{{userPaymentInfo.balance.toFixed(2)}}</div>
              </template>
            </q-field>
          </q-item-section>
        </q-item>
      </q-list>
    </q-card-section>
    <q-card-section>
      <q-separator spaced />
      <q-item-label header>充值</q-item-label>
      <q-list>
        <q-item class="full-width">
          <q-item-section>
            <q-input v-model="rechargeCode" ref="rechargeCode"  name="rechargeCode"
                     type="text"  debounce="500"  counter hint="充值码"
                     autocomplete="off"
                     maxlength="20"   :dense="true" lazy-rules clearable outlined>
              <template v-slot:prepend>
                <q-icon name="payment" />
              </template>
            </q-input>
          </q-item-section>
          <q-item-section>
            <q-input  v-model="rechargePWD" ref="rechargePWD"  name="rechargePWD"
                      type="text"  debounce="500"  counter hint="充值密码"
                      autocomplete="off"
                      maxlength="20"   :dense="true" lazy-rules clearable outlined>
              <template v-slot:prepend>
                <q-icon name="password" />
              </template>
              <template v-slot:after>
                <q-btn round dense flat icon="eva-refresh-outline" >
                  <q-tooltip
                      transition-show="scale"
                      transition-hide="scale"
                  >
                    确认修改
                  </q-tooltip>
                </q-btn>
              </template>
            </q-input>
          </q-item-section>
        </q-item>
      </q-list>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  name: "PaymentSettings",
  data () {
    return {
      userPaymentInfo: {
        // constant
        userId: '100002',
        balance: 1278.00,
        // could modify
      },
      rechargeCode: '',
      rechargePWD: '',
    }
  },
}
</script>

<style scoped>

</style>
