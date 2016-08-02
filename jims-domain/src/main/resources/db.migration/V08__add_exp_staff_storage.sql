-- Create table
create table EXP_STAFF_STORAGE
(
  id           VARCHAR2(64) not null,
  staff_id     VARCHAR2(64) not null,
  staff_name   VARCHAR2(42),
  storage_code VARCHAR2(20),
  storage_name VARCHAR2(20)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table
comment on table EXP_STAFF_STORAGE
  is '人员与所管理库房对应表';
-- Add comments to the columns
comment on column EXP_STAFF_STORAGE.id
  is '主键';
comment on column EXP_STAFF_STORAGE.staff_id
  is '员工表主键';
comment on column EXP_STAFF_STORAGE.staff_name
  is '员工名字';
comment on column EXP_STAFF_STORAGE.storage_code
  is '库房代码';
comment on column EXP_STAFF_STORAGE.storage_name
  is '库房名称';
