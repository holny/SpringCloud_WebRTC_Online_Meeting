<template>
  <q-card class="my-card no-wrap  bg-grey-1 full-width" flat bordered>
    <q-card-section>
        <q-table
            :grid="$q.screen.xs"
            title="交易流水"
            :data="data"
            :columns="columns"
            :pagination.sync="pagination"
            :loading="loading"
            row-key="tradingId"
            @request="onRequest"
            :filter="filter"
            class="no-border no-box-shadow bg-grey-1"

        >
          <template v-slot:top-right>
            <q-input dense debounce="300" v-model="filter" placeholder="Search">
              <template v-slot:append>
                <q-icon name="search" />
              </template>
            </q-input>
          </template>
        </q-table>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  name: "TradingRecords",
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
          name: 'tradingId',
          required: true,
          label: '交易ID',
          align: 'left',
          field: row => row.tradingId,
          format: val => `${val}`,
          sortable: true
        },
        {
          name: 'target',
          align: 'center',
          label: '交易对象',
          field: 'target',
          sortable: false
        },
        { name: 'tradingGMT', label: '交易时间', field: 'tradingGMT', sortable: true },
        { name: 'type', label: '交易类型', field: 'type', sortable: false },
        {
          name: 'amount',
          label: '金额',
          field: 'amount',
          sortable: true,
          format: val =>  val.toFixed(2),
          sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
        },
        {
          name: 'status',
          label: '状态',
          field: 'status',
          sortable: false
        },
        {
          name: 'balance',
          label: '余额',
          field: 'balance',
          sortable: true,
          format: val =>  val.toFixed(2),
          sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
        }
      ],
      data: [],
      original: [
        {
          tradingId: '123125123123',
          target: 'Tom',
          tradingGMT: '2021-07-02 14:32:21',
          type: '流出',
          amount: 4.0,
          status: '已完成',
          balance: 132.0
        },
        {
          tradingId: '123125123123',
          target: 'Tom',
          tradingGMT: '2021-07-02 14:32:21',
          type: '流出',
          amount: 4.0,
          status: '已完成',
          balance: 132.0
        },
        {
          tradingId: '123125123123',
          target: 'Tom',
          tradingGMT: '2021-07-02 14:32:21',
          type: '流出',
          amount: 4.0,
          status: '已完成',
          balance: 132.0
        },
        {
          tradingId: '123125123123',
          target: 'Tom',
          tradingGMT: '2021-07-02 14:32:21',
          type: '流出',
          amount: 4.0,
          status: '已完成',
          balance: 132.0
        },
        {
          tradingId: '123125123123',
          target: 'Tom',
          tradingGMT: '2021-07-02 14:32:21',
          type: '流出',
          amount: 4.354,
          status: '已完成',
          balance: 132.0
        }
      ]
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
    }
  }
}
</script>

<style scoped>
.my-card {
  transition: box-shadow .3s;
  padding: 0px;
  border-radius:10px;


}
.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}


</style>
