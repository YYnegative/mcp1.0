/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/6/20 17:39:25                           */
/*==============================================================*/


drop table if exists hdi_refunds_apply_detail;

drop table if exists hdi_refunds_apply_master;

drop table if exists hdi_refunds_detail;

drop table if exists hdi_refunds_master;

/*==============================================================*/
/* Table: hdi_refunds_apply_detail                              */
/*==============================================================*/
create table hdi_refunds_apply_detail
(
   id                   bigint(20) not null auto_increment comment '主键id',
   apply_master_id      bigint(20) not null comment '退货申请主单id',
   accept_master_id     bigint(20) comment '验证主单id',
   accept_no            varchar(64) comment '验收单编号',
   accept_detail_id     bigint(20) comment '验收明细单id',
   goods_type           int(1) comment '商品类型(1:药品;2:试剂;3:耗材)',
   goods_id             bigint(20) not null comment '医院商品id',
   sources_goods_id     varchar(64) comment '原医院商品id',
   specs_id             bigint(20) comment '医院商品规格id',
   sources_specs_id     varchar(64) comment '原医院商品规格id',
   lot_id               bigint(20) not null comment '生产批号id',
   goods_unit_code      varchar(64) comment '商品单位编码',
   apply_refunds_number int(11) comment '申请退货数量',
   reality_refunds_number int(11) comment '实际退货数量',
   supply_price         decimal(11,2) comment '供货单价',
   refunds_remark       varchar(512) comment '退货原因',
   create_id            bigint(20) not null comment '创建人id',
   create_time          datetime not null comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   primary key (id)
);

alter table hdi_refunds_apply_detail comment '退货申请单明细信息';

/*==============================================================*/
/* Table: hdi_refunds_apply_master                              */
/*==============================================================*/
create table hdi_refunds_apply_master
(
   id                   bigint(20) not null auto_increment comment '主键id',
   supplier_id          bigint(20) not null comment '供应商id',
   sources_supplier_id  varchar(64) comment '原系统医院供应商id',
   hospital_id          bigint(20) not null comment '医院id',
   sources_hospital_id  varchar(64) comment '原系统医院id',
   store_house_id       bigint(20) not null comment '医院库房id',
   sources_store_house_id varchar(64) comment '原系统医院库房id',
   refunds_apply_no     varchar(64) not null comment '医院退货申请单号',
   apply_time           datetime not null comment '申请时间',
   status               int(1) not null comment '状态(0:未确认;1:已作废;2:已确认;3:已退货;4:部分退货)',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   data_source          int(1) comment '数据来源(0:系统录入;1:医院SPD)',
   primary key (id)
);

alter table hdi_refunds_apply_master comment '退货申请单信息';

/*==============================================================*/
/* Table: hdi_refunds_detail                                    */
/*==============================================================*/
create table hdi_refunds_detail
(
   id                   bigint(20) not null auto_increment comment '主键id',
   master_id            bigint(20) not null comment '退货主单id',
   sources_type         int(1) comment '来源类型(0:退货申请单;1:验收单;医院退货申请单号不为空则为0,否则为1)',
   sources_master_id    bigint(20) comment '来源主单id(根据来源类型,对应退货申请主单或验收主单id)',
   sources_detail_id    bigint(20) comment '来源明细单id(根据来源类型,对应退货申请明细单或验收明细单id)',
   accept_no            varchar(64) comment '验收单编号',
   goods_type           int(1) comment '商品类型(1:药品;2:试剂;3:耗材)',
   goods_id             bigint(20) not null comment '医院商品id',
   sources_goods_id     varchar(64) comment '原医院商品id',
   specs_id             bigint(20) not null comment '医院商品规格id',
   sources_specs_id     varchar(64) comment '原医院商品规格id',
   lot_id               bigint(20) comment '生产批号id',
   goods_unit_code      varchar(64) comment '商品单位编码',
   apply_refunds_number int(11) comment '申请退货数量',
   reality_refunds_number int(11) comment '实际退货数量',
   refunds_price        decimal(11,2) comment '退货单价',
   refunds_remark       varchar(512) comment '退货原因',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) not null comment '创建人id',
   create_time          datetime not null comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   primary key (id)
);

alter table hdi_refunds_detail comment '退货单明细信息';

/*==============================================================*/
/* Table: hdi_refunds_master                                    */
/*==============================================================*/
create table hdi_refunds_master
(
   id                   bigint(20) not null auto_increment comment '主键id',
   refunds_no           varchar(64) not null comment '退货单编号',
   supplier_id          bigint(20) not null comment '供应商id',
   sources_supplier_id  varchar(64) comment '原系统医院供应商id',
   hospital_id          bigint(20) not null comment '医院id',
   sources_hospital_id  varchar(64) comment '原系统医院id',
   store_house_id       bigint(20) not null comment '医院库房id',
   sources_store_house_id varchar(64) comment '原系统医院库房id',
   refunds_apply_no     varchar(64) comment '医院退货申请单号',
   regression_number    varchar(64) comment '消退单号',
   refunds_time         datetime not null comment '退货时间',
   status               int(1) not null comment '状态(0:未确认;1:已提交;2:已完成)',
   dept_id              bigint(20) comment '所属机构',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人id',
   edit_time            datetime comment '修改时间',
   del_flag             int(1) default 0 comment '是否删除(-1:已删除;0:正常)',
   data_source          int(1) comment '数据来源(0:系统录入;1:医院SPD)',
   primary key (id)
);

alter table hdi_refunds_master comment '退货单信息';

