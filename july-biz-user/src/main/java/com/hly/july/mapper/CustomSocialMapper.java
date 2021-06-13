package com.hly.july.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hly.july.common.biz.entity.Social;
import com.hly.july.common.biz.vo.SocialVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @ClassName CustomSocialMapper
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/13 19:17
 * @Version 1.0.0
 **/
@Mapper
public interface CustomSocialMapper extends BaseMapper<SocialVO> {

    @Select("<script>select s.social_id,s.user_id,s.peer_id,s.social_type,s.gmt_create,s.gmt_last_contact,s.remark_name,s.tag,u.user_name as peer_user_name,u.gender as peer_gender,u.phone_number as peer_phone_number,u.email as peer_email,u.avatar as peer_avatar,u.gmt_last_login as peer_gmt_last_login,u.status as peer_status,u.gmt_birthday as peer_gmt_birthday,u.nick_name as peer_nick_name,u.role as peer_role,u.authority as peer_authority from social s left outer join user u on s.peer_id = u.user_id where s.user_id=#{userId, jdbcType=VARCHAR} <if test='type!=null'> and s.social_type=#{type, jdbcType=VARCHAR}</if></script>")
    public List<SocialVO> getSocialVOByUserIdAndType(@Param("userId") String userId, @Param("type") String type);
}
