import request from '@/utils/request'

export function createNewVideo (data) {
    return request({
        url: '/api/video/',
        method: 'post',
        data: data
    })
}
export function getVideoInfoListBySearch (search) {
    return request({
        url: '/api/video/search',
        method: 'post',
        data:search
    })
}

export function getVideoInfoById (videoId) {
    return request({
        url: '/api/video/'+videoId,
        method: 'get',
    })
}
