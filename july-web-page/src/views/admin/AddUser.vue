<template>
  <q-card class="my-card bg-grey-1 no-wrap full-width" flat bordered style="min-width: 500px">
    <q-card-section>
      <div class="text-h5 q-mt-sm text-center">新用户</div>
    </q-card-section>
    <q-card-section>
      <q-separator spaced />
      <q-item-label header>用户信息</q-item-label>
      <q-list>
        <q-item class="full-width">
          <q-item-section >
            <q-file outlined bottom-slots v-model="avatarFile" label="选择上传新头像文件"
                    accept=".png, .jpg, .jpeg, image/*"
                    @rejected="onUploadAvatarRejected"
                    max-file-size="10485760"
                    :dense="true"  clearable counter>
              <template v-slot:append>
                <q-icon name="create_new_folder" @click.stop />
              </template>
            </q-file>
          </q-item-section>
          <q-item-section >
            <q-input class="col" v-model="userInfo.userName" ref="userName"  name="userName"
                     type="text"  debounce="500"  counter hint="用户名"
                     autocomplete="on" :rules="[ val => validUsername(val)|| userNameValidateWarning]"
                     maxlength="20"   :dense="true" lazy-rules clearable outlined>
              <template v-slot:prepend>
                <q-icon name="person_pin" />
              </template>
            </q-input>
          </q-item-section>
        </q-item>
        <q-item class="full-width">
          <q-item-section >
            <q-input class="col" v-model="userInfo.nickName" ref="nickName"  name="nickName"
                     type="text"  debounce="500"  counter  hint="昵称"
                     autocomplete="on" :rules="[ val => validNickname(val)|| nickNameValidateWarning]"
                     maxlength="20" :dense="true" lazy-rules clearable outlined>
              <template v-slot:prepend>
                <q-icon name="person_pin" />
              </template>
            </q-input>
          </q-item-section>
          <q-item-section >
            <q-input :dense="true" v-model="userInfo.gmtBirthday" outlined type="date" hint="出生日期" @keyup.native="birthdayValidateWarning='请选择正确的出生日期'" :rules="[ val => validBirthday(val)|| birthdayValidateWarning]" clearable>
              <template v-slot:prepend>
                <q-icon name="person_pin" />
              </template>
            </q-input>
          </q-item-section>
        </q-item>
        <q-item class="full-width">
          <q-item-section >
            <q-input class="col" v-model="userInfo.email" ref="email"  name="email"
                     type="text"  debounce="500"  counter  hint="邮箱地址"
                     autocomplete="on" @keyup.native="emailValidateWarning='请输入符合格式的邮箱地址'" :rules="[ val => validEmail(val)|| emailValidateWarning]"
                     maxlength="20" :dense="true" lazy-rules clearable outlined>
              <template v-slot:prepend>
                <q-icon name="email" />
              </template>
            </q-input>
          </q-item-section>
          <q-item-section >
            <q-input class="col" v-model="userInfo.phoneNumber" ref="phoneNumber"  name="phoneNumber"
                     type="text"  debounce="500"  counter  hint="手机号码"
                     autocomplete="on" @keyup.native="phoneNumberValidateWarning='请输入符合格式的手机号码'" :rules="[ val => validPhoneNumber(val)|| phoneNumberValidateWarning]"
                     maxlength="15" :dense="true" lazy-rules clearable outlined>
              <template v-slot:prepend>
                <q-icon name="phone_iphone" />
              </template>
            </q-input>
          </q-item-section>
        </q-item>
        <q-item class="full-width">
          <q-item-section >
            <q-select
                outlined
                bottom-slots
                :dense="true"
                v-model="userInfo.gender"
                :options="genderOptions"
                :option-value="opt => Object(opt) === opt && 'value' in opt ? opt.value : null"
                :option-label="opt => Object(opt) === opt && 'label' in opt ? opt.label : '- 未知 -'"
                hint="选择性别"
                emit-value
                map-options
            >
            </q-select>
          </q-item-section>
          <q-item-section >
            <q-select
                outlined
                bottom-slots
                :dense="true"
                v-model="userInfo.role"
                :options="roleOptions"
                :option-value="opt => Object(opt) === opt && 'value' in opt ? opt.value : null"
                :option-label="opt => Object(opt) === opt && 'label' in opt ? opt.label : '- 未知 -'"
                hint="选择系统角色"
                emit-value
                map-options
            >
            </q-select>
          </q-item-section>
        </q-item>
        <q-item class="full-width">
          <q-item-section >
            <q-select
                outlined
                bottom-slots
                :dense="true"
                v-model="userInfo.status"
                :options="statusOptions"
                :option-value="opt => Object(opt) === opt && 'value' in opt ? opt.value : null"
                :option-label="opt => Object(opt) === opt && 'label' in opt ? opt.label : '- 未知 -'"
                hint="选择账号状态"
                emit-value
                map-options
                style="width: 50%"
            >
            </q-select>
          </q-item-section>
        </q-item>
      </q-list>
    </q-card-section>
    <q-card-actions  align="around">
      <q-btn push color="primary" label="保存" />
      <q-btn v-close-popup push color="white" text-color="primary" label="取消" />
    </q-card-actions>
  </q-card>
