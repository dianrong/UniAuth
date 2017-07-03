package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtendExample;
import com.dianrong.common.uniauth.server.data.mapper.AttributeExtendMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.service.cache.AttributeExtendCache;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.inner.AttributeExtendInnerService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;

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
  
  @Autowired
  private AttributeExtendCache attributeExtendCache;
  
  @Autowired
  private AttributeExtendInnerService attributeExtendInnerService;
  
  @Resource(name = "attributeExtendDataFilter")
  private DataFilter dataFilter;

  /**
   * 新增扩展数据.
   */
  public AttributeExtendDto add(String code, String category, String subcategory,
      String description) {
    return attributeExtendInnerService.add(code, category, subcategory, description);
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
   * 根据ProfileId获取关联的扩展属性.
   */
  public List<AttributeExtend> getAttributesByProfileId(Long profileId) {
    return attributeExtendInnerService.getAttributesByProfileId(profileId);
  }
}

