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
   id                   bigint(20) not null auto_increment comment '����id',
   sources_id           varchar(64) comment 'ԭϵͳ��Ʒid',
   hospital_id          bigint(20) not null comment 'ҽԺid',
   consumables_code     varchar(64) not null comment '�Ĳı���',
   consumables_name     varchar(128) not null comment '�Ĳ�����',
   common_name          varchar(128) comment 'ͨ������',
   goods_nature         int(1) not null comment '��Ʒ����(0:����;1:����)',
   type_id              varchar(64) comment '��Ʒ����id',
   type_name            varchar(128) comment '��Ʒ��������',
   factory_id           varchar(128) not null comment '��������id',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   goods_unit           varchar(64) comment '��Ʒ��λ',
   buying_unit          varchar(64) comment '�ɹ���λ',
   convert_unit         varchar(128) comment 'ת����λ',
   pic_url              varchar(256) comment 'ͼƬ��ַ',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   is_match             int(1) default 0 comment '�Ƿ�ƥ��(0:δƥ��;1:��ƥ��)',
   data_source          int(1) comment '������Դ(0:ϵͳ¼��;1:ҽԺSPD)',
   primary key (id)
);

alter table hdi_goods_hospital_consumables comment 'ҽԺ�Ĳ���Ϣ';

/*==============================================================*/
/* Table: hdi_goods_hospital_consumables_approvals              */
/*==============================================================*/
create table hdi_goods_hospital_consumables_approvals
(
   id                   bigint(20) not null auto_increment comment '����id',
   consumables_id       bigint(20) not null comment '�Ĳ�id',
   approvals            varchar(128) not null comment '��׼�ĺ�',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_hospital_consumables_approvals comment 'ҽԺ�Ĳ���׼�ĺ�';

/*==============================================================*/
/* Table: hdi_goods_hospital_consumables_specs                  */
/*==============================================================*/
create table hdi_goods_hospital_consumables_specs
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_specs_id     varchar(64) comment 'ԭϵͳ��Ʒ���id',
   consumables_id       bigint(20) not null comment '�Ĳ�id',
   specs_code           varchar(64) not null comment '������',
   specs                varchar(128) not null comment '��Ʒ���',
   guid                 varchar(128) comment 'ȫ��Ψһ��',
   status               int(1) not null DEFAULT '1' comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_hospital_consumables_specs comment 'ҽԺ�ĲĹ��';

/*==============================================================*/
/* Table: hdi_goods_hospital_drugs                              */
/*==============================================================*/
create table hdi_goods_hospital_drugs
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_id           varchar(64) comment 'ԭϵͳ��Ʒid',
   hospital_id          bigint(20) not null comment 'ҽԺid',
   drugs_code           varchar(64) not null comment 'ҩƷ����',
   drugs_name           varchar(128) not null comment 'ҩƷ����',
   common_name          varchar(128) comment 'ͨ������',
   goods_nature         int(1) not null comment '��Ʒ����(0:����;1:����)',
   type_id              varchar(64) comment '��Ʒ����id',
   type_name            varchar(128) comment '��Ʒ��������',
   factory_id           varchar(128) not null comment '��������id',
   approvals            varchar(128) comment '��׼�ĺ�',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   goods_unit           varchar(64) comment '��Ʒ��λ',
   buying_unit          varchar(64) comment '�ɹ���λ',
   convert_unit         varchar(128) comment 'ת����λ',
   pic_url              varchar(256) comment 'ͼƬ��ַ',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   is_match             int(1) default 0 comment '�Ƿ�ƥ��(0:δƥ��;1:��ƥ��)',
   data_source          int(1) comment '������Դ(0:ϵͳ¼��;1:ҽԺSPD)',
   primary key (id)
);

alter table hdi_goods_hospital_drugs comment 'ҽԺҩƷ��Ϣ';

/*==============================================================*/
/* Table: hdi_goods_hospital_drugs_specs                        */
/*==============================================================*/
create table hdi_goods_hospital_drugs_specs
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_specs_id     varchar(64) comment 'ԭϵͳ��Ʒ���id',
   drugs_id             bigint(20) not null comment 'ҩƷid',
   specs_code           varchar(64) not null comment '������',
   specs                varchar(128) not null comment '��Ʒ���',
   guid                 varchar(128) comment 'ȫ��Ψһ��',
   status               int(1) not null DEFAULT '1' comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_hospital_drugs_specs comment 'ҽԺҩƷ���';

