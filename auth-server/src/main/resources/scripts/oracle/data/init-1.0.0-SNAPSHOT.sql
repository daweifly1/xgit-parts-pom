
insert into IUS_ACCOUNT (USER_ID, LOGIN_NAME, LAST_LOGIN_TIME, LAST_LOGIN_IP, STATUS, CREATE_TIME, UPDATE_TIME)
values ('118744196566745088', 'admin001', '27-2月 -18 10.53.32.268815 上午', null, 0, '25-5月 -05 12.00.00.000000 上午', '27-2月 -18 10.53.32.268815 上午');

insert into IUS_PASSWORD (USER_ID, PASSWORD, UPDATE_TIME, TYPE)
values ('118744196566745088', '-5e9a7dea49f0453e4ffae5346d5b6618', '05-2月 -18 07.24.46.000000 下午', 0);


insert into IUS_PROFILE (ID, NAME, MOBILE, TELEPHONE, SEX, DEPT_ID, ICON, NICKNAME, EMAIL, REMARK, LOCKED, CREATE_DATE, SPACE_ID, ID_NUMBER)
values ('118744196566745088', 'eqwe12121', '12312312312', '88888888', 1, '145466319502835712', null, '大锤', '12@asd.sd', null, 0, null, '1', null);


insert into IUS_ROLE (ID, NAME, REMARK, TYPE, CHANNEL, SPACE_ID, DEPT_ID)
values ('1', '系统管理员', '9', 1, 0, '0', '0');

insert into IUS_ROLE (ID, NAME, REMARK, TYPE, CHANNEL, SPACE_ID, DEPT_ID)
values ('118731003802943488', '默认用户', '9', 2, 0, '0', '0');

insert into IUS_TEMPLATE (ID, NAME, SITE, REMARK)
values ('1', '管理平台权限模板', 0, '管理平台用');


insert into IUS_USER_ROLES (USER_ID, ROLE_ID, ROLE_FLAG)
values ('118744196566745088', '1', 1);

insert into IUS_WORKSPACE (ID, NAME, SITE, REMARK, STATUS, TEMP_ID, SPACE_TYPE)
values ('1', '管理员', 0, '系统默认', 0, '1', 1);

insert into version_record (ID, VERSION, update_time)
values ('4', '0.0.4-SNAPSHOT', sysdate);