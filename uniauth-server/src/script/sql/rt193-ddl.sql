-- hr_le
ALTER TABLE hr_le MODIFY `company_code` VARCHAR(120) COMMENT '公司编码';

-- hr_job
ALTER TABLE hr_job MODIFY `job_code` VARCHAR(120) COMMENT '职位编码';

-- hr_dept
ALTER TABLE hr_dept MODIFY `department_code` VARCHAR(120) COMMENT '部门编码';
ALTER TABLE hr_dept MODIFY `bu_short_code` VARCHAR(100) COMMENT '一级部门缩写';
ALTER TABLE hr_dept MODIFY `dept_level` VARCHAR(100) COMMENT '部门级别';
ALTER TABLE hr_dept MODIFY `parents_dept_code` VARCHAR(120) COMMENT '上级部门编码';
ALTER TABLE hr_dept MODIFY `parents_dept_name` VARCHAR(120) COMMENT '上级部门名称';
ALTER TABLE hr_dept MODIFY `location_code` VARCHAR(120) COMMENT '位置编码';
ALTER TABLE hr_dept MODIFY `location_name` VARCHAR(120) COMMENT '位置';
ALTER TABLE hr_dept MODIFY `cost_center` VARCHAR(120) COMMENT '成本中心';
ALTER TABLE hr_dept MODIFY `manager_num` VARCHAR(120) COMMENT '部门经理编号';

-- hr_person
ALTER TABLE hr_person MODIFY `persom_number` VARCHAR(120) COMMENT '员工编号';
ALTER TABLE hr_person MODIFY `old_number` VARCHAR(120) COMMENT '老员工编号';
ALTER TABLE hr_person MODIFY `last_name_cn` VARCHAR(80) COMMENT '中文姓';
ALTER TABLE hr_person MODIFY `first_name_cn` VARCHAR(80) COMMENT '中文名';
ALTER TABLE hr_person MODIFY `last_name_en` VARCHAR(80) COMMENT '拼音姓';
ALTER TABLE hr_person MODIFY `first_name_en` VARCHAR(80) COMMENT '拼音名';
ALTER TABLE hr_person MODIFY `middle_name_en` VARCHAR(80) COMMENT '中间名';
ALTER TABLE hr_person MODIFY `middle_name_en` VARCHAR(80) COMMENT '中间名';
ALTER TABLE hr_person MODIFY `english_name` VARCHAR(120) COMMENT '英文名';
ALTER TABLE hr_person MODIFY `email_address` VARCHAR(200) COMMENT '工作邮箱地址';
ALTER TABLE hr_person MODIFY `location_code` VARCHAR(120) COMMENT '位置编码';
ALTER TABLE hr_person MODIFY `location_name` VARCHAR(120) COMMENT '位置';
ALTER TABLE hr_person MODIFY `mob_phone_number` VARCHAR(80) COMMENT '手机号码';
ALTER TABLE hr_person MODIFY `bank_name` VARCHAR(120) COMMENT '银行名称';
ALTER TABLE hr_person MODIFY `bank_branch_name` VARCHAR(120) COMMENT '分行名称';
ALTER TABLE hr_person MODIFY `bank_sub_branch_name` VARCHAR(120) COMMENT '子分行名称';
ALTER TABLE hr_person MODIFY `bank_account` VARCHAR(120) COMMENT '银行账号';
ALTER TABLE hr_person MODIFY `action_code` VARCHAR(120) COMMENT '操作编码';
ALTER TABLE hr_person MODIFY `travel_subsidies` VARCHAR(100) COMMENT '差标(根据业务规则按职级转换)';
ALTER TABLE hr_person MODIFY `bu_short_code` VARCHAR(120) COMMENT '业务单位短码';
ALTER TABLE hr_person MODIFY `bu_code` VARCHAR(120) COMMENT '业务单位编码';
ALTER TABLE hr_person MODIFY `bu_name` VARCHAR(120) COMMENT '业务单位名称';
ALTER TABLE hr_person MODIFY `department_code` VARCHAR(120) COMMENT '部门编码';
ALTER TABLE hr_person MODIFY `department_name` VARCHAR(120) COMMENT '部门名称';
ALTER TABLE hr_person MODIFY `job_code` VARCHAR(120) COMMENT '职位编码';
ALTER TABLE hr_person MODIFY `job_name` VARCHAR(120) COMMENT '职位名称';
ALTER TABLE hr_person MODIFY `job_name` VARCHAR(120) COMMENT '职位名称';
ALTER TABLE hr_person MODIFY `manager_num` VARCHAR(120) COMMENT '经理编号';
ALTER TABLE hr_person MODIFY `manager` VARCHAR(120) COMMENT '经理';
ALTER TABLE hr_person MODIFY `legal_entities_code` VARCHAR(120) COMMENT '法人编码';
ALTER TABLE hr_person MODIFY `legal_entities_name` VARCHAR(120) COMMENT '所属法人';
ALTER TABLE hr_person MODIFY `company_code` VARCHAR(120) COMMENT '所属公司编码';
ALTER TABLE hr_person MODIFY `company_name` VARCHAR(120) COMMENT '所属公司名称';
