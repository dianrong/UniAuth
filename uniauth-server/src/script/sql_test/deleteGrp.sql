use uniauth;

select @rootGrpId := (select id from grp grp where grp.code='GRP_ROOT');

CREATE TEMPORARY TABLE IF NOT EXISTS temp_grpid select id from grp grp where grp.id not in
(select path.descendant from grp_path path where path.ancestor = @rootGrpId);

delete from user_grp where user_grp.grp_id in (select id from temp_grpid);

delete from grp_tag where grp_tag.grp_id in (select id from temp_grpid);

delete from grp_path where grp_path.descendant in (select id from temp_grpid);

delete from grp where grp.id in (select id from temp_grpid);