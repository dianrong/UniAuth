/**user表**/
insert into user(id, name, email, phone, password, password_salt, last_login_time, last_login_ip, fail_count, status, create_date, last_update, password_date)
values('300000002', '管理员1', 'admin1@xxx.com', '181xxxxxxxx', 'GRodddDAZjK2tGZ6kT7ImP8ILwU=', 'I9JTzG2zzBAW3Q5NvP8lRg==', now(), '192.168.18.5', '0', '0', now(), now(), now());
insert into user(id, name, email, phone, password, password_salt, last_login_time, last_login_ip, fail_count, status, create_date, last_update, password_date)
values('300000003', '管理员2', 'admin2@xxx.com', '170xxxxxxxx', 'GRodddDAZjK2tGZ6kT7ImP8ILwU=', 'I9JTzG2zzBAW3Q5NvP8lRg==', now(), '192.168.18.5', '0', '0', now(), now(), now());
insert into user(id, name, email, phone, password, password_salt, last_login_time, last_login_ip, fail_count, status, create_date, last_update, password_date)
values('300000004', '管理员3', 'admin3@xxx.com', '150xxxxxxxx', 'GRodddDAZjK2tGZ6kT7ImP8ILwU=', 'I9JTzG2zzBAW3Q5NvP8lRg==', now(), '192.168.18.5', '0', '0', now(), now(), now());


/**grp表**/
insert into grp(id, name, code, description,status, create_date, last_update) values(100003, 'techops上的crm域管理员组', 'GRP_TECHOPS_CRM_ADMIN','techops上的crm域管理员组，组内的人期望可以管理crm域的role，permission等资源', 0, now(), now());
insert into grp(id, name, code, description,status, create_date, last_update) values(100004, 'techops上的ams域管理员组', 'GRP_TECHOPS_AMS_ADMIN','techops上的ams域管理员组，组内的人期望可以管理ams域的role，permission等资源', 0, now(), now());

/**grp_path表*/
insert into grp_path(ancestor, descendant, deepth) values(100001, 100003, 1);
insert into grp_path(ancestor, descendant, deepth) values(100001, 100004, 1);

insert into grp_path(ancestor, descendant, deepth) values(100003, 100003, 0);
insert into grp_path(ancestor, descendant, deepth) values(100004, 100004, 0);


/**user_grp表*/
insert into user_grp(user_id, grp_id, type) values(300000002, 100003, 0);
insert into user_grp(user_id, grp_id, type) values(300000003, 100004, 0);
insert into user_grp(user_id, grp_id, type) values(300000004, 100003, 0);

/**role_code表*/

/**domain表**/
insert into domain(id, code, display_name, description, status, create_date, last_update) values(2, 'crm', '客户关系管理系统', '客户关系管理系统，管理客户关系', 0, now(), now());
insert into domain(id, code, display_name, description, status, create_date, last_update) values(3, 'ams', '资产管理系统', '资产管理系统，管理公司资产', 0, now(), now());

/**role表**/
insert into role(id, name,description, status, domain_id, role_code_id) values(2, 'techops上的crm域管理员角色', 'techops上的crm域管理员角色，管理crm域的权限', 0, 1, 2);
insert into role(id, name,description, status, domain_id, role_code_id) values(3, 'techops上的ams域管理员角色', 'techops上的ams域管理员角色，管理ams域的权限', 0, 1, 2);

/**perm_type表*/

/**permission表*/
insert into permission(id, value, description, status, perm_type_id, domain_id) values(2, 'crm', '拥有techops上crm域的permission，全权管理在techops上的crm域',0,1,1);
insert into permission(id, value, description, status, perm_type_id, domain_id) values(3, 'ams', '拥有techops上ams域的permission，全权管理在techops上的ams域',0,1,1);

/**role_permission表*/
insert into role_permission(role_id, permission_id) values(2, 2);
insert into role_permission(role_id, permission_id) values(3, 3);

/**user_role表*/
insert into user_role(user_id, role_id) values(300000004, 3);


/**grp_role表*/
insert into grp_role(grp_id, role_id)  values(100003, 2);
insert into grp_role(grp_id, role_id)  values(100004, 3);

/**stakeholder*/
insert into stakeholder(id, name, email, phone, jobtitle, domain_id) values(1, '研发人员1', 'developer1@xxx.com', '159xxxxxxxx', '开发人员', 1);

/**tagType*/
INSERT INTO `tag_type` (`id`, `code`, `domain_id`) VALUES ('1', 'testTagType1', '1');
INSERT INTO `tag_type` (`id`, `code`, `domain_id`) VALUES ('2', 'testTagType2', '1');

/**tag*/
INSERT INTO `tag` (`id`, `code`, `status`, `description`, `tag_type_id`) VALUES (1, 'testTag1', '0', 'testTag1', '1');
INSERT INTO `tag` (`id`, `code`, `status`, `description`, `tag_type_id`) VALUES (2, 'testTag2', '0', 'testTag2', '1');
INSERT INTO `tag` (`id`, `code`, `status`, `description`, `tag_type_id`) VALUES (3, 'testTag3', '0', 'testTag3', '2');

/**user_tag*/
INSERT INTO `user_tag` (`tag_id`, `user_id`) VALUES ('1', '300000001');
INSERT INTO `user_tag` (`tag_id`, `user_id`) VALUES ('2', '300000001');
INSERT INTO `user_tag` (`tag_id`, `user_id`) VALUES ('3', '300000001');

/**grp_tag*/
INSERT INTO `grp_tag` (`tag_id`, `grp_id`) VALUES ('1', '100003');
INSERT INTO `grp_tag` (`tag_id`, `grp_id`) VALUES ('2', '100004');
INSERT INTO `grp_tag` (`tag_id`, `grp_id`) VALUES ('3', '100003');
