package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.data.entity.GrpAttributeRecords;
import com.dianrong.common.uniauth.server.data.mapper.GrpAttributeRecordsMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GrpAttributeRecordsInnerService {

  @Autowired
  private GrpAttributeRecordsMapper grpAttributeRecordsMapper;
  
  /**
   * 插入属性值修改记录.
   */
  @Transactional
  public int insert(GrpAttributeRecords record) {
    Assert.notNull(record);
    return grpAttributeRecordsMapper.insert(record);
  }
}
