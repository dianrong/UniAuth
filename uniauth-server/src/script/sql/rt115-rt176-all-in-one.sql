-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NULL,
  `email` VARCHAR(64) NOT NULL,
  `phone` VARCHAR(64) NULL,
  `password` VARCHAR(64) NOT NULL,
  `password_salt` VARCHAR(64) NULL,
  `last_login_time` DATETIME NULL,
  `last_login_ip` VARCHAR(128) NULL,
  `fail_count` TINYINT(3) NOT NULL DEFAULT 0,
  `status` TINYINT(3) NOT NULL DEFAULT 0,
  `create_date` DATETIME NULL,
  `last_update` DATETIME NULL,
  `password_date` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE INDEX `user_email_idx` ON `user` (`email` ASC);

CREATE INDEX `user_name_idx` ON `user` (`name` ASC);

CREATE INDEX `user_phone_idx` ON `user` (`phone` ASC);


-- -----------------------------------------------------
-- Table `grp`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `grp` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NULL,
  `code` VARCHAR(128) NOT NULL,
  `description` VARCHAR(512) NULL,
  `status` TINYINT(3) NOT NULL DEFAULT 0,
  `create_date` DATETIME NULL,
  `last_update` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE INDEX `group_name_idx` ON `grp` (`name` ASC);

CREATE UNIQUE INDEX `code_UNIQUE` ON `grp` (`code` ASC);


-- -----------------------------------------------------
-- Table `grp_path`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `grp_path` (
  `ancestor` INT NOT NULL,
  `descendant` INT NOT NULL,
  `deepth` TINYINT(3) NOT NULL,
  PRIMARY KEY (`ancestor`, `descendant`),
  CONSTRAINT `fk_grp_path_grp1`
    FOREIGN KEY (`ancestor`)
    REFERENCES `grp` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_grp_path_grp2`
    FOREIGN KEY (`descendant`)
    REFERENCES `grp` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_grp_path_grp2_idx` ON `grp_path` (`descendant` ASC);


-- -----------------------------------------------------
-- Table `user_grp`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_grp` (
  `user_id` BIGINT(20) NOT NULL,
  `grp_id` INT NOT NULL,
  `type` TINYINT(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`, `grp_id`),
  CONSTRAINT `fk_user_group_grp1`
    FOREIGN KEY (`grp_id`)
    REFERENCES `grp` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_grp_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_user_group_grp1_idx` ON `user_grp` (`grp_id` ASC);

CREATE INDEX `fk_user_grp_user1_idx` ON `user_grp` (`user_id` ASC);


-- -----------------------------------------------------
-- Table `domain`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `domain` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(64) NOT NULL,
  `display_name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(512) NULL,
  `status` TINYINT(3) NOT NULL DEFAULT 0,
  `create_date` DATETIME NULL,
  `last_update` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `name_UNIQUE` ON `domain` (`code` ASC);

CREATE UNIQUE INDEX `display_name_UNIQUE` ON `domain` (`display_name` ASC);


-- -----------------------------------------------------
-- Table `role_code`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `role_code` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ADMIN, USER, VIEW',
  `code` VARCHAR(32) NOT NULL COMMENT 'ADMIN, USER, VIEW',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `code_UNIQUE` ON `role_code` (`code` ASC);


-- -----------------------------------------------------
-- Table `role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NULL,
  `description` VARCHAR(512) NULL,
  `status` TINYINT(3) NOT NULL DEFAULT 0,
  `domain_id` INT NOT NULL,
  `role_code_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_role_domain1`
    FOREIGN KEY (`domain_id`)
    REFERENCES `domain` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_role_role_code1`
    FOREIGN KEY (`role_code_id`)
    REFERENCES `role_code` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_role_domain1_idx` ON `role` (`domain_id` ASC);

CREATE INDEX `fk_role_role_code1_idx` ON `role` (`role_code_id` ASC);

CREATE UNIQUE INDEX `domain_rolename_unique` ON `role` (`domain_id` ASC, `name` ASC);

-- -----------------------------------------------------
-- Table `user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` BIGINT(20) NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  CONSTRAINT `fk_user_role_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_user_role_user1_idx` ON `user_role` (`user_id` ASC);

CREATE INDEX `fk_user_role_role1_idx` ON `user_role` (`role_id` ASC);


-- -----------------------------------------------------
-- Table `perm_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `perm_type` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'URIPATTERN',
  `type` VARCHAR(32) NOT NULL COMMENT 'URI_PATTERN',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `type_UNIQUE` ON `perm_type` (`type` ASC);


-- -----------------------------------------------------
-- Table `permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `permission` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(128) NULL,
  `description` VARCHAR(512) NULL,
  `status` TINYINT(3) NOT NULL DEFAULT 0,
  `perm_type_id` INT NOT NULL,
  `domain_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_permission_perm_type1`
    FOREIGN KEY (`perm_type_id`)
    REFERENCES `perm_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_permission_domain1`
    FOREIGN KEY (`domain_id`)
    REFERENCES `domain` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_permission_perm_type1_idx` ON `permission` (`perm_type_id` ASC);

CREATE INDEX `fk_permission_domain1_idx` ON `permission` (`domain_id` ASC);

CREATE UNIQUE INDEX `perm_unique` ON `permission` (`value` ASC, `perm_type_id` ASC, `domain_id` ASC);


