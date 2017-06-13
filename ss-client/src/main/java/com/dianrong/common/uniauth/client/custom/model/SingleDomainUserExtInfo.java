package com.dianrong.common.uniauth.client.custom.model;

import com.dianrong.common.uniauth.client.support.PermissionUtil;
import com.dianrong.common.uniauth.common.bean.dto.AllDomainPermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.regular.SSRegularPattern;
import org.springframework.util.StringUtils;

/**
 * 单个域的用户信息model.
 * 
 * @author wanglin
 */
@Slf4j
@ToString
public final class SingleDomainUserExtInfo extends User {
  private static final long serialVersionUID = 8347558918889027136L;

  private Long id;
  private UserDto userDto;
  private DomainDto domainDto;
  private Map<String, Set<String>> permMap;
  private Map<String, Set<PermissionDto>> permDtoMap;
  // current login user support regular pattern set
  private volatile Set<SSRegularPattern> regularPatterns;

  /**
   * 权限判断是否有对应的域.
   */
  public Boolean hasDomain(String domainPerm) {
    if (permMap == null || permMap.get(AppConstants.PERM_TYPE_DOMAIN) == null) {
      return Boolean.FALSE;
    } else {
      Set<String> domainPerms = permMap.get(AppConstants.PERM_TYPE_DOMAIN);
      if (domainPerms.contains(domainPerm)) {
        return Boolean.TRUE;
      } else {
        return Boolean.FALSE;
      }
    }
  }

  /**
   * 判断是否有权限.
   */
  public Boolean hasPrivilege(String privilegePerm) {
    if (permMap == null || permMap.get(AppConstants.PERM_TYPE_PRIVILEGE) == null) {
      return Boolean.FALSE;
    } else {
      Set<String> privilegesHave = permMap.get(AppConstants.PERM_TYPE_PRIVILEGE);
      if (privilegesHave.contains(privilegePerm)) {
        return Boolean.TRUE;
      } else {
        return Boolean.FALSE;
      }
    }
  }

  /**
   * 判断是否拥有任意一个权限.
   */
  public Boolean hasAnyPrivilege(String... privilegePerms) {
    if (privilegePerms == null || privilegePerms.length == 0) {
      return Boolean.FALSE;
    } else {
      Set<String> privilegesHave = permMap.get(AppConstants.PERM_TYPE_PRIVILEGE);
      for (String privilege : privilegePerms) {
        if (privilegesHave.contains(privilege)) {
          return Boolean.TRUE;
        }
      }
      return Boolean.FALSE;
    }
  }

  /**
   * 判断是否所有的权限都有.
   */
  public Boolean hasAllPrivileges(String... privilegePerms) {
    if (privilegePerms == null || privilegePerms.length == 0) {
      return Boolean.TRUE;
    } else {
      Set<String> privilegesHave = permMap.get(AppConstants.PERM_TYPE_PRIVILEGE);
      if (privilegesHave.containsAll(Arrays.asList(privilegePerms))) {
        return Boolean.TRUE;
      } else {
        return Boolean.FALSE;
      }
    }
  }

  /**
   * Get current user's all permitted regular patterns set.
   * 
   * @return unmodifiable set , not null
   */
  public Set<SSRegularPattern> getAllPermittedRegularPattern() {
    if (regularPatterns == null) {
      synchronized (this) {
        if (regularPatterns == null) {
          this.regularPatterns = constructPermittedRegularPattern();
        }
      }
    }
    return Collections.unmodifiableSet(this.regularPatterns);
  }

  // for initiate current user's regular pattern set
  private Set<SSRegularPattern> constructPermittedRegularPattern() {
    Set<PermissionDto> permissions =
        permDtoMap.get(DomainDefine.CasPermissionControlType.REGULAR_PATTERN.getTypeStr());
    if (permissions == null || permissions.isEmpty()) {
      return Collections.emptySet();
    }
    Set<SSRegularPattern> patterns = new HashSet<>();
    for (PermissionDto p : permissions) {
      checkAndAddPattern(patterns, p);
    }
    return patterns;
  }

  private void checkAndAddPattern(Set<SSRegularPattern> patterns, PermissionDto p) {
    String httpMethod = p.getValueExt();
    if (httpMethod != null) {
      httpMethod = httpMethod.trim();
      // exclude ALL
      if (!AppConstants.HTTP_METHOD_ALL.equalsIgnoreCase(httpMethod)) {
        try {
          HttpMethod.valueOf(httpMethod);
        } catch (IllegalArgumentException e) {
          log.warn("'" + httpMethod + "' is not a valid http method.", e);
          return;
        }
      }
      Pattern pattern = PatternCacheManager.getPattern(p.getValue());
      if (pattern != null) {
        patterns.add(SSRegularPattern.build(httpMethod, pattern));
      }
    }
  }

  /**
   * used for pattern cache.
   * 
   * @author wanglin
   */
  private static class PatternCacheManager {
    /**
     * . Pattern.compile(patternStr) is a heavy method, so cache the patterns
     */
    private static final ConcurrentMap<String, Pattern> patternCaches = Maps.newConcurrentMap();
    private static final Pattern invalidSyntaxPattern = Pattern.compile("invalidSyntaxPattern");

