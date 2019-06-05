
create table IUS_ACCOUNT
(
  user_id         VARCHAR2(18) not null,
  login_name      VARCHAR2(128) not null,
  last_login_time TIMESTAMP(6),
  last_login_ip   VARCHAR2(64),
  status          INTEGER not null,
  create_time     TIMESTAMP(6) not null,
  update_time     TIMESTAMP(6) not null
)
;
comment on table IUS_ACCOUNT
  is '账号信息表';
comment on column IUS_ACCOUNT.user_id
  is '用户id';
comment on column IUS_ACCOUNT.login_name
  is '登录名';
comment on column IUS_ACCOUNT.last_login_time
  is '最近登录时间';
comment on column IUS_ACCOUNT.last_login_ip
  is '最近登录IP';
comment on column IUS_ACCOUNT.status
  is '状态（0有效，1无效）';
comment on column IUS_ACCOUNT.create_time
  is '添加时间';
comment on column IUS_ACCOUNT.update_time
  is '修改时间';


create table IUS_AUTH
(
  auth_id   NUMBER(10) not null,
  auth_name VARCHAR2(10) not null,
  auth_desc VARCHAR2(10)
)
;
comment on table IUS_AUTH
  is '操作权限表';
comment on column IUS_AUTH.auth_id
  is '权限id';
comment on column IUS_AUTH.auth_name
  is '权限展示名称';
comment on column IUS_AUTH.auth_desc
  is '权限描述';
alter table IUS_AUTH
  add constraint PK_IUS_AUTH primary key (AUTH_ID)
  using index
  ;


create table IUS_DEPARTMENT
(
  id        VARCHAR2(18) not null,
  space_id  VARCHAR2(18) not null,
  name      VARCHAR2(50) not null,
  code      VARCHAR2(50) not null,
  parent_id VARCHAR2(18) not null,
  seq       INTEGER,
  leaf      INTEGER not null,
  status    INTEGER default 0 not null
)
;
comment on column IUS_DEPARTMENT.id
  is 'ID';
comment on column IUS_DEPARTMENT.space_id
  is '所属工作空间ID';
comment on column IUS_DEPARTMENT.name
  is '机构名称';
comment on column IUS_DEPARTMENT.code
  is 'code';
comment on column IUS_DEPARTMENT.parent_id
  is '上级机构ID';
comment on column IUS_DEPARTMENT.seq
  is '排序';
comment on column IUS_DEPARTMENT.leaf
  is '1：叶子节点';
comment on column IUS_DEPARTMENT.status
  is '状态（0有效）';


create table IUS_MENU
(
  id        NUMBER(10) not null,
  name      VARCHAR2(50) not null,
  code      VARCHAR2(10) not null,
  parent_id NUMBER(10) not null,
  seq       NUMBER(9),
  icon      VARCHAR2(255),
  state     VARCHAR2(255),
  show_flag NUMBER(1) not null,
  url       VARCHAR2(255),
  channel   NUMBER(9) default 0 not null,
  leaf      NUMBER(1) not null,
  site      NUMBER not null
)
;
comment on column IUS_MENU.id
  is 'id';
comment on column IUS_MENU.name
  is '资源名称';
comment on column IUS_MENU.code
  is 'code';
comment on column IUS_MENU.parent_id
  is '上级资源id';
comment on column IUS_MENU.seq
  is '排序';
comment on column IUS_MENU.icon
  is '图标';
comment on column IUS_MENU.state
  is 'state';
comment on column IUS_MENU.show_flag
  is '展示标识（1展示）';
comment on column IUS_MENU.url
  is 'URL';
comment on column IUS_MENU.channel
  is '渠道（0初始化）';
comment on column IUS_MENU.leaf
  is '是否叶节点（0非叶子节点，1叶子节点）';
comment on column IUS_MENU.site
  is '0管理端（具体由业务确定）';
alter table IUS_MENU
  add constraint IUS_MENU_PK primary key (ID)
  using index
  ;


