ALTER TABLE `domain`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键，数据库自动生成' ,
CHANGE COLUMN `code` `code` VARCHAR(64) NOT NULL COMMENT '域编码' ,
CHANGE COLUMN `display_name` `display_name` VARCHAR(128) NOT NULL COMMENT '显示名称' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '域描述' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '状态' ,
CHANGE COLUMN `create_date` `create_date` DATETIME NULL DEFAULT NULL COMMENT '创建时间' ,
CHANGE COLUMN `last_update` `last_update` DATETIME NULL DEFAULT NULL COMMENT '最后更新时间' ;

ALTER TABLE `grp`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '组的id，唯一键' ,
CHANGE COLUMN `name` `name` VARCHAR(128) NULL DEFAULT NULL COMMENT '组名称' ,
CHANGE COLUMN `code` `code` VARCHAR(128) NOT NULL COMMENT '组编码' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '组描述' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '状态' ,
CHANGE COLUMN `create_date` `create_date` DATETIME NULL DEFAULT NULL COMMENT '创建时间' ,
CHANGE COLUMN `last_update` `last_update` DATETIME NULL DEFAULT NULL COMMENT '最后修改时间' ;

ALTER TABLE `grp_role`
CHANGE COLUMN `grp_id` `grp_id` INT(11) NOT NULL COMMENT '组的id' ,
CHANGE COLUMN `role_id` `role_id` INT(11) NOT NULL COMMENT '角色的id' ;

ALTER TABLE `grp_tag`
CHANGE COLUMN `tag_id` `tag_id` INT(11) NOT NULL COMMENT '标签的id' ,
CHANGE COLUMN `grp_id` `grp_id` INT(11) NOT NULL COMMENT '组的id' ;

ALTER TABLE `grp_path`
CHANGE COLUMN `ancestor` `ancestor` INT(11) NOT NULL COMMENT '父节点id' ,
CHANGE COLUMN `descendant` `descendant` INT(11) NOT NULL COMMENT '子节点id' ,
CHANGE COLUMN `deepth` `deepth` TINYINT(3) NOT NULL COMMENT '树的深度' ;

ALTER TABLE `grp_role`
CHANGE COLUMN `grp_id` `grp_id` INT(11) NOT NULL COMMENT '组id，外键' ,
CHANGE COLUMN `role_id` `role_id` INT(11) NOT NULL COMMENT '角色id，外键' ;

ALTER TABLE `grp_tag`
CHANGE COLUMN `tag_id` `tag_id` INT(11) NOT NULL COMMENT '标签id,外键' ,
CHANGE COLUMN `grp_id` `grp_id` INT(11) NOT NULL COMMENT '组id,外键' ;

ALTER TABLE `permission`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `value` `value` VARCHAR(128) NULL DEFAULT NULL COMMENT '权限值' ,
CHANGE COLUMN `value_ext` `value_ext` VARCHAR(128) NULL DEFAULT NULL COMMENT '权限扩展值' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '权限描述' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '状态' ,
CHANGE COLUMN `perm_type_id` `perm_type_id` INT(11) NOT NULL COMMENT '权限类型，外键' ,
CHANGE COLUMN `domain_id` `domain_id` INT(11) NOT NULL COMMENT '域的id' ;

ALTER TABLE `perm_type`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键，数据库自动生成' ,
CHANGE COLUMN `type` `type` VARCHAR(32) NOT NULL COMMENT '权限类型' ;

ALTER TABLE `stakeholder`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键，数据库自动生成' ,
CHANGE COLUMN `name` `name` VARCHAR(64) NULL DEFAULT NULL COMMENT '名称' ,
CHANGE COLUMN `email` `email` VARCHAR(64) NULL DEFAULT NULL COMMENT '邮箱' ,
CHANGE COLUMN `phone` `phone` VARCHAR(64) NULL DEFAULT NULL COMMENT '电话号码' ,
CHANGE COLUMN `jobtitle` `jobtitle` VARCHAR(64) NULL DEFAULT NULL COMMENT '职位' ,
CHANGE COLUMN `domain_id` `domain_id` INT(11) NOT NULL COMMENT '域id，外键' ;

ALTER TABLE `role`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `name` `name` VARCHAR(64) NULL DEFAULT NULL COMMENT '角色名称' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '角色描述' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '状态' ,
CHANGE COLUMN `domain_id` `domain_id` INT(11) NOT NULL COMMENT '域的id,外键' ,
CHANGE COLUMN `role_code_id` `role_code_id` INT(11) NOT NULL COMMENT '角色code,外键' ;


ALTER TABLE `role_code`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `code` `code` VARCHAR(32) NOT NULL COMMENT '角色编码' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '描述' ;

ALTER TABLE `role_permission`
CHANGE COLUMN `role_id` `role_id` INT(11) NOT NULL COMMENT '角色id，外键' ,
CHANGE COLUMN `permission_id` `permission_id` INT(11) NOT NULL COMMENT '权限的id，外键' ;

