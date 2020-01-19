/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/6/27 17:04:48                           */
/*==============================================================*/


drop table if exists hdi_goods_hospital_consumables;

drop table if exists hdi_goods_hospital_consumables_approvals;

drop table if exists hdi_goods_hospital_consumables_specs;

drop table if exists hdi_goods_hospital_drugs;

drop table if exists hdi_goods_hospital_drugs_specs;

drop table if exists hdi_goods_hospital_reagent;

drop table if exists hdi_goods_hospital_reagent_specs;

drop table if exists hdi_goods_platform_consumables;

drop table if exists hdi_goods_platform_consumables_approvals;

drop table if exists hdi_goods_platform_consumables_specs;

drop table if exists hdi_goods_platform_drugs;

drop table if exists hdi_goods_platform_drugs_specs;

drop table if exists hdi_goods_platform_reagent;

drop table if exists hdi_goods_platform_reagent_specs;

drop table if exists hdi_goods_supplier_consumables;

drop table if exists hdi_goods_supplier_consumables_approvals;

drop table if exists hdi_goods_supplier_consumables_specs;

drop table if exists hdi_goods_supplier_drugs;

drop table if exists hdi_goods_supplier_drugs_specs;

drop table if exists hdi_goods_supplier_reagent;

drop table if exists hdi_goods_supplier_reagent_specs;

DROP TABLE IF EXISTS hdi_goods_supplier_send;

DROP TABLE IF EXISTS pub_supplygoods;

/*==============================================================*/
/* Table: hdi_goods_hospital_consumables                        */
/*==============================================================*/
create table hdi_goods_hospital_consumables
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_id           varchar(64) comment '原系统商品id',
   hospital_id          bigint(20) not null comment '医院id',
   consumables_code     varchar(64) not null comment '耗材编码',
   consumables_name     varchar(128) not null comment '耗材名称',
   common_name          varchar(128) comment '通用名称',
   goods_nature         int(1) not null comment '商品属性(0:国产;1:进口)',
   type_id              varchar(64) comment '商品分类id',
   type_name            varchar(128) comment '商品分类名称',
   factory_id           varchar(128) not null comment '生产厂商id',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   goods_unit           varchar(64) comment '商品单位',
   buying_unit          varchar(64) comment '采购单位',
   convert_unit         varchar(128) comment '转换单位',
   pic_url              varchar(256) comment '图片地址',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   is_match             int(1) default 0 comment '是否匹对(0:未匹对;1:已匹对)',
   data_source          int(1) comment '数据来源(0:系统录入;1:医院SPD)',
   primary key (id)
);

alter table hdi_goods_hospital_consumables comment '医院耗材信息';

/*==============================================================*/
/* Table: hdi_goods_hospital_consumables_approvals              */
/*==============================================================*/
create table hdi_goods_hospital_consumables_approvals
(
   id                   bigint(20) not null auto_increment comment '主键id',
   consumables_id       bigint(20) not null comment '耗材id',
   approvals            varchar(128) not null comment '批准文号',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_hospital_consumables_approvals comment '医院耗材批准文号';

/*==============================================================*/
/* Table: hdi_goods_hospital_consumables_specs                  */
/*==============================================================*/
create table hdi_goods_hospital_consumables_specs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_specs_id     varchar(64) comment '原系统商品规格id',
   consumables_id       bigint(20) not null comment '耗材id',
   specs_code           varchar(64) not null comment '规格编码',
   specs                varchar(128) not null comment '商品规格',
   guid                 varchar(128) comment '全球唯一码',
   status               int(1) not null DEFAULT '1' comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_hospital_consumables_specs comment '医院耗材规格';

/*==============================================================*/
/* Table: hdi_goods_hospital_drugs                              */
/*==============================================================*/
create table hdi_goods_hospital_drugs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_id           varchar(64) comment '原系统商品id',
   hospital_id          bigint(20) not null comment '医院id',
   drugs_code           varchar(64) not null comment '药品编码',
   drugs_name           varchar(128) not null comment '药品名称',
   common_name          varchar(128) comment '通用名称',
   goods_nature         int(1) not null comment '商品属性(0:国产;1:进口)',
   type_id              varchar(64) comment '商品分类id',
   type_name            varchar(128) comment '商品分类名称',
   factory_id           varchar(128) not null comment '生产厂商id',
   approvals            varchar(128) comment '批准文号',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   goods_unit           varchar(64) comment '商品单位',
   buying_unit          varchar(64) comment '采购单位',
   convert_unit         varchar(128) comment '转换单位',
   pic_url              varchar(256) comment '图片地址',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   is_match             int(1) default 0 comment '是否匹对(0:未匹对;1:已匹对)',
   data_source          int(1) comment '数据来源(0:系统录入;1:医院SPD)',
   primary key (id)
);

