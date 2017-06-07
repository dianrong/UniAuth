package com.dianrong.common.uniauth.server.service.cache;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.GrpExample;
import com.dianrong.common.uniauth.server.data.entity.GrpExample.Criteria;
import com.dianrong.common.uniauth.server.data.mapper.GrpMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = {"grp"})
public class GrpCache {

  @Autowired
  private GrpMapper grpMapper;

  /**
   * 根据组code来缓存组信息.
   *
   * @param grpCode 组编码 不能为空
   * @param tenancyId 租户id 不能为空
   * @return 组信息
   */
  @Cacheable(key = "#tenancyId + ':' + #grpCode.toUpperCase()")
  public Grp getGrpInfoByCode(String grpCode, Long tenancyId) {
    Assert.notNull(grpCode);
    Assert.notNull(tenancyId);
    GrpExample grpExample = new GrpExample();
    Criteria grpCriteria = grpExample.createCriteria();
    grpCriteria.andCodeEqualTo(grpCode).andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTenancyIdEqualTo(tenancyId);
    List<Grp> grpList = grpMapper.selectByExample(grpExample);
    if (grpList == null || grpList.isEmpty()) {
      return null;
    }
    return grpList.get(0);
  }
}
