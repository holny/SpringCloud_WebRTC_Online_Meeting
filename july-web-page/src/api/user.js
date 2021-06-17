import request from '@/utils/request'

export function login (data) {
  return request({
    url: '/api/user/login',
    method: 'post',
    data
  })
}

export function register (data) {
  return request({
    url: '/api/user/register',
    method: 'post',
    data
  })
}

export function validate (data) {
  return request({
    url: '/api/user/duplicate',
    method: 'post',
    data
  })
}

export function getUserInfo (userId) {
  return request({
    url: '/api/user/' + userId,
    method: 'get'
    // params: { data }
  })
}

export function logout () {
  return request({
    url: '/api/user/logout',
    method: 'post'
  })
}

export function searchUser (search) {
  return request({
    url: '/api/user/search',
    method: 'get',
    params: { search:search }
  })
}

export function getRelation (userId,peerId,category) {
  return request({
    url: '/api/relation/'+userId,
    method: 'get',
    params: { peerId:peerId,category:category }
  })
}
export function upInsertRelation (userId,category,data) {
  return request({
    url: '/api/relation/'+userId,
    method: 'post',
    params: { category:category },
    data
  })
}

export function removeRelation (userId,peerId,peerType) {
  return request({
    url: '/api/relation/'+userId,
    method: 'delete',
    params: { peerId:peerId,peerType:peerType }
  })
}

export function upInsertRecentContact (userId,recentData) {
  console.log("upInsertRecentContact")
  console.log(recentData)
  return request({
    url: '/api/relation/'+userId+'/recent',
    method: 'post',
    data:recentData
  })
}

export function removeRecentContact (userId,recentData) {
  console.log("removeRecentContact")
  console.log(recentData)
  return request({
    url: '/api/relation/'+userId+'/recent',
    method: 'delete',
    data:recentData
  })
}
