/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/6/27 17:06:15                           */
/*==============================================================*/


drop table if exists hdi_license_agent_info;

drop table if exists hdi_license_classify_info;

drop table if exists hdi_license_factory_info;

drop table if exists hdi_license_goods_info;

drop table if exists hdi_license_hospital_examine;

drop table if exists hdi_license_supplier_info;

/*==============================================================*/
/* Table: hdi_license_agent_info                                */
/*==============================================================*/
create table hdi_license_agent_info
(
   id                   bigint(20) not null auto_increment comment '主键id',
   supplier_id          bigint(20) not null comment '供应商id',
   agent_id             bigint(20) not null comment '代理商id',
   classify_id          bigint(20) not null comment '分类id',
   name                 varchar(128) not null comment '证照名称',
   number               varchar(128) not null comment '证照编号',
   begin_time           datetime not null comment '效期开始时间',
   end_time             datetime not null comment '效期结束时间',
   pic_url              varchar(256) comment '证照图片',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   new_license_id       bigint(20) comment '新证照id(换证)',
   primary key (id)
);

alter table hdi_license_agent_info comment '代理商证照信息';

/*==============================================================*/
/* Table: hdi_license_classify_info                             */
/*==============================================================*/
create table hdi_license_classify_info
(
   id                   bigint(20) not null auto_increment comment '主键id',
   type                 int(1) not null comment '证照类型(0:商品证照;1:供应商证照;2:厂商证照;3:代理商证照)',
   name                 varchar(64) not null comment '分类名称',
   is_warning           int(1) not null comment '是否预警(0:否;1:是)',
   early_date           int(3) not null comment '预警天数',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   primary key (id)
);

alter table hdi_license_classify_info comment '证照分类信息';

/*==============================================================*/
/* Table: hdi_license_factory_info                              */
/*==============================================================*/
create table hdi_license_factory_info
(
   id                   bigint(20) not null auto_increment comment '主键id',
   supplier_id          bigint(20) not null comment '供应商id',
   factory_id           bigint(20) not null comment '厂商id',
   classify_id          bigint(20) not null comment '分类id',
   name                 varchar(128) not null comment '证照名称',
   number               varchar(128) not null comment '证照编号',
   begin_time           datetime not null comment '效期开始时间',
   end_time             datetime not null comment '效期结束时间',
   pic_url              varchar(256) comment '证照图片',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   new_license_id       bigint(20) comment '新证照id(换证)',
   primary key (id)
);

alter table hdi_license_factory_info comment '厂商证照信息';

/*==============================================================*/
/* Table: hdi_license_goods_info                                */
/*==============================================================*/
create table hdi_license_goods_info
(
   id                   bigint(20) not null auto_increment comment '主键id',
   supplier_id          bigint(20) not null comment '供应商id',
   goods_id             bigint(20) not null comment '商品id',
   goods_type           int(1) not null comment '商品类别(1:药品;2:试剂;3:耗材)',
   classify_id          bigint(20) not null comment '分类id',
   name                 varchar(128) not null comment '证照名称',
   number               varchar(128) not null comment '证照编号',
   begin_time           datetime not null comment '效期开始时间',
   end_time             datetime not null comment '效期结束时间',
   pic_url              varchar(256) comment '证照图片',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   new_license_id       bigint(20) comment '新证照id(换证)',
   primary key (id)
);

alter table hdi_license_goods_info comment '商品证照信息';

/*==============================================================*/
/* Table: hdi_license_hospital_examine                          */
/*==============================================================*/
create table hdi_license_hospital_examine
(
   id                   bigint(20) not null auto_increment comment '主键id',
   license_id           bigint(20) not null comment '证照id',
   license_type         int(1) not null comment '证照类型(0:商品证照;1:供应商证照;2:厂商证照;3:代理商证照)',
   classify_id          bigint(20) comment '分类id',
   name                 varchar(128) not null comment '证照名称',
   number               varchar(128) not null comment '证照编号',
   begin_time           datetime comment '效期开始时间',
   end_time             datetime comment '效期结束时间',
   pic_url              varchar(256) comment '证照图片',
   business_id          bigint(20) not null comment '业务id(商品id或供应商id)',
   business_name        varchar(128) comment '业务名称(商品名称或供应商名称)',
   hospital_id          bigint(20) not null comment '医院id',
   hospital_name        varchar(128) comment '医院名称',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   status               int(1) not null comment '审核状态(0:待审批;1:审核通过;2:审核不通过)',
   examine_opinion      varchar(512) comment '审批意见',
   examine_time         datetime comment '审批时间',
   primary key (id)
);

alter table hdi_license_hospital_examine comment '证照医院审批';

/*==============================================================*/
/* Table: hdi_license_supplier_info                             */
/*==============================================================*/
create table hdi_license_supplier_info
(
   id                   bigint(20) not null auto_increment comment '主键id',
   supplier_id          bigint(20) not null comment '供应商id',
   classify_id          bigint(20) not null comment '分类id',
   name                 varchar(128) not null comment '证照名称',
   number               varchar(128) not null comment '证照编号',
   begin_time           datetime not null comment '效期开始时间',
   end_time             datetime not null comment '效期结束时间',
   pic_url              varchar(256) comment '证照图片',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   new_license_id       bigint(20) comment '新证照id(换证)',
   primary key (id)
);

alter table hdi_license_supplier_info comment '供应商证照信息';

