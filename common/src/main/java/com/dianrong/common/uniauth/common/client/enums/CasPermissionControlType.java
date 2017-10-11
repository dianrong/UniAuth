package com.dianrong.common.uniauth.common.client.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * Uniauth的权限控制类型定义.
 */
public enum CasPermissionControlType {
  // default type
  URI_PATTERN("URI_PATTERN", "URI_PATTERN"),
  // 正则类型
  REGULAR_PATTERN("REGULAR_PATTERN", "REGULAR_PATTERN"),
  // 启用所有
  ALL("ALL", "URI_PATTERN", "REGULAR_PATTERN"),
  // 一种都不启用
  NONE("NONE");
  private final String typeStr;
  private final Set<String> supportTypes;

  private CasPermissionControlType(String type, String... types) {
    Assert.notNull(type);
    this.typeStr = type;
    this.supportTypes = new HashSet<String>(Arrays.asList(types));
  }

  public String getTypeStr() {
    return typeStr;
  }

  public boolean support(String type) {
    return supportTypes.contains(type);
  }

  /**
   * get CasPermissionControlType all type string, split with ,
   *
   * @return types string. eg. ALL, NONE...
   */
  public static String allType() {
    StringBuilder sb = new StringBuilder();
    int index = 0;
    for (CasPermissionControlType type : CasPermissionControlType.values()) {
      if (index > 0) {
        sb.append(", ");
      }
      sb.append(type.getTypeStr());
      index++;
    }
    return sb.toString();
  }
}
