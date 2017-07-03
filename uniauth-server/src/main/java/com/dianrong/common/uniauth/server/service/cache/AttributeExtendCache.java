package com.dianrong.common.uniauth.server.service.cache;

import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.mapper.AttributeExtendMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = {"attributeExtend"})
public class AttributeExtendCache {

  @Autowired
  private AttributeExtendMapper attributeExtendMapper;

  @Resource(name = "attributeExtendDataFilter")
  private DataFilter dataFilter;

  /**
   * 根据id获取数据.
   */
  @Cacheable(key = "#id")
  public AttributeExtendDto getById(Long id) {
    AttributeExtend attributeExtend = attributeExtendMapper.selectByPrimaryKey(id);
    AttributeExtendDto attributeExtendDto =
        BeanConverter.convert(attributeExtend, AttributeExtendDto.class);
    return attributeExtendDto;
  }

  /**
   * 根据id修改扩展数据.
   */
  @CacheEvict(key = "#id")
  public int updateByKey(Long id, String code, String category, String subcategory,
      String description) {
    CheckEmpty.checkEmpty(id, "id");
    if (!StringUtil.strIsNullOrEmpty(code)) {
      // 过滤数据
      dataFilter.updateFieldCheck(TypeParseUtil.parseToIntegerFromObject(id),
          FieldType.FIELD_TYPE_CODE, code.trim());
    }
    AttributeExtend attributeExtend = new AttributeExtend();
    attributeExtend.setCode(code);
    attributeExtend.setCategory(category);
    attributeExtend.setSubcategory(subcategory);
    attributeExtend.setDescription(description);
    attributeExtend.setId(id);
    return attributeExtendMapper.updateByPrimaryKeySelective(attributeExtend);
  }
}

