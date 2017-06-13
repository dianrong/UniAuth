package com.dianrong.common.uniauth.server.datafilter;

/**
 * 用于处理数据过滤的data模型.
 *
 * @author wanglin
 */
public class FilterData {

  /**
   * . 过滤的字段类型
   */
  private FieldType type;

  /**
   * . 字段对应的数据
   */
  private Object value;

  /**
   * . 构造函数
   *
   * @param type type
   * @param value value
   */
  public FilterData(FieldType type, Object value) {
    this.type = type;
    this.value = value;
  }

  public FieldType getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }

  /**
   * 构造FilterData 对象.
   *
   * @param type type
   * @param value value
   * @return 构造完成的对象
   */
  public static FilterData buildFilterData(FieldType type, Object value) {
    return new FilterData(type, value);
  }
}
