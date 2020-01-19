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
   cate_id              bigint(20) not null auto_increment comment '����id',
   cate_name            varchar(30) comment '��������',
   cate_no              varchar(30) comment '�������',
   level_order          bigint(10) comment '�㼶����',
   cate_level           int(4) comment '���ڵ�Ϊ1����һ�㣬��������',
   pcate_id             bigint(20) comment '������id',
   dept_id              bigint(20) comment '��������id',
   memo                 varchar(200) comment '��ע',
   del_flag             int(1) comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (cate_id)
);

alter table hdi_unicode_consumables_cate comment '�Ĳķ�����Ϣ';

/*==============================================================*/
/* Table: hdi_unicode_drugs_cate                                */
/*==============================================================*/
create table hdi_unicode_drugs_cate
(
   cate_id              bigint(20) not null auto_increment comment '����id',
   cate_name            varchar(30) comment '��������',
   cate_no              varchar(30) comment '�������',
   level_order          bigint(10) comment '�㼶����',
   cate_level           int(4) comment '���ڵ�Ϊ1����һ�㣬��������',
   pcate_id             bigint(20) comment '������id',
   dept_id              bigint(20) comment '��������id',
   memo                 varchar(200) comment '��ע',
   del_flag             int(1) comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (cate_id)
);

alter table hdi_unicode_drugs_cate comment 'ҩƷ������Ϣ';

/*==============================================================*/
/* Table: hdi_unicode_goods_ship                                */
/*==============================================================*/
create table hdi_unicode_goods_ship
(
   ship_id              bigint(20) not null auto_increment comment '��ϵid',
   dept_id              bigint(20) comment '��������id',
   torg_id              bigint(20) comment 'Ŀ�������ʶ',
   torg_type            int(4) comment 'Ŀ���������(0:ҽԺ;1:��Ӧ��)',
   tgoods_type          int(4) comment '��Ʒ����(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
   tgoods_id            bigint(20) comment 'Ŀ����Ʒid',
   pgoods_id            bigint(20) comment 'ƽ̨��Ʒid',
   tspecs_id            bigint(20) comment 'Ŀ����id',
   pspecs_id            bigint(20) comment 'ƽ̨���id',
   tapproval_id         bigint(20) comment 'Ŀ����Ʒ����id',
   papproval_id         bigint(20) comment 'ƽ̨��Ʒ����id',
   ship_flag            int(1) comment '�Ƿ�ƥ��(0:δƥ��,1:��ƥ��)',
   check_status         int(1) comment '���״̬(0:δ���,1:�����)',
   del_flag             int(1) comment '�Ƿ�ɾ��(0:��,1:��)',
   cremanid             bigint(20) comment '������id',
   cremanname           varchar(30) comment '����������',
   credate              datetime comment '����ʱ��',
   editmanid            bigint(20) comment '�޸���id',
   editmanname          varchar(30) comment '�޸�������',
   editdate             datetime comment '�޸�ʱ��',
   memo                 varchar(200) comment '��ע',
   primary key (ship_id)
);

alter table hdi_unicode_goods_ship comment '��Ʒƥ���ϵ';

