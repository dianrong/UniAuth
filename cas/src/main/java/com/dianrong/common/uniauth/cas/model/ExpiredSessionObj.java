package com.dianrong.common.uniauth.cas.model;

import java.io.Serializable;

/**
 * 一个泛型Model,有过期的处理逻辑.
 */
public class ExpiredSessionObj<T extends Serializable> implements Serializable {

  private static final long serialVersionUID = -5592978188308898593L;

  /**
   * 时间戳.
   */
  private long startMillis;

  /**
   * 存活时间millis.
   */
  private long lifeMillis;

  /**
   * 内容.
   */
  private T content;

  /**
   * 构造函数.
   */
  public ExpiredSessionObj(T content, long lifeMilles) {
    this.content = content;
    this.startMillis = System.currentTimeMillis();
    this.lifeMillis = lifeMilles < 0 ? 0 : lifeMilles;
  }

  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }

  /**
   * 判断当前对象是否已过期.
   */
  public boolean isExpired() {
    long nowMillis = System.currentTimeMillis();
    return nowMillis - startMillis > lifeMillis;
  }

  /**
   * Build a new ExpiredSessionObj.
   *
   * @param content the content
   * @param lifeMillis the content alive milliseconds
   * @return a new ExpiredSessionObj
   */
  public static <E extends Serializable> ExpiredSessionObj<E> build(E content, long lifeMillis) {
    return new ExpiredSessionObj<E>(content, lifeMillis);
  }
}