create table IUS_MENU_AUTH
(
  id      NUMBER(10) not null,
  menu_id NUMBER(10) not null,
  auth_id NUMBER(10) not null,
  status  INTEGER not null
)
;
comment on table IUS_MENU_AUTH
  is '菜单权限关联关系表';
comment on column IUS_MENU_AUTH.id
  is 'ID';
comment on column IUS_MENU_AUTH.menu_id
  is '资源ID';
comment on column IUS_MENU_AUTH.auth_id
  is '操作权限ID';
comment on column IUS_MENU_AUTH.status
  is '状态（0有效）';


create table IUS_PASSWORD
(
  user_id     VARCHAR2(18) not null,
  password    VARCHAR2(128) not null,
  update_time TIMESTAMP(6) not null,
  type        INTEGER default 0 not null
)
;
comment on table IUS_PASSWORD
  is '密码信息表';
comment on column IUS_PASSWORD.user_id
  is '用户ID';
comment on column IUS_PASSWORD.password
  is '加严之后的md5值';
comment on column IUS_PASSWORD.update_time
  is '修改时间';
comment on column IUS_PASSWORD.type
  is '用来区分密码安全等级，比如支付，查询密码等，具体有业务来定';
alter table IUS_PASSWORD
  add primary key (USER_ID, TYPE)
  using index
  ;


create table IUS_PROFILE
(
  id          VARCHAR2(18) not null,
  name        VARCHAR2(255) not null,
  mobile      VARCHAR2(18),
  telephone   VARCHAR2(20),
  sex         NUMBER,
  dept_id     VARCHAR2(18) not null,
  icon        VARCHAR2(50),
  nickname    VARCHAR2(50),
  email       VARCHAR2(50),
  remark      VARCHAR2(500),
  locked      NUMBER not null,
  create_date TIMESTAMP(6),
  space_id    VARCHAR2(18) not null,
  id_number   VARCHAR2(20)
)
;
comment on table IUS_PROFILE
  is '用户概述信息';
comment on column IUS_PROFILE.name
  is '姓名';
comment on column IUS_PROFILE.mobile
  is '手机号';
comment on column IUS_PROFILE.telephone
  is '联系电话';
comment on column IUS_PROFILE.sex
  is '性别（0女，1男）';
comment on column IUS_PROFILE.dept_id
  is '机构ID';
comment on column IUS_PROFILE.icon
  is '头像';
comment on column IUS_PROFILE.nickname
  is '昵称';
comment on column IUS_PROFILE.email
  is '邮箱';
comment on column IUS_PROFILE.remark
  is '备注';
comment on column IUS_PROFILE.locked
  is '0正常，1锁住';
comment on column IUS_PROFILE.create_date
  is '添加时间';
comment on column IUS_PROFILE.space_id
  is '工作空间ID';
comment on column IUS_PROFILE.id_number
  is '身份证号';
alter table IUS_PROFILE
  add constraint IUS_PROFILE_PK primary key (ID)
  using index
 ;


create table IUS_ROLE
(
  id       VARCHAR2(18) not null,
  name     VARCHAR2(32) not null,
  remark   VARCHAR2(128),
  type     NUMBER(9) default 1 not null,
  channel  NUMBER(9) default 0 not null,
  space_id VARCHAR2(18) not null,
  dept_id  VARCHAR2(18) default 0 not null
)
;
comment on table IUS_ROLE
  is '角色信息表';
comment on column IUS_ROLE.id
  is '角色ID';
comment on column IUS_ROLE.name
  is '角色名称';
comment on column IUS_ROLE.remark
  is '9:系统默认，用户不能删除';
comment on column IUS_ROLE.type
  is '1：系统管理员，2普通角色';
comment on column IUS_ROLE.channel
  is '渠道（0初始化，1平台录入）';
comment on column IUS_ROLE.space_id
  is '所属工作空间ID';
comment on column IUS_ROLE.dept_id
  is '所属部门ID（0表示角色与部门不关联）';
