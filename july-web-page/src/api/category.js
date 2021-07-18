import request from '@/utils/request'

export function createNewCategory (data) {
    return request({
        url: '/api/category/',
        method: 'post',
        data
    })
}

export function getMainCategoryByType (categoryType) {
    return request({
        url: '/api/category/',
        method: 'get',
        params: { type:categoryType }
    })
}

export function getAllMainCategory () {
    return request({
        url: '/api/category/',
        method: 'get'
    })
}

export function getAllSubCategory (mainCategoryId) {
    return request({
        url: '/api/category/'+mainCategoryId,
        method: 'get'
    })
}

export function removeCategoryByTagId (categoryId,level) {
    return request({
        url: '/api/category/'+categoryId,
        method: 'delete',
        params: { level:level }
    })
}
