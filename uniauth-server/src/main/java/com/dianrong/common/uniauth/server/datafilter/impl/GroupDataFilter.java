package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.GrpExample;
import com.dianrong.common.uniauth.server.data.entity.GrpPathGrpExample;
import com.dianrong.common.uniauth.server.data.mapper.GrpMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpPathMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.support.tree.TreeType;
import com.dianrong.common.uniauth.server.support.tree.TreeTypeHolder;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组的数据过滤处理实现.
 *
 * @author wanglin
 */
@Service("groupDataFilter")
public class GroupDataFilter extends CurrentAbstractDataFilter<Grp> {

  @Autowired
  private GrpMapper grpMapper;

  @Autowired
  private GrpPathMapper grpPathMapper;

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    GrpPathGrpExample condition = new GrpPathGrpExample();
    treeTypeCheck(condition);
    GrpExample.Criteria criteria = condition.createCriteria();
    criteria.andStatusEqualTo(AppConstants.STATUS_ENABLED).andTenancyIdEqualTo(getTenancyId());
    // 构造查询条件
    for (FilterData fd : equalsField) {
      switch (fd.getType()) {
        case FIELD_TYPE_ID:
          criteria.andIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_CODE:
          criteria.andCodeEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
          break;
        default:
          break;
      }
    }
    // 查询
    int count = grpMapper.countByExample(condition);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.group");
  }

  @Override
  protected Grp getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "grpId");
    GrpPathGrpExample condition = new GrpPathGrpExample();
    condition.createCriteria().andIdEqualTo(id).andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTenancyIdEqualTo(getTenancyId());
    treeTypeCheck(condition);
    List<Grp> grpList = grpMapper.selectByExample(condition);
    if (ObjectUtil.IsNotEmptyOrNull(grpList)) {
      return grpList.get(0);
    }
    return null;
  }

  /**
   * 加入TreeType检测的逻辑.
   */
  private void treeTypeCheck(GrpPathGrpExample condition){
    TreeType type = TreeTypeHolder.get();
    if (type == null) {
      return;
    }
    condition.setGrpCode(type.getRootCode());
    condition.setStatus(AppConstants.STATUS_ENABLED);
    condition.setTenancyId(getTenancyId());
  }
}
