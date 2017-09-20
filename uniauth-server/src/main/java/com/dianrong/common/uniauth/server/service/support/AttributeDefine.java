package com.dianrong.common.uniauth.server.service.support;

import com.dianrong.common.uniauth.server.service.attribute.transalate.AttributeTypeTranslator;
import com.dianrong.common.uniauth.server.service.attribute.transalate.AttributeTypeTranslatorFactory;
import com.dianrong.common.uniauth.server.service.attribute.transalate.type.Email;
import com.dianrong.common.uniauth.server.service.attribute.transalate.type.Phone;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.util.Map;

/**
 * 用户扩展字段定义.
 */
public enum AttributeDefine {

  // Table user
  USER_NAME(AttributeTable.USER, "name"),
  EMAIL(AttributeTable.USER, "email", true, Email.class),
  PHONE(AttributeTable.USER, "phone", true, Phone.class),
  STAFF_NO(AttributeTable.USER, "staff_no", true),
  LDAP_DN(AttributeTable.USER, "ldap_dn", true),
  USER_GUID(AttributeTable.USER, "user_guid", true),

  // Table user_detail
  FIRST_NAME(AttributeTable.USER_DETAIL, "first_name"),
  LAST_NAME(AttributeTable.USER_DETAIL, "last_name"),
  DISPLAY_NAME(AttributeTable.USER_DETAIL, "display_name"),
  NICK_NAME(AttributeTable.USER_DETAIL, "nick_name"),
  IDENTITY_NO(AttributeTable.USER_DETAIL, "identity_no"),
  MOTTO(AttributeTable.USER_DETAIL, "motto"),
  IMAGE(AttributeTable.USER_DETAIL, "image"),
  SSN(AttributeTable.USER_DETAIL, "ssn"),
  WEIBO(AttributeTable.USER_DETAIL, "weibo"),
  WECHAT_NO(AttributeTable.USER_DETAIL, "wechat_no"),
  ADDRESS(AttributeTable.USER_DETAIL, "address"),
  BIRTHDAY(AttributeTable.USER_DETAIL, "birthday", Date.class),
  GENDER(AttributeTable.USER_DETAIL, "gender"),
  POSITION(AttributeTable.USER_DETAIL, "position"),
  LAST_POSITION_MODIFY_DATE(AttributeTable.USER_DETAIL, "last_position_modify_date", Date.class),
  DEPARTMENT(AttributeTable.USER_DETAIL, "department"),
  TITLE(AttributeTable.USER_DETAIL, "title"),
  AID(AttributeTable.USER_DETAIL, "aid", Long.class),
  ENTRY_DATE(AttributeTable.USER_DETAIL, "entry_date", Date.class),
  LEAVE_DATE(AttributeTable.USER_DETAIL, "leave_date", Date.class),

  // Table group
  GROUP_NAME(AttributeTable.GRP, "name"),
  GROUP_CODE(AttributeTable.GRP, "code", true),
  GROUP_DESCRIPTION(AttributeTable.GRP, "description");

  public static final String UN_READER_VALUE = "No permission to read";

  /**
   * 系统定义表中的User的属性编码与定义的Map.
   */
  private static final Map<String, AttributeDefine> SYSTEM_DEFINE_USE_ATTRIBUTES_MAP =
      Maps.newHashMap();

  /**
   * 系统定义表中的Group的属性编码与定义的Map.
   */
  private static final Map<String, AttributeDefine> SYSTEM_DEFINE_GROUP_ATTRIBUTES_MAP =
      Maps.newHashMap();
  

  static {
    for (AttributeDefine ad : AttributeDefine.values()) {
      if (AttributeTable.isUserTable(ad.getDefineTable())) {
        SYSTEM_DEFINE_USE_ATTRIBUTES_MAP.put(ad.getAttributeCode().trim().toLowerCase(), ad);
      }
      if (AttributeTable.isGrpTable(ad.getDefineTable())) {
        SYSTEM_DEFINE_GROUP_ATTRIBUTES_MAP.put(ad.getAttributeCode().trim().toLowerCase(), ad);
      }
    }
  }

  /**
   * 根据传入的属性Code返回系统定义的用户属性定义.
   * @return 如果为空,则传入的attributeCode不是系统预定义的.
   */
  public static final AttributeDefine getSystemDefineUserAttribute(String attributeCode) {
    if (StringUtils.isBlank(attributeCode)) {
      return null;
    }
    return SYSTEM_DEFINE_USE_ATTRIBUTES_MAP.get(attributeCode.trim().toLowerCase());
  }

  /**
   * 判断传入的属性Code是否属于系统定义的Group属性.
   */
  public static final AttributeDefine getSystemDefineGroupAttribute(String attributeCode) {
    if (StringUtils.isBlank(attributeCode)) {
      return null;
    }
    return SYSTEM_DEFINE_GROUP_ATTRIBUTES_MAP.get(attributeCode.trim().toLowerCase());
  }

  /**
   * 定义扩展属性所在的表.
   */
  private final AttributeTable defineTable;

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

  /**
   * 是否是唯一的属性.
   */
  private final boolean isUniqueField;

  private AttributeDefine(AttributeTable defineTable, String fieldName, Class<?> clz) {
    this(defineTable, fieldName, fieldName, true, true, false, clz);
  }

  private AttributeDefine(AttributeTable defineTable, String fieldName, boolean isUniqueField,
      Class<?> clz) {
    this(defineTable, fieldName, fieldName, true, true, isUniqueField, clz);
  }
  
  private AttributeDefine(AttributeTable defineTable, String fieldName) {
    this(defineTable, fieldName, fieldName, true, true, false, String.class);
  }

  private AttributeDefine(AttributeTable defineTable, String fieldName, boolean isUniqueField) {
    this(defineTable, fieldName, fieldName, true, true, isUniqueField, String.class);
  }

  private AttributeDefine(AttributeTable defineTable, String attributeCode, String fieldName,
      boolean readable, boolean writable, boolean isUniqueField, Class<?> fieldType) {
    this.defineTable = defineTable;
    this.attributeCode = attributeCode;
    this.fieldName = fieldName;
    this.readable = readable;
    this.writable = writable;
    this.fieldType = fieldType;
    this.isUniqueField = isUniqueField;
  }


  /**
   * 属性的java类型.
   */
  private final Class<?> fieldType;

  public Class<?> getFieldType() {
    return fieldType;
  }

  public AttributeTable getDefineTable() {
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

  public boolean isUniqueField() {
    return isUniqueField;
  }

  public AttributeTypeTranslator getTypeTranslator() {
    return AttributeTypeTranslatorFactory.getTranslator(this.fieldType);
  }
}
