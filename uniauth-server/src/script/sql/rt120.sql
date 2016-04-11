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