    /**
     * . get Pattern from patternStr, 1 from cache; 2 from Pattern.compile(patternStr)
     * 
     * @param patternStr the pattern string
     * @return Pattern. if return is null, means patternStr is a syntax pattern string
     */
    public static final Pattern getPattern(String patternStr) {
      if (patternStr == null) {
        return null;
      }
      Pattern p = patternCaches.get(patternStr);
      if (p == null) {
        try {
          p = Pattern.compile(patternStr);
        } catch (PatternSyntaxException e) {
          log.warn(patternStr + " is not a syntax pattern string", e);
        }
        // syntax pattern
        if (p == null) {
          patternCaches.putIfAbsent(patternStr, invalidSyntaxPattern);
          return null;
        }
        patternCaches.putIfAbsent(patternStr, p);
        return patternCaches.get(patternStr);
      } else {
        // syntax pattern
        if (p == invalidSyntaxPattern) {
          return null;
        }
        return p;
      }
    }
  }

  public SingleDomainUserExtInfo(String username, String password, boolean enabled,
      boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities, Long id, UserDto userDto,
      DomainDto domainDto, Map<String, Set<String>> permMap) {
    this(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
        authorities, id, userDto, domainDto, permMap, null);
  }

  /**
   * 构造函数.
   */
  public SingleDomainUserExtInfo(String username, String password, boolean enabled,
      boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities, Long id, UserDto userDto,
      DomainDto domainDto, Map<String, Set<String>> permMap,
      Map<String, Set<PermissionDto>> permDtoMap) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
        authorities);
    Assert.notNull(userDto);
    this.id = id;
    this.userDto = userDto;
    this.domainDto = domainDto;
    this.permMap = permMap;
    this.permDtoMap = permDtoMap;
  }

  public static SingleDomainUserExtInfo emptyAuthorityUserInfo(String username, Long id,
      UserDto userDto, DomainDto domainDto) {
    return emptyAuthorityUserInfo(username, id, userDto, domainDto, null);
  }

  /**
   * 空权限的构造函数.
   */
  public static SingleDomainUserExtInfo emptyAuthorityUserInfo(String username, Long id,
      UserDto userDto, DomainDto domainDto, AllDomainPermissionDto ipaPermissionDto) {
    Map<String, Set<String>> permMap = Maps.newHashMap();
    Map<String, Set<PermissionDto>> permDtoMap = Maps.newHashMap();
    Collection<GrantedAuthority> authorities = Lists.newArrayList();
    // 增加对IPA账号的支持
    PermissionUtil.mergeIPAPermission(ipaPermissionDto, authorities, permMap, permDtoMap);
    return new SingleDomainUserExtInfo(username, "", true, true, true, true, authorities, id,
        userDto, domainDto, permMap, permDtoMap);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserDto getUserDto() {
    return userDto;
  }

  public void setUserDto(UserDto userDto) {
    this.userDto = userDto;
  }

  public DomainDto getDomainDto() {
    return domainDto;
  }

  public void setDomainDto(DomainDto domainDto) {
    this.domainDto = domainDto;
  }

  public Map<String, Set<String>> getPermMap() {
    return permMap;
  }

  public void setPermMap(Map<String, Set<String>> permMap) {
    this.permMap = permMap;
  }

  public Map<String, Set<PermissionDto>> getPermDtoMap() {
    return permDtoMap;
  }

  public SingleDomainUserExtInfo setPermDtoMap(Map<String, Set<PermissionDto>> permDtoMap) {
    this.permDtoMap = permDtoMap;
    return this;
  }

  @Override
  public boolean equals(Object rhs) {
    if (rhs instanceof SingleDomainUserExtInfo) {
      SingleDomainUserExtInfo rhsUser = (SingleDomainUserExtInfo) rhs;
      // tenancyId must be equal
      if (!this.userDto.getTenancyId().equals(rhsUser.getUserDto().getTenancyId())) {
        return false;
      }
      if (StringUtils.hasText(userDto.getEmail())) {
        return this.userDto.getEmail().equals(rhsUser.getUserDto().getEmail());
      }
      if (StringUtils.hasText(userDto.getPhone())) {
        return this.userDto.getPhone().equals(rhsUser.getUserDto().getPhone());
      }
      if (StringUtils.hasText(userDto.getAccount())) {
        return this.userDto.getAccount().equals(rhsUser.getUserDto().getAccount());
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 0;
    result = prime * result
        + ((this.userDto.getTenancyId() == null) ? 0 : this.userDto.getTenancyId().hashCode());
    result = prime * result
        + ((this.userDto.getEmail() == null) ? 0 : this.userDto.getEmail().hashCode());
    if (!StringUtils.hasText(this.userDto.getEmail())) {
      result = prime * result
          + ((this.userDto.getPhone() == null) ? 0 : this.userDto.getPhone().hashCode());
    }
    if (!StringUtils.hasText(this.userDto.getEmail())
        && !StringUtils.hasText(this.userDto.getPhone())) {
      result = prime * result
          + ((this.userDto.getAccount() == null) ? 0 : this.userDto.getAccount().hashCode());
    }
    return result;
  }
}