alter table hdi_goods_hospital_drugs comment '医院药品信息';

/*==============================================================*/
/* Table: hdi_goods_hospital_drugs_specs                        */
/*==============================================================*/
create table hdi_goods_hospital_drugs_specs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_specs_id     varchar(64) comment '原系统商品规格id',
   drugs_id             bigint(20) not null comment '药品id',
   specs_code           varchar(64) not null comment '规格编码',
   specs                varchar(128) not null comment '商品规格',
   guid                 varchar(128) comment '全球唯一码',
   status               int(1) not null DEFAULT '1' comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_hospital_drugs_specs comment '医院药品规格';

/*==============================================================*/
/* Table: hdi_goods_hospital_reagent                            */
/*==============================================================*/
create table hdi_goods_hospital_reagent
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_id           varchar(64) comment '原系统商品id',
   hospital_id          bigint(20) not null comment '医院id',
   reagent_code         varchar(64) not null comment '试剂编码',
   reagent_name         varchar(128) not null comment '试剂名称',
   common_name          varchar(128) comment '通用名称',
   goods_nature         int(1) not null comment '商品属性(0:国产;1:进口)',
   type_id              varchar(64) comment '商品分类id',
   type_name            varchar(128) comment '商品分类名称',
   factory_id           varchar(128) not null comment '生产厂商id',
   approvals            varchar(128) comment '批准文号',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   goods_unit           varchar(64) comment '商品单位',
   buying_unit          varchar(64) comment '采购单位',
   convert_unit         varchar(128) comment '转换单位',
   pic_url              varchar(256) comment '图片地址',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   is_match             int(1) default 0 comment '是否匹对(0:未匹对;1:已匹对)',
   data_source          int(1) comment '数据来源(0:系统录入;1:医院SPD)',
   primary key (id)
);

alter table hdi_goods_hospital_reagent comment '医院试剂信息';

/*==============================================================*/
/* Table: hdi_goods_hospital_reagent_specs                      */
/*==============================================================*/
create table hdi_goods_hospital_reagent_specs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_specs_id     varchar(64) comment '原系统商品规格id',
   reagen_id            bigint(20) not null comment '试剂id',
   specs_code           varchar(64) not null comment '规格编码',
   specs                varchar(128) not null comment '商品规格',
   guid                 varchar(128) comment '全球唯一码',
   status               int(1) not null DEFAULT '1' comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_hospital_reagent_specs comment '医院试剂规格';

/*==============================================================*/
/* Table: hdi_goods_platform_consumables                        */
/*==============================================================*/
create table hdi_goods_platform_consumables
(
   id                   bigint(20) not null auto_increment comment '主键id',
   goods_unicode        varchar(128) not null comment '商品统一编码',
   consumables_code     varchar(64) not null comment '耗材编码',
   consumables_name     varchar(128) not null comment '耗材名称',
   common_name          varchar(128) comment '通用名称',
   goods_nature         int(1) not null comment '商品属性(0:国产;1:进口)',
   type_id              bigint(20) not null comment '商品分类id',
   factory_id           varchar(128) not null comment '生产厂商id',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   goods_unit           varchar(64) not null comment '商品单位',
   store_way            varchar(64) comment '储存方式',
   pic_url              varchar(256) comment '图片地址',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   primary key (id)
);

alter table hdi_goods_platform_consumables comment '平台耗材信息';

