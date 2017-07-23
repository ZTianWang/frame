/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50630
Source Host           : localhost:3306
Source Database       : frame

Target Server Type    : MYSQL
Target Server Version : 50630
File Encoding         : 65001

Date: 2016-06-18 21:13:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` char(32) NOT NULL,
  `account` varchar(15) NOT NULL,
  `password` varchar(32) NOT NULL,
  `cell_phone` varchar(15) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `content` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_unique_account` (`account`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('4a629d4f3d2146aba54ac69c250d8961', 'admin', '14e1b600b1fd579f47433b88e8d85291', '15801187545', 'mrlixiaopeng@163.com', '2016-04-09 21:01:42', '');
INSERT INTO `account` VALUES ('90575b95489f4ae39ba7f5b4c5d85dba', '1111111111', '737207bfff986b451956db85a7c8d380', '15801187545', '15801187545@qq.com', '2016-06-18 19:00:21', '');
INSERT INTO `account` VALUES ('d18d9b39a0c14b02a2ede8334dec12cc', '111', '3049a1f0f1c808cdaa4fbed0e01649b1', '15878978978', '15878978978@qq.com', '2016-06-03 11:53:01', '');

-- ----------------------------
-- Table structure for `account_role`
-- ----------------------------
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE `account_role` (
  `id` char(32) NOT NULL,
  `account_id` char(32) NOT NULL,
  `role_id` char(32) NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_account_role_account_id` (`account_id`),
  KEY `fk_account_role_role_id` (`role_id`),
  CONSTRAINT `fk_account_role_account_id` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `fk_account_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account_role
-- ----------------------------
INSERT INTO `account_role` VALUES ('556c5ec952ee48fd9f12c7c37513ffa3', 'd18d9b39a0c14b02a2ede8334dec12cc', '7b71ef26faa84c4ab0d8df57c4ed9043', '2016-06-03 11:53:01');
INSERT INTO `account_role` VALUES ('a804b4eb7be34d6ca5b51156397790a2', '4a629d4f3d2146aba54ac69c250d8961', 'dd8f999945bc493f9e13455527e6f6f2', '2016-04-25 15:17:45');
INSERT INTO `account_role` VALUES ('b676313fda304e0abce39db1dfe70cf5', '4a629d4f3d2146aba54ac69c250d8961', '7b71ef26faa84c4ab0d8df57c4ed9043', '2016-04-25 15:18:23');
INSERT INTO `account_role` VALUES ('d3c666adc6dc4a339e16c99e2b9cfa6d', 'd18d9b39a0c14b02a2ede8334dec12cc', 'dd8f999945bc493f9e13455527e6f6f2', '2016-06-03 11:53:01');

-- ----------------------------
-- Table structure for `attachment`
-- ----------------------------
DROP TABLE IF EXISTS `attachment`;
CREATE TABLE `attachment` (
  `id` char(32) NOT NULL,
  `file_unique_id` char(45) NOT NULL COMMENT '文件的唯一名称',
  `record_id` char(32) DEFAULT NULL COMMENT '附件属于哪条记录，那么此处就是那条记录的id',
  `file_name` varchar(255) NOT NULL COMMENT '文件名',
  `file_type` tinyint(2) NOT NULL COMMENT '文件类型',
  `file_suffix` varchar(6) NOT NULL COMMENT '文件后缀',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小',
  `downloads` int(11) NOT NULL COMMENT '下载次数',
  `create_date` datetime NOT NULL,
  `account_id` char(32) NOT NULL COMMENT '上传人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of attachment
-- ----------------------------

-- ----------------------------
-- Table structure for `menu`
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` char(32) NOT NULL,
  `name` varchar(15) NOT NULL COMMENT '菜单名',
  `url` varchar(20) DEFAULT NULL COMMENT 'url路径',
  `parent_id` char(32) DEFAULT NULL COMMENT '上级id',
  `icon` varchar(20) DEFAULT NULL,
  `sort_num` int(11) NOT NULL DEFAULT '0',
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_menu_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('menu0a7500b14502829e822de33619c1', '权限设置', null, null, 'icon-indent-left', '2', '2016-04-25 22:04:56');
INSERT INTO `menu` VALUES ('menu0a7500b14502829e822de33619cf', '系统设置', null, null, 'icon-tasks', '1', '2016-04-25 15:20:31');
INSERT INTO `menu` VALUES ('menu15355b6a4becbe93ce85a659f3c0', '店铺管理', null, 'menu0a7500b14502829e822de33619cf', null, '2', '2016-04-25 19:50:24');
INSERT INTO `menu` VALUES ('menu15355b6a4becbe93ce85a659f3d1', '角色管理', 'role/manager', 'menu0a7500b14502829e822de33619c1', null, '1', '2016-04-25 22:08:32');
INSERT INTO `menu` VALUES ('menu15355b6a4becbe93ce85a666f3c2', '店铺管理2', 'shop/manager2', 'menu15355b6a4becbe93ce85a659f3c0', null, '2', '2016-05-17 15:27:59');
INSERT INTO `menu` VALUES ('menu15355b6a4becbe93ce85a666f3d1', '店铺管理1', 'shop/manager1', 'menu15355b6a4becbe93ce85a659f3c0', null, '1', '2016-05-17 15:27:15');
INSERT INTO `menu` VALUES ('menuff9657ad4cfd8c520eeef43d75bf', '账号管理', 'account/manager', 'menu0a7500b14502829e822de33619cf', null, '1', '2016-04-25 15:49:25');

-- ----------------------------
-- Table structure for `menu_function`
-- ----------------------------
DROP TABLE IF EXISTS `menu_function`;
CREATE TABLE `menu_function` (
  `id` char(32) NOT NULL,
  `menu_id` char(32) NOT NULL,
  `name` varchar(10) NOT NULL COMMENT '按钮名字',
  `flag` varchar(25) NOT NULL COMMENT '英文标示，页面据此绑定事件',
  `url` varchar(66) NOT NULL COMMENT '请求的url',
  `type` varchar(8) NOT NULL COMMENT '请求的http类型',
  `sort_num` int(11) DEFAULT '0',
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_function_name` (`name`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu_function
-- ----------------------------
INSERT INTO `menu_function` VALUES ('fun215355b6a4becbe93ce85a659f888', 'menu15355b6a4becbe93ce85a659f3d1', '查看菜单', 'showMenus', '/role/menusfunctions/{roleId}', 'GET', '1', '2016-06-01 11:45:32');
INSERT INTO `menu_function` VALUES ('fun215355b6a4becbe93ce85a66623cf', 'menu15355b6a4becbe93ce85a666f3c2', '列表', 'page', '/shop2/page', 'GET', '0', '2016-05-27 14:20:48');
INSERT INTO `menu_function` VALUES ('fun215355b6a4becbe93ce8iii59f3d1', 'menu15355b6a4becbe93ce85a659f3d1', '编辑菜单', 'editMenus', '/role/functions/{roleId}', 'PUT', '1', '2016-06-01 11:44:56');
INSERT INTO `menu_function` VALUES ('fun472c6aa6a4b6ca7bf16884aa97a4f', 'menuff9657ad4cfd8c520eeef43d75bf', '编辑', 'edit', '/account/{id}', 'PUT', '4', '2016-04-25 15:59:34');
INSERT INTO `menu_function` VALUES ('fun472c6aa6a4b6ca7bf16884aa97a66', 'menuff9657ad4cfd8c520eeef43d75bf', '审核', 'audit', '/account/audit/{id}', 'PATCH', '1', '2016-04-26 11:08:29');
INSERT INTO `menu_function` VALUES ('fun5924b2a7146b395fd37a453fc1911', 'menu15355b6a4becbe93ce85a666f3d1', '审核', 'audit', '/shop/audit/{id}', 'PUT', '2', '2016-04-26 11:06:15');
INSERT INTO `menu_function` VALUES ('fun5924b2a7146b395fd37a453fc1922', 'menu15355b6a4becbe93ce85a666f3d1', '新增', 'create', '/shop', 'POST', '1', '2016-04-26 11:07:00');
INSERT INTO `menu_function` VALUES ('fun5924b2a7146b395fd37a453fc1933', 'menuff9657ad4cfd8c520eeef43d75bf', '查看', 'show', '/account/{id}', 'GET', '1', '2016-05-09 16:49:30');
INSERT INTO `menu_function` VALUES ('fun5924b2a7146b395fd37a453fc1941', 'menuff9657ad4cfd8c520eeef43d75bf', '列表', 'page', '/account/page', 'GET', '0', '2016-04-26 08:40:49');
INSERT INTO `menu_function` VALUES ('fun5924b2a7146b395fd37a453fc194d', 'menuff9657ad4cfd8c520eeef43d75bf', '新增', 'create', '/account', 'POST', '3', '2016-04-25 15:37:07');
INSERT INTO `menu_function` VALUES ('fun666665b6a4becbe93ce85a659f301', 'menu15355b6a4becbe93ce85a659f3d1', '列表', 'page', '/role/page', 'GET', '0', '2016-05-24 19:48:34');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` char(32) NOT NULL,
  `name` varchar(15) NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('7b71ef26faa84c4ab0d8df57c4ed9043', '测试', '2016-04-25 15:15:16');
INSERT INTO `role` VALUES ('dd8f999945bc493f9e13455527e6f6f2', '管理员', '2016-04-25 15:14:46');

-- ----------------------------
-- Table structure for `role_function`
-- ----------------------------
DROP TABLE IF EXISTS `role_function`;
CREATE TABLE `role_function` (
  `id` char(32) NOT NULL,
  `role_id` char(32) NOT NULL,
  `function_id` char(32) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_r_m_f` (`role_id`,`function_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_function
-- ----------------------------
INSERT INTO `role_function` VALUES ('0c40d52246a0445a883e7fe285f96abd', '7b71ef26faa84c4ab0d8df57c4ed9043', 'fun666665b6a4becbe93ce85a659f301', '2016-06-03 11:49:12');
INSERT INTO `role_function` VALUES ('1b4ab0302ce44f659042d3ebb0121f44', 'dd8f999945bc493f9e13455527e6f6f2', 'fun5924b2a7146b395fd37a453fc1911', '2016-06-01 12:47:34');
INSERT INTO `role_function` VALUES ('2c8215355b6a4becbe93ce85a659f888', 'dd8f999945bc493f9e13455527e6f6f2', 'fun215355b6a4becbe93ce85a659f888', '2016-06-01 11:47:38');
INSERT INTO `role_function` VALUES ('5a69bbd9c01a40eeafc6be70a5b5dd05', '7b71ef26faa84c4ab0d8df57c4ed9043', 'fun215355b6a4becbe93ce85a66623cf', '2016-06-03 11:52:42');
INSERT INTO `role_function` VALUES ('6bedaba8d6cf4d7c86f0ea8cbb59ad6b', '7b71ef26faa84c4ab0d8df57c4ed9043', 'fun5924b2a7146b395fd37a453fc1941', '2016-06-01 12:49:13');
INSERT INTO `role_function` VALUES ('82983cdb80c54957870047509878e67c', 'dd8f999945bc493f9e13455527e6f6f2', 'fun472c6aa6a4b6ca7bf16884aa97a66', '2016-04-25 19:51:37');
INSERT INTO `role_function` VALUES ('a5f5ed2057c644c087c6556cfb67fa91', '7b71ef26faa84c4ab0d8df57c4ed9043', 'fun5924b2a7146b395fd37a453fc1933', '2016-06-01 12:48:56');
INSERT INTO `role_function` VALUES ('b0e3221d56a147779885a3524a45b6a4', 'dd8f999945bc493f9e13455527e6f6f2', 'fun666665b6a4becbe93ce85a659f301', '2016-05-24 19:49:04');
INSERT INTO `role_function` VALUES ('b0e3221d56a147779885a3524a461c1b', 'dd8f999945bc493f9e13455527e6f6f2', 'fun472c6aa6a4b6ca7bf16884aa97a4f', '2016-04-25 15:24:29');
INSERT INTO `role_function` VALUES ('b0e3221d56a148879885a3524a256d2b', 'dd8f999945bc493f9e13455527e6f6f2', 'fun5924b2a7146b395fd37a453fc194d', '2016-05-17 15:41:20');
INSERT INTO `role_function` VALUES ('b0e3221d56a148879885a3524a461c1a', 'dd8f999945bc493f9e13455527e6f6f2', 'fun5924b2a7146b395fd37a453fc1941', '2016-04-25 15:24:29');
INSERT INTO `role_function` VALUES ('b0e3221d56a148879885a3524a461d2b', 'dd8f999945bc493f9e13455527e6f6f2', 'fun5924b2a7146b395fd37a453fc1933', '2016-05-17 15:40:42');
INSERT INTO `role_function` VALUES ('b1d57660f2df4b2aa22f44fdc078dce3', 'dd8f999945bc493f9e13455527e6f6f2', 'fun5924b2a7146b395fd37a453fc1922', '2016-04-25 15:51:15');
INSERT INTO `role_function` VALUES ('bf7a6b22a78d40e0b1bc4f44cf91123c', '7b71ef26faa84c4ab0d8df57c4ed9043', 'fun472c6aa6a4b6ca7bf16884aa97a66', '2016-06-02 09:16:54');
INSERT INTO `role_function` VALUES ('d9c255ba8334412dbccc5a1730656c1d', '7b71ef26faa84c4ab0d8df57c4ed9043', 'fun5924b2a7146b395fd37a453fc194d', '2016-06-01 12:49:13');
INSERT INTO `role_function` VALUES ('dd8f999945bc493f9e13455527e777', 'dd8f999945bc493f9e13455527e6f6f2', 'fun215355b6a4becbe93ce85a66623cf', '2016-05-27 14:22:23');
INSERT INTO `role_function` VALUES ('e08f06c2f01e4382a4d1cc0ee0da8c02', 'dd8f999945bc493f9e13455527e6f6f2', 'fun215355b6a4becbe93ce8iii59f3d1', '2016-06-01 12:46:31');
