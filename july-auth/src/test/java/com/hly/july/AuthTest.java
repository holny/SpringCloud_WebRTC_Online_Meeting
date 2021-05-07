package com.hly.july;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Linyuan Hou
 * @date 2021/3/15 19:15
 */
@RunWith(SpringRunner.class)
public class AuthTest {
    @Test
    public void test1(){ //对原始密码加密
        String hashpw = BCrypt.hashpw("hly4321", BCrypt.gensalt()); System.out.println(hashpw); //校验原始密码和BCrypt密码是否一致
        boolean checkpw = BCrypt.checkpw("hly4321",
                hashpw);
        System.out.println(checkpw);
    }

}
