
//获取当前网址，
let URL_CURRENT_WWW_PATH = window.document.location.href;
//获取主机地址之后的目录，
let URL_PATH_NAME = window.document.location.pathname;
//获取主机地址，如： http://localhost:8083
let URL_HOST_PATH = URL_CURRENT_WWW_PATH.substring(0, URL_CURRENT_WWW_PATH.indexOf(URL_PATH_NAME));
//获取带"/"的项目名
let URL_PROJECT_PATH = URL_PATH_NAME.substring(0, URL_PATH_NAME.substr(1).indexOf('/') + 1);
let URL_HOST_PROJECT_PATH = URL_HOST_PATH+URL_PROJECT_PATH;
let TOKEN_HEADER_KEYWORD = 'Authorization';
let TOKEN_PREFIX_KEYWORD='Bearer ';
let API;
let PAGE;
$(function () {
    if (URL_PROJECT_PATH.indexOf("pages") != -1){
        URL_PROJECT_PATH = '';
        URL_HOST_PROJECT_PATH = URL_HOST_PATH+URL_PROJECT_PATH;
    }
    console.log(URL_CURRENT_WWW_PATH);
    console.log(URL_PATH_NAME);
    console.log(URL_HOST_PATH);
    console.log(URL_PROJECT_PATH);
    console.log(URL_HOST_PROJECT_PATH);
    API = {
        AUTH_TOKEN: URL_HOST_PROJECT_PATH+'/oauth/token',
        USER_LOGIN: URL_HOST_PROJECT_PATH+'/user/login',
        USER_REGISTER: URL_HOST_PROJECT_PATH+'/user/register',
        USER_VALIDATE: URL_HOST_PROJECT_PATH+'/user/validate',
        GET_USER: URL_HOST_PROJECT_PATH+'/user/',
        CLIENT_SECRET:'anVseV9jbGllbnQ6aGx5NDMyMQ=='
    };

    PAGE = {
        PAGE_MEETING: URL_HOST_PROJECT_PATH+'/pages/meeting.html',
    };
    initPage();
});

let ROLE_VISITOR = 'ROLE_VISITOR';
let ROLE_USER = 'ROLE_USER';
let ROLE_AUTHOR = 'ROLE_AUTHOR';
let ROLE_EXPERT = 'ROLE_EXPERT';
let ROLE_ADMIN = 'ROLE_ADMIN';
let ROLE_SUPER_ADMIN = 'ROLE_SUPER_ADMIN';

let RESOURCE_USER = 'RESOURCE_USER';
let RESOURCE_VIDEO = 'RESOURCE_VIDEO';
let RESOURCE_COMMENT = 'RESOURCE_COMMENT';
let RESOURCE_MEETING = 'RESOURCE_MEETING';

let ACTION_CREATE = 'CREATE';
let ACTION_UPDATE = 'UPDATE';
let ACTION_DELETE = 'DELETE';
let ACTION_VIEW = 'VIEW';




function initPage(){
    initHost();
}

let HOST_INFO;
let HOST_TOKEN_MAP;
function initHost(){
    console.log('initHost');
    try {
        let storageGet = localStorageGet(LS_KEY_AUTH_TOKEN);
        HOST_TOKEN_MAP = JSON.parse(storageGet);
        console.log(HOST_TOKEN_MAP);
    } catch(err) {
        console.log('get host token error');
        console.log(err);
        //处理错误
    }
    if(isNotEmpty(HOST_TOKEN_MAP)) {
        $.ajax({
            url: API.GET_USER + HOST_TOKEN_MAP.userId,
            type: 'GET',
            contentType: "application/json;charset=UTF-8",
            dataType: 'json',
            beforeSend: function (request) {
                if (isNotEmpty(HOST_TOKEN_MAP)) {
                    if (isNotEmpty(isNotEmpty(HOST_TOKEN_MAP.access_token))) {
                        request.setRequestHeader(TOKEN_HEADER_KEYWORD, TOKEN_PREFIX_KEYWORD + HOST_TOKEN_MAP.access_token);
                    } else {
                        console.log('HOST_TOKEN_MAP.access_token is empty');
                        console.log(HOST_TOKEN_MAP);
                    }
                } else {
                    console.log('HOST_TOKEN_MAP is empty');
                }
            },
            success: function (result) {
                console.log(result);
                if (result.code == 10001) {
                    console.log('login success');
                    initHostView(result.data);

                }else if (result.code == 20312) {       // Token 过期 error code，后端自定义的
                    console.log('token is expired');
                    localStorageDelete(LS_KEY_AUTH_TOKEN);
                } else {
                    console.log('login fail');
                }

            },
            error: function (result) {
                console.log('login error');
                console.log(result);
                console.log('login fail');
            }
        });
    }else{
        console.log('can not get any toke from localstorage, so no host info');
    }

}

