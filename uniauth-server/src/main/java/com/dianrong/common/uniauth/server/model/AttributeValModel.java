package com.dianrong.common.uniauth.server.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 扩展属性与值的Model.
 * 
 * @author wanglin
 */
@ToString
@Setter
@Getter
public class AttributeValModel implements Serializable {

  private static final long serialVersionUID = 6726014542153028741L;

  private Long id;

  private String code;

  private String category;

  private String subcategory;

  private String description;

  private String value;
}
