package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.ApiPermissionDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.ApiPermission;
import com.dianrong.common.uniauth.server.data.entity.ApiPermissionExample;
import com.dianrong.common.uniauth.server.data.entity.ApiPermissionExample.Criteria;
import com.dianrong.common.uniauth.server.data.mapper.ApiPermissionMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiPermissionService {

  @Autowired
  private ApiPermissionMapper apiPermissionMapper;

  /**
   * 查询共有权限信息列表.
   */
  public List<ApiPermissionDto> searchAllPublicPermissions() {
    ApiPermissionExample example = new ApiPermissionExample();
    Criteria criteria = example.createCriteria();
    criteria.andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTypeEqualTo(ApiPermissionDto.PUBLIC_PERMISSION);
    List<ApiPermissionDto> permissionDtos = Lists.newArrayList();
    List<ApiPermission> permissions = apiPermissionMapper.selectByExample(example);
    if (permissions != null && !permissions.isEmpty()) {
      for (ApiPermission p : permissions) {
        permissionDtos.add(BeanConverter.convert(p));
      }
    }
    return permissionDtos;
  }

  /**
   * 查询私有权限信息列表.
   */
  public List<ApiPermissionDto> searchPrivatePermissions(Integer apiCallerId) {
    CheckEmpty.checkEmpty(apiCallerId, "apiCallerId");
    List<ApiPermissionDto> permissionDtos = Lists.newArrayList();
    List<ApiPermission> privatePermissions =
        apiPermissionMapper.searchAllPrivatePermissions(apiCallerId);
    if (privatePermissions != null && !privatePermissions.isEmpty()) {
      for (ApiPermission p : privatePermissions) {
        permissionDtos.add(BeanConverter.convert(p));
      }
    }
    return permissionDtos;
  }
}
