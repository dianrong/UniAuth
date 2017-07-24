package com.dianrong.common.techops.action;

import com.dianrong.common.techops.bean.BatchProcessResult;
import com.dianrong.common.techops.exp.BatchProcessException;
import com.dianrong.common.techops.service.BatchService;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 批量处理的Action.
 * 
 * @author wanglin
 */
@RestController
@RequestMapping("batch")
public class BatchAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(BatchAction.class);

  @Autowired
  private BatchService batchService;

  /**
   * 批量添加用户.
   */
  @RequestMapping(value = "add-user", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
  public Response<BatchProcessResult> addUser(
      @RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
    try {
      return Response.success(batchService.addUser(file.getInputStream()));
    } catch (BatchProcessException bpe) {
      LOGGER.error("failed to process batch add user", bpe);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, bpe.getMessage()));
    }
  }

  /**
   * 批量禁用用户.只能超级管理员干.
   */
  @RequestMapping(value = "disable-user", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<BatchProcessResult> disableUser(
      @RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
    try {
      return Response.success(batchService.disableUser(file.getInputStream()));
    } catch (BatchProcessException bpe) {
      LOGGER.error("failed to process batch disable user", bpe);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, bpe.getMessage()));
    }
  }

  /**
   * 批量关联用户和组.
   */
  @RequestMapping(value = "relate-user-grp", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
      + "and hasPermission(#groupId, 'PERM_GROUP_OWNER')")
  public Response<BatchProcessResult> relateUserGrp(
      @RequestParam(value = "file", required = true) MultipartFile file,
      @RequestParam(value = "groupId", required = true) Integer groupId) throws IOException {
    try {
      return Response.success(batchService.relateUsersGrp(file.getInputStream(), groupId));
    } catch (BatchProcessException bpe) {
      LOGGER.error("failed to process batch relate users and group", bpe);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, bpe.getMessage()));
    }
  }

  /**
   * 批量关联用户和标签.
   */
  @RequestMapping(value = "relate-user-tag", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and hasPermission(#tagId, 'PERM_TAGID_CHECK'))")
  public Response<BatchProcessResult> relateUserTag(
      @RequestParam(value = "file", required = true) MultipartFile file,
      @RequestParam(value = "tagId", required = true) Integer tagId) throws IOException {
    try {
      return Response.success(batchService.relateUsersTag(file.getInputStream(), tagId));
    } catch (BatchProcessException bpe) {
      LOGGER.error("failed to process batch relate users and tag", bpe);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, bpe.getMessage()));
    }
  }

  /**
   * 批量关联用户和角色.
   */
  @RequestMapping(value = "relate-user-role", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and hasPermission(#roleId, 'PERM_ROLEID_CHECK'))")
  public Response<BatchProcessResult> relateUserRole(
      @RequestParam(value = "file", required = true) MultipartFile file,
      @RequestParam(value = "roleId", required = true) Integer roleId) throws IOException {
    try {
      return Response.success(batchService.relateUsersRole(file.getInputStream(), roleId));
    } catch (BatchProcessException bpe) {
      LOGGER.error("failed to process batch relate users and role", bpe);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, bpe.getMessage()));
    }
  }

  /**
   * 批量关联多个用户和多个组.
   */
  @RequestMapping(value = "relate-user-mult-grp", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<BatchProcessResult> relateUserMultGrp(
      @RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
    return Response.success(batchService.relateUserGrp(file.getInputStream()));
  }

  /**
   * 批量添加角色.
   */
  @RequestMapping(value = "add-role", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#domainId))")
  public Response<BatchProcessResult> addRole(
      @RequestParam(value = "file", required = true) MultipartFile file,
      @RequestParam(value = "domainId", required = true) Integer domainId) throws IOException {
    try {
      return Response.success(batchService.addRole(file.getInputStream(), domainId));
    } catch (BatchProcessException bpe) {
      LOGGER.error("failed to process batch add roles", bpe);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, bpe.getMessage()));
    }
  }

  /**
   * 批量添加权限.
   */
  @RequestMapping(value = "add-permission", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#domainId))")
  public Response<BatchProcessResult> addPermission(
      @RequestParam(value = "file", required = true) MultipartFile file,
      @RequestParam(value = "domainId", required = true) Integer domainId) throws IOException {
    try {
      return Response.success(batchService.addPermission(file.getInputStream(), domainId));
    } catch (BatchProcessException bpe) {
      LOGGER.error("failed to process batch add permissions", bpe);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, bpe.getMessage()));
    }
  }
}
