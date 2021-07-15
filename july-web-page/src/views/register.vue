<template>
 <div class="login-container">
   <div class="q-pa-md row items-start q-gutter-md">
     <q-card class="my-card" flat bordered>

       <q-img height="50px" placeholder-src="https://cdn.quasar.dev/img/parallax2.jpg"
       />

       <q-card-section>
         <div class="text-h5 text-teal-5">注册</div>

         <q-form
             class="column q-gutter-y-lg" ref="registerForm"
         >
           <div class="col text-caption text-grey">
             Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
           </div>
           <q-input class="col" v-model="registerForm.userName" ref="userName" prop="userName"  name="userName"
                    type="text" label="请输入姓名 *"  tabindex="1" debounce="500"
                    autocomplete="on" @keyup.native="registerUsernameWarningShow=false;registerUsernameWarning='请输入符合格式的姓名';" :rules="[ val => validUsername(val)|| registerUsernameWarning]"
                    maxlength="20" placeholder="User name"  :dense="dense" lazy-rules clearable outlined>
             <template v-slot:prepend>
               <q-icon name="person_pin" />
             </template>
           </q-input>

           <q-input class="col" v-model="registerForm.email" ref="email" prop="email"  name="email"
                    type="text" label="请输入邮箱地址 *"  tabindex="2" debounce="500"
                    autocomplete="on" @keyup.native="registerEmailWarningShow=false;registerEmailWarning='请输入符合格式的邮箱地址'" :rules="[ val => validEmail(val)]"
                    maxlength="20" placeholder="Email address"  :dense="dense" lazy-rules clearable outlined>
             <template v-slot:prepend>
               <q-icon name="email" />
             </template>
           </q-input>

           <q-input class="col" v-model="registerForm.phoneNumber" ref="phoneNumber" prop="phoneNumber"  name="phoneNumber"
                    type="text" label="请输入手机号码 *"  tabindex="3" debounce="500"
                    autocomplete="on" @keyup.native="registerPhoneNumberWarningShow=false;registerPhoneNumberWarning='请输入符合格式的手机号码'" :rules="[ val => validPhoneNumber(val)]"
                    maxlength="15" placeholder="Phone number"  :dense="dense" lazy-rules clearable outlined>
             <template v-slot:prepend>
               <q-icon name="phone_iphone" />
             </template>
           </q-input>

           <q-input class="col" v-model="registerForm.newPassword" ref="newPassword" prop="newPassword"  name="newPassword"
                    :type="isShowNewPwd ? 'text':'password'" label="请输入密码 *"  tabindex="4" debounce="200" @keyup.native="newPasswordKeyUpEvent" :rules="[ val => validNewPassword(val)|| registerNewPWDWarning]"
                    autocomplete="off" @blur="isNewPWDCapsMode = false"
                    maxlength="25" placeholder="Password" :dense="dense" lazy-rules outlined>
             <template v-slot:prepend>
               <q-icon name="password" />
             </template>
             <template v-slot:append>
               <q-icon
                   :name="isShowNewPwd ? 'visibility':'visibility_off'"
                   class="cursor-pointer"
                   @click="isShowNewPwd = !isShowNewPwd"
               />
               <q-icon v-show="isNewPWDCapsMode"
                       name="font_download"
               />
             </template>
           </q-input>
           <q-input class="col" v-model="registerForm.confirmPassword" ref="confirmPassword" prop="confirmPassword"  name="password"
                    :type="isShowConfirmPwd ?  'text':'password'" label="请输入密码 *"  tabindex="5" debounce="200" @keyup.native="confirmPasswordKeyUpEvent" :rules="[ val => validConfirmPassword(val)|| registerConfirmPWDWarning]"
                    autocomplete="off" @blur="isConfirmPWDCapsMode = false"
                    maxlength="25" placeholder="Repeat password" :dense="dense" lazy-rules outlined>
             <template v-slot:prepend>
               <q-icon name="password" />
             </template>
             <template v-slot:append>
               <q-icon
                   :name="isShowConfirmPwd ?  'visibility':'visibility_off'"
                   class="cursor-pointer"
                   @click="isShowConfirmPwd = !isShowConfirmPwd"
               />
               <q-icon v-show="isConfirmPWDCapsMode"
                       name="font_download"
               />
             </template>
           </q-input>
           <q-input class="col" v-model="registerForm.registerCode" ref="registerCode" prop="registerCode"  name="registerCode"
                    type="text" label="请输入注册码"  tabindex="6" debounce="500"
                    autocomplete="on" @keyup.native="registerRegCodeWarningShow=false;registerRegCodeWarning='请输入符合格式的注册码';" :rules="[ val => validRegisterCode(val)|| registerRegCodeWarning]"
                    maxlength="12" placeholder="Reigster code"  :dense="dense" lazy-rules clearable outlined>
             <template v-slot:prepend>
               <q-icon name="bolt" />
             </template>
           </q-input>

           <q-btn class="col btn-fixed-width" :disable="loading" color="primary" @click.native.prevent="handleRegister" :label="loading?'':'注册'" tabindex="4"><q-spinner-ball color="white" v-show="loading" /></q-btn>
         </q-form>
       </q-card-section>

       <q-card-actions>
         <q-btn flat :dense="dense" color="positive" label="首页" />
         <q-space/>
         <q-btn flat :dense="dense" color="primary" label="登录" @click="$emit('changeCom','login')" />
       </q-card-actions>
     </q-card>
   </div>
 </div>
</template>

