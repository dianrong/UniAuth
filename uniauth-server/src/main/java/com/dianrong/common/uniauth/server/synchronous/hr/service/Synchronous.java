package com.dianrong.common.uniauth.server.synchronous.hr.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.HrSynchronousLogDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.util.SystemUtil;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.mapper.HrSynchronousLogMapper;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.synchronous.exp.*;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.*;
import com.dianrong.common.uniauth.server.synchronous.hr.support.*;
import com.dianrong.common.uniauth.server.synchronous.support.FileContentAnalyzer;
import com.dianrong.common.uniauth.server.synchronous.support.FileLoader;
import com.dianrong.common.uniauth.server.synchronous.support.ProcessLocker;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.UniBundle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 处理批量的更新的逻辑.
 */
@Slf4j @Service public class Synchronous {

  private static final int THREAD_POOL_SIZE = 5;

  /**
   * 异步线程池.
   */
  private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

  /**
   * 文件内容分析器.
   */
  private FileContentAnalyzer<DepartmentList> hrDeptAnalyzer = new HrDeptAnalyzer();
  private FileContentAnalyzer<JobList> hrJobAnalyzer = new HrJobAnalyzer();
  private FileContentAnalyzer<LegalEntityList> hrLeAnalyzer = new HrLeAnalyzer();
  private FileContentAnalyzer<PersonList> hrPersonAnalyzer = new HrPersonAnalyzer();

  /**
   * 获取锁任务.
   */
  @Autowired private ProcessLocker processLocker;

  /**
   * 加载文件的加载器.
   */
  @Autowired private FileLoader fileLoader;

  /**
   * 开关.
   */
  @Autowired
  private HrDataSynchronousSwitcher switchControl;

  /**
   * 在过期多少天之后删除.
   */
  @Value("#{uniauthConfig['synchronization.hr.delete.expired.days']?:'7'}") private int
      deleteAfterExpiredDays = 7;

  /**
   * 数据库操作service
   */
  @Autowired private SynchronousDb synchronousDb;

  /**
   * 同步日志记录Mapper.
   */
  @Autowired
  private HrSynchronousLogMapper hrSynchronousLogMapper;

  @Autowired private SFTPFileDeleter ftpFileDeleter;

  /**
   * 异步执行同步数据处理逻辑.
   */
  public void startSynchronize() {
    startSynchronize(false);
  }

  /**
   * 同步数据处理逻辑.
   * @param asynchronous 是否异步执行.
   */
  public void startSynchronize(boolean asynchronous) {
    if (!switchControl.isOn()) {
      log.info("HR data synchronous switch is off, so just ignore startSynchronize call.");
      return;
    }
    try {
      // 获取锁
      processLocker.lock();
    } catch (AcquireLockFailureException ale) {
      log.info("Acquire lock failed, " + ale.getMessage(), ale);
      throw new AppException(InfoName.ACQUIRE_LOCK_FAILED,
          UniBundle.getMsg("hr.data.synchronous.lock.acquire.failed", ale.getHoldLockServerIp()));
    }

    if (asynchronous) {
      EXECUTOR_SERVICE.submit(new Runnable() {
        @Override public void run() {
          synchronousDataProcess();
        }
      });
    } else {
      synchronousDataProcess();
    }
  }

