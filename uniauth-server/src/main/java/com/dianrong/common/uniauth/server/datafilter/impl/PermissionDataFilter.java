package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Permission;
import com.dianrong.common.uniauth.server.data.entity.PermissionExample;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * . 权限的数据过滤处理实现.
 *
 * @author wanglin
 */
@Service("permissionDataFilter")
public class PermissionDataFilter extends CurrentAbstractDataFilter<Permission> {

  @Autowired
  private PermissionMapper permissionMapper;

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.permission");
  }

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    PermissionExample condition = new PermissionExample();
    PermissionExample.Criteria criteria = condition.createCriteria();

    criteria.andStatusEqualTo(AppConstants.STATUS_ENABLED).andTenancyIdEqualTo(getTenancyId());
    // 构造查询条件
    for (FilterData fd : equalsField) {
      switch (fd.getType()) {
        case FIELD_TYPE_ID:
          criteria.andIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_VALUE:
          criteria.andValueEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_PERM_TYPE_ID:
          criteria.andPermTypeIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_DOMAIN_ID:
          criteria.andDomainIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
          break;
        default:
          break;
      }
    }
    // 查询
    List<Permission> permissiones = permissionMapper.selectByExample(condition);
    if (permissiones != null && !permissiones.isEmpty()) {
      return true;
    }
    return false;
  }

  @Override
  protected Permission getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "permissionId");
    PermissionExample condition = new PermissionExample();
    condition.createCriteria().andIdEqualTo(id).andStatusEqualTo(AppConstants.STATUS_ENABLED);
    List<Permission> selectByExample = permissionMapper.selectByExample(condition);
    if (selectByExample != null && !selectByExample.isEmpty()) {
      return selectByExample.get(0);
    }
    return null;
  }
}
