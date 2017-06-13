package com.dianrong.common.uniauth.common.server.cxf.server;

import org.springframework.core.Ordered;

/**
 * . cxf header 的消费 实现Ordered的接口，同一个key可以有多个实现， 由高优先级的覆盖低优先级(返回的order越小，优先级越大)，从而实现用户自定义的consumer覆盖
 * uniauth提供的默认实现
 *
 * @author wanglin
 */
public interface HeaderConsumer extends Ordered {

  /**
   * . the header key
   *
   * @return can not be null
   */
  public String key();

  /**
   * .
   *
   * @param value can not be null
   */
  public void consume(String value);
}
