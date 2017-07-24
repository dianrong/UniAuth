package com.dianrong.common.uniauth.server.service.attributerecord.handler;

import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.service.attributerecord.AttributeValIdentity;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;

/**
 * 处理不同情况下的日志记录方式.
 */
public interface AttributeRecordHandler {

  /**
   * 在调用目标方法之前回调的方法.
   * 
   * @param valIdentity 唯一标识某一条属性记录,不能为空.
   * @return 扩展属性操作前的值.
   * @throws InvalidParameterTypeException 如果传入的identity和extendId类型不合法.
   */
  ExtendVal invokeTargetBefore(AttributeValIdentity valIdentity);

  /**
   * 在调用目标方法之后回调方法.
   * 
   * @param identity 唯一标识某一条属性记录,不能为空.
   * @param throwable 如果在调用目标方法的时候抛出的异常.
   * @param originalVal 在调用操作之前的值.有可能为空, 如果为Add操作.
   * @param 操作记录需要写入到数据库中的记录.
   * @throws InvalidParameterTypeException 如果传入的identity和extendId类型不合法.
   */
  AttributeRecords invokeTargetAfter(AttributeValIdentity identity, ExtendVal originalVal,
      Throwable throwable);
}
