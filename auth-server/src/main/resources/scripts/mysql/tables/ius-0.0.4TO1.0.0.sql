CREATE TABLE `ius_thirdparty_oauth` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
	`app_id` VARCHAR(128) NULL COMMENT '应用id',
	`user_id` VARCHAR(20) NOT NULL COMMENT '用户id',
	`thirdparty_id` VARCHAR(128) NOT NULL COMMENT '第三方账户id(微信openId)',
	`bind_type` TINYINT NOT NULL DEFAULT '1' COMMENT '绑定类型(1-微信)',
	`meta_data` VARCHAR(2000) NULL COMMENT '第三方元数据',
	`remark` VARCHAR(100) NULL COMMENT '备注',
	`bind_status` TINYINT NOT NULL DEFAULT '0' COMMENT '绑定状态(0-未绑定 1-已绑定 2-已解绑)',
	`bind_time` DATETIME NULL COMMENT '绑定时间',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='第三方授权绑定表'
COLLATE='utf8_general_ci'
;

CREATE TABLE `ius_thirdparty_secret` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
	`app_id` VARCHAR(200) NOT NULL COMMENT '应用id',
	`app_secret` VARCHAR(200) NOT NULL COMMENT '应用秘钥',
	`token` VARCHAR(200) NULL COMMENT 'token',
	`aes_key` VARCHAR(200) NULL COMMENT 'AES加密key',
	`workspace_id` VARCHAR(100) NULL COMMENT '工作空间id',
	`setting_status` TINYINT NULL DEFAULT '0' COMMENT '设定状态(0-启用 1-停用)',
	`update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='第三方授权秘钥表'
COLLATE='utf8_general_ci'
;

ALTER TABLE ius_role ADD seq_no INT DEFAULT 0 COMMENT "序号";