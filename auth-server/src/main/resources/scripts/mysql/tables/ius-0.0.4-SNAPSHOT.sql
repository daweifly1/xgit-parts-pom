CREATE TABLE IF NOT EXISTS `ius_account` (
  `user_id` varchar(72) NOT NULL COMMENT '用户id',
  `login_name` varchar(512) NOT NULL COMMENT '登陆名',
  `last_login_time` datetime(6) DEFAULT NULL COMMENT '上次登陆时间',
  `last_login_ip` varchar(256) DEFAULT NULL COMMENT '上次登陆ip',
  `status` double NOT NULL COMMENT '状态',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_time` datetime(6) NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账号信息表';

CREATE TABLE IF NOT EXISTS `ius_app_authority` (
  `app_id` varchar(50) NOT NULL COMMENT '应用编号',
  `auth_code` int(11) NOT NULL COMMENT '权限码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方应用权限表';

CREATE TABLE IF NOT EXISTS `ius_app_info` (
  `app_id` varchar(32) NOT NULL COMMENT '应用id',
  `app_secret` varchar(32) NOT NULL COMMENT '应用秘钥',
  `app_name` varchar(32) NOT NULL COMMENT '应用名称',
  `redirect_url` varchar(32) NOT NULL COMMENT '跳转路径',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `app_id` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方应用信息表';

CREATE TABLE IF NOT EXISTS `ius_auth` (
  `auth_id` bigint(20) NOT NULL COMMENT '权限ID',
  `auth_name` varchar(40) DEFAULT NULL COMMENT '权限名称',
  `auth_desc` varchar(40) DEFAULT NULL COMMENT '权限描述',
  PRIMARY KEY (`auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限信息表';

CREATE TABLE IF NOT EXISTS `ius_department` (
  `id` varchar(72) DEFAULT NULL COMMENT '部门ID',
  `space_id` varchar(72) DEFAULT NULL COMMENT '工作空间ID',
  `name` varchar(200) DEFAULT NULL COMMENT '机构名称',
  `code` varchar(200) DEFAULT NULL COMMENT '机构编号',
  `parent_id` varchar(72) DEFAULT NULL COMMENT '上级机构ID',
  `seq` double DEFAULT NULL COMMENT '排序',
  `leaf` double DEFAULT NULL COMMENT '是否叶节点（1：叶节点）',
  `status` double DEFAULT '0' COMMENT '状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分支机构表';

CREATE TABLE IF NOT EXISTS `ius_menu` (
  `id` bigint(20) NOT NULL COMMENT '菜单ID',
  `name` varchar(200) DEFAULT NULL COMMENT '菜单名称',
  `code` varchar(40) DEFAULT NULL COMMENT '菜单code',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级菜单ID',
  `seq` int(11) DEFAULT NULL COMMENT '排序编号',
  `icon` varchar(1020) DEFAULT NULL COMMENT '图标',
  `state` varchar(1020) DEFAULT NULL COMMENT 'state',
  `show_flag` tinyint(4) DEFAULT NULL COMMENT '展示标识',
  `url` varchar(1020) DEFAULT NULL COMMENT 'url路径',
  `channel` int(11) DEFAULT '0' COMMENT 'channel`',
  `leaf` tinyint(4) DEFAULT NULL COMMENT '是否叶节点（1：叶节点）',
  `site` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源信息表';

CREATE TABLE IF NOT EXISTS `ius_menu_auth` (
  `id` bigint(20) DEFAULT NULL COMMENT '关系ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  `auth_id` bigint(20) DEFAULT NULL COMMENT '权限ID',
  `status` double DEFAULT NULL COMMENT '状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源权限关系表';

CREATE TABLE IF NOT EXISTS `ius_oauth_record` (
  `app_id` varchar(36) NOT NULL COMMENT '应用编号',
  `user_id` varchar(36) NOT NULL COMMENT '用户编号',
  `auth_time` varchar(36) NOT NULL COMMENT '授权时间',
  `is_del` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标识'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth授权记录表';

CREATE TABLE IF NOT EXISTS `ius_password` (
  `user_id` varchar(72) NOT NULL COMMENT '用户ID',
  `password` varchar(512) NOT NULL COMMENT '密码',
  `update_time` datetime(6) NOT NULL COMMENT '更新时间',
  `type` double NOT NULL DEFAULT '0' COMMENT '类型',
  PRIMARY KEY (`user_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='密码信息';

CREATE TABLE IF NOT EXISTS `ius_profile` (
  `id` varchar(72) NOT NULL COMMENT '用户信息ID',
  `name` varchar(1020) DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(72) DEFAULT NULL COMMENT '手机',
  `telephone` varchar(80) DEFAULT NULL COMMENT '电话',
  `sex` double DEFAULT NULL COMMENT '性别',
  `dept_id` varchar(72) DEFAULT NULL COMMENT '部门ID',
  `icon` varchar(200) DEFAULT NULL COMMENT '图标',
  `nickname` varchar(200) DEFAULT NULL COMMENT '昵称',
  `email` varchar(200) DEFAULT NULL COMMENT 'email',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `locked` double DEFAULT NULL COMMENT '是否锁定',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `space_id` varchar(72) DEFAULT NULL COMMENT '工作空间ID',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户资料信息';

CREATE TABLE IF NOT EXISTS `ius_role` (
  `id` varchar(72) NOT NULL COMMENT '角色ID',
  `name` varchar(128) NOT NULL COMMENT '角色名称',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '角色类型',
  `channel` int(11) NOT NULL DEFAULT '0' COMMENT 'channel',
  `space_id` varchar(72) DEFAULT NULL COMMENT '空间ID',
  `dept_id` varchar(72) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息';

CREATE TABLE IF NOT EXISTS `ius_role_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色权限关系ID',
  `role_id` varchar(72) NOT NULL COMMENT '角色ID',
  `auth_id` bigint(20) NOT NULL COMMENT '权限ID',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色权限关系表';

CREATE TABLE IF NOT EXISTS `ius_role_menu` (
  `role_id` varchar(72) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `channel` int(11) NOT NULL DEFAULT '0' COMMENT 'channel'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单关系表';

CREATE TABLE IF NOT EXISTS `ius_template` (
  `id` varchar(72) DEFAULT NULL COMMENT '模板ID',
  `name` varchar(200) DEFAULT NULL COMMENT '模板名称',
  `site` double DEFAULT NULL COMMENT '端口号',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板信息';

CREATE TABLE IF NOT EXISTS `ius_tmp_auth` (
  `tmp_id` varchar(72) DEFAULT NULL COMMENT '模板ID',
  `auth_id` double DEFAULT NULL COMMENT '权限ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板权限关系表';

CREATE TABLE IF NOT EXISTS `ius_tmp_menu` (
  `tmp_id` varchar(72) DEFAULT NULL COMMENT '模板ID',
  `menu_id` double DEFAULT NULL COMMENT '菜单ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板菜单关系表';

CREATE TABLE IF NOT EXISTS `ius_user_roles` (
  `user_id` varchar(18) NOT NULL COMMENT '用户id',
  `role_id` varchar(18) NOT NULL COMMENT '角色id',
  `role_flag` INT(11) NULL DEFAULT '0',
  UNIQUE KEY `uq__ius_user_roles_ids` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

CREATE TABLE IF NOT EXISTS `ius_workspace` (
  `id` varchar(72) DEFAULT NULL COMMENT '工作空间ID',
  `name` varchar(200) DEFAULT NULL COMMENT '工作空间名称',
  `site` double DEFAULT NULL COMMENT '端口号',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `status` double DEFAULT NULL COMMENT '状态',
  `temp_id` varchar(72) DEFAULT NULL,
  `space_type` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作空间';

CREATE TABLE `version_record` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`version` VARCHAR(50) NOT NULL DEFAULT '0',
	`update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
)
COMMENT='版本记录表，同步数据库与代码版本用，请勿手动删除！'
ENGINE=InnoDB
COLLATE='utf8_general_ci'
;

alter table ius_profile add area_code varchar(20) comment '地区编码';