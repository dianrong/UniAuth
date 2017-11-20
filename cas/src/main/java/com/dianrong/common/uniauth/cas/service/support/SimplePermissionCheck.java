package com.dianrong.common.uniauth.cas.service.support;

import com.dianrong.common.uniauth.cas.service.UserInfoManageService;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
public class SimplePermissionCheck implements PermissionCheck {

  @Autowired
  private UniClientFacade uniClientFacade;

  @Autowired
  private UserInfoManageService userInfoManageService;

  /**
   * 缓存的techops的域Id.
   */
  private volatile Integer techopsDomainId;

  @Override
  public boolean canKickOutAccount(Long userId, Long beKickOutUserId) {
    Assert.notNull(userId, "userId");
    Assert.notNull(beKickOutUserId, "beKickOutUserId");
    // userId和kickOutedUserId 必须是同一个租户

    UserDto userDto = userInfoManageService.queryUserById(userId);
    UserDto beKickOutUserDto = userInfoManageService.queryUserById(beKickOutUserId);
    if (userDto == null || beKickOutUserDto == null) {
      return false;
    }
    if (!userDto.getTenancyId().equals(beKickOutUserDto.getTenancyId())) {
      return false;
    }

    // 获取user在Techops域中的所有角色
    Integer domainId = queryTechopsDomainId();
    RoleParam roleParam = new RoleParam();
    roleParam.setUserIds(Arrays.asList(userId));
    roleParam.setTenancyId(StringUtil.translateObjectToLong(userDto.getTenancyId()));
    roleParam.setDomainId(domainId);
    Response<Map<Long, Set<RoleDto>>> response = uniClientFacade.getRoleResource()
        .getUserRoles(roleParam);

    // userId 拥有Techops的超级管理员角色
    if (response.getData() != null) {
      Map<Long, Set<RoleDto>> map = response.getData();
      Set<RoleDto> roleDtoSet = map.get(userId);
      if (!CollectionUtils.isEmpty(roleDtoSet)) {
        for (RoleDto roleDto : roleDtoSet) {
          if (AppConstants.ROLE_SUPER_ADMIN.equals(roleDto.getRoleCode())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * 获取Techops的域Id.
   */
  private Integer queryTechopsDomainId() {
    if (techopsDomainId != null) {
      return techopsDomainId;
    }
    synchronized (this) {
      if (techopsDomainId == null) {
        List<String> codes = Arrays.asList(AppConstants.DOMAIN_CODE_TECHOPS);
        Response<List<DomainDto>> response = uniClientFacade.getDomainResource()
            .getAllLoginDomains(new DomainParam().setDomainCodeList(codes));
        if (!CollectionUtils.isEmpty(response.getInfo()) || CollectionUtils
            .isEmpty(response.getData())) {
          throw new UniauthCommonException("Failed query tehcops domain id.");
        }
        techopsDomainId = response.getData().get(0).getId();
      }
    }
    return techopsDomainId;
  }
}
