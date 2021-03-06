import {localStorageGet, localStorageSet, localStorageDelete} from '@/utils/storage'

const TokenKey = 'July-Token'
const RefreshTokenKey = 'July-RefreshToken'
const HostInfoKey = 'July-HostInfo'
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

export function getHostInfo () {
  return JSON.parse(localStorageGet(HostInfoKey))
}

export function setHostInfo (hostInfo) {
  return localStorageSet(HostInfoKey, JSON.stringify(hostInfo))
}

export function removeHostInfo () {
  return localStorageDelete(HostInfoKey)
}
