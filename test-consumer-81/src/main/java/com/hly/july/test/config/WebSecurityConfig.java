package com.hly.july.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Linyuan Hou
 * @date 2021/3/16 09:56
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //安全拦截机制(最重要)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and().csrf().disable();
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/consumer/payment/**").hasRole("ADMINS")
//                .antMatchers("/consumer/test/**").hasAuthority("PROS")
//                .antMatchers("/consumer/**").authenticated()//所有/r/**的请求必须认证通过 .anyRequest().permitAll()//除了/r/**，其它的请求可以访问
//                ;
//        http.addFilterBefore(tokenAuthenticationFilter, LogoutFilter.class);//添加JWT身份认证的filter
//        http
//                // 基于token，所以不需要session
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.csrf().disable()
//                .authorizeRequests()
////                .antMatchers("/consumer/payment/**").hasRole("ADMIN")
////                .antMatchers("/consumer/test/**").hasAuthority("PRO")
////                .antMatchers("/consumer/**").authenticated()//所有/r/**的请求必须认证通过 .anyRequest().permitAll()//除了/r/**，其它的请求可以访问
//                .anyRequest().permitAll()//除了/r/**，其它的请求可以访问
        ;
    }
}
