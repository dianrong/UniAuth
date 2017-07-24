package com.dianrong.common.uniauth.server.service.attributerecord;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 唯一标识某一条记录.
 */
@ToString
@Getter
@Setter
public class AttributeValIdentity {
  /**
   * 记录的主键id.
   */
  private Long primaryId;
  
  /**
   * 用户id或者组id.
   */
  private Object identity;
  
  /**
   * 扩展属性id.
   */
  private Long extendId;
}
