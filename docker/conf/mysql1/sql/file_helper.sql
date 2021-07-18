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

 Date: 18/07/2021 14:15:53
*/
USE july_v1;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_helper
-- ----------------------------
DROP TABLE IF EXISTS `file_helper`;
CREATE TABLE `file_helper` (
  `file_id` varchar(32) NOT NULL,
  `uploader_id` varchar(32) DEFAULT NULL,
  `type` int DEFAULT NULL,
  `save_path` varchar(255) DEFAULT NULL,
  `file_name` varchar(100) DEFAULT NULL,
  `file_suffix` varchar(12) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_update` datetime(6) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `server_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `server_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `file_size` bigint DEFAULT NULL,
  `file_md5` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
