<template>
  <q-card class="my-card bg-grey-1 no-wrap full-width" flat bordered style="min-width: 500px">
    <q-card-section>
      <div class="text-h5 q-mt-sm text-center">我的信息</div>
    </q-card-section>
    <q-card-section>
      <q-separator spaced />
      <q-item-label header>基本信息</q-item-label>
      <q-list>
        <q-item class="full-width">
          <q-item-section >
            <q-field hint="用户Id" :dense="true" style="width: 80%">
              <template v-slot:control>
                <div class="self-center full-width no-outline text-bold" tabindex="0">{{userInfo.userId}}</div>
              </template>
            </q-field>
          </q-item-section>
          <q-item-section >
            <q-field hint="创建时间" :dense="true" style="width: 80%">
              <template v-slot:control>
                <div class="self-center full-width no-outline text-bold" tabindex="0">{{userInfo.gmtCreate}}</div>
              </template>
            </q-field>
          </q-item-section>
        </q-item>
        <q-item class="full-width">
          <q-item-section >
            <q-field hint="上一次登录时间" :dense="true" style="width: 80%">
              <template v-slot:control>
                <div class="self-center full-width no-outline text-bold" tabindex="0">{{userInfo.gmtLastLogin}}</div>
              </template>
            </q-field>
          </q-item-section>
          <q-item-section >
            <q-field hint="上一次登录IP" :dense="true" style="width: 80%">
              <template v-slot:control>
                <div class="self-center full-width no-outline text-bold" tabindex="0">{{userInfo.lastLoginIp}}</div>
              </template>
            </q-field>
          </q-item-section>
        </q-item>
        <q-item class="full-width">
          <q-item-section >
            <q-field hint="系统角色" :dense="true" style="width: 80%">
              <template v-slot:control>
                <div class="self-center full-width no-outline text-bold" tabindex="0">{{userInfo.role}}</div>
              </template>
            </q-field>
          </q-item-section>
          <q-item-section >
            <q-field hint="账号状态" :dense="true" style="width: 80%">
              <template v-slot:control>
                <div class="self-center full-width no-outline text-bold" tabindex="0">{{userInfo.status}}</div>
              </template>
            </q-field>
          </q-item-section>
        </q-item>
        <q-item class="full-width">
          <q-item-section >
            <q-field hint="等级" :dense="true" style="width: 40%">
              <template v-slot:control>
                <div class="self-center full-width no-outline text-bold" tabindex="0">{{userInfo.level}}</div>
              </template>
            </q-field>
          </q-item-section>

        </q-item>
      </q-list>
    </q-card-section>
    <q-card-section>
      <q-separator spaced />
      <q-item-label header>个性信息</q-item-label>
      <q-list>
        <q-item class="full-width">
          <q-item-section >

            <q-file outlined bottom-slots v-model="avatarFile" label="选择上传新头像文件"
                    accept=".png, .jpg, .jpeg, image/*"
                    @rejected="onUploadAvatarRejected"
                    max-file-size="10485760" style="width: 73%"
                    :dense="true"  clearable counter>
              <template v-slot:before>
                <q-avatar>
                  <img src="https://cdn.quasar.dev/img/avatar5.jpg">
                </q-avatar>
              </template>

              <template v-slot:append>
                <q-icon name="create_new_folder" @click.stop />
              </template>

              <template v-slot:after>
                <q-btn v-show="avatarFile!=null" round dense flat icon="eva-cloud-upload-outline" >
                  <q-tooltip
                      transition-show="scale"
                      transition-hide="scale"
                  >
                    确认上传
                  </q-tooltip>
                </q-btn>
              </template>
            </q-file>
          </q-item-section>
          <q-item-section >
            <q-input class="col" v-model="userInfo.userName" ref="userName"  name="userName"
                     type="text"  debounce="500" style="width: 80%" counter hint="用户名"
                     autocomplete="on" :rules="[ val => validUsername(val)|| userNameValidateWarning]"
                     maxlength="20"   :dense="true" lazy-rules clearable outlined>
              <template v-slot:prepend>
                <q-icon name="person_pin" />
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
        <q-item class="full-width">
          <q-item-section >
            <q-input class="col" v-model="userInfo.nickName" ref="nickName"  name="nickName"
                     type="text"  debounce="500" style="width: 80%" counter  hint="昵称"
                     autocomplete="on" :rules="[ val => validNickname(val)|| nickNameValidateWarning]"
                     maxlength="20" :dense="true" lazy-rules clearable outlined>
              <template v-slot:prepend>
                <q-icon name="person_pin" />
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
          <q-item-section >
            <q-input :dense="true" v-model="userInfo.gmtBirthday" outlined type="date" hint="出生日期" @keyup.native="birthdayValidateWarning='请选择正确的出生日期'" :rules="[ val => validBirthday(val)|| birthdayValidateWarning]" clearable>
              <template v-slot:prepend>
                <q-icon name="person_pin" />
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
        <q-item class="full-width">
          <q-item-section >
            <q-input class="col" v-model="userInfo.email" ref="email"  name="email"
                     type="text"  debounce="500" style="width: 80%" counter  hint="邮箱地址"
                     autocomplete="on" @keyup.native="emailValidateWarning='请输入符合格式的邮箱地址'" :rules="[ val => validEmail(val)|| emailValidateWarning]"
                     maxlength="20" :dense="true" lazy-rules clearable outlined>
              <template v-slot:prepend>
                <q-icon name="email" />
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
          <q-item-section >
            <q-input class="col" v-model="userInfo.phoneNumber" ref="phoneNumber"  name="phoneNumber"
                     type="text"  debounce="500" style="width: 80%" counter  hint="手机号码"
                     autocomplete="on" @keyup.native="phoneNumberValidateWarning='请输入符合格式的手机号码'" :rules="[ val => validPhoneNumber(val)|| phoneNumberValidateWarning]"
                     maxlength="15" :dense="true" lazy-rules clearable outlined>
              <template v-slot:prepend>
                <q-icon name="phone_iphone" />
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
        <q-item class="full-width">
          <q-item-section >
            <q-select
                outlined
                bottom-slots
                :dense="true"
                v-model="userInfo.gender"
                :options="genderOptions"
                :option-value="opt => Object(opt) === opt && 'code' in opt ? opt.code : null"
                :option-label="opt => Object(opt) === opt && 'desc' in opt ? opt.desc : '- 未知 -'"
                hint="选择性别"
                emit-value
                map-options
                style="width: 40%;"
            >
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
            </q-select>
          </q-item-section>
        </q-item>
      </q-list>
    </q-card-section>
  </q-card>
