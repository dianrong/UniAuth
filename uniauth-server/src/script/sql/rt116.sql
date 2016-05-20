drop table if exists audit;

CREATE TABLE `audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `req_date` datetime DEFAULT NULL,
  `domain_id` int(11) default null,
  `req_ip` varchar(128) DEFAULT NULL,
  `req_uuid` varchar(128) DEFAULT NULL,
  `req_url` varchar(64) DEFAULT NULL,
  `req_seq` bigint(20) DEFAULT NULL,
  `req_class` varchar(64) DEFAULT NULL,
  `req_method` varchar(64) DEFAULT NULL,
  `req_success` tinyint(3) DEFAULT NULL,
  `req_exp` varchar(3072) DEFAULT NULL,
  `req_elapse` bigint(20) DEFAULT NULL,
  `req_param` varchar(256) DEFAULT NULL,
  `req_result` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

delete from user_grp where user_id > 0;
delete from grp_role where grp_id > 0;
delete from grp_path where ancestor > 0;

alter table `user` AUTO_INCREMENT=300000010;
update user set id = 300000001 where id = 200000001;

alter table `grp` AUTO_INCREMENT=100010;
update grp set id = 100001 where id = 1;
update grp set id = 100002 where id = 2;

insert into grp_path(ancestor, descendant, deepth) values(100001, 100001, 0);
insert into grp_path(ancestor, descendant, deepth) values(100001, 100002, 1);
insert into grp_path(ancestor, descendant, deepth) values(100002, 100002, 0);

insert into user_grp(user_id, grp_id, type) values(300000001, 100002, 0);
insert into user_grp(user_id, grp_id, type) values(300000001, 100001, 1);

insert into grp_role(grp_id, role_id)  values(100002, 1);




