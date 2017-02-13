-- 插入数据
DROP procedure IF EXISTS `proc_insert_techops_ctl`;

-- 添加techops的api访问控制
DELIMITER $$
CREATE PROCEDURE `proc_insert_techops_ctl`()
BEGIN
declare techops_domain_id int(11);
declare techops_perm_id int(11);
declare techops_caller_id int(11);

select id into techops_domain_id from domain where code = 'techops' and status = 0;
-- insert permission
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^.*',0,'ALL',0,'uniauth内部专用权限',now(),now());
-- insert techops api_caller_info
insert into api_caller_info(domain_id,password,status,comment,create_date,last_update) 
values(techops_domain_id, '=610{8(lxt42nslg*k9q9|5z!:3@~9e72#|rw1*6arz5zad@7+muh!e-w792', 0, 'techops和cas对应的caller', now(), now());

select id into techops_perm_id from api_permission limit 0, 1;
select id into techops_caller_id from api_caller_info limit 0, 1;

-- insert permission和caller的映射
insert into caller_permission(api_caller_id, api_permission_id) values(techops_caller_id, techops_perm_id);
END$$
DELIMITER ;

call proc_insert_techops_ctl();

DROP procedure IF EXISTS `proc_insert_techops_ctl`;


-- public permissions
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs$',0,'ALL',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^.*',0,'GET',1,'',now(),now());

-- audit
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/audit/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/audit/delete$',0,'POST',1,'',now(),now());

-- config
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/config/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/config/cfg-types$',0,'POST',1,'',now(),now());

-- domain
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/domain/login/alldomains$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/domain/domaininfo$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/domain/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/domain/stakeholder/query$',0,'POST',1,'',now(),now());

-- group
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/group/tree$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/group/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/group/groupowners$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/group/domain/roles$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/group/checkowner$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/group/querytagswithchecked$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/group/userInGroupOrSub$',0,'POST',1,'',now(),now());

-- permission
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/permission/allpermtypecodes$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/permission/searchperms$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/permission/roles$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/permission/urlrolemappings$',0,'POST',1,'',now(),now());

-- role
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/role/allrolecodes$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/role/searchroles$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/role/domain/perms$',0,'POST',1,'',now(),now());

-- tag
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/tag/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/tag/tag-types$',0,'POST',1,'',now(),now());

-- tenancy
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/tenancy/query$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/tenancy/queryDefault$',0,'POST',1,'',now(),now());

-- userextend
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userextend/searchuserextend$',0,'POST',1,'',now(),now());

-- userextendval
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userextendval/searchbyuseridandstatus$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userextendval/searchbyuseridandcode$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userextendval/searchbyuseridentity$',0,'POST',1,'',now(),now());

-- user
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/searchusers$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/userswithrolechecked$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/userswithtagchecked$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/domain/roles$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/login$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/usedetailinfo$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/usedetailinfobyid$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/singleuser$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/userInfobytag$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/tagsWithUserChecked$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/search-user-by-roleid$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/search-user-by-tagid$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user/query-user/group-and-role$',0,'POST',1,'',now(),now());

-- userextend
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userextend/adduserextend$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userextend/updateuserextend$',0,'POST',1,'',now(),now());

-- userextendval
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userextendval/add$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userextendval/delbyid$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userextendval/updatebyid$',0,'POST',1,'',now(),now());