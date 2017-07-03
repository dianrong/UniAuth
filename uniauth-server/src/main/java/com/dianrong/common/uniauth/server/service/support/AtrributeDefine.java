package com.dianrong.common.uniauth.server.service.support;

import com.google.common.collect.Sets;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户扩展字段定义.
 */
public enum AtrributeDefine {

  // Table user
  USER_NAME(AtrributeTable.USER, "name"), 
  EMAIL(AtrributeTable.USER, "email", false), 
  PHONE(AtrributeTable.USER, "phone", false), 
  STAFF_NO(AtrributeTable.USER, "staff_no", false), 
  LDAP_DN(AtrributeTable.USER, "ldap_dn", false), 
  USER_GUID(AtrributeTable.USER, "user_guid", false),

  // Table user_detail
  FIRST_NAME(AtrributeTable.USER_DETAIL, "first_name"), 
  LAST_NAME(AtrributeTable.USER_DETAIL, "last_name"), 
  DISPLAY_NAME(AtrributeTable.USER_DETAIL, "display_name"), 
  NICK_NAME( AtrributeTable.USER_DETAIL, "nick_name"), 
  IDENTITY_CARD(AtrributeTable.USER_DETAIL, "identity_card"), 
  MOTTO(AtrributeTable.USER_DETAIL, "motto"), 
  IMAGE(AtrributeTable.USER_DETAIL, "image"), 
  SSN(AtrributeTable.USER_DETAIL, "ssn"), 
  WEIBO(AtrributeTable.USER_DETAIL, "weibo"), 
  WECHAT_NO( AtrributeTable.USER_DETAIL, "wechat_no"), 
  ADDRESS(AtrributeTable.USER_DETAIL, "address"), 
  BIRTHDAY(AtrributeTable.USER_DETAIL, "birthday"), 
  GENDER(AtrributeTable.USER_DETAIL, "gender"), 
  POSITION(AtrributeTable.USER_DETAIL, "position"), 
  LAST_POSITION_MODIFY_DATE(AtrributeTable.USER_DETAIL, "last_position_modify_date"), 
  DEPARTMENT(AtrributeTable.USER_DETAIL, "department"), 
  TITLE(AtrributeTable.USER_DETAIL, "title"), 
  AID(AtrributeTable.USER_DETAIL, "aid"), 
  ENTRY_DATE(AtrributeTable.USER_DETAIL, "entry_date"), 
  LEAVE_DATE( AtrributeTable.USER_DETAIL, "leave_date"),

  // Table group
  GROUP_NAME(AtrributeTable.GRP, "name"), 
  GROUP_CODE(AtrributeTable.GRP, "code", false), 
  GROUP_DESCRiPTION(AtrributeTable.GRP, "description"),;

  /**
   * 系统定义表中的User的属性集合.
   */
  private static final Set<String> SYSTEM_DEFINE_USE_ATTRIBUTES = Sets.newHashSet();

  /**
   * 系统定义表中的Group的属性集合.
   */
  private static final Set<String> SYSTEM_DEFINE_GROUP_ATTRIBUTES = Sets.newHashSet();

  static {
    for (AtrributeDefine ad : AtrributeDefine.values()) {
      if (AtrributeTable.isUserTable(ad.getDefineTable())) {
        SYSTEM_DEFINE_USE_ATTRIBUTES.add(ad.getAttributeCode());
      }
      if (AtrributeTable.isGrpTable(ad.getDefineTable())) {
        SYSTEM_DEFINE_GROUP_ATTRIBUTES.add(ad.getAttributeCode());
      }
    }
  }

  /**
   * 判断传入的属性Code是否属于系统定义的User属性.
   */
  public static final boolean isSystemDefineUserAttribute(String attributeCode) {
    if (StringUtils.isBlank(attributeCode)) {
      return false;
    }
    return SYSTEM_DEFINE_USE_ATTRIBUTES.contains(attributeCode);
  }

  /**
   * 判断传入的属性Code是否属于系统定义的Group属性.
   */
  public static final boolean isSystemDefineGroupAttribute(String attributeCode) {
    if (StringUtils.isBlank(attributeCode)) {
      return false;
    }
    return SYSTEM_DEFINE_GROUP_ATTRIBUTES.contains(attributeCode);
  }

  private AtrributeDefine(AtrributeTable defineTable, String fieldName) {
    this(defineTable, fieldName, fieldName, true, true);
  }

  private AtrributeDefine(AtrributeTable defineTable, String fieldName, boolean writable) {
    this(defineTable, fieldName, fieldName, true, writable);
  }

  private AtrributeDefine(AtrributeTable defineTable, String attributeCode, String fieldName,
      boolean readable, boolean writable) {
    this.defineTable = defineTable;
    this.attributeCode = attributeCode;
    this.fieldName = fieldName;
    this.readable = readable;
    this.writable = writable;
  }

  /**
   * 定义扩展属性所在的表.
   */
  private final AtrributeTable defineTable;

  /**
   * 扩展属性的code,默认与fieldName是一致的.
   */
  private final String attributeCode;

  /**
   * 在表中字段对应的名称.
   */
  private final String fieldName;

  /**
   * 该属性是否可通过profile读取到..
   */
  private final boolean readable;

  /**
   * 该属性是否可通过profile修改.某些属性是不允许通过profile修改的,需要通过其他接口,以便进行相应的校验处理.
   */
  private final boolean writable;

  public AtrributeTable getDefineTable() {
    return defineTable;
  }

  public String getAttributeCode() {
    return attributeCode;
  }

  public String getFieldName() {
    return fieldName;
  }

  public boolean isReadable() {
    return readable;
  }

  public boolean isWritable() {
    return writable;
  }
}
