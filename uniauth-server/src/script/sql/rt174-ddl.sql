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
  `create_date` DATETIME  comment '创建时间',
  `last_update` DATETIME  comment '最近更新时间',
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