-- Attribute privilege
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/permission/search-users-by-perms$',0,'POST',1,'',now(),now());
