package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.server.data.entity.HrDept;
import com.dianrong.common.uniauth.server.synchronous.exp.InvalidContentException;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.DepartmentList;
import com.dianrong.common.uniauth.server.synchronous.support.AbstractFileContentAnalyzer;
import org.springframework.util.StringUtils;

import java.util.List;

public class HrDeptAnalyzer extends AbstractFileContentAnalyzer<DepartmentList> {

  public static final int ITEM_LENGTH = 15;

  @Override public DepartmentList analyze(String content) throws InvalidContentException {
    DepartmentList result = new DepartmentList();
    if (!StringUtils.hasText(content)) {
      return result;
    }

    // 实际解析过程
    List<String> strList = anaToList(content);
    // 抛弃第一行
    for(int i =1; i < strList.size(); i++) {
      String recordStr = strList.get(i);
      String[] items = splitContentRow(recordStr);
      itemLengthCheck(items, ITEM_LENGTH);
      HrDept hrDept = new HrDept();
      hrDept.setDepartmentId(strToLong(items[0]));
      hrDept.setDepartmentCode(items[1]);
      hrDept.setDepartmentName(items[2]);
      hrDept.setDateFrom(strToDate(items[3]));
      hrDept.setActiveStatus(items[4]);
      hrDept.setDeptLevel(items[5]);
      hrDept.setParentsDeptId(strToLong(items[6]));
      hrDept.setParentsDeptCode(items[7]);
      hrDept.setParentsDeptName(items[8]);
      hrDept.setLocationId(strToLong(items[9]));
      hrDept.setLocationCode(items[10]);
      hrDept.setLocationName(items[11]);
      hrDept.setManagerId(strToLong(items[12]));
      hrDept.setManagerNum(items[13]);
      hrDept.setCostCenter(items[14]);
      result.add(hrDept);
    }
    return result;
  }
}
