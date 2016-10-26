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