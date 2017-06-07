package com.dianrong.common.uniauth.server.mq.v1;

/**
 * 通知消息类型定义.
 *
 * @author wanglin
 */
public enum NotifyInfoType {
  USER_ADD, // 新增用户
  USER_DISABLE, // 用户禁用
  USER_ENABLE, // 用户启用

  GROUP_ADD, // 新增组
  GROUP_DISABLE, // 禁用组
  GROUP_ENABLE, // 启用组
  GROUP_MOVE, // 移动组

  USERS_TO_GROUP_ADD, // 用户与组的关联关系添加
  USERS_TO_GROUP_REMOVE, // 用户与组的关联关系删除
  USERS_TO_GROUP_EXCHANGE // 用户与组的关联关系转移
}
