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

 Date: 18/07/2021 14:16:17
*/
USE july_v1;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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

SET FOREIGN_KEY_CHECKS = 1;
