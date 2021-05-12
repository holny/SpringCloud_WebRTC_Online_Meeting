function localStorageSet(key, value) {
    localStorage.setItem(key, value);
}

function localStorageGet(key) {
    return localStorage.getItem(key);
}
//获取当前网址，
let URL_CURRENT_WWW_PATH = window.document.location.href;
//获取主机地址之后的目录，
let URL_PATH_NAME = window.document.location.pathname;
//获取主机地址，如： http://localhost:8083
let URL_HOST_PATH = URL_CURRENT_WWW_PATH.substring(0, URL_CURRENT_WWW_PATH.indexOf(URL_PATH_NAME));
//获取带"/"的项目名
let URL_PROJECT_PATH = URL_PATH_NAME.substring(0, URL_PATH_NAME.substr(1).indexOf('/') + 1);
let URL_HOST_PROJECT_PATH = URL_HOST_PATH+URL_PROJECT_PATH;
let API;
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
        CLIENT_SECRET:'anVseV9jbGllbnQ6aGx5NDMyMQ=='
    };
});

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