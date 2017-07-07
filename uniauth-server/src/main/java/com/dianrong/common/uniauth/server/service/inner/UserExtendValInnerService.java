package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendValMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordType;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户扩展属性值Service的内部实现.
 */
@Slf4j
@Service
public class UserExtendValInnerService extends TenancyBasedService {

  @Autowired
  private UserExtendValMapper userExtendValMapper;


  // Data filter
  @Resource(name = "userExtendValDataFilter")
  private DataFilter dataFilter;

  /**
   * 新增.
   */
  @Transactional
  @ExtendAttributeRecord(type = RecordType.USER, operate = RecordOperate.ADD)
  public UserExtendVal addNew(Long userId, Long extendId, String value) {
    CheckEmpty.checkEmpty(userId, "userId");
    CheckEmpty.checkEmpty(extendId, "extendId");

    // 数据过滤
    dataFilter.addFieldsCheck(FilterType.EXSIT_DATA,
        FilterData.buildFilterData(FieldType.FIELD_TYPE_USER_ID, userId),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_EXTEND_ID, extendId));

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
  public void update(Long userId, Long extendId, String value) {
    CheckEmpty.checkEmpty(userId, "userId");
    CheckEmpty.checkEmpty(extendId, "extendId");
    userExtendValMapper.updateValue(userId, extendId, value);
  }

  /**
   * 删除.
   */
  @Transactional
  @ExtendAttributeRecord(type = RecordType.USER, operate = RecordOperate.DELETE, primaryIdIndex = 0)
  public int delById(Long id) {
    CheckEmpty.checkEmpty(id, "id");
    return userExtendValMapper.deleteByPrimaryKey(id);
  }

  /**
   * 根据扩展属性值的主键id修改数据.
   */
  @Transactional
  @ExtendAttributeRecord(type = RecordType.USER, operate = RecordOperate.UPDATE, primaryIdIndex = 0)
  public int updateById(Long id, Long userId, Long extendId, String value) {
    CheckEmpty.checkEmpty(id, "id");
    if (userId == null && extendId == null && StringUtils.isBlank(value)) {
      // none to update, just ignore
      log.warn("user extend value update! update item is null, so just return, the id is {}!", id);
      return 0;
    }
    // 过滤数据
    List<FilterData> filterFileds = new ArrayList<FilterData>();
    if (userId != null) {
      filterFileds.add(FilterData.buildFilterData(FieldType.FIELD_TYPE_USER_ID, userId));
    }
    if (extendId != null) {
      filterFileds.add(FilterData.buildFilterData(FieldType.FIELD_TYPE_EXTEND_ID, extendId));
    }
    if (!filterFileds.isEmpty()) {
      dataFilter.updateFieldsCheck(TypeParseUtil.parseToIntegerFromObject(id),
          filterFileds.toArray(new FilterData[filterFileds.size()]));
    }
    UserExtendVal userExtendVal = new UserExtendVal();
    userExtendVal.setId(id);
    userExtendVal.setExtendId(extendId);
    userExtendVal.setUserId(userId);
    userExtendVal.setValue(value);
    return userExtendValMapper.updateByPrimaryKeySelective(userExtendVal);
  }

  /**
   * 根据UserId和扩展属性Id查找信息.
   */
  public UserExtendVal queryByUserIdAndExtendId(Long userId, Long extendId) {
    UserExtendValExample userExtendValExample = new UserExtendValExample();
    UserExtendValExample.Criteria criteria = userExtendValExample.createCriteria();
    criteria.andUserIdEqualTo(userId).andExtendIdEqualTo(extendId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<UserExtendVal> userExtendValList =
        userExtendValMapper.selectByExample(userExtendValExample);
    if (ObjectUtil.collectionIsEmptyOrNull(userExtendValList)) {
      return null;
    }
    return userExtendValList.get(0);
  }

  /**
   * 根据主键id查找.
   */
  public UserExtendVal queryByPrimaykey(Long primaryId) {
    CheckEmpty.checkEmpty(primaryId, "primaryId");
    return userExtendValMapper.selectByPrimaryKey(primaryId);
  }
}
