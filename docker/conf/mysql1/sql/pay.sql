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

 Date: 18/07/2021 14:16:09
*/
USE july_v1;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay
-- ----------------------------
DROP TABLE IF EXISTS `pay`;
CREATE TABLE `pay` (
  `pay_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `from_id` varchar(32) DEFAULT NULL,
  `to_id` varchar(32) DEFAULT NULL,
  `from_type` int DEFAULT NULL,
  `to_type` int DEFAULT NULL,
  `amount` decimal(65,0) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_done` datetime(6) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  `leave_message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
