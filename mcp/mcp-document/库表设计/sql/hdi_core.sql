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
   accept_detail_id     bigint(20) not null auto_increment comment '����ϸ��id',
   accept_master_id     bigint(20) comment '��������id',
   goodsclass           int(4) comment '��Ʒ���(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
   ygoodsid             varchar(50) comment 'ԭҽԺ��Ʒid',
   ygoodsno             varchar(50) comment 'ԭҽԺ��Ʒ����',
   ygoodsname           varchar(128) comment 'ԭҽԺ��Ʒ����',
   ygoodstypeid         varchar(50) comment 'ԭҽԺ��Ʒ���id',
   ygoodstypeno         varchar(64) comment 'ԭҽԺ��Ʒ������',
   ygoodstypename       varchar(128) comment 'ԭҽԺ��Ʒ�������',
   goodsid              bigint(20) comment 'ƽ̨ҽԺ��Ʒid',
   goodsno              varchar(30) comment 'ƽ̨ҽԺ��Ʒ����',
   goodsname            varchar(128) comment 'ƽ̨ҽԺ��Ʒ����',
   goodstypeid          bigint(20) comment 'ƽ̨ҽԺ��Ʒ���id',
   goodstypeno          varchar(64) comment 'ƽ̨ҽԺ��Ʒ������',
   goodstype            varchar(100) comment 'ƽ̨ҽԺ��Ʒ�������',
   goodsunit            varchar(64) comment '������Ʒ��λ',
   accept_qty           double(16,2) comment '��������',
   lotid                bigint(20) comment '����id',
   lotno                varchar(50) comment '��������',
   proddate             date comment '��������',
   invadate             date comment 'ʧЧ����',
   orgdataid            varchar(50) comment 'ԭ��������id',
   orgdatadtlid         varchar(50) comment 'ԭ����������ϸid',
   sourceid             bigint(20) comment '��������id',
   sourcedtlid          bigint(20) comment '����ϸ��id',
   memo                 varchar(200) comment '��ע',
   cremanid             bigint(20) comment '������id',
   credate              datetime comment '����ʱ��',
   primary key (accept_detail_id)
);

alter table hdi_core_accept_detail comment '����ϸ����Ϣ';

/*==============================================================*/
/* Table: hdi_core_accept_master                                */
/*==============================================================*/
create table hdi_core_accept_master
(
   accept_master_id     bigint(20) not null auto_increment comment '��������id',
   acceptno             varchar(50) comment '���յ����',
   his_supplyid         varchar(64) comment 'ԭ��Ӧ��id',
   sources_supplier_code varchar(128) comment 'ԭ��Ӧ�̱���',
   sources_supplier_name varchar(128) comment 'ԭ��Ӧ������',
   supplier_id          bigint(20) comment 'ƽ̨��Ӧ��id',
   supplier_code        varchar(128) comment 'ƽ̨��Ӧ�̱���',
   supplier_name        varchar(128) comment 'ƽ̨��Ӧ������',
   uorganid             varchar(64) comment 'ԭҽԺid',
   sources_hospital_code varchar(128) comment 'ԭҽԺ����',
   sources_hospital_name varchar(128) comment 'ԭҽԺ����',
   horg_id              bigint(20) comment 'ƽ̨ҽԺid',
   hospital_code        varchar(128) comment 'ƽ̨ҽԺ����',
   hospital_name        varchar(128) comment 'ƽ̨ҽԺ����',
   ystorehouseid        varchar(50) comment 'ԭҽԺ�ⷿid',
   storehouseid         bigint(20) comment 'ƽ̨�ⷿid',
   storehouse_no        varchar(100) comment '�ⷿ����',
   storehouse_name      varchar(50) comment '�ⷿ����',
   settle_flag          int(1) comment '�����ʶ(0:δ����;1:�ѽ���)',
   sourceid             bigint(20) comment '������id',
   orgdataid            varchar(50) comment 'ԭ���յ�id',
   datasource           int(1) comment '������Դ(1:�ֹ�;2: �ӿ�)',
   dept_id              bigint(20) comment '��Ӧ����������',
   memo                 varchar(200) comment '��ע',
   del_flag             int(1) comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   cremanid             bigint(20) comment '������',
   credate              datetime comment '����ʱ��',
   primary key (accept_master_id)
);

alter table hdi_core_accept_master comment '����������Ϣ';

/*==============================================================*/
/* Table: hdi_core_label_detail                                 */
/*==============================================================*/
create table hdi_core_label_detail
(
   labeldtlid           bigint(20) not null auto_increment comment '��ǩ��ϸid',
   labelid              bigint(20) comment '��ǩid',
   label_qty            double(16,2) comment '��ǩ����',
   supply_master_id     bigint(20) comment '��������id',
   supply_detail_id     bigint(20) comment '����ϸ��id',
   primary key (labeldtlid)
);