/*==============================================================*/
/* Table: hdi_goods_platform_consumables_approvals              */
/*==============================================================*/
create table hdi_goods_platform_consumables_approvals
(
   id                   bigint(20) not null auto_increment comment '主键id',
   consumables_id       bigint(20) not null comment '耗材id',
   approvals            varchar(128) not null comment '批准文号',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_platform_consumables_approvals comment '平台耗材批准文号';

/*==============================================================*/
/* Table: hdi_goods_platform_consumables_specs                  */
/*==============================================================*/
create table hdi_goods_platform_consumables_specs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   consumables_id       bigint(20) not null comment '耗材id',
   specs_code           varchar(64) not null comment '规格编码',
   specs                varchar(128) not null comment '商品规格',
   guid                 varchar(128) comment '全球唯一码',
   status               int(1) not null DEFAULT '1' comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_platform_consumables_specs comment '平台耗材规格';

/*==============================================================*/
/* Table: hdi_goods_platform_drugs                              */
/*==============================================================*/
create table hdi_goods_platform_drugs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   goods_unicode        varchar(128) not null comment '商品统一编码',
   drugs_code           varchar(64) not null comment '药品编码',
   drugs_name           varchar(128) not null comment '药品名称',
   common_name          varchar(128) comment '通用名称',
   goods_nature         int(1) not null comment '商品属性(0:国产;1:进口)',
   type_id              bigint(20) not null comment '商品分类id',
   factory_id           varchar(128) not null comment '生产厂商id',
   approvals            varchar(128) comment '批准文号',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   goods_unit           varchar(64) not null comment '商品单位',
   store_way            varchar(64) comment '储存方式',
   pic_url              varchar(256) comment '图片地址',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   primary key (id)
);

alter table hdi_goods_platform_drugs comment '平台药品信息';

/*==============================================================*/
/* Table: hdi_goods_platform_drugs_specs                        */
/*==============================================================*/
create table hdi_goods_platform_drugs_specs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   drugs_id             bigint(20) not null comment '药品id',
   specs_code           varchar(64) not null comment '规格编码',
   specs                varchar(128) not null comment '商品规格',
   guid                 varchar(128) comment '全球唯一码',
   status               int(1) not null DEFAULT '1' comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_platform_drugs_specs comment '平台药品规格';

/*==============================================================*/
/* Table: hdi_goods_platform_reagent                            */
/*==============================================================*/
create table hdi_goods_platform_reagent
(
   id                   bigint(20) not null auto_increment comment '主键id',
   goods_unicode        varchar(128) not null comment '商品统一编码',
   reagent_code         varchar(64) not null comment '试剂编码',
   reagent_name         varchar(128) not null comment '试剂名称',
   common_name          varchar(128) comment '通用名称',
   goods_nature         int(1) not null comment '商品属性(0:国产;1:进口)',
   type_id              bigint(20) not null comment '商品分类id',
   factory_id           varchar(128) not null comment '生产厂商id',
   approvals            varchar(128) comment '批准文号',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   goods_unit           varchar(64) not null comment '商品单位',
   store_way            varchar(64) comment '储存方式',
   pic_url              varchar(256) comment '图片地址',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   primary key (id)
);

alter table hdi_goods_platform_reagent comment '平台试剂信息';

/*==============================================================*/
/* Table: hdi_goods_platform_reagent_specs                      */
/*==============================================================*/
create table hdi_goods_platform_reagent_specs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   reagen_id            bigint(20) not null comment '试剂id',
   specs_code           varchar(64) not null comment '规格编码',
   specs                varchar(128) not null comment '商品规格',
   guid                 varchar(128) comment '全球唯一码',
   status               int(1) not null DEFAULT '1' comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_platform_reagent_specs comment '平台试剂规格';

/*==============================================================*/
/* Table: hdi_goods_supplier_consumables                        */
/*==============================================================*/
create table hdi_goods_supplier_consumables
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_id           varchar(64) comment '原系统商品id',
   supplier_id          bigint(20) not null comment '供应商id',
   consumables_code     varchar(64) not null comment '耗材编码',
   consumables_name     varchar(128) not null comment '耗材名称',
   common_name          varchar(128) comment '通用名称',
   goods_nature         int(1) not null comment '商品属性(0:国产;1:进口)',
   type_id              varchar(64) comment '商品分类id',
   type_name            varchar(128) comment '商品分类名称',
   factory_id           varchar(128) not null comment '生产厂商id',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   goods_unit           varchar(64) comment '商品单位',
   supply_unit          varchar(64) comment '供货单位',
   convert_unit         varchar(128) comment '转换单位',
   agent_id             bigint(20) comment '代理商id',
   pic_url              varchar(256) comment '图片地址',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   is_match             int(1) default 0 comment '是否匹对(0:未匹对;1:已匹对)',
   data_source          int(1) comment '数据来源(0:系统录入;1:供应商ERP)',
   is_upload            int(1) comment '是否上传(0:未上传;1:已上传)',
   primary key (id)
);

