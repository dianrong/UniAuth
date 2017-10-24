-- Modify hr_dept table, add field bu_short_code
ALTER TABLE hr_dept ADD bu_short_code VARCHAR(50) COMMENT '一级部门缩写' after department_name;