alter table hdi_core_label_detail comment '��ǩ��ϸ��Ϣ';

/*==============================================================*/
/* Table: hdi_core_label_master                                 */
/*==============================================================*/
create table hdi_core_label_master
(
   labelid              bigint(20) not null auto_increment comment '��ǩid',
   sources_supplier_id  varchar(64) comment 'ԭ��Ӧ��id',
   sources_supplier_code varchar(128) comment 'ԭ��Ӧ�̱���',
   sources_supplier_name varchar(128) comment 'ԭ��Ӧ������',
   supplier_id          bigint(20) comment 'ƽ̨��Ӧ��id',
   supplier_code        varchar(128) comment 'ƽ̨��Ӧ�̱���',
   supplier_name        varchar(128) comment 'ƽ̨��Ӧ������',
   sources_hospital_id  varchar(64) comment 'ԭҽԺid',
   sources_hospital_code varchar(128) comment 'ԭҽԺ����',
   sources_hospital_name varchar(128) comment 'ԭҽԺ����',
   horg_id              bigint(20) comment 'ƽ̨ҽԺid',
   hospital_code        varchar(128) comment 'ƽ̨ҽԺ����',
   hospital_name        varchar(128) comment 'ƽ̨ҽԺ����',
   sources_storehouse_id varchar(50) comment 'ԭҽԺ�ⷿid',
   storehouseid         bigint(20) comment 'ƽ̨�ⷿid',
   storehouse_no        varchar(100) comment '�ⷿ����',
   storehouse_name      varchar(50) comment '�ⷿ����',
   labelno              varchar(50) comment '��ǩ����',
   labelstatus          int(1) comment '��ǩ��ӡ״̬(0:δ��ӡ;1:�Ѵ�ӡ)',
   sourceid             bigint(20) comment '��������id',
   image_url            varchar(255) comment '��ǩ��ά��ͼƬ��ַ',
   dept_id              bigint(20) comment '��Ӧ����������',
   del_flag             int(1) comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   cremanid             bigint(20) comment '������',
   credate              datetime comment '����ʱ��',
   primary key (labelid)
);

alter table hdi_core_label_master comment '��ǩ��Ϣ';

/*==============================================================*/
/* Table: hdi_core_lot                                          */
/*==============================================================*/
create table hdi_core_lot
(
   lotid                bigint(20) not null auto_increment comment '����id',
   supplier_id          bigint(20) comment 'ƽ̨��Ӧ��id',
   dept_id              bigint(20) comment '��������id',
   goodsclass           int(4) comment '��Ʒ���(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
   goodsid              bigint(20) comment 'ƽ̨��Ʒid',
   goodstypeid          bigint(20) comment 'ƽ̨��Ӧ����Ʒ���id',
   lottype              int(1) comment '��������(1:������; 2:�������)',
   lotstatus            int(1) comment '����״̬(0:ͣ��;1:����)',
   lotno                varchar(50) comment '����',
   proddate             date comment '��������',
   invadate             date comment 'ʧЧ����',
   del_flag             int(1) comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (lotid)
);

alter table hdi_core_lot comment '��Ʒ������Ϣ';

/*==============================================================*/
/* Table: hdi_core_purchase_detail                              */
/*==============================================================*/
create table hdi_core_purchase_detail
(
   purchase_detail_id   bigint(20) not null auto_increment comment '�ɹ�ϸ��id',
   purchase_master_id   bigint(20) comment '�ɹ�����id',
   goodsclass           int(4) comment '��Ʒ���(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
   yhgoodsid            varchar(64) comment 'ԭҽԺ��Ʒid',
   yhgoodsname          varchar(128) comment 'ԭҽԺ��Ʒ����',
   yhgoodstypeid        varchar(50) comment 'ԭҽԺ��Ʒ���id',
   yhgoodsno            varchar(50) comment 'ԭҽԺ��Ʒ������',
   yhgoodstypename      varchar(128) comment 'ԭҽԺ��Ʒ�������',
   hgoodsunit           varchar(50) comment 'ҽԺ�ɹ���Ʒ��λ',
   hunitprice           double(16,2) comment '�ɹ�����',
   hqty                 double(16,2) comment 'ҽԺ�ɹ�����',
   hgoodsid             bigint(20) comment 'ƽ̨ҽԺ��Ʒid',
   hgoodsno             varchar(30) comment 'ƽ̨ҽԺ��Ʒ����',
   hgoodsname           varchar(128) comment 'ƽ̨ҽԺ��Ʒ����',
   hgoodstypeid         bigint(20) comment 'ƽ̨ҽԺ��Ʒ���id',
   hgoodstypeno         varchar(64) comment 'ƽ̨ҽԺ��Ʒ������',
   hgoodstype           varchar(100) comment 'ƽ̨ҽԺ��Ʒ�������',
   orgdataid            varchar(50) comment 'ԭ�ɹ�����id',
   orgdatadtlid         varchar(50) comment 'ԭ�ɹ�ϸ��id',
   sourceid             varchar(50) comment '������Դ��ʶ',
   sourcedtlid          varchar(50) comment '������Դ��ϸ��ʶ',
   memo                 varchar(200) comment '��ע',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (purchase_detail_id)
);

