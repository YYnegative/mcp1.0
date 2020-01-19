/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/9/17 17:27:34                           */
/*==============================================================*/


drop table if exists hdi_core_accept_detail;

drop table if exists hdi_core_accept_master;

drop table if exists hdi_core_label_detail;

drop table if exists hdi_core_label_master;

drop table if exists hdi_core_lot;

drop table if exists hdi_core_purchase_detail;

drop table if exists hdi_core_purchase_master;

drop table if exists hdi_core_storehouse;

drop table if exists hdi_core_supply_detail;

drop table if exists hdi_core_supply_master;

/*==============================================================*/
/* Table: hdi_core_accept_detail                                */
/*==============================================================*/
create table hdi_core_accept_detail
(
   accept_detail_id     bigint(20) not null auto_increment comment '验收细单id',
   accept_master_id     bigint(20) comment '验收主单id',
   goodsclass           int(4) comment '商品类别(1:药品;2:试剂;3:耗材)',
   ygoodsid             varchar(50) comment '原医院商品id',
   ygoodsno             varchar(50) comment '原医院商品编码',
   ygoodsname           varchar(128) comment '原医院商品名称',
   ygoodstypeid         varchar(50) comment '原医院商品规格id',
   ygoodstypeno         varchar(64) comment '原医院商品规格编码',
   ygoodstypename       varchar(128) comment '原医院商品规格名称',
   goodsid              bigint(20) comment '平台医院商品id',
   goodsno              varchar(30) comment '平台医院商品编码',
   goodsname            varchar(128) comment '平台医院商品名称',
   goodstypeid          bigint(20) comment '平台医院商品规格id',
   goodstypeno          varchar(64) comment '平台医院商品规格编码',
   goodstype            varchar(100) comment '平台医院商品规格名称',
   goodsunit            varchar(64) comment '验收商品单位',
   accept_qty           double(16,2) comment '验收数量',
   lotid                bigint(20) comment '批号id',
   lotno                varchar(50) comment '生产批号',
   proddate             date comment '生产日期',
   invadate             date comment '失效日期',
   orgdataid            varchar(50) comment '原验收数据id',
   orgdatadtlid         varchar(50) comment '原验收数据明细id',
   sourceid             bigint(20) comment '供货主单id',
   sourcedtlid          bigint(20) comment '供货细单id',
   memo                 varchar(200) comment '备注',
   cremanid             bigint(20) comment '创建人id',
   credate              datetime comment '创建时间',
   primary key (accept_detail_id)
);

alter table hdi_core_accept_detail comment '验收细单信息';

/*==============================================================*/
/* Table: hdi_core_accept_master                                */
/*==============================================================*/
create table hdi_core_accept_master
(
   accept_master_id     bigint(20) not null auto_increment comment '验收主单id',
   acceptno             varchar(50) comment '验收单编号',
   his_supplyid         varchar(64) comment '原供应商id',
   sources_supplier_code varchar(128) comment '原供应商编码',
   sources_supplier_name varchar(128) comment '原供应商名称',
   supplier_id          bigint(20) comment '平台供应商id',
   supplier_code        varchar(128) comment '平台供应商编码',
   supplier_name        varchar(128) comment '平台供应商名称',
   uorganid             varchar(64) comment '原医院id',
   sources_hospital_code varchar(128) comment '原医院编码',
   sources_hospital_name varchar(128) comment '原医院名称',
   horg_id              bigint(20) comment '平台医院id',
   hospital_code        varchar(128) comment '平台医院编码',
   hospital_name        varchar(128) comment '平台医院名称',
   ystorehouseid        varchar(50) comment '原医院库房id',
   storehouseid         bigint(20) comment '平台库房id',
   storehouse_no        varchar(100) comment '库房编码',
   storehouse_name      varchar(50) comment '库房名称',
   settle_flag          int(1) comment '结算标识(0:未结算;1:已结算)',
   sourceid             bigint(20) comment '供货单id',
   orgdataid            varchar(50) comment '原验收单id',
   datasource           int(1) comment '数据来源(1:手工;2: 接口)',
   dept_id              bigint(20) comment '供应商所属机构',
   memo                 varchar(200) comment '备注',
   del_flag             int(1) comment '是否删除(-1:已删除;0:正常)',
   cremanid             bigint(20) comment '创建人',
   credate              datetime comment '创建时间',
   primary key (accept_master_id)
);

