package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Tag;
import com.dianrong.common.uniauth.server.data.entity.TagExample;
import com.dianrong.common.uniauth.server.data.mapper.TagMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * tag 数据过滤的接口.
 *
 * @author wanglin
 */
@Service("tagDataFilter")
public class TagDataFilter extends CurrentAbstractDataFilter<Tag> {

  @Autowired
  private TagMapper tagMapper;

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.tag");
  }

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    TagExample condition = new TagExample();
    TagExample.Criteria criteria = condition.createCriteria();

    criteria.andStatusEqualTo(AppConstants.STATUS_ENABLED).andTenancyIdEqualTo(getTenancyId());
    // 构造查询条件
    for (FilterData fd : equalsField) {
      switch (fd.getType()) {
        case FIELD_TYPE_CODE:
          criteria.andCodeEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_TAG_TYPE_ID:
          criteria.andTagTypeIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
          break;
        default:
          break;
      }
    }
    // 查询
    int count = tagMapper.countByExample(condition);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  protected Tag getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "tagId");
    TagExample condition = new TagExample();
    condition.createCriteria().andIdEqualTo(id).andStatusEqualTo(AppConstants.STATUS_ENABLED);
    List<Tag> selectByExample = tagMapper.selectByExample(condition);

    if (selectByExample != null && !selectByExample.isEmpty()) {
      return selectByExample.get(0);
    }
    return null;
  }
}
