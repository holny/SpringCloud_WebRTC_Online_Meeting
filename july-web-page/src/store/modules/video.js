import {createNewVideo,getVideoInfoById,getVideoInfoListBySearch} from "@/api/video";
import { RESULT_CODE} from "@/utils/constant";

const state = {}

const mutations = {}

const actions = {
    newVideo ({ commit }, videoInfo) {
        console.log('action - newVideo')
        console.log(videoInfo)
        // const {account, password, rememberMe} = userInfo
        return new Promise((resolve, reject) => {
            createNewVideo(videoInfo).then(response => {
                commit
                const { data } = response
                console.log(data)
                if (data.code === RESULT_CODE.SUCCESS) {
                    resolve(data.data)
                } else {
                    reject(data.msg)
                }
            }).catch(error => {
                reject(error)
            })
        })
    },
    getVideoInfoById ({ commit }, videoId) {
        console.log('action - getVideoInfoById')
        console.log(videoId)
        // const {account, password, rememberMe} = userInfo
        return new Promise((resolve, reject) => {
            getVideoInfoById(videoId).then(response => {
                commit
                const { data } = response
                console.log(data)
                if (data.code === RESULT_CODE.SUCCESS) {
                    resolve(data.data)
                } else {
                    reject(data.msg)
                }
            }).catch(error => {
                reject(error)
            })
        })
    },
    getVideoInfosBySearch ({ commit }, search) {
        console.log('action - getVideoInfoListBySearch')
        console.log(search)
        // const {account, password, rememberMe} = userInfo
        return new Promise((resolve, reject) => {
            getVideoInfoListBySearch(search).then(response => {
                commit
                const { data } = response
                console.log(data)
                if (data.code === RESULT_CODE.SUCCESS) {
                    resolve(data.data)
                } else {
                    reject(data.msg)
                }
            }).catch(error => {
                reject(error)
            })
        })
    },
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
