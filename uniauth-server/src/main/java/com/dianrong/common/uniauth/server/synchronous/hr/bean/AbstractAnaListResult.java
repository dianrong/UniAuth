package com.dianrong.common.uniauth.server.synchronous.hr.bean;

import com.dianrong.common.uniauth.common.util.Assert;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * AnaListResult的模板方法.
 */
public abstract class AbstractAnaListResult<T> implements AnaListResult<T> {

  /**
   * 结果List.
   */
  private List<T> result = Lists.newArrayList();

  private String fileName;

  @Override
  public List<T> content() {
    return this.result;
  }

  @Override
  public void add(T t) {
    Assert.notNull(t);
    this.result.add(t);
  }

  @Override
  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
