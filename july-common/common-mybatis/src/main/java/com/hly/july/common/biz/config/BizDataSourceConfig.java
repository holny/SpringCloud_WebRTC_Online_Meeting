package com.hly.july.common.biz.config;


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
public class BizDataSourceConfig {

    @ConfigurationProperties(prefix = "spring.datasource-biz.druid")
    @Bean(name="bizDataSource")
    public DataSource dataSource(@Qualifier("bizWallFilter") WallFilter wallFilter){
        DruidDataSource dataSource = new DruidDataSource();
        List filterList=new ArrayList<>();
        filterList.add(wallFilter);
        dataSource.setProxyFilters(filterList);
        return  dataSource;
    }


    @Bean(name="bizTransactionManager")
    @DependsOn("bizDataSource")
    public DataSourceTransactionManager transactionManager(@Qualifier("bizDataSource") DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean(name = "bizWallFilter")
    @DependsOn("bizWallConfig")
    public WallFilter wallFilter(@Qualifier("bizWallConfig")WallConfig wallConfig) {
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig);
        return wallFilter;
    }

    @Bean(name = "bizWallConfig")
    public WallConfig wallConfig() {
        WallConfig wallConfig = new WallConfig();
        wallConfig.setMultiStatementAllow(true);//允许一次执行多条语句
        wallConfig.setNoneBaseStatementAllow(true);//允许一次执行多条语句
        return wallConfig;
    }

}