/*==============================================================*/
/* Table: hdi_goods_hospital_reagent                            */
/*==============================================================*/
create table hdi_goods_hospital_reagent
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_id           varchar(64) comment 'ԭϵͳ��Ʒid',
   hospital_id          bigint(20) not null comment 'ҽԺid',
   reagent_code         varchar(64) not null comment '�Լ�����',
   reagent_name         varchar(128) not null comment '�Լ�����',
   common_name          varchar(128) comment 'ͨ������',
   goods_nature         int(1) not null comment '��Ʒ����(0:����;1:����)',
   type_id              varchar(64) comment '��Ʒ����id',
   type_name            varchar(128) comment '��Ʒ��������',
   factory_id           varchar(128) not null comment '��������id',
   approvals            varchar(128) comment '��׼�ĺ�',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   goods_unit           varchar(64) comment '��Ʒ��λ',
   buying_unit          varchar(64) comment '�ɹ���λ',
   convert_unit         varchar(128) comment 'ת����λ',
   pic_url              varchar(256) comment 'ͼƬ��ַ',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   is_match             int(1) default 0 comment '�Ƿ�ƥ��(0:δƥ��;1:��ƥ��)',
   data_source          int(1) comment '������Դ(0:ϵͳ¼��;1:ҽԺSPD)',
   primary key (id)
);

alter table hdi_goods_hospital_reagent comment 'ҽԺ�Լ���Ϣ';

/*==============================================================*/
/* Table: hdi_goods_hospital_reagent_specs                      */
/*==============================================================*/
create table hdi_goods_hospital_reagent_specs
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_specs_id     varchar(64) comment 'ԭϵͳ��Ʒ���id',
   reagen_id            bigint(20) not null comment '�Լ�id',
   specs_code           varchar(64) not null comment '������',
   specs                varchar(128) not null comment '��Ʒ���',
   guid                 varchar(128) comment 'ȫ��Ψһ��',
   status               int(1) not null DEFAULT '1' comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_hospital_reagent_specs comment 'ҽԺ�Լ����';

/*==============================================================*/
/* Table: hdi_goods_platform_consumables                        */
/*==============================================================*/
create table hdi_goods_platform_consumables
(
   id                   bigint(20) not null auto_increment comment '����id',
   goods_unicode        varchar(128) not null comment '��Ʒͳһ����',
   consumables_code     varchar(64) not null comment '�Ĳı���',
   consumables_name     varchar(128) not null comment '�Ĳ�����',
   common_name          varchar(128) comment 'ͨ������',
   goods_nature         int(1) not null comment '��Ʒ����(0:����;1:����)',
   type_id              bigint(20) not null comment '��Ʒ����id',
   factory_id           varchar(128) not null comment '��������id',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   goods_unit           varchar(64) not null comment '��Ʒ��λ',
   store_way            varchar(64) comment '���淽ʽ',
   pic_url              varchar(256) comment 'ͼƬ��ַ',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (id)
);

alter table hdi_goods_platform_consumables comment 'ƽ̨�Ĳ���Ϣ';