-- -----------------------------------------------------
-- Table `grp_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `grp_role` (
  `grp_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`grp_id`, `role_id`),
  CONSTRAINT `fk_group_domain_role_acl_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_group_role_grp1`
    FOREIGN KEY (`grp_id`)
    REFERENCES `grp` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_group_domain_role_acl_role1_idx` ON `grp_role` (`role_id` ASC);

CREATE INDEX `fk_group_role_grp1_idx` ON `grp_role` (`grp_id` ASC);


-- -----------------------------------------------------
-- Table `role_permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `role_permission` (
  `role_id` INT NOT NULL,
  `permission_id` INT NOT NULL,
  PRIMARY KEY (`role_id`, `permission_id`),
  CONSTRAINT `fk_role_permission_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_role_permission_permission1`
    FOREIGN KEY (`permission_id`)
    REFERENCES `permission` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_role_permission_role1_idx` ON `role_permission` (`role_id` ASC);

CREATE INDEX `fk_role_permission_permission1_idx` ON `role_permission` (`permission_id` ASC);

-- -----------------------------------------------------
-- Table `audit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `audit` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `obj_id` BIGINT(20) NULL,
  `obj_name` VARCHAR(32) NULL,
  `action_date` DATETIME NULL,
  `action` TINYINT(2) NULL COMMENT 'how',
  `before_action` VARCHAR(1000) NULL,
  `after_action` VARCHAR(1000) NULL,
  `client_ip` VARCHAR(128) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE INDEX `audit_userid_idx` ON `audit` (`user_id` ASC);

CREATE INDEX `audit_action_date` ON `audit` (`action_date` ASC);


-- -----------------------------------------------------
-- Table `stakeholder`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stakeholder` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NULL,
  `email` VARCHAR(64) NULL,
  `phone` VARCHAR(64) NULL,
  `jobtitle` VARCHAR(64) NULL,
  `domain_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_stakeholder_domain1`
    FOREIGN KEY (`domain_id`)
    REFERENCES `domain` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_stakeholder_domain1_idx` ON `stakeholder` (`domain_id` ASC);

alter table `user` AUTO_INCREMENT=200000000;
alter table `role_code` add column `description` VARCHAR(512) NULL AFTER `code`;
insert into `role_code`(`code`, `description`) values ('ROLE_SUPER_ADMIN', '超级管理员');
insert into `role_code`(`code`, `description`) values ('ROLE_ADMIN', '管理员');
insert into `role_code`(`code`, `description`) values ('ROLE_NORMAL', '普通用户');
insert into `role_code`(`code`, `description`) values ('ROLE_GUEST', 'Guest用户');

insert into `perm_type`(`id`, `type`) values (1, 'DOMAIN');
insert into `perm_type`(`id`, `type`) values (2, 'URI_PATTERN');

insert into user(id, name, email, phone, password, password_salt, last_login_time, last_login_ip, fail_count, status, create_date, last_update, password_date)
values('200000001', 'guige', 'first.admin@test.com', '13011111111', 'GRodddDAZjK2tGZ6kT7ImP8ILwU=', 'I9JTzG2zzBAW3Q5NvP8lRg==', now(), '192.168.18.5', '0', '0', now(), now(), now());

insert into grp(id, name, code, description,status, create_date, last_update) values(1, '点融网', 'GRP_ROOT','点融网根组', 0, now(), now());
insert into grp(id, name, code, description,status, create_date, last_update) values(2, 'techops超级管理员组', 'GRP_TECHOPS_SUPER_ADMIN','techops超级管理员组，组内的人期拥有techops顶级权限', 0, now(), now());
insert into grp_path(ancestor, descendant, deepth) values(1, 1, 0);
insert into grp_path(ancestor, descendant, deepth) values(1, 2, 1);
insert into grp_path(ancestor, descendant, deepth) values(2, 2, 0);

insert into user_grp(user_id, grp_id, type) values(200000001, 2, 0);
insert into user_grp(user_id, grp_id, type) values(200000001, 1, 1);

insert into domain(id, code, display_name, description, status, create_date, last_update) values(1, 'techops', '权限管理系统', '点融权限管理系统，管理各个子系统的权限和资源访问', 0, now(), now());

insert into role(id, name,description, status, domain_id, role_code_id) values(1, 'techops超级管理员角色', 'techops超级管理员角色，管理所有其他域的权限', 0, 1, 1);

insert into permission(id, value, description, status, perm_type_id, domain_id) values(1, 'techops', '拥有techops整个域的permission，在techops上可进行任何操作(管理组需要额外配置)',0,1,1);

insert into role_permission(role_id, permission_id) values(1, 1);

insert into grp_role(grp_id, role_id)  values(2, 1);

-- change the primarykey of user_grp
ALTER TABLE `user_grp` CHANGE COLUMN `type` `type` TINYINT(3) NOT NULL ,DROP PRIMARY KEY,ADD PRIMARY KEY (`user_id`, `grp_id`, `type`);

alter table `audit` add column `req_uuid` varchar(128) not null;
alter table `audit` add column `req_url` varchar(256) not null;
alter table `audit` add column `req_success` tinyint(3) not null;
alter table `audit` add column `req_exp` varchar(1024);

drop table if exists audit;

CREATE TABLE `audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `req_date` datetime DEFAULT NULL,
  `domain_id` int(11) default null,
  `req_ip` varchar(128) DEFAULT NULL,
  `req_uuid` varchar(128) DEFAULT NULL,
  `req_url` varchar(64) DEFAULT NULL,
  `req_seq` bigint(20) DEFAULT NULL,
  `req_class` varchar(64) DEFAULT NULL,
  `req_method` varchar(64) DEFAULT NULL,
  `req_success` tinyint(3) DEFAULT NULL,
  `req_exp` varchar(3072) DEFAULT NULL,
  `req_elapse` bigint(20) DEFAULT NULL,
  `req_param` varchar(256) DEFAULT NULL,
  `req_result` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

delete from user_grp where user_id > 0;
delete from grp_role where grp_id > 0;
delete from grp_path where ancestor > 0;

alter table `user` AUTO_INCREMENT=300000010;
update user set id = 300000001 where id = 200000001;

alter table `grp` AUTO_INCREMENT=100010;
update grp set id = 100001 where id = 1;
update grp set id = 100002 where id = 2;

insert into grp_path(ancestor, descendant, deepth) values(100001, 100001, 0);
insert into grp_path(ancestor, descendant, deepth) values(100001, 100002, 1);
insert into grp_path(ancestor, descendant, deepth) values(100002, 100002, 0);

insert into user_grp(user_id, grp_id, type) values(300000001, 100002, 0);
insert into user_grp(user_id, grp_id, type) values(300000001, 100001, 1);

insert into grp_role(grp_id, role_id)  values(100002, 1);


CREATE TABLE `cfg` (
  `id` int(11) NOT NULL,
  `key` varchar(64) NOT NULL,
  `type` varchar(16) NOT NULL,
  `value` varchar(512) DEFAULT NULL,
  `file` blob,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_UNIQUE` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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

ALTER TABLE `permission` ADD COLUMN `value_ext` VARCHAR(128) NULL AFTER `value`;

CREATE TABLE IF NOT EXISTS `tag_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(32) NOT NULL,
  `domain_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  CONSTRAINT `fk_tag_type_domain`
  FOREIGN KEY (`domain_id`)
  REFERENCES `domain` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `tag` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(64) NULL,
  `status` TINYINT(3) NOT NULL,
  `description` VARCHAR(512) NULL,
  `tag_type_id` INT(11) NOT NULL,
  `create_date` DATETIME NULL,
  `last_update` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_tag_code_idx` (`code` ASC),
  CONSTRAINT `fk_tag_type`
  FOREIGN KEY (`tag_type_id`)
  REFERENCES `tag_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_tag` (
  `tag_id` INT(11) NOT NULL,
  `user_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`tag_id`, `user_id`),
  INDEX `fk_user_tag_user1_idx` (`user_id` ASC),
  INDEX `fk_tag_user_tag1_idx` (`tag_id` ASC),
  CONSTRAINT `fk_user_tag_tag1`
  FOREIGN KEY (`tag_id`)
  REFERENCES `tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_tag_user1`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

