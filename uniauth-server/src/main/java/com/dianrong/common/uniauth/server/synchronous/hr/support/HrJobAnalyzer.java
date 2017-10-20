package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.server.data.entity.HrJob;
import com.dianrong.common.uniauth.server.synchronous.exp.InvalidContentException;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.JobList;
import com.dianrong.common.uniauth.server.synchronous.support.AbstractFileContentAnalyzer;
import java.util.List;
import org.springframework.util.StringUtils;

public class HrJobAnalyzer extends AbstractFileContentAnalyzer<JobList> {

  public static final int ITEM_LENGTH = 7;

  @Override
  public JobList analyze(String content) throws InvalidContentException {
    JobList result = new JobList();
    if (!StringUtils.hasText(content)) {
      return result;
    }

    // 实际解析过程
    List<String> strList = anaToList(content);
    for (int i = 1; i < strList.size(); i++) {
      String recordStr = strList.get(i);
      String[] items = splitContentRow(recordStr);
      itemLengthCheck(items, ITEM_LENGTH);
      HrJob hrJob = new HrJob();
      hrJob.setJobId(strToLong(items[0]));
      hrJob.setJobCode(items[1]);
      hrJob.setJobName(items[2]);
      hrJob.setEffectiveStartDate(strToDate(items[3]));
      hrJob.setEffectiveEndDate(strToDate(items[4]));
      hrJob.setSales(items[5]);
      hrJob.setActiveStatus(items[6]);
      result.add(hrJob);
    }
    return result;
  }
}
