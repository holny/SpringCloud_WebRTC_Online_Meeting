package com.hly.july.service.impl;

import com.hly.july.common.biz.constant.ContainerEnum;
import com.hly.july.common.biz.exception.ServiceInternalException;
import com.hly.july.common.biz.result.Result;
import com.hly.july.common.biz.result.ResultCode;
import com.hly.july.common.biz.vo.RecentVO;
import com.hly.july.common.biz.vo.RelationVO;
import com.hly.july.entity.Event;
import com.hly.july.entity.EventEnum;
import com.hly.july.service.api.BizUserApiService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName AsyncJobService
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/18 14:50
 * @Version 1.0.0
 **/
@Service
@Slf4j
public class AsyncJobService {
    @Autowired
    private BizUserApiService bizUserApiService;

    @Autowired
    private UserService userService;

    @Async("taskExecutor")
    public Boolean upInsertRecentContact(String userId,String peerId, RecentVO recentVO){
        recentVO.setUserId(userId);
        recentVO.setUserId(peerId);
        Result<List<RelationVO>> recentVOListResult =  bizUserApiService.upInsertRecentContact(userId,recentVO);
        if(recentVOListResult.getCode()== ResultCode.SUCCESS.getCode()){
            Event<List<RelationVO>> eventToUserId = Event.buildPersonal(EventEnum.EVENT_RECENT_CHANGED,userId,recentVOListResult.getData());
            List<RelationVO> relationVOS = recentVOListResult.getData();
            if(relationVOS!=null){
                for (RelationVO relationVO : relationVOS) {
                    relationVO
                }
            }
            try {
                userService.sendPersonalEvent(userId,eventToUserId);
            }catch (ServiceInternalException e){
                log.error("upInsertRecentContact sendPersonalEvent-- e:{}",e.getErrorMsg());
                return false;
            }
        }else{
            log.error("upInsertRecentContact upInsertRecentContact-- result fail:code:{},msg:{}",recentVOListResult.getCode(),recentVOListResult.getMsg());
            return false;
        }
        return true;
    }

}
