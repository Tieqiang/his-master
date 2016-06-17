-- Create table
create table LOCAL_PROGRAM_SETTING
(
  ID             VARCHAR2(64) not null,
  APP_NAME       VARCHAR2(100),
  APP_LOCAL_PATH VARCHAR2(200),
  LOGIN_USER     VARCHAR2(100),
  APP_CLASS      VARCHAR2(100),
  APP_WIDTH_EXT  VARCHAR2(100),
  COLOR          VARCHAR2(50),
  PIC_URL        VARCHAR2(200)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 1
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table
comment on table LOCAL_PROGRAM_SETTING
  is '个人界面程序管理';
-- Add comments to the columns
comment on column LOCAL_PROGRAM_SETTING.ID
  is '主键';
comment on column LOCAL_PROGRAM_SETTING.APP_NAME
  is '应用程序名称';
comment on column LOCAL_PROGRAM_SETTING.APP_LOCAL_PATH
  is '应用程序本地路径';
comment on column LOCAL_PROGRAM_SETTING.LOGIN_USER
  is '当前登录人';
comment on column LOCAL_PROGRAM_SETTING.APP_CLASS
  is '程序分类';
comment on column LOCAL_PROGRAM_SETTING.APP_WIDTH_EXT
  is '包含扩展名的程序例如qq.exe';
comment on column LOCAL_PROGRAM_SETTING.COLOR
  is '背景颜色';
comment on column LOCAL_PROGRAM_SETTING.PIC_URL
  is '背景图片地址';
-- Create/Recreate primary, unique and foreign key constraints
alter table LOCAL_PROGRAM_SETTING
  add constraint LOCAL_PROGRAM_SETTING_PK primary key (ID)
  using index
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
