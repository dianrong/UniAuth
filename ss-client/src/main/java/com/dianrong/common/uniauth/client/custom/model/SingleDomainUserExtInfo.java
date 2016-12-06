package com.dianrong.common.uniauth.client.custom.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.regular.SSRegularPattern;

import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.google.common.collect.Maps;

/**
 * 单个域的用户信息model 
 * @author wanglin
 */
public class SingleDomainUserExtInfo extends User {
	private static final long serialVersionUID = 8347558918889027136L;

	private static final Logger logger = Logger.getLogger(SingleDomainUserExtInfo.class);
	
	private Long id;
	private UserDto userDto;
	private DomainDto domainDto;
	private Map<String, Set<String>> permMap;
	private Map<String, Set<PermissionDto>> permDtoMap;
	// current login user support regular pattern set
	private volatile Set<SSRegularPattern> regularPatterns;
	private Object lock = new Serializable(){
        private static final long serialVersionUID = 5277588062932851730L;
    };

	public Boolean hasDomain(String domainPerm) {
		if(permMap == null || permMap.get(AppConstants.PERM_TYPE_DOMAIN) == null) {
			return Boolean.FALSE;
		} else {
			Set<String> domainPerms = permMap.get(AppConstants.PERM_TYPE_DOMAIN);
			if(domainPerms.contains(domainPerm)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}
	}

	public Boolean hasPrivilege(String privilegePerm) {
		if(permMap == null || permMap.get(AppConstants.PERM_TYPE_PRIVILEGE) == null) {
			return Boolean.FALSE;
		} else {
			Set<String> privilegesHave = permMap.get(AppConstants.PERM_TYPE_PRIVILEGE);
			if(privilegesHave.contains(privilegePerm)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}
	}

	public Boolean hasAnyPrivilege(String... privilegePerms) {
		if(privilegePerms == null || privilegePerms.length == 0) {
			return Boolean.FALSE;
		} else {
			Set<String> privilegesHave = permMap.get(AppConstants.PERM_TYPE_PRIVILEGE);
			for(String privilege : privilegePerms) {
				if(privilegesHave.contains(privilege)) {
					return Boolean.TRUE;
				}
			}
			return Boolean.FALSE;
		}
	}

	public Boolean hasAllPrivileges(String... privilegePerms) {
		if(privilegePerms == null || privilegePerms.length == 0) {
			return Boolean.TRUE;
		} else {
			Set<String> privilegesHave = permMap.get(AppConstants.PERM_TYPE_PRIVILEGE);
			if(privilegesHave.containsAll(Arrays.asList(privilegePerms))) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}
	}
	
	/**
	 *  get current user's all permitted regular patterns set
	 * @return unmodifiable set , not null
	 */
	public Set<SSRegularPattern> getAllPermittedRegularPattern() {
		if (regularPatterns == null) {
			synchronized (lock) {
				if (regularPatterns == null) {
					this.regularPatterns = constructPermittedRegularPattern();
				}
			}
		}
		return Collections.unmodifiableSet(this.regularPatterns);
	}
	
	// for initiate current user's regular pattern set
	private Set<SSRegularPattern> constructPermittedRegularPattern(){
		Set<PermissionDto> permissions =  permDtoMap.get(DomainDefine.CasPermissionControlType.REGULAR_PATTERN.getTypeStr());
		if (permissions == null || permissions.isEmpty()) {
			return Collections.emptySet();
		}
		Set<SSRegularPattern> patterns = new HashSet<SSRegularPattern>();
		for (PermissionDto p: permissions) {
			String httpMethod = p.getValueExt();
			if(httpMethod != null){
				httpMethod = httpMethod.trim();
				// exclude ALL
				if(!AppConstants.HTTP_METHOD_ALL.equalsIgnoreCase(httpMethod)){
					try{
						HttpMethod.valueOf(httpMethod);
					}catch(IllegalArgumentException e){
						logger.warn("'" + httpMethod + "' is not a valid http method.", e);
						// ignore invalid httpMethod configuration
						continue;
					}
				}
				Pattern pattern = PatternCacheManager.getPattern(p.getValue());
				if (pattern != null) {
					patterns.add(SSRegularPattern.build(httpMethod, pattern));
				}
			}
		}
		return patterns;
	}
	
	/**
	 * used for pattern cache
	 * @author wanglin
	 */
	private static class PatternCacheManager {
		/**.
		 * Pattern.compile(patternStr) is a heavy method, so cache the patterns
		 */
		private static final ConcurrentMap< String, Pattern>  patternCaches = Maps.newConcurrentMap();
		private static final Pattern invalidSyntaxPattern =  Pattern.compile("invalidSyntaxPattern");
		
		/**.
		 * get Pattern from patternStr, 1 from cache; 2 from Pattern.compile(patternStr) 
		 * @param patternStr  the pattern string
		 * @return Pattern. if return is null, means patternStr is a syntax pattern string
		 */
		public static final Pattern getPattern(String patternStr) {
			if (patternStr == null) {
				return null;
			}
			Pattern p = patternCaches.get(patternStr);
			if ( p == null) {
				try {
					p = Pattern.compile(patternStr);
				} catch(PatternSyntaxException e) {
					logger.warn(patternStr + " is not a syntax pattern string", e);
				}
				// syntax pattern
				if ( p == null) {
					patternCaches.putIfAbsent(patternStr, invalidSyntaxPattern);
					return null;
				}
				patternCaches.putIfAbsent(patternStr, p);
				return patternCaches.get(patternStr);
			} else {
				// syntax pattern
				if ( p == invalidSyntaxPattern) {
					return null;
				}
				return p;
			}
		}
	}
	
	public SingleDomainUserExtInfo(String username, String password, boolean enabled, boolean accountNonExpired,
					   boolean credentialsNonExpired, boolean accountNonLocked,
					   Collection<? extends GrantedAuthority> authorities,
					   Long id, UserDto userDto, DomainDto domainDto, Map<String, Set<String>> permMap) {
		this(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id, userDto, domainDto, permMap, null);
	}
	
	public SingleDomainUserExtInfo(String username, String password, boolean enabled, boolean accountNonExpired,
			   boolean credentialsNonExpired, boolean accountNonLocked,
			   Collection<? extends GrantedAuthority> authorities,
			   Long id, UserDto userDto, DomainDto domainDto, Map<String, Set<String>> permMap, Map<String, Set<PermissionDto>> permDtoMap) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.userDto = userDto;
		this.domainDto = domainDto;
		this.permMap = permMap;
		this.permDtoMap = permDtoMap;
	}
	
	public static SingleDomainUserExtInfo emptyAuthorityUserInfo(String username,  Long id, UserDto userDto, DomainDto domainDto){
		return new SingleDomainUserExtInfo(username, "", true, true, true, true, new ArrayList<GrantedAuthority>(), id, userDto, domainDto, new HashMap<String, Set<String>>(),
				new HashMap<String, Set<PermissionDto>>());
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
  public String toString() {
    return "SingleDomainUserExtInfo [id=" + id + ", userDto=" + userDto + ", domainDto=" + domainDto + ", permMap=" + permMap + ", permDtoMap=" + permDtoMap + ", regularPatterns=" + regularPatterns + ", lock=" + lock + "]";
  }
}
