package com.dianrong.common.uniauth.server.datafilter;

/**
 * . 数据过滤的类型枚举
 *
 * @author wanglin
 */
public enum FilterType {
  /**
   * 数据如果存在,则报错.
   */
  EXSIT_DATA,
  /**
   * 如果数据不存在,则报错.
   */
  NO_DATA
}
