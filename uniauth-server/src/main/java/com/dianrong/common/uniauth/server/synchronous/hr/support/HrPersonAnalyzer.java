package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.server.data.entity.HrPerson;
import com.dianrong.common.uniauth.server.synchronous.exp.InvalidContentException;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.PersonList;
import com.dianrong.common.uniauth.server.synchronous.support.AbstractFileContentAnalyzer;
import com.google.common.collect.Lists;
import com.jcraft.jsch.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class HrPersonAnalyzer extends AbstractFileContentAnalyzer<PersonList> {

  public static final int ITEM_LENGTH = 47;

  @Override public PersonList analyze(String content) throws InvalidContentException {
    PersonList result = new PersonList();
    if (!StringUtils.hasText(content)) {
      return result;
    }

    // 实际解析过程
    List<String> strList = anaToList(content);
    for (int i = 1; i < strList.size(); i++) {
      String recordStr = strList.get(i);
      String[] items = splitContentRow(recordStr);
      itemLengthCheck(items, ITEM_LENGTH);
      HrPerson hrPerson = new HrPerson();
      hrPerson.setPersonId(strToLong(items[0]));
      hrPerson.setPersomNumber(items[1]);
      hrPerson.setOldNumber(items[2]);
      hrPerson.setIfEngName(items[3]);
      hrPerson.setLastNameCn(items[4]);
      hrPerson.setFirstNameCn(items[5]);
      hrPerson.setLastNameEn(items[6]);
      hrPerson.setFirstNameEn(items[7]);
      hrPerson.setMiddleNameEn(items[8]);
      hrPerson.setEnglishName(items[9]);
      hrPerson.setGender(items[10]);
      hrPerson.setEmailAddress(items[11]);
      hrPerson.setPersonTypeId(strToLong(items[12]));
      hrPerson.setSeededPersonTypeKey(items[13]);
      hrPerson.setUserPersonType(items[14]);
      hrPerson.setLocationId(strToLong(items[15]));
      hrPerson.setLocationCode(items[16]);
      hrPerson.setLocationName(items[17]);
      hrPerson.setMobPhoneNumber(items[18]);
      hrPerson.setEffectiveStartDate(strToDate(items[19]));
      hrPerson.setAssignmentStatusType(items[20]);
      hrPerson.setActionCode(items[21]);
      hrPerson.setTravelSubsidies(items[22]);
      hrPerson.setBuId(strToLong(items[23]));
      hrPerson.setBuShortCode(items[24]);
      hrPerson.setBuCode(items[25]);
      hrPerson.setBuName(items[26]);
      hrPerson.setDepartmentId(strToLong(items[27]));
      hrPerson.setDepartmentCode(items[28]);
      hrPerson.setDepartmentName(items[29]);
      hrPerson.setJobId(strToLong(items[30]));
      hrPerson.setJobCode(items[31]);
      hrPerson.setJobName(items[32]);
      hrPerson.setManagerId(strToLong(items[33]));
      hrPerson.setManagerNum(items[34]);
      hrPerson.setManager(items[35]);
      hrPerson.setLegalEntitiesId(strToLong(items[36]));
      hrPerson.setLegalEntitiesCode(items[37]);
      hrPerson.setLegalEntitiesName(items[38]);
      hrPerson.setCompanyId(strToLong(items[39]));
      hrPerson.setCompanyCode(items[40]);
      hrPerson.setCompanyName(items[41]);
      hrPerson.setActualTerminationDate(strToDate(items[42]));
      hrPerson.setBankName(items[43]);
      hrPerson.setBankBranchName(items[44]);
      hrPerson.setBankSubBranchName(items[45]);
      hrPerson.setBankAccount(items[46]);
      result.add(hrPerson);
    }
    return result;
  }
}
