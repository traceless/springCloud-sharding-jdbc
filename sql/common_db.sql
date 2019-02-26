/*
 Navicat Premium Data Transfer

 Source Server         : docker-mysql
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : 127.0.0.1
 Source Database       : common_db

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : utf-8

 Date: 02/26/2019 15:38:21 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `db_config`
-- ----------------------------
DROP TABLE IF EXISTS `db_config`;
CREATE TABLE `db_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '数据库名称，最好不要中文',
  `master_url` varchar(255) DEFAULT NULL COMMENT '数据库连接',
  `position` int(3) DEFAULT NULL COMMENT '分库对应的分区的位置，小于1000',
  `slave_url` varchar(255) DEFAULT NULL COMMENT '从数据库连接',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `md5` varchar(32) DEFAULT NULL COMMENT 'MD5 值，用于校验数据库配置是否正确，防止手动改数据库',
  `username` varchar(255) DEFAULT NULL COMMENT '登录名（主从数据库共用）',
  `password` varchar(255) DEFAULT NULL COMMENT '密码（主从数据库共用）',
  `module_code` varchar(32) DEFAULT NULL COMMENT '模块编码，垂直分库，属于哪个模块功能的库',
  `status` varchar(15) DEFAULT NULL COMMENT '状态，生产，扩容中，测试，同步收集',
  `effective_time` timestamp NULL DEFAULT NULL COMMENT '生效日期',
  `hlod1` varchar(255) DEFAULT NULL,
  `hlod2` varchar(255) DEFAULT NULL,
  `hlod3` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `module_code` (`module_code`),
  CONSTRAINT `db_module` FOREIGN KEY (`module_code`) REFERENCES `module_config` (`module_code`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='数据库配置，按模块配置多台数据库';

-- ----------------------------
--  Records of `db_config`
-- ----------------------------
BEGIN;
INSERT INTO `db_config` VALUES ('1', 'user_db_1', 'jdbc:mysql://127.0.0.1:3306/user_db_1?useUnicode=true&characterEncoding=UTF-8&useSSL=false', '0', 'jdbc:mysql://127.0.0.1:3306/user_db_1?useUnicode=true&characterEncoding=UTF-8&useSSL=false', null, null, 'root', '123456', 'user_server', null, null, null, null, null, null, null), ('2', 'user_db_2', 'jdbc:mysql://127.0.0.1:3306/user_db_2?useUnicode=true&characterEncoding=UTF-8&useSSL=false', '500', 'jdbc:mysql://127.0.0.1:3306/user_db_2?useUnicode=true&characterEncoding=UTF-8&useSSL=false', null, null, 'root', '123456', 'user_server', null, null, null, null, null, null, null), ('3', 'payment_db_1', 'jdbc:mysql://127.0.0.1:3306/payment_db_1?useUnicode=true&characterEncoding=UTF-8&useSSL=false', '0', 'jdbc:mysql://127.0.0.1:3306/payment_db_1?useUnicode=true&characterEncoding=UTF-8&useSSL=false', null, null, 'root', '123456', 'payment_server', null, null, null, null, null, null, null), ('4', 'default', 'jdbc:mysql://127.0.0.1:3306/common_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false', '0', 'jdbc:mysql://127.0.0.1:3306/common_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false', null, null, 'root', '123456', 'common_db', null, null, null, null, null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `module_config`
-- ----------------------------
DROP TABLE IF EXISTS `module_config`;
CREATE TABLE `module_config` (
  `id` int(11) NOT NULL,
  `module_code` varchar(32) DEFAULT NULL COMMENT '模块编码',
  `module_name` varchar(32) DEFAULT NULL COMMENT '模块名称',
  `remark` varchar(255) DEFAULT NULL,
  `table_num` int(3) DEFAULT NULL COMMENT '分表的数量，用于求模定位具体的表',
  `hold1` varchar(255) DEFAULT NULL,
  `hlod2` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `module_code` (`module_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模块信息，按模块垂直分库';

-- ----------------------------
--  Records of `module_config`
-- ----------------------------
BEGIN;
INSERT INTO `module_config` VALUES ('1', 'user_server', '用户服务', '系统设置以及用户服务等', '3', null, null, null, null), ('2', 'payment_server', '支付服务', '支付平台', '5', null, null, null, null), ('3', 'common_db', '公共数据库', '公共数据库', '0', null, null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_config`
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='空表，例子';

-- ----------------------------
--  Records of `sys_config`
-- ----------------------------
BEGIN;
INSERT INTO `sys_config` VALUES ('1', 'test', 'test1', '1', '1'), ('2', 'test2', 'test2', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `table_config`
-- ----------------------------
DROP TABLE IF EXISTS `table_config`;
CREATE TABLE `table_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(255) DEFAULT NULL,
  `table_columns` varchar(255) DEFAULT NULL COMMENT '分库字段，column1,column2',
  `module_code` varchar(32) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `hold2` varchar(255) DEFAULT NULL,
  `hold3` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `module_code` (`module_code`),
  CONSTRAINT `table_module` FOREIGN KEY (`module_code`) REFERENCES `module_config` (`module_code`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='表信息，根据模块信息分表';

-- ----------------------------
--  Records of `table_config`
-- ----------------------------
BEGIN;
INSERT INTO `table_config` VALUES ('1', 'ph_user_info', 'user_id,user_name,hash_val', 'user_server', null, null, null, null, null), ('2', 'ph_order_info', 'order_id,user_id,user_name,hash_val', 'user_server', null, null, null, null, null), ('3', 'ph_payment_info', 'payment_id,order_id', 'payment_server', null, null, null, null, null);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
