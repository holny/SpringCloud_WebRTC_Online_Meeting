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

 Date: 18/07/2021 14:15:42
*/
USE july_v1;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for expert
-- ----------------------------
DROP TABLE IF EXISTS `expert`;
CREATE TABLE `expert` (
  `expert_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pre_note` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `accept_date` varchar(255) DEFAULT NULL,
  `accept_time` varchar(255) DEFAULT NULL,
  `unit_cost` decimal(10,2) DEFAULT NULL,
  `type` int DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `category_main` varchar(32) DEFAULT NULL,
  `category_sub` varchar(32) DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`expert_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
