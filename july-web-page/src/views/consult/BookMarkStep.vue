<template>
    <q-card class="my-card bg-grey-1" flat bordered>
      <q-card-section>
        <div class="text-h5 q-mt-sm">预约咨询</div>
      </q-card-section>
      <q-card-section>
        <q-stepper
            v-model="step"
            header-nav
            ref="stepper"
            color="primary"
            class="bg-grey-1 no-border-radius no-box-shadow"
            animated
        >
          <q-step
              :name="1"
              title="Select campaign settings"
              icon="settings"
              :done="step > 1"
              :header-nav="step > 1"
          >
            For each ad campaign that you create, you can control how much you're willing to
            spend on clicks and conversions, which networks and geographical locations you want
            your ads to show on, and more.

            <q-stepper-navigation>
              <q-btn @click="() => { done1 = true; step = 2 }" color="primary" label="Continue" />
            </q-stepper-navigation>
          </q-step>

          <q-step
              :name="2"
              title="Create an ad group"
              caption="Optional"
              icon="create_new_folder"
              :done="step > 2"
              :header-nav="step > 2"
          >
            An ad group contains one or more ads which target a shared set of keywords.

            <q-stepper-navigation>
              <q-btn @click="() => { done2 = true; step = 3 }" color="primary" label="Continue" />
              <q-btn flat @click="step = 1" color="primary" label="Back" class="q-ml-sm" />
            </q-stepper-navigation>
          </q-step>

          <q-step
              :name="3"
              title="Create an ad"
              icon="add_comment"
              :header-nav="step > 3"
          >
            Try out different ad text to see what brings in the most customers, and learn how to
            enhance your ads using features like ad extensions. If you run into any problems with
            your ads, find out how to tell if they're running and how to resolve approval issues.

            <q-stepper-navigation>
              <q-btn color="primary" @click="done3 = true" label="Finish" />
              <q-btn flat @click="step = 2" color="primary" label="Back" class="q-ml-sm" />
            </q-stepper-navigation>
          </q-step>
        </q-stepper>
      </q-card-section>
    </q-card>
</template>

<script>
export default {
  name: "BookMarkStep",
  data () {
    return {
      step: 1
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
