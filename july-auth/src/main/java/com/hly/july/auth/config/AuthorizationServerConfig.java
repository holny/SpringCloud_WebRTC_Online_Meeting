package com.hly.july.auth.config;


import com.hly.july.auth.service.JdbcClientDetailsServiceImpl;
import com.hly.july.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.*;

/**
 * @author Linyuan Hou
 * @date 2021/3/12 18:31
 * AuthorizationServerConfigurerAdapter要求配置以下几个类，这几个类是由Spring创建的独立的配置对象，它们 会被Spring传入AuthorizationServerConfigurer中进行配置。
 * ClientDetailsServiceConfigurer、AuthorizationServerEndpointsConfigurer、AuthorizationServerSecurityConfigurer
 * https://projects.spring.io/spring-security-oauth/docs/oauth2.html
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("oauthDataSource")
    private DataSource dataSource;


    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public PasswordEncoder passwordEncoder;


    /**
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //tokenkey这个endpoint当使用JwtToken且使用非对称加密时，资源服务用于获取公钥而开放的，这里指这个 endpoint完全公开。
                .tokenKeyAccess("permitAll()") // /oauth/token_key 安全配置
                //checkToken这个endpoint完全公开
                .checkTokenAccess("permitAll()") // /oauth/check_token 安全配置
                //允许表单认证
                .allowFormAuthenticationForClients();
    }

    /**
     * 配置客户端详情(数据库)
     * ClientDetailsServiceConfigurer:用来配置客户端详情服务(ClientDetailsService)，客户端详情信息在 这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
     * 我们需要把客户端信息配置在认证服务器上来表示认证服务器所认可的客户端。一般可配置在认证服务器的内存中，但是这样很不方便管理扩展。所以实际最好配置在数据库中的。
     * 系统提供的二个ClientDetailsService实现类：JdbcClientDetailsService、InMemoryClientDetailsService
     * ClientDetails有几个重要的属性如下列表:
     * clientId:(必须的)用来标识客户的Id。
     * secret:(需要值得信任的客户端)客户端安全码，如果有的话。
     * scope:用来限制客户端的访问范围，如果为空(默认)的话，那么客户端拥有全部的访问范围。
     * authorizedGrantTypes:此客户端可以使用的授权类型，默认为空。 authorization_code,password,refresh_token,implicit,client_credentials
     * authorities:此客户端可以使用的权限(基于Spring Security authorities)。
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
//        ((JdbcClientDetailsService)
//                clientDetailsService).setPasswordEncoder(passwordEncoder());
//        return clientDetailsService;
//
//        auth.userDetailsService(userServiceDetail).passwordEncoder(new BCryptPasswordEncoder());
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        /**
         * 定义了一些操作使得你可以对令牌进行一些必要的管理，令牌可以被用来加载身份信息，里面包含了这个令牌的相关权限。
         * 需要继承 DefaultTokenServices 这个类， 里面包含了一些有用实现，你可以使用它来修改令牌的格式和令牌的存储。默认的，当它尝试创建一个令牌的时 候，是使用随机值来进行填充的，除了持久化令牌是委托一个 TokenStore 接口来实现以外，这个类几乎帮你做了 所有的事情。
         */
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(clientDetailsService());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore());
        // 设置access_token有效时长12小时，默认12小时
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
        // 设置refresh_token有效时长7天，默认30天
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(),jwtAccessTokenConverter()));
        tokenServices.setTokenEnhancer(tokenEnhancerChain);
        endpoints
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
                .tokenServices(tokenServices)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
