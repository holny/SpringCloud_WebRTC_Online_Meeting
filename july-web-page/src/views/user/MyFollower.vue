<template>
  <q-card class="my-card no-wrap full-width bg-grey-1" flat bordered>
    <q-card-section>
      <q-table
          :grid="$q.screen.xs"
          title="关注我的"
          :data="data"
          :columns="columns"
          :pagination.sync="pagination"
          :loading="loading"
          row-key="userId"
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
  name: "MyFollower",
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
          name: 'userId',
          required: true,
          label: '用户Id',
          align: 'left',
          field: row => row.userId,
          format: val => `${val}`,
          sortable: true
        },
        {
          name: 'userName',
          align: 'center',
          label: '用户名',
          field: 'userName',
          sortable: false
        }, {
          name: 'nickName',
          align: 'center',
          label: '昵称',
          field: 'nickName',
          sortable: false
        }, {
          name: 'level',
          align: 'center',
          label: '等级',
          field: 'level',
          sortable: true
        },
        { name: 'followGMT', label: '关注时间', field: 'followGMT', sortable: true },
      ],
      data: [],
      original: [
        {
          userId: '123125123123',
          userName: 'Tom',
          nickName: 'Cat',
          level: '12',
          followGMT: '2021-07-02 14:32:21',
        },
        {
          userId: '123125123124',
          userName: 'Tom',
          nickName: 'Cat',
          level: '12',
          followGMT: '2021-07-02 14:32:21',
        },
        {
          userId: '123125123125',
          userName: 'Tom',
          nickName: 'Cat',
          level: '12',
          followGMT: '2021-07-02 14:32:21',
        },
        {
          userId: '123125123126',
          userName: 'Tom',
          nickName: 'Cat',
          level: '12',
          followGMT: '2021-07-02 14:32:21',
        },
        {
          userId: '123125123127',
          userName: 'Tom',
          nickName: 'Cat',
          level: '12',
          followGMT: '2021-07-02 14:32:21',
        },
        {
          userId: '123125123128',
          userName: 'Tom',
          nickName: 'Cat',
          level: '12',
          followGMT: '2021-07-02 14:32:21',
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