alter table hdi_core_accept_master comment '验收主单信息';

/*==============================================================*/
/* Table: hdi_core_label_detail                                 */
/*==============================================================*/
create table hdi_core_label_detail
(
   labeldtlid           bigint(20) not null auto_increment comment '标签明细id',
   labelid              bigint(20) comment '标签id',
   label_qty            double(16,2) comment '标签数量',
   supply_master_id     bigint(20) comment '供货主单id',
   supply_detail_id     bigint(20) comment '供货细单id',
   primary key (labeldtlid)
);

alter table hdi_core_label_detail comment '标签明细信息';

/*==============================================================*/
/* Table: hdi_core_label_master                                 */
/*==============================================================*/
create table hdi_core_label_master
(
   labelid              bigint(20) not null auto_increment comment '标签id',
   sources_supplier_id  varchar(64) comment '原供应商id',
   sources_supplier_code varchar(128) comment '原供应商编码',
   sources_supplier_name varchar(128) comment '原供应商名称',
   supplier_id          bigint(20) comment '平台供应商id',
   supplier_code        varchar(128) comment '平台供应商编码',
   supplier_name        varchar(128) comment '平台供应商名称',
   sources_hospital_id  varchar(64) comment '原医院id',
   sources_hospital_code varchar(128) comment '原医院编码',
   sources_hospital_name varchar(128) comment '原医院名称',
   horg_id              bigint(20) comment '平台医院id',
   hospital_code        varchar(128) comment '平台医院编码',
   hospital_name        varchar(128) comment '平台医院名称',
   sources_storehouse_id varchar(50) comment '原医院库房id',
   storehouseid         bigint(20) comment '平台库房id',
   storehouse_no        varchar(100) comment '库房编码',
   storehouse_name      varchar(50) comment '库房名称',
   labelno              varchar(50) comment '标签编码',
   labelstatus          int(1) comment '标签打印状态(0:未打印;1:已打印)',
   sourceid             bigint(20) comment '供货主单id',
   image_url            varchar(255) comment '标签二维码图片地址',
   dept_id              bigint(20) comment '供应商所属机构',
   del_flag             int(1) comment '是否删除(-1:已删除;0:正常)',
   cremanid             bigint(20) comment '创建人',
   credate              datetime comment '创建时间',
   primary key (labelid)
);

alter table hdi_core_label_master comment '标签信息';

/*==============================================================*/
/* Table: hdi_core_lot                                          */
/*==============================================================*/
create table hdi_core_lot
(
   lotid                bigint(20) not null auto_increment comment '批号id',
   supplier_id          bigint(20) comment '平台供应商id',
   dept_id              bigint(20) comment '所属机构id',
   goodsclass           int(4) comment '商品类别(1:药品;2:试剂;3:耗材)',
   goodsid              bigint(20) comment '平台商品id',
   goodstypeid          bigint(20) comment '平台供应商商品规格id',
   lottype              int(1) comment '批号类型(1:生产批; 2:灭菌批号)',
   lotstatus            int(1) comment '批号状态(0:停用;1:启用)',
   lotno                varchar(50) comment '批号',
   proddate             date comment '生产日期',
   invadate             date comment '失效日期',
   del_flag             int(1) comment '是否删除(-1:已删除;0:正常)',
   primary key (lotid)
);

alter table hdi_core_lot comment '商品批号信息';

/*==============================================================*/
/* Table: hdi_core_purchase_detail                              */
/*==============================================================*/
create table hdi_core_purchase_detail
(
   purchase_detail_id   bigint(20) not null auto_increment comment '采购细单id',
   purchase_master_id   bigint(20) comment '采购主单id',
   goodsclass           int(4) comment '商品类别(1:药品;2:试剂;3:耗材)',
   yhgoodsid            varchar(64) comment '原医院商品id',
   yhgoodsname          varchar(128) comment '原医院商品名称',
   yhgoodstypeid        varchar(50) comment '原医院商品规格id',
   yhgoodsno            varchar(50) comment '原医院商品规格编码',
   yhgoodstypename      varchar(128) comment '原医院商品规格名称',
   hgoodsunit           varchar(50) comment '医院采购商品单位',
   hunitprice           double(16,2) comment '采购单价',
   hqty                 double(16,2) comment '医院采购数量',
   hgoodsid             bigint(20) comment '平台医院商品id',
   hgoodsno             varchar(30) comment '平台医院商品编码',
   hgoodsname           varchar(128) comment '平台医院商品名称',
   hgoodstypeid         bigint(20) comment '平台医院商品规格id',
   hgoodstypeno         varchar(64) comment '平台医院商品规格编码',
   hgoodstype           varchar(100) comment '平台医院商品规格名称',
   orgdataid            varchar(50) comment '原采购主单id',
   orgdatadtlid         varchar(50) comment '原采购细单id',
   sourceid             varchar(50) comment '数据来源标识',
   sourcedtlid          varchar(50) comment '数据来源明细标识',
   memo                 varchar(200) comment '备注',
   create_id            bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人',
   edit_time            datetime comment '修改时间',
   primary key (purchase_detail_id)
);

