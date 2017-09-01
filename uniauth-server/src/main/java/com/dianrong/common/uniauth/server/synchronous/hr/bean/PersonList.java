package com.dianrong.common.uniauth.server.synchronous.hr.bean;

import com.dianrong.common.uniauth.server.data.entity.HrPerson;

/**
 * 员工文件分析结果.
 */
public class PersonList extends AbstractAnaListResult<HrPerson> {
  @Override public SynchronousFile synchronousFile() {
    return SynchronousFile.PERSON_UA;
  }
}
