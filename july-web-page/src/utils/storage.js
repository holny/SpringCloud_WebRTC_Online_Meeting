import { SessionStorage, Cookies } from 'quasar'

// 存储客户端信息，无需请求服务器。数据永久保存，除非用户手动清理客户端缓存。5M左右，
export function localStorageSet (key, value) {
  return localStorage.setItem(key, value)
}

export function localStorageGet (key) {
  return localStorage.getItem(key)
}

export function localStorageDelete (key) {
  return localStorage.removeItem(key)
}

// 存储用户信息，获取数据需要与服务器建立连接。可存储的数据有限，且依赖于服务器，无需请求服务器的数据尽量不要存放在cookie中，以免影响页面性能。
export function CookieSet (key, value) {
  return Cookies.set(key, value)
}

export function CookieGet (key) {
  return Cookies.get(key)
}

export function CookieDelete (key) {
  return Cookies.remove(key)
}

// 存储客户端信息，无需请求服务器。据保存在当前会话，刷新页面数据不会被清除，结束会话（关闭浏览器、关闭页面、跳转页面）数据失效。
export function sessionStorageSet (key, value) {
  return SessionStorage.set(key, value)
}

export function sessionStorageGet (key) {
  return SessionStorage.getItem(key)
}

export function sessionStorageDelete (key) {
  return SessionStorage.remove(key)
}
