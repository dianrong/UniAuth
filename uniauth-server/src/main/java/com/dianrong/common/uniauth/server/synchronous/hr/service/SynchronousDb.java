package com.dianrong.common.uniauth.server.synchronous.hr.service;

import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.HrDept;
import com.dianrong.common.uniauth.server.data.entity.HrJob;
import com.dianrong.common.uniauth.server.data.entity.HrLe;
import com.dianrong.common.uniauth.server.data.entity.HrPerson;
import com.dianrong.common.uniauth.server.data.mapper.HrDeptMapper;
import com.dianrong.common.uniauth.server.data.mapper.HrJobMapper;
import com.dianrong.common.uniauth.server.data.mapper.HrLeMapper;
import com.dianrong.common.uniauth.server.data.mapper.HrPersonMapper;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.support.group.GroupProcess;
import com.dianrong.common.uniauth.server.support.group.GroupProcessUtil;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.DepartmentList;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.JobList;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.LegalEntityList;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.PersonList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 同步操作的数据库操作
 */
@Slf4j @Service public class SynchronousDb extends TenancyBasedService {

  /**
   * 数据库操作Mapper
   */
  @Autowired private HrDeptMapper hrDeptMapper;

  @Autowired private HrJobMapper hrJobMapper;

  @Autowired private HrLeMapper hrLeMapper;

  @Autowired private HrPersonMapper hrPersonMapper;

  /**
   * 批量同步操作的数据库操作.
   */
  @Transactional public void dbProcess(DepartmentList departmentList, JobList jobList,
      LegalEntityList legalEntityList, PersonList personList) {
    // 清空表数据
    hrDeptMapper.clearTable();
    hrJobMapper.clearTable();
    hrLeMapper.clearTable();
    hrPersonMapper.clearTable();

    // 插入部门信息
    GroupProcessUtil.groupProcess(departmentList.content(), new GroupProcess<HrDept>() {
      @Override public void process(List<HrDept> groupList) {
        if (ObjectUtil.collectionIsEmptyOrNull(groupList)) {
          return;
        }
        hrDeptMapper.insertBatch(groupList);
      }
    });
    // 插入职位信息
    GroupProcessUtil.groupProcess(jobList.content(), new GroupProcess<HrJob>() {
      @Override public void process(List<HrJob> groupList) {
        if (ObjectUtil.collectionIsEmptyOrNull(groupList)) {
          return;
        }
        hrJobMapper.insertBatch(groupList);
      }
    });
    // 插入公司实体信息
    GroupProcessUtil.groupProcess(legalEntityList.content(), new GroupProcess<HrLe>() {
      @Override public void process(List<HrLe> groupList) {
        if (ObjectUtil.collectionIsEmptyOrNull(groupList)) {
          return;
        }
        hrLeMapper.insertBatch(groupList);
      }
    });
    // 插入员工
    GroupProcessUtil.groupProcess(personList.content(), new GroupProcess<HrPerson>() {
      @Override public void process(List<HrPerson> groupList) {
        if (ObjectUtil.collectionIsEmptyOrNull(groupList)) {
          return;
        }
        hrPersonMapper.insertBatch(groupList);
      }
    });
  }
}
