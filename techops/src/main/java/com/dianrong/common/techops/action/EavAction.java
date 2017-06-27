package com.dianrong.common.techops.action;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendPageParam;
import com.dianrong.common.uniauth.common.bean.request.UserExtendParam;
import com.dianrong.common.uniauth.common.bean.request.UserExtendValParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 扩展信息管理相关action.
 */
@SuppressWarnings("deprecation")
@RestController
@RequestMapping("eav")
public class EavAction {

  @Resource
  private UniClientFacade uniFacade;

  /**
   * 查询扩展编码信息.
   */
  @RequestMapping(value = "/code/query", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
  public Response<PageDto<UserExtendDto>> queryEavCodes(
      @RequestBody UserExtendPageParam pageParam) {
    return uniFacade.getUserExtendRWResource().searchUserExtend(pageParam);
  }

  /**
   * 新增扩展类型.
   */
  @RequestMapping(value = "/code/insert", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops') ")
  public Response<UserExtendDto> addEavCode(@RequestBody UserExtendParam param) {
    return uniFacade.getUserExtendRWResource().addUserExtend(param);
  }

  /**
   * 更新扩展类型.
   */
  @RequestMapping(value = "/code/modify", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops') ")
  public Response<Integer> modifyEavCode(@RequestBody UserExtendParam param) {
    return uniFacade.getUserExtendRWResource().updateUserExtend(param);
  }

  /**
   * 根据条件查询扩展类型.
   */
  @RequestMapping(value = "/user/query", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
  public Response<PageDto<UserExtendValDto>> queryUserEavCodes(
      @RequestBody UserExtendValParam param) {
    return uniFacade.getUserExtendValRWResource().searchByUserIdAndCode(param);
  }

  /**
   * 更新用户的扩展值.
   */
  @RequestMapping(value = "/user/modify", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
  public Response<Integer> modifyUserEavCode(@RequestBody UserExtendValParam param) {
    return uniFacade.getUserExtendValRWResource().updateById(param);
  }

  /**
   * 新增用户扩展信息.
   */
  @RequestMapping(value = "/user/insert", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
  public Response<UserExtendValDto> addUserEavCode(@RequestBody UserExtendValParam param) {
    return uniFacade.getUserExtendValRWResource().add(param);
  }

  /**
   * 删除用户的扩展信息.
   */
  @RequestMapping(value = "/user/delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
  public Response<Integer> disableUserEavCode(@RequestBody UserExtendValParam param) {
    return uniFacade.getUserExtendValRWResource().delById(param);
  }
}
