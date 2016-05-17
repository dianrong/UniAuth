
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
