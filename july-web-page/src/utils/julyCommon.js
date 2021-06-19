import {getToken} from "@/utils/auth";
import { Notify } from 'quasar'

export let JULY = {
    WEBSOCKET_URI_ENDPOINT:'http://localhost:80/meeting/endpointWS?Authorization=' + getToken(),
    WEBSOCKET_URI_SEND_HEARTBEAT:'/app/heartbeat',
    WEBSOCKET_URI_SUBSCRIBE_HEARTBEAT:'/user/topic/heartbeat',
    WEBSOCKET_URI_SEND_HEARTBEAT_INTERVAL: 50000,
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
    convertPrintGender:convertPrintGender
}

function notify(message,level,position){
    Notify.create({
        type: level,
        message: message,
        position: position,
        timeout: 5000
    })

}

function filterPrintRole (roleArray) {
    let role = "普通用户"
    for(let index in roleArray){
        if(roleArray[index]==='ROLE_VISITOR'){
            role = '游客(Visitor)'
        }else if(roleArray[index]==='ROLE_USER'){
            role = '普通用户(User)'
        }else if(roleArray[index]==='ROLE_AUTHOR'){
            role = 'Up主(Author)'
        }else if(roleArray[index]==='ROLE_EXPERT'){
            role = '专家(Expert)'
        }else if(roleArray[index]==='ROLE_ADMIN'){
            role = '管理员(Admin)'
        }else if(roleArray[index]==='ROLE_SUPER_ADMIN'){
            role = '超级管理员(SuperAdmin)'
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
    return printGender
}

