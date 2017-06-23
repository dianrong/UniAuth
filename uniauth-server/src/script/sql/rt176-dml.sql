-- metrics check
insert into api_permission(uri,status,method,type,comment,create_date,last_update) 
values('^/metrics/.*$',0,'ALL',1,'',now(),now());