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

