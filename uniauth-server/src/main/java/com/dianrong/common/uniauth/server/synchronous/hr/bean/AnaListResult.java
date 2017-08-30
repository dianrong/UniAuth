package com.dianrong.common.uniauth.server.synchronous.hr.bean;

import java.util.List;

/**
 * 分析结果,其中内容为集合.
 */
public interface AnaListResult<T> {

  /**
   * 返回实际结果,有序.
   */
  List<T> content();

  /**
   * 添加分析结果.
   * @param t 添加的嗯对象.
   */
  void add(T t);

  /**
   * 处理的文件.
   */
  SynchronousFile synchronousFile();
}
