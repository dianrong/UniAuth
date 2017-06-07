package com.dianrong.common.techops.service;

import com.dianrong.common.techops.bean.LoginUser;
import com.dianrong.common.techops.helper.CustomizeBeanConverter;
import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.enm.PermTypeEnum;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import com.google.common.collect.Lists;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TechOpsService {

  @Resource
  private UARWFacade uarwFacade;

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  /**
   * 获取当前的登陆用户.
   */
  public UserExtInfo getLoginUserInfo() {
    UserExtInfo userExtInfo =
        (UserExtInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userExtInfo;
  }

  /**
   * 获取登陆用户信息.
   */
  public LoginUser getLoginUser() {
    UserExtInfo userExtInfo = this.getLoginUserInfo();
    List<String> authorizedDomainCodeList = Lists.newArrayList();
    List<DomainDto> domainDtoList = Lists.newArrayList();
    List<RoleDto> returnRoleList = Lists.newArrayList();
    // construct returnUser
    UserDto returnUserDto = new UserDto();
    returnUserDto.setId(userExtInfo.getUserDto().getId());
    returnUserDto.setEmail(userExtInfo.getUserDto().getEmail());
    returnUserDto.setName(userExtInfo.getUserDto().getName());
    returnUserDto.setPhone(userExtInfo.getUserDto().getPhone());

    Map<String, Set<String>> returnPermMap = new TreeMap<>();

    DomainDto domainDto = userExtInfo.getDomainDto();
    if (domainDto != null) {
      List<RoleDto> roleDtoList = domainDto.getRoleList();
      if (roleDtoList != null && !roleDtoList.isEmpty()) {
        for (RoleDto roleDto : roleDtoList) {
          // construct returnRole
          RoleDto returnRoleDto = CustomizeBeanConverter.convert(roleDto);
          returnRoleList.add(returnRoleDto);
          String roleCode = roleDto.getRoleCode();
          // construct returnPermissions
          Map<String, Set<String>> rolePermMap = roleDto.getPermMap();
          if (rolePermMap != null && rolePermMap.size() > 0) {
            Set<String> rolePermMapKeySet = rolePermMap.keySet();
            for (String key : rolePermMapKeySet) {
              Set<String> returnPermValue = returnPermMap.get(key);
              if (returnPermValue != null) {
                returnPermValue.addAll(rolePermMap.get(key));
              } else {
                Set<String> set = new HashSet<>();
                set.addAll(rolePermMap.get(key));
                returnPermMap.put(key, set);
              }
            }
          }

          if (AppConstants.ROLE_ADMIN.equals(roleCode)
              || AppConstants.ROLE_SUPER_ADMIN.equals(roleCode)) {
            Set<String> domainCodeList = rolePermMap.get(PermTypeEnum.DOMAIN.toString());
            if (domainCodeList != null && !domainCodeList.isEmpty()) {
              for (String domainCode : domainCodeList) {
                if (!authorizedDomainCodeList.contains(domainCode)) {
                  authorizedDomainCodeList.add(domainCode);
                }
              }
            }
          }
        }
      }

      if (!authorizedDomainCodeList.isEmpty()) {
        DomainParam domainParam = new DomainParam();
        if (!authorizedDomainCodeList.contains(AppConstants.DOMAIN_CODE_TECHOPS)) {
          domainParam.setDomainCodeList(authorizedDomainCodeList);
        }
        // construct switchable domainDtoList.
        domainDtoList = uarwFacade.getDomainRWResource().getAllLoginDomains(domainParam).getData();
      }
    }
    LoginUser loginUser = new LoginUser();
    loginUser.setPermMap(returnPermMap);
    loginUser.setRoles(returnRoleList);
    loginUser.setSwitchableDomains(domainDtoList);
    loginUser.setUser(returnUserDto);

    // cas service url
    String casUrl = allZkNodeMap.get("cas_server");
    loginUser.setUserInfoUpdateUrl(casUrl == null ? "#" : casUrl.trim() + "/login?edituserinfo=go");
    return loginUser;
  }

}
