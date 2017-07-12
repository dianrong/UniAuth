-- Update Table `user_detail`, set user_id unique index
ALTER TABLE user_detail ADD UNIQUE INDEX uq_idx_user_detail(`user_id`);
  
-- Update Table `user_work_relationship`
ALTER TABLE user_work_relationship MODIFY `type` TINYINT(4) default 0  COMMENT '类型';