alter table hdi_goods_supplier_consumables comment '供应商耗材信息';

/*==============================================================*/
/* Table: hdi_goods_supplier_consumables_approvals              */
/*==============================================================*/
create table hdi_goods_supplier_consumables_approvals
(
   id                   bigint(20) not null auto_increment comment '主键id',
   consumables_id       bigint(20) not null comment '耗材id',
   approvals            varchar(128) not null comment '批准文号',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_supplier_consumables_approvals comment '供应商耗材批准文号';

/*==============================================================*/
/* Table: hdi_goods_supplier_consumables_specs                  */
/*==============================================================*/
create table hdi_goods_supplier_consumables_specs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_specs_id     varchar(64) comment '原系统商品规格id',
   consumables_id       bigint(20) not null comment '耗材id',
   specs_code           varchar(64) not null comment '规格编码',
   specs                varchar(128) not null comment '商品规格',
   guid                 varchar(128) comment '全球唯一码',
   status               int(1) not null DEFAULT '1' comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_supplier_consumables_specs comment '供应商耗材规格';

/*==============================================================*/
/* Table: hdi_goods_supplier_drugs                              */
/*==============================================================*/
create table hdi_goods_supplier_drugs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_id           varchar(64) comment '原系统商品id',
   supplier_id          bigint(20) not null comment '供应商id',
   drugs_code           varchar(64) not null comment '药品编码',
   drugs_name           varchar(128) not null comment '药品名称',
   common_name          varchar(128) comment '通用名称',
   goods_nature         int(1) not null comment '商品属性(0:国产;1:进口)',
   type_id              varchar(64) comment '商品分类id',
   type_name            varchar(128) comment '商品分类名称',
   factory_id           varchar(128) not null comment '生产厂商id',
   approvals            varchar(128) comment '批准文号',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   goods_unit           varchar(64) comment '商品单位',
   supply_unit          varchar(64) comment '供货单位',
   convert_unit         varchar(128) comment '转换单位',
   agent_id             bigint(20) comment '代理商id',
   pic_url              varchar(256) comment '图片地址',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   is_match             int(1) default 0 comment '是否匹对(0:未匹对;1:已匹对)',
   data_source          int(1) comment '数据来源(0:系统录入;1:供应商ERP)',
   is_upload            int(1) comment '是否上传(0:未上传;1:已上传)',
   primary key (id)
);

alter table hdi_goods_supplier_drugs comment '供应商药品信息';

/*==============================================================*/
/* Table: hdi_goods_supplier_drugs_specs                        */
/*==============================================================*/
create table hdi_goods_supplier_drugs_specs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_specs_id     varchar(64) comment '原系统商品规格id',
   drugs_id             bigint(20) not null comment '药品id',
   specs_code           varchar(64) not null comment '规格编码',
   specs                varchar(128) not null comment '商品规格',
   guid                 varchar(128) comment '全球唯一码',
   status               int(1) not null DEFAULT '1' comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_supplier_drugs_specs comment '供应商药品规格';

/*==============================================================*/
/* Table: hdi_goods_supplier_reagent                            */
/*==============================================================*/
create table hdi_goods_supplier_reagent
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_id           varchar(64) comment '原系统商品id',
   supplier_id          bigint(20) not null comment '供应商id',
   reagent_code         varchar(64) not null comment '试剂编码',
   reagent_name         varchar(128) not null comment '试剂名称',
   common_name          varchar(128) comment '通用名称',
   goods_nature         int(1) not null comment '商品属性(0:国产;1:进口)',
   type_id              varchar(64) comment '商品分类id',
   type_name            varchar(128) comment '商品分类名称',
   factory_id           varchar(128) not null comment '生产厂商id',
   approvals            varchar(128) comment '批准文号',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   goods_unit           varchar(64) comment '商品单位',
   supply_unit          varchar(64) comment '供货单位',
   convert_unit         varchar(128) comment '转换单位',
   agent_id             bigint(20) comment '代理商id',
   pic_url              varchar(256) comment '图片地址',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   is_match             int(1) default 0 comment '是否匹对(0:未匹对;1:已匹对)',
   data_source          int(1) comment '数据来源(0:系统录入;1:供应商ERP)',
   is_upload            int(1) comment '是否上传(0:未上传;1:已上传)',
   primary key (id)
);

alter table hdi_goods_supplier_reagent comment '供应商试剂信息';

