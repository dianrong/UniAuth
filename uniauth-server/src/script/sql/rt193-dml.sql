insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/group/query-grps-relate-user$',0,'POST',1,'',now(),now());

-- system login
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/user/system-login$',0,'POST',1,'',now(),now());
