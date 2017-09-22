-- Update table hr_synchronous_log
ALTER TABLE hr_synchronous_log MODIFY `failure_msg` VARCHAR(2000) COMMENT '失败原因';

-- Update table hr_person
ALTER TABLE hr_person MODIFY `persom_number` VARCHAR(30) COMMENT '员工编号';
ALTER TABLE hr_person MODIFY `old_number` VARCHAR(30) COMMENT '老员工编号';
ALTER TABLE hr_person MODIFY `if_eng_name` VARCHAR(30) COMMENT '是否英文姓名(Y/N)';
ALTER TABLE hr_person MODIFY `last_name_cn` VARCHAR(30) COMMENT '中文姓';
ALTER TABLE hr_person MODIFY `first_name_cn` VARCHAR(30) COMMENT '中文名';
ALTER TABLE hr_person MODIFY `last_name_en` VARCHAR(30) COMMENT '拼音姓';
ALTER TABLE hr_person MODIFY `first_name_en` VARCHAR(30) COMMENT '拼音名';
ALTER TABLE hr_person MODIFY `middle_name_en` VARCHAR(30) COMMENT '中间名';
ALTER TABLE hr_person MODIFY `english_name` VARCHAR(60) COMMENT '英文名';
ALTER TABLE hr_person MODIFY `bu_short_code` VARCHAR(50) COMMENT '业务单位短码';
ALTER TABLE hr_person MODIFY `bu_code` VARCHAR(60) COMMENT '业务单位编码';

-- Drop unused tables
Drop table if exists attribute_extend_bak;
Drop table if exists migrate_user_tag_update_failed;
Drop table if exists migrate_userid_update_failed;
Drop table if exists old_new_user;
Drop table if exists temp_user_grp_stat;
Drop table if exists user_bak;
Drop table if exists user_extend_val_bak;
Drop table if exists user_extend_val_bak2;
Drop table if exists user_work_relationship;
