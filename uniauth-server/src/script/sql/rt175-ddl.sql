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
  )ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT '用户详细信息';
  
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
  INDEX idx_user_work_relationship_type_tenancy_id(`type`, `tenancy_id`) COMMENT '加快根据类型查询速度',
  CONSTRAINT `fk_user_work_relationship_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_work_relationship_manager_id` FOREIGN KEY (`manager_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_work_relationship_supervisor_id` FOREIGN KEY (`supervisor_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_work_relationship_tenancy_id` FOREIGN KEY (`tenancy_id`) REFERENCES `tenancy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  )ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT '用户组织关系表';
  
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
  )ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT '用户属性操作记录';
  
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
  )ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT '组的扩展属性表';
  
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
  )ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT '组的扩展属性操作记录';