  /**
   * 同步数据处理方法.
   */
  private void synchronousDataProcess(){
    // 同步日志对象.
    HrSynchronousLog hrSynchronousLog = new HrSynchronousLog();
    hrSynchronousLog.setSynchronousStartTime(new Date());
    // 同步HR数据
    hrSynchronousLog.setSynchronousType(HrSynchronousLogType.SYNCHRONOUS_HR_DATA.toString());
    StringBuilder sb = new StringBuilder();
    sb.append(SynchronousFile.DEPT_UA.getName()).append(",")
        .append(SynchronousFile.JOB_UA.getName()).append(",")
        .append(SynchronousFile.LE_UA.getName()).append(",")
        .append(SynchronousFile.PERSON_UA.getName());
    hrSynchronousLog.setProcessContent(sb.toString());
    hrSynchronousLog.setComputerIp(SystemUtil.getLocalIp());
    try {
      // 加载所有的文件内容
      DepartmentList departmentList =
          hrDeptAnalyzer.analyze(fileLoader.loadFile(SynchronousFile.DEPT_UA.getName()));
      JobList jobList =
          hrJobAnalyzer.analyze(fileLoader.loadFile(SynchronousFile.JOB_UA.getName()));
      LegalEntityList legalEntityList =
          hrLeAnalyzer.analyze(fileLoader.loadFile(SynchronousFile.LE_UA.getName()));
      PersonList personList =
          hrPersonAnalyzer.analyze(fileLoader.loadFile(SynchronousFile.PERSON_UA.getName()));

      // 外键约束检测
      foreignKeyCheck(departmentList, jobList, legalEntityList, personList);

      // 数据库操作
      synchronousDb.dbProcess(departmentList, jobList, legalEntityList, personList);

      // 同步成功
      hrSynchronousLog.setSynchronousResult(HrSynchronousLogResult.SUCCESS.toString());
    } catch (FileLoadFailureException | SFTPServerProcessException flfe) {
      // 加载FTP文件失败
      log.error("Synchronous HR system data failed, load ftp file failed.", flfe);
      hrSynchronousLog.setSynchronousResult(HrSynchronousLogResult.FAILURE.toString());
      hrSynchronousLog.setFailureMsg(flfe.getMessage());
      throw new AppException(InfoName.INTERNAL_ERROR, flfe.getMessage());
    } catch (InvalidContentException fkfe) {
      // 解析文件内容失败
      log.error("Synchronous HR system data failed, ftp file content is invalid.", fkfe);
      hrSynchronousLog.setSynchronousResult(HrSynchronousLogResult.FAILURE.toString());
      hrSynchronousLog.setFailureMsg(fkfe.getMessage());
      throw new AppException(InfoName.INTERNAL_ERROR, fkfe.getMessage());
    } catch (ForeignKeyCheckFailureException fkfe) {
      // 外键Check失败
      log.error("Synchronous HR system data failed, foreign key check failure.", fkfe);
      hrSynchronousLog.setSynchronousResult(HrSynchronousLogResult.FAILURE.toString());
      hrSynchronousLog.setFailureMsg(fkfe.getMessage());
      throw new AppException(InfoName.INTERNAL_ERROR, fkfe.getMessage());
    } catch (Exception e) {
      // 其他异常
      log.error("Synchronous HR system data failed, exception occured.", e);
      hrSynchronousLog.setSynchronousResult(HrSynchronousLogResult.FAILURE.toString());
      String expInfo = ExceptionUtils.getStackTrace(e);
      hrSynchronousLog.setFailureMsg(expInfo);
      throw new AppException(InfoName.INTERNAL_ERROR, expInfo);
    } finally {
      hrSynchronousLog.setSynchronousEndTime(new Date());
      // 插入同步日志
      hrSynchronousLogMapper.insert(hrSynchronousLog);
    }
  }

  /**
   * 根据条件分页查询同步日志数据.
   *
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @param type       日志类型
   * @param computerIp 操作服务器的ip
   * @param result     同步结果
   * @param pageNumber 分页页码
   * @param pageSize   分页大小
   * @return 查询结果
   */
  public PageDto<HrSynchronousLogDto> queryHrSynchronousLog(Date startTime, Date endTime,
      String type, String computerIp, String result, Integer pageNumber, Integer pageSize) {
    if (pageNumber == null || pageSize == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.empty", "pageNumber, pageSize"));
    }
    HrSynchronousLogExample hrSynchronousLogExample = new HrSynchronousLogExample();
    hrSynchronousLogExample.setOrderByClause("create_date desc");
    hrSynchronousLogExample.setPageOffSet(pageNumber * pageSize);
    hrSynchronousLogExample.setPageSize(pageSize);
    HrSynchronousLogExample.Criteria criteria = hrSynchronousLogExample.createCriteria();
    if (startTime != null) {
      criteria.andSynchronousStartTimeGreaterThanOrEqualTo(startTime);
    }
    if (endTime != null) {
      criteria.andSynchronousEndTimeLessThan(endTime);
    }
    if (StringUtils.hasText(type)) {
      criteria.andSynchronousTypeEqualTo(type);
    }
    if (StringUtils.hasText(computerIp)) {
      criteria.andComputerIpLike("%" + computerIp + "%");
    }
    if (StringUtils.hasText(result)) {
      criteria.andSynchronousResultEqualTo(result);
    }
    int count = hrSynchronousLogMapper.countByExample(hrSynchronousLogExample);
    ParamCheck.checkPageParams(pageNumber, pageSize, count);
    List<HrSynchronousLog> hrSynchronousLogs =
        hrSynchronousLogMapper.selectByExample(hrSynchronousLogExample);
    if (hrSynchronousLogs == null) {
      return null;
    }
    List<HrSynchronousLogDto> hrSynchronousLogDtos = new ArrayList<>(hrSynchronousLogs.size());
    for (HrSynchronousLog hrSynchronousLog : hrSynchronousLogs) {
      hrSynchronousLogDtos.add(BeanConverter.convert(hrSynchronousLog));
    }
    return new PageDto<>(pageNumber, pageSize, count, hrSynchronousLogDtos);
  }

