-- ProfileDefinition
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/profiles/\d+$',0,'GET',1,'',now(),now());
-- UserProfile
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/userprofiles/\d+$',0,'GET',1,'',now(),now());
-- GroupProfile
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/groupprofiles/\d+$',0,'GET',1,'',now(),now());