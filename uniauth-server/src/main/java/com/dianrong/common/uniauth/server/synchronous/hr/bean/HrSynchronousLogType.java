package com.dianrong.common.uniauth.server.synchronous.hr.bean;

/**
 * 日志类型.
 */
public enum HrSynchronousLogType {
  /**
   * 同步HR的数据.
   */
  SYNCHRONOUS_HR_DATA("Synchronous HR system data"),

  /**
   * 删除过期的文件.
   */
  DELETE_FTP_HR_EXPIRED_DATA("Delete expired synchronization files on the FTP server"),
  ;

  private String description;

  private HrSynchronousLogType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
