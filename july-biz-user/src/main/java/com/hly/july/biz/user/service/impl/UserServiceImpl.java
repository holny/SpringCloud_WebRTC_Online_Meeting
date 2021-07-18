package com.hly.july.biz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.db.entity.User;
import com.hly.july.common.db.mapper.UserMapper;
import com.hly.july.common.core.constant.JulyConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Boolean isDuplicateUser(User user){
        if(user!=null) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(user.getUserId() != null, "user_id", user.getUserId())
                    .or().eq(StringUtils.isNotBlank(user.getEmail()), "email", user.getEmail())
                    .or().like(StringUtils.isNotBlank(user.getPhoneNumber()), "phone_number", user.getPhoneNumber());
            List<User> userList = baseMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(userList)) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

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

    public List<User> getUserBySearch(String search,Integer maxNumber,List<Integer> userStatusList){
        if(StringUtils.isNotEmpty(search)&&maxNumber!=null) {
            search = StringEscapeUtils.escapeSql(search);
            search = search.trim();
            final String searchFinal = search;
//            String[] searchFragment = search.split(" ");
            List<User> userList = new ArrayList<>();
            int stillNeed = maxNumber;
            if (org.apache.commons.lang3.StringUtils.isNumeric(searchFinal)){
                QueryWrapper<User> userIdQueryWrapper = new QueryWrapper<>();
                userIdQueryWrapper.like("user_id",searchFinal).in("status",userStatusList).orderByDesc("exp").last("limit 3");
                List<User> userIdUserList =baseMapper.selectList(userIdQueryWrapper);
                log.info("getUserBySearch userIdUserList:{}",userIdUserList.toString());
                if(CollectionUtils.isNotEmpty(userIdUserList)){
                    userList.addAll(userIdUserList);
                    stillNeed = stillNeed- userIdUserList.size();
                }
            }
            QueryWrapper<User> userNameQueryWrapper = new QueryWrapper<>();
            userNameQueryWrapper.and(wrapper -> wrapper.like("user_name",searchFinal).or().like("nick_name",searchFinal)).in("status",userStatusList).orderByDesc("exp").last("limit "+stillNeed);;
            List<User> nameUserList =baseMapper.selectList(userNameQueryWrapper);
            log.info("getUserBySearch nameUserList:{}",nameUserList.toString());
            if(CollectionUtils.isNotEmpty(nameUserList)){
                userList.addAll(nameUserList);
            }
            if (userList.size()<=5&&search.contains(" ")){
                stillNeed = maxNumber - userList.size();
                String[] searchFragment = search.split(" ");
                if (searchFragment.length>0) {
                    QueryWrapper<User> fragQueryWrapper = new QueryWrapper<>();
                    boolean hasSearchedKey = false;
                    for (int i = 0; i < searchFragment.length; i++) {
                        final String fragSearch = searchFragment[i];
                        if (org.apache.commons.lang3.StringUtils.isNumeric(fragSearch) && !hasSearchedKey) {
                            fragQueryWrapper.like("user_id", fragSearch);
                            hasSearchedKey = true;
                        } else {
                            fragQueryWrapper.and(wrapper->wrapper.like("user_name", fragSearch).or().like("nick_name", fragSearch));
                        }

                    }
                    fragQueryWrapper.in("status",userStatusList).orderByDesc("exp").last("limit "+stillNeed);;
                }
                List<User> fragUserList =baseMapper.selectList(userNameQueryWrapper);
                log.info("getUserBySearch fragUserList:{}",fragUserList.toString());
                if(CollectionUtils.isNotEmpty(fragUserList)){
                    userList.addAll(fragUserList);
                }
            }
            return userList;

        }
        return null;
    }

    public User getUserByEmail(String email){
        if(StringUtils.isNotBlank(email)) {
            email = StringEscapeUtils.escapeSql(email);
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
            phoneNumber = StringEscapeUtils.escapeSql(phoneNumber);
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

    public int updateUserByUserId(String userId,User updateUser){
        if(StringUtils.isNumeric(userId)&&updateUser!=null) {
            updateUser.setUserId(userId);
            return baseMapper.updateById(updateUser);
        }
        return -1;
    }

    public User getUserListByUserIdAndStatus(String userId,List<Integer> statusList){
        if(StringUtils.isNotEmpty(userId)&&CollectionUtils.isNotEmpty(statusList)){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).in("status",statusList);
            List<User> userList = baseMapper.selectList(queryWrapper);
            if(userList.size()>0){
                return userList.get(0);
            }else{
                return null;
            }
        }
        return null;
    }

    public List<User> getUserListByUserIdsAndStatus(List<String> userIds,List<Integer> statusList){
        if(CollectionUtils.isNotEmpty(userIds)&&CollectionUtils.isNotEmpty(statusList)){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("user_id", userIds).in("status",statusList);
            List<User> userList = baseMapper.selectList(queryWrapper);
            return userList;
        }
        return null;
    }

}
