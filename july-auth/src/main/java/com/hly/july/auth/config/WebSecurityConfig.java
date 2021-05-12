package com.hly.july.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Linyuan Hou
 * @date 2021/3/15 15:50
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)  // 开启Spring security 注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//            http
//                .authorizeRequests()
//                .antMatchers("/**").permitAll()
//                .and().csrf().disable();
        http.csrf().disable();
        http
                .requestMatchers().antMatchers("/oauth/**","/login/**","/logout/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin().permitAll(); //新增login form支持用户登录及授权
//        http.authorizeRequests()
//                .antMatchers("/login", "/oauth/authorize").permitAll()
//                .anyRequest().authenticated().and().csrf().disable();
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/auth/userAuth").permitAll()
//                .anyRequest().authenticated();
//                .authorizeRequests()
//                .antMatchers("/*").hasAnyAuthority("create")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin();
//        http
//                .authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
//                .and()
//                .authorizeRequests().antMatchers("/getPublicKey","/oauth/logout").permitAll()
//
//                // @link https://gitee.com/xiaoym/knife4j/issues/I1Q5X6 (Security放行url)
//                .antMatchers("/webjars/**","/doc.html","/swagger-resources/**","/v2/api-docs").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable();
    }


}
