package com.hly.july.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.mapper.UserMapper;
import com.hly.july.common.biz.service.IUserService;
import com.hly.july.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Boolean isDuplicateUser(User user){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(user.getUserId() != null,"user_id", user.getUserId())
                .or().eq(StringUtils.isNotBlank(user.getEmail()),"email", user.getEmail())
                .or().like(StringUtils.isNotBlank(user.getPhoneNumber()),"phone_number", user.getPhoneNumber());
        List<User> userList = baseMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(userList)){
            return true;
        }else{
            return false;
        }
    }

}
