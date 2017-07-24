package com.dianrong.common.uniauth.server.service.support;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 定义扩展属性相关的Table.
 */
public enum AtrributeTable {
  USER("user", "id", false), USER_DETAIL("user_detail", "user_id", true), USER_WORK_RELATIONSHIP(
      "user_work_relationship", "user_id", true), GRP("grp", "id", false);
  private static final Set<AtrributeTable> USER_TABLE = Sets.newHashSet();
  private static final Set<AtrributeTable> GROUP_TABLE = Sets.newHashSet();

  static {
    USER_TABLE.add(USER);
    USER_TABLE.add(USER_DETAIL);
    USER_TABLE.add(USER_WORK_RELATIONSHIP);
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

  /**
   * 在进行扩展属性更新的时候是否需要在更新前检查一下记录是否存在.
   */
  private final boolean updateAttributeCheck;

  private AtrributeTable(String tableName, String identityFieldName, boolean updateAttributeCheck) {
    this.tableName = tableName;
    this.identityFieldName = identityFieldName;
    this.updateAttributeCheck = updateAttributeCheck;
  }

  public String getTableName() {
    return tableName;
  }

  public String getIdentityFieldName() {
    return identityFieldName;
  }

  public boolean isUpdateAttributeCheck() {
    return updateAttributeCheck;
  }
}
