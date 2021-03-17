package com.hly.july.auth.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * @author Linyuan Hou
 * @date 2021/3/12 19:49
 */
@Slf4j
public class JdbcClientDetailsServiceImpl extends JdbcClientDetailsService {
    public JdbcClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @SneakyThrows
    public ClientDetails loadClientByClientId(String clientId)  {
        log.info("hly clientId:{}",clientId);
        ClientDetails clientDetails = super.loadClientByClientId(clientId);
        log.info("hly clientDetails:{}",clientDetails.toString());
        return clientDetails;
    }
}
