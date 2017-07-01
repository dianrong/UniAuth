package com.dianrong.common.uniauth.common.bean.request;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Profile值更新使用的参数Model.
 */
@ToString
@Getter
@Setter
public class ProfileParam extends Operator {

  private static final long serialVersionUID = -1765107973570952500L;

  /**
   * 更新的扩展属性值.AttributeCode:AttributeExtend
   */
  private Map<String, AttributeExtendParam> attributes;
}
