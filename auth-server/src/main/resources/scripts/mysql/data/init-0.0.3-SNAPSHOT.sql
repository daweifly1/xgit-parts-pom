INSERT INTO `ius_account` (`user_id`, `login_name`, `last_login_time`, `last_login_ip`, `status`, `create_time`, `update_time`) VALUES
	('117744196566745088', 'admin001', '2018-02-06 18:25:09.000000', '', 0, '2018-01-18 13:42:02.000000', '2018-02-06 18:25:09.000000');

INSERT INTO `ius_department` (`id`, `space_id`, `name`, `code`, `parent_id`, `seq`, `leaf`, `status`) VALUES
	('1', '1', '综合人才部', '002', '0', 1, 1, NULL);

INSERT INTO `ius_menu` (`id`, `name`, `code`, `parent_id`, `seq`, `icon`, `state`, `show_flag`, `url`, `channel`, `leaf`, `site`) VALUES
	(1, '内部管理', '001', 0, 1, '', 'app.internal', 1, '/internal', 0, 0, 0),
	(2, '组织机构', '001001', 1, 2, '', 'app.internal.organize', 1, '/internal/organize', 0, 1, 0),
	(3, '岗位管理', '001002', 1, 3, '', 'app.internal.role', 1, '/internal/role', 0, 1, 0),
	(4, '用户列表', '001003', 1, 4, '', 'app.internal.user', 1, '/internal/user', 0, 1, 0);

INSERT INTO `ius_password` (`user_id`, `password`, `update_time`, `type`) VALUES
	('117744196566745088', '4e97ee1bb895e98a26263ee4ac9621', '2018-01-18 19:20:08.000000', 0);

INSERT INTO `ius_profile` (`id`, `name`, `mobile`, `telephone`, `sex`, `dept_id`, `icon`, `nickname`, `email`, `remark`, `locked`, `create_date`, `space_id`) VALUES
	('117744196566745088', 'test3', '13786590859', '02583376542', 1, '1', NULL, '小', '123@163.com', NULL, 0, NULL, '1');

INSERT INTO `ius_role` (`id`, `name`, `remark`, `type`, `channel`, `space_id`, `dept_id`) VALUES
	('1', '默认管理员角色-请勿删除', '9', 0, 0, '0', NULL);

INSERT INTO `ius_role_auth` (`id`, `role_id`, `auth_id`, `status`) VALUES
	(1, '1', 10, 0);

INSERT INTO `ius_role_menu` (`role_id`, `menu_id`, `channel`) VALUES
	('1', 1, 0),
	('1', 2, 0),
	('1', 3, 0),
	('1', 4, 0);

INSERT INTO `ius_template` (`id`, `name`, `site`, `remark`) VALUES
	('1', '管理端', 0, '管理端');

INSERT INTO `ius_user_roles` (`user_id`, `role_id`) VALUES
	('117744196566745088', '1');

INSERT INTO `ius_workspace` (`id`, `name`, `site`, `remark`, `status`, `temp_id`, `space_type`) VALUES
	('1', '管理员', 0, '系统默认', 0, '1', 1);