/*==============================================================*/
/* Table: hdi_goods_supplier_reagent_specs                      */
/*==============================================================*/
create table hdi_goods_supplier_reagent_specs
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_specs_id     varchar(64) comment '原系统商品规格id',
   reagen_id            bigint(20) not null comment '试剂id',
   specs_code           varchar(64) not null comment '规格编码',
   specs                varchar(128) not null comment '商品规格',
   guid                 varchar(128) comment '全球唯一码',
   status               int(1) not null DEFAULT '1' comment '状态(0:停用;1:启用)',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   primary key (id)
);

alter table hdi_goods_supplier_reagent_specs comment '供应商试剂规格';


-- ----------------------------
-- Table structure for hdi_goods_supplier_send
-- ----------------------------
CREATE TABLE hdi_goods_supplier_send (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  supplier_id bigint(20) NOT NULL COMMENT '供应商id',
  hospital_id bigint(20) NOT NULL COMMENT '医院id',
  goods_type int(1) NOT NULL COMMENT '商品类别(1:药品;2:试剂;3:耗材)',
  platform_goods_id bigint(20) DEFAULT NULL COMMENT '平台商品id',
  platform_goods_code varchar(64) DEFAULT NULL COMMENT '平台商品编码',
  platform_goods_specs_id bigint(20) DEFAULT NULL COMMENT '平台商品规格id',
  platform_goods_specs_code varchar(64) DEFAULT NULL COMMENT '平台商品规格编码',
  goods_id bigint(20) NOT NULL DEFAULT '0' COMMENT '供应商商品id',
  goods_specs_id bigint(20) NOT NULL DEFAULT '0' COMMENT '供应商商品规格id',
  dept_id bigint(20) NOT NULL DEFAULT '0' COMMENT '供应商所属机构',
  create_id bigint(20) NOT NULL COMMENT '创建人id',
  create_time datetime NOT NULL COMMENT '创建时间',
  is_upload int(1) NOT NULL DEFAULT '0' COMMENT '是否上传(0:未上传;1:已上传)',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='供应商商品下发表';

-- ----------------------------
-- Table structure for pub_supplygoods
-- ----------------------------
CREATE TABLE pub_supplygoods (
  mgoodsid varchar(50) NOT NULL COMMENT '商品主键',
  uorganid varchar(50) DEFAULT NULL COMMENT '机构名称',
  supplyid varchar(50) DEFAULT NULL COMMENT '供应商id',
  platformno varchar(50) DEFAULT NULL COMMENT '平台编码',
  goodsno varchar(50) DEFAULT NULL COMMENT '商品编码',
  goodstype varchar(200) DEFAULT NULL COMMENT '商品规格',
  goodsname varchar(200) DEFAULT NULL COMMENT '商品名称',
  goodsshortname varchar(200) DEFAULT NULL COMMENT '商品名缩写（耗材简写）',
  goodsunit varchar(50) DEFAULT NULL COMMENT '商品单位',
  packsize decimal(10,4) DEFAULT NULL COMMENT '包装大小',
  rationmrtr decimal(10,0) DEFAULT NULL COMMENT '关系倍数分子',
  ratiodnmtr decimal(10,0) DEFAULT NULL COMMENT '关系倍数分母',
  bidprice decimal(12,4) DEFAULT NULL COMMENT '进货价',
  purchasetype decimal(10,0) DEFAULT NULL COMMENT '采购类别',
  goodsprop decimal(10,0) DEFAULT NULL COMMENT '储存属性',
  storagecondiction decimal(4,0) DEFAULT NULL COMMENT '储存条件',
  factoryname varchar(200) DEFAULT NULL COMMENT '厂家名称',
  registerdocno varchar(50) DEFAULT NULL COMMENT '注册证号',
  usestatus decimal(4,0) DEFAULT NULL COMMENT '使用状态',
  goodscategorytype int(11) DEFAULT NULL COMMENT '商品所属类别',
  mixtype decimal(11,0) DEFAULT NULL COMMENT '混装类别',
  receiptflag int(11) DEFAULT NULL COMMENT '接收标识',
  transdate datetime DEFAULT NULL COMMENT '传送时间',
  unitcover varchar(50) DEFAULT NULL COMMENT '单位比例',
  pay decimal(4,0) DEFAULT NULL COMMENT '收费标识',
  PRIMARY KEY (mgoodsid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='供应商商品下发到hdi临时表';

