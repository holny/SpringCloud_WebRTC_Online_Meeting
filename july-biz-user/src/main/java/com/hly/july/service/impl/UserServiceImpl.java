package com.hly.july.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.mapper.UserMapper;
import com.hly.july.common.biz.service.IUserService;
import com.hly.july.common.constant.JulyConstants;
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

    public User getUserByAccount(String account){
        if(StringUtils.isNotBlank(account)) {
            if(JulyConstants.DEFAULT_LOGIN_BY_ACCOUNT.equals("phoneNumber")){
                return getUserByPhoneNumber(account);
            }else{
                return getUserByEmail(account);
            }
        }
        return null;
    }

    public User getUserByEmail(String email){
        if(StringUtils.isNotBlank(email)) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email);
            List<User> userList = baseMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(userList)) {
                if (userList.size() == 1) {
                    return userList.get(0);
                } else if (userList.size() > 1) {
                    log.warn("Duplicate email in User DB, user list:" + userList.toString());
                    return userList.get(0);
                }
            }
        }
        return null;
    }

    public User getUserByPhoneNumber(String phoneNumber){
        if(StringUtils.isNotBlank(phoneNumber)) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone_number", phoneNumber);
            List<User> userList = baseMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(userList)) {
                if (userList.size() == 1) {
                    return userList.get(0);
                } else if (userList.size() > 1) {
                    log.warn("Duplicate phone_number in User DB, user list:" + userList.toString());
                    return userList.get(0);
                }
            }
        }
        return null;
    }

}
