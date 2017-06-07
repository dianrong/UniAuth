package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.server.data.entity.Cfg;
import com.dianrong.common.uniauth.server.data.entity.CfgExample;
import com.dianrong.common.uniauth.server.data.mapper.CfgMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配置的数据过滤处理实现.
 *
 * @author wanglin
 */
@Service("cfgDataFilter")
public class CfgDataFilter extends CurrentAbstractDataFilter<Cfg> {

  @Autowired
  private CfgMapper cfgMapper;

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    CfgExample condition = new CfgExample();
    CfgExample.Criteria criteria = condition.createCriteria();
    // 构造查询条件
    for (FilterData fd : equalsField) {
      switch (fd.getType()) {
        case FIELD_TYPE_CFG_KEY:
          criteria.andCfgKeyEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
          break;
        default:
          break;
      }
    }
    // 查询
    int count = cfgMapper.countByExample(condition);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.cfg");
  }

  @Override
  protected Cfg getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "cfgId");
    CfgExample condition = new CfgExample();
    condition.createCriteria().andIdEqualTo(id);
    List<Cfg> selectByExample = cfgMapper.selectByExample(condition);
    if (selectByExample != null && !selectByExample.isEmpty()) {
      return selectByExample.get(0);
    }
    return null;
  }
}
