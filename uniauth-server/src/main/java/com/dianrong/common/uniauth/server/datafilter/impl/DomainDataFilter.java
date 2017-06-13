package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.DomainExample;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 域名的数据过滤处理实现.
 *
 * @author wanglin
 */
@Service("domainDataFilter")
public class DomainDataFilter extends CurrentAbstractDataFilter<Domain> {

  @Autowired
  private DomainMapper domainMapper;

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    DomainExample condition = new DomainExample();
    DomainExample.Criteria criteria = condition.createCriteria();
    criteria.andStatusEqualTo(AppConstants.STATUS_ENABLED);
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
    int count = domainMapper.countByExample(condition);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.domain");
  }

  @Override
  protected Domain getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "domainId");
    DomainExample condition = new DomainExample();
    condition.createCriteria().andIdEqualTo(id).andStatusEqualTo(AppConstants.STATUS_ENABLED);
    List<Domain> selectByExample = domainMapper.selectByExample(condition);
    if (selectByExample != null && !selectByExample.isEmpty()) {
      return selectByExample.get(0);
    }
    return null;
  }
}
