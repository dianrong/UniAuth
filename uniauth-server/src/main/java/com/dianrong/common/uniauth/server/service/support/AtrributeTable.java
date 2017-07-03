package com.dianrong.common.uniauth.server.service.support;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 定义扩展属性相关的Table.
 */
public enum AtrributeTable {
  USER("user", "id"), USER_DETAIL("user_detail", "user_id"), GRP("grp", "id");
  private static final Set<AtrributeTable> USER_TABLE = Sets.newHashSet();
  private static final Set<AtrributeTable> GROUP_TABLE = Sets.newHashSet();

  static {
    USER_TABLE.add(USER);
    USER_TABLE.add(USER_DETAIL);
    GROUP_TABLE.add(GRP);
  }

  public static boolean isUserTable(AtrributeTable table) {
    return USER_TABLE.contains(table);
  }

  public static boolean isGrpTable(AtrributeTable table) {
    return GROUP_TABLE.contains(table);
  }

  private final String tableName;

  private final String identityFieldName;

  private AtrributeTable(String tableName, String identityFieldName) {
    this.tableName = tableName;
    this.identityFieldName = identityFieldName;
  }

  public String getTableName() {
    return tableName;
  }

  public String getIdentityFieldName() {
    return identityFieldName;
  }
}
