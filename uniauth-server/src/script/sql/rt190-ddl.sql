-- Modify user table, add field type
ALTER TABLE user ADD type TINYINT(3) NOT NULL DEFAULT 0 COMMENT '用户类型:0 普通用户, 1 系统管理员账号' after user_guid;
