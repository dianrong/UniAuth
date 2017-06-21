package com.dianrong.common.techops.service;

import com.dianrong.common.techops.bean.BatchProcessResult;
import com.dianrong.common.techops.exp.BatchProcessException;
import com.dianrong.common.techops.util.UniBundle;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.GroupQuery;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.enm.UserActionEnum;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
   * @param path 批量上传的文件的输入流
   * @throws BatchProcessException 批量处理异常
   */
  public BatchProcessResult addUser(InputStream inputStream)
      throws IOException, BatchProcessException {
    List<String> content = readContentFromInputStream(inputStream);
    // 解析文件内容
    List<UserParam> addUserParams = Lists.newArrayList();
    for (int i = 0; i < content.size(); i++) {
      String paramStr = content.get(i);
      String[] params = split(paramStr, ",");
      if (StringUtils.isBlank(getItem(params ,1)) && StringUtils.isBlank(getItem(params ,2))) {
        throw new BatchProcessException(
            UniBundle.getMsg("service.batch.process.file.analysis.fail", i + 1, paramStr));
      }
      UserParam userParam = new UserParam();
      userParam.setName(getItem(params ,0));
      userParam.setEmail(getItem(params, 1));
      userParam.setPhone(getItem(params, 2));
      addUserParams.add(userParam);
    }

    // 实际添加处理前的预处理,检查文件内容格式
    if (!addUserParams.isEmpty()) {
      Set<String> emails = Sets.newHashSet();
      Set<String> phones = Sets.newHashSet();
      for (UserParam addUserParam : addUserParams) {
        String email = addUserParam.getEmail();
        String phone = addUserParam.getPhone();
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
    for (UserParam addUserParam : addUserParams) {
      Response<UserDto> response = null;
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
    List<String> content = readContentFromInputStream(inputStream);
    // 解析文件内容
    List<UserParam> disableUserParams = Lists.newArrayList();
    for (int i = 0; i < content.size(); i++) {
      String paramStr = content.get(i);
      String[] params = split(paramStr, ",");
      if (StringUtils.isBlank(getItem(params ,0)) && StringUtils.isBlank(getItem(params ,1))) {
        throw new BatchProcessException(
            UniBundle.getMsg("service.batch.process.file.analysis.fail", i + 1, paramStr));
      }
      UserParam userParam = new UserParam();
      userParam.setEmail(getItem(params ,0));
      userParam.setPhone(getItem(params ,1));
      disableUserParams.add(userParam);
    }

    BatchProcessResult processResult = new BatchProcessResult();
    // 批量添加用户
    for (UserParam disableUserParam : disableUserParams) {
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
   * 批量关联用户和组.
   * 
   * @throws BatchProcessException 批量处理异常
   */
  public BatchProcessResult relateUserGrp(String grpCode, InputStream inputStream)
      throws IOException {
    // check group exist
    GroupQuery groupQuery = new GroupQuery();
    groupQuery.setCode(grpCode);
    groupQuery.setStatus(AppConstants.STATUS_ENABLED);
    Response<PageDto<GroupDto>> grpResponse =
        uarwFacade.getGroupRWResource().queryGroup(groupQuery);
    if (!CollectionUtils.isEmpty(grpResponse.getInfo()) || grpResponse.getData() == null
        || grpResponse.getData().getData() == null || grpResponse.getData().getData().isEmpty()) {
      throw new BatchProcessException(UniBundle.getMsg("service.batch.process.query.grp.none"));
    }

    // get group id
    Integer grpId = grpResponse.getData().getData().get(0).getId();
    List<String> content = readContentFromInputStream(inputStream);
    // 解析文件内容
    List<UserParam> uParams = Lists.newArrayList();
    for (int i = 0; i < content.size(); i++) {
      String paramStr = content.get(i);
      String[] params = split(paramStr, ",");
      if (StringUtils.isBlank(getItem(params ,0)) && StringUtils.isBlank(getItem(params ,1))) {
        throw new BatchProcessException(
            UniBundle.getMsg("service.batch.process.file.analysis.fail", i + 1, paramStr));
      }
      UserParam userParam = new UserParam();
      userParam.setEmail(getItem(params ,0));
      userParam.setPhone(getItem(params ,1));
      uParams.add(userParam);
    }

    BatchProcessResult processResult = new BatchProcessResult();
    Set<Long> userIds = Sets.newHashSet();
    for (UserParam userParam : uParams) {
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
   * 解析字符串.处理最后一个来分隔符的情况.
   */
  private String[] split(String str, String delimiter) {
    if (StringUtils.isBlank(str)) {
      return new String[] {};
    }
    String[] items = str.split(delimiter);
    if (str.endsWith(delimiter)) {
      String[] newItmes = new String[items.length + 1];
      System.arraycopy(items, 0, newItmes, 0, items.length);
      newItmes[newItmes.length - 1] = "";
      items = newItmes;
    }
    return items;
  }

  /**
   * 从数组中获取对应index的数据,不报数组越界错误.
   */
  private <T> T getItem(T[] items, int index) {
    int length = items.length;
    if (index >= length) {
      return null;
    }
    return items[index];
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
    query.setEmail(userParam.getEmail());
    query.setPhone(userParam.getPhone());
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
   * 从inputStream读取上传文件的内容.
   */
  private List<String> readContentFromInputStream(InputStream inputStream) throws IOException {
    List<String> content = Lists.newArrayList();
    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
    String line = null;
    while ((line = in.readLine()) != null) {
      content.add(line);
    }
    return content;
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
