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

 Date: 18/07/2021 14:16:32
*/
USE july_v1;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `tag_id` varchar(32) NOT NULL,
  `tag_name` varchar(32) DEFAULT NULL,
  `tag_author_id` varchar(32) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
