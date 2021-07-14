<template>
  <div>
    <q-card class="my-card no-wrap  bg-grey-1 full-width" flat bordered>
      <q-card-section>
        <q-table
            title="用户管理"
            :data="data"
            :columns="columns"
            :pagination.sync="pagination"
            :loading="loading"
            row-key="userId"
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
            <q-btn flat round color="positive" icon="person_add" class="q-ml-lg" @click="addUserDialog = true"><q-tooltip>添加用户</q-tooltip></q-btn>
          </template>

          <template v-slot:body="props">
            <q-tr :props="props">
              <q-td key="userId" :props="props">
                <div style="cursor: pointer"  @click="goUserProfile(props.row.userId)">{{ props.row.userId }}</div>
                <q-popup-edit v-model="props.row.userId" disable>
                  <q-input v-model="props.row.userId" type="text" hint="用户ID" autocomplete="on"  maxlength="32"  :dense="true"
                           autofocus counter  clearable outlined>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="userName" :props="props">
                {{ props.row.userName }}
                <q-popup-edit v-model="props.row.userName" :validate="validUsername" @hide="validUsername"
                              @save="updateUserInfoField({userId:props.row.userId,userName:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.userName" type="text" hint="用户名" autocomplete="on"  :error="userNameValidateError"
                           :error-message="userNameValidateWarning" maxlength="20"  counter :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="person_pin" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="nickName" :props="props">
                {{ props.row.nickName }}
                <q-popup-edit v-model="props.row.nickName"
                              :validate="validNickname"
                              @hide="validNickname"  @save="updateUserInfoField({userId:props.row.userId,nickName:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.nickName" type="text" hint="昵称" autocomplete="on"  :error="nickNameValidateError"
                           :error-message="nickNameValidateWarning" maxlength="20"   :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="person_pin" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="gender" :props="props">
                {{ props.row.gender }}
                <q-popup-edit v-model="props.row.gender" @save="updateUserInfoField({userId:props.row.userId,gender:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-option-group
                      inline
                      :options="genderOptions"
                      type="radio"
                      v-model="props.row.gender"
                  />
                </q-popup-edit>
              </q-td>
              <q-td key="phoneNumber" :props="props">
                {{ props.row.phoneNumber }}
                <q-popup-edit v-model="props.row.phoneNumber"
                              :validate="validPhoneNumber"
                              @hide="validPhoneNumber"  @save="updateUserInfoField({userId:props.row.userId,phoneNumber:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.phoneNumber" type="text" hint="手机号码" autocomplete="on"  :error="phoneNumberValidateError"
                           :error-message="phoneNumberValidateWarning" maxlength="16"    mask="(##) ###########"
                           fill-mask :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="phone_iphone" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="email" :props="props">
                {{ props.row.email }}
                <q-popup-edit v-model="props.row.email"
                              :validate="validEmail"
                              @hide="validEmail"  @save="updateUserInfoField({userId:props.row.userId,email:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input v-model="props.row.email" type="text" hint="邮箱地址" autocomplete="on"  :error="emailValidateError"
                           :error-message="emailValidateWarning" maxlength="25"  :dense="true"
                           autofocus  clearable outlined>
                    <template v-slot:prepend>
                      <q-icon name="email" />
                    </template>
                  </q-input>
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
              <q-td key="gmtBirthday" :props="props">
                {{ props.row.gmtBirthday }}
                <q-popup-edit v-model="props.row.gmtBirthday"
                              :validate="validBirthday"
                              @hide="validBirthday"  @save="updateUserInfoField({userId:props.row.userId,gmtBirthday:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input :dense="true" v-model="props.row.gmtBirthday" outlined type="date" :hint="props.row.gmtBirthday" :error="birthdayValidateError"
                           :error-message="birthdayValidateWarning" clearable>
                    <template v-slot:prepend>
                      <q-icon name="event" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
              <q-td key="gmtLastLogin" :props="props">
                {{ props.row.gmtLastLogin }}
                <q-popup-edit v-model="props.row.gmtLastLogin" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="lastLoginIp" :props="props">
                {{ props.row.lastLoginIp }}
                <q-popup-edit v-model="props.row.lastLoginIp" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="status" :props="props">
                {{ props.row.status }}
                <q-popup-edit v-model="props.row.status" @save="updateUserInfoField({userId:props.row.userId,status:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-option-group
                      inline
                      :options="statusOptions"
                      type="radio"
                      v-model="props.row.status"
                  />
                </q-popup-edit>
              </q-td>
              <q-td key="role" :props="props">
                {{ props.row.role }}
                <q-popup-edit v-model="props.row.role" @save="updateUserInfoField({userId:props.row.userId,role:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-option-group
                      inline
                      :options="roleOptions"
                      type="radio"
                      v-model="props.row.role"
                  />
                </q-popup-edit>
              </q-td>
              <q-td key="authority" :props="props">
                {{ props.row.authority }}
                <q-popup-edit v-model="props.row.authority" disable>
                </q-popup-edit>
              </q-td>
              <q-td key="level" :props="props">
                {{ props.row.level }}
                <q-popup-edit v-model="props.row.level" @save="updateUserInfoField({userId:props.row.userId,level:$event})"
                              buttons label-set="保存" label-cancel="关闭">
                  <q-input :dense="true" v-model="props.row.level" outlined  type="text"  mask="######"
                           fill-mask hint="等级经验" clearable>
                    <template v-slot:prepend>
                      <q-icon name="edit_road" />
                    </template>
                  </q-input>
                </q-popup-edit>
              </q-td>
            </q-tr>
          </template>
        </q-table>
      </q-card-section>

    </q-card>
    <q-dialog v-model="addUserDialog">
      <AddUser></AddUser>
    </q-dialog>
  </div>
