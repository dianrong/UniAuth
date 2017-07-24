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
  )ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT '三方账号与Uniauth账号的关联关系';