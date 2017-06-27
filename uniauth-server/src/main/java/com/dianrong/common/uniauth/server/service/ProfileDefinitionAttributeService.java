package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttribute;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionAttributeExample;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionAttributeMapper;
import com.dianrong.common.uniauth.server.service.support.ProcessListQuery;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 内部Service.只服务与ProfileService.或者内部其他Service调用.
 */
@Slf4j
@Service
public class ProfileDefinitionAttributeService extends TenancyBasedService {

  @Autowired
  private ProfileDefinitionAttributeMapper profileDefinitionAttributeMapper;

  @Autowired
  private AttributeExtendService attributeExtendService;

  /**
   * 批量添加Profile与扩展属性的关联关系.
   */
  @Transactional
  public void batchInsert(List<ProfileDefinitionAttribute> infoes) {
    if (ObjectUtil.collectionIsEmptyOrNull(infoes)) {
      log.debug("empty list to insert, just ignore!");
      return;
    }
    profileDefinitionAttributeMapper.batchInsert(infoes);
  }

  /**
   * 扩展Profile的扩展属性.
   */
  @Transactional
  public void extendProfileAttributes(Long id, Map<String, String> attributes) {
    CheckEmpty.checkEmpty(id, "profile definition id");
    // 处理扩展属性
    List<AttributeExtend> attributeExtends = Lists.newArrayList();
    // 关联Profile_definition和扩展属性
    if (attributes != null && !attributes.isEmpty()) {
      for (Map.Entry<String, String> entry : attributes.entrySet()) {
        AttributeExtend attributeExtend = attributeExtendService
            .addAttributeExtendIfNonExistent(entry.getKey(), null, null, entry.getValue());
        attributeExtends.add(attributeExtend);
      }
    }

    List<ProfileDefinitionAttribute> destDefinitionAttributes =
        BeanConverter.convert(id, attributeExtends);
    // 获取所有的关联关系
    List<ProfileDefinitionAttribute> existProfileDefinitionAttributes = query(id, null);
    // 添加的扩展属性列表
    List<ProfileDefinitionAttribute> listToInsert =
        (new ProcessListQuery<ProfileDefinitionAttribute, Long>() {
          @Override
          public Long getId(ProfileDefinitionAttribute o) {
            return o.getAttributeId();
          }
        }).getProcessList(existProfileDefinitionAttributes, destDefinitionAttributes, true);
    batchInsert(listToInsert);
  }

  /**
   * 根据条件删除记录.
   * 
   * @param profileId Profile的id, 不能为空.
   * @param attributeId 扩展属性的id, 可为空.
   */
  @Transactional
  public void delete(Long profileId, Long attributeId) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    ProfileDefinitionAttributeExample example = new ProfileDefinitionAttributeExample();
    ProfileDefinitionAttributeExample.Criteria criteria = example.createCriteria();
    criteria.andProfileIdEqualTo(profileId);
    if (attributeId != null) {
      criteria.andAttributeIdEqualTo(attributeId);
    }
    profileDefinitionAttributeMapper.deleteByExample(example);
  }

  /**
   * 根据条件查询Profile和Attribute的关联关系信息.
   * 
   * @param profileId Profile的id, 不能为空.
   * @param attributeId 扩展属性的id, 可为空.
   */
  public List<ProfileDefinitionAttribute> query(Long profileId, Long attributeId) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    ProfileDefinitionAttributeExample example = new ProfileDefinitionAttributeExample();
    ProfileDefinitionAttributeExample.Criteria criteria = example.createCriteria();
    criteria.andProfileIdEqualTo(profileId);
    if (attributeId != null) {
      criteria.andAttributeIdEqualTo(attributeId);
    }
    return profileDefinitionAttributeMapper.selectByExample(example);
  }
}
