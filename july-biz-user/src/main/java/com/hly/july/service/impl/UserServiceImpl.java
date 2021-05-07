package com.hly.july.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.mapper.UserMapper;
import com.hly.july.common.biz.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author MPG
 * @since 2021-04-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
