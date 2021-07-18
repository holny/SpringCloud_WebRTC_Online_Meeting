import request from '@/utils/request'

export function createNewTag (data) {
    return request({
        url: '/api/tag/',
        method: 'post',
        data
    })
}

export function getInvisibleTagByType (tagType) {
    return request({
        url: '/api/tag/',
        method: 'get',
        params: { type:tagType }
    })
}

export function getAllInvisibleTag () {
    return request({
        url: '/api/tag/',
        method: 'get'
    })
}

export function getTagByTagId (tagId) {
    return request({
        url: '/api/tag/'+tagId,
        method: 'get'
    })
}

export function removeTagByTagId (tagId) {
    return request({
        url: '/api/tag/'+tagId,
        method: 'delete'
    })
}
