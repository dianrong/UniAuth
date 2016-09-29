-- -----------------------------------------------------
-- Table `tenancy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tenancy` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
   `code` VARCHAR(30) NOT NULL comment '每一个租户的唯一标识code',
  `name` VARCHAR(64) NULL,
  `contact_name` VARCHAR(30) NULL,
  `phone` VARCHAR(30) NULL,
  `description` VARCHAR(200) NULL,
  `status` TINYINT(3) NOT NULL DEFAULT 0,
  `create_date` DATETIME NULL,
  `last_update` DATETIME NULL,
  	PRIMARY KEY (`id`)
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tenancy` (`code`, `name`, `contact_name`, `phone`,`description`, `create_date`, `last_update` ) 
VALUES('DIANRONG-WEBSITE' , '点融网', '点融网', '400-921-9218', '点融网', now(), now());

-- update table structure
alter table audit add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after req_result ;
alter table audit add index audit_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table cfg add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after cfg_type_id ;
alter table cfg add index cfg_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table domain add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after status ;
alter table domain add index domain_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table grp add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after status ;
alter table grp add index grp_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table permission add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after domain_id ;
alter table permission add index permission_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table role add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after role_code_id ;
alter table role add index role_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table stakeholder add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after domain_id ;
alter table stakeholder add index stakeholder_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table tag add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after tag_type_id ;
alter table tag add index tag_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table tag_type add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after domain_id;
alter table tag_type add index tag_type_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table `user` add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after status ;
alter table `user` add index user_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table `user_extend` add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after description ;
alter table `user_extend` add index user_extend_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table `user_extend_val` add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after status ;
alter table `user_extend_val` add index user_extend_val_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

-- init data
set sql_safe_updates = 0;
update audit INNER JOIN tenancy ON tenancy.code='DIANRONG-WEBSITE'  SET audit.tenancy_id = tenancy.id;

update cfg INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET cfg.tenancy_id = tenancy.id;

update domain INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET domain.tenancy_id = tenancy.id;

update grp INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET grp.tenancy_id = tenancy.id;

update permission INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET permission.tenancy_id = tenancy.id;

update role INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET role.tenancy_id = tenancy.id;

update stakeholder INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET stakeholder.tenancy_id = tenancy.id;

update tag INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET tag.tenancy_id = tenancy.id;

update tag_type INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET tag_type.tenancy_id = tenancy.id;

update `user` INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET `user`.tenancy_id = tenancy.id;

update user_extend INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET user_extend.tenancy_id = tenancy.id;

update user_extend_val INNER JOIN tenancy ON  tenancy.code='DIANRONG-WEBSITE'   SET user_extend_val.tenancy_id = tenancy.id;
