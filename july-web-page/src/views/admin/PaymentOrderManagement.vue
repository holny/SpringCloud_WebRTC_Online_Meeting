<template>
  <div>
    <q-card class="my-card no-wrap  bg-grey-1 full-width" flat bordered>
      <q-card-section>
        <q-table
            title="支付管理"
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
              <q-td key="paymentId" :props="props">
                {{ props.row.paymentId }}
              </q-td>
              <q-td key="providerId" :props="props">
                <div style="cursor: pointer"  @click="goUserProfile(props.row.providerId)">{{ props.row.providerId }}</div>
              </q-td>
              <q-td key="receiverId" :props="props">
                <div style="cursor: pointer"  @click="goUserProfile(props.row.expertId)">{{ props.row.receiverId }}</div>
              </q-td>
              <q-td key="status" :props="props">
                {{ props.row.status }}
                <q-popup-edit v-model="props.row.status" @save="updatePaymentInfoField({paymentId:props.row.paymentId,status:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-option-group
                      inline
                      :options="statusOptions"
                      type="radio"
                      v-model="props.row.status"
                  />
                </q-popup-edit>
              </q-td>
              <q-td key="amount" :props="props">
                {{ props.row.amount }}
                <q-popup-edit v-model="props.row.amount" @save="updatePaymentInfoField({paymentId:props.row.paymentId,amount:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.amount" type="number" hint="数额" autocomplete="off" :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="payments" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="type" :props="props">
                {{ props.row.type }}
              </q-td>
              <q-td key="leaveMessage" :props="props">
                {{ props.row.leaveMessage }}
                <q-popup-edit v-model="props.row.leaveMessage" @save="updatePaymentInfoField({paymentId:props.row.paymentId,leaveMessage:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.leaveMessage" type="text" maxlength="50" counter autogrow hint="支付留言" autocomplete="on" :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="info" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="createGMT" :props="props">
                {{ props.row.createGMT }}
                <q-popup-edit v-model="props.row.createGMT" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="payGMT" :props="props">
                {{ props.row.payGMT }}
                <q-popup-edit v-model="props.row.payGMT" disable>
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
  name: "PaymentOrderManagement",
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
          name: 'paymentId',
          required: true,
          label: '支付ID',
          align: 'left',
          field: row => row.paymentId,
          format: val => `${val}`,
          sortable: true
        },
        {
          name: 'providerId',
          align: 'center',
          label: '支付人ID',
          field: 'providerId',
          sortable: false
        },
        {
          name: 'receiverId',
          align: 'center',
          label: '接收人ID',
          field: 'receiverId',
          sortable: false
        },
        {
          name: 'status',
          align: 'center',
          label: '状态',
          field: 'status',
          sortable: false
        },
        { name: 'amount', label: '数额', align: 'center', field: 'amount', sortable: true },
        { name: 'type', label: '类型', align: 'center', field: 'type', sortable: true },
        { name: 'leaveMessage', label: '留言', align: 'center', field: 'leaveMessage', sortable: true },
        { name: 'createGMT', label: '创建时间', align: 'center', field: 'createGMT', sortable: true },
        { name: 'payGMT', label: '支付时间', align: 'center', field: 'payGMT', sortable: true },

      ],
      data: [],
      original: [
        {
          paymentId: '123125123123',
          providerId: '1241231',
          receiverId: '15123512',
          status: 1,
          amount: 123.1,
          type: 1,
          leaveMessage: '支付留言',
          createGMT: '2021-07-02 14:32:21',
          payGMT: '2021-07-02 14:32:21',
        },
        {
          paymentId: '123125123124',
          providerId: '1241231',
          receiverId: '15123512',
          status: 1,
          amount: 123.1,
          type: 1,
          leaveMessage: '支付留言',
          createGMT: '2021-07-02 14:32:21',
          payGMT: '2021-07-02 14:32:21',
        },
        {
          paymentId: '123125123125',
          providerId: '1241231',
          receiverId: '15123512',
          status: 1,
          amount: 123.1,
          type: 1,
          leaveMessage: '支付留言',
          createGMT: '2021-07-02 14:32:21',
          payGMT: '2021-07-02 14:32:21',
        },
      ],
      statusOptions:[{value:0,label:'已删除',color: 'negative'},{value: 1,label:'已终止',color: 'negative'}, {value: 2,label:'待支付',color:'positive'}, {value: 3,label:'支付完成',color:'positive'}],
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
    updatePaymentInfoField(update){
      console.log('updatePaymentInfoField')
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