ALTER TABLE `tag`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `code` `code` VARCHAR(64) NULL DEFAULT NULL COMMENT '标签的编码' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL COMMENT '标签的状态' ,
CHANGE COLUMN `description` `description` VARCHAR(512) NULL DEFAULT NULL COMMENT '标签描述' ,
CHANGE COLUMN `tag_type_id` `tag_type_id` INT(11) NOT NULL COMMENT '标签类型，外键' ,
CHANGE COLUMN `create_date` `create_date` DATETIME NULL DEFAULT NULL COMMENT '创建时间' ,
CHANGE COLUMN `last_update` `last_update` DATETIME NULL DEFAULT NULL COMMENT '最后更新时间' ;

ALTER TABLE `tag_type`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `code` `code` VARCHAR(32) NOT NULL COMMENT '标签类型编码' ,
CHANGE COLUMN `domain_id` `domain_id` INT(11) NOT NULL COMMENT '关联的域，外键' ;

ALTER TABLE `user`
CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
CHANGE COLUMN `name` `name` VARCHAR(64) NULL DEFAULT NULL COMMENT '用户姓名' ,
CHANGE COLUMN `email` `email` VARCHAR(64) NOT NULL COMMENT '用户邮箱' ,
CHANGE COLUMN `phone` `phone` VARCHAR(64) NULL DEFAULT NULL COMMENT '电话号码' ,
CHANGE COLUMN `password` `password` VARCHAR(64) NOT NULL COMMENT '密码' ,
CHANGE COLUMN `last_login_time` `last_login_time` DATETIME NULL DEFAULT NULL COMMENT '最后一次登录的时间' ,
CHANGE COLUMN `last_login_ip` `last_login_ip` VARCHAR(128) NULL DEFAULT NULL COMMENT '最后一次登录然ip' ,
CHANGE COLUMN `fail_count` `fail_count` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '失败的次数' ,
CHANGE COLUMN `status` `status` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '状态' ,
CHANGE COLUMN `create_date` `create_date` DATETIME NULL DEFAULT NULL COMMENT '创建时间 ' ,
CHANGE COLUMN `last_update` `last_update` DATETIME NULL DEFAULT NULL COMMENT '最后更新时间' ,
CHANGE COLUMN `password_date` `password_date` DATETIME NULL DEFAULT NULL COMMENT '更新密码的时间' ;

ALTER TABLE `user_grp`
CHANGE COLUMN `user_id` `user_id` BIGINT(20) NOT NULL COMMENT '用户id，外键' ,
CHANGE COLUMN `grp_id` `grp_id` INT(11) NOT NULL COMMENT '对应组的id，外键' ,
CHANGE COLUMN `type` `type` TINYINT(3) NOT NULL COMMENT '类型，（0表示普通会员，1表示owner）' ;

ALTER TABLE `user_role`
CHANGE COLUMN `user_id` `user_id` BIGINT(20) NOT NULL COMMENT '用户id,外键' ,
CHANGE COLUMN `role_id` `role_id` INT(11) NOT NULL COMMENT '角色id，外键' ;

ALTER TABLE `audit`
CHANGE COLUMN `user_id` `user_id` BIGINT(20) NULL DEFAULT NULL COMMENT '用户id' ,
CHANGE COLUMN `req_date` `req_date` DATETIME NULL DEFAULT NULL COMMENT '访问时间' ,
CHANGE COLUMN `domain_id` `domain_id` INT(11) NULL DEFAULT NULL COMMENT '域id' ,
CHANGE COLUMN `req_ip` `req_ip` VARCHAR(128) NULL DEFAULT NULL COMMENT '访问者ip' ,
CHANGE COLUMN `req_url` `req_url` VARCHAR(64) NULL DEFAULT NULL COMMENT '访问的url' ,
CHANGE COLUMN `req_seq` `req_seq` BIGINT(20) NULL DEFAULT NULL COMMENT '访问的序号' ,
CHANGE COLUMN `req_class` `req_class` VARCHAR(64) NULL DEFAULT NULL COMMENT '访问对应的类' ,
CHANGE COLUMN `req_method` `req_method` VARCHAR(64) NULL DEFAULT NULL COMMENT '访问对应的方法' ,
CHANGE COLUMN `req_success` `req_success` TINYINT(3) NULL DEFAULT NULL COMMENT '请求是否成功' ,
CHANGE COLUMN `req_exp` `req_exp` VARCHAR(3072) NULL DEFAULT NULL COMMENT '请求产生的异常信息' ,
CHANGE COLUMN `req_elapse` `req_elapse` BIGINT(20) NULL DEFAULT NULL COMMENT '处理请求所用的时间（ms）' ,
CHANGE COLUMN `req_param` `req_param` VARCHAR(256) NULL DEFAULT NULL COMMENT '请求的参数' ,
CHANGE COLUMN `req_result` `req_result` VARCHAR(1024) NULL DEFAULT NULL COMMENT '请求的返回结果' ;
