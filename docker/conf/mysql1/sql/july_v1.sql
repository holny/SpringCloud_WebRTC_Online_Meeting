/*
 Navicat Premium Data Transfer

 Source Server         : July_docker_mysql8_main
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3305
 Source Schema         : july_v1

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 23/06/2021 17:29:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for container
-- ----------------------------
DROP TABLE IF EXISTS `container`;
CREATE TABLE `container` (
  `container_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `container_type` int NOT NULL,
  `content_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content_type` int NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`container_id`,`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `msg_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `label` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `gmt_start` datetime NOT NULL,
  `gmt_end` datetime DEFAULT NULL,
  `message` mediumblob,
  `participant` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `status` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`gmt_start`,`participant`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for relation
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
  `rel_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `peer_id` varchar(32) NOT NULL,
  `remark_name` varchar(100) DEFAULT NULL,
  `peer_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `rel_type` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`user_id`,`peer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` varchar(32) NOT NULL,
  `user_name` varchar(32) DEFAULT NULL,
  `password` varchar(64) NOT NULL,
  `gender` tinyint NOT NULL DEFAULT '1',
  `phone_number` varchar(20) NOT NULL,
  `email` varchar(36) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `gmt_create` datetime NOT NULL,
  `gmt_update` datetime DEFAULT NULL,
  `gmt_last_login` datetime DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `gmt_birthday` datetime DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `authority` varchar(1200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `level` varchar(32) DEFAULT '0',
  `last_login_ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
