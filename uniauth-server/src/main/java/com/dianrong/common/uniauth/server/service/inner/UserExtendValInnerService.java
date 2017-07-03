package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendValMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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

  // Data filter
  @Resource(name = "userExtendValDataFilter")
  private DataFilter dataFilter;

  /**
   * 添加或者更新用户属性.
   */
  @Transactional
  public UserExtendValDto addOrUpdate(Long userId, Long extendId, String value) {
    CheckEmpty.checkEmpty(userId, "userId");
    CheckEmpty.checkEmpty(extendId, "extendId");

    // 数据过滤
    dataFilter.addFieldsCheck(FilterType.EXSIT_DATA,
        FilterData.buildFilterData(FieldType.FIELD_TYPE_USER_ID, userId),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_EXTEND_ID, extendId));

    UserExtendValExample userExtendValExample = new UserExtendValExample();
    UserExtendValExample.Criteria criteria = userExtendValExample.createCriteria();
    criteria.andUserIdEqualTo(userId).andExtendIdEqualTo(extendId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<UserExtendVal> existUserExtendVal =
        userExtendValMapper.selectByExample(userExtendValExample);
    UserExtendVal record = new UserExtendVal();
    if (ObjectUtil.collectionIsEmptyOrNull(existUserExtendVal)) {
      // add
      record.setExtendId(extendId);
      record.setUserId(userId);
      record.setValue(value);
      record.setTenancyId(tenancyService.getTenancyIdWithCheck());
      userExtendValMapper.insert(record);
    } else {
      // update
      record.setValue(value);
      userExtendValMapper.updateByExampleSelective(record, userExtendValExample);
      record.setExtendId(extendId);
      record.setUserId(userId);
      record.setTenancyId(tenancyService.getTenancyIdWithCheck());
    }
    return BeanConverter.convert(record, UserExtendValDto.class);
  }

  /**
   * 更新系统自定义的用户属性值.
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
