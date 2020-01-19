/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/6/21 9:04:46                            */
/*==============================================================*/


drop table if exists temp_spd_license_classify;

drop table if exists temp_spd_license_factory;

drop table if exists temp_spd_license_goods;

drop table if exists temp_spd_license_supplier;

/*==============================================================*/
/* Table: temp_spd_license_classify                        */
/*==============================================================*/
create table temp_spd_license_classify
(
   id                   varchar(50) not null comment '����id',
   type                 decimal(1,0) not null comment '֤������(0:��Ʒ֤��;1:��Ӧ��֤��;2:����֤��;3:������֤��)',
   name                 varchar(128) not null comment '��������',
   is_warning           decimal(1,0) not null comment '�Ƿ�Ԥ��(0:��;1:��)',
   early_date           decimal(3,0) not null comment 'Ԥ������',
   status               decimal(1,0) not null comment '״̬(0:ͣ��;1:����)',
   dept_id              varchar(50) comment '��������(ҽԺid)',
   create_id            varchar(50) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              varchar(50) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             decimal(1,0) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (id)
);

alter table temp_spd_license_classify comment '֤�շ�����Ϣ';

/*==============================================================*/
/* Table: temp_spd_license_factory                         */
/*==============================================================*/
create table temp_spd_license_factory
(
   id                   varchar(50) not null comment '����id',
   supplier_id          varchar(50) not null comment '��Ӧ��id',
   factory_id           varchar(50) not null comment '����id',
   classify_id          varchar(50) not null comment '����id',
   name                 varchar(128) not null comment '֤������',
   number               varchar(128) not null comment '֤�ձ��',
   begin_time           datetime not null comment 'Ч�ڿ�ʼʱ��',
   end_time             datetime not null comment 'Ч�ڽ���ʱ��',
   pic_url              varchar(256) comment '֤��ͼƬ',
   status               decimal(1,0) not null comment '״̬(0:ͣ��;1:����)',
   dept_id              varchar(50) comment '��������(ҽԺid)',
   create_id            varchar(50) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              varchar(50) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             decimal(1,0) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   new_license_id       varchar(50) comment '��֤��id(��֤)',
   primary key (id)
);

alter table temp_spd_license_factory comment '����֤����Ϣ';

/*==============================================================*/
/* Table: temp_spd_license_goods                           */
/*==============================================================*/
create table temp_spd_license_goods
(
   id                   varchar(50) not null comment '����id',
   supplier_id          varchar(50) comment '��Ӧ��id',
   goods_specs_id       varchar(50) not null comment '��Ʒ���id',
   goods_type           decimal(1,0) not null comment '��Ʒ���(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
   classify_id          varchar(50) not null comment '����id',
   name                 varchar(128) not null comment '֤������',
   number               varchar(128) not null comment '֤�ձ��',
   begin_time           datetime not null comment 'Ч�ڿ�ʼʱ��',
   end_time             datetime not null comment 'Ч�ڽ���ʱ��',
   pic_url              varchar(256) comment '֤��ͼƬ',
   status               decimal(1,0) not null comment '״̬(0:ͣ��;1:����)',
   dept_id              varchar(50) comment '��������(ҽԺid)',
   create_id            varchar(50) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              varchar(50) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             decimal(1,0) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   new_license_id       varchar(50) comment '��֤��id(��֤)',
   primary key (id)
);

alter table temp_spd_license_goods comment '��Ʒ֤����Ϣ';

/*==============================================================*/
/* Table: temp_spd_license_supplier                        */
/*==============================================================*/
create table temp_spd_license_supplier
(
   id                   varchar(50) not null comment '����id',
   supplier_id          varchar(50) not null comment '��Ӧ��id',
   classify_id          varchar(50) not null comment '����id',
   name                 varchar(128) not null comment '֤������',
   number               varchar(128) not null comment '֤�ձ��',
   begin_time           datetime not null comment 'Ч�ڿ�ʼʱ��',
   end_time             datetime not null comment 'Ч�ڽ���ʱ��',
   pic_url              varchar(256) comment '֤��ͼƬ',
   status               decimal(1,0) not null comment '״̬(0:ͣ��;1:����)',
   dept_id              varchar(50) comment '��������(ҽԺid)',
   create_id            varchar(50) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              varchar(50) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             decimal(1,0) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   new_license_id       varchar(50) comment '��֤��id(��֤)',
   primary key (id)
);

alter table temp_spd_license_supplier comment '��Ӧ��֤����Ϣ';

