/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/9/16 17:24:47                           */
/*==============================================================*/


drop table if exists hdi_unicode_consumables_cate;

drop table if exists hdi_unicode_drugs_cate;

drop table if exists hdi_unicode_goods_ship;

drop table if exists hdi_unicode_goods_ship_hist;

drop table if exists hdi_unicode_reagent_cate;

drop table if exists hdi_unicode_supply_ship;

/*==============================================================*/
/* Table: hdi_unicode_consumables_cate                          */
/*==============================================================*/
create table hdi_unicode_consumables_cate
(
   cate_id              bigint(20) not null auto_increment comment '分类id',
   cate_name            varchar(30) comment '分类名称',
   cate_no              varchar(30) comment '分类编码',
   level_order          bigint(10) comment '层级排序',
   cate_level           int(4) comment '根节点为1，第一层，依次往下',
   pcate_id             bigint(20) comment '父分类id',
   dept_id              bigint(20) comment '所属机构id',
   memo                 varchar(200) comment '备注',
   del_flag             int(1) comment '是否删除(-1:已删除;0:正常)',
   primary key (cate_id)
);

alter table hdi_unicode_consumables_cate comment '耗材分类信息';

/*==============================================================*/
/* Table: hdi_unicode_drugs_cate                                */
/*==============================================================*/
create table hdi_unicode_drugs_cate
(
   cate_id              bigint(20) not null auto_increment comment '分类id',
   cate_name            varchar(30) comment '分类名称',
   cate_no              varchar(30) comment '分类编码',
   level_order          bigint(10) comment '层级排序',
   cate_level           int(4) comment '根节点为1，第一层，依次往下',
   pcate_id             bigint(20) comment '父分类id',
   dept_id              bigint(20) comment '所属机构id',
   memo                 varchar(200) comment '备注',
   del_flag             int(1) comment '是否删除(-1:已删除;0:正常)',
   primary key (cate_id)
);

alter table hdi_unicode_drugs_cate comment '药品分类信息';

/*==============================================================*/
/* Table: hdi_unicode_goods_ship                                */
/*==============================================================*/
create table hdi_unicode_goods_ship
(
   ship_id              bigint(20) not null auto_increment comment '关系id',
   dept_id              bigint(20) comment '所属机构id',
   torg_id              bigint(20) comment '目标机构标识',
   torg_type            int(4) comment '目标机构类型(0:医院;1:供应商)',
   tgoods_type          int(4) comment '商品类型(1:药品;2:试剂;3:耗材)',
   tgoods_id            bigint(20) comment '目标商品id',
   pgoods_id            bigint(20) comment '平台商品id',
   tspecs_id            bigint(20) comment '目标规格id',
   pspecs_id            bigint(20) comment '平台规格id',
   tapproval_id         bigint(20) comment '目标商品批文id',
   papproval_id         bigint(20) comment '平台商品批文id',
   ship_flag            int(1) comment '是否匹对(0:未匹配,1:已匹配)',
   check_status         int(1) comment '审核状态(0:未审核,1:已审核)',
   del_flag             int(1) comment '是否删除(0:否,1:是)',
   cremanid             bigint(20) comment '创建人id',
   cremanname           varchar(30) comment '创建人姓名',
   credate              datetime comment '创建时间',
   editmanid            bigint(20) comment '修改人id',
   editmanname          varchar(30) comment '修改人姓名',
   editdate             datetime comment '修改时间',
   memo                 varchar(200) comment '备注',
   primary key (ship_id)
);

alter table hdi_unicode_goods_ship comment '商品匹配关系';

