-- relate users and role
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/role/relate-users-and-role$',0,'POST',1,'',now(),now());
-- relate users and tag
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/tag/relate-users-and-tag$',0,'POST',1,'',now(),now());