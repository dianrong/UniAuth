package com.dianrong.common.uniauth.server.synchronous.hr.bean;

import com.dianrong.common.uniauth.server.data.entity.HrLe;

/**
 * 公司实体文件分析结果.
 */
public class LegalEntityList extends AbstractAnaListResult<HrLe> {

  @Override
  public SynchronousFile synchronousFile() {
    return SynchronousFile.LE_UA;
  }
}
