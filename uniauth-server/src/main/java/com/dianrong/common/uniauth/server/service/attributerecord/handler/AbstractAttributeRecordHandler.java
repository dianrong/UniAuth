package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.AttributeValIdentity;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理不同情况下的日志记录方式.
 */
@Slf4j
public abstract class AbstractAttributeRecordHandler implements AttributeRecordHandler {

  @Override
  public AttributeRecords invokeTargetAfter(AttributeValIdentity valIdentity, ExtendVal originalVal,
      Throwable throwable) {
    if (throwable != null) {
      log.debug("attribute value process error!", throwable);
      return null;
    }
    return doInvokeTargetAfter(valIdentity, originalVal);
  }

  /**
   * 具体处理业务方法.
   * 
   * @param valIdentity 唯一标识一条记录,不能为空.
   */
  protected abstract AttributeRecords doInvokeTargetAfter(AttributeValIdentity valIdentity,
      ExtendVal originalVal);
}