</template>

<script>
import {FUN} from "@/utils/julyCommon";
import {isNotEmpty, validatePhone, validateUsername, validEmail} from "@/utils/validate";
import {date} from "quasar";
import {CONSTANT} from "@/utils/constant";

export default {
  name: "AddUser",
  data () {
    return {
      userInfo: {
        // could modify
        avatar: '',
        gmtBirthday: '',
        userName: '',
        nickName: '',
        email: '',
        phoneNumber:'',
        gender: '',
        role: '',
        status:'',
      },
      avatarFile:'',
      userNameValidateWarning: '请输入符合格式的姓名',
      nickNameValidateWarning: '请输入符合格式的昵称',
      birthdayValidateWarning: '请输入符合格式的出生日期',
      emailValidateWarning: '请输入符合格式的邮箱地址',
      phoneNumberValidateWarning: '请输入符合格式的手机号码',
      statusOptions:[{value:0,label:'已删除',color: 'negative'}, {value:1,label:'正常',color:'positive'}, {value:2,label:'被禁止',color:'purple'}, {value:3,label:'被锁定',color:'warning'}, {value:4,label:'过期',color:'pink'}],
      genderOptions:[{value:'male',label:'男',color: 'green'}, {value:'female',label:'女',color:'teal'}],
      roleOptions:[{value:'ROLE_VISITOR',label:'Visitor',color: 'indigo'}, {value:'ROLE_USER',label:'User',color:'blue'}, {value:'ROLE_AUTHOR',label:'Author',color:'cyan'}, {value:'ROLE_EXPERT',label:'Expert',color:'teal'}, {value:'ROLE_ADMIN',label:'Admin',color:'amber'}, {value:'ROLE_SUPER_ADMIN',label:'Super admin',color:'orange'}],

    }
  },
  methods:{
    validUsername(val) {
      if (isNotEmpty(val) && validateUsername(val)) {
        return true
      } else {
        return false
      }
    },
    validNickname(val) {
      if (isNotEmpty(val) && validateUsername(val)) {
        return true
      } else {
        return false
      }
    },
    validBirthday(val) {
      if (isNotEmpty(val)) {
        let nowDate = new Date(Date.now())  // 记录现在更新时间
        let tempDate = date.extractDate(val, CONSTANT.DATE_FORMAT_3)
        if(nowDate>tempDate){
          return true
        }else{
          this.birthdayValidateWarning='请选择以往日期'
        }
      } else {
        return false
      }
    },
    async validEmail(val) {
      if (isNotEmpty(val) && validEmail(val)) {
        let pass = await this.duplicateUser({email: val})
        console.log('validateEmail: pass:'+pass)
        if (!pass){
          this.emailValidateWarning='已存在相同Email地址'
          return this.emailValidateWarning
        }else{
          return true
        }
      } else {
        return this.emailValidateWarning
      }
    },
    async validPhoneNumber(val) {
      if (isNotEmpty(val) && validatePhone(val)) {
        let pass = await this.duplicateUser({phoneNumber: val})
        console.log('validPhoneNumber: pass:'+pass)
        if (!pass){
          this.phoneNumberValidateWarning='已存在相同手机号码'
          return this.phoneNumberValidateWarning
        }else{
          return true
        }
      } else {
        return this.phoneNumberValidateWarning
      }
    },
    onUploadAvatarRejected (rejectedEntries) {
      // Notify plugin needs to be installed
      // https://quasar.dev/quasar-plugins/notify#Installation
      FUN.notify(`${rejectedEntries.length} file(s) did not pass validation constraints`,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
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
  }
}
</script>

<style scoped>
.my-card {
  transition: box-shadow .3s;
  border-radius:10px;
  width: 100%;
}
.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}
</style>
