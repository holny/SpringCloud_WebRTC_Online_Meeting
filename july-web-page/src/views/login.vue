<template>
 <div class="login-container">
   <div class="q-pa-md row items-start q-gutter-md">
     <q-card class="my-card" flat bordered>
       <q-img height="50px" placeholder-src="https://cdn.quasar.dev/img/parallax2.jpg"
       />

       <q-card-section>
         <div class="text-h5 text-orange-9">登录</div>

         <q-form
             class="column q-gutter-y-lg" ref="loginForm"
         >
           <div class="col text-caption text-grey">
             Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
           </div>
           <q-input class="col" v-model="loginForm.account" ref="account" prop="account"  name="account"
                    type="text" label="请输入账号"  tabindex="1" debounce="500"
                    autocomplete="off" @keyup.native="accountWarningShow=false;accountWarning='请输入符合格式的账号';" :rules="[ val => validAccount(val)|| accountWarning]"
                    maxlength="20" placeholder="xxxx@mail.com"  :dense="dense" lazy-rules clearable outlined>
             <template v-slot:prepend>
               <q-icon name="account_circle" />
             </template>
           </q-input>
           <q-input class="col" v-model="loginForm.password" ref="password" prop="password"  name="password"
                    :type="isPwd ? 'password' : 'text'" label="请输入密码"  tabindex="2" debounce="500"
                    autocomplete="off" @keyup.enter.native="handleLogin" @keyup.native="passwordKeyUpEvent" :rules="[ val => validPassword(val) || passwordWarning]"
                    maxlength="25" placeholder="Password" :dense="dense" lazy-rules outlined  show-password>
             <template v-slot:prepend>
               <q-icon name="password" />
             </template>
             <template v-slot:append>
               <q-icon
                   :name="isPwd ? 'visibility_off' : 'visibility'"
                   class="cursor-pointer"
                   @click="isPwd = !isPwd"
               />
               <q-icon v-show="isCapsMode"
                   name="font_download"
               />
             </template>

           </q-input>
           <q-checkbox class="col float-left"  v-model="loginForm.rememberMe" :dense="dense" name="rememberMe" label="记住登录" tabindex="3"  color="teal" />
           <q-btn class="col btn-fixed-width" color="primary" :disable="loading" @click.native.prevent="handleLogin" :label="loading?'':'登录'" tabindex="4"><q-spinner-ball color="white" v-show="loading" /></q-btn>
         </q-form>
       </q-card-section>

       <q-card-actions>
         <q-btn flat :dense="dense" color="positive" label="首页" />
         <q-space/>
         <q-btn flat :dense="dense" color="primary" label="注册" @click="$emit('changeCom','register')" />
       </q-card-actions>

       <q-ajax-bar
           ref="ajaxBar"
           position="bottom"
           color="accent"
           size="10px"
           skip-hijack
       />
     </q-card>
   </div>
 </div>
</template>

<script>

import {isNotEmpty,isPassword} from "@/utils/validate";

export default {
  name: 'LoginCard',
  // components: { SocialSign },
  data () {
    return {
      dense: true,
      isPwd: true,
      loginForm: {
        account: '',
        password: '',
        rememberMe: false
      },
      isCapsMode: false,
      accountWarning: '',
      accountWarningShow: false,
      passwordWarning: '',
      passwordWarningShow: false,
      loading: false,
      redirect: undefined,
      otherQuery: {}
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        const query = route.query
        if (query) {
          console.log('watch route handler query:' + JSON.stringify(query))
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
    validAccount (val) {
      if (this.accountWarningShow){
        return false
      }else{
        return isNotEmpty(val)
      }
    },
    validPassword (val) {
      if (this.passwordWarningShow) {
        console.log('validPassword')
        return false
      }else{
        return isPassword(val)
      }
    },
    trigger () {
      const bar = this.$refs.bar

      bar.start()

      this.timer = setTimeout(() => {
        if (this.$refs.bar) {
          this.$refs.bar.stop()
        }
      }, Math.random() * 3000 + 1000)
    },
    passwordKeyUpEvent (e){
      this.passwordWarningShow=false
      this.passwordWarning='请输入符合格式的密码'
      const {key} = e
      this.isCapsMode = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    handleLogin () {
      this.$refs.loginForm.validate().then(valid =>{
        console.log("loginForm valid")
        console.log(valid)
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm)
              .then(() => {
                this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
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
                console.log("login error")
                console.log(error)
                this.$q.notify({
                  type: 'negative',
                  message: error,
                  position: 'top',
                  timeout: 5000
                })
                this.loading = false
              })
        } else {
          console.log('error submit!!')
          // this.passwordWarning = 'error submit!'
          // this.passwordWarningShow = true
          // this.$refs.password.validate()
          this.$q.notify({
            type: 'warning',
            message: '请检查输入信息是否正确再提交',
            position: 'top',
            timeout: 5000
          })
          return false
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

  // afterQRScan() {
  //   if (e.key === 'x-admin-oauth-code') {
  //     const code = getQueryObject(e.newValue)
  //     const codeMap = {
  //       wechat: 'code',
  //       tencent: 'code'
  //     }
  //     const type = codeMap[this.auth_type]
  //     const codeName = code[type]
  //     if (codeName) {
  //       this.$store.dispatch('LoginByThirdparty', codeName).then(() => {
  //         this.$router.push({ path: this.redirect || '/' })
  //       })
  //     } else {
  //       alert('第三方登录失败')
  //     }
  //   }
  // }
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
