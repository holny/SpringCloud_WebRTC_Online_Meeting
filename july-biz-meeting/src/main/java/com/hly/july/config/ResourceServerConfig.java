package com.hly.july.config;

import com.hly.july.common.constant.AuthorityEnum;
import com.hly.july.common.constant.RoleEnum;
import com.hly.july.common.exception.CustomAccessDeniedHandler;
import com.hly.july.common.exception.CustomAuthenticationEntryPoint;
import com.hly.july.common.properties.RSAKeyProperties;
import com.hly.july.filter.TokenAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @author Linyuan Hou
 * @date 2021/3/16 09:49
 */
@Slf4j
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Autowired
    private RSAKeyProperties keyProperties;

    @Bean
    protected JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        Resource resource = new FileSystemResource(System.getProperty("user.dir") + "/certificate/july-pubkey_0317.txt");
//        String publicKey;
//        try {
//            publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
//        } catch (IOException e) {
//            throw new RuntimeException("获取jwt公钥信息失败");
//        }
        log.info("linyhou keyProperties.getPublicKeyString:{}",keyProperties.getPublicKeyString());
        converter.setVerifierKey(keyProperties.getPublicKeyString());

        return converter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
//        resources
//                /**
//                 * jwt 无状态方式
//                 */
//                .tokenStore(new JwtTokenStore(accessTokenConverter()))
//                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                .tokenServices(tokenService())
//                .stateless(true);
        resources.tokenStore(new JwtTokenStore(accessTokenConverter()))
                //自定义Token异常信息,用于token校验失败返回信息
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                //授权异常处理
                .accessDeniedHandler(new CustomAccessDeniedHandler()).stateless(true);
//        resources.resourceId(RESOURCE_ID)
//                .tokenStore(tokenStore)
//                .stateless(true);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests()
//                .antMatchers("/**").access("#oauth2.hasScope('all')")
//                .and().csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCr  eationPolicy.STATELESS);    // JWT 设置spring security 无状态，STATELESS不使用任何session，

        http.csrf().disable()
                .authorizeRequests()
//                .antMatchers("/meeting/endpointChat/**").hasRole(RoleEnum.ROLE_SUPER_ADMIN.getDesc())
                .antMatchers("/meeting/endpointChat/**").authenticated()
//                .antMatchers("/meeting/endpointChat/**").hasAnyRole(RoleEnum.ROLE_USER.getCode(),RoleEnum.ROLE_AUTHOR.getCode(),RoleEnum.ROLE_EXPERT.getCode(),RoleEnum.ROLE_ADMIN.getCode(),RoleEnum.ROLE_SUPER_ADMIN.getCode())
//                .antMatchers("/meeting/**").permitAll()
//                .antMatchers("/consumer/test/**").hasAuthority("PRO")
                .antMatchers("/**").permitAll()//所有/r/**的请求必须认证通过 .anyRequest().permitAll()//除了/r/**，其它的请求可以访问
        ;
        http.addFilterBefore(tokenAuthenticationFilter, LogoutFilter.class);//添加JWT身份认证的filter
        http
                // 基于token，所以不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //资源服务令牌解析服务
//    @Bean
//    public ResourceServerTokenServices tokenService() {
////使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
//        RemoteTokenServices service=new RemoteTokenServices();
//        service.setCheckTokenEndpointUrl("http://localhost:8000/oauth/check_token"); service.setClientId("client");
//        service.setClientSecret("hly4321");
//        return service;
//    }


}
