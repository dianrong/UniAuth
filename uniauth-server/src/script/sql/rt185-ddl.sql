-- HR system data
-- -----------------------------------------------------
-- Table `hr_dept`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_dept` (
  `department_id` BIGINT(20) NOT NULL COMMENT '部门id',
  `department_code` VARCHAR(60) COMMENT '部门编码',
  `department_name` VARCHAR(120) COMMENT '部门名称',
  `date_from` DATE COMMENT '开始日期',
  `active_status` VARCHAR(20) COMMENT 'A有效，I无效',
  `dept_level` VARCHAR(40) COMMENT '部门级别',
  `parents_dept_id` BIGINT(20) COMMENT '上级部门id',
  `parents_dept_code` VARCHAR(20) COMMENT '上级部门编码',
  `parents_dept_name` VARCHAR(50) COMMENT '上级部门名称',
  `location_id` BIGINT(20) COMMENT '位置id',
  `location_code` VARCHAR(60) COMMENT '位置编码',
  `location_name` VARCHAR(60) COMMENT '位置',
  `manager_id` BIGINT(20) COMMENT '部门经理id',
  `manager_num` VARCHAR(30) COMMENT '部门经理编号',
  `cost_center` varchar(60) COMMENT '成本中心',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`department_id`) COMMENT '主键'
)ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT 'HR系统的部门信息';

-- -----------------------------------------------------
-- Table `hr_job`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_job` (
  `job_id` BIGINT(20) NOT NULL COMMENT '职位id',
  `job_code` VARCHAR(60) COMMENT '职位编码',
  `job_name` VARCHAR(120) COMMENT '职位名称',
  `effective_start_date` DATE COMMENT '开始日期',
  `effective_end_date` DATE COMMENT '结束日期',
  `sales` VARCHAR(20) COMMENT '是否销售(none sales和sales)',
  `active_status` VARCHAR(10) COMMENT 'A有效，I无效',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`job_id`) COMMENT '主键'
)ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT 'HR系统的职位信息';

-- -----------------------------------------------------
-- Table `hr_le`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_le` (
  `company_id` BIGINT(20) NOT NULL COMMENT '公司id',
  `company_code` VARCHAR(30) COMMENT '公司编码',
  `company_name` VARCHAR(120) COMMENT '公司名称',
  `effective_start_date` DATE COMMENT '生效开始日期',
  `active_status` VARCHAR(20) COMMENT 'A有效，I无效',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`company_id`) COMMENT '主键'
)ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT 'HR系统的法律实体信息';

-- -----------------------------------------------------
-- Table `hr_person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_person` (
  `person_id` BIGINT(20) NOT NULL COMMENT '员工id',
  `persom_number` VARCHAR(10) COMMENT '员工编号',
  `old_number` VARCHAR(10) COMMENT '老员工编号',
  `if_eng_name` VARCHAR(10) COMMENT '是否英文姓名(Y/N)',
  `last_name_cn` VARCHAR(10) COMMENT '中文姓',
  `first_name_cn` VARCHAR(10) COMMENT '中文名',
  `last_name_en` VARCHAR(10) COMMENT '拼音姓',
  `first_name_en` VARCHAR(10) COMMENT '拼音名',
  `middle_name_en` VARCHAR(10) COMMENT '中间名',
  `english_name` VARCHAR(20) COMMENT '英文名',
  `gender` VARCHAR(10) COMMENT '性别(M-男,F-女)',
  `email_address` VARCHAR(60) COMMENT '工作邮箱地址',
  `person_type_id` BIGINT(20) COMMENT '人员类型id',
  `seeded_person_type_key` VARCHAR(20) COMMENT '人员类型短码',
  `user_person_type` VARCHAR(30) COMMENT '人员类型',
  `location_id` BIGINT(20) COMMENT '位置id',
  `location_code` VARCHAR(60) COMMENT '位置编码',
  `location_name` VARCHAR(60) COMMENT '位置',
  `mob_phone_number` VARCHAR(30) COMMENT '手机号码',
  `bank_name` VARCHAR(80) COMMENT '银行名称',
  `bank_branch_name` VARCHAR(80) COMMENT '分行名称',
  `bank_sub_branch_name` VARCHAR(80) COMMENT '子分行名称',
  `bank_account` VARCHAR(60) COMMENT '银行账号',
  `effective_start_date` DATE COMMENT '开始日期',
  `assignment_status_type` VARCHAR(20) COMMENT '分配状态类型',
  `action_code` VARCHAR(20) COMMENT '操作编码',
  `travel_subsidies` VARCHAR(20) COMMENT '差标(根据业务规则按职级转换)',
  `bu_id` BIGINT(20) COMMENT '业务单位(关联部门表部门id)',
  `bu_short_code` VARCHAR(20) COMMENT '业务单位短码',
  `bu_code` VARCHAR(20) COMMENT '业务单位编码',
  `bu_name` VARCHAR(100) COMMENT '业务单位名称',
  `department_id` BIGINT(20) COMMENT '部门id',
  `department_code` VARCHAR(20) COMMENT '部门编码',
  `department_name` VARCHAR(50) COMMENT '部门名称',
  `job_id` BIGINT(20) COMMENT '职位id',
  `job_code` VARCHAR(20) COMMENT '职位编码',
  `job_name` VARCHAR(50) COMMENT '职位名称',
  `manager_id` BIGINT(20) COMMENT '经理id',
  `manager_num` VARCHAR(30) COMMENT '经理编号',
  `manager` VARCHAR(30) COMMENT '经理',
  `legal_entities_id` BIGINT(20) COMMENT '法人id',
  `legal_entities_code` VARCHAR(20) COMMENT '法人编码',
  `legal_entities_name` VARCHAR(60) COMMENT '所属法人',
  `company_id` BIGINT(20) COMMENT '所属公司id',
  `company_code` VARCHAR(30) COMMENT '所属公司编码',
  `company_name` VARCHAR(120) COMMENT '所属公司名称',
  `actual_termination_date` DATE COMMENT '最后工作日',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`person_id`) COMMENT '主键'
)ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT 'HR系统的用户信息';

-- -----------------------------------------------------
-- Table `hr_synchronous_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_synchronous_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `synchronous_start_time` DATETIME NOT NULL COMMENT '同步开始时间',
  `synchronous_end_time` DATETIME NOT NULL COMMENT '同步结束时间',
  `synchronous_type` VARCHAR(30) COMMENT '同类类型(同步文件内容到数据库或删除服务器上的过期文件)',
  `process_content` VARCHAR(200) COMMENT '操作的内容.(可以是文件列表，也可以是时间范围内容的文件)',
  `computer_ip` VARCHAR(20) COMMENT '同步服务器的ip',
  `synchronous_result` VARCHAR(10) COMMENT '同步结果(success或failure)',
  `failure_msg` VARCHAR(500) COMMENT '失败原因',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`id`) COMMENT '主键'
)ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT 'HR系统数据同步日志';
