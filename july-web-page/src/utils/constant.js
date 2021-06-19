
export let CONSTANT = {
    CONTAINER_GROUP : "group",
    CONTAINER_PERSON : "person",
    WS_METHOD_PERSONAL : "personal",
    WS_METHOD_BROADCAST : "broadcast",
    SHOUTING_MESSAGE : "message",
    SHOUTING_EVENT : "event",
    CONTACTS_CATEGORY_RECENT: "recent",
    CONTACTS_CATEGORY_BOOKMARK: "bookmark",
    CONTACTS_CATEGORY_GROUP: "group",

    CHAT_WATCH_ACTION_ENTRY:"entry",
    CHAT_WATCH_ACTION_LEAVE:"leave",

    RELATION_TYPE_FRIEND:'friend', //1
    RELATION_TYPE_GROUP:'group', //1
    DATE_FORMAT:'YYYY-MM-DD HH:mm:ss'
}

export let EVENT_CODE = {
    NEW_JOINER : 'E1001',// E1001 有新人进入事件
    RECENT_CHANGED: 'E1005',// E1005 最近联系人列表改变
    UNREAD_CHANGED: 'E1006' // E1006 未读消息数改变
}

export let RESULT_CODE = {
    SUCCESS : 10001,// 成功返回
}
