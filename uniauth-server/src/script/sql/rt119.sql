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