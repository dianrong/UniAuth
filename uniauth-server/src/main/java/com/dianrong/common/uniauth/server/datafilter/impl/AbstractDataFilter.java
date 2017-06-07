package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;

/**
 * . 该抽象方法主要是加入了数据校验的开关-- dataFilterSwitch
 *
 * @author wanglin
 */
public abstract class AbstractDataFilter implements DataFilter {

  /**
   * . 数据过滤的开关变量
   */
  private boolean dataFilterSwitch = true;

  @Override
  public void addFieldCheck(FilterType ftype, FieldType type, Object fieldValue) {
    addFieldsCheck(ftype, FilterData.buildFilterData(type, fieldValue));
  }

  @Override
  public void updateFieldCheck(Integer id, FieldType type, Object fieldValue) {
    updateFieldsCheck(id, FilterData.buildFilterData(type, fieldValue));
  }

  /**
   * . 数据过滤并且伴随字段相等的情况。
   *
   * @param ftype 过滤的方式
   * @param equalsField 比较的字段以及值
   */
  @Override
  public void addFieldsCheck(FilterType ftype, FilterData... equalsField) {
    if (!isDataFilterSwitch()) {
      return;
    }
    doAddFieldsCheck(ftype, equalsField);
  }

  /**
   * . 判断数据是否重复并且伴随字段相等的情况。
   *
   * @param id keyid
   * @param equalsField 比较的字段以及值
   */
  @Override
  public void updateFieldsCheck(Integer id, FilterData... equalsField) {
    if (!isDataFilterSwitch()) {
      return;
    }
    doUpdateFieldsCheck(id, equalsField);
  }

  /**
   * . addFieldsCheck 的委托方法
   *
   * @param ftype 过滤的方式
   * @param equalsField 需要比较的字段以及值
   */
  protected abstract void doAddFieldsCheck(FilterType ftype, FilterData... equalsField);

  /**
   * . updateFieldsCheck的委托方法
   *
   * @param id keyid
   * @param equalsField 需要比较的字段以及值
   */
  protected abstract void doUpdateFieldsCheck(Integer id, FilterData... equalsField);

  public boolean isDataFilterSwitch() {
    return dataFilterSwitch;
  }

  public void setDataFilterSwitch(boolean dataFilterSwitch) {
    this.dataFilterSwitch = dataFilterSwitch;
  }
}
