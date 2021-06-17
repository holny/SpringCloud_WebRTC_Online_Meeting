package com.hly.july.common.biz.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author Linyuan Hou
 * @date 2021/5/12 21:09
 */
public class EncryptUtils {
    public static String PasswordEncryptByBCrypt(String password){
        String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());  //校验原始密码和BCrypt密码是否一致
//        boolean checkpw = BCrypt.checkpw(password,
//                hashpw);
        return hashpw;
    }
}
