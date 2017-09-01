package com.dianrong.common.uniauth.server.synchronous.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 外键检测失败的异常.
 */
public class ForeignKeyCheckFailureException extends UniauthCommonException{

  private static final long serialVersionUID = -2876152277599263785L;

  /**
   * 被检测的表.
   */
  private String checkedTableName;

  /**
   * 被检测的字段名.
   */
  private String checkedFieldName;

  /**
   * 被检测的字段值.
   */
  private Object checkedFieldVal;

  /**
   * 外连接的表.
   */
  private String linkedTableName;

  /**
   * 外连接的字段名.
   */
  private String linkedFieldName;

  /**
   * Define a empty method.
   */
  public ForeignKeyCheckFailureException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public ForeignKeyCheckFailureException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public ForeignKeyCheckFailureException(String msg, Throwable t) {
    super(msg, t);
  }

  public String getCheckedTableName() {
    return checkedTableName;
  }

  public void setCheckedTableName(String checkedTableName) {
    this.checkedTableName = checkedTableName;
  }

  public String getCheckedFieldName() {
    return checkedFieldName;
  }

  public void setCheckedFieldName(String checkedFieldName) {
    this.checkedFieldName = checkedFieldName;
  }

  public Object getCheckedFieldVal() {
    return checkedFieldVal;
  }

  public void setCheckedFieldVal(Object checkedFieldVal) {
    this.checkedFieldVal = checkedFieldVal;
  }

  public String getLinkedTableName() {
    return linkedTableName;
  }

  public void setLinkedTableName(String linkedTableName) {
    this.linkedTableName = linkedTableName;
  }

  public String getLinkedFieldName() {
    return linkedFieldName;
  }

  public void setLinkedFieldName(String linkedFieldName) {
    this.linkedFieldName = linkedFieldName;
  }
}
