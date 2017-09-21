package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.server.data.mapper.ExtendValMapper;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Maps;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
   * @param isUniqueField 属性是否是唯一的.
   * @param needUpdateCheck 是否需要进行更新前的check(是否有可能做Insert操作).
   */
  @Transactional
  public void addOrUpdateSystemDefineAttribute(Object identityId, String idFieldName,
      String tableName, String fieldName, Object value, boolean isUniqueField,
      boolean needUpdateCheck) {
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

    // 更新的属性是有唯一性的
    if (isUniqueField) {
      boolean isCheckPass = true;
      List<Map<String, Object>> extendValList = extendValMapper.queryEnableExtendVal(params);
      if (!CollectionUtils.isEmpty(extendValList)) {
        isCheckPass = false;
        if (extendValList.size() == 1) {
          Object existIdentityId = extendValList.get(0).get(idFieldName);
          // update 操作
          if (identityId.equals(existIdentityId)) {
            isCheckPass = true;
          }
        }
      }
      if (!isCheckPass) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("data.filter.extend.value.duplication", tableName, fieldName, value));
      }
    }

    if (!needUpdateCheck) {
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
