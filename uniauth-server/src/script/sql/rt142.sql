-- -----------------------------------------------------
-- Table `tenancy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tenancy` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT comment '租户主键id',
   `code` VARCHAR(30) NOT NULL comment '每一个租户的唯一标识code',
  `name` VARCHAR(64) NULL comment '租户名称，比如点融网',
  `contact_name` VARCHAR(30) NULL comment '租户联系人姓名',
  `phone` VARCHAR(30) NULL comment '租户联系电话',
  `description` VARCHAR(200) NULL comment '租户描述',
  `status` TINYINT(3) NOT NULL comment '状态，0:启用,1:禁用' DEFAULT 0,
  `create_date` DATETIME NULL comment '租户创建时间',
  `last_update` DATETIME NULL comment '最新更新时间',
  	PRIMARY KEY (`id`) 
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tenancy` (`code`, `name`, `contact_name`, `phone`,`description`, `create_date`, `last_update` ) 
VALUES('DIANRONG-WEBSITE' , '点融网', '点融网', '400-921-9218', '点融网', now(), now());

-- update table structure
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

alter table user_extend add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after description ;
alter table user_extend add index user_extend_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

alter table user_extend_val add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after status ;
alter table user_extend_val add index user_extend_val_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

-- init data
set sql_safe_updates = 0;

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

-- add foreign key

alter table cfg add constraint fk_cfg_tenancy foreign key(tenancy_id) references tenancy(id);

alter table domain add constraint fk_domain_tenancy foreign key(tenancy_id) references tenancy(id);

alter table grp add constraint fk_grp_tenancy foreign key(tenancy_id) references tenancy(id);

alter table permission add constraint fk_permission_tenancy foreign key(tenancy_id) references tenancy(id);

alter table role add constraint fk_role_tenancy foreign key(tenancy_id) references tenancy(id);

alter table stakeholder add constraint fk_stakeholder_tenancy foreign key(tenancy_id) references tenancy(id);

alter table tag add constraint fk_tag_tenancy foreign key(tenancy_id) references tenancy(id);

alter table tag_type add constraint fk_tag_type_tenancy foreign key(tenancy_id) references tenancy(id);

alter table `user` add constraint fk_user_tenancy foreign key(tenancy_id) references tenancy(id);

alter table user_extend add constraint fk_user_extend_tenancy foreign key(tenancy_id) references tenancy(id);

alter table user_extend_val add constraint fk_user_extend_val_tenancy foreign key(tenancy_id) references tenancy(id);

-- update index 
alter table user drop index `user_email_idx`;
alter table user add index  `user_email_idx` (`email`,`tenancy_id`) comment 'user index, email + tenancy_id';

alter table user drop index `user_name_idx`;
alter table user add index  `user_name_idx` (`name`,`tenancy_id`) comment 'user index, name + tenancy_id';

alter table user drop index `user_phone_idx`;
alter table user add index  `user_phone_idx` (`phone`,`tenancy_id`) comment 'user index, phone + tenancy_id';

alter table grp drop index `group_name_idx`;
alter table grp add index  `group_name_idx` (`name`,`tenancy_id`) comment 'grp index, name + tenancy_id';

alter table cfg drop index `cfg_key_UNIQUE`;
alter table cfg add unique index  `cfg_key_UNIQUE` (`cfg_key`,`tenancy_id`) comment 'cfg index, cfg_key + tenancy_id';

alter table tag_type drop index `code_domainid_UNIQUE`;
alter table tag_type add unique index `code_domainid_UNIQUE` (`code`,`domain_id`, `tenancy_id`) comment 'tag_type index: code + domain_id + tenancy_id';

-- for audit
alter table audit add tenancy_id BIGINT(20) NOT NULL DEFAULT 0 comment '租户id' after req_result ;
alter table audit add index audit_tenancy_id (tenancy_id) comment '根据租户id查询的索引';

#update audit INNER JOIN tenancy ON tenancy.code='DIANRONG-WEBSITE'  SET audit.tenancy_id = tenancy.id;

-- 循环更新audit表的tenancy_id 字段
DROP procedure IF EXISTS `multi_tenancy_update_audit`;
DELIMITER $$
CREATE PROCEDURE `multi_tenancy_update_audit`()
BEGIN
declare  audit_top_id int(10);
declare while_index int(10) default 0;
declare while_times int(10);
declare update_min_id int(10);
declare update_max_id int(10);
declare dest_tenancy_id int(10);
declare update_per_num int(10) default 500;

set sql_safe_updates = 0;
select max(id) into audit_top_id from audit;
select max(id) into dest_tenancy_id from tenancy where code='DIANRONG-WEBSITE' ;
set while_times = (audit_top_id  DIV update_per_num) + 1;

-- drop index
IF EXISTS (SELECT * FROM information_schema.statistics WHERE table_name = 'audit' AND index_name = 'uniauth_audit_userid_idx' AND table_schema=database()) THEN  
alter table audit drop index `uniauth_audit_userid_idx`;
END IF;  
IF EXISTS (SELECT * FROM information_schema.statistics WHERE table_name = 'audit' AND index_name = 'uniauth_audit_action_date' AND table_schema=database()) THEN  
alter table audit drop index`uniauth_audit_action_date`;
END IF;  

 while while_index<while_times do
    SET update_min_id = while_index * update_per_num;
    SET update_max_id = update_min_id + update_per_num;
    update audit SET tenancy_id = dest_tenancy_id  WHERE id >= update_min_id and id < update_max_id;
    set while_index=while_index+1;
end while;

-- add index
alter table audit add index  `uniauth_audit_userid_idx` (`user_id`,`tenancy_id`) comment 'audit index, user_id + tenancy_id';
alter table audit add index  `uniauth_audit_action_date` (`req_date`,`tenancy_id`) comment 'audit index, req_date + tenancy_id';
END$$
DELIMITER ;

-- 耗时(幂等)
call multi_tenancy_update_audit();
-- 删除没用的存储过程
DROP procedure IF EXISTS `multi_tenancy_update_audit`;

alter table audit add constraint fk_audit_tenancy foreign key(tenancy_id) references tenancy(id);