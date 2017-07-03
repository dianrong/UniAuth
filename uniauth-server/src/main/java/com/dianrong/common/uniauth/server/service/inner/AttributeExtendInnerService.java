package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtendExample;
import com.dianrong.common.uniauth.server.data.mapper.AttributeExtendMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AttributeExtendInnerService extends TenancyBasedService {

  @Autowired
  private AttributeExtendMapper attributeExtendMapper;

  @Resource(name = "attributeExtendDataFilter")
  private DataFilter dataFilter;

  /**
   * 新增扩展数据.
   */
  public AttributeExtendDto add(String code, String category, String subcategory,
      String description) {
    CheckEmpty.checkEmpty(code, "attributeExtendCode");
    dataFilter.addFieldCheck(FilterType.EXSIT_DATA, FieldType.FIELD_TYPE_CODE, code.trim());
    AttributeExtend attributeExtend =
        innerAddAttributeExtend(code, category, subcategory, description);
    AttributeExtendDto attributeExtendDto =
        BeanConverter.convert(attributeExtend, AttributeExtendDto.class);
    return attributeExtendDto;
  }
  
  /**
   * 如果不存在则添加一个.
   */
  @Transactional
  public AttributeExtend addAttributeExtendIfNonExistent(String code,
      AttributeValModel attributeVal) {
    String category = attributeVal != null ? attributeVal.getCategory() : null;
    String subCategory = attributeVal != null ? attributeVal.getSubcategory() : null;
    String description = attributeVal != null ? attributeVal.getDescription() : null;
    return addAttributeExtendIfNonExistent(code, category, subCategory, description);
  }

  /**
   * 如果不存在则添加一个.
   */
  @Transactional
  public AttributeExtend addAttributeExtendIfNonExistent(String code, String category,
      String subcategory, String description) {
    CheckEmpty.checkEmpty(code, "attributeExtendCode");
    AttributeExtend attributeExtend = queryAttributeExtendByCode(code);
    if (attributeExtend != null) {
      return attributeExtend;
    }
    log.debug("add new attibute code: {}", code);
    return innerAddAttributeExtend(code, category, subcategory, description);
  }
  
  /**
   * 查询AttributeExtend.
   */
  public AttributeExtend queryAttributeExtendByCode(String code) {
    CheckEmpty.checkEmpty(code, "attributeExtendCode");
    AttributeExtendExample example = new AttributeExtendExample();
    AttributeExtendExample.Criteria criteria = example.createCriteria();
    criteria.andCodeEqualTo(code).andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<AttributeExtend> attributeExtends = attributeExtendMapper.selectByExample(example);
    if (attributeExtends != null && !attributeExtends.isEmpty()) {
      return attributeExtends.get(0);
    }
    return null;
  }

  /**
   * 根据ProfileId获取关联的扩展属性.
   */
  public List<AttributeExtend> getAttributesByProfileId(Long profileId) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    return attributeExtendMapper.getAttributesByProfileId(profileId);
  }
  
  /**
   * 添加一个新的扩展属性. 代码不做Code的唯一性校验.
   */
  @Transactional
  private AttributeExtend innerAddAttributeExtend(String code, String category, String subcategory,
      String description) {
    AttributeExtend attributeExtend = new AttributeExtend();
    attributeExtend.setCode(code);
    attributeExtend.setCategory(category);
    attributeExtend.setSubcategory(subcategory);
    attributeExtend.setDescription(description);
    attributeExtend.setTenancyId(tenancyService.getTenancyIdWithCheck());
    attributeExtendMapper.insertSelective(attributeExtend);
    return attributeExtend;
  }
}