alter table hdi_core_purchase_detail comment '�ɹ�ϸ����Ϣ';

/*==============================================================*/
/* Table: hdi_core_purchase_master                              */
/*==============================================================*/
create table hdi_core_purchase_master
(
   purchase_master_id   bigint(20) not null auto_increment comment '�ɹ�����id',
   purplanno            varchar(50) comment '�ɹ������',
   sources_supplier_id  varchar(64) comment 'ԭ��Ӧ��id',
   sources_supplier_code varchar(128) comment 'ԭ��Ӧ�̱���',
   sources_supplier_name varchar(128) comment 'ԭ��Ӧ������',
   supplier_id          bigint(20) comment 'ƽ̨��Ӧ��id',
   supplier_code        varchar(128) comment 'ƽ̨��Ӧ�̱���',
   supplier_name        varchar(128) comment 'ƽ̨��Ӧ������',
   sources_hospital_id  varchar(64) comment 'ԭҽԺid',
   sources_hospital_code varchar(128) comment 'ԭҽԺ����',
   sources_hospital_name varchar(128) comment 'ԭҽԺ����',
   horg_id              bigint(20) comment 'ƽ̨ҽԺid',
   hospital_code        varchar(128) comment 'ƽ̨ҽԺ����',
   hospital_name        varchar(128) comment 'ƽ̨ҽԺ����',
   sources_storehouse_id varchar(50) comment 'ԭҽԺ�ⷿid',
   storehouseid         bigint(50) comment 'ƽ̨�ⷿid',
   storehouse_no        varchar(50) comment '�ⷿ����',
   storehouse_name      varchar(50) comment '�ⷿ����',
   purplantime          datetime comment '�ɹ�ʱ��',
   expecttime           datetime comment 'Ԥ�Ƶ���ʱ��',
   purchasestatus       int(4) comment '�ɹ���״̬(0:������;1:δȷ��;2:��ȷ��;3:�ѹ���;4:���ֹ���)',
   supply_addr          varchar(200) comment '������ַ',
   orgdataid            varchar(50) comment 'ԭ�ɹ���id',
   sourceid             varchar(50) comment '������Դ��ʶ',
   datasource           int(1) comment '������Դ(1:�ֹ�;2: �ӿ�)',
   dept_id              bigint(20) comment '��Ӧ����������',
   del_flag             int(1) comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   memo                 varchar(200) comment '��ע',
   cremanid             bigint(20) comment '������id',
   credate              datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (purchase_master_id)
);

alter table hdi_core_purchase_master comment '�ɹ�������Ϣ';

/*==============================================================*/
/* Table: hdi_core_storehouse                                   */
/*==============================================================*/
create table hdi_core_storehouse
(
   storehouseid         bigint(20) not null auto_increment comment '�ⷿid',
   storehousename       varchar(50) comment '�ⷿ����',
   storehouseno         varchar(100) comment '�ⷿ����',
   shaddress            varchar(200) comment '�ⷿ��ַ',
   orgdataid            varchar(50) comment 'ԭҽԺ�ⷿid',
   uorganid             varchar(50) comment 'ԭҽԺ����id',
   horg_id              bigint(20) comment 'ƽ̨ҽԺid',
   dept_id              bigint(20) comment '��������id',
   cremanid             bigint(20) comment '������id',
   cremanname           varchar(30) comment '����������',
   credate              datetime comment '����ʱ��',
   editmanid            bigint(20) comment '�޸���id',
   editmanname          varchar(30) comment '�޸�������',
   editdate             datetime comment '�޸�ʱ��',
   memo                 varchar(200) comment '��ע',
   data_source          int(1) comment '������Դ(0:ϵͳ¼��;1:ҽԺSPD)',
   primary key (storehouseid)
);

alter table hdi_core_storehouse comment 'ҽԺ�ⷿ��Ϣ';

