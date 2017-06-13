package com.dianrong.common.uniauth.client.custom;

import com.dianrong.common.uniauth.client.support.CheckDomainDefine;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class SSUserDetailService implements UserDetailsService {

  @Autowired
  private UniClientFacade uniClientFacade;

  @Autowired
  private DomainDefine domainDefine;

  @Autowired(required = false)
  private UserInfoCallBack userInfoCallBack;

  @Override
  public UserDetails loadUserByUsername(String userName)
      throws UsernameNotFoundException, DataAccessException {
    String currentDomainCode = domainDefine.getDomainCode();
    String userInfoClass = domainDefine.getUserInfoClass();

    CheckDomainDefine.checkDomainDefine(currentDomainCode);
    // currentDomainCode = currentDomainCode.substring(AppConstants.ZK_DOMAIN_PREFIX.length());

    if (userName == null || "".equals(userName.toString())) {
      throw new UsernameNotFoundException(userName + " not found");
    } else {
      LoginParam loginParam = new LoginParam();
      loginParam.setAccount(userName);
      Response<UserDetailDto> response = uniClientFacade.getUserResource()
          .getUserDetailInfo(loginParam);
      UserDetailDto userDetailDto = response.getData();

      if (userDetailDto == null) {
        throw new UsernameNotFoundException(userName + " not found");
      } else {
        UserDto userDto = userDetailDto.getUserDto();
        Long id = userDto.getId();
        List<DomainDto> domainDtoList = userDetailDto.getDomainList();
        DomainDto currentDomainDto = null;

        if (domainDtoList != null && !domainDtoList.isEmpty()) {
          for (DomainDto domainDto : domainDtoList) {
            String domainCode = domainDto.getCode();
            if (currentDomainCode.equals(domainCode)) {
              currentDomainDto = domainDto;
              break;
            }
          }
        }
        Map<String, Set<String>> permMap = new HashMap<String, Set<String>>();
        Map<String, Set<PermissionDto>> permDtoMap = new HashMap<>();

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if (currentDomainDto != null) {
          Integer domainId = currentDomainDto.getId();
          ReflectionUtils.setStaticField(DomainDefine.class.getName(), "domainId", domainId);

          List<RoleDto> roleDtoList = currentDomainDto.getRoleList();
          if (roleDtoList != null && !roleDtoList.isEmpty()) {
            for (RoleDto roleDto : roleDtoList) {
              String roleCode = roleDto.getRoleCode();
              SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleCode);
              authorities.add(authority);

              mergePermMap(permMap, roleDto.getPermMap());
              mergePermMap(permDtoMap, roleDto.getPermDtoMap());
            }
          }
        }

        if (userInfoClass == null || "".equals(userInfoClass.trim())) {
          return new UserExtInfo(userName, "fake_password", true, true, true, true, authorities, id,
              userDto, currentDomainDto, permMap, permDtoMap);
        } else {
          try {
            Class<?> clazz = Class.forName(userInfoClass);
            Constructor<?> construct = clazz
                .getConstructor(String.class, String.class, Boolean.TYPE, Boolean.TYPE,
                    Boolean.TYPE, Boolean.TYPE, Collection.class,
                    Long.class, UserDto.class, DomainDto.class, Map.class, Map.class);
            UserExtInfo userExtInfo = (UserExtInfo) construct
                .newInstance(userName, "fake_password", true, true, true, true, authorities, id,
                    userDto, currentDomainDto,
                    permMap, permDtoMap);

            if (userInfoCallBack != null) {
              userInfoCallBack.fill(userExtInfo);
            }
            return userExtInfo;
          } catch (Exception e) {
            log.error(
                "Prepare to use ss-client's UserExtInfo, not the "
                + "subsystem's customized one, possible reasons:\n (1) "
                    + userInfoClass + " not found. \n (2) "
                    + userInfoClass
                    + " is not a instance of UserExtInfo.\n (3) "
                    + "userInfoCallBack.fill(userExtInfo) error.",
                e);
            return new UserExtInfo(userName, "fake_password", true, true, true, true, authorities,
                id, userDto, currentDomainDto, permMap, permDtoMap);
          }
        }
      }
    }
  }

  private <T> void mergePermMap(Map<String, Set<T>> permMap, Map<String, Set<T>> subPermMap) {
    Set<Entry<String, Set<T>>> subEntrySet = subPermMap.entrySet();
    Iterator<Entry<String, Set<T>>> subEntryIterator = subEntrySet.iterator();
    while (subEntryIterator.hasNext()) {
      Entry<String, Set<T>> subEntry = subEntryIterator.next();
      String permTypeName = subEntry.getKey();
      Set<T> permValueSet = subEntry.getValue();

      if (permMap.containsKey(permTypeName)) {
        permMap.get(permTypeName).addAll(permValueSet);
      } else {
        permMap.put(permTypeName, permValueSet);
      }
    }
  }

}
