package com.dianrong.common.uniauth.server.data.mapper;

import com.dianrong.common.uniauth.server.support.audit.MapperAudit;

import java.util.List;
import java.util.Map;


public interface ExtendValMapper {

  int queryExistRecordNum(Map<String, Object> params);

  List<Map<String, Object>> queryEnableExtendVal(Map<String, Object> params);

  @MapperAudit int updateSystemDefineAttribute(Map<String, Object> params);

  @MapperAudit
  int insertSystemDefineAttribute(Map<String, Object> params);
}