ALTER TABLE `grp_path` ADD INDEX `fk_path_grp_path2_idx` (`ancestor` ASC);

-- insert into tag_type(`code`) values ('TITLE');

CREATE TABLE IF NOT EXISTS `grp_tag` (
  `tag_id` INT(11) NOT NULL,
  `grp_id` INT(11) NOT NULL,
  INDEX `fk_tag_grp_tag1_idx` (`tag_id` ASC),
  PRIMARY KEY (`grp_id`, `tag_id`),
  INDEX `fk_grp_tag_grp1_idx` (`grp_id` ASC),
  CONSTRAINT `fk_tag_grp_tag1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tag_grp_grp1`
    FOREIGN KEY (`grp_id`)
    REFERENCES `grp` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE `user_extend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑主键',
  `code` varchar(50) NOT NULL COMMENT '扩展属性代码（名称）',
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展属性描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='用户扩展属性主表';

CREATE TABLE `user_extend_val` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `extend_id` bigint(20) NOT NULL COMMENT '扩展属性id',
  `value_` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展属性值',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '扩展属性状态：0-启用、1-禁用',
  PRIMARY KEY (`id`),
  KEY `fk_user_extend_val_user_id` (`user_id`),
  KEY `fk_user_extend_val_user_extend_id` (`extend_id`),
  CONSTRAINT `fk_user_extend_val_user_extend_id` FOREIGN KEY (`extend_id`) REFERENCES `user_extend` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_extend_val_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB COMMENT='用户扩展属性值表';

ALTER TABLE audit COMMENT='用于记录整个系统操作日志';
ALTER TABLE cfg COMMENT='系统产品定制化配置表';
ALTER TABLE cfg_type COMMENT='产品定制化配置的code';
ALTER TABLE `domain` COMMENT='被集成系统表';
ALTER TABLE grp COMMENT='组';
ALTER TABLE grp_path COMMENT='组与组之间层级关系表';
ALTER TABLE grp_role COMMENT='组与角色之间的多对多关联关系表';
ALTER TABLE grp_tag COMMENT='组与tag之间的多对多关联关系表';
ALTER TABLE permission COMMENT='权限表';
ALTER TABLE perm_type COMMENT='权限类别表';
ALTER TABLE role COMMENT='角色表';
ALTER TABLE role_code COMMENT='角色对应的code枚举表,用于springsecurity hasRole方法判断用户是否有权限';
ALTER TABLE role_permission COMMENT='角色与权限的多对多关联表';
ALTER TABLE stakeholder COMMENT='集成系统的利益相关者表';
ALTER TABLE tag COMMENT='标签';
ALTER TABLE `tag_type` COMMENT='标签类别';
ALTER TABLE `user` COMMENT='用户表';
ALTER TABLE user_grp COMMENT='用户与组的多对多关联关系表';
ALTER TABLE user_role COMMENT='用户与角色的多对多关联关系表';
ALTER TABLE user_tag COMMENT='用户与标签的多对多关联关系表';


INSERT INTO user_extend(code, description) SELECT 'cast_wechat_id','openid' FROM DUAL WHERE NOT EXISTS(SELECT id FROM user_extend WHERE code = 'cast_wechat_id');

alter table tag_type drop index `code_UNIQUE`;
alter table tag_type add unique key `code_domainid_UNIQUE` (`code`,`domain_id`) comment 'update table tag_type unique_key from code to code + domain_id';

ALTER TABLE `domain`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键，数据库自动生成' ,
CHANGE COLUMN `code` `code` VARCHAR(64) NOT NULL COMMENT '域编码' ,
CHANGE COLUMN `display_name` `display_name` VARCHAR(128) NOT NULL COMMENT '显示名称' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '域描述' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '状态' ,
CHANGE COLUMN `create_date` `create_date` DATETIME NULL DEFAULT NULL COMMENT '创建时间' ,
CHANGE COLUMN `last_update` `last_update` DATETIME NULL DEFAULT NULL COMMENT '最后更新时间' ;

ALTER TABLE `grp`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '组的id，唯一键' ,
CHANGE COLUMN `name` `name` VARCHAR(128) NULL DEFAULT NULL COMMENT '组名称' ,
CHANGE COLUMN `code` `code` VARCHAR(128) NOT NULL COMMENT '组编码' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '组描述' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '状态' ,
CHANGE COLUMN `create_date` `create_date` DATETIME NULL DEFAULT NULL COMMENT '创建时间' ,
CHANGE COLUMN `last_update` `last_update` DATETIME NULL DEFAULT NULL COMMENT '最后修改时间' ;

ALTER TABLE `grp_role`
CHANGE COLUMN `grp_id` `grp_id` INT(11) NOT NULL COMMENT '组的id' ,
CHANGE COLUMN `role_id` `role_id` INT(11) NOT NULL COMMENT '角色的id' ;

ALTER TABLE `grp_tag`
CHANGE COLUMN `tag_id` `tag_id` INT(11) NOT NULL COMMENT '标签的id' ,
CHANGE COLUMN `grp_id` `grp_id` INT(11) NOT NULL COMMENT '组的id' ;

ALTER TABLE `grp_path`
CHANGE COLUMN `ancestor` `ancestor` INT(11) NOT NULL COMMENT '父节点id' ,
CHANGE COLUMN `descendant` `descendant` INT(11) NOT NULL COMMENT '子节点id' ,
CHANGE COLUMN `deepth` `deepth` TINYINT(3) NOT NULL COMMENT '树的深度' ;

ALTER TABLE `grp_role`
CHANGE COLUMN `grp_id` `grp_id` INT(11) NOT NULL COMMENT '组id，外键' ,
CHANGE COLUMN `role_id` `role_id` INT(11) NOT NULL COMMENT '角色id，外键' ;

ALTER TABLE `grp_tag`
CHANGE COLUMN `tag_id` `tag_id` INT(11) NOT NULL COMMENT '标签id,外键' ,
CHANGE COLUMN `grp_id` `grp_id` INT(11) NOT NULL COMMENT '组id,外键' ;

ALTER TABLE `permission`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `value` `value` VARCHAR(128) NULL DEFAULT NULL COMMENT '权限值' ,
CHANGE COLUMN `value_ext` `value_ext` VARCHAR(128) NULL DEFAULT NULL COMMENT '权限扩展值' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '权限描述' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '状态' ,
CHANGE COLUMN `perm_type_id` `perm_type_id` INT(11) NOT NULL COMMENT '权限类型，外键' ,
CHANGE COLUMN `domain_id` `domain_id` INT(11) NOT NULL COMMENT '域的id' ;

ALTER TABLE `perm_type`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键，数据库自动生成' ,
CHANGE COLUMN `type` `type` VARCHAR(32) NOT NULL COMMENT '权限类型' ;

ALTER TABLE `stakeholder`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键，数据库自动生成' ,
CHANGE COLUMN `name` `name` VARCHAR(64) NULL DEFAULT NULL COMMENT '名称' ,
CHANGE COLUMN `email` `email` VARCHAR(64) NULL DEFAULT NULL COMMENT '邮箱' ,
CHANGE COLUMN `phone` `phone` VARCHAR(64) NULL DEFAULT NULL COMMENT '电话号码' ,
CHANGE COLUMN `jobtitle` `jobtitle` VARCHAR(64) NULL DEFAULT NULL COMMENT '职位' ,
CHANGE COLUMN `domain_id` `domain_id` INT(11) NOT NULL COMMENT '域id，外键' ;

ALTER TABLE `role`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `name` `name` VARCHAR(64) NULL DEFAULT NULL COMMENT '角色名称' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '角色描述' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '状态' ,
CHANGE COLUMN `domain_id` `domain_id` INT(11) NOT NULL COMMENT '域的id,外键' ,
CHANGE COLUMN `role_code_id` `role_code_id` INT(11) NOT NULL COMMENT '角色code,外键' ;


ALTER TABLE `role_code`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `code` `code` VARCHAR(32) NOT NULL COMMENT '角色编码' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '描述' ;

ALTER TABLE `role_permission`
CHANGE COLUMN `role_id` `role_id` INT(11) NOT NULL COMMENT '角色id，外键' ,
CHANGE COLUMN `permission_id` `permission_id` INT(11) NOT NULL COMMENT '权限的id，外键' ;

ALTER TABLE `tag`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `code` `code` VARCHAR(64) NULL DEFAULT NULL COMMENT '标签的编码' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL COMMENT '标签的状态' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '标签描述' ,
CHANGE COLUMN `tag_type_id` `tag_type_id` INT(11) NOT NULL COMMENT '标签类型，外键' ,
CHANGE COLUMN `create_date` `create_date` DATETIME NULL DEFAULT NULL COMMENT '创建时间' ,
CHANGE COLUMN `last_update` `last_update` DATETIME NULL DEFAULT NULL COMMENT '最后更新时间' ;

ALTER TABLE `tag_type`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `code` `code` VARCHAR(32) NOT NULL COMMENT '标签类型编码' ,
CHANGE COLUMN `domain_id` `domain_id` INT(11) NOT NULL COMMENT '关联的域，外键' ;

ALTER TABLE `user`
CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `name` `name` VARCHAR(64) NULL DEFAULT NULL COMMENT '用户姓名' ,
CHANGE COLUMN `email` `email` VARCHAR(64) NOT NULL COMMENT '用户邮箱' ,
CHANGE COLUMN `phone` `phone` VARCHAR(64) NULL DEFAULT NULL COMMENT '电话号码' ,
CHANGE COLUMN `password` `password` VARCHAR(64) NOT NULL COMMENT '密码' ,
CHANGE COLUMN `last_login_time` `last_login_time` DATETIME NULL DEFAULT NULL COMMENT '最后一次登录的时间' ,
CHANGE COLUMN `last_login_ip` `last_login_ip` VARCHAR(128) NULL DEFAULT NULL COMMENT '最后一次登录然ip' ,
CHANGE COLUMN `fail_count` `fail_count` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '失败的次数' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '状态' ,
CHANGE COLUMN `create_date` `create_date` DATETIME NULL DEFAULT NULL COMMENT '创建时间 ' ,
CHANGE COLUMN `last_update` `last_update` DATETIME NULL DEFAULT NULL COMMENT '最后更新时间' ,
CHANGE COLUMN `password_date` `password_date` DATETIME NULL DEFAULT NULL COMMENT '更新密码的时间' ;

ALTER TABLE `user_grp`
CHANGE COLUMN `user_id` `user_id` BIGINT(20) NOT NULL COMMENT '用户id，外键' ,
CHANGE COLUMN `grp_id` `grp_id` INT(11) NOT NULL COMMENT '对应组的id，外键' ,
CHANGE COLUMN `type` `type` TINYINT(3) NOT NULL COMMENT '类型，（0表示普通会员，1表示owner）' ;

ALTER TABLE `user_role`
CHANGE COLUMN `user_id` `user_id` BIGINT(20) NOT NULL COMMENT '用户id,外键' ,
CHANGE COLUMN `role_id` `role_id` INT(11) NOT NULL COMMENT '角色id，外键' ;

ALTER TABLE `audit`
CHANGE COLUMN `user_id` `user_id` BIGINT(20) NULL DEFAULT NULL COMMENT '用户id' ,
CHANGE COLUMN `req_date` `req_date` DATETIME NULL DEFAULT NULL COMMENT '访问时间' ,
CHANGE COLUMN `domain_id` `domain_id` INT(11) NULL DEFAULT NULL COMMENT '域id' ,
CHANGE COLUMN `req_ip` `req_ip` VARCHAR(128) NULL DEFAULT NULL COMMENT '访问者ip' ,
CHANGE COLUMN `req_url` `req_url` VARCHAR(64) NULL DEFAULT NULL COMMENT '访问的url' ,
CHANGE COLUMN `req_seq` `req_seq` BIGINT(20) NULL DEFAULT NULL COMMENT '访问的序号' ,
CHANGE COLUMN `req_class` `req_class` VARCHAR(64) NULL DEFAULT NULL COMMENT '访问对应的类' ,
CHANGE COLUMN `req_method` `req_method` VARCHAR(64) NULL DEFAULT NULL COMMENT '访问对应的方法' ,
CHANGE COLUMN `req_success` `req_success` TINYINT(3) NULL DEFAULT NULL COMMENT '请求是否成功' ,
CHANGE COLUMN `req_exp` `req_exp` VARCHAR(3072) NULL DEFAULT NULL COMMENT '请求产生的异常信息' ,
CHANGE COLUMN `req_elapse` `req_elapse` BIGINT(20) NULL DEFAULT NULL COMMENT '处理请求所用的时间（ms）' ,
CHANGE COLUMN `req_param` `req_param` VARCHAR(256) NULL DEFAULT NULL COMMENT '请求的参数' ,
CHANGE COLUMN `req_result` `req_result` VARCHAR(1024) NULL DEFAULT NULL COMMENT '请求的返回结果' ;


-- -----------------------------------------------------
-- Table `tenancy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tenancy` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT comment '租户主键id',
   `code` VARCHAR(30) NOT NULL comment '每一个租户的唯一标识code',
  `name` VARCHAR(64) NULL comment '租户名称，比如点融网',
  `contact_name` VARCHAR(30) NULL comment '租户联系人姓名',
  `phone` VARCHAR(30) NULL comment '租户联系电话',
  `description` VARCHAR(200) NULL comment '租户描述',
  `status` TINYINT(3) NOT NULL comment '状态，0:启用,1:禁用' DEFAULT 0,
  `create_date` DATETIME NULL comment '租户创建时间',
  `last_update` DATETIME NULL comment '最新更新时间',
  	PRIMARY KEY (`id`)
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tenancy` (`code`, `name`, `contact_name`, `phone`,`description`, `create_date`, `last_update` )
VALUES('DIANRONG-WEBSITE' , '点融网', '点融网', '400-921-9218', '点融网', now(), now());

-- update table structure
alter table cfg add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after cfg_type_id ;
alter table cfg add index cfg_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table domain add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after status ;
alter table domain add index domain_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table grp add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after status ;
alter table grp add index grp_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table permission add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after domain_id ;
alter table permission add index permission_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table role add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after role_code_id ;
alter table role add index role_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table stakeholder add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after domain_id ;
alter table stakeholder add index stakeholder_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table tag add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after tag_type_id ;
alter table tag add index tag_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table tag_type add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after domain_id;
alter table tag_type add index tag_type_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table `user` add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after status ;
alter table `user` add index user_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table user_extend add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after description ;
alter table user_extend add index user_extend_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table user_extend_val add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after status ;
alter table user_extend_val add index user_extend_val_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

-- init data
set sql_safe_updates = 0;

update cfg INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET cfg.tenancy_id = tenancy.id;

update domain INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET domain.tenancy_id = tenancy.id;

update grp INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET grp.tenancy_id = tenancy.id;

update permission INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET permission.tenancy_id = tenancy.id;

update role INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET role.tenancy_id = tenancy.id;

update stakeholder INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET stakeholder.tenancy_id = tenancy.id;

update tag INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET tag.tenancy_id = tenancy.id;

update tag_type INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET tag_type.tenancy_id = tenancy.id;

update `user` INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET `user`.tenancy_id = tenancy.id;

update user_extend INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET user_extend.tenancy_id = tenancy.id;

update user_extend_val INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET user_extend_val.tenancy_id = tenancy.id;

-- add foreign key

alter table cfg add constraint fk_cfg_tenancy foreign key(tenancy_id) references tenancy(id);

alter table domain add constraint fk_domain_tenancy foreign key(tenancy_id) references tenancy(id);

alter table grp add constraint fk_grp_tenancy foreign key(tenancy_id) references tenancy(id);

alter table permission add constraint fk_permission_tenancy foreign key(tenancy_id) references tenancy(id);

alter table role add constraint fk_role_tenancy foreign key(tenancy_id) references tenancy(id);

alter table stakeholder add constraint fk_stakeholder_tenancy foreign key(tenancy_id) references tenancy(id);

alter table tag add constraint fk_tag_tenancy foreign key(tenancy_id) references tenancy(id);

alter table tag_type add constraint fk_tag_type_tenancy foreign key(tenancy_id) references tenancy(id);

alter table `user` add constraint fk_user_tenancy foreign key(tenancy_id) references tenancy(id);

alter table user_extend add constraint fk_user_extend_tenancy foreign key(tenancy_id) references tenancy(id);

alter table user_extend_val add constraint fk_user_extend_val_tenancy foreign key(tenancy_id) references tenancy(id);

-- update index
alter table user drop index `user_email_idx`;
alter table user add index  `user_email_idx` (`email`,`tenancy_id`) comment 'user index, email + tenancy_id';

alter table user drop index `user_name_idx`;
alter table user add index  `user_name_idx` (`name`,`tenancy_id`) comment 'user index, name + tenancy_id';

alter table user drop index `user_phone_idx`;
alter table user add index  `user_phone_idx` (`phone`,`tenancy_id`) comment 'user index, phone + tenancy_id';

alter table grp drop index `group_name_idx`;
alter table grp add index  `group_name_idx` (`name`,`tenancy_id`) comment 'grp index, name + tenancy_id';

alter table cfg drop index `cfg_key_UNIQUE`;
alter table cfg add unique index  `cfg_key_UNIQUE` (`cfg_key`,`tenancy_id`) comment 'cfg index, cfg_key + tenancy_id';

alter table tag_type drop index `code_domainid_UNIQUE`;
alter table tag_type add unique index `code_domainid_UNIQUE` (`code`,`domain_id`, `tenancy_id`) comment 'tag_type index: code + domain_id + tenancy_id';

-- for audit
alter table audit add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after req_result ;
alter table audit add index audit_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

#update audit INNER JOIN tenancy ON tenancy.code='DIANRONG-WEBSITE'  SET audit.tenancy_id = tenancy.id;

-- 循环更新audit表的tenancy_id 字段
DROP procedure IF EXISTS `multi_tenancy_update_audit`;
DELIMITER $$
CREATE PROCEDURE `multi_tenancy_update_audit`()
BEGIN
declare  audit_top_id int(10);
declare while_index int(10) default 0;
declare while_times int(10);
declare update_min_id int(10);
declare update_max_id int(10);
declare dest_tenancy_id int(10);
declare update_per_num int(10) default 500;

set sql_safe_updates = 0;
select max(id) into audit_top_id from audit;
select max(id) into dest_tenancy_id from tenancy where code='DIANRONG-WEBSITE' ;
set while_times = (audit_top_id  DIV update_per_num) + 1;

-- drop index
IF EXISTS (SELECT * FROM information_schema.statistics WHERE table_name = 'audit' AND index_name = 'uniauth_audit_userid_idx' AND table_schema=database()) THEN
alter table audit drop index `uniauth_audit_userid_idx`;
END IF;
IF EXISTS (SELECT * FROM information_schema.statistics WHERE table_name = 'audit' AND index_name = 'uniauth_audit_action_date' AND table_schema=database()) THEN
alter table audit drop index`uniauth_audit_action_date`;
END IF;

 while while_index<while_times do
    SET update_min_id = while_index * update_per_num;
    SET update_max_id = update_min_id + update_per_num;
    update audit SET tenancy_id = dest_tenancy_id  WHERE id >= update_min_id and id < update_max_id;
    set while_index=while_index+1;
end while;

-- add index
alter table audit add index  `uniauth_audit_userid_idx` (`user_id`,`tenancy_id`) comment 'audit index, user_id + tenancy_id';
alter table audit add index  `uniauth_audit_action_date` (`req_date`,`tenancy_id`) comment 'audit index, req_date + tenancy_id';
END$$
DELIMITER ;

-- 耗时(幂等)
call multi_tenancy_update_audit();
-- 删除没用的存储过程
DROP procedure IF EXISTS `multi_tenancy_update_audit`;

alter table audit add constraint fk_audit_tenancy foreign key(tenancy_id) references tenancy(id);

-- -----------------------------------------------------
-- Table `user_pwd_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_pwd_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT comment '主键id',
  `user_id` BIGINT(20) NOT NULL comment '用户id',
  `password` VARCHAR(64) NOT NULL comment '用户密码',
  `password_salt` VARCHAR(64) NOT NULL comment '用户密码钥',
  `create_date` DATETIME NOT NULL comment '密码创建时间',
  `tenancy_id` BIGINT(20) NOT NULL comment '租户id',
  	PRIMARY KEY (`id`) comment '主键' ,
  	INDEX  user_pwd_log_userid(`user_id`) comment '用户id索引',
  	INDEX  user_pwd_log_create_date(`create_date`) comment '密码创建时间索引',
  	FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  	FOREIGN KEY (`tenancy_id`) REFERENCES `tenancy` (`id`)
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;

INSERT INTO perm_type(`type`) SELECT 'REGULAR_PATTERN' FROM DUAL WHERE NOT EXISTS(
	SELECT 1 FROM perm_type WHERE `type` = 'REGULAR_PATTERN'
);

-- delete table foreign key constraint tenancy_id
alter table audit drop foreign key fk_audit_tenancy;
alter table domain drop foreign key fk_domain_tenancy;
alter table cfg drop foreign key  fk_cfg_tenancy;
alter table stakeholder drop foreign key  fk_stakeholder_tenancy;

-- update dianrong tenancyCode from DIANRONG-WEBSITE to DIANRONG
set sql_safe_updates = 0;
update tenancy set code = 'DIANRONG' where code = 'DIANRONG-WEBSITE';
-- update none tenancy related info, set tenancyId = -1(none tenancy related id)
update domain set tenancy_id = -1;
update cfg set tenancy_id = -1;

-- -----------------------------------------------------
-- Table `api_caller_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_caller_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT comment '主键id',
  `domain_id` INT(11) NOT NULL comment '对应的域id',
  `password` VARCHAR(64) NOT NULL comment '密码',
  `status` TINYINT(3) NOT NULL comment '状态:0:启用,1:禁用' DEFAULT 0,
  `comment` VARCHAR(120) comment 'comment',
  `create_date` DATETIME  comment '创建时间',
  `last_update` DATETIME  comment '最近更新时间',
  	PRIMARY KEY (`id`) comment '主键' ,
  	FOREIGN KEY (`domain_id`) REFERENCES `domain` (`id`)
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;

  -- -----------------------------------------------------
-- Table `api_permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_permission` (
  `id` INT(11) NOT NULL AUTO_INCREMENT comment '主键id',
  `uri` VARCHAR(80) NOT NULL comment '权限对应的请求uri',
  `status` TINYINT(3) NOT NULL comment '状态:0:启用,1:禁用' DEFAULT 0,
  `method` VARCHAR(10) NOT NULL comment '包括:GET,POST,ALL三种',
  `type` TINYINT(3) NOT NULL comment '权限类型:0:私有权限类型(需要主动分配),1:共有权限类型(默认每个域都有)' DEFAULT 0,
  `comment` VARCHAR(120) comment 'comment',
  `create_date` DATETIME  comment '创建时间',
  `last_update` DATETIME  comment '最近更新时间',
  	PRIMARY KEY (`id`) comment '主键'
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;

  -- -----------------------------------------------------
-- Table `caller_permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `caller_permission` (
  `api_caller_id` INT(11) NOT NULL comment 'api访问域的id',
  `api_permission_id` INT(11) NOT NULL comment '访问权限id',
  	UNIQUE KEY `unique_caller_permission` (`api_caller_id`,`api_permission_id`) ,
  	FOREIGN KEY (`api_caller_id`) REFERENCES `api_caller_info` (`id`),
  	FOREIGN KEY (`api_permission_id`) REFERENCES `api_permission` (`id`)
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;

-- audit table update
alter table audit add request_domain_code varchar(64) default '' comment '请求访问者的域名code';

-- 插入数据
DROP procedure IF EXISTS `proc_insert_techops_ctl`;

-- 添加techops的api访问控制
DELIMITER $$
CREATE PROCEDURE `proc_insert_techops_ctl`()
BEGIN
declare techops_domain_id int(11);
declare techops_perm_id int(11);
declare techops_caller_id int(11);

select id into techops_domain_id from domain where code = 'techops' and status = 0;
-- insert permission
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^.*',0,'ALL',0,'uniauth内部专用权限',now(),now());
-- insert techops api_caller_info
insert into api_caller_info(domain_id,password,status,comment,create_date,last_update)
values(techops_domain_id, '=610{8(lxt42nslg*k9q9|5z!:3@~9e72#|rw1*6arz5zad@7+muh!e-w792', 0, 'techops和cas对应的caller', now(), now());

select id into techops_perm_id from api_permission limit 0, 1;
select id into techops_caller_id from api_caller_info limit 0, 1;

-- insert permission和caller的映射
insert into caller_permission(api_caller_id, api_permission_id) values(techops_caller_id, techops_perm_id);
END$$
DELIMITER ;

call proc_insert_techops_ctl();

DROP procedure IF EXISTS `proc_insert_techops_ctl`;


-- public permissions
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs$',0,'ALL',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^.*',0,'GET',1,'',now(),now());

-- audit
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/audit/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/audit/delete$',0,'POST',1,'',now(),now());

-- config
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/config/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/config/cfg-types$',0,'POST',1,'',now(),now());

-- domain
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/domain/login/alldomains$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/domain/domaininfo$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/domain/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/domain/stakeholder/query$',0,'POST',1,'',now(),now());

-- group
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/group/tree$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/group/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/group/groupowners$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/group/domain/roles$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/group/checkowner$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/group/querytagswithchecked$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/group/userInGroupOrSub$',0,'POST',1,'',now(),now());

-- permission
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/permission/allpermtypecodes$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/permission/searchperms$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/permission/roles$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/permission/urlrolemappings$',0,'POST',1,'',now(),now());

-- role
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/role/allrolecodes$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/role/searchroles$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/role/domain/perms$',0,'POST',1,'',now(),now());

-- tag
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/tag/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/tag/tag-types$',0,'POST',1,'',now(),now());

-- tenancy
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/tenancy/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/tenancy/queryDefault$',0,'POST',1,'',now(),now());

-- userextend
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/userextend/searchuserextend$',0,'POST',1,'',now(),now());

-- userextendval
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/userextendval/searchbyuseridandstatus$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/userextendval/searchbyuseridandcode$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/userextendval/searchbyuseridentity$',0,'POST',1,'',now(),now());

-- user
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/searchusers$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/userswithrolechecked$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/userswithtagchecked$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/domain/roles$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/login$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/usedetailinfo$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/usedetailinfobyid$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/singleuser$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/userInfobytag$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/tagsWithUserChecked$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/search-user-by-roleid$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/search-user-by-tagid$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/query-user/group-and-role$',0,'POST',1,'',now(),now());

-- userextend
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/userextend/adduserextend$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/userextend/updateuserextend$',0,'POST',1,'',now(),now());

-- userextendval
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/userextendval/add$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/userextendval/delbyid$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/userextendval/updatebyid$',0,'POST',1,'',now(),now());

-- group
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/group/list-group-relate-to-user$',0,'POST',1,'',now(),now());

-- group
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/userextendval/search$',0,'POST',1,'',now(),now());

ALTER TABLE `audit`
ADD FULLTEXT INDEX `idx_audit_req_param_fulltext` (`req_param` ASC)  COMMENT '查询用户在uniauth产生的所有历史记录';

-- tenancy
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/tenancy/query-enable-tenancy-by-code$',0,'POST',1,'',now(),now());

-- user
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/vpn-login$',0,'POST',1,'',now(),now());

-- update user table's email colomn
ALTER TABLE user MODIFY `email` VARCHAR(64) comment '用户邮箱';

-- -----------------------------------------------------
-- Table `user_third_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_third_account` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` BIGINT(20) NOT NULL COMMENT '与user表的主键id一一映射',
  `third_account` VARCHAR(60) NOT NULL COMMENT '第三方账号',
  `type` VARCHAR(10) NOT NULL COMMENT '字符串的类型,比如IPA，OA等',
  `last_login_ip` DATETIME COMMENT '第三方账号通过Uniauth的最新登陆ip',
  `last_login_time` DATETIME COMMENT '第三方账号通过Uniauth的最新登陆时间',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `tenancy_id` BIGINT(20) NOT NULL COMMENT'租户id',
  PRIMARY KEY (`id`) COMMENT '主键',
  UNIQUE INDEX unique_index_user_third_account_1(`user_id`, `third_account`, `type`, `tenancy_id`),
  INDEX index_user_third_account_user_id_tenancy_id(`user_id`, `tenancy_id`) COMMENT '加快根据用户id查询速度',
  INDEX index_user_third_account_third_account_type_tenancy_id(`third_account`, `type`, `tenancy_id`) COMMENT '加快根据根据第三方账号和类型反查用户id',
  CONSTRAINT `fk_user_third_account_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_third_account_tenancy_id` FOREIGN KEY (`tenancy_id`) REFERENCES `tenancy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
  -- Add new permission type - THIRD_ACCOUNT
insert into perm_type(`type`)  values('THIRD_ACCOUNT');

-- Update table user_extend
ALTER TABLE user_extend MODIFY `description` VARCHAR(200) COMMENT '扩展属性描述';
ALTER TABLE user_extend ADD category VARCHAR(30) COMMENT '扩展属性类型' after code;
ALTER TABLE user_extend ADD subcategory VARCHAR(30) COMMENT '扩展属性子类型' after category;
ALTER TABLE user_extend ADD INDEX idx_user_extend_code_tenancy_id (`code`, `tenancy_id`) COMMENT '加快根据Code查询速度';

-- Rename table user_extend to attribute_extend
ALTER TABLE user_extend rename attribute_extend;

-- Update table user
ALTER TABLE user ADD staff_no VARCHAR(80) COMMENT '员工编号' after phone;
ALTER TABLE user ADD ldap_dn VARCHAR(100) COMMENT 'ldap账号' after staff_no;
ALTER TABLE user ADD user_guid VARCHAR(80) COMMENT '用户的guid' after ldap_dn;
ALTER TABLE user ADD INDEX idx_user_staff_no_tenancy_id (`staff_no`, `tenancy_id`) COMMENT '根据staff_no查询速度加快';
ALTER TABLE user ADD INDEX idx_user_ldap_dn_tenancy_id (`ldap_dn`, `tenancy_id`) COMMENT '根据ldap_dn查询速度加快';
ALTER TABLE user ADD INDEX idx_user_user_guid_tenancy_id (`user_guid`, `tenancy_id`) COMMENT '根据user_guid查询速度加快';

-- Update table user_extend_val
ALTER TABLE user_extend_val DROP COLUMN status;
ALTER TABLE user_extend_val change value_ `value` VARCHAR(200) COMMENT '扩展属性值';
ALTER TABLE user_extend_val ADD INDEX idx_user_extend_val_value_tenancy_id (`value`, `tenancy_id`) COMMENT '根据扩展属性值索引';
ALTER TABLE user_extend_val ADD create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';
ALTER TABLE user_extend_val ADD last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间';

-- -----------------------------------------------------
-- Table `user_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` BIGINT(20) NOT NULL COMMENT '与user表的主键id一一映射',
  `first_name` VARCHAR(45) COMMENT '名',
  `last_name` VARCHAR(45) COMMENT '姓',
  `display_name` VARCHAR(80) COMMENT '显示的名称',
  `nick_name` VARCHAR(80) COMMENT '别名',
  `identity_no` VARCHAR(20) COMMENT '身份证号码',
  `motto` VARCHAR(300) COMMENT '座右铭',
  `image` VARCHAR(200) COMMENT '头像资源所在的地址',
  `ssn` VARCHAR(40) COMMENT 'SSN号码',
  `weibo` VARCHAR(200) COMMENT '微博',
  `wechat_no` VARCHAR(60) COMMENT '微信号',
  `address` VARCHAR(200) COMMENT '联系地址',
  `birthday` Date COMMENT '生日',
  `gender` VARCHAR(10) COMMENT '性别',
  `position` VARCHAR(80) COMMENT '职位',
  `department` VARCHAR(80) COMMENT '所在部门',
  `title` VARCHAR(60) COMMENT '职称',
  `aid` BIGINT(20) COMMENT '关联的Actor表id',
  `last_position_modify_date` DATETIME COMMENT '上一次职位更新时间',
  `entry_date` DATETIME COMMENT '入职时间',
  `leave_date` DATETIME COMMENT '离职时间',
  `remark` VARCHAR(200) COMMENT '备注',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `tenancy_id` BIGINT(20) not null default -1 COMMENT'租户id',
  PRIMARY KEY (`id`) COMMENT '主键',
  CONSTRAINT `fk_user_detail_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_detail_tenancy_id` FOREIGN KEY (`tenancy_id`) REFERENCES `tenancy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
-- -----------------------------------------------------
-- Table `user_work_relationship`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_work_relationship` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` BIGINT(20) NOT NULL COMMENT '关联的用户id',
  `manager_id` BIGINT(20) COMMENT '经理的id',
  `supervisor_id` BIGINT(20) COMMENT '监管者的id',
  `type` tinyint(4) NOT NULL COMMENT '类型' default 0,
  `assignment_date` DATETIME COMMENT '分配时间',
  `hire_date` DATETIME COMMENT '雇佣时间',
  `business_unit_name` VARCHAR(60) COMMENT '所在业务部门名称',
  `department_name` VARCHAR(60) COMMENT '所在部门名称',
  `legal_entity_name` VARCHAR(60) COMMENT '法定实体名称',
  `work_phone` VARCHAR(60) COMMENT '工作电话',
  `work_location` VARCHAR(100) COMMENT '工作地点',
  `work_address` VARCHAR(100) COMMENT '工作地点',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `tenancy_id` BIGINT(20) not null default -1 COMMENT'租户id',
  PRIMARY KEY (`id`) COMMENT '主键' ,
  INDEX index_user_work_relationship_type_tenancy_id(`type`, `tenancy_id`) COMMENT '加快根据类型查询速度',
  CONSTRAINT `fk_user_work_relationship_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_work_relationship_manager_id` FOREIGN KEY (`manager_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_work_relationship_supervisor_id` FOREIGN KEY (`supervisor_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_work_relationship_tenancy_id` FOREIGN KEY (`tenancy_id`) REFERENCES `tenancy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
-- -----------------------------------------------------
-- Table `user_attribute_records`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_attribute_records` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` BIGINT(20) NOT NULL COMMENT '关联的用户id',
  `opt_type` VARCHAR(40) NOT NULL COMMENT '记录操作类型, 比如ADD,UPDATE,DELETE等',
  `opt_id` BIGINT(20) COMMENT '操作人的id',
  `opt_name` VARCHAR(64) COMMENT '操作人的姓名',
  `opt_date` DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `extend_id` BIGINT(20) NOT NULL COMMENT '关联的扩展属性id',
  `pre_val` VARCHAR(200) COMMENT '操作前的值',
  `cur_val` VARCHAR(200) COMMENT '操作后的值',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `tenancy_id` BIGINT(20) not null default -1 COMMENT'租户id',
  PRIMARY KEY (`id`) COMMENT '主键',
  INDEX idx_user_attribute_records_opt_type_tenancy_id(`opt_type`, `tenancy_id`) COMMENT '加快根据操作类型的查询速度',
  INDEX idx_user_attribute_records_opt_date_tenancy_id(`opt_date`, `tenancy_id`) COMMENT '加快根据操作时间的查询速度',
  INDEX idx_user_attribute_records_pre_val_tenancy_id(`pre_val`, `tenancy_id`) COMMENT '加快根据修改前的值的查询速度',
  INDEX idx_user_attribute_records_cur_val_tenancy_id(`cur_val`, `tenancy_id`) COMMENT '加快根据修改后的值的查询速度',
  CONSTRAINT `fk_user_attribute_records_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_attribute_records_tenancy_id` FOREIGN KEY (`tenancy_id`) REFERENCES `tenancy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_attribute_records_extend_id` FOREIGN KEY (`extend_id`) REFERENCES `attribute_extend` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
-- -----------------------------------------------------
-- Table `grp_extend_val`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `grp_extend_val` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `grp_id` INT(11) NOT NULL COMMENT '对应的组id',
  `extend_id` BIGINT(20) NOT NULL COMMENT '关联的扩展属性id',
  `value` VARCHAR(200) COMMENT '扩展属性值',
  `tenancy_id` BIGINT(20) NOT NULL COMMENT '租户id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`id`) COMMENT '主键',
  INDEX idx_grp_extend_val_value_tenancy_id(`value`, `tenancy_id`) COMMENT '加快根据扩展值查询速度',
  CONSTRAINT `fk_grp_extend_val_grp_id` FOREIGN KEY (`grp_id`) REFERENCES `grp` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_grp_extend_val_tenancy_id` FOREIGN KEY (`tenancy_id`) REFERENCES `tenancy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_grp_extend_val_extend_id` FOREIGN KEY (`extend_id`) REFERENCES `attribute_extend` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
-- -----------------------------------------------------
-- Table `grp_attribute_records`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `grp_attribute_records` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `grp_id` INT(11) NOT NULL COMMENT '关联的组id',
  `opt_type` VARCHAR(40) NOT NULL COMMENT '记录操作类型, 比如ADD,UPDATE,DELETE等',
  `opt_id` BIGINT(20) COMMENT '操作人的id',
  `opt_name` VARCHAR(64) COMMENT '操作人的姓名',
  `opt_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `extend_id` BIGINT(20) NOT NULL COMMENT '关联的扩展属性id',
  `pre_val` VARCHAR(200) COMMENT '操作前的值',
  `cur_val` VARCHAR(200) COMMENT '操作后的值',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `tenancy_id` BIGINT(20) not null default -1 COMMENT'租户id',
  PRIMARY KEY (`id`) COMMENT '主键',
  INDEX idx_grp_attribute_records_opt_type_tenancy_id(`opt_type`, `tenancy_id`) COMMENT '加快根据操作类型的查询速度',
  INDEX idx_grp_attribute_records_opt_date_tenancy_id(`opt_date`, `tenancy_id`) COMMENT '加快根据操作时间的查询速度',
  INDEX idx_grp_attribute_records_pre_val_tenancy_id(`pre_val`, `tenancy_id`) COMMENT '加快根据修改前的值的查询速度',
  INDEX idx_grp_attribute_records_cur_val_tenancy_id(`cur_val`, `tenancy_id`) COMMENT '加快根据修改后的值的查询速度',
  CONSTRAINT `fk_grp_attribute_records_grp_id` FOREIGN KEY (`grp_id`) REFERENCES `grp` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_grp_attribute_records_tenancy_id` FOREIGN KEY (`tenancy_id`) REFERENCES `tenancy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_grp_attribute_records_extend_id` FOREIGN KEY (`extend_id`) REFERENCES `attribute_extend` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
  -- init administrator account
UPDATE `user` set password_date = null WHERE email = 'first.admin@test.com';

-- metrics check
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/metrics/.*$',0,'ALL',1,'',now(),now());
