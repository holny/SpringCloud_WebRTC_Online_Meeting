import { login, register, validate, getUserInfo,searchUser,getRelation,upInsertRelation,removeRelation,upInsertRecentContact,removeRecentContact} from '@/api/user'
import { getToken, setToken, getRefreshToken, setRefreshToken, getHostId, setHostId, removeHostId,setHostInfo,getHostInfo,removeHostInfo, removeRefreshToken, removeToken } from '@/utils/auth'
import {RESULT_CODE} from "@/utils/constant";
const state = {
  hostToken: getToken(),
  hostRefreshToken: getRefreshToken(),
  hostId: getHostId(),
  hostInfo: getHostInfo(),
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
  SET_HOST_INFO: (state, hostInfo) => {
    state.hostInfo = hostInfo
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
    removeHostInfo()
    commit('SET_HOST_TOKEN', null)
    commit('SET_HOST_REFRESH_TOKEN', null)
    commit('SET_HOST_ID', null)
    commit('SET_HOST_INFO', null)
    return new Promise((resolve, reject) => {
      login({ email: account.trim(), password: password, rememberMe: rememberMe }).then(response => {
        const { data } = response
        console.log(data)
        if (data.code === RESULT_CODE.SUCCESS) {
          // commit('SET_HOST_TOKEN', data.data.access_token, { root: true })
          commit('SET_HOST_TOKEN', data.data.access_token)
          setToken(data.data.access_token)
          commit('SET_HOST_REFRESH_TOKEN', data.data.refresh_token)
          setRefreshToken(data.data.refresh_token)
          commit('SET_HOST_ID', data.data.userId)
          setHostId(data.data.userId)
          commit('SET_HOST_INFO', data.data.userInfo)
          setHostInfo(data.data.userInfo)
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
    removeHostInfo()
    commit('SET_HOST_TOKEN', null)
    commit('SET_HOST_REFRESH_TOKEN', null)
    commit('SET_HOST_ID', null)
    commit('SET_HOST_INFO', null)
    return new Promise((resolve, reject) => {
      register({userName: registerInfo.userName, email: registerInfo.email, phoneNumber: registerInfo.phoneNumber, password: registerInfo.confirmPassword, registerCode: registerInfo.registerCode}).then(response => {
        const { data } = response
        console.log(data)
        if (data.code ===  RESULT_CODE.SUCCESS) {
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
  // user logout
  logout({ commit}) {
    return new Promise(resolve => {
      removeToken()
      removeRefreshToken()
      removeHostId()
      removeHostInfo()
      commit('SET_HOST_TOKEN', null)
      commit('SET_HOST_REFRESH_TOKEN', null)
      commit('SET_HOST_ID', null)
      commit('SET_HOST_INFO', null)
      resolve()
    })
  },
  // user validate
  validate ({ commit }, userInfo) {
    console.log('action - duplicate')
    console.log(userInfo)
    removeToken()
    removeRefreshToken()
    removeHostId()
    removeHostInfo()
    commit('SET_HOST_TOKEN', null)
    commit('SET_HOST_REFRESH_TOKEN', null)
    commit('SET_HOST_ID', null)
    commit('SET_HOST_INFO', null)
    return new Promise((resolve, reject) => {
      validate(userInfo).then(response => {
        const { data } = response
        console.log(data)
        if (data.code ===  RESULT_CODE.SUCCESS) {
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
        if (data.code ===  RESULT_CODE.SUCCESS) {
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
        if (data.code ===  RESULT_CODE.SUCCESS) {
          resolve(data.data)
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  getUserRelation ({ commit },data) {
    commit
    console.log('action - getUserRelation')
    return new Promise((resolve, reject) => {
      getRelation(data.userId,data.peerId,data.category).then(response => {
        const { data } = response
        // console.log(data)
        if (data.code ===  RESULT_CODE.SUCCESS) {
          resolve(data.data)
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  upInsertUserRelation({ commit },payload) {
    commit
    console.log('action - upInsertUserRelation')
    return new Promise((resolve, reject) => {
      upInsertRelation(payload.userId,payload.category,payload.data).then(response => {
        const { data } = response
        // console.log(data)
        if (data.code ===  RESULT_CODE.SUCCESS) {
          resolve(data.data)
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  removeUserRelation ({ commit },payload) {
    commit
    console.log('action - removeUserRelation')
    return new Promise((resolve, reject) => {
      removeRelation(payload.userId,payload.peerId,payload.peerType,payload.relType).then(response => {
        const { data } = response
        // console.log(data)
        if (data.code ===  RESULT_CODE.SUCCESS) {
          resolve(data.data)
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  upInsertUserRecentContact({ commit },payload) {
    commit
    console.log('action - upInsertUserRecentContact')
    console.log(payload)
    return new Promise((resolve, reject) => {
      upInsertRecentContact(payload.userId,payload.data).then(response => {
        const { data } = response
        // console.log(data)
        if (data.code ===  RESULT_CODE.SUCCESS) {
          resolve(data.data)
        } else {
          reject(data.msg)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  removeUserRecentContact({ commit },payload) {
    commit
    console.log('action - removeUserRecentContact')
    console.log(payload)
    return new Promise((resolve, reject) => {
      removeRecentContact(payload.userId,payload.data).then(response => {
        const { data } = response
        // console.log(data)
        if (data.code ===  RESULT_CODE.SUCCESS) {
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
