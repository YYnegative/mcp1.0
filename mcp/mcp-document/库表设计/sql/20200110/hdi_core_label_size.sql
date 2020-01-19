/*
Navicat MySQL Data Transfer

Source Server         : mcp
Source Server Version : 50718
Source Host           : 192.168.1.12:3521
Source Database       : mcp-dev

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2020-01-09 21:39:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hdi_core_label_size
-- ----------------------------
DROP TABLE IF EXISTS `hdi_core_label_size`;
CREATE TABLE "hdi_core_label_size" (
  "id" bigint(20) NOT NULL AUTO_INCREMENT,
  "user_id" bigint(20) NOT NULL COMMENT '用户id',
  "dept_id" int(20) DEFAULT NULL COMMENT '部门id',
  "type_id" int(1) DEFAULT NULL COMMENT '属性类别(0:标签;1:批次码)',
  "qrcode_width" int(20) DEFAULT NULL COMMENT '二维码宽',
  "qrcode_height" int(20) DEFAULT NULL COMMENT '二维码高',
  "pdf_width" double(20,0) DEFAULT NULL COMMENT 'pdf宽',
  "pdf_height" double(20,0) DEFAULT NULL COMMENT 'pdf高',
  "create_id" bigint(20) DEFAULT NULL COMMENT '创建人id',
  "create_name" varchar(100) DEFAULT NULL COMMENT '创建人名称',
  "create_time" datetime(6) DEFAULT NULL COMMENT '创建时间',
  "edit_id" bigint(20) DEFAULT NULL COMMENT '修改人id',
  "edit_name" varchar(100) DEFAULT NULL COMMENT '修改人名称',
  "edit_time" datetime(6) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY ("id")
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

