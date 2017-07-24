package com.dianrong.common.uniauth.server.data.mapper;

import java.util.Map;

public interface ExtendValMapper {
  
  int queryExistRecordNum(Map<String, Object> params);
  
  int updateSystemDefineAttribute(Map<String, Object> params);
  
  int insertSystemDefineAttribute(Map<String, Object> params);
}
