package com.dianrong.common.uniauth.server.test.profile;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserWorkRelationshipDto;
import com.dianrong.common.uniauth.common.bean.request.UserWorkRelationshipParam;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import com.dianrong.common.uniauth.server.resource.UserWorkRelationshipResource;
import com.dianrong.common.uniauth.server.test.BaseTest;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserWorkRelationshipResourceTest extends BaseTest {

  @Autowired
  private UserWorkRelationshipResource userWorkRelationshipResource;

  @Test
  public void testSearchByUserId() {
    UserWorkRelationshipParam param = new UserWorkRelationshipParam();
    param.setUserId(300000011L);
    Response<UserWorkRelationshipDto> response = userWorkRelationshipResource.searchByUserId(param);
    System.out.println(JsonUtil.object2Jason(response));
  }

  @Test
  public void testAddOrUpdateUserWrokRelationship() {
    UserWorkRelationshipParam param = new UserWorkRelationshipParam();
    param.setTenancyCode("dianrong");
    param.setUserId(300000011L);
    param.setAssignmentDate(new Date());
    param.setBusinessUnitName("Arch Team");
    param.setDepartmentName("成都研发中心");
    param.setHireDate(new Date());
    param.setLegalEntityName("点融金融");
    param.setManagerId(300000001L);
    param.setSupervisorId(300000010L);
    param.setWorkAddress("软件园C区");
    Response<UserWorkRelationshipDto> response =
        userWorkRelationshipResource.addOrUpdateUserWrokRelationship(param);
    System.out.println(JsonUtil.object2Jason(response));
  }

  @Test
  public void testUpdateUserWrokRelationship() {
    UserWorkRelationshipParam param = new UserWorkRelationshipParam();
    param.setTenancyCode("dianrong");
    param.setUserId(300000011L);
    param.setManagerId(300000011L);
    Response<UserWorkRelationshipDto> response =
        userWorkRelationshipResource.updateUserWrokRelationship(param);
    System.out.println(JsonUtil.object2Jason(response));
  }
}
