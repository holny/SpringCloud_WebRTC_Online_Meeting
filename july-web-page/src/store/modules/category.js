import {createNewCategory,getAllMainCategory,getAllSubCategory,getMainCategoryByType,removeCategoryByTagId} from "@/api/category";
import { RESULT_CODE} from "@/utils/constant";

const state = {}

const mutations = {}

const actions = {
    newCategory ({ commit }, category) {
        console.log('action - newCategory')
        console.log(category)
        return new Promise((resolve, reject) => {
            createNewCategory(category).then(response => {
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
    getMainCategoryByType ({ commit },categoryType) {
        console.log('action - getMainCategoryByType')
        console.log(categoryType)
        return new Promise((resolve, reject) => {
            getMainCategoryByType(categoryType).then(response => {
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
    getAllMainCategory ({ commit }) {
        console.log('action - getAllMainCategory')
        return new Promise((resolve, reject) => {
            getAllMainCategory().then(response => {
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

    getAllSubCategory ({ commit },mainCategoryId) {
        console.log('action - getAllSubCategory')
        console.log(mainCategoryId)
        return new Promise((resolve, reject) => {
            getAllSubCategory(mainCategoryId).then(response => {
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
    deleteCategoryByIdAndLevel ({ commit },data) {
        console.log('action - deleteCategoryByIdAndLevel')
        console.log(data.categoryId)
        console.log(data.level)
        return new Promise((resolve, reject) => {
            removeCategoryByTagId(data.categoryId,data.level).then(response => {
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
