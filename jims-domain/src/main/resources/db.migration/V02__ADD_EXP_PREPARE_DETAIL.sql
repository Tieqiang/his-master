-- Create table
-- zhuq
create table EXP_PREPARE_DETAIL
(
  id               VARCHAR2(64) not null,
  exp_bar_code     VARCHAR2(16),
  master_id        VARCHAR2(64),
  use_flag         VARCHAR2(2),
  use_date         VARCHAR2(32),
  use_patient_id   VARCHAR2(64),
  user_dept        VARCHAR2(64),
  imp_docno_first  VARCHAR2(64),
  exp_docno_first  VARCHAR2(64),
  imp_docno_second VARCHAR2(64),
  exp_docno_second VARCHAR2(64),
  operator         VARCHAR2(16),
  print_flag       VARCHAR2(2)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table EXP_PREPARE_DETAIL
  is ' 备货明细记录';
-- Add comments to the columns 
comment on column EXP_PREPARE_DETAIL.exp_bar_code
  is '条形码';
comment on column EXP_PREPARE_DETAIL.master_id
  is '备货记录ID';
comment on column EXP_PREPARE_DETAIL.use_flag
  is '是否使用';
comment on column EXP_PREPARE_DETAIL.use_date
  is '使用日期';
comment on column EXP_PREPARE_DETAIL.use_patient_id
  is '使用病人';
comment on column EXP_PREPARE_DETAIL.user_dept
  is '使用科室';
comment on column EXP_PREPARE_DETAIL.imp_docno_first
  is '入库单号';
comment on column EXP_PREPARE_DETAIL.exp_docno_first
  is '出库单号';
comment on column EXP_PREPARE_DETAIL.imp_docno_second
  is '入库单号';
comment on column EXP_PREPARE_DETAIL.exp_docno_second
  is '出库单号';
comment on column EXP_PREPARE_DETAIL.operator
  is '扫码人员';
comment on column EXP_PREPARE_DETAIL.print_flag
  is '是否打印';
-- Create/Recreate primary, unique and foreign key constraints 
alter table EXP_PREPARE_DETAIL
  add constraint PK_EXP_PREPARE_DETAIL primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;
