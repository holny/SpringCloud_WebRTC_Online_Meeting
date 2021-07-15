package com.hly.july.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.db.entity.User;
import com.hly.july.common.db.mapper.UserMapper;
import com.hly.july.common.core.constant.JulyConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
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
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IService<User> {

    public User getUserByAccount(String account){
        if(StringUtils.isNotBlank(account)) {
            account = StringEscapeUtils.escapeSql(account);
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
