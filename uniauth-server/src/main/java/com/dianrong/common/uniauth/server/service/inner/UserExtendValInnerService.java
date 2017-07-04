package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendValMapper;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordType;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户扩展属性值Service的内部实现.
 */
@Service
public class UserExtendValInnerService extends TenancyBasedService {

  @Autowired
  private UserExtendValMapper userExtendValMapper;

  /**
   * 新增.
   */
  @Transactional
  @ExtendAttributeRecord(type = RecordType.USER, operate = RecordOperate.ADD)
  public UserExtendVal addNew(Long userId, Long extendId, String value) {
    UserExtendVal record = new UserExtendVal();
    record.setExtendId(extendId);
    record.setUserId(userId);
    record.setValue(value);
    record.setTenancyId(tenancyService.getTenancyIdWithCheck());
    userExtendValMapper.insert(record);
    return record;
  }

  /**
   * 更新.
   */
  @Transactional
  @ExtendAttributeRecord(type = RecordType.USER, operate = RecordOperate.UPDATE)
  public UserExtendVal update(Long userId, Long extendId, String value) {
    UserExtendValExample userExtendValExample = new UserExtendValExample();
    UserExtendValExample.Criteria criteria = userExtendValExample.createCriteria();
    criteria.andUserIdEqualTo(userId).andExtendIdEqualTo(extendId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    UserExtendVal record = new UserExtendVal();
    userExtendValMapper.updateByExampleSelective(record, userExtendValExample);
    return record;
  }
  
  /**
   * 根据UserId和扩展属性Id查找信息.
   */
  public UserExtendVal queryByUserIdAndExtendId(Long userId, Long extendId) {
    UserExtendValExample userExtendValExample = new UserExtendValExample();
    UserExtendValExample.Criteria criteria = userExtendValExample.createCriteria();
    criteria.andUserIdEqualTo(userId).andExtendIdEqualTo(extendId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<UserExtendVal> userExtendValList = userExtendValMapper.selectByExample(userExtendValExample);
    if (ObjectUtil.collectionIsEmptyOrNull(userExtendValList)) {
      return null;
    }
    return userExtendValList.get(0);
  }

  /**
   * 更新系统自定义的用户属性值.
   * 
   * @param userId 用户id
   * @param idFieldName 在表中用户id所在的字段名
   * @param tableName 需要更新的表名.
   * @param fieldName 需要更新的字段名.
   * @param value 属性需要更新成的值.
   */
  @Transactional
  public void updateSystemDefineUserAttribute(Long userId, String idFieldName, String tableName,
      String fieldName, String value) {
    CheckEmpty.checkEmpty(userId, "userId");
    CheckEmpty.checkEmpty(idFieldName, "idFieldName");
    CheckEmpty.checkEmpty(tableName, "tableName");
    CheckEmpty.checkEmpty(fieldName, "fieldName");
    Map<String, Object> params = Maps.newHashMap();
    params.put("idFieldName", idFieldName);
    params.put("userId", userId);
    params.put("tableName", tableName);
    params.put("fieldName", fieldName);
    params.put("value", value);
    userExtendValMapper.updateSystemDefineUserAttribute(params);
  }
}