/*==============================================================*/
/* Table: hdi_unicode_goods_ship_hist                           */
/*==============================================================*/
create table hdi_unicode_goods_ship_hist
(
   shiphist_id          bigint(20) not null auto_increment comment '历史记录id',
   ship_id              bigint(20) comment '关系id',
   dept_id              bigint(20) comment '所属机构id',
   torg_id              bigint(20) comment '目标机构标识',
   torg_type            int(4) comment '目标机构类型(0:医院;1:供应商)',
   tgoods_type          int(4) comment '商品类型(1:药品;2:试剂;3:耗材)',
   tgoods_id            bigint(20) comment '目标商品id',
   pgoods_id            bigint(20) comment '平台商品id',
   tspecs_id            bigint(20) comment '目标规格id',
   pspecs_id            bigint(20) comment '平台规格id',
   tapproval_id         bigint(20) comment '目标商品批文id',
   papproval_id         bigint(20) comment '平台商品批文id',
   ship_flag            int(1) comment '是否匹对(0:未匹配,1:已匹配)',
   check_status         int(1) comment '审核状态(0:未审核,1:已审核)',
   oper_type            int(1) comment '操作类型(1:匹对,2:商品信息变更)',
   del_flag             int(1) comment '是否删除(0:否,1:是)',
   cremanid             bigint(20) comment '创建人id',
   cremanname           varchar(30) comment '创建人姓名',
   credate              datetime comment '创建时间',
   editmanid            bigint(20) comment '修改人id',
   editmanname          varchar(30) comment '修改人姓名',
   editdate             datetime comment '修改时间',
   memo                 varchar(200) comment '备注',
   primary key (shiphist_id)
);

alter table hdi_unicode_goods_ship_hist comment '商品匹配关系历史记录';

/*==============================================================*/
/* Table: hdi_unicode_reagent_cate                              */
/*==============================================================*/
create table hdi_unicode_reagent_cate
(
   cate_id              bigint(20) not null auto_increment comment '分类id',
   cate_name            varchar(30) comment '分类名称',
   cate_no              varchar(30) comment '分类编码',
   level_order          bigint(10) comment '层级排序',
   cate_level           int(4) comment '根节点为1，第一层，依次往下',
   pcate_id             bigint(20) comment '父分类id',
   dept_id              bigint(20) comment '所属机构id',
   memo                 varchar(200) comment '备注',
   del_flag             int(1) comment '是否删除(-1:已删除;0:正常)',
   primary key (cate_id)
);

alter table hdi_unicode_reagent_cate comment '试剂分类信息';

/*==============================================================*/
/* Table: hdi_unicode_supply_ship                               */
/*==============================================================*/
create table hdi_unicode_supply_ship
(
   ship_id              bigint(20) not null auto_increment comment '关系id',
   dept_id              bigint(20) comment '部门id',
   supplier_hospital_ref_id bigint(20) comment '平台医院供应商关系id',
   hospital_id          bigint(20) comment '平台医院id',
   supplier_id          bigint(20) comment '平台供应商id',
   sources_ship_id      varchar(50) comment '原医院供应商关系id',
   sources_supplier_id  varchar(50) comment '原医院供应商id',
   sources_supplier_name varchar(128) comment '原医院供应商名称',
   "sources_supplier_credit_code 原医院供应商编码" varchar(128) comment '原医院供应商编码',
   sources_hospital_id  varchar(50) comment '原医院id',
   sources_hospital_name varchar(128) comment '原医院名称',
   sources_hospital_credit_code varchar(128) comment '原医院编码',
   ship_flag2           int(1) comment '是否匹对(0:未匹配,1:已匹配)',
   check_status         int(1) comment '审核状态(0:未审核,1:已审核)',
   cremanid             bigint(20) comment '创建人id',
   cremanname           varchar(30) comment '创建人姓名',
   credate              datetime comment '创建时间',
   editmanid            bigint(20) comment '修改人id',
   editmanname          varchar(30) comment '修改人姓名',
   editdate             datetime comment '修改时间',
   datasource           int(2) comment '数据来源(1:接口,2:手工)',
   del_flag             int(1) comment '是否删除(0:否,1:是)',
   memo                 varchar(200) comment '备注',
   primary key (ship_id)
);

alter table hdi_unicode_supply_ship comment '供应商匹配关系';

