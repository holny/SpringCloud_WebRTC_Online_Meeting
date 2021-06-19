package com.hly.july.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.biz.constant.RelationTypeEnum;
import com.hly.july.common.biz.constant.UserStatusEnum;
import com.hly.july.common.biz.entity.Relation;
import com.hly.july.common.biz.entity.User;
import com.hly.july.common.biz.mapper.RelationMapper;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.common.biz.utils.RedisUtils;
import com.hly.july.common.biz.vo.RecentVO;
import com.hly.july.common.biz.vo.RelationVO;
import com.hly.july.common.biz.constant.ContainerEnum;
import com.hly.july.common.biz.constant.UserConstants;
import com.hly.july.common.biz.exception.ServiceInternalException;
import com.hly.july.common.biz.util.DateUtils;
import com.hly.july.common.biz.util.JulyAuthorityUtils;
import com.hly.july.mapper.CustomRelationMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类, 前端传来category: recent、bookmark、group，对应的数据库type: person,person,group。 recent由redis中USER_SOCIAL_RECENT确定
 * </p>
 *
 * @author Linyuan Hou
 * @since 2021-06-12
 */
@Service
@Slf4j
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation> implements IService<Relation> {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CustomRelationMapper customRelationMapper;

    @Resource
    private RedisUtils redisUtils;

    private String USER_RELATION_RECENT = "user_relation_recent_";
    private String REDIS_UNREAD_COUNT = "chat_unread_count_";

    public List<RelationVO> getUserRelation(String userId, String peerId, String category,Integer relType) throws ServiceInternalException{
        log.info("getUserRelation userId:{},peerId:{},category:{}",userId,peerId,category);
        if(StringUtils.isNotEmpty(userId)){
            if(StringUtils.isNotEmpty(peerId)){
                Integer type=null;
                if (StringUtils.isEmpty(category)){
                    type=null;
                }else if (category.toLowerCase().equals("bookmark")||category.toLowerCase().equals("recent")){
                    type = ContainerEnum.PERSON.getCode();
                }else if (category.toLowerCase().equals("group")){
                    type=ContainerEnum.GROUP.getCode();
                }
                List<RelationVO> relationVOS = customRelationMapper.getRelationVOByUserIdAndPeerIdAndType(userId,peerId,type, relType);
                if (relationVOS==null||relationVOS.size()==0){
                    List<Integer> visibleStatusList = UserStatusEnum.getAllUserStatusCodeList();
                    User peer = userService.getUserListByUserIdAndStatus(peerId,visibleStatusList);
                    if(peer!=null){
                        RelationVO relationVO = RelationVO.build(userId,peer);
                        relationVO.setRelTypeCode(RelationTypeEnum.TEMP.getCode());
                        relationVO.setRelType(RelationTypeEnum.TEMP.getDesc());
                        relationVO.setCategory(category);
                        relationVOS = new ArrayList<>();
                        relationVOS.add(relationVO);
                    }else{
                        throw new ServiceInternalException("不存在此聊天对象");
                    }
                }

                relationVOS.forEach(relationVO -> {
                    if(ContainerEnum.PERSON.getCode()==relationVO.getPeerTypeCode()){
                        relationVO.setCategory("bookmark");
                    }else if(ContainerEnum.GROUP.getCode()==relationVO.getPeerTypeCode()){
                        relationVO.setCategory("group");
                    }
                    relationVO.setPeerRole(JulyAuthorityUtils.roleClassifyString2Set(relationVO.getPeerRawRole()));
                    relationVO.setPeerAuthority(JulyAuthorityUtils.authorityClassifyString2Map(relationVO.getPeerRawAuthority()));
                    relationVO.setPeerType(ContainerEnum.getDescByCode(Integer.valueOf(relationVO.getPeerTypeCode())));
                    relationVO.setRelType(RelationTypeEnum.getDescByCode(Integer.valueOf(relationVO.getRelTypeCode())));
                });
                return relationVOS;
            }else{
                if (StringUtils.isBlank(category)){
                    List<RelationVO> relationVOS = customRelationMapper.getRelationVOByUserIdAndPeerIdAndType(userId,null,null,relType);
                    for (RelationVO relationVO : relationVOS) {
                        if(ContainerEnum.PERSON.getCode()==relationVO.getPeerTypeCode()){
                            relationVO.setCategory("bookmark");
                        }else if(ContainerEnum.GROUP.getCode()==relationVO.getPeerTypeCode()){
                            relationVO.setCategory("group");
                        }
                        relationVO.setPeerRole(JulyAuthorityUtils.roleClassifyString2Set(relationVO.getPeerRawRole()));
                        relationVO.setPeerAuthority(JulyAuthorityUtils.authorityClassifyString2Map(relationVO.getPeerRawAuthority()));
                        relationVO.setPeerType(ContainerEnum.getDescByCode(Integer.valueOf(relationVO.getPeerTypeCode())));
                        relationVO.setRelType(RelationTypeEnum.getDescByCode(Integer.valueOf(relationVO.getRelTypeCode())));
                    }
                    List<RelationVO> recentRelVOs = getUserRecentContactRelationVO(userId);
                    if(recentRelVOs!=null){
                        relationVOS.addAll(recentRelVOs);
                    }
                    return relationVOS;
                }else if (category.toLowerCase().equals("recent")){
                    List<RelationVO> recentRelVOs = getUserRecentContactRelationVO(userId);
                    return recentRelVOs;
                }else if (category.toLowerCase().equals("bookmark")){
                    List<RelationVO> relationVOS = customRelationMapper.getRelationVOByUserIdAndPeerIdAndType(userId,null,ContainerEnum.PERSON.getCode(),relType);
                    relationVOS.forEach(relationVO -> {
                        relationVO.setCategory(category.toLowerCase());
                        relationVO.setPeerRole(JulyAuthorityUtils.roleClassifyString2Set(relationVO.getPeerRawRole()));
                        relationVO.setPeerAuthority(JulyAuthorityUtils.authorityClassifyString2Map(relationVO.getPeerRawAuthority()));
                        relationVO.setPeerType(ContainerEnum.getDescByCode(Integer.valueOf(relationVO.getPeerTypeCode())));
                        relationVO.setRelType(RelationTypeEnum.getDescByCode(Integer.valueOf(relationVO.getRelTypeCode())));
                    });
                    return relationVOS;
                }else if (category.toLowerCase().equals("group")){
                    List<RelationVO> relationVOS = customRelationMapper.getRelationVOByUserIdAndPeerIdAndType(userId,null,ContainerEnum.GROUP.getCode(),relType);
                    relationVOS.forEach(relationVO -> {
                        relationVO.setCategory(category.toLowerCase());
                        relationVO.setPeerRole(JulyAuthorityUtils.roleClassifyString2Set(relationVO.getPeerRawRole()));
                        relationVO.setPeerAuthority(JulyAuthorityUtils.authorityClassifyString2Map(relationVO.getPeerRawAuthority()));
                        relationVO.setPeerType(ContainerEnum.getDescByCode(Integer.valueOf(relationVO.getPeerTypeCode())));
                        relationVO.setRelType(RelationTypeEnum.getDescByCode(Integer.valueOf(relationVO.getRelTypeCode())));
                    });
                    return relationVOS;
                }

                return null;
            }
        }else{
            return null;
        }
    }

    public List<RelationVO> getUserRelationByIdAndType(String userId, String peerId, Integer peerType,List<Integer> relType) throws ServiceInternalException{
        log.info("getUserRelation userId:{},peerId:{},peerType:{},relType:{}",userId,peerId,peerType,relType);
        if(StringUtils.isNotEmpty(userId)){
            List<RelationVO> relationVOS = customRelationMapper.getRelationVOByUserIdAndPeerIdAndTypeList(userId,peerId,peerType,relType);
            relationVOS.forEach(relationVO -> {
                relationVO.setPeerRole(JulyAuthorityUtils.roleClassifyString2Set(relationVO.getPeerRawRole()));
                relationVO.setPeerAuthority(JulyAuthorityUtils.authorityClassifyString2Map(relationVO.getPeerRawAuthority()));
                relationVO.setPeerType(ContainerEnum.getDescByCode(Integer.valueOf(relationVO.getPeerTypeCode())));
                relationVO.setRelType(RelationTypeEnum.getDescByCode(Integer.valueOf(relationVO.getRelTypeCode())));
            });
            return relationVOS;
        }else{
            return null;
        }
    }

    public Integer getUnRead(String senderId,Integer senderType,String receiverId){
        if(senderId!=null&&senderType!=null&&receiverId!=null){
            if (redisUtils.hHasKey(REDIS_UNREAD_COUNT + receiverId,senderId)){
                return (Integer)redisUtils.hGet(REDIS_UNREAD_COUNT + receiverId,senderId);
            }else{
                redisUtils.hSet(REDIS_UNREAD_COUNT + receiverId,senderId,0);
                return 0;
            }
        }else{
            return 0;
        }
    }

    public List<RelationVO> getUserRecentContactRelationVO(String userId){
        if(StringUtils.isNotEmpty(userId)){
            List<RecentVO> recentVOS = (ArrayList<RecentVO>) redisUtils.get(USER_RELATION_RECENT + userId);
            if(recentVOS!=null) {
                if(CollectionUtils.isEmpty(recentVOS)){
                    return new ArrayList<>();
                }
                Map<String, RecentVO> recentMap = new HashMap<>();
                recentVOS.forEach(recentVO -> {
                    recentMap.put(recentVO.getPeerId(), recentVO);
                });
                List<String> recentPeerIdList = new ArrayList<>(recentMap.keySet());
                List<Integer> visibleStatusList = UserStatusEnum.getAllUserStatusCodeList();
                List<User> userList = userService.getUserListByUserIdsAndStatus(recentPeerIdList,visibleStatusList);
                List<RelationVO> relationVOS = new ArrayList<>();
                if(CollectionUtils.isEmpty(userList)){
                    return relationVOS;
                }
                userList.forEach(user -> {
                    RelationVO relationVO = RelationVO.build(userId,user);
                    if (recentMap.containsKey(relationVO.getPeerId())) {
                        relationVO.setGmtLastContact(recentMap.get(relationVO.getPeerId()).getGmtLastContact());
                    }
                    relationVO.setRelTypeCode(RelationTypeEnum.TEMP.getCode());
                    relationVO.setRelType(RelationTypeEnum.TEMP.getDesc());
                    relationVOS.add(relationVO);
                });
                // 降序排序
                relationVOS.sort(new Comparator<RelationVO>() {
                    @Override
                    public int compare(RelationVO o1, RelationVO o2) {
                        return o2.getGmtLastContact().compareTo(o1.getGmtLastContact());
                    }
                });
                if(relationVOS!=null){
                    relationVOS.forEach(relationVO -> {
                        Integer tempUnReadCount = getUnRead(relationVO.getPeerId(),relationVO.getPeerTypeCode(),relationVO.getUserId());
                        relationVO.setUnReadMsgCount(tempUnReadCount);
                        relationVO.setCategory("recent");
                        relationVO.setPeerRole(JulyAuthorityUtils.roleClassifyString2Set(relationVO.getPeerRawRole()));
                        relationVO.setPeerAuthority(JulyAuthorityUtils.authorityClassifyString2Map(relationVO.getPeerRawAuthority()));
                        relationVO.setPeerType(ContainerEnum.getDescByCode(Integer.valueOf(relationVO.getPeerTypeCode())));
                        relationVO.setRelType(RelationTypeEnum.getDescByCode(Integer.valueOf(relationVO.getRelTypeCode())));
                    });
                }
                return relationVOS;
            }else{
                return new ArrayList<>();
            }
        }else{
            return null;
        }
    }

    public int upInsertRelationByUserIdAndPeerIdAndCategory(Relation relation) throws ServiceInternalException{
        if (relation!=null&&relation.getUserId()!=null&&relation.getPeerId()!=null ){
            if (relation.getRemarkName()!=null||relation.getTag()!=null) {
                Long newId= IdWorker.getId();
                relation.setRelId(newId.toString());
                relation.setGmtCreate(DateUtils.getCurrentDateTime());
                UpdateWrapper<Relation> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set(relation.getRemarkName() != null, "remark_name", relation.getRemarkName()).set(relation.getTag() != null, "tag", relation.getTag())
                        .eq("user_id", relation.getUserId()).eq("peer_id", relation.getPeerId()).eq("peer_type", relation.getPeerType());
                boolean result = super.saveOrUpdate(relation,updateWrapper);
                if(result){
                    return 1;
                }else{
                    return -1;
                }
            }else{
                QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", relation.getUserId()).eq("peer_id", relation.getPeerId()).eq("peer_type", relation.getPeerType());
                List<Relation> relationList = baseMapper.selectList(queryWrapper);
                if(relationList.size()>1){
                    throw new ServiceInternalException("重复的数据大于2个");
                }else if(relationList.size()==1){
                    throw new ServiceInternalException("此关系已经存在");
                }else{
                    Long newId= IdWorker.getId();
                    relation.setRelId(newId.toString());
                    relation.setGmtCreate(DateUtils.getCurrentDateTime());
                    return baseMapper.insert(relation);
                }
            }
        }else{
            return -1;
        }
    }

    public List<RecentVO> upInsertRecentList(RecentVO recentVO) throws ServiceInternalException{
        log.info("upInsertRecentList recentVO:{}",recentVO.toString());
        if (recentVO!=null&&recentVO.getUserId()!=null&&recentVO.getPeerId()!=null&&recentVO.getPeerType()!=null){
            if (recentVO.getGmtLastContact() == null) {
                recentVO.setGmtLastContact(DateUtils.getCurrentDateTime());
            }
            boolean isExisted = false;
            List<RecentVO> recentVOS = (ArrayList<RecentVO>) redisUtils.get(USER_RELATION_RECENT + recentVO.getUserId());
            if (recentVOS!=null) {
                RecentVO theOlder = null;
                // 找到用户的所有recent列表中相同userId和peerId的对象，同时找到列表中最老的对象最为新插入对象的替代对象
                for (RecentVO tempVO : recentVOS) {
                    if (recentVO.getUserId().equals(tempVO.getUserId()) && recentVO.getPeerId().equals(tempVO.getPeerId())) {
                        tempVO.setGmtLastContact(recentVO.getGmtLastContact());
                        isExisted = true;
                        break;
                    }
                    if (theOlder == null) {
                        theOlder = tempVO;
                    }else if (tempVO.getGmtLastContact()!=null&&theOlder.getGmtLastContact().after(tempVO.getGmtLastContact())) {
                        theOlder = tempVO;
                    }
                }
                if (!isExisted) {
                    // 如果不存在，就说明上面的没有更新成功，需要插入新的数据
                    if (recentVOS.size() >= UserConstants.USER_SOCIAL_RECENT_MAX_NUM) {
                        if (theOlder != null) {
                            recentVOS.remove(theOlder);
                        } else {
                            recentVOS.remove(recentVOS.size() - 1);;
                        }
                    }
                    recentVOS.add(recentVO);
                }
                // 降序排序
                recentVOS.sort(new Comparator<RecentVO>() {
                    @Override
                    public int compare(RecentVO o1, RecentVO o2) {
                        return o2.getGmtLastContact().compareTo(o1.getGmtLastContact());
                    }
                });
                redisUtils.set(USER_RELATION_RECENT + recentVO.getUserId(), recentVOS);
            }else{
                recentVOS= new ArrayList<>();
                recentVOS.add(recentVO);
                if (recentVOS.size() > UserConstants.USER_SOCIAL_RECENT_MAX_NUM) {
                    throw new ServiceInternalException("服务器限制为不能添加临时聊天");
                }else{
                    redisUtils.set(USER_RELATION_RECENT + recentVO.getUserId(), recentVOS);
                }
            }
            return recentVOS;
        }else{
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
    }

    public List<RecentVO>  deleteUserRecentRelation(RecentVO recentVO) throws ServiceInternalException{
        if (recentVO!=null&&recentVO.getUserId()!=null&&recentVO.getPeerId()!=null&&recentVO.getPeerType()!=null){
            List<RecentVO> recentVOS = (ArrayList<RecentVO>) redisUtils.get(USER_RELATION_RECENT + recentVO.getUserId());
            if (recentVOS!=null) {
                RecentVO needRemovedVO = null;
                for (RecentVO tempVO : recentVOS) {
                    if (recentVO.getUserId().equals(tempVO.getUserId()) && recentVO.getPeerId().equals(tempVO.getPeerId())) {
                        needRemovedVO = tempVO;
                        break;
                    }
                }
                if(needRemovedVO!=null) {
                    recentVOS.remove(needRemovedVO);
                    redisUtils.set(USER_RELATION_RECENT + recentVO.getUserId(), recentVOS);
                }
            }else{
                recentVOS = new ArrayList<>();
            }
            return recentVOS;
        }else{
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
    }

    public Boolean deleteUserRelationByIdAndType(String userId,String peerId,String peerTypeCode){
        if (userId!=null && peerId!=null){
            userId = StringEscapeUtils.escapeSql(userId);
            peerId = StringEscapeUtils.escapeSql(peerId);
            peerTypeCode = StringEscapeUtils.escapeSql(peerTypeCode);
            QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("peer_id",peerId);
            if(StringUtils.isNotEmpty(peerId)){
                queryWrapper.eq("peer_type",peerTypeCode);
            }
            super.remove(queryWrapper);
            return true;
        }else{
            return false;
        }

    }
}