</template>

<script>
import {FUN} from "@/utils/julyCommon";
import {isNotEmpty, validatePhone, validateUsername, validEmail} from "@/utils/validate";
import {date} from "quasar";
import {CONSTANT} from "@/utils/constant";

export default {
  name: "MyInfoSettings",
  components:{

  },
  data () {
    return {
      userInfo: {
        // constant
        userId: '100002',
        gmtCreate: '2021-06-03 14:32:21',
        gmtLastLogin: '2021-07-13 15:22:51',
        lastLoginIp: '192:168:131:22',
        level: '14',
        role: '管理员',
        status: '正常',
        // could modify
        avatar: 'https://cdn.quasar.dev/img/avatar5.jpg',
        gmtBirthday: '1996-07-13',
        userName: 'Tom',
        nickName: 'Cat',
        email: 'test@test.com',
        phoneNumber:'+86  136666666',
        gender: 'male'
      },
      userNameValidateWarning: '请输入符合格式的姓名',
      nickNameValidateWarning: '请输入符合格式的昵称',
      birthdayValidateWarning: '请选择正确的出生日期',
      emailValidateWarning: '请输入符合格式的邮箱地址',
      phoneNumberValidateWarning: '请输入符合格式的手机号码',
      nickName_isCorrect: null,
      email_isCorrect: null,
      phoneNumber_isCorrect: null,
      avatarFile: null,
      genderOptions:[{code:'male',desc:'男'}, {code:'female',desc:'女'}]
    }
  },
  activated() {
    console.log("MyInfoSetting created")
  },
  created() {
    console.log("MyInfoSetting created")
  },
  mounted () {
    console.log("MyInfoSetting mounted")
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
  height: 100%;
  border-radius:10px;
  width: 100%;
}
.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}
</style>
