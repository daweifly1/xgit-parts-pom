create table version_record
(
  id NUMBER(11) not null,
  version VARCHAR2(50) not null,
  update_time TIMESTAMP(6) not null
)
;

comment on table version_record
  is '版本记录表，同步数据库与代码版本用，请勿手动删除！';

alter table IUS_PROFILE add area_code varchar2(20);
comment on column IUS_PROFILE.area_code
  is '地区编码';