alter table hdi_core_purchase_detail comment '采购细单信息';

/*==============================================================*/
/* Table: hdi_core_purchase_master                              */
/*==============================================================*/
create table hdi_core_purchase_master
(
   purchase_master_id   bigint(20) not null auto_increment comment '采购主单id',
   purplanno            varchar(50) comment '采购单编号',
   sources_supplier_id  varchar(64) comment '原供应商id',
   sources_supplier_code varchar(128) comment '原供应商编码',
   sources_supplier_name varchar(128) comment '原供应商名称',
   supplier_id          bigint(20) comment '平台供应商id',
   supplier_code        varchar(128) comment '平台供应商编码',
   supplier_name        varchar(128) comment '平台供应商名称',
   sources_hospital_id  varchar(64) comment '原医院id',
   sources_hospital_code varchar(128) comment '原医院编码',
   sources_hospital_name varchar(128) comment '原医院名称',
   horg_id              bigint(20) comment '平台医院id',
   hospital_code        varchar(128) comment '平台医院编码',
   hospital_name        varchar(128) comment '平台医院名称',
   sources_storehouse_id varchar(50) comment '原医院库房id',
   storehouseid         bigint(50) comment '平台库房id',
   storehouse_no        varchar(50) comment '库房编码',
   storehouse_name      varchar(50) comment '库房名称',
   purplantime          datetime comment '采购时间',
   expecttime           datetime comment '预计到货时间',
   purchasestatus       int(4) comment '采购单状态(0:已作废;1:未确认;2:已确认;3:已供货;4:部分供货)',
   supply_addr          varchar(200) comment '供货地址',
   orgdataid            varchar(50) comment '原采购单id',
   sourceid             varchar(50) comment '数据来源标识',
   datasource           int(1) comment '数据来源(1:手工;2: 接口)',
   dept_id              bigint(20) comment '供应商所属机构',
   del_flag             int(1) comment '是否删除(-1:已删除;0:正常)',
   memo                 varchar(200) comment '备注',
   cremanid             bigint(20) comment '创建人id',
   credate              datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人',
   edit_time            datetime comment '修改时间',
   primary key (purchase_master_id)
);

alter table hdi_core_purchase_master comment '采购主单信息';

/*==============================================================*/
/* Table: hdi_core_storehouse                                   */
/*==============================================================*/
create table hdi_core_storehouse
(
   storehouseid         bigint(20) not null auto_increment comment '库房id',
   storehousename       varchar(50) comment '库房名称',
   storehouseno         varchar(100) comment '库房编码',
   shaddress            varchar(200) comment '库房地址',
   orgdataid            varchar(50) comment '原医院库房id',
   uorganid             varchar(50) comment '原医院机构id',
   horg_id              bigint(20) comment '平台医院id',
   dept_id              bigint(20) comment '所属机构id',
   cremanid             bigint(20) comment '创建人id',
   cremanname           varchar(30) comment '创建人姓名',
   credate              datetime comment '创建时间',
   editmanid            bigint(20) comment '修改人id',
   editmanname          varchar(30) comment '修改人姓名',
   editdate             datetime comment '修改时间',
   memo                 varchar(200) comment '备注',
   data_source          int(1) comment '数据来源(0:系统录入;1:医院SPD)',
   primary key (storehouseid)
);

alter table hdi_core_storehouse comment '医院库房信息';

