package com.dianrong.common.uniauth.server.datafilter;

/**
 * 枚举关键字段取值的类型.
 *
 * @author wanglin
 */
public enum FieldType {
  /**
   * 通过id去查找.
   */
  FIELD_TYPE_ID("id", "fieldTypeId"),

  /**
   * 通过code去查找.
   */
  FIELD_TYPE_CODE("code", "fieldTypeCode"),

  /**
   * 通过email去查找.
   */
  FIELD_TYPE_EMAIL("email", "fieldTypeEmail"),

  /**
   * . 通过phone去查找
   */
  FIELD_TYPE_PHONE("phone", "fieldTypePhone"),

  /**
   * 通过表的value字段来找.
   */
  FIELD_TYPE_VALUE("value", "fieldTypeValue"),

  /**
   * 通过表的cfg_key字段来找.
   */
  FIELD_TYPE_CFG_KEY("cfgKey", "fieldTypeCfgKey"),

  /**
   * type_id.
   */
  FIELD_TYPE_PERM_TYPE_ID("permTypeId", "fieldTypePermTypeId"),

  /**
   * domain_id.
   */
  FIELD_TYPE_DOMAIN_ID("domainId", "fieldTypeDomainId"),

  /**
   * tag_type_id.
   */
  FIELD_TYPE_TAG_TYPE_ID("tagTypeId", "fieldTypeTagTypeId"),

  /**
   * user_id.
   */
  FIELD_TYPE_USER_ID("userId", "fieldTypeUserId"),
  
  /**
   * grp_id.
   */
  FIELD_TYPE_GRP_ID("grpId", "fieldTypeGrpId"),

  /**
   * extend_id.
   */
  FIELD_TYPE_EXTEND_ID("extendId", "fieldTypeExtendId"),
  /**
   * roleTypeId.
   */
  FIELD_TYPE_ROLE_CODE_ID("roleCodeId", "fieldTypeRoleCodeId"),
  /**
   * name.
   */
  FIELD_TYPE_NAME("name", "fieldTypeName");

  // 字段名称
  private final String fieldName;

  // 描述
  private final String typeDes;

  private FieldType(String fieldName, String typeDes) {
    this.fieldName = fieldName;
    this.typeDes = typeDes;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getTypeDes() {
    return typeDes;
  }

  @Override
  public String toString() {
    return typeDes;
  }
}
