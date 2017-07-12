package com.dianrong.common.uniauth.server.test.profile;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailInfoDto;
import com.dianrong.common.uniauth.common.bean.request.UserDetailInfoParam;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import com.dianrong.common.uniauth.server.resource.UserDetailResource;
import com.dianrong.common.uniauth.server.test.BaseTest;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDetailResourceTest extends BaseTest {

  @Autowired
  private UserDetailResource userDetailResource;
  
  @Test
  public void testSearchByUserId() {
    UserDetailInfoParam param = new UserDetailInfoParam();
    param.setUserId(300000010L);
    Response<UserDetailInfoDto> response = userDetailResource.searchByUserId(param);
    System.out.println(JsonUtil.object2Jason(response));
  }
  
  @Test
  public void testAddOrUpdateUserDetail() {
    UserDetailInfoParam param = new UserDetailInfoParam();
    param.setTenancyCode("dianrong");
    param.setUserId(300000011L);
    param.setFirstName("王");
    param.setLastName("xx");
    param.setDisplayName("java工程师");
    param.setNickName("程序猿");
    param.setWechatNo("dianrong.test@weibo.com");
    param.setAid(1998L);
    param.setEntryDate(new Date());
    param.setIdentityNo("xxxxxxxxxxx");
    param.setMotto("人法地、地法天、天法道、道法自然。");
    param.setSsn("ssn_no");
    Response<UserDetailInfoDto> response = userDetailResource.addOrUpdateUserDetail(param);
    System.out.println(JsonUtil.object2Jason(response));
  }
  
  @Test
  public void testUpdateUserDetail() {
    UserDetailInfoParam param = new UserDetailInfoParam();
    param.setTenancyCode("dianrong");
    param.setUserId(300000010L);
    param.setNickName("测试");
    param.setIdentityNo("xxxxxxxxxxx");
    param.setMotto("人法地、地法天、天法道、道法自然。");
    param.setSsn("ssn_no");
    Response<UserDetailInfoDto> response = userDetailResource.updateUserDetail(param);
    System.out.println(JsonUtil.object2Jason(response));
  }
}
