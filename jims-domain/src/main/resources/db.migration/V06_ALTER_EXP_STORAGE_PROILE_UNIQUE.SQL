--删除之前的
alter table EXP_STORAGE_PROFILE drop constraint AK_UQ_EXP_STORAGE_PRO_EXP_STOR


--新加一个字段 supplier
alter table EXP_STORAGE_PROFILE
  add constraint AK_U_EXP_STORAGE_PRO_EXP_STOR unique (SUPPLIER,exp_code,exp_spec,storage)