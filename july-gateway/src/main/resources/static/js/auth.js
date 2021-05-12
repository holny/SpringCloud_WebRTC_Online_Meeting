
$(function () {
    if($('#register_form').length>0){
        $('#register_form').find('input[name*=email]').change(function (){
            let user_data = {
                'email':$(this).val()
            };
            inputUserValidate(user_data,$(this),'邮箱地址已存在');
        });

        $('#register_form').find('input[name*=phoneNumber]').change(function (){
            let user_data = {
                'phoneNumber':$(this).val()
            };
            inputUserValidate(user_data,$(this),'手机号码已存在');
        });
    }

})



function inputFeedback(input,type,msg){
    console.log(input);
    if(type=='error') {
        let login_feed_back = input.parent().next();
        console.log(input);
        console.log(login_feed_back);
        login_feed_back.empty().append('<span class="fas fa-exclamation-circle mr-2">').append(msg);
        input.focus(function () {
            login_feed_back.empty();
            input.unbind("focus");
        });
        input.val("");
    }
    if(type=='warning') {
        let login_feed_back = input.parent().next();
        console.log(input);
        console.log(login_feed_back);
        login_feed_back.empty().append('<span class="fas fa-exclamation-circle mr-2">').append(msg);
        input.focus(function () {
            login_feed_back.empty();
            input.unbind("focus");
        });
    }

}

function inputUserValidate(user_data,input,msg){
    console.log(user_data);
    $.ajax({
        url: API.USER_VALIDATE,
        type: 'POST',
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(user_data),
        dataType: 'json',
        async: false,
        success: function (result) {
            console.log(result);
            if (result.code == 10001) {
                console.log('validate success');
            } else {
                console.log('validate fail');
                if (input.length>0){
                    inputFeedback(input,'warning',msg);
                }
            }
        },
        error: function (result) {
            console.log('validate error ');
        }
    });
}

function userLogin(form){
    lockForm(form);
    buttonSpinnerStart(form.find('button[type*=button]'));
    let loginAccount = form.find('input[name*=email]').val().trim();
    let password = form.find('input[type*=password]').val().trim();
    let remember = form.find('input[name*=rememberMe]').prop('checked');
    let auth = {
        'email':loginAccount,
        'password':password,
        'rememberMe':remember
    };
    console.log(auth);
    $.ajax({
        url: API.USER_LOGIN,
        type: 'POST',
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(auth),
        dataType : 'json',
        success: function (result) {
            console.log(result);
            if (result.code == 10001) {
                console.log('login success');
                console.log(result.data.access_token);
                localStorageSet(LS_KEY_AUTH_TOKEN,result.data);
            } else {
                console.log('login fail');
                inputFeedback(form.find('input[type*=password]'),'error',result.msg);
            }
            unLockForm(form);
            buttonSpinnerEnd(form.find('button[type*=button]'));
        },
        error: function (result) {
            console.log('login error');
            console.log(result);
            console.log('login fail');
            inputFeedback(form.find('input[type*=password]'),'error','网络错误');
            unLockForm(form);
            buttonSpinnerEnd(form.find('button[type*=button]'));
        }
    });
}

function userRegister(form){
    lockForm(form);
    buttonSpinnerStart(form.find('button[type*=button]'));
    let userName = form.find('input[name*=userName]').val().trim();
    let email = form.find('input[name*=email]').val().trim();
    let phoneNumber = form.find('input[name*=phoneNumber]').val().trim();
    let password1 = form.find('#registerPassword1_input').val().trim();
    let password2 = form.find('#registerPassword2_input').val().trim();
    let registerCode = form.find('input[name*=registerCode]').val().trim();
    let registerTerms = form.find('#registerTerms_checkbox').prop('checked');
    console.log(registerTerms);
    let auth = {
        'userName':userName,
        'email':email,
        'phoneNumber':phoneNumber,
        'password':password1,
        'registerCode':registerCode
    };
    console.log(auth);
    $.ajax({
        url: API.USER_REGISTER,
        type: 'POST',
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(auth),
        dataType : 'json',
        success: function (result) {
            console.log(result);
            if (result.code == 10001) {
                console.log('register success');
                // console.log(result.data.access_token);
                // localStorageSet(LS_KEY_AUTH_TOKEN,result.data);
            } else {
                console.log('register fail');
                inputFeedback(form.find('#registerCode_input'),'error',result.msg);
            }
            unLockForm(form);
            buttonSpinnerEnd(form.find('button[type*=button]'));
        },
        error: function (result) {
            console.log('register error');
            console.log(result);
            console.log('register fail');
            inputFeedback(form.find('#registerCode_input'),'error','网络错误');
            unLockForm(form);
            buttonSpinnerEnd(form.find('button[type*=button]'));
        }
    });
}