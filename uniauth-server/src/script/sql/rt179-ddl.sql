  -- Update Table `user_detail`, set user_id unique index
  ALTER TABLE user_detail ADD UNIQUE INDEX uq_idx_user_detail(`user_id`);