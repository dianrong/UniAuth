package com.dianrong.common.uniauth.common.bean.request;

import java.util.Set;

import lombok.ToString;

/**
 * Profile定义相关接口的请求参数.
 */
@ToString
public class ProfileDefinitionParam extends PageParam {

  private static final long serialVersionUID = -1765107973570952500L;

  /**
   * Profile的名称,可为中文
   */
  private String name;
  
  /**
   * Profile的编码,整个租户唯一的. 返回的扩展属性值以其作为Profile的name返回. <br>
   * ProfileBo ｛<br>
   *     salesBasicProfile: ｛... ｝ <br>
   *     lenderSalesBasicProfile: ｛... ｝ <br>
   *     lenderDirectSalesProfile: ｛... ｝ <br>
   * ｝ <br>
   *   其中proofileBo, salesBasicProfile, lenderSalesBasicProfile, lenderDirectSalesProfile等就为code<br> 
   */
  private String code;
  
  /**
   * Profile的描述信息.
   */
  private String description;
  
  /**
   * Profile对应的扩展属性列表
   */
  private Set<String> attributes;
}
