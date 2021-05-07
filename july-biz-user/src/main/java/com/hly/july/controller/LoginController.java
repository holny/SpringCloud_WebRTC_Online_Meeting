package com.hly.july.controller;

import com.hly.july.common.result.Result;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Linyuan Hou
 * @date 2021/4/24 15:33
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;

    @PostMapping(value = "/{userId}")
    public Result<String> insertNewUser(@PathVariable String userId, @RequestBody User user){
        log.info("Insert a new User:"+user.toString());
        userService.save(user);
        return Result.success("success new user");
    }

    @GetMapping(value = "/{userId}")
    public Result<User> getUser(@PathVariable String userId){
        log.info("get a userId:"+userId);
        User user = userService.getById(userId);
        return Result.success("get success",user);
    }

}
