package com.dianrong.common.uniauth.server.data.mapper;

import com.dianrong.common.uniauth.server.data.entity.UserPwdLog;
import com.dianrong.common.uniauth.server.data.query.UserPwdLogQueryParam;
import com.dianrong.common.uniauth.server.support.audit.MapperAudit;
import com.dianrong.common.uniauth.server.support.audit.NoneMapperAudit;

import java.util.List;

@MapperAudit
public interface UserPwdLogMapper {

  /**
   * 插入新的记录.
   */
  int insert(UserPwdLog log);

  /**
   * 根据条件查询数据.
   */
  @NoneMapperAudit
  List<UserPwdLog> queryUserPwdLogs(UserPwdLogQueryParam condition);
}
