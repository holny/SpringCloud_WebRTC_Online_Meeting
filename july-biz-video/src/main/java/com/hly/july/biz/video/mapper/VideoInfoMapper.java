package com.hly.july.biz.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hly.july.biz.video.entity.VideoSearchVO;
import com.hly.july.common.db.entity.Video;
import com.hly.july.common.web.vo.VideoInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @ClassName VideoInfoMapper
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/17 22:36
 * @Version 1.0.0
 **/
@Mapper
public interface VideoInfoMapper{
    /**
     * <p>
     * 查询 : 根据state状态查询用户列表，分页显示
     * </p>
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @return 分页对象
     */
    Page<VideoInfoVO> selectVideoVoInfoPageBySearch(Page<?> page, @Param("searchVO")VideoSearchVO searchVO);
    List<VideoInfoVO> selectVideoVoInfoBySearch(@Param("searchVO")VideoSearchVO searchVO);

}

