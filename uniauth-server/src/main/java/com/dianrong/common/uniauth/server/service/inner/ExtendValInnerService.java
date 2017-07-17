package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.server.data.mapper.ExtendValMapper;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Maps;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 扩展属性值Service的内部实现(用户和组的扩展属性).
 */
@Service
public class ExtendValInnerService extends TenancyBasedService {

  @Autowired
  private ExtendValMapper extendValMapper;

  /**
   * 添加(或更新)系统自定义的属性值.
   * 
   * @param identityId id属性
   * @param idFieldName 在表中用户id所在的字段名
   * @param tableName 需要更新的表名.
   * @param fieldName 需要更新的字段名.
   * @param value 属性需要更新成的值.
   * @param needUpdateChcek 是否需要进行更新前的check(是否有可能做Insert操作).
   */
  @Transactional
  public void addOrUpdateSystemDefineAttribute(Object identityId, String idFieldName,
      String tableName, String fieldName, Object value, boolean needUpdateChcek) {
    CheckEmpty.checkEmpty(identityId, "identityId");
    CheckEmpty.checkEmpty(idFieldName, "idFieldName");
    CheckEmpty.checkEmpty(tableName, "tableName");
    CheckEmpty.checkEmpty(fieldName, "fieldName");
    Map<String, Object> params = Maps.newHashMap();
    params.put("idFieldName", idFieldName);
    params.put("identityId", identityId);
    params.put("tableName", tableName);
    params.put("fieldName", fieldName);
    params.put("value", value);
    params.put("tenancyId", tenancyService.getTenancyIdWithCheck());
    if (!needUpdateChcek) {
      extendValMapper.updateSystemDefineAttribute(params);
      return;
    }
    // 首先查询一下对应的记录是否存在,存在则更新,不存在则添加
    int existNum = extendValMapper.queryExistRecordNum(params);
    if (existNum > 0) {
      extendValMapper.updateSystemDefineAttribute(params);
    } else {
      extendValMapper.insertSystemDefineAttribute(params);
    }
  }
}
