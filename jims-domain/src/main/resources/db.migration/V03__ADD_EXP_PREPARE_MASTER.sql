-- Create table
--  zhuq
create table EXP_PREPARE_MASTER
(
  id                  VARCHAR2(64) not null,
  exp_id              VARCHAR2(64),
  supplier_id         VARCHAR2(64),
  firm_id             VARCHAR2(64),
  prepare_date        VARCHAR2(32),
  prepare_count       VARCHAR2(10),
  operator            VARCHAR2(16),
  prepare_person_name VARCHAR2(16),
  phone               VARCHAR2(16),
  price               NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table EXP_PREPARE_MASTER
  is '供应商备货主记录';
-- Add comments to the columns 
comment on column EXP_PREPARE_MASTER.exp_id
  is '消耗品';
comment on column EXP_PREPARE_MASTER.supplier_id
  is '供应商';
comment on column EXP_PREPARE_MASTER.firm_id
  is '生产商';
comment on column EXP_PREPARE_MASTER.prepare_date
  is '备货时间';
comment on column EXP_PREPARE_MASTER.prepare_count
  is '备货数量';
comment on column EXP_PREPARE_MASTER.operator
  is '操作人';
comment on column EXP_PREPARE_MASTER.prepare_person_name
  is '备货人';
comment on column EXP_PREPARE_MASTER.phone
  is '备货人电话';
comment on column EXP_PREPARE_MASTER.price
  is '备货价格';
-- Create/Recreate primary, unique and foreign key constraints 
alter table EXP_PREPARE_MASTER
  add constraint PK_EXP_PREPARE_MASTER primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;
