CREATE TABLE `sys_account_social` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_id` bigint(20) DEFAULT NULL COMMENT '账号ID',
  `social_account` varchar(255) DEFAULT NULL COMMENT '社交账号',
  `type` varchar(20) DEFAULT '' COMMENT '社交账号类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_social` (`social_account`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社交账号关系表';

