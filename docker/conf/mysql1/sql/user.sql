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

 Date: 24/04/2021 20:32:41
*/
USE july_v1;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` bigint unsigned NOT NULL,
  `user_name` varchar(32) DEFAULT NULL,
  `password` varchar(64) NOT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '1',
  `phone_number` varchar(20) NOT NULL,
  `email` varchar(36) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `gmt_create` datetime NOT NULL,
  `gmt_update` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `gmt_birthday` datetime DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `authority` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
