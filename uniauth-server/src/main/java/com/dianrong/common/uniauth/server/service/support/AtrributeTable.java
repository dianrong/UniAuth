package com.dianrong.common.uniauth.server.service.support;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 定义扩展属性相关的Table.
 */
public enum AtrributeTable {
  USER("user"), USER_DETAIL("user_detail"), GRP("grp");
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

  private AtrributeTable(String tableName) {
    this.tableName = tableName;
  }

  public String getTableName() {
    return tableName;
  }
}
