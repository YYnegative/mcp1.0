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
   id                   bigint(20) not null auto_increment comment '����id',
   apply_master_id      bigint(20) not null comment '�˻���������id',
   accept_master_id     bigint(20) comment '��֤����id',
   accept_no            varchar(64) comment '���յ����',
   accept_detail_id     bigint(20) comment '������ϸ��id',
   goods_type           int(1) comment '��Ʒ����(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
   goods_id             bigint(20) not null comment 'ҽԺ��Ʒid',
   sources_goods_id     varchar(64) comment 'ԭҽԺ��Ʒid',
   specs_id             bigint(20) comment 'ҽԺ��Ʒ���id',
   sources_specs_id     varchar(64) comment 'ԭҽԺ��Ʒ���id',
   lot_id               bigint(20) not null comment '��������id',
   goods_unit_code      varchar(64) comment '��Ʒ��λ����',
   apply_refunds_number int(11) comment '�����˻�����',
   reality_refunds_number int(11) comment 'ʵ���˻�����',
   supply_price         decimal(11,2) comment '��������',
   refunds_remark       varchar(512) comment '�˻�ԭ��',
   create_id            bigint(20) not null comment '������id',
   create_time          datetime not null comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (id)
);

alter table hdi_refunds_apply_detail comment '�˻����뵥��ϸ��Ϣ';

/*==============================================================*/
/* Table: hdi_refunds_apply_master                              */
/*==============================================================*/
create table hdi_refunds_apply_master
(
   id                   bigint(20) not null auto_increment comment '����id',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   sources_supplier_id  varchar(64) comment 'ԭϵͳҽԺ��Ӧ��id',
   hospital_id          bigint(20) not null comment 'ҽԺid',
   sources_hospital_id  varchar(64) comment 'ԭϵͳҽԺid',
   store_house_id       bigint(20) not null comment 'ҽԺ�ⷿid',
   sources_store_house_id varchar(64) comment 'ԭϵͳҽԺ�ⷿid',
   refunds_apply_no     varchar(64) not null comment 'ҽԺ�˻����뵥��',
   apply_time           datetime not null comment '����ʱ��',
   status               int(1) not null comment '״̬(0:δȷ��;1:������;2:��ȷ��;3:���˻�;4:�����˻�)',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   data_source          int(1) comment '������Դ(0:ϵͳ¼��;1:ҽԺSPD)',
   primary key (id)
);

alter table hdi_refunds_apply_master comment '�˻����뵥��Ϣ';

/*==============================================================*/
/* Table: hdi_refunds_detail                                    */
/*==============================================================*/
create table hdi_refunds_detail
(
   id                   bigint(20) not null auto_increment comment '����id',
   master_id            bigint(20) not null comment '�˻�����id',
   sources_type         int(1) comment '��Դ����(0:�˻����뵥;1:���յ�;ҽԺ�˻����뵥�Ų�Ϊ����Ϊ0,����Ϊ1)',
   sources_master_id    bigint(20) comment '��Դ����id(������Դ����,��Ӧ�˻�������������������id)',
   sources_detail_id    bigint(20) comment '��Դ��ϸ��id(������Դ����,��Ӧ�˻�������ϸ����������ϸ��id)',
   accept_no            varchar(64) comment '���յ����',
   goods_type           int(1) comment '��Ʒ����(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
   goods_id             bigint(20) not null comment 'ҽԺ��Ʒid',
   sources_goods_id     varchar(64) comment 'ԭҽԺ��Ʒid',
   specs_id             bigint(20) not null comment 'ҽԺ��Ʒ���id',
   sources_specs_id     varchar(64) comment 'ԭҽԺ��Ʒ���id',
   lot_id               bigint(20) comment '��������id',
   goods_unit_code      varchar(64) comment '��Ʒ��λ����',
   apply_refunds_number int(11) comment '�����˻�����',
   reality_refunds_number int(11) comment 'ʵ���˻�����',
   refunds_price        decimal(11,2) comment '�˻�����',
   refunds_remark       varchar(512) comment '�˻�ԭ��',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) not null comment '������id',
   create_time          datetime not null comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (id)
);

alter table hdi_refunds_detail comment '�˻�����ϸ��Ϣ';

/*==============================================================*/
/* Table: hdi_refunds_master                                    */
/*==============================================================*/
create table hdi_refunds_master
(
   id                   bigint(20) not null auto_increment comment '����id',
   refunds_no           varchar(64) not null comment '�˻������',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   sources_supplier_id  varchar(64) comment 'ԭϵͳҽԺ��Ӧ��id',
   hospital_id          bigint(20) not null comment 'ҽԺid',
   sources_hospital_id  varchar(64) comment 'ԭϵͳҽԺid',
   store_house_id       bigint(20) not null comment 'ҽԺ�ⷿid',
   sources_store_house_id varchar(64) comment 'ԭϵͳҽԺ�ⷿid',
   refunds_apply_no     varchar(64) comment 'ҽԺ�˻����뵥��',
   regression_number    varchar(64) comment '���˵���',
   refunds_time         datetime not null comment '�˻�ʱ��',
   status               int(1) not null comment '״̬(0:δȷ��;1:���ύ;2:�����)',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   data_source          int(1) comment '������Դ(0:ϵͳ¼��;1:ҽԺSPD)',
   primary key (id)
);

alter table hdi_refunds_master comment '�˻�����Ϣ';

