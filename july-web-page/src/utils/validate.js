/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal (path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL (url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase (str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase (str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets (str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail (email) {
  // const reg = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@(([[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  const reg = /^([a-z0-9_.-]+)@([\da-z.-]+)\.([a-z.]{2,6})$/
  return reg.test(email)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString (str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray (arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}

/* 是否手机号码 */
export function validatePhone (value) {
  const reg = /^[1][3,4,5,7,8][0-9]{9}$/
  // const reg =/^1[3456789]\d{9}$/;
  if (value === '' || value === undefined || value === null) {
    return false
  } else {
    if ((!reg.test(value)) && value !== '') {
      return false
    } else {
      return true
    }
  }
}

/* 验证内容是否英文数字以及下划线 */
export function isPassword (value) {
  const reg = /^[_a-zA-Z0-9]+$/
  if (value === '' || value === undefined || value === null) {
    return false
  } else {
    if (!reg.test(value)) {
      return false
    } else {
      return true
    }
  }
}

/* 验证内容是否纯数字 */
export function isNumerical (value) {
  const reg = /^[0-9]*$/
  if (value === '' || value === undefined || value === null) {
    return false
  } else {
    if (!reg.test(value)) {
      return false
    } else {
      return true
    }
  }
}

/* 验证内容是否英文数字以及下划线 */
export function validateUsername (value) {
  const reg = /(^([a-zA-Z0-9_.-]+){4,10}$)|(^([\u4e00-\u9fa5.]+){2,10}$)/
  if (value === '' || value === undefined || value === null) {
    return false
  } else {
    if (!reg.test(value)) {
      return false
    } else {
      return true
    }
  }
}

/* 验证内容是否中文或者英文等无特殊字符组成 */
export function validateText (value) {
  const reg = /(^(([a-zA-Z0-9_.-]+)|([\u2E80-\u9FFF]+)|([%&'\s,;[\]+=?$\x22]+)){2,10}$)/
  if (value === '' || value === undefined || value === null) {
    return false
  } else {
    if (!reg.test(value)) {
      return false
    } else {
      return true
    }
  }
}

export function isNotEmpty (param) {
  if (param) {
    let param_type = typeof (param)
    if (param_type === 'object') {
      // 要判断的是【对象】或【数组】或【null】等
      if (typeof (param.length) === 'undefined') {
        if (JSON.stringify(param) === '{}') {
          return false// 空值，空对象
        }
      } else if (param.length === 0) {
        return false// 空值，空数组
      }
    } else if (param_type === 'string') {
      // 如果要过滤空格等字符
      var new_param = param.trim()
      if (new_param.length === 0) {
        // 空值，例如:带有空格的字符串" "。
        return false
      }
    } else if (param_type === 'boolean') {
      // if(!param){
      //     return false;
      // }
      return true
    } else if (param_type === 'number') {
      // if(!param){
      //     return false;
      // }
      return true
    }
    return true// 非空值
  } else {
    // 空值,例如：
    // (1)null
    // (2)可能使用了js的内置的名称，例如：var name=[],这个打印类型是字符串类型。
    // (3)空字符串''、""。
    // (4)数字0、00等，如果可以只输入0，则需要另外判断。
    return false
  }
}
