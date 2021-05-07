package com.hly.july.auth.controller;

import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.service.IUserService;
import com.hly.july.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Linyuan Hou
 * @date 2021/5/6 17:26
 */
@Slf4j
@RestController
public class UserAuthController {

    @Autowired
    private IUserService userService;

    @GetMapping(value = "/{userId}")
    public Result<User> getUser(@PathVariable String userId){
        log.info("get a userId:"+userId);
        User user = userService.getById(userId);
        return Result.success("get success",user);
    }

}
