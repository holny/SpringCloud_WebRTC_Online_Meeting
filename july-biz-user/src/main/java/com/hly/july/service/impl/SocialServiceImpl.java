package com.hly.july.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.biz.entity.Social;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.mapper.SocialMapper;
import com.hly.july.common.biz.vo.SocialVO;
import com.hly.july.common.constant.UserConstants;
import com.hly.july.common.constant.UserStatusEnum;
import com.hly.july.common.exception.ServiceInternalException;
import com.hly.july.common.result.Result;
import com.hly.july.common.result.ResultCode;
import com.hly.july.common.util.DateUtils;
import com.hly.july.mapper.CustomSocialMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Linyuan Hou
 * @since 2021-06-12
 */
@Service
@Slf4j
public class  SocialServiceImpl extends ServiceImpl<SocialMapper, Social> implements IService<Social> {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CustomSocialMapper customSocialMapper;

    public List<Social> getUserSocialByUserIdAndType(String userId,String type){
        QueryWrapper<Social> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(userId!= null,"user_id", userId);
        if (StringUtils.isBlank(type)){
            queryWrapper.eq("social_type","all");
        }else if (type.toLowerCase().equals("recent")){
            queryWrapper.eq("social_type","recent");
        }else if (type.toLowerCase().equals("mark")){
            queryWrapper.eq("social_type","mark");
        }else if (type.toLowerCase().equals("group")){
            queryWrapper.eq("social_type","group");
        }else{
            queryWrapper.eq("social_type","all");
        }
        queryWrapper.orderByDesc("gmt_last_contact");
        List<Social> userList = baseMapper.selectList(queryWrapper);
        return userList;
    }

    public List<SocialVO> getUserSocialVOByUserIdAndType(String userId, String type){
        if(StringUtils.isNotEmpty(userId)){
            String ty;
            if (StringUtils.isBlank(type)){
                ty=null;
            }else if (type.toLowerCase().equals("recent")){
                ty="recent";
            }else if (type.toLowerCase().equals("mark")){
                ty="mark";
            }else if (type.toLowerCase().equals("group")){
                ty="group";
            }else{
                ty=null;
            }

            List<SocialVO> socialVOS = customSocialMapper.getSocialVOByUserIdAndType(userId,type);
            return socialVOS;
        }else{
            return null;
        }
    }

    public Boolean addUserSocialByIdAndType(String userId, String peerId, String type) throws ServiceInternalException{
        if (userId!=null && peerId!=null && StringUtils.isNotEmpty(type)){
            userId = StringEscapeUtils.escapeSql(userId);
            peerId = StringEscapeUtils.escapeSql(peerId);
            if("recent".equals(type.toLowerCase()) || "bookmark".equals(type.toLowerCase())){
                User peer = userService.getById(peerId);
                List<Integer> visibleUserStatusList = UserStatusEnum.getVisibleUserStatusCodeList();
                if (peer==null ||!visibleUserStatusList.contains(peer.getStatus())){
                    throw new ServiceInternalException("对方不存在");
                }
            }
            if("recent".equals(type.toLowerCase())){
                List<Social> recentSocialList = getUserSocialByUserIdAndType(userId,type);
                Social updateSocial = new Social();
                if(recentSocialList.size()>= UserConstants.USER_SOCIAL_RECENT_MAX_NUM){
                    Social needRemoved = recentSocialList.get(UserConstants.USER_SOCIAL_RECENT_MAX_NUM-1);
                    updateSocial.setSocialId(needRemoved.getSocialId());
                }else{
                    Long newSocialId= IdWorker.getId();
                    updateSocial.setSocialId(newSocialId.toString());
                }
                updateSocial.setSocialType("recent");
                updateSocial.setUserId(userId);
                updateSocial.setPeerId(peerId);
                updateSocial.setGmtCreate(DateUtils.getCurrentDateTime());
                updateSocial.setGmtLastContact(DateUtils.getCurrentDateTime());
                return super.saveOrUpdate(updateSocial);
            }else if("bookmark".equals(type.toLowerCase())){
                List<Social> recentSocialList = getUserSocialByUserIdAndType(userId,type);
                Social updateSocial = new Social();
                if(recentSocialList.size()>= UserConstants.USER_SOCIAL_BOOK_MARK_MAX_NUM){
                    throw new ServiceInternalException(ResultCode.USER_SOCIAL_OVER_MAX);
                }
                Long newSocialId= IdWorker.getId();
                updateSocial.setSocialId(newSocialId.toString());
                updateSocial.setSocialType("bookmark");
                updateSocial.setUserId(userId);
                updateSocial.setPeerId(peerId);
                updateSocial.setGmtCreate(DateUtils.getCurrentDateTime());
                updateSocial.setGmtLastContact(DateUtils.getCurrentDateTime());
                return super.saveOrUpdate(updateSocial);
            }else if("group".equals(type.toLowerCase())){
                List<Social> recentSocialList = getUserSocialByUserIdAndType(userId,type);
                Social updateSocial = new Social();
                if(recentSocialList.size()>= UserConstants.USER_SOCIAL_GROUP_MAX_NUM){
                    throw new ServiceInternalException(ResultCode.USER_SOCIAL_OVER_MAX);
                }
                Long newSocialId= IdWorker.getId();
                updateSocial.setSocialId(newSocialId.toString());
                updateSocial.setSocialType("group");
                updateSocial.setUserId(userId);
                updateSocial.setPeerId(peerId);
                updateSocial.setGmtCreate(DateUtils.getCurrentDateTime());
                updateSocial.setGmtLastContact(DateUtils.getCurrentDateTime());
                return super.saveOrUpdate(updateSocial);
            }
            Social userSocial = new Social();
            userSocial.setUserId(userId);
            userSocial.setPeerId(peerId);
            userSocial.setSocialType(type);
            userSocial.setGmtCreate(DateUtils.getCurrentDateTime());
            userSocial.setGmtLastContact(DateUtils.getCurrentDateTime());
            return super.saveOrUpdate(userSocial);
        }else{
            return null;
        }

    }

    public Boolean deleteUserSocialByIdAndType(String userId,String peerId,String type){
        if (userId!=null && peerId!=null){
            userId = StringEscapeUtils.escapeSql(userId);
            peerId = StringEscapeUtils.escapeSql(peerId);
            type = StringEscapeUtils.escapeSql(type);
            QueryWrapper<Social> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("peer_id",peerId);
            if(StringUtils.isNotEmpty(peerId)){
                queryWrapper.eq("social_type",type);
            }
            queryWrapper.eq("user_id", userId).eq("peer_id",peerId);
            return super.remove(queryWrapper);
        }else{
            return false;
        }

    }
}
