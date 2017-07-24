package com.dianrong.common.uniauth.sharerw.facade;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendPageParam;
import com.dianrong.common.uniauth.common.bean.request.UserExtendParam;
import com.dianrong.common.uniauth.common.bean.request.UserExtendValParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IUserExtendRWResource;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IUserExtendValRWResource;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wenlongchen.
 * @since May 16, 2016
 */
@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:ss-uniauth-common.xml"})
public class UARWFacadeTest {

  @Autowired
  private UniClientFacade facade;

  private ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);

  @Test
  public void addUserExtend() throws Exception {
    IUserExtendRWResource userExtendResource = facade.getUserExtendRWResource();

    UserExtendParam userExtendParam = new UserExtendParam();
    userExtendParam.setCode("aaaa");
    userExtendParam.setDescription("aaaaaaaaaaaaaaaaaaaaaaa");
    Response<UserExtendDto> response1 = userExtendResource.addUserExtend(userExtendParam);

    System.out.println(mapper.writeValueAsString(response1));
  }

  @Test
  public void updateUserExtend() throws JsonProcessingException {
    IUserExtendRWResource userExtendResource = facade.getUserExtendRWResource();

    UserExtendParam userExtendParam = new UserExtendParam();
    userExtendParam.setId(9l);
    userExtendParam.setCode("wwf");
    userExtendParam.setDescription("aaaaaaaaacccaaaaaaaaaaaaaa");
    Response<Integer> response1 = userExtendResource.updateUserExtend(userExtendParam);

    System.out.println(mapper.writeValueAsString(response1));
  }

  @Test
  public void searchUserExtend() throws JsonProcessingException {
    IUserExtendRWResource userExtendResource = facade.getUserExtendRWResource();

    UserExtendPageParam userExtendParam = new UserExtendPageParam();
    userExtendParam.setCode("a");
    userExtendParam.setPageNumber(1);
    userExtendParam.setPageSize(3);
    Response<PageDto<UserExtendDto>> response1 =
        userExtendResource.searchUserExtend(userExtendParam);
    System.out.println(mapper.writeValueAsString(response1));
  }

  @Test
  public void addUserExtendVal() throws JsonProcessingException {
    IUserExtendValRWResource userExtendResource = facade.getUserExtendValRWResource();

    UserExtendValParam userExtendValParam = new UserExtendValParam();
    userExtendValParam.setExtendId(2l);
    userExtendValParam.setUserId(300000001l);
    userExtendValParam.setValue("5");

    Response<UserExtendValDto> response1 = userExtendResource.add(userExtendValParam);

    System.out.println(mapper.writeValueAsString(response1));
  }

  @Test
  public void updateUserExtendVal() throws JsonProcessingException {
    IUserExtendValRWResource userExtendResource = facade.getUserExtendValRWResource();

    UserExtendValParam userExtendValParam = new UserExtendValParam();
    userExtendValParam.setId(2l);
    userExtendValParam.setValue("8");

    Response<Integer> response1 = userExtendResource.updateById(userExtendValParam);

    System.out.println(mapper.writeValueAsString(response1));
  }

  @Test
  public void delUserExtendVal() throws JsonProcessingException {
    IUserExtendValRWResource userExtendResource = facade.getUserExtendValRWResource();

    UserExtendValParam userExtendValParam = new UserExtendValParam();
    userExtendValParam.setId(2l);

    Response<Integer> response1 = userExtendResource.delById(userExtendValParam);

    System.out.println(mapper.writeValueAsString(response1));
  }

  @Test
  public void searchUserExtendVal() throws JsonProcessingException {
    IUserExtendValRWResource userExtendResource = facade.getUserExtendValRWResource();

    UserExtendValParam userExtendValParam = new UserExtendValParam();
    userExtendValParam.setUserId(300000001l);

    Response<List<UserExtendValDto>> response1 =
        userExtendResource.searchByUserId(userExtendValParam);
    System.out.println(mapper.writeValueAsString(response1));

    userExtendValParam.setStatus((byte) 0);
    response1 = userExtendResource.searchByUserId(userExtendValParam);
    System.out.println(mapper.writeValueAsString(response1));
  }

  @Test
  public void searchPageUserExtendVal() throws JsonProcessingException {
    IUserExtendValRWResource userExtendResource = facade.getUserExtendValRWResource();

    UserExtendValParam userExtendValParam = new UserExtendValParam();
    userExtendValParam.setUserId(300000001l);
    userExtendValParam.setPageNumber(0);
    userExtendValParam.setPageSize(50);

    userExtendValParam.setExtendCode("aa");
    userExtendValParam.setPageSize(5);
    userExtendValParam.setQueryOnlyUsed(false);
    Response<PageDto<UserExtendValDto>> response1 =
        userExtendResource.searchByUserIdAndCode(userExtendValParam);
    System.out.println(mapper.writeValueAsString(response1));

    userExtendValParam.setExtendCode("b");
    response1 = userExtendResource.searchByUserIdAndCode(userExtendValParam);
    System.out.println(mapper.writeValueAsString(response1));

    userExtendValParam.setExtendCode(null);
    response1 = userExtendResource.searchByUserIdAndCode(userExtendValParam);
    System.out.println(mapper.writeValueAsString(response1));
  }
}
