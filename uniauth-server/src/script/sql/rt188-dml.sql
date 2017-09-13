-- init organization root

DROP procedure IF EXISTS `proc_init_organization_root`;

DELIMITER $$
CREATE PROCEDURE `proc_init_organization_root`()
BEGIN
declare done int default 0;
declare t_org_id int(11);
declare t_user_num int(11);
declare t_user_id BIGINT(20);
declare t_tenancy_id BIGINT(20);
declare t_tenancy_name VARCHAR(64);
declare tenancy_cur cursor for select id, name from tenancy where status = 0;
declare continue handler for not found set done = 1;

-- 循环插入数据
open tenancy_cur;
root_loop: loop
fetch tenancy_cur into t_tenancy_id, t_tenancy_name;
if done = 1 then
leave root_loop;
end if;
insert into grp(name, code, description,status, create_date, last_update, tenancy_id) values(t_tenancy_name, 'ORGANIZATION_ROOT', concat(t_tenancy_name, '根组织关系'), 0, now(), now(), t_tenancy_id);

select id into t_org_id from grp where status = 0 and code = 'ORGANIZATION_ROOT' and tenancy_id = t_tenancy_id;

insert into grp_path values(t_org_id, t_org_id, 0);

select count(id) into t_user_num from user where status = 0 and email = 'first.admin@test.com' and tenancy_id = t_tenancy_id;

if t_user_num = 1 then
	select id into t_user_id from user where status = 0 and email = 'first.admin@test.com' and tenancy_id = t_tenancy_id;
    insert into user_grp values(t_user_id, t_org_id, 1);
end if;

end loop root_loop;
close tenancy_cur;
END$$
DELIMITER ;

call proc_init_organization_root();

DROP procedure IF EXISTS `proc_init_organization_root`;
