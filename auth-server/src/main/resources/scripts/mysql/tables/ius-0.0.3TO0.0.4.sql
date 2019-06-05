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