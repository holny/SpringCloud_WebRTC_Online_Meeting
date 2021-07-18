import {getToken, getHostInfo} from "@/utils/auth";
import { Notify } from 'quasar'
import {isNotEmpty} from "@/utils/validate";

export let JULY = {
    WEBSOCKET_URI_ENDPOINT:'http://localhost:80/meeting/endpointWS?Authorization=',
    WEBSOCKET_URI_SEND_HEARTBEAT:'/app/heartbeat',
    WEBSOCKET_URI_SUBSCRIBE_HEARTBEAT:'/user/topic/heartbeat',
    WEBSOCKET_URI_SEND_HEARTBEAT_INTERVAL: 5000,
    WEBSOCKET_URI_GET_ALL_UNREAD_COUNT_INTERVAL: 2000,
    WEBSOCKET_HEADERS: {'Authorization': 'Bearer ' + getToken()},
    CHAT_WATCH_SEND_INTERVAL: 5000,
}
export let FUN = {
    NOTIFY_LEVEL_ERROR :'negative',
    NOTIFY_LEVEL_WARNING : 'warning',
    NOTIFY_LEVEL_INFO : 'positive',
    NOTIFY_POSITION_TOP :'top',
    notify:notify,
    filterPrintRole:filterPrintRole,
    convertPrintGender:convertPrintGender,
    convertUserStatus:convertUserStatus,
    getFormatHostInfo:getFormatHostInfo,
    deepCopy:deepCopy,
}

function notify(message,level,position){
    Notify.create({
        type: level,
        message: message,
        position: position,
        timeout: 10000,
        progress: true,
        classes: 'glossy',
        closeBtn: true
    })

}

function filterPrintRole (roleArray) {
    let role = "普通用户"
    for(let index in roleArray){
        if(roleArray[index]==='ROLE_VISITOR'){
            role = '游客'
        }else if(roleArray[index]==='ROLE_USER'){
            role = '普通用户'
        }else if(roleArray[index]==='ROLE_AUTHOR'){
            role = 'Up主'
        }else if(roleArray[index]==='ROLE_EXPERT'){
            role = '专家'
        }else if(roleArray[index]==='ROLE_ADMIN'){
            role = '管理员'
        }else if(roleArray[index]==='ROLE_SUPER_ADMIN'){
            role = '超级管理员'
        }
    }
    return role
}

function convertPrintGender (gender) {
    let printGender = "普通用户"
    if(gender==='male'){
        printGender = '男'
    }else if(gender==='female'){
        printGender = '女'
    }else {
        printGender = gender
    }
    console.log(printGender)
    return printGender
}

function convertUserStatus (status) {
    let result = "- 未知 -"
    if(status===0){
        result = '已删除'
    }else if(status===1){
        result = '正常'
    }else if(status===2){
        result = '被禁止'
    }else if(status===3){
        result = '被锁定'
    }else if(status===4){
        result = '过期'
    }
    return result
}

function deepCopy (object) {
    return JSON.parse(JSON.stringify(object)) //深度复制
}


async function getFormatHostInfo(_that){
    let hostInfo = getHostInfo()
    let result=null
    if(isNotEmpty(hostInfo)){
        await _that.$store.dispatch('user/getHostInfo', hostInfo.userId)
            .then((data) => {
                console.log('got host info successful')
                result = data
            })
            .catch((msg) => {
                FUN.notify(msg,FUN.NOTIFY_LEVEL_ERROR,FUN.NOTIFY_POSITION_TOP)
                result = null
            })
    }
    return result
}