/*==============================================================*/
/* Table: hdi_goods_platform_consumables_approvals              */
/*==============================================================*/
create table hdi_goods_platform_consumables_approvals
(
   id                   bigint(20) not null auto_increment comment '����id',
   consumables_id       bigint(20) not null comment '�Ĳ�id',
   approvals            varchar(128) not null comment '��׼�ĺ�',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_platform_consumables_approvals comment 'ƽ̨�Ĳ���׼�ĺ�';

/*==============================================================*/
/* Table: hdi_goods_platform_consumables_specs                  */
/*==============================================================*/
create table hdi_goods_platform_consumables_specs
(
   id                   bigint(20) not null auto_increment comment '����id',
   consumables_id       bigint(20) not null comment '�Ĳ�id',
   specs_code           varchar(64) not null comment '������',
   specs                varchar(128) not null comment '��Ʒ���',
   guid                 varchar(128) comment 'ȫ��Ψһ��',
   status               int(1) not null DEFAULT '1' comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_platform_consumables_specs comment 'ƽ̨�ĲĹ��';

/*==============================================================*/
/* Table: hdi_goods_platform_drugs                              */
/*==============================================================*/
create table hdi_goods_platform_drugs
(
   id                   bigint(20) not null auto_increment comment '����id',
   goods_unicode        varchar(128) not null comment '��Ʒͳһ����',
   drugs_code           varchar(64) not null comment 'ҩƷ����',
   drugs_name           varchar(128) not null comment 'ҩƷ����',
   common_name          varchar(128) comment 'ͨ������',
   goods_nature         int(1) not null comment '��Ʒ����(0:����;1:����)',
   type_id              bigint(20) not null comment '��Ʒ����id',
   factory_id           varchar(128) not null comment '��������id',
   approvals            varchar(128) comment '��׼�ĺ�',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   goods_unit           varchar(64) not null comment '��Ʒ��λ',
   store_way            varchar(64) comment '���淽ʽ',
   pic_url              varchar(256) comment 'ͼƬ��ַ',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (id)
);

alter table hdi_goods_platform_drugs comment 'ƽ̨ҩƷ��Ϣ';

/*==============================================================*/
/* Table: hdi_goods_platform_drugs_specs                        */
/*==============================================================*/
create table hdi_goods_platform_drugs_specs
(
   id                   bigint(20) not null auto_increment comment '����id',
   drugs_id             bigint(20) not null comment 'ҩƷid',
   specs_code           varchar(64) not null comment '������',
   specs                varchar(128) not null comment '��Ʒ���',
   guid                 varchar(128) comment 'ȫ��Ψһ��',
   status               int(1) not null DEFAULT '1' comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_platform_drugs_specs comment 'ƽ̨ҩƷ���';

/*==============================================================*/
/* Table: hdi_goods_platform_reagent                            */
/*==============================================================*/
create table hdi_goods_platform_reagent
(
   id                   bigint(20) not null auto_increment comment '����id',
   goods_unicode        varchar(128) not null comment '��Ʒͳһ����',
   reagent_code         varchar(64) not null comment '�Լ�����',
   reagent_name         varchar(128) not null comment '�Լ�����',
   common_name          varchar(128) comment 'ͨ������',
   goods_nature         int(1) not null comment '��Ʒ����(0:����;1:����)',
   type_id              bigint(20) not null comment '��Ʒ����id',
   factory_id           varchar(128) not null comment '��������id',
   approvals            varchar(128) comment '��׼�ĺ�',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   goods_unit           varchar(64) not null comment '��Ʒ��λ',
   store_way            varchar(64) comment '���淽ʽ',
   pic_url              varchar(256) comment 'ͼƬ��ַ',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (id)
);

alter table hdi_goods_platform_reagent comment 'ƽ̨�Լ���Ϣ';

/*==============================================================*/
/* Table: hdi_goods_platform_reagent_specs                      */
/*==============================================================*/
create table hdi_goods_platform_reagent_specs
(
   id                   bigint(20) not null auto_increment comment '����id',
   reagen_id            bigint(20) not null comment '�Լ�id',
   specs_code           varchar(64) not null comment '������',
   specs                varchar(128) not null comment '��Ʒ���',
   guid                 varchar(128) comment 'ȫ��Ψһ��',
   status               int(1) not null DEFAULT '1' comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_platform_reagent_specs comment 'ƽ̨�Լ����';

/*==============================================================*/
/* Table: hdi_goods_supplier_consumables                        */
/*==============================================================*/
create table hdi_goods_supplier_consumables
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_id           varchar(64) comment 'ԭϵͳ��Ʒid',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   consumables_code     varchar(64) not null comment '�Ĳı���',
   consumables_name     varchar(128) not null comment '�Ĳ�����',
   common_name          varchar(128) comment 'ͨ������',
   goods_nature         int(1) not null comment '��Ʒ����(0:����;1:����)',
   type_id              varchar(64) comment '��Ʒ����id',
   type_name            varchar(128) comment '��Ʒ��������',
   factory_id           varchar(128) not null comment '��������id',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   goods_unit           varchar(64) comment '��Ʒ��λ',
   supply_unit          varchar(64) comment '������λ',
   convert_unit         varchar(128) comment 'ת����λ',
   agent_id             bigint(20) comment '������id',
   pic_url              varchar(256) comment 'ͼƬ��ַ',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   is_match             int(1) default 0 comment '�Ƿ�ƥ��(0:δƥ��;1:��ƥ��)',
   data_source          int(1) comment '������Դ(0:ϵͳ¼��;1:��Ӧ��ERP)',
   is_upload            int(1) comment '�Ƿ��ϴ�(0:δ�ϴ�;1:���ϴ�)',
   primary key (id)
);

alter table hdi_goods_supplier_consumables comment '��Ӧ�̺Ĳ���Ϣ';

/*==============================================================*/
/* Table: hdi_goods_supplier_consumables_approvals              */
/*==============================================================*/
create table hdi_goods_supplier_consumables_approvals
(
   id                   bigint(20) not null auto_increment comment '����id',
   consumables_id       bigint(20) not null comment '�Ĳ�id',
   approvals            varchar(128) not null comment '��׼�ĺ�',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_supplier_consumables_approvals comment '��Ӧ�̺Ĳ���׼�ĺ�';

/*==============================================================*/
/* Table: hdi_goods_supplier_consumables_specs                  */
/*==============================================================*/
create table hdi_goods_supplier_consumables_specs
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_specs_id     varchar(64) comment 'ԭϵͳ��Ʒ���id',
   consumables_id       bigint(20) not null comment '�Ĳ�id',
   specs_code           varchar(64) not null comment '������',
   specs                varchar(128) not null comment '��Ʒ���',
   guid                 varchar(128) comment 'ȫ��Ψһ��',
   status               int(1) not null DEFAULT '1' comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_supplier_consumables_specs comment '��Ӧ�̺ĲĹ��';

/*==============================================================*/
/* Table: hdi_goods_supplier_drugs                              */
/*==============================================================*/
create table hdi_goods_supplier_drugs
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_id           varchar(64) comment 'ԭϵͳ��Ʒid',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   drugs_code           varchar(64) not null comment 'ҩƷ����',
   drugs_name           varchar(128) not null comment 'ҩƷ����',
   common_name          varchar(128) comment 'ͨ������',
   goods_nature         int(1) not null comment '��Ʒ����(0:����;1:����)',
   type_id              varchar(64) comment '��Ʒ����id',
   type_name            varchar(128) comment '��Ʒ��������',
   factory_id           varchar(128) not null comment '��������id',
   approvals            varchar(128) comment '��׼�ĺ�',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   goods_unit           varchar(64) comment '��Ʒ��λ',
   supply_unit          varchar(64) comment '������λ',
   convert_unit         varchar(128) comment 'ת����λ',
   agent_id             bigint(20) comment '������id',
   pic_url              varchar(256) comment 'ͼƬ��ַ',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   is_match             int(1) default 0 comment '�Ƿ�ƥ��(0:δƥ��;1:��ƥ��)',
   data_source          int(1) comment '������Դ(0:ϵͳ¼��;1:��Ӧ��ERP)',
   is_upload            int(1) comment '�Ƿ��ϴ�(0:δ�ϴ�;1:���ϴ�)',
   primary key (id)
);

alter table hdi_goods_supplier_drugs comment '��Ӧ��ҩƷ��Ϣ';

/*==============================================================*/
/* Table: hdi_goods_supplier_drugs_specs                        */
/*==============================================================*/
create table hdi_goods_supplier_drugs_specs
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_specs_id     varchar(64) comment 'ԭϵͳ��Ʒ���id',
   drugs_id             bigint(20) not null comment 'ҩƷid',
   specs_code           varchar(64) not null comment '������',
   specs                varchar(128) not null comment '��Ʒ���',
   guid                 varchar(128) comment 'ȫ��Ψһ��',
   status               int(1) not null DEFAULT '1' comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_supplier_drugs_specs comment '��Ӧ��ҩƷ���';

/*==============================================================*/
/* Table: hdi_goods_supplier_reagent                            */
/*==============================================================*/
create table hdi_goods_supplier_reagent
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_id           varchar(64) comment 'ԭϵͳ��Ʒid',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   reagent_code         varchar(64) not null comment '�Լ�����',
   reagent_name         varchar(128) not null comment '�Լ�����',
   common_name          varchar(128) comment 'ͨ������',
   goods_nature         int(1) not null comment '��Ʒ����(0:����;1:����)',
   type_id              varchar(64) comment '��Ʒ����id',
   type_name            varchar(128) comment '��Ʒ��������',
   factory_id           varchar(128) not null comment '��������id',
   approvals            varchar(128) comment '��׼�ĺ�',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   goods_unit           varchar(64) comment '��Ʒ��λ',
   supply_unit          varchar(64) comment '������λ',
   convert_unit         varchar(128) comment 'ת����λ',
   agent_id             bigint(20) comment '������id',
   pic_url              varchar(256) comment 'ͼƬ��ַ',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   is_match             int(1) default 0 comment '�Ƿ�ƥ��(0:δƥ��;1:��ƥ��)',
   data_source          int(1) comment '������Դ(0:ϵͳ¼��;1:��Ӧ��ERP)',
   is_upload            int(1) comment '�Ƿ��ϴ�(0:δ�ϴ�;1:���ϴ�)',
   primary key (id)
);

alter table hdi_goods_supplier_reagent comment '��Ӧ���Լ���Ϣ';

/*==============================================================*/
/* Table: hdi_goods_supplier_reagent_specs                      */
/*==============================================================*/
create table hdi_goods_supplier_reagent_specs
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_specs_id     varchar(64) comment 'ԭϵͳ��Ʒ���id',
   reagen_id            bigint(20) not null comment '�Լ�id',
   specs_code           varchar(64) not null comment '������',
   specs                varchar(128) not null comment '��Ʒ���',
   guid                 varchar(128) comment 'ȫ��Ψһ��',
   status               int(1) not null DEFAULT '1' comment '״̬(0:ͣ��;1:����)',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   primary key (id)
);

alter table hdi_goods_supplier_reagent_specs comment '��Ӧ���Լ����';


-- ----------------------------
-- Table structure for hdi_goods_supplier_send
-- ----------------------------
CREATE TABLE hdi_goods_supplier_send (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '����id',
  supplier_id bigint(20) NOT NULL COMMENT '��Ӧ��id',
  hospital_id bigint(20) NOT NULL COMMENT 'ҽԺid',
  goods_type int(1) NOT NULL COMMENT '��Ʒ���(1:ҩƷ;2:�Լ�;3:�Ĳ�)',
  platform_goods_id bigint(20) DEFAULT NULL COMMENT 'ƽ̨��Ʒid',
  platform_goods_code varchar(64) DEFAULT NULL COMMENT 'ƽ̨��Ʒ����',
  platform_goods_specs_id bigint(20) DEFAULT NULL COMMENT 'ƽ̨��Ʒ���id',
  platform_goods_specs_code varchar(64) DEFAULT NULL COMMENT 'ƽ̨��Ʒ������',
  goods_id bigint(20) NOT NULL DEFAULT '0' COMMENT '��Ӧ����Ʒid',
  goods_specs_id bigint(20) NOT NULL DEFAULT '0' COMMENT '��Ӧ����Ʒ���id',
  dept_id bigint(20) NOT NULL DEFAULT '0' COMMENT '��Ӧ����������',
  create_id bigint(20) NOT NULL COMMENT '������id',
  create_time datetime NOT NULL COMMENT '����ʱ��',
  is_upload int(1) NOT NULL DEFAULT '0' COMMENT '�Ƿ��ϴ�(0:δ�ϴ�;1:���ϴ�)',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='��Ӧ����Ʒ�·���';

-- ----------------------------
-- Table structure for pub_supplygoods
-- ----------------------------
CREATE TABLE pub_supplygoods (
  mgoodsid varchar(50) NOT NULL COMMENT '��Ʒ����',
  uorganid varchar(50) DEFAULT NULL COMMENT '��������',
  supplyid varchar(50) DEFAULT NULL COMMENT '��Ӧ��id',
  platformno varchar(50) DEFAULT NULL COMMENT 'ƽ̨����',
  goodsno varchar(50) DEFAULT NULL COMMENT '��Ʒ����',
  goodstype varchar(200) DEFAULT NULL COMMENT '��Ʒ���',
  goodsname varchar(200) DEFAULT NULL COMMENT '��Ʒ����',
  goodsshortname varchar(200) DEFAULT NULL COMMENT '��Ʒ����д���Ĳļ�д��',
  goodsunit varchar(50) DEFAULT NULL COMMENT '��Ʒ��λ',
  packsize decimal(10,4) DEFAULT NULL COMMENT '��װ��С',
  rationmrtr decimal(10,0) DEFAULT NULL COMMENT '��ϵ��������',
  ratiodnmtr decimal(10,0) DEFAULT NULL COMMENT '��ϵ������ĸ',
  bidprice decimal(12,4) DEFAULT NULL COMMENT '������',
  purchasetype decimal(10,0) DEFAULT NULL COMMENT '�ɹ����',
  goodsprop decimal(10,0) DEFAULT NULL COMMENT '��������',
  storagecondiction decimal(4,0) DEFAULT NULL COMMENT '��������',
  factoryname varchar(200) DEFAULT NULL COMMENT '��������',
  registerdocno varchar(50) DEFAULT NULL COMMENT 'ע��֤��',
  usestatus decimal(4,0) DEFAULT NULL COMMENT 'ʹ��״̬',
  goodscategorytype int(11) DEFAULT NULL COMMENT '��Ʒ�������',
  mixtype decimal(11,0) DEFAULT NULL COMMENT '��װ���',
  receiptflag int(11) DEFAULT NULL COMMENT '���ձ�ʶ',
  transdate datetime DEFAULT NULL COMMENT '����ʱ��',
  unitcover varchar(50) DEFAULT NULL COMMENT '��λ����',
  pay decimal(4,0) DEFAULT NULL COMMENT '�շѱ�ʶ',
  PRIMARY KEY (mgoodsid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='��Ӧ����Ʒ�·���hdi��ʱ��';

