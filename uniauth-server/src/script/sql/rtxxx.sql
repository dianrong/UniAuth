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
-- Table `group_code`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `group_code` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'SALES, RISK, EMPLOYEE, ADMIN',
  `code` VARCHAR(32) NOT NULL COMMENT 'Employee, SALES, OPERATION, RISK, ADMINISTRATOR,',
  `description` VARCHAR(512) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `code_UNIQUE` ON `group_code` (`code` ASC);


-- -----------------------------------------------------
-- Table `group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `group` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NULL,
  `description` VARCHAR(512) NULL,
  `status` TINYINT(3) NOT NULL DEFAULT 0,
  `create_date` DATETIME NULL,
  `last_update` DATETIME NULL,
  `group_code_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_group_group_code1`
    FOREIGN KEY (`group_code_id`)
    REFERENCES `group_code` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_group_group_code1_idx` ON `group` (`group_code_id` ASC);

CREATE INDEX `group_name_idx` ON `group` (`name` ASC);


-- -----------------------------------------------------
-- Table `group_path`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `group_path` (
  `ancestor` INT NOT NULL,
  `descendant` INT NOT NULL,
  `deepth` TINYINT(3) NOT NULL,
  PRIMARY KEY (`ancestor`, `descendant`),
  CONSTRAINT `fk_group_path_group1`
    FOREIGN KEY (`ancestor`)
    REFERENCES `group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_group_path_group2`
    FOREIGN KEY (`descendant`)
    REFERENCES `group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_group_path_group1_idx` ON `group_path` (`ancestor` ASC);

CREATE INDEX `fk_group_path_group2_idx` ON `group_path` (`descendant` ASC);


-- -----------------------------------------------------
-- Table `user_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_group` (
  `group_id` INT NOT NULL,
  `user_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`group_id`, `user_id`),
  CONSTRAINT `fk_user_group_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_user_group_user1_idx` ON `user_group` (`user_id` ASC);

CREATE INDEX `fk_user_group_group1_idx` ON `user_group` (`group_id` ASC);


-- -----------------------------------------------------
-- Table `domain`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `domain` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `display_name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(512) NULL,
  `create_date` DATETIME NULL,
  `last_update` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `name_UNIQUE` ON `domain` (`name` ASC);

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

CREATE INDEX `domain_rolename_unique` ON `role` (`domain_id` ASC, `name` ASC);


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
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'URIPATTERN, PRIVILEGE',
  `type` VARCHAR(32) NOT NULL COMMENT 'URI_PATTERN, PRIVILEGE',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `type_UNIQUE` ON `perm_type` (`type` ASC);


-- -----------------------------------------------------
-- Table `permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `permission` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(128) NULL,
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
-- Table `group_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `group_role` (
  `group_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`group_id`, `role_id`),
  CONSTRAINT `fk_group_domain_group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_group_domain_role_acl_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_group_domain_group1_idx` ON `group_role` (`group_id` ASC);

CREATE INDEX `fk_group_domain_role_acl_role1_idx` ON `group_role` (`role_id` ASC);


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
insert into `role_code`(`code`, `description`) values ('ADMIN', '管理员');
insert into `role_code`(`code`, `description`) values ('USER', '普通用户');
insert into `role_code`(`code`, `description`) values ('VIEW', 'Guest用户');

insert into `group_code`(`code`, `description`) values ('SALES', '销售组用的code, 给子系统用于标识组别');
insert into `group_code`(`code`, `description`) values ('RISK', '风控组用的code, 给子系统用于标识组别');
insert into `group_code`(`code`, `description`) values ('EMPLOYEE', '员工组用的code, 给子系统用于标识组别');
insert into `group_code`(`code`, `description`) values ('ADMIN', '管理员组用的code, 给子系统用于标识组别');