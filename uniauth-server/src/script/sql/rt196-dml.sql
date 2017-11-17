insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/role/user-roles$',0,'POST',1,'',now(),now());

insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/query-by-id$',0,'POST',1,'',now(),now());
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/query-by-account$',0,'POST',1,'',now(),now());
