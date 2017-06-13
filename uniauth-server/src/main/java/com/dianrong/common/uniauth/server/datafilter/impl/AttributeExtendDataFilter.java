package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtendExample;
import com.dianrong.common.uniauth.server.data.mapper.AttributeExtendMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 过滤处理扩展属性.
 *
 * @author wanglin
 */
@Service("attributeExtendDataFilter")
public class AttributeExtendDataFilter extends CurrentAbstractDataFilter<AttributeExtend> {

  @Autowired
  private AttributeExtendMapper attributeExtendMapper;

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.AttributeExtend");
  }

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    AttributeExtendExample condition = new AttributeExtendExample();
    AttributeExtendExample.Criteria criteria = condition.createCriteria();
    criteria.andTenancyIdEqualTo(getTenancyId());
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
    int count = attributeExtendMapper.countByExample(condition);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  protected AttributeExtend getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "AttributeExtendId");
    AttributeExtendExample condition = new AttributeExtendExample();
    condition.createCriteria().andIdEqualTo(TypeParseUtil.parseToLongFromObject(id));
    List<AttributeExtend> selectByExample = attributeExtendMapper.selectByExample(condition);
    if (selectByExample != null && !selectByExample.isEmpty()) {
      return selectByExample.get(0);
    }
    return null;
  }
}
