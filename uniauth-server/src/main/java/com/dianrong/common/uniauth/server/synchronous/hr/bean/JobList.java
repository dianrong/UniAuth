package com.dianrong.common.uniauth.server.synchronous.hr.bean;

import com.dianrong.common.uniauth.server.data.entity.HrJob;

/**
 * Job分析结果.
 */
public class JobList extends AbstractAnaListResult<HrJob> {

  @Override
  public SynchronousFile synchronousFile() {
    return SynchronousFile.JOB_UA;
  }
}
