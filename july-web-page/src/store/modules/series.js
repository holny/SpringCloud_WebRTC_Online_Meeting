import {createNewSeries,getMyAllSeries,getSeriesBySeriesId} from "@/api/series";
import { RESULT_CODE} from "@/utils/constant";

const state = {}

const mutations = {}

const actions = {
    newSeries ({ commit }, seriesInfo) {
        console.log('action - newSeries')
        console.log(seriesInfo)
        // const {account, password, rememberMe} = userInfo
        return new Promise((resolve, reject) => {
            createNewSeries(seriesInfo).then(response => {
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
    myAllSeries ({ commit }) {
        console.log('action - myAllSeries')
        return new Promise((resolve, reject) => {
            getMyAllSeries().then(response => {
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
    getSeries ({ commit },seriesId) {
        console.log('action - getSeries')
        console.log(seriesId)
        return new Promise((resolve, reject) => {
            getSeriesBySeriesId(seriesId).then(response => {
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
