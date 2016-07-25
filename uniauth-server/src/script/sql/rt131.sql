alter table tag_type drop index `code_UNIQUE`;
alter table tag_type add unique key `code_domainid_UNIQUE` (`code`,`domain_id`) comment 'update table tag_type unique_key from code to code + domain_id';