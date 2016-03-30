ALTER TABLE `grp` DROP INDEX `code_UNIQUE`;
ALTER TABLE `domain` DROP INDEX `name_UNIQUE`;
ALTER TABLE `domain` DROP INDEX `display_name_UNIQUE`;
ALTER TABLE `role` DROP INDEX `domain_rolename_unique`;
ALTER TABLE `permission` DROP INDEX `perm_unique`;

ALTER TABLE `cfg` CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;

insert into `role_code`(`code`, `description`) values ('ROLE_GRAND_MASTER', '宗师');
insert into `role_code`(`code`, `description`) values ('ROLE_MASTER', '大师');
insert into `role_code`(`code`, `description`) values ('ROLE_DIAMOND', '钻石');
insert into `role_code`(`code`, `description`) values ('ROLE_PLATINUM', '白金');
insert into `role_code`(`code`, `description`) values ('ROLE_GOLD', '黄金');
insert into `role_code`(`code`, `description`) values ('ROLE_SILVER', '白银');
insert into `role_code`(`code`, `description`) values ('ROLE_BRONZE', '青铜');
insert into `role_code`(`code`, `description`) values ('ROLE_STEEL', '钢铁');

drop table `cfg`;

CREATE TABLE IF NOT EXISTS `cfg_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;
CREATE UNIQUE INDEX `cfg_type_UNIQUE` ON `cfg_type` (`code` ASC);

insert into `cfg_type`(`code`) values ('FILE');
insert into `cfg_type`(`code`) values ('TEXT');

CREATE TABLE IF NOT EXISTS `cfg` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `cfg_key` VARCHAR(64) NOT NULL,
  `value` VARCHAR(512) NOT NULL,
  `file` MEDIUMBLOB NULL DEFAULT NULL,
  `cfg_type_id` INT NOT NULL,
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

select @cfg_file_id := (select id from `cfg_type` where code='FILE');
select @cfg_text_id := (select id from `cfg_type` where code='TEXT');
insert into `cfg`(`cfg_key`,`value`,`file`,`cfg_type_id`) values ('TECHOPS_LOGO', 'logo.png', null, @cfg_file_id);
insert into `cfg`(`cfg_key`,`value`,`file`,`cfg_type_id`) values ('TECHOPS_ICON', 'favicon.png', null, @cfg_file_id);
insert into `cfg`(`cfg_key`,`value`,`file`,`cfg_type_id`) values ('TECHOPS_TITLE', '点融网-权限运维系统', null, @cfg_text_id);