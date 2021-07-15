package com.hly.july.common.auth.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.hly.july.common.core.constant.AuthConstants;
import com.nimbusds.jose.JWSObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.userdetails.UserDetails;


import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Linyuan Hou
 * @date 2021/3/16 17:53
 */
@Slf4j
@Data
public class JwtUtils {
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * 生成token令牌
     * @param userDetails 用户
     * @return token令牌
     */
    public static String generateToken(UserDetails userDetails, PrivateKey privateKey) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        return generateToken(claims, privateKey);
    }

    public static String getTokenFromServletRequest(HttpServletRequest request) {
        String tokenRaw = request.getHeader(AuthConstants.TOKEN_HEADER_KEYWORD);
        return getTokenFromHeader(tokenRaw);
    }

    public static String getTokenFromHttpRequest(HttpRequest request) {
        String tokenRaw = request.getHeaders().getFirst(AuthConstants.TOKEN_HEADER_KEYWORD);
        return getTokenFromHeader(tokenRaw);
    }

    public static JSONObject getJSONObjectFromToken(String token) {
        //                Claims claims = JwtUtils.getClaimsFromToken(token, rsaKeyProp.getPublicKey());
////                List<String> authorList = (List<String>)claims.get("authorities");
//                String jti = claims.get(AuthConstants.TOKEN_JTI_KEYWORD) != null?(String)claims.get(AuthConstants.TOKEN_JTI_KEYWORD):null;
        try{
            JWSObject jwsObject = JWSObject.parse(token);
            String payload = jwsObject.getPayload().toString();
            JSONObject jsonObject = JSONUtil.parseObj(payload);
            return jsonObject;
        }catch (ParseException e){
            log.error("Token parse jtl error, token:{}, e:{}",token,e.getMessage());
            return null;
        }
    }

    private static String getTokenFromHeader(String tokenRaw){
        if (!StrUtil.isBlank(tokenRaw)) {
            if(tokenRaw.startsWith(AuthConstants.TOKEN_PREFIX_KEYWORD)){
                String token = StringUtils.removeStart(tokenRaw,AuthConstants.TOKEN_PREFIX_KEYWORD);
                if(!StrUtil.isBlank(token)) {
                    return token;
                }else{
                    return null;
                }
            }else{
                return tokenRaw;
            }
        }else{
            return null;
        }
    }

    /**
     * 从令牌中获取用户名
     * @param token 令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token, PublicKey publicKey) {
        if(StringUtils.isBlank(token)){
            log.warn("getUsernameFromToken, token is null");
            return null;
        }
        if(publicKey==null){
            log.warn("getUsernameFromToken, publicKey is null");
            return null;
        }
        String username;
        try {
            Claims claims = getClaimsFromToken(token, publicKey);
            username = claims.getSubject();  // 取出为null
            log.info("getUsernameFromToken, claims:{}", claims.toString());
            log.info("getUsernameFromToken, subject:{}", claims.getSubject());
        } catch (Exception e) {
            log.error("Can not get Username from Token, token:{}", token);
            username = null;
        }
        return username;
    }

    /**
     * 判断令牌是否过期
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token, PublicKey publicKey) {
        if(StringUtils.isBlank(token)){
            log.warn("isTokenExpired, token is null");
            return true;
        }
        if(publicKey==null){
            log.warn("isTokenExpired, publicKey is null");
            return true;
        }
        try {
            Claims claims = getClaimsFromToken(token, publicKey);
            Date expiration = claims.getExpiration();
            log.info("Token, expiration:{}",expiration.toString());
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("isTokenExpired, Exception:{}",e.getMessage());
            return true;
        }
    }

    /**
     * 刷新令牌
     * @param token 原令牌
     * @return 新令牌
     */
    public static String refreshToken(String token, PublicKey publicKey, PrivateKey privateKey) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token, publicKey);
            refreshedToken = generateToken(claims, privateKey);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证令牌
     * @param token         令牌
     * @return 是否有效
     */
    public static Boolean validateToken(String token, PublicKey publicKey) {
        if(StringUtils.isBlank(token)){
            log.warn("validateToken, token is null");
            return false;
        }
        if(publicKey==null){
            log.warn("validateToken, publicKey is null");
            return false;
        }
        Claims claims = getClaimsFromToken(token,publicKey);
        return claims!=null;
    }

    /**
     * 验证令牌, 资源服务器OncePerRequestFilter使用不了,getUsernameFromToken中claims.getSubject()返回为null，
     * 所以只通过判断能否从token中解析出claims来决定token是否有效。
     * @param token         令牌
     * @param userDetails   用户
     * @return 是否有效
     */
    public static Boolean validateToken(String token, UserDetails userDetails, PublicKey publicKey) {
        if(StringUtils.isBlank(token)){
            log.warn("validateToken, token is null");
            return false;
        }
        if(userDetails==null){
            log.warn("validateToken, userDetails is null");
            return false;
        }
        if(publicKey==null){
            log.warn("validateToken, publicKey is null");
            return false;
        }
        String username = getUsernameFromToken(token, publicKey);
        if(StringUtils.isNotBlank(username)) {
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, publicKey));
        }else{
            log.warn("validateToken, username is empty");
            return false;
        }
    }

    /**
     * 生成令牌
     * @param claims 数据声明
     * @return token令牌
     */
    private static String generateToken(Map<String, Object> claims, PrivateKey privateKey) {
        Date date = new Date(System.currentTimeMillis());
        return Jwts.builder().setClaims(claims)
                .setId(createJTI())
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + AuthConstants.TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 获取数据声明
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims getClaimsFromToken(String token, PublicKey publicKey) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("Can not get Claims from Token, token:{}", token);
            e.printStackTrace();
            claims = null;
        }
        return claims;
    }


    private static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }


}
