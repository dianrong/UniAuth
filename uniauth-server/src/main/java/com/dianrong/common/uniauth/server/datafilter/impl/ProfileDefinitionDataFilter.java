package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.server.data.entity.ProfileDefinition;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionExample;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Profile的数据过滤实现.
 *
 * @author wanglin
 */
@Service("profileDefinitionDataFilter")
public class ProfileDefinitionDataFilter extends CurrentAbstractDataFilter<ProfileDefinition> {

  @Autowired
  private ProfileDefinitionMapper profileDefinitionMapper;

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    ProfileDefinitionExample condition = new ProfileDefinitionExample();
    ProfileDefinitionExample.Criteria criteria = condition.createCriteria();
    criteria.andTenancyIdEqualTo(getTenancyId());
    // 构造查询条件
    for (FilterData fd : equalsField) {
      switch (fd.getType()) {
        case FIELD_TYPE_CODE:
          criteria.andCodeEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_ID:
          criteria.andIdEqualTo(TypeParseUtil.parseToLongFromObject(fd.getValue()));
          break;
        default:
          break;
      }
    }
    // 查询
    int count = profileDefinitionMapper.countByExample(condition);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.profiledefinition");
  }

  @Override
  protected ProfileDefinition getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "profileDefinitionId");
    ProfileDefinitionExample condition = new ProfileDefinitionExample();
    condition.createCriteria().andIdEqualTo(TypeParseUtil.parseToLongFromObject(id));
    List<ProfileDefinition> selectByExample = profileDefinitionMapper.selectByExample(condition);
    if (selectByExample != null && !selectByExample.isEmpty()) {
      return selectByExample.get(0);
    }
    return null;
  }
}
