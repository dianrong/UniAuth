package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.Role;
import com.dianrong.common.uniauth.server.data.entity.RoleExample;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * . 角色的数据过滤处理实现.
 *
 * @author wanglin
 */
@Service("roleDataFilter")
public class RoleDataFilter extends CurrentAbstractDataFilter<Role> {

  @Autowired
  private RoleMapper roleMapper;

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.role");
  }

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    RoleExample condition = new RoleExample();
    RoleExample.Criteria criteria = condition.createCriteria();
    criteria.andStatusEqualTo(AppConstants.STATUS_ENABLED).andTenancyIdEqualTo(getTenancyId());
    // 构造查询条件
    for (FilterData fd : equalsField) {
      switch (fd.getType()) {
        case FIELD_TYPE_DOMAIN_ID:
          criteria.andDomainIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_NAME:
          criteria.andNameEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_ROLE_CODE_ID:
          criteria.andRoleCodeIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
          break;
        default:
          break;
      }
    }
    // 查询
    int count = roleMapper.countByExample(condition);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  protected Role getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "roleId");
    RoleExample condition = new RoleExample();
    condition.createCriteria().andIdEqualTo(id).andStatusEqualTo(AppConstants.STATUS_ENABLED);
    List<Role> roles = roleMapper.selectByExample(condition);
    if (ObjectUtil.IsNotEmptyOrNull(roles)) {
      return roles.get(0);
    }
    return null;
  }
}
