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

 Date: 18/07/2021 14:15:18
*/
USE july_v1;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for consult
-- ----------------------------
DROP TABLE IF EXISTS `consult`;
CREATE TABLE `consult` (
  `consult_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `customer_id` varchar(32) DEFAULT NULL,
  `expert_id` varchar(32) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `leave_message` varchar(255) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_start` datetime(6) DEFAULT NULL,
  `gmt_start_actual` datetime(6) DEFAULT NULL,
  `gmt_stop_actual` datetime(6) DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `duration_actual` int DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `pay_id` varchar(32) DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`consult_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
