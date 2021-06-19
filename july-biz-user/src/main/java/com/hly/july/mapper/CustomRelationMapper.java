package com.hly.july.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hly.july.common.biz.vo.RelationVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @ClassName CustomRelationMapper
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/13 19:17
 * @Version 1.0.0
 **/
@Mapper
public interface CustomRelationMapper extends BaseMapper<RelationVO> {

    @Select("<script>select r.rel_type as rel_type_code,r.rel_id,r.user_id,r.peer_id,r.peer_type as peer_type_code,r.gmt_create,r.remark_name,r.tag,u.user_name as peer_user_name,u.gender as peer_gender,u.phone_number as peer_phone_number,u.email as peer_email,u.avatar as peer_avatar,u.gmt_last_login as peer_gmt_last_login,u.status as peer_status,u.gmt_birthday as peer_gmt_birthday,u.nick_name as peer_nick_name,u.role as peer_raw_role,u.authority as peer_raw_authority from relation r left outer join user u on r.peer_id = u.user_id where r.user_id=#{userId, jdbcType=VARCHAR}  <if test='peerId!=null'> and r.peer_id=#{peerId, jdbcType=VARCHAR}</if>  <if test='peerType!=null'> and r.peer_type=#{peerType, jdbcType=VARCHAR} </if> <if test='relType!=null'> and r.rel_type=#{relType, jdbcType=VARCHAR} </if> order by r.gmt_create desc</script>")
    public List<RelationVO> getRelationVOByUserIdAndPeerIdAndType(@Param("userId") String userId, @Param("peerId") String peerId, @Param("peerType") Integer peerType, @Param("relType") Integer relType);

    @Select("<script>select r.rel_type as rel_type_code,r.rel_id,r.user_id,r.peer_id,r.peer_type as peer_type_code,r.gmt_create,r.remark_name,r.tag,u.user_name as peer_user_name,u.gender as peer_gender,u.phone_number as peer_phone_number,u.email as peer_email,u.avatar as peer_avatar,u.gmt_last_login as peer_gmt_last_login,u.status as peer_status,u.gmt_birthday as peer_gmt_birthday,u.nick_name as peer_nick_name,u.role as peer_raw_role,u.authority as peer_raw_authority from relation r left outer join user u on r.peer_id = u.user_id where r.user_id=#{userId, jdbcType=VARCHAR}  <if test='peerIds!=null and peerIds.size()>0'> and r.peer_id in <foreach collection='peerIds' item='peerId' open='(' separator=',' close=')'>#{userId, jdbcType=VARCHAR}</foreach> </if>  <if test='peerType!=null'> and r.peer_type=#{peerType, jdbcType=VARCHAR} </if> <if test='relType!=null'> and r.rel_type=#{relType, jdbcType=VARCHAR} </if> order by r.gmt_create desc </script>")
    public List<RelationVO> getRelationVOByUserIdAndPeerIdsAndType(@Param("userId") String userId, @Param("peerIds") List<String> peerIds, @Param("peerType") Integer peerType, @Param("relType") Integer relType);

    @Select("<script>select r.rel_type as rel_type_code,r.rel_id,r.user_id,r.peer_id,r.peer_type as peer_type_code,r.gmt_create,r.remark_name,r.tag,u.user_name as peer_user_name,u.gender as peer_gender,u.phone_number as peer_phone_number,u.email as peer_email,u.avatar as peer_avatar,u.gmt_last_login as peer_gmt_last_login,u.status as peer_status,u.gmt_birthday as peer_gmt_birthday,u.nick_name as peer_nick_name,u.role as peer_raw_role,u.authority as peer_raw_authority from relation r left outer join user u on r.peer_id = u.user_id where r.user_id=#{userId, jdbcType=VARCHAR}  <if test='peerId!=null'> and r.peer_id=#{peerId, jdbcType=VARCHAR}</if>  <if test='peerType!=null'> and r.peer_type=#{peerType, jdbcType=VARCHAR} </if> <if test='relTypeList!=null and relTypeList.size()>0'> and r.rel_type in <foreach collection='relTypeList' item='relType' open='(' separator=',' close=')'>#{relType, jdbcType=VARCHAR}</foreach>  </if> order by r.gmt_create desc</script>")
    public List<RelationVO> getRelationVOByUserIdAndPeerIdAndTypeList(@Param("userId") String userId, @Param("peerId") String peerId, @Param("peerType") Integer peerType, @Param("relTypeList") List<Integer> relType);
}
