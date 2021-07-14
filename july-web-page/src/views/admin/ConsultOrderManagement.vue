<template>
  <div>
    <q-card class="my-card no-wrap  bg-grey-1 full-width" flat bordered>
      <q-card-section>
        <q-table
            title="咨询管理"
            :data="data"
            :columns="columns"
            :pagination.sync="pagination"
            :loading="loading"
            row-key="videoId"
            @request="onRequest"
            :filter="filter"
            flat
            bordered
            table-header-class="my-sticky-column-table"
            table-class="my-sticky-column-table"
            class="no-border no-box-shadow bg-grey-1"
        >
          <template v-slot:top-right>
            <q-input dense debounce="300" v-model="filter" placeholder="Search">
              <template v-slot:append>
                <q-icon name="search" />
              </template>
            </q-input>
          </template>

          <template v-slot:body="props">
            <q-tr :props="props">
              <q-td key="consultId" :props="props">
                {{ props.row.consultId }}
                <q-popup-edit v-model="props.row.consultId" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="customerId" :props="props">
                <div style="cursor: pointer"  @click="goUserProfile(props.row.customerId)">{{ props.row.customerId }}</div>
                <q-popup-edit v-model="props.row.customerId" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="expertId" :props="props">
                <div style="cursor: pointer"  @click="goUserProfile(props.row.expertId)">{{ props.row.expertId }}</div>
                <q-popup-edit v-model="props.row.expertId" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="status" :props="props">
                {{ props.row.status }}
                <q-popup-edit v-model="props.row.status" @save="updateConsultInfoField({consultId:props.row.consultId,status:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-option-group
                      inline
                      :options="statusOptions"
                      type="radio"
                      v-model="props.row.status"
                  />
                </q-popup-edit>
              </q-td>
              <q-td key="consultField" :props="props">
                {{ props.row.consultField }}
                <q-popup-edit v-model="props.row.consultField" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="leaveMessage" :props="props">
                {{ props.row.leaveMessage }}
                <q-popup-edit v-model="props.row.leaveMessage" @save="updateConsultInfoField({consultId:props.row.consultId,leaveMessage:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.leaveMessage" type="text" maxlength="150" counter autogrow hint="用户留言" autocomplete="on" :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="info" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="consultStartGMT" :props="props">
                {{ props.row.consultStartGMT }}
                <q-popup-edit v-model="props.row.consultStartGMT" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="consultTimeSpan" :props="props">
                {{ props.row.consultTimeSpan }}
                <q-popup-edit v-model="props.row.consultTimeSpan" @save="updateConsultInfoField({consultId:props.row.consultId,consultTimeSpan:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.consultTimeSpan" type="number" hint="咨询时长" autocomplete="off" :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="schedule" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="cost" :props="props">
                {{ props.row.cost }}
                <q-popup-edit v-model="props.row.cost" @save="updateConsultInfoField({consultId:props.row.consultId,cost:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.cost" type="number" hint="花费" autocomplete="off" :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="payments" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="paymentId" :props="props">
                {{ props.row.paymentId }}
                <q-popup-edit v-model="props.row.paymentId" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="createGMT" :props="props">
                {{ props.row.createGMT }}
                <q-popup-edit v-model="props.row.createGMT" disable>
                </q-popup-edit>
              </q-td>
            </q-tr>
          </template>
        </q-table>
      </q-card-section>

    </q-card>
  </div>
</template>

<script>

