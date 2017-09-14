-- Query synchronous log
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/synchronous-data/search-log$',0,'GET',1,'',now(),now());

insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/attribute-extend/search-attribute-extend$',0,'GET',1,'',now(),now());

-- update dianrong tenancy name
UPDATE tenancy SET name='点融' where code = 'DIANRONG';