/*==============================================================*/
/* Table: hdi_core_supply_detail                                */
/*==============================================================*/
create table hdi_core_supply_detail
(
   supply_detail_id     bigint(20) not null auto_increment comment '����ϸ��id',
   supply_master_id     bigint(20) comment '��������id',
   purchase_master_id   bigint(20) comment '�ɹ�����id',
   purchase_detail_id   bigint(20) comment '�ɹ�ϸ��id',
   sourceid             varchar(50) comment 'ERP���ⵥ��ʶ',
   sourcedtlid          varchar(50) comment 'ERP������ϸ����ʶ',
   purplanid            varchar(50) comment '�ɹ��ƻ���ʶ',
   purplandtlid         varchar(50) comment '�ɹ��ƻ���ϸ��ʶ',
   goodsclass           int(4) comment '��Ʒ���(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
   goodsid              bigint(20) comment '��Ӧ����Ʒid',
   goodsno              varchar(128) comment '��Ӧ����Ʒ����',
   goodsname            varchar(128) comment '��Ӧ����Ʒ����',
   goodstypeid          bigint(20) comment '��Ӧ����Ʒ���id',
   goodstypeno          varchar(128) comment '��Ӧ����Ʒ������',
   goodstype            varchar(128) comment '��Ӧ����Ʒ�������',
   goodsunit            varchar(50) comment '��Ӧ�̲ɹ���λ',
   supply_qty           double(16,2) comment '��������',
   supply_unitprice     double(16,2) comment '��������',
   lotid                bigint(20) comment '����id',
   lotno                varchar(50) comment '��������',
   proddate             date comment '��������',
   invadate             date comment 'ʧЧ����',
   batch_code           varchar(255) comment '���α���',
   image_url            varchar(255) comment '���ζ�ά��ͼƬ��ַ',
   orgdataid            varchar(50) comment 'ԭ���ݱ�ʶ',
   orgdatadtlid         varchar(50) comment 'ԭ������ϸ��ʶ',
   memo                 varchar(200) comment '��ע',
   cremanid             bigint(20) comment '������id',
   credate              datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (supply_detail_id)
);

alter table hdi_core_supply_detail comment '����ϸ����Ϣ';

/*==============================================================*/
/* Table: hdi_core_supply_master                                */
/*==============================================================*/
create table hdi_core_supply_master
(
   supply_master_id     bigint(20) not null auto_increment comment '��������id',
   supplyno             varchar(50) comment '����������',
   salno                varchar(50) comment '���۵���',
   sources_supplier_id  varchar(64) comment 'ԭ��Ӧ��id',
   sources_supplier_code varchar(128) comment 'ԭ��Ӧ�̱���',
   sources_supplier_name varchar(128) comment 'ԭ��Ӧ������',
   supplier_id          bigint(20) comment 'ƽ̨��Ӧ��id',
   supplier_code        varchar(128) comment 'ƽ̨��Ӧ�̱���',
   supplier_name        varchar(128) comment 'ƽ̨��Ӧ������',
   sources_hospital_id  varchar(64) comment 'ԭҽԺid',
   sources_hospital_code varchar(128) comment 'ԭҽԺ����',
   sources_hospital_name varchar(128) comment 'ԭҽԺ����',
   horg_id              bigint(20) comment 'ƽ̨ҽԺid',
   hospital_code        varchar(128) comment 'ƽ̨ҽԺ����',
   hospital_name        varchar(128) comment 'ƽ̨ҽԺ����',
   sources_storehouse_id varchar(50) comment 'ԭҽԺ�ⷿid',
   storehouseid         bigint(20) comment 'ƽ̨ҽԺ�ⷿid',
   storehouse_no        varchar(100) comment '�ⷿ����',
   storehouse_name      varchar(50) comment '�ⷿ����',
   purchase_master_id   bigint(20) comment '�ɹ�����id',
   purplanno            varchar(50) comment '�ɹ��ƻ����',
   supply_type          int(1) comment '��������(0:��Ʊ��ͬ��;1:Ʊ��ͬ��)',
   supply_time          datetime comment '����ʱ��',
   expect_time          datetime comment 'Ԥ�Ƶ���ʱ��',
   supply_status        int(4) comment '����״̬(0:δ�ύ;1:���ύ;2:������;3:��������;4:����)',
   supply_addr          varchar(200) comment '������ַ',
   sourceid             varchar(50) comment 'ERP���ⵥ��ʶ',
   orgdataid            varchar(50) comment '��Դ����id',
   datasource           int(1) comment '������Դ(1:�ֹ�;2: �ӿ�)',
   memo                 varchar(200) comment '��ע',
   dept_id              bigint(20) comment '��Ӧ����������',
   del_flag             int(1) comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   cremanid             bigint(20) comment '������id',
   credate              datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (supply_master_id)
);

alter table hdi_core_supply_master comment '����������Ϣ';

