package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtendExample;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserExample;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample.Criteria;
import com.dianrong.common.uniauth.server.data.entity.ext.UserExtendValExt;
import com.dianrong.common.uniauth.server.data.mapper.AttributeExtendMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendValMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserExtendValService extends TenancyBasedService {

  @Autowired
  private UserExtendValMapper userExtendValMapper;

  @Autowired
  private AttributeExtendMapper attributeExtendMapper;

  @Autowired
  private UserMapper userMapper;
  
  @Resource(name = "userExtendValDataFilter")
  private DataFilter dataFilter;

  /**
   * 添加一个用户扩展属性值.
   */
  @Transactional
  public UserExtendValDto add(Long userId, Long extendId, String value) {
    CheckEmpty.checkEmpty(userId, "userId");
    CheckEmpty.checkEmpty(extendId, "extendId");

    // 数据过滤
    dataFilter.addFieldsCheck(FilterType.EXSIT_DATA,
        FilterData.buildFilterData(FieldType.FIELD_TYPE_USER_ID, userId),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_EXTEND_ID, extendId));

    UserExtendVal userExtendVal = new UserExtendVal();
    userExtendVal.setExtendId(extendId);
    userExtendVal.setUserId(userId);
    userExtendVal.setValue(value);
    userExtendVal.setTenancyId(tenancyService.getTenancyIdWithCheck());

    userExtendValMapper.insertSelective(userExtendVal);

    return BeanConverter.convert(userExtendVal, UserExtendValDto.class);
  }

  /**
   * 根据扩展属性值的主键id删除数据.
   *
   * @return 删除的个数
   */
  public int delById(Long id) {
    CheckEmpty.checkEmpty(id, "id");
    return userExtendValMapper.deleteByPrimaryKey(id);
  }

  /**
   * 根据扩展属性值的主键id修改数据.
   */
  @Transactional
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
   * 根据用户id查询扩展属性值.
   *
   * @param userId 用户Id
   */
  public List<UserExtendValDto> searchByUserId(Long userId) {
    CheckEmpty.checkEmpty(userId, "userId");

    UserExtendValExample example = new UserExtendValExample();
    Criteria criteria = example.createCriteria().andUserIdEqualTo(userId);
    criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());

    List<UserExtendVal> userExtendVals = userExtendValMapper.selectByExample(example);
    List<UserExtendValDto> userExtendValDtos = new ArrayList<UserExtendValDto>();
    UserExtendValDto userExtendValDto;
    AttributeExtend attributeExtend;
    for (UserExtendVal userExtendVal : userExtendVals) {
      userExtendValDto = BeanConverter.convert(userExtendVal, UserExtendValDto.class);
      attributeExtend = attributeExtendMapper.selectByPrimaryKey(userExtendValDto.getExtendId());
      userExtendValDto.setExtendCode(attributeExtend.getCode());
      userExtendValDto.setExtendDescription(attributeExtend.getDescription());
      userExtendValDtos.add(userExtendValDto);
    }

    return userExtendValDtos;
  }

  /**
   * 根据用户id和code分页查询数据.
   *
   * @param userId 必填
   * @param code 可选
   */
  public PageDto<UserExtendValDto> searchByUserIdAndCode(Long userId, String code,
      Integer pageNumber, Integer pageSize, boolean queryOnlyUsed) {
    CheckEmpty.checkEmpty(userId, "userId");
    CheckEmpty.checkEmpty(pageNumber, "pageNumber");
    CheckEmpty.checkEmpty(pageSize, "pageSize");

    Map<String, String> params = new HashMap<String, String>();
    params.put("userId", userId.toString());
    params.put("extendCode", code == null ? null : '%' + code + '%');
    params.put("tenancyId", tenancyService.getTenancyIdWithCheck().toString());

    int count = queryOnlyUsed ? userExtendValMapper.countByUserExtend(params)
        : userExtendValMapper.countByCode(params);
    ParamCheck.checkPageParams(pageNumber, pageSize, count);
    params.put("queryOnlyUsed", String.valueOf(queryOnlyUsed));
    params.put("startIndex", String.valueOf(pageNumber * pageSize));
    params.put("pageSize", pageSize.toString());
    List<UserExtendValExt> userExtendValExts = userExtendValMapper.selectByUserIdAndCode(params);
    // 转换
    List<UserExtendValDto> userExtendDtos = new ArrayList<UserExtendValDto>();

    for (UserExtendValExt userExtendValExt : userExtendValExts) {
      userExtendDtos.add(BeanConverter.convert(userExtendValExt, UserExtendValDto.class));
    }
    // 生成分页对象
    PageDto<UserExtendValDto> pageDto =
        new PageDto<UserExtendValDto>(pageNumber, pageSize, count, userExtendDtos);
    return pageDto;
  }

  /**
   * 根据条件查询用户扩展属性值列表.
   *
   * @param extendId 扩展属性id
   * @param value 扩展属性值
   * @param includeDisableUserRelatedExtendVal 是否包含禁用用户关联的扩展属性值
   * @return 符合条件的扩展属性值列表
   */
  public List<UserExtendValDto> search(Long extendId, String value,
      Boolean includeDisableUserRelatedExtendVal) {
    CheckEmpty.checkEmpty(extendId, "extendId");
    CheckEmpty.checkEmpty(value, "value");

    UserExtendValExample example = new UserExtendValExample();
    Criteria criteria = example.createCriteria();
    criteria.andExtendIdEqualTo(extendId).andValueEqualTo(value);
    criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<UserExtendVal> userExtendVals = userExtendValMapper.selectByExample(example);

    List<UserExtendValDto> userExtendValDtos = Lists.newArrayList();
    if (userExtendVals == null || userExtendVals.isEmpty()) {
      return userExtendValDtos;
    }

    // 过滤禁用用户关联的属性值信息
    if (!(includeDisableUserRelatedExtendVal != null && includeDisableUserRelatedExtendVal)) {
      List<Long> userIds = Lists.newArrayList();
      for (UserExtendVal extendVal : userExtendVals) {
        userIds.add(extendVal.getUserId());
      }
      UserExample userExample = new UserExample();
      userExample.createCriteria().andIdIn(userIds).andStatusEqualTo(AppConstants.STATUS_ENABLED);
      List<User> users = userMapper.selectByExample(userExample);
      if (users == null || users.isEmpty()) {
        return Lists.newArrayList();
      }
      Set<Long> enableUserIds = Sets.newHashSet();
      for (User u : users) {
        enableUserIds.add(u.getId());
      }
      List<UserExtendVal> tempUserExtendVals = Lists.newArrayList();
      for (UserExtendVal extendVal : userExtendVals) {
        if (enableUserIds.contains(extendVal.getUserId())) {
          tempUserExtendVals.add(extendVal);
        }
      }
      userExtendVals = tempUserExtendVals;
    }

    AttributeExtend extend = attributeExtendMapper.selectByPrimaryKey(extendId);
    if (extend == null) {
      return userExtendValDtos;
    }

    for (UserExtendVal extendVal : userExtendVals) {
      UserExtendValDto userExtendValDto = BeanConverter.convert(extendVal, UserExtendValDto.class);
      userExtendValDto.setExtendCode(extend.getCode());
      userExtendValDto.setExtendDescription(extend.getDescription());
      userExtendValDtos.add(userExtendValDto);
    }

    return userExtendValDtos;
  }

  /**
   * 根据用户和扩展属性id集合获取扩展属性Code和扩展属性值的Map.
   * <p>UserExtendCode->UserExtendVal</p>
   */
  public Map<String, ExtendVal> queryAttributeVal(Long userId, List<Long> extendAttributeIds) {
    CheckEmpty.checkEmpty(userId, "userId");
    Map<String, ExtendVal> resultMap = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(extendAttributeIds)) {
      return resultMap;
    }
    AttributeExtendExample attributeExtendExample = new AttributeExtendExample();
    AttributeExtendExample.Criteria criteria = attributeExtendExample.createCriteria();
    criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck()).andIdIn(extendAttributeIds);
    List<AttributeExtend> attributeExtends = attributeExtendMapper.selectByExample(attributeExtendExample);
    if (ObjectUtil.collectionIsEmptyOrNull(attributeExtends)) {
      return resultMap;
    }
    Map<Long, AttributeExtend> attributeExtendMap = Maps.newHashMap();
    for (AttributeExtend ae: attributeExtends) {
      attributeExtendMap.put(ae.getId(), ae);
    }
    
    UserExtendValExample userExtendValExample = new UserExtendValExample();
    UserExtendValExample.Criteria uevCriteria = userExtendValExample.createCriteria();
    uevCriteria.andExtendIdIn(extendAttributeIds).andUserIdEqualTo(userId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<UserExtendVal> userExtendVals = userExtendValMapper.selectByExample(userExtendValExample);
    if (ObjectUtil.collectionIsEmptyOrNull(userExtendVals)) {
      return resultMap;
    }
    for (UserExtendVal val:userExtendVals) {
      resultMap.put(attributeExtendMap.get(val.getExtendId()).getCode(), val);
    }
    return resultMap;
  }
}

