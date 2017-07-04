package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;

import lombok.extern.slf4j.Slf4j;

/**
 *处理不同情况下的日志记录方式.
 */
@Slf4j
public abstract class AbstractAttributeRecordHandler implements AttributeRecordHandler {
  
  public AttributeRecords invokeTargetAfter(Object identity, Object extendId, ExtendVal originalVal, Throwable throwable) {
    if (throwable != null) {
      log.debug("attribute value process error!", throwable);
      return null;
    }
    return doInvokeTargetAfter(identity, extendId, originalVal);
  }
  
  /**
   * 具体处理业务方法.
   */
  protected abstract AttributeRecords doInvokeTargetAfter(Object identity, Object extendId, ExtendVal originalVal);
}
