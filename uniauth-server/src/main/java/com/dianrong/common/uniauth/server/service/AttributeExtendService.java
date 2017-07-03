package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtendExample;
import com.dianrong.common.uniauth.server.data.mapper.AttributeExtendMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.dianrong.common.uniauth.server.service.cache.AttributeExtendCache;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AttributeExtendService extends TenancyBasedService {

  @Autowired
  private AttributeExtendMapper attributeExtendMapper;
  
  @Autowired
  private AttributeExtendCache attributeExtendCache;

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
   * 根据id获取数据.
   */
  public AttributeExtendDto getById(Long id) {
    return attributeExtendCache.getById(id);
  }

  /**
   * 根据id修改扩展数据.
   */
  public int updateByKey(Long id, String code, String category, String subcategory,
      String description) {
    CheckEmpty.checkEmpty(id, "id");
    return attributeExtendCache.updateByKey(id, code, category, subcategory, description);
  }

  /**
   * 根据code模糊分页查询数据.
   */
  public PageDto<AttributeExtendDto> search(String code, Integer pageNumber, Integer pageSize) {
    CheckEmpty.checkEmpty(pageNumber, "pageNumber");
    CheckEmpty.checkEmpty(pageSize, "pageSize");

    AttributeExtendExample example = new AttributeExtendExample();
    example.setPageOffSet(pageNumber * pageSize);
    example.setPageSize(pageSize);
    example.setOrderByClause("id desc");
    AttributeExtendExample.Criteria criteria = example.createCriteria();
    if (StringUtils.isNotBlank(code)) {
      criteria.andCodeLike('%' + code + '%');
    }
    criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    // 查询
    int count = attributeExtendMapper.countByExample(example);
    ParamCheck.checkPageParams(pageNumber, pageSize, count);
    List<AttributeExtend> attributeExtends = attributeExtendMapper.selectByExample(example);
    // 转换
    List<AttributeExtendDto> attributeExtendDtos = new ArrayList<AttributeExtendDto>();
    for (AttributeExtend attributeExtend : attributeExtends) {
      attributeExtendDtos.add(BeanConverter.convert(attributeExtend, AttributeExtendDto.class));
    }
    // 生成分页对象
    PageDto<AttributeExtendDto> pageDto =
        new PageDto<AttributeExtendDto>(pageNumber, pageSize, count, attributeExtendDtos);
    return pageDto;
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
    AttributeExtendExample example = new AttributeExtendExample();
    AttributeExtendExample.Criteria criteria = example.createCriteria();
    criteria.andCodeEqualTo(code).andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<AttributeExtend> attributeExtends = attributeExtendMapper.selectByExample(example);
    if (attributeExtends != null && !attributeExtends.isEmpty()) {
      return attributeExtends.get(0);
    }
    log.debug("add new attibute code: {}", code);
    return innerAddAttributeExtend(code, category, subcategory, description);
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

