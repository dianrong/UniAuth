package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.GrpExample;
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
    GrpExample grpExample = constructGrpExample();
    GrpExample.Criteria criteria = grpExample.createCriteria();
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
    int count = grpMapper.countByExample(grpExample);
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
    GrpExample grpExample = constructGrpExample();
    grpExample.createCriteria().andIdEqualTo(id).andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTenancyIdEqualTo(getTenancyId());
    List<Grp> grpList = grpMapper.selectByExample(grpExample);
    if (ObjectUtil.IsNotEmptyOrNull(grpList)) {
      return grpList.get(0);
    }
    return null;
  }

  /**
   * 构造一个GrpExample.
   */
  private GrpExample constructGrpExample(){
    GrpExample grpExample = new GrpExample();
    grpExample.setGrpCode(TreeTypeHolder.get(TreeType.NORMAL).getRootCode());
    grpExample.setStatus(AppConstants.STATUS_ENABLED);
    grpExample.setTenancyId(getTenancyId());
    return grpExample;
  }
}
