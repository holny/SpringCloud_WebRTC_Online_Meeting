import { login, register, validate, getUserInfo,searchUser,getSocial,addSocial,removeSocial } from '@/api/user'
import { getToken, setToken, getRefreshToken, setRefreshToken, getHostId, setHostId, removeHostId, removeRefreshToken, removeToken } from '@/utils/auth'

const state = {
  hostToken: getToken(),
  hostRefreshToken: getRefreshToken(),
  hostId: getHostId(),
  hostUserName: '',
  hostAvatar: '',
  hostStatus: '',
  hostAuthority: [],
  hostRoles: []
}

const mutations = {
  SET_HOST_TOKEN: (state, token) => {
    state.hostToken = token
  },
  SET_HOST_REFRESH_TOKEN: (state, token) => {
    state.hostRefreshToken = token
  },
  SET_HOST_ID: (state, hostId) => {
    state.hostId = hostId
  },
  SET_HOST_USER_NAME: (state, name) => {
    state.hostUserName = name
  },
  SET_HOST_AVATAR: (state, avatar) => {
    state.hostAvatar = avatar
  },
  SET_HOST_STATUS: (state, status) => {
    state.hostStatus = status
  },
  SET_HOST_AUTHORITY: (state, authority) => {
    state.hostAuthority = authority
  },
  SET_HOST_ROLES: (state, roles) => {
    state.hostRoles = roles
  }
}

