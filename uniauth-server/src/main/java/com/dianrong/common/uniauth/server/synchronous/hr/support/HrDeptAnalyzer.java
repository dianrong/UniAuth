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
      String[] items = recordStr.split(rowDelimiter);
      itemLengthCheck(items, ITEM_LENGTH);
      HrDept hrDept = new HrDept();
      hrDept.setDepartmentId(strToLong(clearItem(items[0])));
      hrDept.setDepartmentCode(clearItem(items[1]));
      hrDept.setDepartmentName(clearItem(items[2]));
      hrDept.setDateFrom(strToDate(clearItem(items[3])));
      hrDept.setActiveStatus(clearItem(items[4]));
      hrDept.setDeptLevel(clearItem(items[5]));
      hrDept.setParentsDeptId(strToLong(clearItem(items[6])));
      hrDept.setParentsDeptCode(clearItem(items[7]));
      hrDept.setParentsDeptName(clearItem(items[8]));
      hrDept.setLocationId(strToLong(clearItem(items[9])));
      hrDept.setLocationCode(clearItem(items[10]));
      hrDept.setLocationName(clearItem(items[11]));
      hrDept.setManagerId(strToLong(clearItem(items[12])));
      hrDept.setManagerNum(clearItem(items[13]));
      hrDept.setCostCenter(clearItem(items[14]));
      result.add(hrDept);
    }
    return result;
  }
}
