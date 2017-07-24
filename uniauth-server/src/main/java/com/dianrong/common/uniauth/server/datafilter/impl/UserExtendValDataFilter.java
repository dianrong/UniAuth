package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendValMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户扩展值数据过滤.
 *
 * @author wanglin
 */
@Service("userExtendValDataFilter")
public class UserExtendValDataFilter extends CurrentAbstractDataFilter<UserExtendVal> {

  @Autowired
  private UserExtendValMapper userExtendValMapper;

  @Override
  protected String getProcessTableName() {
    return UniBundle.getMsg("data.filter.table.name.userextendval");
  }

  @Override
  protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
    UserExtendValExample condition = new UserExtendValExample();
    UserExtendValExample.Criteria criteria = condition.createCriteria();
    criteria.andTenancyIdEqualTo(getTenancyId());
    // 构造查询条件
    for (FilterData fd : equalsField) {
      switch (fd.getType()) {
        case FIELD_TYPE_USER_ID:
          criteria.andUserIdEqualTo(TypeParseUtil.parseToLongFromObject(fd.getValue()));
          break;
        case FIELD_TYPE_EXTEND_ID:
          criteria.andExtendIdEqualTo(TypeParseUtil.parseToLongFromObject(fd.getValue()));
          break;
        default:
          break;
      }
    }
    int count = userExtendValMapper.countByExample(condition);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @Override
  protected UserExtendVal getEnableRecordByPrimaryKey(Integer id) {
    CheckEmpty.checkEmpty(id, "UserExtendValId");
    UserExtendValExample condtion = new UserExtendValExample();
    condtion.createCriteria().andIdEqualTo(TypeParseUtil.parseToLongFromObject(id));
    List<UserExtendVal> infoes = userExtendValMapper.selectByExample(condtion);
    if (infoes != null && infoes.size() > 0) {
      return infoes.get(0);
    }
    return null;
  }
}
