package com.dianrong.common.uniauth.server.synchronous.hr.bean;

import com.dianrong.common.uniauth.server.data.entity.HrDept;

/**
 * 部门信息分析结果.
 */
public class DepartmentList extends AbstractAnaListResult<HrDept> {

  @Override
  public SynchronousFile synchronousFile() {
    return SynchronousFile.DEPT_UA;
  }
}
