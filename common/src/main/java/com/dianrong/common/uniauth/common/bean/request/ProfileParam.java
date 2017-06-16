package com.dianrong.common.uniauth.common.bean.request;

import java.util.Map;
import java.util.Set;

import lombok.ToString;

/**
 * Profile更新使用的参数Model.
 */
@ToString
public class ProfileParam extends Operator {

  private static final long serialVersionUID = -1765107973570952500L;

  /**
   * 在进行更新操作的时候,指定关注的扩展属性Code.Profile code->Profile attribute code.
   * 如果为null或空则代表关注对应Profile的所有扩展属性.
   */
  private Map<String, Set<String>> concernAttributeCodes;
  
  /**
   * 更新的扩展属性值.
   */
  private Map<String, Object> profile;
}
