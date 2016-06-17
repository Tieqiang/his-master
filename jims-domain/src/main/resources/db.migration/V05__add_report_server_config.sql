-- Create table
create table REPORT_SERVER_CONFIG
(
  ID            VARCHAR2(64) not null,
  HOSPITAL_ID   VARCHAR2(64),
  IP            VARCHAR2(100),
  PORT          VARCHAR2(10),
  REMOTE_IP     VARCHAR2(100),
  REMOTE_PORT   VARCHAR2(10),
  HOSPITAL_NAME VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table REPORT_SERVER_CONFIG
  is '报表服务器参数配置';
-- Add comments to the columns 
comment on column REPORT_SERVER_CONFIG.ID
  is '主键';
comment on column REPORT_SERVER_CONFIG.IP
  is '内网IP';
comment on column REPORT_SERVER_CONFIG.PORT
  is '端口后';
comment on column REPORT_SERVER_CONFIG.REMOTE_IP
  is '公网IP';
comment on column REPORT_SERVER_CONFIG.REMOTE_PORT
  is '公网端口号';
comment on column REPORT_SERVER_CONFIG.HOSPITAL_NAME
  is '医院名称';
-- Create/Recreate primary, unique and foreign key constraints 
alter table REPORT_SERVER_CONFIG
  add constraint REPORT_SERVER_CONFIG_PK primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;