alter table IUS_ROLE
  add constraint IUS_ROLE_PK primary key (ID)
  using index
  ;


create table IUS_ROLE_AUTH
(
  id      NUMBER(10) not null,
  role_id VARCHAR2(18) not null,
  auth_id NUMBER(10) not null,
  status  NUMBER(1) not null
)
;
comment on table IUS_ROLE_AUTH
  is '角色权限关联关系表';
comment on column IUS_ROLE_AUTH.role_id
  is '角色ID';
comment on column IUS_ROLE_AUTH.auth_id
  is '权限ID';
comment on column IUS_ROLE_AUTH.status
  is '状态';
alter table IUS_ROLE_AUTH
  add constraint PK_IUS_ROLE_AUTH primary key (ID)
  using index
  ;


create table IUS_ROLE_MENU
(
  role_id VARCHAR2(18) not null,
  menu_id NUMBER(18) not null,
  channel NUMBER(9) default 0 not null
)
;
comment on table IUS_ROLE_MENU
  is '角色与资源关系表';
comment on column IUS_ROLE_MENU.role_id
  is '角色ID';
comment on column IUS_ROLE_MENU.menu_id
  is '资源ID';
comment on column IUS_ROLE_MENU.channel
  is '渠道';


create table IUS_TEMPLATE
(
  id     VARCHAR2(18) not null,
  name   VARCHAR2(100) not null,
  site   INTEGER not null,
  remark VARCHAR2(500)
)
;
comment on table IUS_TEMPLATE
  is '权限模板信息';
comment on column IUS_TEMPLATE.name
  is '权限模板名称';
comment on column IUS_TEMPLATE.site
  is '0平台管理台，1核心企业&代理商&供应商，2承运商（由业务决定）';
comment on column IUS_TEMPLATE.remark
  is '备注';


create table IUS_TMP_AUTH
(
  tmp_id  VARCHAR2(18) not null,
  auth_id INTEGER not null
)
;
comment on table IUS_TMP_AUTH
  is '权限模板关系表';
comment on column IUS_TMP_AUTH.tmp_id
  is '权限模板ID';
comment on column IUS_TMP_AUTH.auth_id
  is '操作权限ID';


create table IUS_TMP_MENU
(
  tmp_id  VARCHAR2(18) not null,
  menu_id INTEGER not null
)
;
comment on table IUS_TMP_MENU
  is '角色与menu关系';
comment on column IUS_TMP_MENU.tmp_id
  is '权限模板ID';
comment on column IUS_TMP_MENU.menu_id
  is '资源ID';


create table IUS_USER_ROLES
(
  user_id   VARCHAR2(18) not null,
  role_id   VARCHAR2(18) not null,
  role_flag NUMBER default 0
)
;
comment on table IUS_USER_ROLES
  is '用户角色表';
comment on column IUS_USER_ROLES.user_id
  is '用户标识';
comment on column IUS_USER_ROLES.role_id
  is '角色标识';
comment on column IUS_USER_ROLES.role_flag
  is '是否主角色（1主角色，0普通角色，由业务决定）';


create table IUS_WORKSPACE
(
  id         VARCHAR2(18) not null,
  name       VARCHAR2(50) not null,
  site       INTEGER,
  remark     VARCHAR2(500),
  status     INTEGER not null,
  temp_id    VARCHAR2(18) not null,
  space_type INTEGER default 1
)

;
comment on table IUS_WORKSPACE
  is '工作空间信息表';
comment on column IUS_WORKSPACE.name
  is '工作空间名称';
comment on column IUS_WORKSPACE.site
  is '所属终端（0管理端），由业务决定';
comment on column IUS_WORKSPACE.remark
  is '备注';
comment on column IUS_WORKSPACE.status
  is '状态（0有效，1无效）';
comment on column IUS_WORKSPACE.temp_id
  is '权限模板ID';
comment on column IUS_WORKSPACE.space_type
  is '工作空间类型';

  commit;