package com.dianrong.common.uniauth.server.service.attributerecord;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.data.mapper.GrpAttributeRecordsMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserAttributeRecordsMapper;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.NotSupportedTypeOperateException;
import com.dianrong.common.uniauth.server.service.attributerecord.handler.AttributeRecordHandler;
import com.google.common.collect.Maps;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用于根据日志记录的不同场景,决定采用的handler.
 */

@Component
public final class AttributeRecordHanlderFactory implements InitializingBean {

  /**
   * 缓存AttributeRecordHandler.
   */
  private final Map<TypeOperate, AttributeRecordHandler> caches = Maps.newHashMap();

  @Autowired
  private GrpAttributeRecordsMapper grpAttributeRecordsMapper;

  @Autowired
  private UserAttributeRecordsMapper userAttributeRecordsMapper;

  /**
   * 此种方式通过Spring的autowird自动注入.
   */
  public AttributeRecordHanlderFactory() {}

  /**
   * 通过代码配置的方式.
   */
  public AttributeRecordHanlderFactory(GrpAttributeRecordsMapper grpAttributeRecordsMapper,
      UserAttributeRecordsMapper userAttributeRecordsMapper) {
    this.setGrpAttributeRecordsMapper(grpAttributeRecordsMapper);
    this.setUserAttributeRecordsMapper(userAttributeRecordsMapper);
  }

  /**
   * 根据传入的typeOperate获取对应当的处理handler.
   * 
   * @throws NotSupportedTypeOperateException 如果传入的typeOperate不支持.
   */
  public AttributeRecordHandler getHandler(TypeOperate typeOperate) {
    AttributeRecordHandler handler = caches.get(typeOperate);
    if (handler == null) {
      throw new NotSupportedTypeOperateException(typeOperate + " is not supported!");
    }
    return handler;
  }

  /**
   * 类的初始化方法.
   */
  public void init() {

  }

  @Override
  public void afterPropertiesSet() throws Exception {
    init();
  }

  public void setUserAttributeRecordsMapper(UserAttributeRecordsMapper userAttributeRecordsMapper) {
    Assert.notNull(userAttributeRecordsMapper);
    this.userAttributeRecordsMapper = userAttributeRecordsMapper;
  }

  public void setGrpAttributeRecordsMapper(GrpAttributeRecordsMapper grpAttributeRecordsMapper) {
    Assert.notNull(grpAttributeRecordsMapper);
    this.grpAttributeRecordsMapper = grpAttributeRecordsMapper;
  }
}