//                // 自定义认证异常处理类
//                .exceptionTranslator(webResponseExceptionTranslator())
                // refresh token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                //      1 重复使用：access token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                //      2 非重复使用：access token过期刷新时， refresh token过期时间延续，在refresh token有效期内刷新便永不失效达到无需再次登录的目的
                .reuseRefreshTokens(false);
    }

    /**
     * JWT内容增强
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new JwtTokenEnhancer();
    }

    /**
     * 使用非对称加密算法对token签名
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        /**
         * RSA 非对称方式
         *
         * 生成 SHA256 的 lion-jwt.jks 签名文件，有效期 3650 天
         * 命令：keytool -genkeypair -alias lion-jwt -validity 3650 -keyalg RSA -dname "CN=jwt,OU=jtw,O=jtw,L=zurich,S=zurich,C=CH" -keypass 123456 -keystore lion-jwt.jks -storepass 123456
         * keytool -genkey -alias july_v1 -keyalg RSA -keypass 123456 -keystore july_v1.jks -storepass 123456
         * keytool -importkeystore -srckeystore july_v1.jks -destkeystore july_v1.jks -deststoretype pkcs12"
         * keytool -genkey -alias july_v1 -keyalg RSA -keypass 123456 -keystore july_v1.jks -storepass 123456
         * -genkey 生成密钥  genkeypair 生成密钥对(公钥和私钥)
         *
         * -alias 别名
         *
         * -keyalg 密钥算法
         *
         * -keypass 密钥口令
         *
         * -keystore 生成密钥库的存储路径和名称
         *
         * -storepass 密钥库口令
         *
         * CN:名称与姓氏，OU:组织单位名称，O:组织名称，L:城市或者区域，ST:省/市/自治区，C:国家/地区代码
         *
         * jwt 公钥获取
         *
         * 命令：keytool -list -rfc --keystore lion-jwt.jks | openssl x509 -inform pem -pubkey
         * keytool -importkeystore -srckeystore july_v1.jks -destkeystore july_v1.jks -deststoretype pkcs12"
         * 密码：123456
         * -----BEGIN PUBLIC KEY-----
         * MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhf6oZLygSrszafyxNgL1
         * N9JggRIRb+eVpmQqPKR/qNJ55yUfduX2F/bxmDYXCFtcEtI+oZ8qnUgeN1OmSZ3N
         * Ma/22dEDE7EhEkeTD8eRjEvem2hnKDq/4SJ8erl9RfLMfITm8wgS67qmV28zdCZW
         * G4K8l9/LE0AajZ34xopj0OpTYpnmbbd589tAnQpXGWjRgIW/MFm562b2JBNY6uMH
         * AAr3DXY/EgycbxhzxwL6F9+tYc2lMfkDyZJqY2LUcw5/hPYli17d+skJKWeHB3+j
         * 3XHrHuuItoPk7rvV9enAQcTN4l6/6+62VSSmJ1JR609RKrgh1NtcbAeFWzqOHH9u
         * LwIDAQAB
         * -----END PUBLIC KEY-----
         * 将生成的公钥信息存放在 lion-pubkey.cert 文件中
         *
         * 注：若使用jar或docker部署，请在jar包根路径或docker容器内创建certificate文件夹，将lion-jwt.jks文件放入
         */
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new FileSystemResource(System.getProperty("user.dir") + "/certificate/july_v1.jks"), "123456".toCharArray());
//        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("july_v1"));
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("july_v1");

//        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(
//                new ClassPathResource("youlai.jks"), "123456".toCharArray());
//        KeyPair keyPair = factory.getKeyPair(
//                "youlai", "123456".toCharArray());
        return keyPair;
    }



    public ClientDetailsService clientDetailsService() {
        JdbcClientDetailsServiceImpl jdbcClientDetailsService = new JdbcClientDetailsServiceImpl(dataSource);
//        jdbcClientDetailsService.setFindClientDetailsSql(AuthConstants.FIND_CLIENT_DETAILS_SQL);
//        jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstants.SELECT_CLIENT_DETAILS_SQL);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);

        return jdbcClientDetailsService;
    }


    /**
     * 定义了一些操作使得你可以对令牌进行一些必要的管理，令牌可以被用来加载身份信息，里面包含了这个令牌的相关权限。
     * 需要继承 DefaultTokenServices 这个类， 里面包含了一些有用实现，你可以使用它来修改令牌的格式和令牌的存储。默认的，当它尝试创建一个令牌的时 候，是使用随机值来进行填充的，除了持久化令牌是委托一个 TokenStore 接口来实现以外，这个类几乎帮你做了 所有的事情。
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenService(){
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService());
        service.setSupportRefreshToken(true);
        service.setTokenStore(tokenStore());
        // 设置access_token有效时长12小时，默认12小时
        service.setAccessTokenValiditySeconds(60 * 60 * 12);
        // 设置refresh_token有效时长7天，默认30天
        service.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        return service;
    }

    /**
     * InMemoryTokenStore:这个版本的实现是被默认采用的，它可以完美的工作在单服务器上(即访问并发量 压力不大的情况下，并且它在失败的时候不会进行备份)，大多数的项目都可以使用这个版本的实现来进行 尝试，你可以在开发的时候使用它来进行管理，因为不会被保存到磁盘中，所以更易于调试。
     * JdbcTokenStore:这是一个基于JDBC的实现版本，令牌会被保存进关系型数据库。使用这个版本的实现时， 你可以在不同的服务器之间共享令牌信息，使用这个版本的时候请注意把"spring-jdbc"这个依赖加入到你的 classpath当中。
     *
     * JwtTokenStore:这个版本的全称是 JSON Web Token(JWT)，它可以把令牌相关的数据进行编码(因此对 于后端服务来说，它不需要进行存储，这将是一个重大优势)，
     * 但是它有一个缺点，那就是撤销一个已经授 权令牌将会非常困难，所以它通常用来处理一个生命周期较短的令牌以及撤销刷新令牌(refresh_token)。
     * 另外一个缺点就是这个令牌占用的空间会比较大，如果你加入了比较多用户凭证信息。JwtTokenStore 不会保存任何数据，但是它在转换令牌值以及授权信息方面与 DefaultTokenServices 所扮演的角色是一样的。
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        /**
         * redis 存储有状态方式
         */
//        return new RedisTokenStore(redisConnectionFactory);
        /**
         * jwt 无状态方式
         */
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


}


