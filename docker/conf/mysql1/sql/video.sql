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

 Date: 18/07/2021 14:16:57
*/
USE july_v1;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `video_id` varchar(32) NOT NULL,
  `author_id` varchar(32) DEFAULT NULL,
  `series_id` varchar(32) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `category_main` varchar(32) DEFAULT NULL,
  `category_sub` varchar(32) DEFAULT NULL,
  `is_top` int DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_update` datetime(6) DEFAULT NULL,
  `gmt_push` datetime(6) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `video_time_duration` bigint DEFAULT NULL,
  `video_poster_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `video_file_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `video_meta` varchar(255) DEFAULT NULL,
  `video_ratio` varchar(255) DEFAULT NULL,
  `thumb` bigint DEFAULT NULL,
  `score` float(2,0) DEFAULT NULL,
  `view` bigint DEFAULT NULL,
  `video_format` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`video_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
