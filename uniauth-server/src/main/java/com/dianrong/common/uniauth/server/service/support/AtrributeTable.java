package com.dianrong.common.uniauth.server.service.support;

/**
 * 定义扩展属性相关的Table.
 */
public enum AtrributeTable {
  USER("user"), 
  USER_DETAIL("user_detail"), 
  GRP("grp")
;
  private final String tableName;
  
  private AtrributeTable(String tableName) {
    this.tableName = tableName;
  }

  public String getTableName() {
    return tableName;
  }
}
