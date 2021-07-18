import request from '@/utils/request'

export function createNewSeries (data) {
    return request({
        url: '/api/series/',
        method: 'post',
        data
    })
}

export function getMyAllSeries () {
    return request({
        url: '/api/series/',
        method: 'get'
    })
}

export function getSeriesBySeriesId (seriesId) {
    return request({
        url: '/api/series/'+seriesId,
        method: 'get'
    })
}
