ALTER TABLE `audit`
ADD FULLTEXT INDEX `idx_audit_req_param_fulltext` (`req_param` ASC)  COMMENT '查询用户在uniauth产生的所有历史记录';
