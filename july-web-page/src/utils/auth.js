import {localStorageGet, localStorageSet, localStorageDelete} from '@/utils/storage'

const TokenKey = 'July-Token'
const RefreshTokenKey = 'July-RefreshToken'
const HostIdKey = 'July-HostId'

export function getToken () {
  let token = localStorageGet(TokenKey)
  return token
}

export function setToken (token) {
  return localStorageSet(TokenKey, token)
}

export function removeToken () {
  return localStorageDelete(TokenKey)
}

export function getRefreshToken () {
  return localStorageGet(RefreshTokenKey)
}

export function setRefreshToken (token) {
  return localStorageSet(RefreshTokenKey, token)
}

export function removeRefreshToken () {
  return localStorageDelete(RefreshTokenKey)
}

export function getHostId () {
  return localStorageGet(HostIdKey)
}

export function setHostId (id) {
  return localStorageSet(HostIdKey, id)
}

export function removeHostId () {
  return localStorageDelete(HostIdKey)
}