</template>

<script>
import {FUN} from '@/utils/julyCommon'
import {isNotEmpty, validatePhone, validateUsername, validEmail} from "@/utils/validate";
import {date} from "quasar";
import {CONSTANT} from "@/utils/constant";
import AddUser from "@/views/admin/AddUser";
export default {
  name: "UserManagement",
  components: {
    AddUser
  },
  data () {
    return {
      addUserDialog: false,
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
          label: '用户ID',
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
        },
        {
          name: 'nickName',
          align: 'center',
          label: '昵称',
          field: 'nickName',
          sortable: false
        },
        { name: 'gender', label: '性别', align: 'center', field: row => row.gender, format: val =>  val=`${val}`+'sdas', sortable: true },
        { name: 'phoneNumber', label: '手机号码', align: 'center', field: 'phoneNumber', sortable: false },
        { name: 'email', label: '邮箱地址', field: 'email', sortable: false },
        { name: 'gmtCreate', label: '创建时间', align: 'center', field: 'gmtCreate', sortable: true },
        { name: 'gmtUpdate', label: '上一次更新', align: 'center', field: 'gmtUpdate', sortable: true },
        { name: 'gmtBirthday', label: '出生日期', align: 'center', field: 'gmtBirthday', sortable: true },
        { name: 'gmtLastLogin', label: '上一次登录', align: 'center', field: 'gmtLastLogin', sortable: true },
        { name: 'lastLoginIp', label: '上一次登录IP', align: 'center', field: 'lastLoginIp', sortable: false },
        { name: 'status', label: '状态', align: 'center', field: 'status', format: val =>  `${val}`+"1", sortable: false },
        {
          name: 'role',
          label: '系统角色',
          field: 'role',
          align: 'center',
          sortable: false,
          format: val =>  FUN.filterPrintRole(val),
        },
        {
          name: 'authority',
          label: '权限',
          field: 'authority',
          sortable: false,
          align: 'center',
        },
        {
          name: 'level',
          label: '等级',
          field: 'level',
          sortable: true,
          align: 'center',
        },
      ],
      data: [],
      original: [
        {
          userId: '123125123123',
          userName: 'Tom',
          nickName: 'Cat',
          gender: 'male',
          phoneNumber: '+86 136662313123',
          email: 'asdadw@aw.csd',
          gmtCreate: '2021-07-02 14:32:21',
          gmtUpdate: '2021-07-02 14:32:21',
          gmtBirthday: '2021-07-02 14:32:21',
          gmtLastLogin: '2021-07-02 14:32:21',
          lastLoginIp: '2021-07-02 14:32:21',
          status: 1,
          role: 'ROLE_EXPERT',
          authority: ['VIDEO_VIEW','COMMENT_VIEW','COMMENT_CREATE'],
          level: 131,
        },
        {
          userId: '123125123124',
          userName: 'Tom',
          nickName: 'Cat',
          gender: 'male',
          phoneNumber: '+86 136662313123',
          email: 'asdadw@aw.csd',
          gmtCreate: '2021-07-02 14:32:21',
          gmtUpdate: '2021-07-02 14:32:21',
          gmtBirthday: '2021-07-02 14:32:21',
          gmtLastLogin: '2021-07-02 14:32:21',
          lastLoginIp: '2021-07-02 14:32:21',
          status: 1,
          role: 'ROLE_EXPERT',
          authority: ['VIDEO_VIEW','COMMENT_VIEW','COMMENT_CREATE'],
          level: 131,
        },
        {
          userId: '123125123125',
          userName: 'Tom',
          nickName: 'Cat',
          gender: 'male',
          phoneNumber: '+86 136662313123',
          email: 'asdadw@aw.csd',
          gmtCreate: '2021-07-02 14:32:21',
          gmtUpdate: '2021-07-02 14:32:21',
          gmtBirthday: '2021-07-02 14:32:21',
          gmtLastLogin: '2021-07-02 14:32:21',
          lastLoginIp: '2021-07-02 14:32:21',
          status: 1,
          role: 'ROLE_EXPERT',
          authority: ['VIDEO_VIEW','COMMENT_VIEW','COMMENT_CREATE'],
          level: 131,
        },
      ],
      userNameValidateError: false,
      userNameValidateWarning: '请输入符合格式的姓名',
      nickNameValidateError: false,
      nickNameValidateWarning: '请输入符合格式的昵称',
      phoneNumberValidateError: false,
      phoneNumberValidateWarning: '请输入符合格式的手机号码',
      birthdayValidateError: false,
      birthdayValidateWarning: '请输入符合格式的出生日期',
      emailValidateError:false,
      emailValidateWarning: '请输入符合格式的邮箱地址',

      nickName_isCorrect: null,
      email_isCorrect: null,
      phoneNumber_isCorrect: null,
      statusOptions:[{value:0,label:'已删除',color: 'negative'}, {value:1,label:'正常',color:'positive'}, {value:2,label:'被禁止',color:'purple'}, {value:3,label:'被锁定',color:'warning'}, {value:4,label:'过期',color:'pink'}],
      genderOptions:[{value:'male',label:'男',color: 'green'}, {value:'female',label:'女',color:'teal'}],
      roleOptions:[{value:'ROLE_VISITOR',label:'Visitor',color: 'indigo'}, {value:'ROLE_USER',label:'User',color:'blue'}, {value:'ROLE_AUTHOR',label:'Author',color:'cyan'}, {value:'ROLE_EXPERT',label:'Expert',color:'teal'}, {value:'ROLE_ADMIN',label:'Admin',color:'amber'}, {value:'ROLE_SUPER_ADMIN',label:'Super admin',color:'orange'}],
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
    updateUserInfoField(update){
      console.log('updateUserInfoField')
      console.log(update)
    },

    validUsername(val) {
      if (isNotEmpty(val) && validateUsername(val)) {
        this.userNameValidateError= false
        return true
      } else {
        this.userNameValidateError= true
        return false
      }
    },
    validNickname(val) {
      if (isNotEmpty(val) && validateUsername(val)) {
        this.nickNameValidateError= false
        return true
      } else {
        this.nickNameValidateError= true
        return false
      }
    },
    validBirthday(val) {
      if (isNotEmpty(val)) {
        console.log(val)
        let nowDate = new Date(Date.now())  // 记录现在更新时间
        let tempDate = date.extractDate(val, CONSTANT.DATE_FORMAT_3)
        if(nowDate>tempDate){
          this.birthdayValidateError= false
          return true
        }else{
          this.birthdayValidateWarning='请选择以往日期'
          this.birthdayValidateError= true
          return false
        }
      } else {
        this.birthdayValidateError= true
        return false
      }
    },
    async validEmail(val) {
      if (isNotEmpty(val) && validEmail(val)) {
        let pass = await this.duplicateUser({email: val})
        console.log('validateEmail: pass:'+pass)
        if (!pass){
          this.emailValidateWarning='已存在相同Email地址'
          this.emailValidateError = true
          return false
        }else{
          this.emailValidateError = false
          return true
        }
      } else {
        this.emailValidateError = true
        return false
      }
    },
    async validPhoneNumber(val) {
      console.log('validPhoneNumber: val:'+val)
      if (isNotEmpty(val) && validatePhone(val)) {
        let pass = await this.duplicateUser({phoneNumber: val})
        console.log('validPhoneNumber: pass:'+pass)
        if (!pass){
          this.phoneNumberValidateWarning='已存在相同手机号码'
          this.phoneNumberValidateError = true
          return false
        }else{
          this.phoneNumberValidateError = false
          return true
        }
      } else {
        this.phoneNumberValidateError = true
        return false
      }
    },
    duplicateUser(val){
      return new Promise((resolve)=> {
        this.$store.dispatch('user/validate', val)
            .then((data) => {
              console.log('duplicateUserInfo pass')
              if (data == 'not pass') {
                resolve(false)
              } else {
                resolve(true)
              }
            })
            .catch((error) => {
              console.log('duplicateUserInfo error ' + error)
              resolve(true)
            })
      })
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
