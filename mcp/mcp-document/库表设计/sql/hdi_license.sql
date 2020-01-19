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
   id                   bigint(20) not null auto_increment comment '����id',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   agent_id             bigint(20) not null comment '������id',
   classify_id          bigint(20) not null comment '����id',
   name                 varchar(128) not null comment '֤������',
   number               varchar(128) not null comment '֤�ձ��',
   begin_time           datetime not null comment 'Ч�ڿ�ʼʱ��',
   end_time             datetime not null comment 'Ч�ڽ���ʱ��',
   pic_url              varchar(256) comment '֤��ͼƬ',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   new_license_id       bigint(20) comment '��֤��id(��֤)',
   primary key (id)
);

alter table hdi_license_agent_info comment '������֤����Ϣ';

/*==============================================================*/
/* Table: hdi_license_classify_info                             */
/*==============================================================*/
create table hdi_license_classify_info
(
   id                   bigint(20) not null auto_increment comment '����id',
   type                 int(1) not null comment '֤������(0:��Ʒ֤��;1:��Ӧ��֤��;2:����֤��;3:������֤��)',
   name                 varchar(64) not null comment '��������',
   is_warning           int(1) not null comment '�Ƿ�Ԥ��(0:��;1:��)',
   early_date           int(3) not null comment 'Ԥ������',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (id)
);

alter table hdi_license_classify_info comment '֤�շ�����Ϣ';

/*==============================================================*/
/* Table: hdi_license_factory_info                              */
/*==============================================================*/
create table hdi_license_factory_info
(
   id                   bigint(20) not null auto_increment comment '����id',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   factory_id           bigint(20) not null comment '����id',
   classify_id          bigint(20) not null comment '����id',
   name                 varchar(128) not null comment '֤������',
   number               varchar(128) not null comment '֤�ձ��',
   begin_time           datetime not null comment 'Ч�ڿ�ʼʱ��',
   end_time             datetime not null comment 'Ч�ڽ���ʱ��',
   pic_url              varchar(256) comment '֤��ͼƬ',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   new_license_id       bigint(20) comment '��֤��id(��֤)',
   primary key (id)
);

alter table hdi_license_factory_info comment '����֤����Ϣ';

/*==============================================================*/
/* Table: hdi_license_goods_info                                */
/*==============================================================*/
create table hdi_license_goods_info
(
   id                   bigint(20) not null auto_increment comment '����id',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   goods_id             bigint(20) not null comment '��Ʒid',
   goods_type           int(1) not null comment '��Ʒ���(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
   classify_id          bigint(20) not null comment '����id',
   name                 varchar(128) not null comment '֤������',
   number               varchar(128) not null comment '֤�ձ��',
   begin_time           datetime not null comment 'Ч�ڿ�ʼʱ��',
   end_time             datetime not null comment 'Ч�ڽ���ʱ��',
   pic_url              varchar(256) comment '֤��ͼƬ',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   new_license_id       bigint(20) comment '��֤��id(��֤)',
   primary key (id)
);

alter table hdi_license_goods_info comment '��Ʒ֤����Ϣ';

/*==============================================================*/
/* Table: hdi_license_hospital_examine                          */
/*==============================================================*/
create table hdi_license_hospital_examine
(
   id                   bigint(20) not null auto_increment comment '����id',
   license_id           bigint(20) not null comment '֤��id',
   license_type         int(1) not null comment '֤������(0:��Ʒ֤��;1:��Ӧ��֤��;2:����֤��;3:������֤��)',
   classify_id          bigint(20) comment '����id',
   name                 varchar(128) not null comment '֤������',
   number               varchar(128) not null comment '֤�ձ��',
   begin_time           datetime comment 'Ч�ڿ�ʼʱ��',
   end_time             datetime comment 'Ч�ڽ���ʱ��',
   pic_url              varchar(256) comment '֤��ͼƬ',
   business_id          bigint(20) not null comment 'ҵ��id(��Ʒid��Ӧ��id)',
   business_name        varchar(128) comment 'ҵ������(��Ʒ���ƻ�Ӧ������)',
   hospital_id          bigint(20) not null comment 'ҽԺid',
   hospital_name        varchar(128) comment 'ҽԺ����',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   status               int(1) not null comment '���״̬(0:������;1:���ͨ��;2:��˲�ͨ��)',
   examine_opinion      varchar(512) comment '�������',
   examine_time         datetime comment '����ʱ��',
   primary key (id)
);

alter table hdi_license_hospital_examine comment '֤��ҽԺ����';

/*==============================================================*/
/* Table: hdi_license_supplier_info                             */
/*==============================================================*/
create table hdi_license_supplier_info
(
   id                   bigint(20) not null auto_increment comment '����id',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   classify_id          bigint(20) not null comment '����id',
   name                 varchar(128) not null comment '֤������',
   number               varchar(128) not null comment '֤�ձ��',
   begin_time           datetime not null comment 'Ч�ڿ�ʼʱ��',
   end_time             datetime not null comment 'Ч�ڽ���ʱ��',
   pic_url              varchar(256) comment '֤��ͼƬ',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   new_license_id       bigint(20) comment '��֤��id(��֤)',
   primary key (id)
);

alter table hdi_license_supplier_info comment '��Ӧ��֤����Ϣ';

