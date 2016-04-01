-- DDL BEGIN:
ALTER TABLE `grp` DROP INDEX `code_UNIQUE`;
ALTER TABLE `domain` DROP INDEX `name_UNIQUE`;
ALTER TABLE `domain` DROP INDEX `display_name_UNIQUE`;
ALTER TABLE `role` DROP INDEX `domain_rolename_unique`;
ALTER TABLE `permission` DROP INDEX `perm_unique`;
ALTER TABLE `cfg` CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;

drop table `cfg`;

CREATE TABLE IF NOT EXISTS `cfg_type` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '数据库自动生成唯一id',
  `code` VARCHAR(32) NOT NULL COMMENT '唯一的配置类型值',
  PRIMARY KEY (`id`))
ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;
CREATE UNIQUE INDEX `cfg_type_UNIQUE` ON `cfg_type` (`code` ASC);

CREATE TABLE IF NOT EXISTS `cfg` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '数据库自动生成唯一id',
  `cfg_key` VARCHAR(64) NOT NULL COMMENT '唯一的配置值',
  `value` VARCHAR(512) NOT NULL COMMENT '如果cfg_type code为FILE则该值为文件名, 如果cfg_type code为TEXT, 则该值为正常value',
  `file` MEDIUMBLOB NULL DEFAULT NULL COMMENT '用于存放文件的字段, 最大16MB',
  `cfg_type_id` INT NOT NULL COMMENT '用于表示存放的cfg的type, FILE或者TEXT',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `cfg_key_UNIQUE` (`cfg_key` ASC),
  INDEX `fk_cfg_cfg_type1_idx` (`cfg_type_id` ASC),
  CONSTRAINT `fk_cfg_cfg_type1`
  FOREIGN KEY (`cfg_type_id`)
  REFERENCES `cfg_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;
-- DDL END

-- DML BEGIN:
BEGIN;
insert into `role_code`(`code`, `description`) values ('ROLE_GRAND_MASTER', '宗师');
insert into `role_code`(`code`, `description`) values ('ROLE_MASTER', '大师');
insert into `role_code`(`code`, `description`) values ('ROLE_DIAMOND', '钻石');
insert into `role_code`(`code`, `description`) values ('ROLE_PLATINUM', '白金');
insert into `role_code`(`code`, `description`) values ('ROLE_GOLD', '黄金');
insert into `role_code`(`code`, `description`) values ('ROLE_SILVER', '白银');
insert into `role_code`(`code`, `description`) values ('ROLE_BRONZE', '青铜');
insert into `role_code`(`code`, `description`) values ('ROLE_STEEL', '钢铁');

insert into `cfg_type`(`code`) values ('FILE');
insert into `cfg_type`(`code`) values ('TEXT');

select @cfg_file_id := (select id from `cfg_type` where code='FILE');
select @cfg_text_id := (select id from `cfg_type` where code='TEXT');
insert into `cfg`(`cfg_key`,`value`,`file`,`cfg_type_id`) values ('TECHOPS_LOGO', 'logo.png', null, @cfg_file_id);
insert into `cfg`(`cfg_key`,`value`,`file`,`cfg_type_id`) values ('TECHOPS_ICON', 'favicon.png', null, @cfg_file_id);
insert into `cfg`(`cfg_key`,`value`,`file`,`cfg_type_id`) values ('TECHOPS_TITLE', '点融网-权限运维系统', null, @cfg_text_id);
insert into `perm_type`(`id`, `type`) values (3, 'PRIVILEGE');
COMMIT;