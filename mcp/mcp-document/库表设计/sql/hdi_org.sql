/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/6/14 14:54:27                           */
/*==============================================================*/


drop table if exists hdi_org_agent_info;

drop table if exists hdi_org_factory_info;

drop table if exists hdi_org_hospital_info;

drop table if exists hdi_org_supplier_hospital_ref;

drop table if exists hdi_org_supplier_info;

/*==============================================================*/
/* Table: hdi_org_agent_info                                    */
/*==============================================================*/
create table hdi_org_agent_info
(
   id                   bigint(20) not null auto_increment comment '主键id',
   supplier_id          bigint(20) not null comment '供应商id',
   agent_code           varchar(128) not null comment '代理商编码',
   agent_name           varchar(128) not null comment '代理商名称',
   credit_code          varchar(64) not null comment '统一社会信用代码',
   province_code        varchar(64) comment '所在省编码',
   city_code            varchar(64) comment '所在市编码',
   area_code            varchar(64) comment '所在区县编码',
   agent_address        varchar(256) comment '代理商地址',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   corporate            varchar(64) comment '法人代表',
   phone                varchar(32) comment '联系电话',
   email                varchar(64) comment '邮箱地址',
   fax                  varchar(64) comment '传真',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   primary key (id)
);

alter table hdi_org_agent_info comment '代理商信息';

/*==============================================================*/
/* Table: hdi_org_factory_info                                  */
/*==============================================================*/
create table hdi_org_factory_info
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_id           varchar(64) comment '原系统厂商id',
   factory_code         varchar(128) comment '厂商编码',
   factory_name         varchar(128) not null comment '厂商名称',
   credit_code          varchar(64) comment '统一社会信用代码',
   country_code         varchar(64) comment '所在国家编码',
   province_code        varchar(64) comment '所在省编码',
   city_code            varchar(64) comment '所在市编码',
   area_code            varchar(64) comment '所在区县编码',
   factory_address      varchar(256) comment '厂商地址',
   status               int(1) not null comment '状态(-1:待审批;0:停用;1:启用)',
   corporate            varchar(64) comment '法人代表',
   phone                varchar(32) comment '联系电话',
   email                varchar(64) comment '邮箱地址',
   fax                  varchar(64) comment '传真',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   data_source          int(1) default 0 comment '数据来源(0:系统录入;1:医院SPD)',
   primary key (id)
);

alter table hdi_org_factory_info comment '厂商信息';

/*==============================================================*/
/* Table: hdi_org_hospital_info                                 */
/*==============================================================*/
create table hdi_org_hospital_info
(
   id                   bigint(20) not null auto_increment comment '主键id',
   parent_id            bigint(20) comment '父供应商id',
   sources_id           varchar(64) comment '原系统医院id',
   hospital_code        varchar(128) not null comment '医院编码',
   hospital_grade       int(1) comment '医院级别(0:三级特等;1:三级甲等;2:三级乙等;3:三级丙等;4:二级甲等;5:二级乙等;6:二级丙等;7:一级甲等;8:一级乙等;9:一级丙等)',
   hospital_name        varchar(128) not null comment '医院名称',
   credit_code          varchar(64) not null comment '统一社会信用代码',
   province_code        varchar(64) comment '所在省编码',
   city_code            varchar(64) comment '所在市编码',
   area_code            varchar(64) comment '所在区县编码',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   hospital_address     varchar(256) comment '医院地址',
   corporate            varchar(64) comment '法人代表',
   phone                varchar(32) comment '联系电话',
   email                varchar(64) comment '邮箱地址',
   is_group             int(1) comment '是否集团机构(0:否;1:是)',
   child_number         int(11) comment '子机构数(集团医院允许创建的子医院数)',
   fax                  varchar(64) comment '传真',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   data_source          int(1) default 0 comment '数据来源(0:系统录入;1:医院SPD)',
   primary key (id)
);

alter table hdi_org_hospital_info comment '医院信息';

/*==============================================================*/
/* Table: hdi_org_supplier_hospital_ref                         */
/*==============================================================*/
create table hdi_org_supplier_hospital_ref
(
   id                   bigint(20) not null auto_increment comment '主键id',
   sources_id           varchar(64) comment '原系统关系id',
   supplier_id          bigint(20) not null comment '供应商id',
   sources_supplier_id  varchar(64) comment '原系统供应商id',
   hospital_id          bigint(20) not null comment '医院id',
   sources_hospital_id  varchar(64) comment '原系统医院id',
   del_flag             int(1) comment '是否删除(-1:已删除;0:正常)',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   data_source          int(1) default 0 comment '数据来源(0:系统录入;1:医院SPD)',
   primary key (id)
);

alter table hdi_org_supplier_hospital_ref comment '供应商医院绑定关系';

/*==============================================================*/
/* Table: hdi_org_supplier_info                                 */
/*==============================================================*/
create table hdi_org_supplier_info
(
   id                   bigint(20) not null auto_increment comment '主键id',
   parent_id            bigint(20) comment '父供应商id',
   sources_id           varchar(64) comment '原系统供应商id',
   supplier_code        varchar(128) not null comment '供应商编码',
   supplier_name        varchar(128) not null comment '供应商名称',
   credit_code          varchar(64) comment '统一社会信用代码',
   province_code        varchar(64) comment '所在省编码',
   city_code            varchar(64) comment '所在市编码',
   area_code            varchar(64) comment '所在区县编码',
   supplier_address     varchar(256) comment '供应商地址',
   status               int(1) not null comment '状态(0:停用;1:启用)',
   supplier_nature      int(1) comment '供应商性质(0:国营企业;1:民营企业;2:中外合资企业;3:股份制企业;4:个体企业)',
   corporate            varchar(64) comment '法人代表',
   phone                varchar(32) comment '联系电话',
   email                varchar(64) comment '邮箱地址',
   fax                  varchar(64) comment '传真',
   is_group             int(1) comment '是否集团机构(0:否;1:是)',
   child_number         int(11) comment '子机构数(集团供应商允许创建的子供应商数)',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   data_source          int(1) default 0 comment '数据来源(0:系统录入;1:医院SPD)',
   primary key (id)
);

alter table hdi_org_supplier_info comment '供应商信息';

