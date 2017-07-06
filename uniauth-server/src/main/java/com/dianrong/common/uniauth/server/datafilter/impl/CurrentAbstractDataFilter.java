package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.UniBundle;
import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 目前阶段需要处理的一个比较固定的流程.
 * @author wanglin
 */
@Slf4j
public abstract class CurrentAbstractDataFilter<T> extends MultiTenancyCheck {

  @Override
  protected void doUpdateFieldsCheck(Integer id, FilterData... equalsField) {
    // 不处理
    if (equalsField == null || equalsField.length == 0) {
      return;
    }
    Object record = getEnableRecordByPrimaryKey(id);
    if (record != null) {
      // 默认是全部相等的
      boolean isEqual = true;
      // 如果数据信息没有改变 则不管
      for (FilterData fd : equalsField) {
        Object v1 = getObjectValue(record, fd.getType());
        Object v2 = fd.getValue();
        if (!ObjectUtil.objectEqualIgnoreCase(v1, v2)) {
          isEqual = false;
          break;
        }
      }
      if (isEqual) {
        return;
      }
    }
    // 查看是否存在其他的记录是该信息
    this.addFieldsCheck(FilterType.EXSIT_DATA, equalsField);
  }

  @Override
  protected void doAddFieldsCheck(FilterType ftype, FilterData... equalsField) {
    // 不处理
    if (equalsField == null || equalsField.length == 0) {
      return;
    }
    Assert.notNull(ftype);
    switch (ftype) {
      case EXSIT_DATA:
        if (multiFieldsDuplicateCheck(equalsField)) {
          throw new AppException(InfoName.VALIDATE_FAIL,
              UniBundle.getMsg("datafilter.data.mutilcondition.exsit.error", getProcessTableName(),
                  getFieldTypeKeyAndValue(equalsField)));
        }
        break;
      case NO_DATA:
        if (!multiFieldsDuplicateCheck(equalsField)) {
          throw new AppException(InfoName.VALIDATE_FAIL,
              UniBundle.getMsg("datafilter.data.mutilcondition.notexsit.error",
                  getProcessTableName(), getFieldTypeKeyAndValue(equalsField)));
        }
        break;
      default:
        break;
    }
  }

  /**
   * 获取描述符.
   * @param equalsField equalsField处理的字段
   */
  private String getFieldTypeKeyAndValue(FilterData... equalsField) {
    if (equalsField == null || equalsField.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    String filterKeyVal = "=";
    String filterEle = ";";
    for (FilterData fd : equalsField) {
      if (sb.toString().length() > 0) {
        sb.append(filterEle);
      }
      sb.append(fd.getType().getFieldName());
      sb.append(filterKeyVal);
      sb.append(StringUtil.getObjectStr(fd.getValue()));
    }
    return sb.toString();
  }

  /**
   * 从Role中获取数据.
   */
  protected Object getObjectValue(Object obj, FieldType type) {
    if (obj == null) {
      return null;
    }
    try {
      if (type == null) {
        return null;
      }
      Field field = ReflectionUtils.findField(obj.getClass(), type.getFieldName());
      if (field == null) {
        return null;
      }
      field.setAccessible(true);
      // 通过反射获取值
      return ReflectionUtils.getField(field, obj);
    } catch (Exception ex) {
      log.warn("failed get object filed", ex);
    }
    return null;
  }
  
  /**
   * 处理查询数据中是否存在对应的字段相等的情况.
   * @param equalsField 字段列表,一定不为空.
   */
  protected abstract boolean multiFieldsDuplicateCheck(FilterData... equalsField);

  /**
   * 获取描述的表的名字.
   * @return 表名
   */
  protected abstract String getProcessTableName();

  /**
   * 根据主键id获取一个启用状态的数据.
   * @param id primary key
   * @return record
   */
  protected abstract T getEnableRecordByPrimaryKey(Integer id);
}
