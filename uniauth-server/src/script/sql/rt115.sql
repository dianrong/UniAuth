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
values('200000001', 'guige', 'first.admin@dianrong.com', '13011111111', 'GRodddDAZjK2tGZ6kT7ImP8ILwU=', 'I9JTzG2zzBAW3Q5NvP8lRg==', now(), '192.168.18.5', '0', '0', now(), now(), now());

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


