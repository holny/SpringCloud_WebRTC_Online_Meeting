package com.hly.july.auth.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class OauthDataSourceConfig {

    @ConfigurationProperties(prefix = "spring.datasource-oauth.druid")
    @Bean(name="oauthDataSource")
    @Primary //表示这里定义的DataSource将覆盖其他来源的DataSource。
    public DataSource dataSource(@Qualifier("oauthWallFilter") WallFilter wallFilter){
        DruidDataSource dataSource = new DruidDataSource();
        List filterList=new ArrayList<>();
        filterList.add(wallFilter);
        dataSource.setProxyFilters(filterList);
        return  dataSource;
    }


    @Bean(name="oauthTransactionManager")
    @DependsOn("oauthDataSource")
    public DataSourceTransactionManager transactionManager(@Qualifier("oauthDataSource") DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean(name = "oauthWallFilter")
    @DependsOn("oauthWallConfig")
    public WallFilter wallFilter(@Qualifier("oauthWallConfig")WallConfig wallConfig) {
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig);
        return wallFilter;
    }

    @Bean(name = "oauthWallConfig")
    public WallConfig wallConfig() {
        WallConfig wallConfig = new WallConfig();
        wallConfig.setMultiStatementAllow(true);//允许一次执行多条语句
        wallConfig.setNoneBaseStatementAllow(true);//允许一次执行多条语句
        return wallConfig;
    }

}

