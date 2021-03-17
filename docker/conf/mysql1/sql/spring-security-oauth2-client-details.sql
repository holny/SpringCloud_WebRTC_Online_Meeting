CREATE DATABASE IF NOT EXISTS july_oauth2 DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
create user 'july_oauth2'@'%' identified by 'auth4321july';
grant all privileges on july_oauth2.* to 'july_oauth2'@'%';
flush privileges;
USE july_oauth2;
/*

 */
-- used in tests that use HSQL
DROP TABLE IF EXISTS `oauth_client_details`;
create table `oauth_client_details` (
                                      client_id VARCHAR(256) PRIMARY KEY,
                                      resource_ids VARCHAR(256),
                                      client_secret VARCHAR(256),
                                      scope VARCHAR(256),
                                      authorized_grant_types VARCHAR(256),
                                      web_server_redirect_uri VARCHAR(256),
                                      authorities VARCHAR(256),
                                      access_token_validity INTEGER,
                                      refresh_token_validity INTEGER,
                                      additional_information VARCHAR(4096),
                                      autoapprove VARCHAR(256)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- DROP TABLE IF EXISTS `oauth_client_details`;
-- CREATE TABLE `oauth_client_details` (
--                                         `client_id` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
--                                         `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
--                                         `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
--                                         `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
--                                         `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
--                                         `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
--                                         `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
--                                         `access_token_validity` int(11) DEFAULT NULL,
--                                         `refresh_token_validity` int(11) DEFAULT NULL,
--                                         `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
--                                         `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
--                                         PRIMARY KEY (`client_id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


-- DROP TABLE IF EXISTS `oauth_client_token`;
-- create table `oauth_client_token` (
--                                     token_id VARCHAR(256),
--                                     token LONGVARBINARY,
--                                     authentication_id VARCHAR(256) PRIMARY KEY,
--                                     user_name VARCHAR(256),
--                                     client_id VARCHAR(256)
-- );
--
-- DROP TABLE IF EXISTS `oauth_access_token`;
-- create table `oauth_access_token` (
--                                     token_id VARCHAR(256),
--                                     token LONGVARBINARY,
--                                     authentication_id VARCHAR(256) PRIMARY KEY,
--                                     user_name VARCHAR(256),
--                                     client_id VARCHAR(256),
--                                     authentication LONGVARBINARY,
--                                     refresh_token VARCHAR(256)
-- );
--
-- DROP TABLE IF EXISTS `oauth_refresh_token`;
-- create table `oauth_refresh_token` (
--                                      token_id VARCHAR(256),
--                                      token LONGVARBINARY,
--                                      authentication LONGVARBINARY
-- );
--
-- DROP TABLE IF EXISTS `oauth_code`;
-- create table `oauth_code` (
--                             code VARCHAR(256), authentication LONGVARBINARY
-- );
--
-- DROP TABLE IF EXISTS `oauth_approvals`;
-- create table `oauth_approvals` (
--                                  userId VARCHAR(256),
--                                  clientId VARCHAR(256),
--                                  scope VARCHAR(256),
--                                  status VARCHAR(10),
--                                  expiresAt TIMESTAMP,
--                                  lastModifiedAt TIMESTAMP
-- );


-- -- customized oauth_client_details table
-- DROP TABLE IF EXISTS `client_details`;
-- create table `client_details` (
--                                appId VARCHAR(256) PRIMARY KEY,
--                                resourceIds VARCHAR(256),
--                                appSecret VARCHAR(256),
--                                scope VARCHAR(256),
--                                grantTypes VARCHAR(256),
--                                redirectUrl VARCHAR(256),
--                                authorities VARCHAR(256),
--                                access_token_validity INTEGER,
--                                refresh_token_validity INTEGER,
--                                additionalInformation VARCHAR(4096),
--                                autoApproveScopes VARCHAR(256)
-- );