/*==============================================================*/
/* Table: hdi_core_supply_detail                                */
/*==============================================================*/
create table hdi_core_supply_detail
(
   supply_detail_id     bigint(20) not null auto_increment comment '供货细单id',
   supply_master_id     bigint(20) comment '供货主单id',
   purchase_master_id   bigint(20) comment '采购主单id',
   purchase_detail_id   bigint(20) comment '采购细单id',
   sourceid             varchar(50) comment 'ERP出库单标识',
   sourcedtlid          varchar(50) comment 'ERP出库明细单标识',
   purplanid            varchar(50) comment '采购计划标识',
   purplandtlid         varchar(50) comment '采购计划明细标识',
   goodsclass           int(4) comment '商品类别(1:药品;2:试剂;3:耗材)',
   goodsid              bigint(20) comment '供应商商品id',
   goodsno              varchar(128) comment '供应商商品编码',
   goodsname            varchar(128) comment '供应商商品名称',
   goodstypeid          bigint(20) comment '供应商商品规格id',
   goodstypeno          varchar(128) comment '供应商商品规格编码',
   goodstype            varchar(128) comment '供应商商品规格名称',
   goodsunit            varchar(50) comment '供应商采购单位',
   supply_qty           double(16,2) comment '供货数量',
   supply_unitprice     double(16,2) comment '供货单价',
   lotid                bigint(20) comment '批号id',
   lotno                varchar(50) comment '生产批号',
   proddate             date comment '生产日期',
   invadate             date comment '失效日期',
   batch_code           varchar(255) comment '批次编码',
   image_url            varchar(255) comment '批次二维码图片地址',
   orgdataid            varchar(50) comment '原数据标识',
   orgdatadtlid         varchar(50) comment '原数据明细标识',
   memo                 varchar(200) comment '备注',
   cremanid             bigint(20) comment '创建人id',
   credate              datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人',
   edit_time            datetime comment '修改时间',
   primary key (supply_detail_id)
);

alter table hdi_core_supply_detail comment '供货细单信息';

/*==============================================================*/
/* Table: hdi_core_supply_master                                */
/*==============================================================*/
create table hdi_core_supply_master
(
   supply_master_id     bigint(20) not null auto_increment comment '供货主单id',
   supplyno             varchar(50) comment '供货单编码',
   salno                varchar(50) comment '销售单号',
   sources_supplier_id  varchar(64) comment '原供应商id',
   sources_supplier_code varchar(128) comment '原供应商编码',
   sources_supplier_name varchar(128) comment '原供应商名称',
   supplier_id          bigint(20) comment '平台供应商id',
   supplier_code        varchar(128) comment '平台供应商编码',
   supplier_name        varchar(128) comment '平台供应商名称',
   sources_hospital_id  varchar(64) comment '原医院id',
   sources_hospital_code varchar(128) comment '原医院编码',
   sources_hospital_name varchar(128) comment '原医院名称',
   horg_id              bigint(20) comment '平台医院id',
   hospital_code        varchar(128) comment '平台医院编码',
   hospital_name        varchar(128) comment '平台医院名称',
   sources_storehouse_id varchar(50) comment '原医院库房id',
   storehouseid         bigint(20) comment '平台医院库房id',
   storehouse_no        varchar(100) comment '库房编码',
   storehouse_name      varchar(50) comment '库房名称',
   purchase_master_id   bigint(20) comment '采购主单id',
   purplanno            varchar(50) comment '采购计划编号',
   supply_type          int(1) comment '供货类型(0:非票货同行;1:票货同行)',
   supply_time          datetime comment '供货时间',
   expect_time          datetime comment '预计到货时间',
   supply_status        int(4) comment '供货状态(0:未提交;1:已提交;2:已验收;3:部分验收;4:拒收)',
   supply_addr          varchar(200) comment '供货地址',
   sourceid             varchar(50) comment 'ERP出库单标识',
   orgdataid            varchar(50) comment '来源数据id',
   datasource           int(1) comment '数据来源(1:手工;2: 接口)',
   memo                 varchar(200) comment '备注',
   dept_id              bigint(20) comment '供应商所属机构',
   del_flag             int(1) comment '是否删除(-1:已删除;0:正常)',
   cremanid             bigint(20) comment '创建人id',
   credate              datetime comment '创建时间',
   edit_id              bigint(20) comment '修改人',
   edit_time            datetime comment '修改时间',
   primary key (supply_master_id)
);

alter table hdi_core_supply_master comment '供货主单信息';

