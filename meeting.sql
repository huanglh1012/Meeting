/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018-05-29 20:52:45                          */
/*==============================================================*/


drop table if exists ATTACHMENT;

drop table if exists ATTACHMENT_CATEGORY;

drop table if exists ATTACHMENT_TEMP;

drop table if exists DEPARTMENT;

drop table if exists EMPLOYEE;

drop table if exists EMPLOYEE_ROLE_RF;

drop table if exists MEETING;

drop table if exists MEETING_ATTACHMENT;

drop table if exists MEETING_MEMBER_RF;

drop table if exists MEETING_MEMBER_ROLE;

drop table if exists MEETING_ROOM;

drop table if exists MEETING_STATE;

drop table if exists MESSAGE_SEND_CENTER;

drop table if exists MESSAGE_SEND_STATE;

drop table if exists POST;

drop table if exists ROLE;

drop table if exists ROLE_SECURITY_RF;

drop table if exists SECURITY;

drop table if exists SEX;

drop table if exists SHORT_MESSAGE_CENTER;

drop table if exists SHORT_MESSAGE_SEND_LOG;

/*==============================================================*/
/* Table: ATTACHMENT                                            */
/*==============================================================*/
create table ATTACHMENT
(
   ATTACHMENT_ID        varchar(32) not null,
   EMPLOYEE_ID          varchar(32),
   ATTACHMENT_NAME      varchar(256) not null,
   ATTACHMENT_RENAME    varchar(256),
   ATTACHMENT_PATH      varchar(1024) not null,
   ATTACHMENT_TEMP_PATH varchar(1024),
   ATTACHMENT_CREATE_TIME timestamp not null,
   ATTACHMENT_TYPE      varchar(128),
   ATTACHMENT_CREATE_ID varchar(128) not null,
   ATTACHMENT_STATUS_ID varchar(32) comment '0,��ɾ��
            1.����',
   ATTACHMENT_SIZE      integer,
   primary key (ATTACHMENT_ID)
);

/*==============================================================*/
/* Table: ATTACHMENT_CATEGORY                                   */
/*==============================================================*/
create table ATTACHMENT_CATEGORY
(
   ATTACHMENT_CATEGORY_ID varchar(32) not null,
   ATTACHMENT_CATEGORY_CODE numeric(8) not null,
   ATTACHMENT_CATEGORY_NAME varchar(128) not null,
   primary key (ATTACHMENT_CATEGORY_ID)
);

alter table ATTACHMENT_CATEGORY comment '0����ͨ����
1�������¼';

/*==============================================================*/
/* Table: ATTACHMENT_TEMP                                       */
/*==============================================================*/
create table ATTACHMENT_TEMP
(
   ATTACHMENT_ID        varchar(32) not null,
   EMPLOYEE_ID          varchar(32),
   ATTACHMENT_NAME      varchar(256) not null,
   ATTACHMENT_RENAME    varchar(256),
   ATTACHMENT_PATH      varchar(1024) not null,
   ATTACHMENT_TEMP_PATH varchar(1024),
   ATTACHMENT_CREATE_TIME timestamp not null,
   ATTACHMENT_TYPE      varchar(128),
   ATTACHMENT_CREATE_ID varchar(128) not null,
   ATTACHMENT_STATUS_ID varchar(32) comment '0,��ɾ��
            1.����',
   ATTACHMENT_SIZE      integer,
   primary key (ATTACHMENT_ID)
);

/*==============================================================*/
/* Table: DEPARTMENT                                            */
/*==============================================================*/
create table DEPARTMENT
(
   DEPARTMENT_ID        varchar(32) not null,
   PARENT_DEPARTMENT_ID varchar(32),
   DEPARTMENT_NAME      varchar(128) not null,
   primary key (DEPARTMENT_ID)
);

/*==============================================================*/
/* Table: EMPLOYEE                                              */
/*==============================================================*/
create table EMPLOYEE
(
   EMPLOYEE_ID          varchar(32) not null,
   POST_ID              varchar(32),
   DEPARTMENT_ID        varchar(32),
   SEX_ID               varchar(32),
   LOGIN                varchar(128),
   PASSWORD             varchar(128),
   EMPLOYEE_NAME        varchar(128) not null,
   TELEPHONE            varchar(128),
   IDENTIFY_CARD_NUMBER varchar(24),
   primary key (EMPLOYEE_ID)
);

