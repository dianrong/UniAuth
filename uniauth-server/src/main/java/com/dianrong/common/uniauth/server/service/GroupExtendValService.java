package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtendExample;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpExtendValExample;
import com.dianrong.common.uniauth.server.data.mapper.AttributeExtendMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpExtendValMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 组扩展属性值处理Service.
 */
@Service
public class GroupExtendValService extends TenancyBasedService {

  @Autowired
  private GrpExtendValMapper grpExtendValMapper;
  
  @Autowired
  private AttributeExtendMapper attributeExtendMapper;
  
  @Resource(name = "grpExtendValDataFilter")
  private DataFilter dataFilter;

  /**
   * 根据组和扩展属性id集合获取扩展属性Code和扩展属性值的Map.
   * <p>GrpExtendCode->GrpExtendVal</p>
   */
  public Map<String, ExtendVal> queryAttributeVal(Integer grpId, List<Long> extendAttributeIds) {
    CheckEmpty.checkEmpty(grpId, "groupId");
    Map<String, ExtendVal> resultMap = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(extendAttributeIds)) {
      return resultMap;
    }
    AttributeExtendExample attributeExtendExample = new AttributeExtendExample();
    AttributeExtendExample.Criteria criteria = attributeExtendExample.createCriteria();
    criteria.andIdIn(extendAttributeIds);
    List<AttributeExtend> attributeExtends = attributeExtendMapper.selectByExample(attributeExtendExample);
    if (ObjectUtil.collectionIsEmptyOrNull(attributeExtends)) {
      return resultMap;
    }
    Map<Long, AttributeExtend> attributeExtendMap = Maps.newHashMap();
    for (AttributeExtend ae: attributeExtends) {
      attributeExtendMap.put(ae.getId(), ae);
    }
    
    GrpExtendValExample grpExtendValExample = new GrpExtendValExample();
    GrpExtendValExample.Criteria gevCriteria = grpExtendValExample.createCriteria();
    gevCriteria.andExtendIdIn(extendAttributeIds).andGrpIdEqualTo(grpId);
    List<GrpExtendVal> grpExtendVals = grpExtendValMapper.selectByExample(grpExtendValExample);
    if (ObjectUtil.collectionIsEmptyOrNull(grpExtendVals)) {
      return resultMap;
    }
    for (GrpExtendVal val:grpExtendVals) {
      resultMap.put(attributeExtendMap.get(val.getExtendId()).getCode(), val);
    }
    return resultMap;
  }
}

