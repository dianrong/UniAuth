-- Group
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/ws/rs/group/query-total-info$',0,'POST',1,'',now(),now());