function initHostView(host){
    let authority = host.authority;
    console.log(authority);
    if (authority.hasOwnProperty(RESOURCE_VIDEO)){
        let action_list = authority[RESOURCE_VIDEO];
        console.log(action_list);
        if(action_list.indexOf(ACTION_VIEW)){
            console.log("有权查看")
        }
    }
    $("#navbar-video-menu")
}

function lockForm(item){
    if(item.find("input").length>0) {
        item.find("input").attr("readonly", "readonly");
    }
    if(item.find("button").length>0) {
        item.find("button").attr("disabled", "disabled");
    }
    if(item.find("select").length>0) {
        item.find("select").attr("disabled", "disabled");
    }

}


function unLockForm(item){
    if(item.find("input").length>0) {
        item.find("input").removeAttr("readonly");
    }
    if(item.find("button").length>0) {
        item.find("button").removeAttr("disabled");
    }
    if(item.find("select").length>0) {
        item.find("select").removeAttr("disabled");
    }
    if(item.find(".captchaPic")>0) {
        item.find(".captchaPic").trigger("click");
    }
}

function buttonSpinnerStart(button){
    console.log('buttonSpinnerStart');
    console.log(button);
    console.log(button.text());
    if (button.length>0 && button.html().indexOf('spinner-border')<0) {
        button.append('...<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>');
        button.attr('disabled', 'disabled');
    }
}

function buttonSpinnerEnd(button){
    console.log('buttonSpinnerEnd');
    console.log(button.html());
    if (button.length>0 && button.html().indexOf('spinner-border')>=0) {
        button.find('.spinner-border').remove();
        let btn_txt = button.text().replace('...','');
        button.empty().append(btn_txt);
    }
}


// LocalStorage key- Oauth Token
let LS_KEY_AUTH_TOKEN = 'july_auth_token';
function localStorageSet(key, value) {
    localStorage.setItem(key, value);
}

function localStorageGet(key) {
    return localStorage.getItem(key);
}

function localStorageDelete(key) {
    return localStorage.removeItem(key);
}

function isNotEmpty(param){
    if(param){
        let param_type = typeof(param);
        if(param_type == 'object'){
            //要判断的是【对象】或【数组】或【null】等
            if(typeof(param.length) == 'undefined'){
                if(JSON.stringify(param) == "{}"){
                    return false;//空值，空对象
                }
            }else if(param.length == 0){
                return false;//空值，空数组
            }
        }else if(param_type == 'string'){
            //如果要过滤空格等字符
            var new_param = param.trim();
            if(new_param.length == 0){
                //空值，例如:带有空格的字符串" "。
                return false;
            }
        }else if(param_type == 'boolean'){
            // if(!param){
            //     return false;
            // }
            return true;
        }else if(param_type== 'number'){
            // if(!param){
            //     return false;
            // }
            return true;
        }
        return true;//非空值
    }else{
        //空值,例如：
        //(1)null
        //(2)可能使用了js的内置的名称，例如：var name=[],这个打印类型是字符串类型。
        //(3)空字符串''、""。
        //(4)数字0、00等，如果可以只输入0，则需要另外判断。
        return false;
    }
}