  /**
   * 删除过期的FTP同步文件.
   */
  public void deleteExpiredFtpFile() {
    deleteExpiredFtpFile(false);

  }

  /**
   * 删除过期的FTP同步文件.
   */
  public void deleteExpiredFtpFile(boolean asynchronous) {
    if (!switchControl.isOn()) {
      log.info("HR data synchronous switch is off, so just ignore delete expired FTP file call.");
      return;
    }
    try {
      // 获取锁
      processLocker.lock();
    } catch (AcquireLockFailureException ale) {
      log.info("Acquire lock failed, " + ale.getMessage(), ale);
      throw new AppException(InfoName.ACQUIRE_LOCK_FAILED,
          UniBundle.getMsg("hr.data.synchronous.lock.acquire.failed", ale.getHoldLockServerIp()));
    }
    if (asynchronous) {
      EXECUTOR_SERVICE.submit(new Runnable() {
        @Override public void run() {
          deleteExpiredFileProcess();
        }
      });
    } else {
      deleteExpiredFileProcess();
    }
  }

  /**
   * 删除过期文件的实际执行逻辑.
   */
  private void deleteExpiredFileProcess(){
    // 同步日志对象.
    HrSynchronousLog hrSynchronousLog = new HrSynchronousLog();
    hrSynchronousLog.setSynchronousStartTime(new Date());
    // 同步HR数据
    hrSynchronousLog.setSynchronousType(HrSynchronousLogType.DELETE_FTP_HR_EXPIRED_DATA.toString());
    hrSynchronousLog.setComputerIp(SystemUtil.getLocalIp());
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, -deleteAfterExpiredDays);
    try {
      List<String> successDeleteFileNames =
          ftpFileDeleter.deleteFtpFileByExpiredTime(calendar.getTime());
      // 删除成功
      hrSynchronousLog.setSynchronousResult(HrSynchronousLogResult.SUCCESS.toString());
      hrSynchronousLog.setProcessContent(successDeleteFileNames.toString());
    } catch (DeleteFTPFileFailureException dfe) {
      log.debug("Failed delete files update time before:" + calendar.getTime(), dfe);
      hrSynchronousLog.setSynchronousResult(HrSynchronousLogResult.FAILURE.toString());
      hrSynchronousLog.setProcessContent(dfe.getDeleteSuccessFileNames().toString());
      hrSynchronousLog.setFailureMsg(dfe.getMessage());
      throw new AppException(InfoName.INTERNAL_ERROR, dfe.getMessage());
    } catch (Exception e) {
      log.debug("Failed delete files update time before:" + calendar.getTime(), e);
      hrSynchronousLog.setSynchronousResult(HrSynchronousLogResult.FAILURE.toString());
      String expInfo = ExceptionUtils.getStackTrace(e);
      hrSynchronousLog.setFailureMsg(expInfo);
      throw new AppException(InfoName.INTERNAL_ERROR, expInfo);
    } finally {
      hrSynchronousLog.setSynchronousEndTime(new Date());
      // 插入同步日志
      hrSynchronousLogMapper.insert(hrSynchronousLog);
    }
  }

  /**
   * 根据传入的内容,进行外键分析检测.
   *
   * @param departmentList  部门信息列表.
   * @param jobList         职位信息列表.
   * @param legalEntityList 法律实体信息列表.
   * @param personList      员工信息列表.
   * @throws ForeignKeyCheckFailureException 外键检测不过.
   */
  private void foreignKeyCheck(DepartmentList departmentList, JobList jobList,
      LegalEntityList legalEntityList, PersonList personList)
      throws ForeignKeyCheckFailureException {
    // 收集外键id集合
    Set<Long> leIds = new HashSet<>(legalEntityList.content().size());
    Set<Long> jobIds = new HashSet<>(jobList.content().size());
    Set<Long> deptIds = new HashSet<>(departmentList.content().size());
    Set<Long> personIds = new HashSet<>(personList.content().size());
    for (HrLe hrLe : legalEntityList.content()) {
      leIds.add(hrLe.getCompanyId());
    }
    for (HrJob hrJob : jobList.content()) {
      jobIds.add(hrJob.getJobId());
    }
    for (HrDept hrDept : departmentList.content()) {
      deptIds.add(hrDept.getDepartmentId());
    }
    for (HrPerson hrPerson : personList.content()) {
      personIds.add(hrPerson.getPersonId());
    }

    // Check hr_dept表的外键
    /*
    * 1 parent_dept_id 需要是deptId.
    * 2 manager_id 需要是personId.
    * */
    for (HrDept hrDept : departmentList.content()) {
      // 父部门id check.
      Long deptId = hrDept.getParentsDeptId();
      if (deptId != null && !deptIds.contains(deptId)) {
        throwForeignKeyCheckFailureException(departmentList.synchronousFile().getTableName(),
            "parentsDeptId", deptId, departmentList.synchronousFile().getTableName(),
            "departmentId");
      }
      // 部门manager id check.
      Long managerId = hrDept.getManagerId();
      if (managerId != null && !personIds.contains(managerId)) {
        throwForeignKeyCheckFailureException(departmentList.synchronousFile().getTableName(),
            "managerId", managerId, personList.synchronousFile().getTableName(), "personId");
      }
    }

    // Check hr_person表的外键
    /*
    * 1 department_id 需要是deptId.
    * 2 job_id 需要是jobId.
    * 3 manager_id 需要是personId.
    * 4 legal_entities_id 需要是leId.
    * */
    for (HrPerson hrPerson : personList.content()) {
      // buId check.
      Long buId = hrPerson.getBuId();
      if (buId != null && !deptIds.contains(buId)) {
        throwForeignKeyCheckFailureException(personList.synchronousFile().getTableName(),
            "buId", buId, departmentList.synchronousFile().getTableName(),
            "departmentId");
      }
      // 部门id check.
      Long deptId = hrPerson.getDepartmentId();
      if (deptId != null && !deptIds.contains(deptId)) {
        throwForeignKeyCheckFailureException(personList.synchronousFile().getTableName(),
            "departmentId", deptId, departmentList.synchronousFile().getTableName(),
            "departmentId");
      }
      // 职位id check.
      Long jobId = hrPerson.getJobId();
      if (jobId != null && !jobIds.contains(jobId)) {
        throwForeignKeyCheckFailureException(personList.synchronousFile().getTableName(), "jobId",
            jobId, jobList.synchronousFile().getTableName(), "jobId");
      }
      // 部门manager id check.
      Long managerId = hrPerson.getManagerId();
      if (managerId != null && !personIds.contains(managerId)) {
        throwForeignKeyCheckFailureException(personList.synchronousFile().getTableName(),
            "managerId", managerId, personList.synchronousFile().getTableName(), "personId");
      }
      // companyId check.
      Long companyId = hrPerson.getCompanyId();
      if (companyId != null && !leIds.contains(companyId)) {
        throwForeignKeyCheckFailureException(personList.synchronousFile().getTableName(),
            "companyId", companyId, legalEntityList.synchronousFile().getTableName(), "companyId");
      }
    }
  }

  /**
   * 外键检测错误,抛出异常.
   */
  private void throwForeignKeyCheckFailureException(String checkedTableName,
      String checkedFieldName, Object checkedFieldVal, String linkedTableName,
      String linkedFieldName) {
    StringBuilder msg = new StringBuilder();
    msg.append("Check foreign key failure.");
    msg.append("The checked table name:").append(checkedTableName).append(",");
    msg.append("The checked field name:").append(checkedFieldName).append(",");
    msg.append("The checked field value:").append(checkedFieldVal).append(",");
    msg.append("The foreign linked table name:").append(linkedTableName).append(".");
    msg.append("The foreign linked field name:").append(linkedFieldName).append(".");
    ForeignKeyCheckFailureException fkc = new ForeignKeyCheckFailureException(msg.toString());
    fkc.setCheckedTableName(checkedTableName);
    fkc.setCheckedFieldName(checkedFieldName);
    fkc.setCheckedFieldVal(checkedFieldVal);
    fkc.setLinkedTableName(linkedTableName);
    fkc.setLinkedFieldName(linkedFieldName);
    throw fkc;
  }
}
