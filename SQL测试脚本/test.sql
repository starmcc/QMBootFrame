/*
 Navicat Premium Data Transfer

 Source Server         : 本地服务器
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 09/12/2018 23:18:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qm_logger
-- ----------------------------
DROP TABLE IF EXISTS `qm_logger`;
CREATE TABLE `qm_logger`  (
  `logId` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `operator` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作者',
  `text` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日志',
  `requestURL` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求URL',
  `requestClassName` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求类',
  `requestMethod` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求方法',
  `requestParam` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求值',
  `createTime` datetime(0) NULL DEFAULT NULL COMMENT '记录时间',
  `responseTime` bigint(20) NOT NULL COMMENT '响应时间',
  PRIMARY KEY (`logId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for qm_relation
-- ----------------------------
DROP TABLE IF EXISTS `qm_relation`;
CREATE TABLE `qm_relation`  (
  `id` int(11) NOT NULL COMMENT '序列',
  `roleId` int(11) NOT NULL COMMENT '角色id',
  `authorityKey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限key',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of qm_relation
-- ----------------------------
INSERT INTO `qm_relation` VALUES (1, 2, 'show');

-- ----------------------------
-- Table structure for qm_role
-- ----------------------------
DROP TABLE IF EXISTS `qm_role`;
CREATE TABLE `qm_role`  (
  `roleId` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `roleName` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名',
  `createTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `createOperator` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建者',
  `updateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `updateOperator` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新者',
  PRIMARY KEY (`roleId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of qm_role
-- ----------------------------
INSERT INTO `qm_role` VALUES (2, '管理员', '2018-12-09 21:38:29', 'admin', '2018-12-09 21:38:44', 'admin');
INSERT INTO `qm_role` VALUES (3, '游客', '2018-12-09 21:40:25', 'admin', '2018-12-09 21:40:33', 'admin');

-- ----------------------------
-- Table structure for qm_user
-- ----------------------------
DROP TABLE IF EXISTS `qm_user`;
CREATE TABLE `qm_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `password` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `roleId` int(2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qm_user
-- ----------------------------
INSERT INTO `qm_user` VALUES (1, 'admin', 'admin', 1);
INSERT INTO `qm_user` VALUES (2, 'admin2', 'admin2', 2);
INSERT INTO `qm_user` VALUES (3, 'admin3', 'admin3', 3);

SET FOREIGN_KEY_CHECKS = 1;
