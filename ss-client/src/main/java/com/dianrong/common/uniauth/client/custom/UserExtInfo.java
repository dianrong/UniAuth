package com.dianrong.common.uniauth.client.custom;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.cons.AppConstants;

public class UserExtInfo extends User {
	private static final long serialVersionUID = 8347558918889027136L;

	private static final Logger logger = Logger.getLogger(UserExtInfo.class);
	
	private Long id;
	private UserDto userDto;
	private DomainDto domainDto;
	private Map<String, Set<String>> permMap;
	private Map<String, Set<PermissionDto>> permDtoMap;
	// current login user support regular pattern set
	private volatile Set<Pattern> regularPatterns;
	private Object lock = new Object();

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
	public Set<Pattern> getAllPermittedRegularPattern() {
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
	private Set<Pattern> constructPermittedRegularPattern(){
		Set<PermissionDto> permissions =  permDtoMap.get(DomainDefine.CasPermissionControlType.REGULAR.getTypeStr());
		if (permissions == null || permissions.isEmpty()) {
			return Collections.emptySet();
		}
		Set<Pattern> patterns = new HashSet<Pattern>();
		for (PermissionDto p: permissions) {
			try {
				patterns.add(Pattern.compile(p.getValue()));
			} catch(PatternSyntaxException e) {
				logger.warn(p.getValue() + " is not a syntax pattern string", e);
			}
		}
		return patterns;
	}
	
	public UserExtInfo(String username, String password, boolean enabled, boolean accountNonExpired,
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

	public UserExtInfo(String username, String password, boolean enabled, boolean accountNonExpired,
					   boolean credentialsNonExpired, boolean accountNonLocked,
					   Collection<? extends GrantedAuthority> authorities,
					   Long id, UserDto userDto, DomainDto domainDto, Map<String, Set<String>> permMap) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.userDto = userDto;
		this.domainDto = domainDto;
		this.permMap = permMap;
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

	public UserExtInfo setPermDtoMap(Map<String, Set<PermissionDto>> permDtoMap) {
		this.permDtoMap = permDtoMap;
		return this;
	}
}
