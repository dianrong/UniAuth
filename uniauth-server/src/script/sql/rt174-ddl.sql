-- update table user_extend
alter table user_extend modify `description` varchar(200) comment '扩展属性描述';
alter table user_extend add category varchar(30) comment '扩展属性类型' after code;
alter table user_extend add subcategory varchar(30) comment '扩展属性子类型' after code;
-- rename table user_extend to attribute_extend
alter table user_extend rename attribute_extend;

-- update table user
alter table user add staff_no varchar(80) comment '员工编号' after phone;
alter table user add ldap_dn varchar(100) comment 'ldap账号' after phone;
alter table user add user_guid varchar(80) comment '用户的guid' after phone;

-- update table user_extend_val
alter table user_extend_val drop column status;
alter table user_extend_val change value_  `value` varchar(200) not null default '' comment '扩展属性值';
alter table user_extend_val add create_date datetime not null default now() comment '记录创建时间';
alter table user_extend_val add last_update datetime not null default now() comment '最近更新时间';

-- -----------------------------------------------------
-- Table `user_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT comment '主键id',
  `user_id` BIGINT(20) NOT NULL comment '与user表的主键id一一映射',
  `first_name` varchar(45) comment '名',
  `last_name` varchar(45) comment '姓',
  `display_name` varchar(80) comment '显示的名称',
  `nick_name` varchar(80) comment '别名',
  `identity_no` varchar(20) comment '身份证号码',
  `motto` varchar(300) comment '座右铭',
  `image` varchar(200) comment '头像资源所在的地址',
  `ssn` varchar(40) comment 'SSN号码',
  `weibo` varchar(200) comment '微博',
  `wechat_no` varchar(60) comment '微信号',
  `address` varchar(200) comment '联系地址',
  `birthday` Date comment '生日',
  `gender` varchar(10) comment '性别',
  `position` varchar(80) comment '职位',
  `department` varchar(80) comment '所在部门',
  `title` varchar(60) comment '职称',
  `aid` BIGINT(20) comment '关联的Actor表id',
  `last_position_modify_date` DATETIME comment '上一次职位更新时间',
  `entry_date` DATETIME comment '入职时间',
  `leave_date` DATETIME comment '离职时间',
  `remark` varchar(200) comment '备注',
  `create_date` DATETIME  not null default now() comment '创建时间',
  `last_update` DATETIME  not null default now() comment '最近更新时间',
   `tenancy_id` BIGINT(20) not null default -1 comment'租户id',
    PRIMARY KEY (`id`) comment '主键' ,
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
-- -----------------------------------------------------
-- Table `user_work_relationship`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_work_relationship` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT comment '主键id',
  `user_id` BIGINT(20) NOT NULL comment '关联的用户id',
  `manager_id` BIGINT(20) comment '经理的id',
  `supervisor_id` BIGINT(20) comment '监管者的id',
  `type` tinyint(4) not null comment '类型' default 0,
  `assignment_date` DATETIME comment '分配时间',
  `hire_date` DATETIME comment '雇佣时间',
  `business_unit_name` varchar(60) comment '所在业务部门名称',
  `department_name` varchar(60) comment '所在部门名称',
  `legal_entity_name` varchar(60) comment '法定实体名称',
  `work_phone` varchar(60) comment '工作电话',
  `work_location` varchar(100) comment '工作低点',
  `work_address` varchar(100) comment '工作低点',
  `create_date` DATETIME  not null default now() comment '创建时间',
  `last_update` DATETIME  not null default now() comment '最近更新时间',
   `tenancy_id` BIGINT(20) not null default -1 comment'租户id',
    PRIMARY KEY (`id`) comment '主键' ,
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
      -- -----------------------------------------------------
-- Table `user_attribute_records`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_attribute_records` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT comment '主键id',
  `user_id` BIGINT(20) NOT NULL comment '关联的用户id',
  `opt_type` varchar(40) NOT NULL comment '记录操作类型, 比如ADD,UPDATE,DELETE等',
  `opt_id` BIGINT(20) comment '操作人的id',
  `opt_name` varchar(64) comment '操作人的姓名',
  `opt_date` DATETIME  not null default now() comment '操作时间',
  `extend_id` INT(11) NOT NULL comment '关联的扩展属性id',
  `pre_val` varchar(200) comment '操作前的值',
  `cur_val` varchar(200) comment '操作后的值',
  `create_date` DATETIME  not null default now() comment '创建时间',
  `last_update` DATETIME  not null default now() comment '最近更新时间',
   `tenancy_id` BIGINT(20) not null default -1 comment'租户id',
    PRIMARY KEY (`id`) comment '主键' ,
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
-- -----------------------------------------------------
-- Table `grp_extend_val`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `grp_extend_val` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT comment '主键id',
  `grp_id` INT(11) NOT NULL comment '对应的组id',
  `extend_id` INT(11) NOT NULL comment '关联的扩展属性id',
  `value` varchar(20) NOT NULL comment '扩展属性值' default '',
  `tenancy_id` BIGINT(20) NOT NULL comment '租户id',
  `create_date` DATETIME  comment '创建时间',
  `last_update` DATETIME  comment '最近更新时间',
    PRIMARY KEY (`id`) comment '主键' ,
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
-- -----------------------------------------------------
-- Table `grp_attribute_records`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `grp_attribute_records` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT comment '主键id',
  `grp_id` INT(11) NOT NULL comment '关联的组id',
  `opt_type` varchar(40) NOT NULL comment '记录操作类型, 比如ADD,UPDATE,DELETE等',
  `opt_id` BIGINT(20) comment '操作人的id',
  `opt_name` varchar(64) comment '操作人的姓名',
  `opt_date` DATETIME  not null default now() comment '操作时间',
  `extend_id` INT(11) NOT NULL comment '关联的扩展属性id',
  `pre_val` varchar(200) comment '操作前的值',
  `cur_val` varchar(200) comment '操作后的值',
  `create_date` DATETIME  not null default now() comment '创建时间',
  `last_update` DATETIME  not null default now() comment '最近更新时间',
   `tenancy_id` BIGINT(20) not null default -1 comment'租户id',
    PRIMARY KEY (`id`) comment '主键' ,
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;
  
  -- -------------------------------------------------------------------
  -- Add table index, forein key
  -- -------------------------------------------------------------------
  -- Table user
  alter table `user` add constraint 外键名 foreign key(需加外键表的字段名) referencnes 关联表名(关联字段名);
注意：外键名不能重复