package com.dianrong.common.techops.service;

import com.dianrong.common.techops.bean.BatchProcessResult;
import com.dianrong.common.techops.exp.BatchProcessException;
import com.dianrong.common.techops.service.analysis.IdentityAnalysisResult;
import com.dianrong.common.techops.service.analysis.InputAnalyzer;
import com.dianrong.common.techops.service.analysis.NormalAnalysisResult;
import com.dianrong.common.techops.util.UniBundle;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.TagParam;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.enm.UserActionEnum;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 处理批量上传处理功能的service.
 * 
 * @author wanglin
 */
@Service
public class BatchService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BatchService.class);

  @Resource
  private UARWFacade uarwFacade;

  /**
   * 处理批量添加用户.
   * 
   * @throws BatchProcessException 批量处理异常
   */
  public BatchProcessResult addUser(InputStream inputStream)
      throws IOException, BatchProcessException {
    // 第三列是用户姓名
    List<IdentityAnalysisResult> results = InputAnalyzer.analysisInputForIdentity(inputStream, 3);
    // 实际添加处理前的预处理,检查文件内容格式
    if (!results.isEmpty()) {
      Set<String> emails = Sets.newHashSet();
      Set<String> phones = Sets.newHashSet();
      for (IdentityAnalysisResult result : results) {
        String email = result.getUserParam().getEmail();
        String phone = result.getUserParam().getPhone();
        if (!StringUtils.isBlank(email)) {
          if (!StringUtil.isEmailAddress(email)) {
            throw new BatchProcessException(
                UniBundle.getMsg("service.batch.process.email.invalid.format", email));
          }
          if (!emails.add(email)) {
            throw new BatchProcessException(
                UniBundle.getMsg("service.batch.process.email.duplicate", email));
          }
        }
        if (!StringUtils.isBlank(phone)) {
          if (!StringUtil.isPhoneNumber(phone)) {
            throw new BatchProcessException(
                UniBundle.getMsg("service.batch.process.phone.invalid.format", phone));
          }
          if (!phones.add(phone)) {
            throw new BatchProcessException(
                UniBundle.getMsg("service.batch.process.phone.duplicate", phone));
          }
        }
      }
    }

    BatchProcessResult processResult = new BatchProcessResult();
    // 批量添加用户
    for (IdentityAnalysisResult result : results) {
      Response<UserDto> response = null;
      UserParam addUserParam = result.getUserParam();
      // 额外的一列是姓名
      addUserParam.setName(result.getAppendInfo().get(0));
      try {
        response = uarwFacade.getUserRWResource().addNewUser(addUserParam);
      } catch (Exception ex) {
        LOGGER.error("failed to invoke add user API!", ex);
      }
      String identity = getIdentity(addUserParam);
      if (!CollectionUtils.isEmpty(response.getInfo())) {
        processResult.getErrors()
            .add(BatchProcessResult.Failure.build(identity, response.getInfo().get(0).getMsg()));
      } else {
        processResult.getSuccesses().add(identity);
      }
    }
    return processResult;
  }

  /**
   * 批量禁用用户.
   * 
   * @throws BatchProcessException 批量处理异常
   */
  public BatchProcessResult disableUser(InputStream inputStream) throws IOException {
    List<IdentityAnalysisResult> results = InputAnalyzer.analysisInputForIdentity(inputStream);
    BatchProcessResult processResult = new BatchProcessResult();
    // 批量添加用户
    for (IdentityAnalysisResult result : results) {
      UserParam disableUserParam = result.getUserParam();
      String identity = getIdentity(disableUserParam);
      // 根据条件查找用户
      List<UserDto> users = queryUser(disableUserParam, AppConstants.STATUS_ENABLED);
      if (users.isEmpty()) {
        processResult.getErrors().add(BatchProcessResult.Failure.build(identity,
            UniBundle.getMsg("service.batch.process.query.user.none")));
        continue;
      }

      Response<UserDto> response = null;
      try {
        response = disableUser(users.get(0).getId());
      } catch (Exception ex) {
        LOGGER.error("failed to invoke add user API!", ex);
      }
      if (!CollectionUtils.isEmpty(response.getInfo())) {
        processResult.getErrors()
            .add(BatchProcessResult.Failure.build(identity, response.getInfo().get(0).getMsg()));
      } else {
        processResult.getSuccesses().add(identity);
      }
    }
    return processResult;
  }

  /**
   * 根据Id禁用用户.
   */
  private Response<UserDto> disableUser(Long userId) {
    UserParam param = new UserParam();
    param.setId(userId);
    param.setStatus(AppConstants.STATUS_DISABLED);
    param.setUserActionEnum(UserActionEnum.STATUS_CHANGE);
    return uarwFacade.getUserRWResource().updateUser(param);
  }

  /**
   * 批量关联用户和组.
   * 
   * @param grpId 组id,不能为空
   * 
   * @throws BatchProcessException 批量处理异常
   */
  public BatchProcessResult relateUsersGrp(InputStream inputStream, Integer grpId)
      throws IOException {
    List<IdentityAnalysisResult> results = InputAnalyzer.analysisInputForIdentity(inputStream);
    BatchProcessResult processResult = new BatchProcessResult();
    Set<Long> userIds = Sets.newHashSet();
    for (IdentityAnalysisResult result : results) {
      UserParam userParam = result.getUserParam();
      String identity = getIdentity(userParam);
      // 根据条件查找用户
      List<UserDto> users = queryUser(userParam);
      if (users.isEmpty()) {
        processResult.getErrors().add(BatchProcessResult.Failure.build(identity,
            UniBundle.getMsg("service.batch.process.query.user.none")));
        continue;
      } else {
        for (UserDto ud : users) {
          userIds.add(ud.getId());
        }
        processResult.getSuccesses().add(identity);
      }
    }

    // 关联用户和组
    UserListParam userListParam = new UserListParam();
    userListParam.setGroupId(grpId);
    userListParam.setUserIds(new ArrayList<>(userIds));
    userListParam.setNormalMember(Boolean.TRUE);
    Response<Void> response = uarwFacade.getGroupRWResource().addUsersIntoGroup(userListParam);
    if (!CollectionUtils.isEmpty(response.getInfo())) {
      throw new BatchProcessException(response.getInfo().get(0).getMsg());
    }
    return processResult;
  }

  /**
   * 批量关联用户和标签.
   * 
   * @param tagId 标签id,不能为空
   * 
   * @throws BatchProcessException 批量处理异常
   */
  public BatchProcessResult relateUsersTag(InputStream inputStream, Integer tagId)
      throws IOException {
    List<IdentityAnalysisResult> results = InputAnalyzer.analysisInputForIdentity(inputStream);
    BatchProcessResult processResult = new BatchProcessResult();
    Set<Long> userIds = Sets.newHashSet();
    for (IdentityAnalysisResult result : results) {
      UserParam userParam = result.getUserParam();
      String identity = getIdentity(userParam);
      // 根据条件查找用户
      List<UserDto> users = queryUser(userParam);
      if (users.isEmpty()) {
        processResult.getErrors().add(BatchProcessResult.Failure.build(identity,
            UniBundle.getMsg("service.batch.process.query.user.none")));
        continue;
      } else {
        for (UserDto ud : users) {
          userIds.add(ud.getId());
        }
        processResult.getSuccesses().add(identity);
      }
    }

    // 关联用户和标签
    TagParam tagParam = new TagParam();
    tagParam.setId(tagId);
    tagParam.setUserIds(new ArrayList<>(userIds));
    Response<Void> response = uarwFacade.getTagRWResource().relateUsersAndTag(tagParam);
    if (!CollectionUtils.isEmpty(response.getInfo())) {
      throw new BatchProcessException(response.getInfo().get(0).getMsg());
    }
    return processResult;
  }

  /**
   * 批量关联用户和角色.
   * 
   * @param roleId 角色id,不能为空
   * 
   * @throws BatchProcessException 批量处理异常
   */
  public BatchProcessResult relateUsersRole(InputStream inputStream, Integer roleId)
      throws IOException {
    List<IdentityAnalysisResult> results = InputAnalyzer.analysisInputForIdentity(inputStream);
    BatchProcessResult processResult = new BatchProcessResult();
    Set<Long> userIds = Sets.newHashSet();
    for (IdentityAnalysisResult result : results) {
      UserParam userParam = result.getUserParam();
      String identity = getIdentity(userParam);
      // 根据条件查找用户
      List<UserDto> users = queryUser(userParam);
      if (users.isEmpty()) {
        processResult.getErrors().add(BatchProcessResult.Failure.build(identity,
            UniBundle.getMsg("service.batch.process.query.user.none")));
        continue;
      } else {
        for (UserDto ud : users) {
          userIds.add(ud.getId());
        }
        processResult.getSuccesses().add(identity);
      }
    }

    // 关联用户和标签
    RoleParam roleParam = new RoleParam();
    roleParam.setId(roleId);
    roleParam.setUserIds(new ArrayList<>(userIds));
    Response<Void> response = uarwFacade.getRoleRWResource().relateUsersAndRole(roleParam);
    if (!CollectionUtils.isEmpty(response.getInfo())) {
      throw new BatchProcessException(response.getInfo().get(0).getMsg());
    }
    return processResult;
  }

  /**
   * 关联用户和组信息.
   * 
   * @throws BatchProcessException 批量处理异常
   */
  public BatchProcessResult relateUserGrp(InputStream inputStream) throws IOException {
    // TODO 目前该块需求不明确,暂时不实现
    return new BatchProcessResult();
  }

  /**
   * 批量添加角色.
   * 
   * @param inputStream 上传文件输入流.
   * @param domainId 操作所在域id.
   * @throws BatchProcessException 批量处理异常.
   */
  public BatchProcessResult addRole(InputStream inputStream, Integer domainId) throws IOException {
    // 添加角色需要三列内容: 角色名称, 角色CODE,角色描述.
    List<NormalAnalysisResult> results = InputAnalyzer.analysisInput(inputStream, 3);
    BatchProcessResult processResult = new BatchProcessResult();
    if (ObjectUtil.collectionIsEmptyOrNull(results)) {
      LOGGER.warn("Batch add roles, but the inputStream content is empty!!!");
      return processResult;
    }
    Map<String, Integer> roleCodeMap = getRoleCodeMap();
    for (NormalAnalysisResult result : results) {
      List<String> addInfo = result.getInfo();
      String roleCode = addInfo.get(1);
      if (StringUtils.isBlank(roleCode) || roleCodeMap.get(roleCode.trim().toUpperCase()) == null) {
        LOGGER.warn("Batch add roles, RoleCode is invalid,  line number: {}, roleCode:{}. ",
            result.getLineNumber(), roleCode);
        processResult.getErrors()
            .add(BatchProcessResult.Failure.build(
                UniBundle.getMsg("service.batch.process.line.num", result.getLineNumber()),
                UniBundle.getMsg("service.batch.process.role.type.invalid", roleCode)));
        continue;
      }
      Integer roleCodeId = roleCodeMap.get(roleCode.trim().toUpperCase());
      RoleParam param = new RoleParam();
      param.setName(addInfo.get(0));
      param.setRoleCodeId(roleCodeId);
      param.setDescription(addInfo.get(2));
      param.setDomainId(domainId);
      Response<RoleDto> addRoleResponse = null;
      try {
        addRoleResponse = uarwFacade.getRoleRWResource().addNewRole(param);
      } catch (Throwable t) {
        String addParam = JsonUtil.object2Jason(param);
        LOGGER.error("Failed add role:" + addParam, t);
        processResult.getErrors()
            .add(BatchProcessResult.Failure.build(
                UniBundle.getMsg("service.batch.process.line.num", result.getLineNumber()),
                UniBundle.getMsg("service.batch.process.add.role.failed", addParam)));
        continue;
      }
      if (!ObjectUtil.collectionIsEmptyOrNull(addRoleResponse.getInfo())) {
        String addParam = JsonUtil.object2Jason(param);
        LOGGER.warn("Failed add new Role:{}, the msg is:", addParam,
            addRoleResponse.getInfo().get(0).getMsg());
        processResult.getErrors()
            .add(BatchProcessResult.Failure.build(
                UniBundle.getMsg("service.batch.process.line.num", result.getLineNumber()),
                UniBundle.getMsg("service.batch.process.add.role.failed",
                    addParam + ", Msg: " + addRoleResponse.getInfo().get(0).getMsg())));
        continue;
      }
      processResult.getSuccesses()
          .add(UniBundle.getMsg("service.batch.process.line.num", result.getLineNumber()));
    }
    return processResult;
  }

  /**
   * 批量添加权限.
   * 
   * @param inputStream 上传文件输入流.
   * @param domainId 操作所在域id.
   * @throws BatchProcessException 批量处理异常
   */
  public BatchProcessResult addPermission(InputStream inputStream, Integer domainId)
      throws IOException {
    // 添加权限需要的:权限值,权限类型,扩展值,权限描述
    List<NormalAnalysisResult> results = InputAnalyzer.analysisInput(inputStream, 4);
    BatchProcessResult processResult = new BatchProcessResult();
    if (ObjectUtil.collectionIsEmptyOrNull(results)) {
      LOGGER.warn("Batch add permissions, but the inputStream content is empty!!!");
      return processResult;
    }
    Map<String, Integer> permissionMap = getPermissionMap();
    for (NormalAnalysisResult result : results) {
      List<String> addInfo = result.getInfo();
      String permType = addInfo.get(1);
      if (StringUtils.isBlank(permType)
          || permissionMap.get(permType.trim().toUpperCase()) == null) {
        LOGGER.warn(
            "Batch add roles, Permission type is invalid,  line number: {}, permission type:{}. ",
            result.getLineNumber(), permType);
        processResult.getErrors()
            .add(BatchProcessResult.Failure.build(
                UniBundle.getMsg("service.batch.process.line.num", result.getLineNumber()),
                UniBundle.getMsg("service.batch.process.perm.type.invalid", permType)));
        continue;
      }
      Integer permissionTypeId = permissionMap.get(permType.trim().toUpperCase());
      PermissionParam permissionParam = new PermissionParam();
      permissionParam.setPermTypeId(permissionTypeId);
      permissionParam.setValue(addInfo.get(0));
      permissionParam.setValueExt(addInfo.get(2));
      permissionParam.setDescription(addInfo.get(3));
      permissionParam.setDomainId(domainId);
      Response<PermissionDto> addPermResponse = null;
      try {
        addPermResponse = uarwFacade.getPermissionRWResource().addNewPerm(permissionParam);
      } catch (Throwable t) {
        String addParam = JsonUtil.object2Jason(permissionParam);
        LOGGER.error("Failed add permission:" + addParam, t);
        processResult.getErrors()
            .add(BatchProcessResult.Failure.build(
                UniBundle.getMsg("service.batch.process.line.num", result.getLineNumber()),
                UniBundle.getMsg("service.batch.process.add.perm.failed", addParam)));
        continue;
      }
      if (!ObjectUtil.collectionIsEmptyOrNull(addPermResponse.getInfo())) {
        String addParam = JsonUtil.object2Jason(permissionParam);
        LOGGER.warn("Failed add new Permission:{}, the msg is:", addParam,
            addPermResponse.getInfo().get(0).getMsg());
        processResult.getErrors()
            .add(BatchProcessResult.Failure.build(
                UniBundle.getMsg("service.batch.process.line.num", result.getLineNumber()),
                UniBundle.getMsg("service.batch.process.add.perm.failed",
                    addParam + ", Msg: " + addPermResponse.getInfo().get(0).getMsg())));
        continue;
      }
      processResult.getSuccesses()
          .add(UniBundle.getMsg("service.batch.process.line.num", result.getLineNumber()));
    }
    return processResult;
  }

  /**
   * 获取角色Code与角色codeId的映射信息.其中角色Code转化为大写.
   */
  private Map<String, Integer> getRoleCodeMap() {
    Response<List<RoleCodeDto>> response = uarwFacade.getRoleRWResource().getAllRoleCodes();
    if (!ObjectUtil.collectionIsEmptyOrNull(response.getInfo())) {
      LOGGER.error(
          "Failed get roleCode information, the error :" + response.getInfo().get(0).getMsg());
      throw new BatchProcessException(
          "Failed get roleCode information, the error :" + response.getInfo().get(0).getMsg());
    }
    Map<String, Integer> map = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(response.getData())) {
      LOGGER.warn("Get a empty roleCode information!");
      return map;
    }
    for (RoleCodeDto dto : response.getData()) {
      map.put(dto.getCode().trim().toUpperCase(), dto.getId());
    }
    return map;
  }

  /**
   * 获取权限类型编码与权限类型Id的映射信息.其中权限类型转化为大写.
   */
  private Map<String, Integer> getPermissionMap() {
    Response<List<PermTypeDto>> response =
        uarwFacade.getPermissionRWResource().getAllPermTypeCodes();
    if (!ObjectUtil.collectionIsEmptyOrNull(response.getInfo())) {
      LOGGER.error("Failed get permission type information, the error :"
          + response.getInfo().get(0).getMsg());
      throw new BatchProcessException("Failed get permission type information, the error :"
          + response.getInfo().get(0).getMsg());
    }
    Map<String, Integer> map = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(response.getData())) {
      LOGGER.warn("Get a empty permission type information!");
      return map;
    }
    for (PermTypeDto dto : response.getData()) {
      map.put(dto.getType().trim().toUpperCase(), dto.getId());
    }
    return map;
  }

  /**
   * 根据条件去查询用户.
   */
  private List<UserDto> queryUser(UserParam userParam) {
    return queryUser(userParam, null);
  }

  /**
   * 根据条件去查询用户.
   */
  private List<UserDto> queryUser(UserParam userParam, Byte status) {
    UserQuery query = new UserQuery();
    query.setEmail(StringUtils.isBlank(userParam.getEmail()) ? null : userParam.getEmail());
    query.setPhone(StringUtils.isBlank(userParam.getPhone()) ? null : userParam.getPhone());
    if (status != null) {
      query.setStatus(status);
    }
    Response<PageDto<UserDto>> response = null;
    try {
      response = uarwFacade.getUserRWResource().searchUser(query);
    } catch (Exception ex) {
      LOGGER.error("failed to query user", ex);
      return Collections.emptyList();
    }
    if (!CollectionUtils.isEmpty(response.getInfo())) {
      LOGGER.error("failed to query user", query);
      return null;
    }
    if (response.getData() == null) {
      return Collections.emptyList();
    }
    List<UserDto> users = response.getData().getData();
    if (users == null) {
      return Collections.emptyList();
    }
    return users;
  }

  /**
   * 根据 UserParam获取用户的Identity信息.
   */
  private String getIdentity(UserParam userParam) {
    if (userParam == null) {
      return null;
    }
    String email = userParam.getEmail();
    String phone = userParam.getPhone();
    String name = userParam.getName();
    return !StringUtils.isBlank(email) ? email : !StringUtils.isBlank(phone) ? phone : name;
  }
}
