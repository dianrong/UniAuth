package com.dianrong.common.uniauth.common.server.cxf.client;

import org.springframework.core.Ordered;

/**
 * . 生成cxf的header值 实现Ordered的接口，同一个key可以有多个实现， 由高优先级的覆盖低优先级(返回的order越小，优先级越大)，从而实现用户自定义的producer覆盖
 * uniauth提供的默认实现
 *
 * @author wanglin
 */
public interface HeaderProducer extends Ordered {

  /**
   * . the header key
   *
   * @return can not be null
   */
  public String key();

  /**
   * . the header value
   *
   * @return if null, will not send this header
   */
  public String produce();
}
