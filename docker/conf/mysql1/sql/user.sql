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

 Date: 18/07/2021 14:16:43
*/
USE july_v1;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
  `exp` bigint DEFAULT '0',
  `last_login_ip` varchar(255) DEFAULT NULL,
  `identification` varchar(15) DEFAULT NULL,
  `ident_info` varchar(30) DEFAULT NULL,
  `balance` decimal(65,0) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
