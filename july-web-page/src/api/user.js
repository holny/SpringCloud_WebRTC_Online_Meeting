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

export function getSocial (userId) {
  return request({
    url: '/api/social/'+userId,
    method: 'get'
  })
}
export function addSocial (userId,peerId,type) {
  return request({
    url: '/api/social/'+userId,
    method: 'post',
    params: { peerId:peerId,type:type }
  })
}

export function removeSocial (userId,peerId,type) {
  return request({
    url: '/api/social/'+userId,
    method: 'delete',
    params: { peerId:peerId,type:type }
  })
}
