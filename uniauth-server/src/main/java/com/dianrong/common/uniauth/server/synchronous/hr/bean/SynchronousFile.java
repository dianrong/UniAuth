package com.dianrong.common.uniauth.server.synchronous.hr.bean;

/**
 * 需要同步的表的表名.
 */
public enum SynchronousFile {
  DEPT_UA("DEPT_UA", "hr_dept"), JOB_UA("JOB_UA", "hr_job"), LE_UA("LE_UA", "hr_le"), PERSON_UA(
      "PERSON_UA", "hr_person");

  private String name;

  private String tableName;

  private SynchronousFile(String name, String tableName) {
    this.name = name;
    this.tableName = tableName;
  }

  public String getName() {
    return name;
  }

  public String getTableName() {
    return tableName;
  }
}
