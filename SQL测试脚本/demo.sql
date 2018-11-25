/*
 Navicat Premium Data Transfer

 Source Server         : 本地服务器
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : test3

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 26/11/2018 01:54:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qm_power
-- ----------------------------
DROP TABLE IF EXISTS `qm_power`;
CREATE TABLE `qm_power`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限表 - 权限id',
  `powerName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qm_power
-- ----------------------------
INSERT INTO `qm_power` VALUES (1, 'demoPower');

-- ----------------------------
-- Table structure for qm_relation
-- ----------------------------
DROP TABLE IF EXISTS `qm_relation`;
CREATE TABLE `qm_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关系表 - 关系id',
  `roleId` int(11) NOT NULL COMMENT '角色id',
  `powerId` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qm_relation
-- ----------------------------
INSERT INTO `qm_relation` VALUES (1, 1, 1);

-- ----------------------------
-- Table structure for qm_role
-- ----------------------------
DROP TABLE IF EXISTS `qm_role`;
CREATE TABLE `qm_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色表 - 角色id',
  `roleName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qm_role
-- ----------------------------
INSERT INTO `qm_role` VALUES (1, '超级管理员');

-- ----------------------------
-- Table structure for qm_user
-- ----------------------------
DROP TABLE IF EXISTS `qm_user`;
CREATE TABLE `qm_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户表 - 用户id',
  `userName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `roleId` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qm_user
-- ----------------------------
INSERT INTO `qm_user` VALUES (1, 'qmyule2018', '123', 1);

SET FOREIGN_KEY_CHECKS = 1;
