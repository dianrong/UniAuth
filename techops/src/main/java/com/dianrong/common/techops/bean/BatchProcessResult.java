package com.dianrong.common.techops.bean;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 批量处理的结果.
 * 
 * @author wanglin
 *
 */
public class BatchProcessResult {

  /**
   * 处理成功的结果.
   */
  private Set<String> successes;

  /**
   * 处理失败的结果.
   */
  private Set<Failure> errors;

  /**
   * 构造函数.
   */
  public BatchProcessResult() {
    this.successes = Sets.newHashSet();
    this.errors = Sets.newHashSet();
  }

  public Set<String> getSuccesses() {
    return successes;
  }

  public void setSuccesses(Set<String> successes) {
    this.successes = successes;
  }

  public Set<Failure> getErrors() {
    return errors;
  }

  public void setErrors(Set<Failure> errors) {
    this.errors = errors;
  }

  @Override
  public String toString() {
    return "BatchProcessResult [successes=" + successes + ", errors=" + errors + "]";
  }

  /**
   * 处理失败的结果对象.
   */
  public static class Failure {

    private String identity;

    private String msg;

    /**
     * 构造批量操作失败结果.
     */
    public static Failure build(String identity, String msg) {
      Failure failure = new Failure();
      failure.setIdentity(identity);
      failure.setMsg(msg);
      return failure;
    }

    public String getIdentity() {
      return identity;
    }

    public void setIdentity(String identity) {
      this.identity = identity;
    }

    public String getMsg() {
      return msg;
    }

    public void setMsg(String msg) {
      this.msg = msg;
    }

    @Override
    public String toString() {
      return "Failure [identity=" + identity + ", msg=" + msg + "]";
    }
  }
}
