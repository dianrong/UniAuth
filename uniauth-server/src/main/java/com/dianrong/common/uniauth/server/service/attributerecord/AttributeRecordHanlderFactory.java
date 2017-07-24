package com.dianrong.common.uniauth.server.service.attributerecord;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordOperate;
import com.dianrong.common.uniauth.server.service.attributerecord.ExtendAttributeRecord.RecordType;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.NotSupportedTypeOperateException;
import com.dianrong.common.uniauth.server.service.attributerecord.handler.AttributeRecordHandler;
import com.dianrong.common.uniauth.server.service.attributerecord.handler.GrpAddAttributeHanlder;
import com.dianrong.common.uniauth.server.service.attributerecord.handler.GrpDeleteAttributeHanlder;
import com.dianrong.common.uniauth.server.service.attributerecord.handler.GrpUpdateAttributeHanlder;
import com.dianrong.common.uniauth.server.service.attributerecord.handler.UserAddAttributeHanlder;
import com.dianrong.common.uniauth.server.service.attributerecord.handler.UserDeleteAttributeHanlder;
import com.dianrong.common.uniauth.server.service.attributerecord.handler.UserUpdateAttributeHanlder;
import com.dianrong.common.uniauth.server.service.inner.GroupExtendValInnerService;
import com.dianrong.common.uniauth.server.service.inner.UserExtendValInnerService;
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
  
  @Autowired
  private UserExtendValInnerService userExtendValInnerService;

  @Autowired
  private GroupExtendValInnerService groupExtendValInnerService;

  /**
   * 缓存AttributeRecordHandler.
   */
  private final Map<TypeOperate, AttributeRecordHandler> caches = Maps.newHashMap();

  /**
   * 此种方式通过Spring的@Autowired自动注入.
   */
  public AttributeRecordHanlderFactory() {}

  /**
   * 构造器方式注入.
   */
  public AttributeRecordHanlderFactory(UserExtendValInnerService userExtendValInnerService,
      GroupExtendValInnerService groupExtendValInnerService) {
    this.setUserExtendValInnerService(userExtendValInnerService);
    this.setGroupExtendValInnerService(groupExtendValInnerService);
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
    caches.put(TypeOperate.build(RecordType.USER, RecordOperate.ADD),
        new UserAddAttributeHanlder(userExtendValInnerService));
    caches.put(TypeOperate.build(RecordType.USER, RecordOperate.UPDATE),
        new UserUpdateAttributeHanlder(userExtendValInnerService));
    caches.put(TypeOperate.build(RecordType.USER, RecordOperate.DELETE),
        new UserDeleteAttributeHanlder(userExtendValInnerService));
    caches.put(TypeOperate.build(RecordType.GROUP, RecordOperate.ADD),
        new GrpAddAttributeHanlder(groupExtendValInnerService));
    caches.put(TypeOperate.build(RecordType.GROUP, RecordOperate.UPDATE),
        new GrpUpdateAttributeHanlder(groupExtendValInnerService));
    caches.put(TypeOperate.build(RecordType.GROUP, RecordOperate.DELETE),
        new GrpDeleteAttributeHanlder(groupExtendValInnerService));
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    init();
  }

  public void setUserExtendValInnerService(UserExtendValInnerService userExtendValInnerService) {
    Assert.notNull(userExtendValInnerService);
    this.userExtendValInnerService = userExtendValInnerService;
  }

  public void setGroupExtendValInnerService(GroupExtendValInnerService groupExtendValInnerService) {
    Assert.notNull(groupExtendValInnerService);
    this.groupExtendValInnerService = groupExtendValInnerService;
  }
}
