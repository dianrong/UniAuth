package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Tenancy;
import com.dianrong.common.uniauth.server.data.entity.TenancyExample;
import com.dianrong.common.uniauth.server.data.mapper.TenancyMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * tenancy数据过滤的接口.
 *
 * @author wanglin
 */
@Service("tenancyDataFilter")
public class TenancyDataFilter extends CurrentAbstractDataFilter<Tenancy> {

  @Autowired
  private TenancyMapper tenancyMapper;

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.tenancy");
  }

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    TenancyExample condition = new TenancyExample();
    TenancyExample.Criteria criteria = condition.createCriteria();

    criteria.andStatusEqualTo(AppConstants.STATUS_ENABLED);
    // 构造查询条件
    for (FilterData fd : equalsField) {
      switch (fd.getType()) {
        case FIELD_TYPE_CODE:
          criteria.andCodeEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
          break;
        default:
          break;
      }
    }
    // 查询
    int count = tenancyMapper.countByExample(condition);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  protected Tenancy getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "tenancyId");
    TenancyExample condition = new TenancyExample();
    condition.createCriteria().andIdEqualTo(TypeParseUtil.parseToLongFromObject(id))
        .andStatusEqualTo(AppConstants.STATUS_ENABLED);
    List<Tenancy> selectByExample = tenancyMapper.selectByExample(condition);
    if (selectByExample != null && !selectByExample.isEmpty()) {
      return selectByExample.get(0);
    }
    return null;
  }
}
