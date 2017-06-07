package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtendExample;
import com.dianrong.common.uniauth.server.data.mapper.AttributeExtendMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeExtendService extends TenancyBasedService {

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

    // 过滤数据
    dataFilter.addFieldCheck(FilterType.FILTER_TYPE_EXSIT_DATA, FieldType.FIELD_TYPE_CODE,
        code.trim());

    AttributeExtend attributeExtend = new AttributeExtend();
    attributeExtend.setCode(code);
    attributeExtend.setCategory(category);
    attributeExtend.setSubcategory(subcategory);
    attributeExtend.setDescription(description);
    attributeExtend.setTenancyId(tenancyService.getTenancyIdWithCheck());
    attributeExtendMapper.insertSelective(attributeExtend);
    AttributeExtendDto attributeExtendDto =
        BeanConverter.convert(attributeExtend, AttributeExtendDto.class);
    return attributeExtendDto;
  }

  /**
   * 根据id获取数据.
   */
  public AttributeExtendDto getById(Long id) {
    AttributeExtend attributeExtend = attributeExtendMapper.selectByPrimaryKey(id);
    AttributeExtendDto attributeExtendDto =
        BeanConverter.convert(attributeExtend, AttributeExtendDto.class);
    return attributeExtendDto;
  }

  /**
   * 根据id修改扩展数据.
   */
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
}

