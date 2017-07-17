insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/group/get-group-by-id-or-code$',0,'POST',1,'',now(),now());

insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/user-work-relationship/search-by-user-id$',0,'GET',1,'',now(),now());
-- GroupProfile
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userdetail/search-by-user-id$',0,'GET',1,'',now(),now());