export default {
  name: "ConsultOrderManagement",
  components: {

  },
  data () {
    return {

      filter: '',
      loading: false,
      pagination: {
        sortBy: 'desc',
        descending: false,
        page: 1,
        rowsPerPage: 10,
        rowsNumber: 10
      },
      columns: [
        {
          name: 'consultId',
          required: true,
          label: '咨询订单ID',
          align: 'left',
          field: row => row.consultId,
          format: val => `${val}`,
          sortable: true
        },
        {
          name: 'customerId',
          align: 'center',
          label: '咨询人ID',
          field: 'customerId',
          sortable: false
        },
        {
          name: 'expertId',
          align: 'center',
          label: '专家ID',
          field: 'expertId',
          sortable: false
        },
        {
          name: 'status',
          align: 'center',
          label: '状态',
          field: 'status',
          sortable: false
        },
        { name: 'consultField', label: '咨询领域', align: 'center', field: 'consultField', sortable: true },
        { name: 'leaveMessage', label: '用户留言', align: 'center', field: 'leaveMessage', sortable: true },
        { name: 'consultStartGMT', label: '预约起始时间', align: 'center', field: 'consultStartGMT', sortable: true },
        { name: 'consultTimeSpan', label: '咨询时长(单位:半小时)', align: 'center', field: 'consultTimeSpan', sortable: true },
        { name: 'cost', label: '费用', align: 'center', field: 'cost', sortable: true },
        {
          name: 'paymentId',
          align: 'center',
          label: '支付ID',
          field: 'paymentId',
          sortable: false
        },
        { name: 'createGMT', label: '创建时间', align: 'center', field: 'createGMT', sortable: true },

      ],
      data: [],
      original: [
        {
          consultId: '123125123123',
          customerId: '1241231',
          expertId: '15123512',
          status: 1,
          consultField: ['Google','Facebook','Wechat'],
          leaveMessage: '咨询留言',
          consultStartGMT: '2021-07-02 14:32:21',
          consultTimeSpan: 8.5,
          cost: 1241,
          paymentId: '123125123123',
          createGMT: '2021-07-02 14:32:21',
        },
        {
          consultId: '123125123123',
          customerId: '1241231',
          expertId: '15123512',
          status: 1,
          consultField: ['Google','Facebook','Wechat'],
          leaveMessage: '咨询留言',
          consultStartGMT: '2021-07-02 14:32:21',
          consultTimeSpan: 8.5,
          cost: 1241,
          paymentId: '123125123123',
          createGMT: '2021-07-02 14:32:21',
        },
        {
          consultId: '123125123123',
          customerId: '1241231',
          expertId: '15123512',
          status: 1,
          consultField: ['Google','Facebook','Wechat'],
          leaveMessage: '咨询留言',
          consultStartGMT: '2021-07-02 14:32:21',
          consultTimeSpan: 8.5,
          cost: 1241,
          paymentId: '123125123123',
          createGMT: '2021-07-02 14:32:21',
        },
      ],
      videoPublishOptions:[{value:'noNeed',label:'无需推送',color: 'positive'}, {value:'publish',label:'推送',color:'purple'}, {value:'done',label:'推送完毕',color:'info'}],
      scoreOptions:[{value: 0 ,label:'0分',color: 'positive'}, {value:0.5,label:'0.5分',color:'red'}, {value:1,label:'1分',color:'pink'}, {value:1,label:'推送完毕',color:'purple'}, {value:1.5,label:'1.5分',color:'deep-purple'}
        , {value:2,label:'2分',color:'indigo'}, {value:2.5,label:'2.5分',color:'blue'}, {value:3,label:'3分',color:'cyan'}, {value:3.5,label:'3.5分',color:'teal'}, {value:4,label:'4分',color:'teal'}
        , {value:4.5,label:'4.5分',color:'green'}, {value:5,label:'5分',color:'positive'}],
      statusOptions:[{value:0,label:'已删除',color: 'negative'},{value: 1,label:'已终止',color: 'negative'}, {value: 2,label:'预约创建',color:'positive'}, {value: 3,label:'待支付',color:'positive'}, {value:4 ,label:'预约接收',color:'positive'}, {value:5,label:'待到时',color:'positive'}
        , {value:6,label:'待双方接入',color:'pink'}, {value:7,label:'待用户接入',color:'pink'}, {value:8,label:'待专家接入',color:'pink'},{value:9,label:'进行中',color:'pink'},{value:10,label:'未完成',color:'pink'}
        ,{value:11,label:'待完成确认',color:'pink'},{value:12,label:'待评价',color:'pink'}, {value:13,label:'完毕',color:'pink'}],
    }
  },
  mounted () {
    // get initial data from server (1st page)
    this.onRequest({
      pagination: this.pagination,
      filter: undefined
    })
  },
  methods: {
    onRequest (props) {
      const { page, rowsPerPage, sortBy, descending } = props.pagination
      const filter = props.filter

      this.loading = true

      // emulate server
      setTimeout(() => {
        // update rowsCount with appropriate value
        this.pagination.rowsNumber = this.getRowsNumberCount(filter)

        // get all rows if "All" (0) is selected
        const fetchCount = rowsPerPage === 0 ? this.pagination.rowsNumber : rowsPerPage

        // calculate starting row of data
        const startRow = (page - 1) * rowsPerPage

        // fetch data from "server"
        const returnedData = this.fetchFromServer(startRow, fetchCount, filter, sortBy, descending)

        // clear out existing data and add new
        this.data.splice(0, this.data.length, ...returnedData)

        // don't forget to update local pagination object
        this.pagination.page = page
        this.pagination.rowsPerPage = rowsPerPage
        this.pagination.sortBy = sortBy
        this.pagination.descending = descending

        // ...and turn of loading indicator
        this.loading = false
      }, 1500)
    },

    // emulate ajax call
    // SELECT * FROM ... WHERE...LIMIT...
    fetchFromServer (startRow, count, filter, sortBy, descending) {
      const data = filter
          ? this.original.filter(row => row.name.includes(filter))
          : this.original.slice()

      // handle sortBy
      if (sortBy) {
        const sortFn = sortBy === 'desc'
            ? (descending
                    ? (a, b) => (a.name > b.name ? -1 : a.name < b.name ? 1 : 0)
                    : (a, b) => (a.name > b.name ? 1 : a.name < b.name ? -1 : 0)
            )
            : (descending
                    ? (a, b) => (parseFloat(b[sortBy]) - parseFloat(a[sortBy]))
                    : (a, b) => (parseFloat(a[sortBy]) - parseFloat(b[sortBy]))
            )
        data.sort(sortFn)
      }

      return data.slice(startRow, startRow + count)
    },

    // emulate 'SELECT count(*) FROM ...WHERE...'
    getRowsNumberCount (filter) {
      if (!filter) {
        return this.original.length
      }
      let count = 0
      this.original.forEach((treat) => {
        if (treat.tradingId.includes(filter)) {
          ++count
        }
      })
      return count
    },
    updateConsultInfoField(update){
      console.log('updateConsultInfoField')
      console.log(update)
    },
    goUserProfile(userId){
      let routeUrl =this.$router.resolve({ name: 'profile', params: { uid:userId }})
      window.open(routeUrl.href, '_blank');
    }
  }
}
</script>

<style lang="sass">
.my-card
  transition: box-shadow .3s
  padding: 0px
  border-radius: 10px

.my-card:hover
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;

.my-sticky-column-table
  thead tr:first-child th:first-child
    background-color: #fff
  td:first-child
    background-color: #f5f5dc

  th:first-child,
  td:first-child
    position: sticky
    left: 0
    z-index: 1
</style>
