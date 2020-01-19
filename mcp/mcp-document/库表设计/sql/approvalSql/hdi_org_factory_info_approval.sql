/*
Navicat MySQL Data Transfer

Source Server         : mcp
Source Server Version : 50718
Source Host           : 192.168.1.12:3521
Source Database       : mcp-dev

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2019-11-21 17:50:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hdi_org_factory_info_approval
-- ----------------------------
DROP TABLE IF EXISTS `hdi_org_factory_info_approval`;
CREATE TABLE "hdi_org_factory_info_approval" (
  "id" bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  "sources_id" varchar(64) DEFAULT NULL COMMENT '原系统厂商id',
  "factory_code" varchar(128) DEFAULT NULL COMMENT '厂商编码',
  "factory_name" varchar(128) NOT NULL COMMENT '厂商名称',
  "credit_code" varchar(64) DEFAULT NULL COMMENT '统一社会信用代码',
  "country_code" varchar(64) DEFAULT NULL COMMENT '所在国家编码',
  "province_code" varchar(64) DEFAULT NULL COMMENT '所在省编码',
  "city_code" varchar(64) DEFAULT NULL COMMENT '所在市编码',
  "area_code" varchar(64) DEFAULT NULL COMMENT '所在区县编码',
  "factory_address" varchar(256) DEFAULT NULL COMMENT '厂商地址',
  "status" int(1) NOT NULL COMMENT '状态(-1:待审批;0:停用;1:启用)',
  "corporate" varchar(64) DEFAULT NULL COMMENT '法人代表',
  "phone" varchar(32) DEFAULT NULL COMMENT '联系电话',
  "email" varchar(64) DEFAULT NULL COMMENT '邮箱地址',
  "fax" varchar(64) DEFAULT NULL COMMENT '传真',
  "dept_id" bigint(20) DEFAULT NULL COMMENT '所属机构',
  "create_id" bigint(20) DEFAULT NULL COMMENT '创建人id',
  "create_time" datetime DEFAULT NULL COMMENT '创建时间',
  "edit_id" bigint(20) DEFAULT NULL COMMENT '修改人id',
  "edit_time" datetime DEFAULT NULL COMMENT '修改时间',
  "del_flag" int(1) DEFAULT '0' COMMENT '是否删除(-1:已删除;0:正常)',
  "data_source" int(1) DEFAULT '0' COMMENT '数据来源(0:系统录入;1:医院SPD)',
  "check_status" int(1) DEFAULT NULL COMMENT '审批状态 (0:待审批;1:审批通过;2:审批不通过)',
  "process_id" varchar(64) DEFAULT NULL COMMENT '流程实例id',
  PRIMARY KEY ("id")
) ENGINE=InnoDB AUTO_INCREMENT=1267 DEFAULT CHARSET=utf8 COMMENT='厂商信息待审批记录表';