/*==============================================================*/
/* Table: EMPLOYEE_ROLE_RF                                      */
/*==============================================================*/
create table EMPLOYEE_ROLE_RF
(
   EMPLOYEE_ROLE_RF_ID  varchar(32) not null,
   EMPLOYEE_ID          varchar(32),
   ROLE_ID              varchar(32),
   primary key (EMPLOYEE_ROLE_RF_ID)
);

/*==============================================================*/
/* Table: MEETING                                               */
/*==============================================================*/
create table MEETING
(
   MEETING_ID           varchar(32) not null,
   MEETING_ROOM_ID      varchar(32) not null,
   MEETING_STATE_ID     varchar(32) not null,
   MEETING_PROPOSE_TIME timestamp not null,
   MEETING_START_TIME   timestamp not null,
   MEETING_END_TIME     timestamp not null,
   MEETING_SUBJECT      varchar(512) not null,
   MEETING_ATTENTIONS   text,
   MEETING_UPLOAD_END_TIME timestamp,
   IS_SEND_MESSAGE_NOTICE tinyint not null,
   primary key (MEETING_ID)
);

/*==============================================================*/
/* Table: MEETING_ATTACHMENT                                    */
/*==============================================================*/
create table MEETING_ATTACHMENT
(
   MEETING_ATTACHMENT_ID varchar(32) not null,
   MEETING_ID           varchar(32) not null,
   ATTACHMENT_CATEGORY_ID varchar(32) not null,
   ATTACHMENT_ID        varchar(32),
   primary key (MEETING_ATTACHMENT_ID)
);

/*==============================================================*/
/* Table: MEETING_MEMBER_RF                                     */
/*==============================================================*/
create table MEETING_MEMBER_RF
(
   MEETING_MEMBER_RF_ID varchar(32) not null,
   EMPLOYEE_ID          varchar(32) not null,
   MEETING_ID           varchar(32) not null,
   MEETING_MEMBER_ROLE_ID varchar(32) not null,
   primary key (MEETING_MEMBER_RF_ID)
);

/*==============================================================*/
/* Table: MEETING_MEMBER_ROLE                                   */
/*==============================================================*/
create table MEETING_MEMBER_ROLE
(
   MEETING_MEMBER_ROLE_ID varchar(32) not null,
   MEETING_MEMBER_ROLE_CODE numeric(8) not null,
   MEETING_MEMBER_ROLE_NAME varchar(128) not null,
   primary key (MEETING_MEMBER_ROLE_ID)
);

alter table MEETING_MEMBER_ROLE comment '0�����鷢����
1������������
2�������λ���Ա';

/*==============================================================*/
/* Table: MEETING_ROOM                                          */
/*==============================================================*/
create table MEETING_ROOM
(
   MEETING_ROOM_ID      varchar(32) not null,
   MEETING_ROOM_NAME    varchar(64) not null,
   MEETING_ROOM_ADDRESS varchar(1024) not null,
   primary key (MEETING_ROOM_ID)
);

/*==============================================================*/
/* Table: MEETING_STATE                                         */
/*==============================================================*/
create table MEETING_STATE
(
   MEETING_STATE_ID     varchar(32) not null,
   MEETING_STATE_CODE   numeric(8) not null,
   MEETING_STATE_NAME   varchar(128) not null,
   primary key (MEETING_STATE_ID)
);

alter table MEETING_STATE comment '0���ѷ���
1���ѽ���';

