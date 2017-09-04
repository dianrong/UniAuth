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

  public static final int ITEM_LENGTH = 39;

  @Override public PersonList analyze(String content) throws InvalidContentException {
    PersonList result = new PersonList();
    if (!StringUtils.hasText(content)) {
      return result;
    }

    // 实际解析过程
    List<String> strList = anaToList(content);
    for (int i = 1; i < strList.size(); i++) {
      String recordStr = strList.get(i);
      String[] items = recordStr.split(rowDelimiter);
      itemLengthCheck(items, ITEM_LENGTH);
      HrPerson hrPerson = new HrPerson();
      hrPerson.setPersonId(strToLong(items[0]));
      hrPerson.setPersomNumber(items[1]);
      hrPerson.setLastNameCn(items[2]);
      hrPerson.setFirstNameCn(items[3]);
      hrPerson.setLastNameEn(items[4]);
      hrPerson.setFirstNameEn(items[5]);
      hrPerson.setMiddleNameEn(items[6]);
      hrPerson.setEnglishName(items[7]);
      hrPerson.setGender(items[8]);
      hrPerson.setEmailAddress(items[9]);
      hrPerson.setPersonTypeId(strToLong(items[10]));
      hrPerson.setSeededPersonTypeKey(items[11]);
      hrPerson.setUserPersonType(items[12]);
      hrPerson.setLocationId(strToLong(items[13]));
      hrPerson.setLocationCode(items[14]);
      hrPerson.setLocationName(items[15]);
      hrPerson.setMobPhoneNumber(items[16]);
      hrPerson.setBankName(items[17]);
      hrPerson.setBankBranchName(items[18]);
      hrPerson.setBankSubBranchName(items[19]);
      hrPerson.setBankAccount(items[20]);
      hrPerson.setEffectiveStartDate(strToDate(items[21]));
      hrPerson.setAssignmentStatusType(items[22]);
      hrPerson.setActionCode(items[23]);
      hrPerson.setTravelSubsidies(items[24]);
      hrPerson.setDepartmentId(strToLong(items[25]));
      hrPerson.setDepartmentCode(items[26]);
      hrPerson.setDepartmentName(items[27]);
      hrPerson.setJobId(strToLong(items[28]));
      hrPerson.setJobCode(items[29]);
      hrPerson.setJobName(items[30]);
      hrPerson.setManagerId(strToLong(items[31]));
      hrPerson.setManagerNum(items[32]);
      hrPerson.setManager(items[33]);
      hrPerson.setLegalEntitiesId(strToLong(items[34]));
      hrPerson.setLegalEntitiesCode(items[35]);
      hrPerson.setLegalEntitiesName(items[36]);
      hrPerson.setCompanyName(items[37]);
      hrPerson.setActualTerminationDate(strToDate(items[38]));
      result.add(hrPerson);
    }
    return result;
  }
}
