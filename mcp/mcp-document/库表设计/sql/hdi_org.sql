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
   id                   bigint(20) not null auto_increment comment '����id',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   agent_code           varchar(128) not null comment '�����̱���',
   agent_name           varchar(128) not null comment '����������',
   credit_code          varchar(64) not null comment 'ͳһ������ô���',
   province_code        varchar(64) comment '����ʡ����',
   city_code            varchar(64) comment '�����б���',
   area_code            varchar(64) comment '�������ر���',
   agent_address        varchar(256) comment '�����̵�ַ',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   corporate            varchar(64) comment '���˴���',
   phone                varchar(32) comment '��ϵ�绰',
   email                varchar(64) comment '�����ַ',
   fax                  varchar(64) comment '����',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   primary key (id)
);

alter table hdi_org_agent_info comment '��������Ϣ';

/*==============================================================*/
/* Table: hdi_org_factory_info                                  */
/*==============================================================*/
create table hdi_org_factory_info
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_id           varchar(64) comment 'ԭϵͳ����id',
   factory_code         varchar(128) comment '���̱���',
   factory_name         varchar(128) not null comment '��������',
   credit_code          varchar(64) comment 'ͳһ������ô���',
   country_code         varchar(64) comment '���ڹ��ұ���',
   province_code        varchar(64) comment '����ʡ����',
   city_code            varchar(64) comment '�����б���',
   area_code            varchar(64) comment '�������ر���',
   factory_address      varchar(256) comment '���̵�ַ',
   status               int(1) not null comment '״̬(-1:������;0:ͣ��;1:����)',
   corporate            varchar(64) comment '���˴���',
   phone                varchar(32) comment '��ϵ�绰',
   email                varchar(64) comment '�����ַ',
   fax                  varchar(64) comment '����',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   data_source          int(1) default 0 comment '������Դ(0:ϵͳ¼��;1:ҽԺSPD)',
   primary key (id)
);

alter table hdi_org_factory_info comment '������Ϣ';

/*==============================================================*/
/* Table: hdi_org_hospital_info                                 */
/*==============================================================*/
create table hdi_org_hospital_info
(
   id                   bigint(20) not null auto_increment comment '����id',
   parent_id            bigint(20) comment '����Ӧ��id',
   sources_id           varchar(64) comment 'ԭϵͳҽԺid',
   hospital_code        varchar(128) not null comment 'ҽԺ����',
   hospital_grade       int(1) comment 'ҽԺ����(0:�����ص�;1:�����׵�;2:�����ҵ�;3:��������;4:�����׵�;5:�����ҵ�;6:��������;7:һ���׵�;8:һ���ҵ�;9:һ������)',
   hospital_name        varchar(128) not null comment 'ҽԺ����',
   credit_code          varchar(64) not null comment 'ͳһ������ô���',
   province_code        varchar(64) comment '����ʡ����',
   city_code            varchar(64) comment '�����б���',
   area_code            varchar(64) comment '�������ر���',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   hospital_address     varchar(256) comment 'ҽԺ��ַ',
   corporate            varchar(64) comment '���˴���',
   phone                varchar(32) comment '��ϵ�绰',
   email                varchar(64) comment '�����ַ',
   is_group             int(1) comment '�Ƿ��Ż���(0:��;1:��)',
   child_number         int(11) comment '�ӻ�����(����ҽԺ����������ҽԺ��)',
   fax                  varchar(64) comment '����',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   data_source          int(1) default 0 comment '������Դ(0:ϵͳ¼��;1:ҽԺSPD)',
   primary key (id)
);

alter table hdi_org_hospital_info comment 'ҽԺ��Ϣ';

/*==============================================================*/
/* Table: hdi_org_supplier_hospital_ref                         */
/*==============================================================*/
create table hdi_org_supplier_hospital_ref
(
   id                   bigint(20) not null auto_increment comment '����id',
   sources_id           varchar(64) comment 'ԭϵͳ��ϵid',
   supplier_id          bigint(20) not null comment '��Ӧ��id',
   sources_supplier_id  varchar(64) comment 'ԭϵͳ��Ӧ��id',
   hospital_id          bigint(20) not null comment 'ҽԺid',
   sources_hospital_id  varchar(64) comment 'ԭϵͳҽԺid',
   del_flag             int(1) comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   data_source          int(1) default 0 comment '������Դ(0:ϵͳ¼��;1:ҽԺSPD)',
   primary key (id)
);

alter table hdi_org_supplier_hospital_ref comment '��Ӧ��ҽԺ�󶨹�ϵ';

/*==============================================================*/
/* Table: hdi_org_supplier_info                                 */
/*==============================================================*/
create table hdi_org_supplier_info
(
   id                   bigint(20) not null auto_increment comment '����id',
   parent_id            bigint(20) comment '����Ӧ��id',
   sources_id           varchar(64) comment 'ԭϵͳ��Ӧ��id',
   supplier_code        varchar(128) not null comment '��Ӧ�̱���',
   supplier_name        varchar(128) not null comment '��Ӧ������',
   credit_code          varchar(64) comment 'ͳһ������ô���',
   province_code        varchar(64) comment '����ʡ����',
   city_code            varchar(64) comment '�����б���',
   area_code            varchar(64) comment '�������ر���',
   supplier_address     varchar(256) comment '��Ӧ�̵�ַ',
   status               int(1) not null comment '״̬(0:ͣ��;1:����)',
   supplier_nature      int(1) comment '��Ӧ������(0:��Ӫ��ҵ;1:��Ӫ��ҵ;2:���������ҵ;3:�ɷ�����ҵ;4:������ҵ)',
   corporate            varchar(64) comment '���˴���',
   phone                varchar(32) comment '��ϵ�绰',
   email                varchar(64) comment '�����ַ',
   fax                  varchar(64) comment '����',
   is_group             int(1) comment '�Ƿ��Ż���(0:��;1:��)',
   child_number         int(11) comment '�ӻ�����(���Ź�Ӧ�����������ӹ�Ӧ����)',
   dept_id              bigint(20) comment '��������',
   create_id            bigint(20) comment '������id',
   create_time          datetime comment '����ʱ��',
   edit_id              bigint(20) comment '�޸���id',
   edit_time            datetime comment '�޸�ʱ��',
   del_flag             int(1) default 0 comment '�Ƿ�ɾ��(-1:��ɾ��;0:����)',
   data_source          int(1) default 0 comment '������Դ(0:ϵͳ¼��;1:ҽԺSPD)',
   primary key (id)
);

alter table hdi_org_supplier_info comment '��Ӧ����Ϣ';

