package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.data.entity.UserAttributeRecords;
import com.dianrong.common.uniauth.server.data.mapper.UserAttributeRecordsMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAttributeRecordsInnerService {

  @Autowired
  private UserAttributeRecordsMapper userAttributeRecordsMapper;

  /**
   * 插入属性值修改记录.
   */
  @Transactional
  public int insert(UserAttributeRecords record) {
    Assert.notNull(record);
    return userAttributeRecordsMapper.insert(record);
  }
}