/*==============================================================*/
/* Table: MESSAGE_SEND_CENTER                                   */
/*==============================================================*/
create table MESSAGE_SEND_CENTER
(
   MESSAGE_SEND_CENTER_ID varchar(32) not null,
   MEETING_ID           varchar(32),
   MESSAGE_SEND_STATE_ID varchar(32),
   SEND_DATETIME        datetime not null,
   SEND_MESSAGE         text not null,
   IS_ACTIVE            tinyint not null comment '0����Ч
            1����Ч
            ���鴴��������Ч״̬�����鱻ɾ��������Ч״̬''',
   primary key (MESSAGE_SEND_CENTER_ID)
);

alter table MESSAGE_SEND_CENTER comment '���鴴��IS_ACTIVE�ֶ���1������ɾ���󣬽���������ÿգ�IS_ACTIVE�ֶ���0';

/*==============================================================*/
/* Table: MESSAGE_SEND_STATE                                    */
/*==============================================================*/
create table MESSAGE_SEND_STATE
(
   MESSAGE_SEND_STATE_ID varchar(32) not null,
   MESSAGE_SEND_STATE_CODE numeric(8) not null,
   MESSAGE_SEND_STATE_NAME varchar(128) not null,
   primary key (MESSAGE_SEND_STATE_ID)
);

alter table MESSAGE_SEND_STATE comment '0��δ����
1������ʧ��
2�����ͳɹ�';

/*==============================================================*/
/* Table: POST                                                  */
/*==============================================================*/
create table POST
(
   POST_ID              varchar(32) not null,
   POST_NAME            varchar(128) not null,
   POST_SUMMARY         varchar(1024),
   primary key (POST_ID)
);

/*==============================================================*/
/* Table: ROLE                                                  */
/*==============================================================*/
create table ROLE
(
   ROLE_ID              varchar(32) not null,
   ROLE_NAME            varchar(128) not null,
   primary key (ROLE_ID)
);

/*==============================================================*/
/* Table: ROLE_SECURITY_RF                                      */
/*==============================================================*/
create table ROLE_SECURITY_RF
(
   ROLE_SECURITY_RF_ID  varchar(32) not null,
   ROLE_ID              varchar(32) not null,
   SECURITY_ID          varchar(32) not null,
   primary key (ROLE_SECURITY_RF_ID)
);

/*==============================================================*/
/* Table: SECURITY                                              */
/*==============================================================*/
create table SECURITY
(
   SECURITY_ID          varchar(32) not null,
   SECURITY_CODE        varchar(32) not null,
   SECURITY_NAME        varchar(128) not null,
   primary key (SECURITY_ID)
);

/*==============================================================*/
/* Table: SEX                                                   */
/*==============================================================*/
create table SEX
(
   SEX_ID               varchar(32) not null,
   SEX_CODE             numeric(8) not null,
   SEX_NAME             varchar(16) not null,
   primary key (SEX_ID)
);

/*==============================================================*/
/* Table: SHORT_MESSAGE_CENTER                                  */
/*==============================================================*/
create table SHORT_MESSAGE_CENTER
(
   SHORT_MESSAGE_CENTER_ID varchar(32) not null,
   SHORT_MESSAGE_CENTER_NAME varchar(128) not null,
   CENTER_PHONE_NUMBER  varchar(128) not null,
   SEND_MESSAGE_PHONE_NUMBER varchar(128) not null,
   MESSAGE_MODEL        varchar(1024),
   primary key (SHORT_MESSAGE_CENTER_ID)
);

alter table SHORT_MESSAGE_CENTER comment '������Ϣ���ƶ��ṩ�Ľӿڵ���';

/*==============================================================*/
/* Table: SHORT_MESSAGE_SEND_LOG                                */
/*==============================================================*/
create table SHORT_MESSAGE_SEND_LOG
(
   SHORT_MESSAGE_SEND_LOG_ID varchar(32) not null,
   SHORT_MESSAGE_CENTER_ID varchar(32),
   MESSAGE_SEND_CENTER_ID varchar(32) not null,
   MESSAGE_SEND_STATE_ID varchar(32) not null,
   MESSAGE_SEND_TELEPHONE text not null,
   MESSAGE_SEND_PARAM   text not null,
   MESSAGE_SEND_TIME    datetime not null,
   MESSAGE_SEND_RESULT  text not null,
   MESSAGE_SEND_COUNT   int not null,
   primary key (SHORT_MESSAGE_SEND_LOG_ID)
);

alter table ATTACHMENT add constraint FK_ATTACHMENT_1 foreign key (EMPLOYEE_ID)
      references EMPLOYEE (EMPLOYEE_ID) on delete restrict on update restrict;

alter table DEPARTMENT add constraint FK_Reference_20 foreign key (PARENT_DEPARTMENT_ID)
      references DEPARTMENT (DEPARTMENT_ID) on delete restrict on update restrict;

alter table EMPLOYEE add constraint FK_EMPLOYEE_1 foreign key (DEPARTMENT_ID)
      references DEPARTMENT (DEPARTMENT_ID) on delete restrict on update restrict;

alter table EMPLOYEE add constraint FK_EMPLOYEE_2 foreign key (POST_ID)
      references POST (POST_ID) on delete restrict on update restrict;

alter table EMPLOYEE add constraint FK_EMPLOYEE_4 foreign key (SEX_ID)
      references SEX (SEX_ID) on delete restrict on update restrict;

alter table EMPLOYEE_ROLE_RF add constraint FK_Reference_21 foreign key (EMPLOYEE_ID)
      references EMPLOYEE (EMPLOYEE_ID) on delete restrict on update restrict;

alter table EMPLOYEE_ROLE_RF add constraint FK_Reference_22 foreign key (ROLE_ID)
      references ROLE (ROLE_ID) on delete restrict on update restrict;

alter table MEETING add constraint FK_MEETING_1 foreign key (MEETING_STATE_ID)
      references MEETING_STATE (MEETING_STATE_ID) on delete restrict on update restrict;

alter table MEETING add constraint FK_MEETING_2 foreign key (MEETING_ROOM_ID)
      references MEETING_ROOM (MEETING_ROOM_ID) on delete restrict on update restrict;

alter table MEETING_ATTACHMENT add constraint FK_MEETING_ATTACHMENT_1 foreign key (MEETING_ID)
      references MEETING (MEETING_ID) on delete restrict on update restrict;

alter table MEETING_ATTACHMENT add constraint FK_MEETING_ATTACHMENT_2 foreign key (ATTACHMENT_CATEGORY_ID)
      references ATTACHMENT_CATEGORY (ATTACHMENT_CATEGORY_ID) on delete restrict on update restrict;

alter table MEETING_ATTACHMENT add constraint FK_MEETING_ATTACHMENT_3 foreign key (ATTACHMENT_ID)
      references ATTACHMENT (ATTACHMENT_ID) on delete restrict on update restrict;

alter table MEETING_MEMBER_RF add constraint FK_MEETING_MEMBOR_RF_1 foreign key (EMPLOYEE_ID)
      references EMPLOYEE (EMPLOYEE_ID) on delete restrict on update restrict;

alter table MEETING_MEMBER_RF add constraint FK_MEETING_MEMBOR_RF_2 foreign key (MEETING_MEMBER_ROLE_ID)
      references MEETING_MEMBER_ROLE (MEETING_MEMBER_ROLE_ID) on delete restrict on update restrict;

alter table MEETING_MEMBER_RF add constraint FK_MEETING_MEMBOR_RF_3 foreign key (MEETING_ID)
      references MEETING (MEETING_ID) on delete restrict on update restrict;

alter table MESSAGE_SEND_CENTER add constraint FK_MESSAGE_SEND_CENTER_1 foreign key (MESSAGE_SEND_STATE_ID)
      references MESSAGE_SEND_STATE (MESSAGE_SEND_STATE_ID) on delete restrict on update restrict;

alter table MESSAGE_SEND_CENTER add constraint FK_MESSAGE_SEND_CENTER_2 foreign key (MEETING_ID)
      references MEETING (MEETING_ID) on delete restrict on update restrict;

alter table ROLE_SECURITY_RF add constraint FK_ROLE_SECURITY_RF_1 foreign key (ROLE_ID)
      references ROLE (ROLE_ID) on delete restrict on update restrict;

alter table ROLE_SECURITY_RF add constraint FK_ROLE_SECURITY_RF_2 foreign key (SECURITY_ID)
      references SECURITY (SECURITY_ID) on delete restrict on update restrict;

alter table SHORT_MESSAGE_SEND_LOG add constraint FK_SHORT_MESSAGE_SEND_LOG_1 foreign key (SHORT_MESSAGE_CENTER_ID)
      references SHORT_MESSAGE_CENTER (SHORT_MESSAGE_CENTER_ID) on delete restrict on update restrict;

alter table SHORT_MESSAGE_SEND_LOG add constraint FK_SHORT_MESSAGE_SEND_LOG_2 foreign key (MESSAGE_SEND_STATE_ID)
      references MESSAGE_SEND_STATE (MESSAGE_SEND_STATE_ID) on delete restrict on update restrict;

alter table SHORT_MESSAGE_SEND_LOG add constraint FK_SHORT_MESSAGE_SEND_LOG_3 foreign key (MESSAGE_SEND_CENTER_ID)
      references MESSAGE_SEND_CENTER (MESSAGE_SEND_CENTER_ID) on delete restrict on update restrict;

