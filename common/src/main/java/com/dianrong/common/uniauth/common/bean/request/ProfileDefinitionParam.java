package com.dianrong.common.uniauth.common.bean.request;

import java.util.Map;
import java.util.Set;

import lombok.ToString;

/**
 * Profile定义相关接口的请求参数.
 */
@ToString
public class ProfileDefinitionParam extends PageParam {

  private static final long serialVersionUID = -1765107973570952500L;

  /**
   * 主键Id.即ProfileId.
   */
  private Long id;

  /**
   * Profile的名称,可为中文.
   */
  private String name;

  /**
   * Profile的编码,整个租户唯一的. 返回的扩展属性值以其作为Profile的name返回. <br>
   * ProfileBo ｛<br>
   * salesBasicProfile: ｛... ｝ <br>
   * lenderSalesBasicProfile: ｛... ｝ <br>
   * lenderDirectSalesProfile: ｛... ｝ <br>
   * ｝ <br>
   * 其中proofileBo, salesBasicProfile, lenderSalesBasicProfile, lenderDirectSalesProfile等就为code<br>
   */
  private String code;

  /**
   * Profile的描述信息.
   */
  private String description;

  /**
   * Profile对应的扩展属性列表.扩展属性Code->AttributeExtend
   */
  private Map<String, AttributeExtendParam> attributes;

  /**
   * 子Profile Id列表.
   */
  private Set<Long> descendantProfileIds;

  public String getName() {
    return name;
  }

  public ProfileDefinitionParam setName(String name) {
    this.name = name;
    return this;
  }

  public String getCode() {
    return code;
  }

  public ProfileDefinitionParam setCode(String code) {
    this.code = code;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public ProfileDefinitionParam setDescription(String description) {
    this.description = description;
    return this;
  }
  
  public Long getId() {
    return id;
  }

  public ProfileDefinitionParam setId(Long id) {
    this.id = id;
    return this;
  }

  public Map<String, AttributeExtendParam> getAttributes() {
    return attributes;
  }

  public ProfileDefinitionParam setAttributes(Map<String, AttributeExtendParam> attributes) {
    this.attributes = attributes;
    return this;
  }

  public Set<Long> getDescendantProfileIds() {
    return descendantProfileIds;
  }

  public ProfileDefinitionParam setDescendantProfileIds(Set<Long> descendantProfileIds) {
    this.descendantProfileIds = descendantProfileIds;
    return this;
  }
}