<script>
import {isNotEmpty,validateUsername, isPassword, validAlphabets,validatePhone} from '@/utils/validate'
export default {
  name: 'RegisterCard',
  // components: { SocialSign },
  data () {
    return {
      registerForm: {
        userName: '',
        email: '',
        phoneNumber: '',
        newPassword: '',
        confirmPassword: '',
        registerCode: ''
      },
      dense: true,
      isShowNewPwd: false,
      isShowConfirmPwd: false,
      isNewPWDCapsMode: false,
      isConfirmPWDCapsMode: false,
      registerUsernameWarning: '',
      registerUsernameWarningShow: false,
      registerEmailWarning: 'asdawfa',
      registerEmailWarningShow: false,
      registerPhoneNumberWarning: '',
      registerPhoneNumberWarningShow: false,
      registerNewPWDWarning: '',
      registerNewPWDWarningShow: false,
      registerConfirmPWDWarning: '',
      registerConfirmPWDWarningShow: false,
      registerRegCodeWarning: '',
      registerRegCodeWarningShow: false,
      loading: false,
      redirect: undefined,
      otherQuery: {}
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        const query = route.query
        console.log('watch route handler query:' + query)
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created () {
    // window.addEventListener('storage', this.afterQRScan)
  },
  // keep-alive会缓存组件状态，activated()和 deactivated() 这两个钩子函数。activated()是keep-alive 组件激活时调用，而 deactivated() 是 keep-alive 组件停用时调用。activated ()替换mounted()
  activated () {
  },
  destroyed () {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    validUsername(val) {
      console.log('validateUsername:'+val +'  '+this.registerUsernameWarningShow)
      if (isNotEmpty(val) && validateUsername(val)&&!this.registerUsernameWarningShow) {
        return true
      } else {
        return false
      }
    },
    async validEmail(val) {
      console.log('validateEmail:'+val +'  '+this.registerEmailWarningShow)
      console.log('validateEmail:'+val +'  '+this.registerEmailWarning)
      if (isNotEmpty(val) && validateUsername(val)&&!this.registerEmailWarningShow) {
        let pass = await this.duplicateUser({email: val})
        console.log('validateEmail: pass:'+pass)
        if (!pass){
          this.registerEmailWarning='已存在相同Email地址'
          return this.registerEmailWarning
        }else{
          return true
        }
      } else {
        return this.registerEmailWarning
      }
    },
    async validPhoneNumber(val) {
      if (isNotEmpty(val) && validatePhone(val)&&!this.registerPhoneNumberWarningShow) {
        let pass = await this.duplicateUser({phoneNumber: val})
        console.log('validPhoneNumber: pass:'+pass)
        if (!pass){
          this.registerPhoneNumberWarning='已存在相同手机号码'
          return this.registerPhoneNumberWarning
        }else{
          return true
        }
      } else {
        return this.registerPhoneNumberWarning
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
    validNewPassword(val) {
      console.log('validNewPassword val ' + val+ '  '+this.registerNewPWDWarningShow)
      if (!isNotEmpty(val) || val.length < 4 || !isPassword(val) || this.registerNewPWDWarningShow) {
        return false
      } else {
        console.log('validNewPassword val pass ')
        if (this.registerForm.confirmPassword !== '') {
          this.$refs.confirmPassword.validate()
        }
        return true
      }
    },
    validConfirmPassword(val) {
      console.log('validConfirmPassword val ' + val+ ' '+this.registerConfirmPWDWarningShow)
      if (!isNotEmpty(val) || val.length < 4 || !isPassword(val) || this.registerConfirmPWDWarningShow) {
        return false
      } else {
        if (val !== this.registerForm.newPassword) {
          console.log('validConfirmPassword val not pass '+this.registerForm.newPassword)
          this.registerConfirmPWDWarning = '与第一次密码不相同'
          return false
        } else{
          console.log('validConfirmPassword val pass ')
          return true
        }
      }
    },
    validRegisterCode(val) {
      if((isNotEmpty(val) && val.length>0 &&!validAlphabets(val)) ||this.registerRegCodeWarningShow){
        return false
      }else{
        return true
      }
    },
    newPasswordKeyUpEvent(e){
      this.registerNewPWDWarningShow=false
      this.registerNewPWDWarning='请输入符合格式的密码'
      const {key} = e
      this.isNewPWDCapsMode = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    confirmPasswordKeyUpEvent(e){
      this.registerConfirmPWDWarningShow=false
      this.registerConfirmPWDWarning='请再次输入符合格式的密码'
      const {key} = e
      this.isConfirmPWDCapsMode = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    handleRegister () {
      this.$refs.registerForm.validate().then(valid =>{
        console.log('registerForm')
        console.log(this.registerForm.userName)
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/register', this.registerForm)
              .then(() => {
                // this.$router.push({path: this.redirect || '/', query: this.otherQuery})
                this.loading = false
                if (this.$route.query.redirect) { //如果存在参数
                  let redirect = this.$route.query.redirect
                  this.$router.push(redirect)//则跳转至进入登录页前的路由
                  console.log("has redirect,"+redirect)
                } else {
                  this.$router.push('/')//否则跳转至首页
                }
              })
              .catch((error) => {
                console.log("register error")
                console.log(error)
                this.$q.notify({
                  type: 'negative',
                  message: error.toString(),
                  position: 'top',
                  timeout: 5000
                })

                this.loading = false
              })
        } else {
          this.$q.notify({
            type: 'warning',
            message: '请检查输入信息是否正确再提交',
            position: 'top',
            timeout: 5000
          })
          console.log('error submit!!')
        }
      })
    },
    getOtherQuery (query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }

  }
}
</script>

<style lang="scss" scoped>
.my-card {
  transition: box-shadow .3s;
  width: 100%;
  max-width: 400px;
  border-radius:10px;

}
.my-card:hover {
  box-shadow: 0px 0px 15px rgba(33,33,33,.2) !important;
}
</style>
