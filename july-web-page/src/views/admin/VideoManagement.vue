<template>
  <div>
    <q-card class="my-card no-wrap  bg-grey-1 full-width" flat bordered>
      <q-card-section>
        <q-table
            title="视频管理"
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
              <q-td key="videoId" :props="props">
                <div style="cursor: pointer"  @click="goViewVideo(props.row.videoId)">{{ props.row.videoId }}</div>
                <q-popup-edit v-model="props.row.videoId" disable>
                  <q-input v-model="props.row.videoId" type="text" hint="视频ID" autocomplete="on"  maxlength="32"  :dense="true"
                           autofocus counter  clearable outlined>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="title" :props="props">
                {{ props.row.title }}
                <q-popup-edit v-model="props.row.title"
                              @save="updateVideoInfoField({videoId:props.row.videoId,title:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.title" type="text" hint="标题" autocomplete="on" maxlength="40"  counter :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="title" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="isTop" :props="props">
                {{ props.row.isTop }}
                <q-popup-edit v-model="props.row.isTop"   @save="updateVideoInfoField({videoId:props.row.videoId,isTop:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-toggle
                      v-model="props.row.isTop"
                      checked-icon="check"
                      color="green"
                      label="置顶视频"
                      unchecked-icon="clear"
                  />
                </q-popup-edit>
              </q-td>
              <q-td key="isPublish" :props="props">
                {{ props.row.isPublish }}
                <q-popup-edit v-model="props.row.isPublish" @save="updateVideoInfoField({videoId:props.row.videoId,isPublish:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-option-group
                      inline
                      :options="videoPublishOptions"
                      type="radio"
                      v-model="props.row.isPublish"
                  />
                </q-popup-edit>
              </q-td>
              <q-td key="gmtCreate" :props="props">
                {{ props.row.gmtCreate }}
                <q-popup-edit v-model="props.row.gmtCreate" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="gmtUpdate" :props="props">
                {{ props.row.gmtUpdate }}
                <q-popup-edit v-model="props.row.gmtUpdate" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="view" :props="props">
                {{ props.row.view }}
                <q-popup-edit v-model="props.row.view" @save="updateVideoInfoField({videoId:props.row.videoId,view:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.view" type="number" hint="浏览数" autocomplete="off" :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="visibility" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="thumb" :props="props">
                {{ props.row.thumb }}
                <q-popup-edit v-model="props.row.thumb" @save="updateVideoInfoField({videoId:props.row.videoId,thumb:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.thumb" type="number" hint="点赞数" autocomplete="off" :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="thumb_up" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="score" :props="props">
                {{ props.row.score }}
                <q-popup-edit v-model="props.row.score" @save="updateVideoInfoField({videoId:props.row.videoId,score:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-option-group
                      inline
                      :options="scoreOptions"
                      type="radio"
                      v-model="props.row.score"
                  />
                </q-popup-edit>
              </q-td>
              <q-td key="info" :props="props">
                {{ props.row.info }}
                <q-popup-edit v-model="props.row.info" @save="updateVideoInfoField({videoId:props.row.videoId,info:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.info" type="text" maxlength="150" counter autogrow hint="视频说明" autocomplete="on" :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="info" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="tag" :props="props">
                {{ props.row.tag }}
                <q-popup-edit v-model="props.row.tag" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="mainCategory" :props="props">
                {{ props.row.mainCategory }}
                <q-popup-edit v-model="props.row.mainCategory" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="subCategory" :props="props">
                {{ props.row.subCategory }}
                <q-popup-edit v-model="props.row.subCategory" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="series" :props="props">
                {{ props.row.series }}
                <q-popup-edit v-model="props.row.series" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="password" :props="props">
                {{ props.row.password }}
                <q-popup-edit v-model="props.row.password" @save="updateVideoInfoField({videoId:props.row.videoId,password:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.password" type="text" counter maxlength="15" hint="视频密码(空为无需密码)" autocomplete="off" :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="password" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="status" :props="props">
                {{ props.row.status }}
                <q-popup-edit v-model="props.row.status" @save="updateVideoInfoField({videoId:props.row.videoId,status:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-option-group
                      inline
                      :options="statusOptions"
                      type="radio"
                      v-model="props.row.status"
                  />
                </q-popup-edit>
              </q-td>
              <q-td key="videoSrc" :props="props">
                {{ props.row.videoSrc }}
                <q-popup-edit v-model="props.row.videoSrc" disable>

                </q-popup-edit>
              </q-td>
              <q-td key="posterSrc" :props="props">
                {{ props.row.posterSrc }}
                <q-popup-edit v-model="props.row.posterSrc" disable>

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
  name: "VideoManagement",
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
          name: 'videoId',
          required: true,
          label: '视频ID',
          align: 'left',
          field: row => row.videoId,
          format: val => `${val}`,
          sortable: true
        },
        {
          name: 'title',
          align: 'center',
          label: '标题',
          field: 'title',
          sortable: false
        },
        {
          name: 'isTop',
          align: 'center',
          label: '是否置顶',
          field: 'isTop',
          sortable: false
        },
        {
          name: 'isPublish',
          align: 'center',
          label: '是否推送',
          field: 'isPublish',
          sortable: false
        },
        { name: 'gmtCreate', label: '创建时间', align: 'center', field: 'gmtCreate', sortable: true },
        { name: 'gmtUpdate', label: '上一次更新', align: 'center', field: 'gmtUpdate', sortable: true },
        { name: 'view', label: '浏览数', align: 'center', field: 'view', sortable: true },
        { name: 'thumb', label: '点赞数', align: 'center', field: 'thumb', sortable: true },
        { name: 'score', label: '评分', align: 'center', field: 'score', sortable: true },
        {
          name: 'info',
          align: 'center',
          label: '说明',
          field: 'info',
          sortable: false
        },
        { name: 'tag', label: '视频标签', align: 'center', field: row => row.gender, format: val =>  val=`${val}`+'sdas', sortable: true },
        { name: 'mainCategory', label: '主类别', align: 'center', field: 'mainCategory', sortable: false },
        { name: 'subCategory', label: '子类别', align: 'center', field: 'subCategory', sortable: false },
        { name: 'series', label: '系列', align: 'center', field: 'series', sortable: false },
        { name: 'password', label: '密码(空为无需密码)', align: 'center', field: 'password', sortable: false },
        { name: 'status', label: '视频状态', align: 'center', field: 'status', sortable: true },
        { name: 'videoSrc', label: '视频文件', align: 'center', field: 'videoSrc', sortable: true },
        { name: 'posterSrc', label: '画报文件', align: 'center', field: 'posterSrc', sortable: false },

      ],
      data: [],
      original: [
        {
          videoId: '123125123123',
          title: '视频标题',
          isTop: false,
          isPublish: 'done',
          gmtCreate: '2021-07-02 14:32:21',
          gmtUpdate: '2021-07-02 14:32:21',
          view: '121231',
          thumb: '1231',
          score: 4.5,
          info: '视频信息Info',
          tag: ['Google','Facebook','Wechat'],
          mainCategory: '主类别',
          subCategory: '子类别',
          series: '系列',
          password: '1234',
          status: 1,
          videoSrc: 'videoSrc',
          posterSrc: 'posterSrc',
        },
        {
          videoId: '123125123124',
          title: '视频标题',
          isTop: false,
          isPublish: 'done',
          gmtCreate: '2021-07-02 14:32:21',
          gmtUpdate: '2021-07-02 14:32:21',
          view: '121231',
          thumb: '1231',
          score: 4.5,
          info: '视频信息Info',
          tag: ['Google','Facebook','Wechat'],
          mainCategory: '主类别',
          subCategory: '子类别',
          series: '系列',
          password: '1234',
          status: 1,
          videoSrc: 'videoSrc',
          posterSrc: 'posterSrc',
        },
        {
          videoId: '123125123125',
          title: '视频标题',
          isTop: false,
          isPublish: 'done',
          gmtCreate: '2021-07-02 14:32:21',
          gmtUpdate: '2021-07-02 14:32:21',
          view: '121231',
          thumb: '1231',
          score: 4.5,
          info: '视频信息Info',
          tag: ['Google','Facebook','Wechat'],
          mainCategory: '主类别',
          subCategory: '子类别',
          series: '系列',
          password: '1234',
          status: 1,
          videoSrc: 'videoSrc',
          posterSrc: 'posterSrc',
        },
      ],
      videoPublishOptions:[{value:'noNeed',label:'无需推送',color: 'positive'}, {value:'publish',label:'推送',color:'purple'}, {value:'done',label:'推送完毕',color:'info'}],
      scoreOptions:[{value: 0 ,label:'0分',color: 'positive'}, {value:0.5,label:'0.5分',color:'red'}, {value:1,label:'1分',color:'pink'}, {value:1,label:'推送完毕',color:'purple'}, {value:1.5,label:'1.5分',color:'deep-purple'}
        , {value:2,label:'2分',color:'indigo'}, {value:2.5,label:'2.5分',color:'blue'}, {value:3,label:'3分',color:'cyan'}, {value:3.5,label:'3.5分',color:'teal'}, {value:4,label:'4分',color:'teal'}
        , {value:4.5,label:'4.5分',color:'green'}, {value:5,label:'5分',color:'positive'}],
      statusOptions:[{value:0,label:'已删除',color: 'negative'}, {value:1,label:'正常',color:'positive'}, {value:2,label:'被禁止',color:'purple'}, {value:3,label:'被锁定',color:'warning'}, {value:4,label:'过期',color:'pink'}],
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
    updateVideoInfoField(update){
      console.log('updateVideoInfoField')
      console.log(update)
    },
    goViewVideo(videoId){
      let routeUrl =this.$router.resolve({ name: 'video', params: { vid:videoId }})
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