/*==============================================================*/
/* Table: hdi_unicode_goods_ship_hist                           */
/*==============================================================*/
create table hdi_unicode_goods_ship_hist
(
   shiphist_id          bigint(20) not null auto_increment comment '��ʷ��¼id',
   ship_id              bigint(20) comment '��ϵid',
   dept_id              bigint(20) comment '��������id',
   torg_id              bigint(20) comment 'Ŀ�������ʶ',
   torg_type            int(4) comment 'Ŀ���������(0:ҽԺ;1:��Ӧ��)',
   tgoods_type          int(4) comment '��Ʒ����(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
   tgoods_id            bigint(20) comment 'Ŀ����Ʒid',
   pgoods_id            bigint(20) comment 'ƽ̨��Ʒid',
   tspecs_id            bigint(20) comment 'Ŀ����id',
   pspecs_id            bigint(20) comment 'ƽ̨���id',
   tapproval_id         bigint(20) comment 'Ŀ����Ʒ����id',
   papproval_id         bigint(20) comment 'ƽ̨��Ʒ����id',
   ship_flag            int(1) comment '�Ƿ�ƥ��(0:δƥ��,1:��ƥ��)',
   check_status         int(1) comment '���״̬(0:δ���,1:�����)',
   oper_type            int(1) comment '��������(1:ƥ��,2:��Ʒ��Ϣ���)',
   del_flag             int(1) comment '�Ƿ�ɾ��(0:��,1:��)',
   cremanid             bigint(20) comment '������id',
   cremanname           varchar(30) comment '����������',
   credate              datetime comment '����ʱ��',
   editmanid            bigint(20) comment '�޸���id',
   editmanname          varchar(30) comment '�޸�������',
   editdate             datetime comment '�޸�ʱ��',
   memo                 varchar(200) comment '��ע',
   primary key (shiphist_id)
);

alter table hdi_unicode_goods_ship_hist comment '��Ʒƥ���ϵ��ʷ��¼';

/*==============================================================*/
/* Table: hdi_unicode_reagent_cate                              */
/*==============================================================*/
create table hdi_unicode_reagent_cate
(
   cate_id              bigint(20) not null auto_increment comment '����id',
   cate_name            varchar(30) comment '��������',
   cate_no              varchar(30) comment '�������',
   level_order          bigint(10) comment '�㼶����',
   cate_level           int(4) comment '���ڵ�Ϊ1����һ�㣬��������',
   pcate_id             bigint(20) comment '������id',
   dept_id              bigint(20) comment '��������id',
   memo                 varchar(200) comment '��ע',
   del_flag             int(1) comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (cate_id)
);

alter table hdi_unicode_reagent_cate comment '�Լ�������Ϣ';

/*==============================================================*/
/* Table: hdi_unicode_supply_ship                               */
/*==============================================================*/
create table hdi_unicode_supply_ship
(
   ship_id              bigint(20) not null auto_increment comment '��ϵid',
   dept_id              bigint(20) comment '����id',
   supplier_hospital_ref_id bigint(20) comment 'ƽ̨ҽԺ��Ӧ�̹�ϵid',
   hospital_id          bigint(20) comment 'ƽ̨ҽԺid',
   supplier_id          bigint(20) comment 'ƽ̨��Ӧ��id',
   sources_ship_id      varchar(50) comment 'ԭҽԺ��Ӧ�̹�ϵid',
   sources_supplier_id  varchar(50) comment 'ԭҽԺ��Ӧ��id',
   sources_supplier_name varchar(128) comment 'ԭҽԺ��Ӧ������',
   "sources_supplier_credit_code ԭҽԺ��Ӧ�̱���" varchar(128) comment 'ԭҽԺ��Ӧ�̱���',
   sources_hospital_id  varchar(50) comment 'ԭҽԺid',
   sources_hospital_name varchar(128) comment 'ԭҽԺ����',
   sources_hospital_credit_code varchar(128) comment 'ԭҽԺ����',
   ship_flag2           int(1) comment '�Ƿ�ƥ��(0:δƥ��,1:��ƥ��)',
   check_status         int(1) comment '���״̬(0:δ���,1:�����)',
   cremanid             bigint(20) comment '������id',
   cremanname           varchar(30) comment '����������',
   credate              datetime comment '����ʱ��',
   editmanid            bigint(20) comment '�޸���id',
   editmanname          varchar(30) comment '�޸�������',
   editdate             datetime comment '�޸�ʱ��',
   datasource           int(2) comment '������Դ(1:�ӿ�,2:�ֹ�)',
   del_flag             int(1) comment '�Ƿ�ɾ��(0:��,1:��)',
   memo                 varchar(200) comment '��ע',
   primary key (ship_id)
);

alter table hdi_unicode_supply_ship comment '��Ӧ��ƥ���ϵ';

