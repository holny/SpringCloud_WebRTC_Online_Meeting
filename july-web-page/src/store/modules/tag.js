import {createNewTag,getTagByTagId,getInvisibleTagByType,getAllInvisibleTag,removeTagByTagId} from "@/api/tag";
import { RESULT_CODE} from "@/utils/constant";

const state = {}

const mutations = {}

const actions = {
    newTag ({ commit }, tagInfo) {
        console.log('action - newTag')
        console.log(tagInfo)
        return new Promise((resolve, reject) => {
            createNewTag(tagInfo).then(response => {
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
    getAllTag ({ commit }) {
        console.log('action - getAllTag')
        return new Promise((resolve, reject) => {
            getAllInvisibleTag().then(response => {
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
    getAllTagByType ({ commit },tagType) {
        console.log('action - getAllTagByType')
        return new Promise((resolve, reject) => {
            getInvisibleTagByType(tagType).then(response => {
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
    getTagByTagId ({ commit },tagId) {
        console.log('action - getTagByTagId')
        console.log(tagId)
        return new Promise((resolve, reject) => {
            getTagByTagId(tagId).then(response => {
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
    deleteTagByTagId ({ commit },tagId) {
        console.log('action - deleteTagByTagId')
        console.log(tagId)
        return new Promise((resolve, reject) => {
            removeTagByTagId(tagId).then(response => {
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
