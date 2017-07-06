package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserExample;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * . 用户的数据过滤处理实现.
 *
 * @author wanglin
 */
@Service("userDataFilter")
public class UserDataFilter extends CurrentAbstractDataFilter<User> {

  @Autowired
  private UserMapper userMapper;

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.user");
  }

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    UserExample condition = new UserExample();
    UserExample.Criteria criteria = condition.createCriteria();
    criteria.andStatusEqualTo(AppConstants.STATUS_ENABLED).andTenancyIdEqualTo(getTenancyId());
    // 构造查询条件
    for (FilterData fd : equalsField) {
      switch (fd.getType()) {
        case FIELD_TYPE_ID:
          criteria.andIdEqualTo(TypeParseUtil.parseToLongFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_EMAIL:
          criteria.andEmailEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_PHONE:
          criteria.andPhoneEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
          break;
        default:
          break;
      }
    }
    // 查询
    int count = userMapper.countByExample(condition);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  protected User getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "userId");
    UserExample condition = new UserExample();
    condition.createCriteria().andIdEqualTo(TypeParseUtil.parseToLongFromObject(id))
        .andStatusEqualTo(AppConstants.STATUS_ENABLED);
    List<User> selectByExample = userMapper.selectByExample(condition);
    if (selectByExample != null && !selectByExample.isEmpty()) {
      return selectByExample.get(0);
    }
    return null;
  }
}