const actions = {
  // user login
  login ({ commit }, userInfo) {
    const {account, password, rememberMe} = userInfo
    console.log('action - login')
    console.log(account)
    console.log(password)
    console.log(rememberMe)
    removeToken()
    removeRefreshToken()
    removeHostId()
    commit('SET_HOST_TOKEN', '')
    commit('SET_HOST_REFRESH_TOKEN', '')
    commit('SET_HOST_ID', '')
    return new Promise((resolve, reject) => {
      login({ email: account.trim(), password: password, rememberMe: rememberMe }).then(response => {
        const { data } = response
        console.log(data)
        if (data.code === 10001) {
          // commit('SET_HOST_TOKEN', data.data.access_token, { root: true })
          commit('SET_HOST_TOKEN', data.data.access_token)
          setToken(data.data.access_token)
          commit('SET_HOST_REFRESH_TOKEN', data.data.refresh_token)
          setRefreshToken(data.data.refresh_token)
          commit('SET_HOST_ID', data.data.userId)
          setHostId(data.data.userId)
          resolve()
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },
  // user register
  register ({ commit }, registerInfo) {
    console.log('action - register')
    console.log(registerInfo)
    removeToken()
    removeRefreshToken()
    removeHostId()
    commit('SET_HOST_TOKEN', '')
    commit('SET_HOST_REFRESH_TOKEN', '')
    commit('SET_HOST_ID', '')
    return new Promise((resolve, reject) => {
      register({userName: registerInfo.userName, email: registerInfo.email, phoneNumber: registerInfo.phoneNumber, password: registerInfo.confirmPassword, registerCode: registerInfo.registerCode}).then(response => {
        const { data } = response
        console.log(data)
        if (data.code === 10001) {
          // commit('SET_HOST_TOKEN', data.data.access_token, { root: true })
          commit('SET_HOST_TOKEN', data.data.token.access_token)
          setToken(data.data.access_token)
          commit('SET_HOST_REFRESH_TOKEN', data.data.token.refresh_token)
          setRefreshToken(data.data.refresh_token)
          commit('SET_HOST_ID', data.data.userId)
          resolve()
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },
  // user validate
  validate ({ commit }, userInfo) {
    console.log('action - duplicate')
    console.log(userInfo)
    removeToken()
    removeRefreshToken()
    removeHostId()
    commit('SET_HOST_TOKEN', '')
    commit('SET_HOST_REFRESH_TOKEN', '')
    commit('SET_HOST_ID', '')
    return new Promise((resolve, reject) => {
      validate(userInfo).then(response => {
        const { data } = response
        console.log(data)
        if (data.code === 10001) {
          resolve('pass')
        } else {
          resolve('not pass')
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user validate
  // eslint-disable-next-line no-unused-vars
  getUserInfo ({ commit }, userId) {
    // console.log('action - getUserInfo')
    // console.log(userId)
    return new Promise((resolve, reject) => {
      getUserInfo(userId).then(response => {
        const { data } = response
        // console.log(data)
        if (data.code === 10001) {
          resolve(data.data)
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  searchUserInfo ({ commit },search) {
    commit
    console.log('action - searchUserInfo')
    console.log(search)
    return new Promise((resolve, reject) => {
      searchUser(search).then(response => {
        const { data } = response
        // console.log(data)
        if (data.code === 10001) {
          resolve(data.data)
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  getUserSocial ({ commit },userId) {
    commit
    console.log('action - getUserSocial')
    console.log(userId)
    return new Promise((resolve, reject) => {
      getSocial(userId).then(response => {
        const { data } = response
        // console.log(data)
        if (data.code === 10001) {
          resolve(data.data)
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  addUserSocial({ commit },userId,peerId,type) {
    commit
    console.log('action - addUserSocial')
    console.log(userId)
    return new Promise((resolve, reject) => {
      addSocial(userId,peerId,type).then(response => {
        const { data } = response
        // console.log(data)
        if (data.code === 10001) {
          resolve(data.data)
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  removeUserSocial ({ commit },userId,peerId,type) {
    commit
    console.log('action - removeUserSocial')
    console.log(userId)
    return new Promise((resolve, reject) => {
      removeSocial(userId,peerId,type).then(response => {
        const { data } = response
        // console.log(data)
        if (data.code === 10001) {
          resolve(data.data)
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  // // get user info
  // getInfo({ commit, state }) {
  //   return new Promise((resolve, reject) => {
  //     getInfo(state.token).then(response => {
  //       const { data } = response
  //
  //       if (!data) {
  //         reject('Verification failed, please Login again.')
  //       }
  //
  //       const { roles, name, avatar, introduction } = data
  //
  //       // roles must be a non-empty array
  //       if (!roles || roles.length <= 0) {
  //         reject('getInfo: roles must be a non-null array!')
  //       }
  //
  //       commit('SET_ROLES', roles)
  //       commit('SET_NAME', name)
  //       commit('SET_AVATAR', avatar)
  //       commit('SET_INTRODUCTION', introduction)
  //       resolve(data)
  //     }).catch(error => {
  //       reject(error)
  //     })
  //   })
  // },

  // // user logout
  // logout({ commit, state, dispatch }) {
  //   return new Promise((resolve, reject) => {
  //     logout(state.token).then(() => {
  //       commit('SET_TOKEN', '')
  //       commit('SET_ROLES', [])
  //       removeToken()
  //       resetRouter()
  //
  //       // reset visited views and cached views
  //       // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
  //       dispatch('tagsView/delAllViews', null, { root: true })
  //
  //       resolve()
  //     }).catch(error => {
  //       reject(error)
  //     })
  //   })
  // },

  // // remove token
  // resetToken({ commit }) {
  //   return new Promise(resolve => {
  //     commit('SET_TOKEN', '')
  //     commit('SET_ROLES', [])
  //     removeToken()
  //     resolve()
  //   })
  // },

  // dynamically modify permissions
  // async changeRoles({ commit, dispatch }, role) {
  //   const token = role + '-token'
  //
  //   commit('SET_TOKEN', token)
  //   setToken(token)
  //
  //   const { roles } = await dispatch('getInfo')
  //
  //   resetRouter()
  //
  //   // generate accessible routes map based on roles
  //   const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
  //   // dynamically add accessible routes
  //   router.addRoutes(accessRoutes)
  //
  //   // reset visited views and cached views
  //   dispatch('tagsView/delAllViews', null, { root: true })
  // }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
