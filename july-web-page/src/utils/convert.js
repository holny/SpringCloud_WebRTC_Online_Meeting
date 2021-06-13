
export function filterPrintRole (roleArray) {
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

export function convertPrintGender (gender) {
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
