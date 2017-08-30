package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.server.data.entity.HrPerson;
import com.dianrong.common.uniauth.server.synchronous.exp.InvalidContentException;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.PersonList;
import com.dianrong.common.uniauth.server.synchronous.support.AbstractFileContentAnalyzer;
import org.springframework.util.StringUtils;

import java.util.List;

public class HrPersonAnalyzer extends AbstractFileContentAnalyzer<PersonList> {

  public static final int ITEM_LENGTH = 39;

  @Override
  public PersonList analyze(String content) throws InvalidContentException {
    PersonList result = new PersonList();
    if (!StringUtils.hasText(content)) {
      return result;
    }

    // 实际解析过程
    List<String> strList = anaToList(content);
    for(int i =1; i < strList.size(); i++) {
      String recordStr = strList.get(i);
      String[] items = recordStr.split(rowDelimiter);
      itemLengthCheck(items, ITEM_LENGTH);
      HrPerson hrPerson = new HrPerson();
      hrPerson.setPersonId(strToLong(clearItem(items[0])));
      hrPerson.setPersomNumber(clearItem(items[1]));
      hrPerson.setLastNameCn(clearItem(items[2]));
      hrPerson.setFirstNameCn(clearItem(items[3]));
      hrPerson.setLastNameEn(clearItem(items[4]));
      hrPerson.setFirstNameEn(clearItem(items[5]));
      hrPerson.setMiddleNameEn(clearItem(items[6]));
      hrPerson.setEnglishName(clearItem(items[7]));
      hrPerson.setGender(clearItem(items[8]));
      hrPerson.setEmailAddress(clearItem(items[9]));
      hrPerson.setPersonTypeId(strToLong(clearItem(items[10])));
      hrPerson.setSeededPersonTypeKey(clearItem(items[11]));
      hrPerson.setUserPersonType(clearItem(items[12]));
      hrPerson.setLocationId(strToLong(clearItem(items[13])));
      hrPerson.setLocationCode(clearItem(items[14]));
      hrPerson.setLocationName(clearItem(items[15]));
      hrPerson.setMobPhoneNumber(clearItem(items[16]));
      hrPerson.setBankName(clearItem(items[17]));
      hrPerson.setBankBranchName(clearItem(items[18]));
      hrPerson.setBankSubBranchName(clearItem(items[19]));
      hrPerson.setBankAccount(clearItem(items[20]));
      hrPerson.setEffectiveStartDate(strToDate(clearItem(items[21])));
      hrPerson.setAssignmentStatusType(clearItem(items[22]));
      hrPerson.setActionCode(clearItem(items[23]));
      hrPerson.setTravelSubsidies(clearItem(items[24]));
      hrPerson.setDepartmentId(strToLong(clearItem(items[25])));
      hrPerson.setDepartmentCode(clearItem(items[26]));
      hrPerson.setDepartmentName(clearItem(items[27]));
      hrPerson.setJobId(strToLong(clearItem(items[28])));
      hrPerson.setJobCode(clearItem(items[29]));
      hrPerson.setJobName(clearItem(items[30]));
      hrPerson.setManagerId(strToLong(clearItem(items[31])));
      hrPerson.setManagerNum(clearItem(items[32]));
      hrPerson.setManager(clearItem(items[33]));
      hrPerson.setLegalEntitiesId(strToLong(clearItem(items[34])));
      hrPerson.setLegalEntitiesCode(clearItem(items[35]));
      hrPerson.setLegalEntitiesName(clearItem(items[36]));
      hrPerson.setCompanyName(clearItem(items[37]));
      hrPerson.setActualTerminationDate(strToDate(clearItem(items[38])));
      result.add(hrPerson);
    }
    return result;
  }
}
