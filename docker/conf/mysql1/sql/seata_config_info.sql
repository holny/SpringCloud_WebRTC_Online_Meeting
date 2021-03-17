/*
 Navicat Premium Data Transfer

 Source Server         : July_docker_mysql8_root
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3305
 Source Schema         : nacos_config

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 12/03/2021 09:55:42
*/
USE nacos_config;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=427 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` VALUES (342, 'transport.type', 'SEATA_GROUP', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2021-03-11 19:55:21', '2021-03-11 19:55:21', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (343, 'transport.server', 'SEATA_GROUP', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2021-03-11 19:55:22', '2021-03-11 19:55:22', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (344, 'transport.heartbeat', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2021-03-11 19:55:22', '2021-03-11 19:55:22', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (345, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-03-11 19:55:22', '2021-03-11 19:55:22', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (346, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2021-03-11 19:55:22', '2021-03-11 19:55:22', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (347, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2021-03-11 19:55:22', '2021-03-11 19:55:22', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (348, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2021-03-11 19:55:22', '2021-03-11 19:55:22', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (349, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-03-11 19:55:23', '2021-03-11 19:55:23', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (350, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2021-03-11 19:55:23', '2021-03-11 19:55:23', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (351, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2021-03-11 19:55:23', '2021-03-11 19:55:23', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (352, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2021-03-11 19:55:23', '2021-03-11 19:55:23', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (353, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2021-03-11 19:55:23', '2021-03-11 19:55:23', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (354, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2021-03-11 19:55:23', '2021-03-11 19:55:23', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (355, 'transport.shutdown.wait', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2021-03-11 19:55:23', '2021-03-11 19:55:23', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (356, 'service.vgroupMapping.july-tx-service-group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2021-03-11 19:55:23', '2021-03-11 19:55:23', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (357, 'service.default.grouplist', 'SEATA_GROUP', '127.0.0.1:8092', '30f452b2689f764de6a48b5212cc6616', '2021-03-11 19:55:23', '2021-03-11 19:55:23', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (358, 'service.enableDegrade', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-03-11 19:55:23', '2021-03-11 19:55:23', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (359, 'service.disableGlobalTransaction', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-03-11 19:55:24', '2021-03-11 19:55:24', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (360, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '10000', 'b7a782741f667201b54880c925faec4b', '2021-03-11 19:55:24', '2021-03-11 19:55:24', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (361, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2021-03-11 19:55:24', '2021-03-11 19:55:24', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (362, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2021-03-11 19:55:24', '2021-03-11 19:55:24', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (363, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2021-03-11 19:55:24', '2021-03-11 19:55:24', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (364, 'client.rm.reportRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2021-03-11 19:55:24', '2021-03-11 19:55:24', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (365, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-03-11 19:55:24', '2021-03-11 19:55:24', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (366, 'client.rm.tableMetaCheckerInterval', 'SEATA_GROUP', '60000', '2b4226dd7ed6eb2d419b881f3ae9c97c', '2021-03-11 19:55:24', '2021-03-11 19:55:24', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (367, 'client.rm.sqlParserType', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2021-03-11 19:55:24', '2021-03-11 19:55:24', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (368, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-03-11 19:55:24', '2021-03-11 19:55:24', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (369, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-03-11 19:55:25', '2021-03-11 19:55:25', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (370, 'client.tm.commitRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2021-03-11 19:55:25', '2021-03-11 19:55:25', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (371, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2021-03-11 19:55:25', '2021-03-11 19:55:25', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (372, 'client.tm.defaultGlobalTransactionTimeout', 'SEATA_GROUP', '60000', '2b4226dd7ed6eb2d419b881f3ae9c97c', '2021-03-11 19:55:25', '2021-03-11 19:55:25', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (373, 'client.tm.degradeCheck', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-03-11 19:55:25', '2021-03-11 19:55:25', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (374, 'client.tm.degradeCheckAllowTimes', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2021-03-11 19:55:25', '2021-03-11 19:55:25', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (375, 'client.tm.degradeCheckPeriod', 'SEATA_GROUP', '2000', '08f90c1a417155361a5c4b8d297e0d78', '2021-03-11 19:55:25', '2021-03-11 19:55:25', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (376, 'store.mode', 'SEATA_GROUP', 'db', 'd77d5e503ad1439f585ac494268b351b', '2021-03-11 19:55:25', '2021-03-11 19:55:25', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (377, 'store.file.dir', 'SEATA_GROUP', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2', '2021-03-11 19:55:26', '2021-03-11 19:55:26', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (378, 'store.file.maxBranchSessionSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2021-03-11 19:55:26', '2021-03-11 19:55:26', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (379, 'store.file.maxGlobalSessionSize', 'SEATA_GROUP', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f', '2021-03-11 19:55:26', '2021-03-11 19:55:26', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (380, 'store.file.fileWriteBufferCacheSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2021-03-11 19:55:26', '2021-03-11 19:55:26', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (381, 'store.file.flushDiskMode', 'SEATA_GROUP', 'async', '0df93e34273b367bb63bad28c94c78d5', '2021-03-11 19:55:26', '2021-03-11 19:55:26', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (382, 'store.file.sessionReloadReadSize', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-03-11 19:55:26', '2021-03-11 19:55:26', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (383, 'store.db.datasource', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2021-03-11 19:55:26', '2021-03-11 19:55:26', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (384, 'store.db.dbType', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2021-03-11 19:55:26', '2021-03-11 19:55:26', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (385, 'store.db.driverClassName', 'SEATA_GROUP', 'com.mysql.cj.jdbc.Driver', '33763409bb7f4838bde4fae9540433e4', '2021-03-11 19:55:26', '2021-03-11 19:55:26', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (386, 'store.db.url', 'SEATA_GROUP', 'jdbc:mysql://127.0.0.1:3305/seata?useUnicode=true&rewriteBatchedStatements=true', '735b83c01fe18a54bc35b09d27729a4c', '2021-03-11 19:55:26', '2021-03-11 19:55:26', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (387, 'store.db.user', 'SEATA_GROUP', 'july_seata', 'ec4811d59fedacd2e80189a27913c93e', '2021-03-11 19:55:27', '2021-03-11 19:55:27', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (388, 'store.db.password', 'SEATA_GROUP', 'julyataes1234', '4c98161455e0039d2d0f1499d193ebed', '2021-03-11 19:55:27', '2021-03-11 19:55:27', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (389, 'store.db.minConn', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2021-03-11 19:55:27', '2021-03-11 19:55:27', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (390, 'store.db.maxConn', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2021-03-11 19:55:27', '2021-03-11 19:55:27', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (391, 'store.db.globalTable', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2021-03-11 19:55:27', '2021-03-11 19:55:27', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (392, 'store.db.branchTable', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2021-03-11 19:55:27', '2021-03-11 19:55:27', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (393, 'store.db.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-03-11 19:55:27', '2021-03-11 19:55:27', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (394, 'store.db.lockTable', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2021-03-11 19:55:27', '2021-03-11 19:55:27', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (395, 'store.db.maxWait', 'SEATA_GROUP', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2021-03-11 19:55:27', '2021-03-11 19:55:27', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (396, 'store.redis.mode', 'SEATA_GROUP', 'single', 'dd5c07036f2975ff4bce568b6511d3bc', '2021-03-11 19:55:27', '2021-03-11 19:55:27', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (397, 'store.redis.single.host', 'SEATA_GROUP', '127.0.0.1', 'f528764d624db129b32c21fbca0cb8d6', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (398, 'store.redis.single.port', 'SEATA_GROUP', '6379', '92c3b916311a5517d9290576e3ea37ad', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (399, 'store.redis.maxConn', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (400, 'store.redis.minConn', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (401, 'store.redis.maxTotal', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (402, 'store.redis.database', 'SEATA_GROUP', '0', 'cfcd208495d565ef66e7dff9f98764da', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (403, 'store.redis.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (404, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (405, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (406, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (407, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (408, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2021-03-11 19:55:28', '2021-03-11 19:55:28', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (409, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (410, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (411, 'client.undo.dataValidation', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (412, 'client.undo.logSerialization', 'SEATA_GROUP', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (413, 'client.undo.onlyCareUpdateColumns', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (414, 'server.undo.logSaveDays', 'SEATA_GROUP', '7', '8f14e45fceea167a5a36dedd4bea2543', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (415, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (416, 'client.undo.logTable', 'SEATA_GROUP', 'undo_log', '2842d229c24afe9e61437135e8306614', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (417, 'client.undo.compress.enable', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (418, 'client.undo.compress.type', 'SEATA_GROUP', 'zip', 'adcdbd79a8d84175c229b192aadc02f2', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (419, 'client.undo.compress.threshold', 'SEATA_GROUP', '64k', 'bd44a6458bdbff0b5cac721ba361f035', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (420, 'log.exceptionRate', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (421, 'transport.serialization', 'SEATA_GROUP', 'seata', 'b943081c423b9a5416a706524ee05d40', '2021-03-11 19:55:29', '2021-03-11 19:55:29', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (422, 'transport.compressor', 'SEATA_GROUP', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2021-03-11 19:55:30', '2021-03-11 19:55:30', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (423, 'metrics.enabled', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-03-11 19:55:30', '2021-03-11 19:55:30', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (424, 'metrics.registryType', 'SEATA_GROUP', 'compact', '7cf74ca49c304df8150205fc915cd465', '2021-03-11 19:55:30', '2021-03-11 19:55:30', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (425, 'metrics.exporterList', 'SEATA_GROUP', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2021-03-11 19:55:30', '2021-03-11 19:55:30', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (426, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2021-03-11 19:55:30', '2021-03-11 19:55:30', NULL, '172.20.1.1', '', '', NULL, NULL, NULL, 'text', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
