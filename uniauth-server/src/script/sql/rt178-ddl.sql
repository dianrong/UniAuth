-- -----------------------------------------------------
-- Table `profile_definition`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `profile_definition` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` VARCHAR(40) NOT NULL COMMENT 'Profile的编码', 
  `name` VARCHAR(30) COMMENT 'Profile的名称',
  `description` VARCHAR(120) COMMENT '描述',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `tenancy_id` BIGINT(20) NOT NULL COMMENT'租户id',
  PRIMARY KEY (`id`) COMMENT '主键',
  UNIQUE INDEX uq_idx_profile_definition_code(`code`, `tenancy_id`),
  CONSTRAINT `fk_profile_definition_tenancy_id` FOREIGN KEY (`tenancy_id`) REFERENCES `tenancy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT 'Profile信息';
  
-- -----------------------------------------------------
-- Table `profile_definition_path`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `profile_definition_path` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `ancestor` BIGINT(20) NOT NULL COMMENT '父Profile id', 
  `descendant` BIGINT(20) NOT NULL COMMENT '子Profile id',
  `deepth` TINYINT(3) NOT NULL COMMENT '关联层级数',
  `num` TINYINT(3) NOT NULL COMMENT '同一种层级关联关系的数量',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`id`) COMMENT '主键',
  UNIQUE INDEX uq_idx_profile_definition_path(`ancestor`, `descendant`, `deepth`),
  CONSTRAINT `fk_profile_definition_path_ancestor` FOREIGN KEY (`ancestor`) REFERENCES `profile_definition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_profile_definition_path_descendant` FOREIGN KEY (`descendant`) REFERENCES `profile_definition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT 'Profile之间的关联关系';
  
  -- -----------------------------------------------------
-- Table `profile_definition_attribute`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `profile_definition_attribute` (
  `profile_id` BIGINT(20) NOT NULL COMMENT 'Profile id', 
  `attribute_id` BIGINT(20) NOT NULL COMMENT '扩展属性id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  UNIQUE INDEX uq_idx_profile_definition_attribute(`profile_id`, `attribute_id`),
  CONSTRAINT `fk_profile_definition_attribute_profile_id` FOREIGN KEY (`profile_id`) REFERENCES `profile_definition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_profile_definition_attribute_attribute_id` FOREIGN KEY (`attribute_id`) REFERENCES `attribute_extend` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT 'Profile与扩展属性之间的关联关系';
  