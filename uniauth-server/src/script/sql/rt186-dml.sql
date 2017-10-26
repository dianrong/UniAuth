-- update dianrong tenancy name
UPDATE tenancy SET name='点融' where code = 'DIANRONG';


-- Attribute privilege
insert into api_permission(uri,status,method,type,comment,create_date,last_update)
values('^/ws/rs/attribute-extend/search-attribute-extend$',0,'POST',1,'',now(),now());
