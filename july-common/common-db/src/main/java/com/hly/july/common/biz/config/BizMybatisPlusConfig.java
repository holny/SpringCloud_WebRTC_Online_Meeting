package com.hly.july.common.biz.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Linyuan Hou
 * @date 2021/5/6 21:19
 */
@Configuration
public class BizMybatisPlusConfig {

    /**
     *  使用MybatisSqlSessionFactoryBean因为，如果使用SqlSessionFactory，运行报错mapper找不到，mybatis plus官方建议使用MybatisSqlSessionFactoryBean
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(@Qualifier("bizDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        return sessionFactory;
    }

}
