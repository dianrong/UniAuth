package com.dianrong.common.uniauth.client.custom;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.regular.SSRegularPattern;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.client.custom.model.AllDomainUserExtInfo;
import com.dianrong.common.uniauth.client.custom.model.SingleDomainUserExtInfo;
import com.dianrong.common.uniauth.client.custom.model.UserExtInfoParam;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.client.DomainDefine;

/**
 * uniauth对外的UserDetails实现
 * 
 * @author wanglin
 */
public class UserExtInfo implements UserDetails {
	private static final long serialVersionUID = 8347558918889027136L;
	private static final Logger logger = Logger.getLogger(UserExtInfo.class);

	// 通过账号密码登陆的域所对应的userExtInfo,可以通过该对象知道具体是从哪一个域登陆的
	private  SingleDomainUserExtInfo loginDomainUserExtInfo;

	// 所有域共享的用户信息
	private  AllDomainUserExtInfo allDomainUserExtInfo =  new AllDomainUserExtInfo();;
	
	/**
	 * get the correct UserExtInfo
	 * 
	 * @return not null
	 */
	private SingleDomainUserExtInfo getCurrentDomainUserExtInfo() {
		SingleDomainUserExtInfo currentDomainUserExtInfo =  this.allDomainUserExtInfo.getUserDetail(DomainDefine.getStaticDomainCode());
		// 用户没有对应域的权限 需要构造一个空权限的对象
		if (currentDomainUserExtInfo == null) {
			SingleDomainUserExtInfo emptyUserInfo = SingleDomainUserExtInfo.emptyAuthorityUserInfo( this.loginDomainUserExtInfo.getUsername(), 
					 this.loginDomainUserExtInfo.getId(),  this.loginDomainUserExtInfo.getUserDto(), new DomainDto());
			// cache
			SingleDomainUserExtInfo exsitOne =  this.allDomainUserExtInfo.addUserDetailIfAbsent(DomainDefine.getStaticDomainCode(), emptyUserInfo);
			if (exsitOne == null) {
				currentDomainUserExtInfo = emptyUserInfo;
			} else {
				currentDomainUserExtInfo = exsitOne;
			}
		}
		logger.info("current domain user extention info :" +  currentDomainUserExtInfo);
		return currentDomainUserExtInfo;
	}

	public Boolean hasDomain(String domainPerm) {
		return getCurrentDomainUserExtInfo().hasDomain(domainPerm);
	}

	public Boolean hasPrivilege(String privilegePerm) {
		return getCurrentDomainUserExtInfo().hasPrivilege(privilegePerm);
	}

	public Boolean hasAnyPrivilege(String... privilegePerms) {
		return getCurrentDomainUserExtInfo().hasAnyPrivilege(privilegePerms);
	}

	public Boolean hasAllPrivileges(String... privilegePerms) {
		return getCurrentDomainUserExtInfo().hasAllPrivileges(privilegePerms);
	}

	/**
	 * get current user's all permitted regular patterns set
	 * 
	 * @return unmodifiable set , not null
	 */
	public Set<SSRegularPattern> getAllPermittedRegularPattern() {
		return getCurrentDomainUserExtInfo().getAllPermittedRegularPattern();
	}

	public UserExtInfo(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, Long id,
			UserDto userDto, DomainDto domainDto,
			Map<String, Set<String>> permMap) {
		this(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id, userDto, domainDto, permMap, null);
	}
	
	public UserExtInfo(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, Long id,
			UserDto userDto, DomainDto domainDto,
			Map<String, Set<String>> permMap,
			Map<String, Set<PermissionDto>> permDtoMap) {
		this(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id, userDto, domainDto, permMap, permDtoMap, new HashMap<String, UserExtInfoParam>());
	}

	public UserExtInfo(String username, String password, boolean enabled,boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, Long id, UserDto userDto, DomainDto domainDto, Map<String, Set<String>> permMap, Map<String, Set<PermissionDto>> permDtoMap, 
			 Map<String, UserExtInfoParam> userExtInfos) {
		this.loginDomainUserExtInfo = new SingleDomainUserExtInfo(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id, userDto, domainDto, permMap, permDtoMap);
		Assert.notNull(userExtInfos);
		for (String domainCode : userExtInfos.keySet()) {
			UserExtInfoParam userExtInfo = userExtInfos.get(domainCode);
			this.allDomainUserExtInfo.addUserDetail(domainCode, 
					new SingleDomainUserExtInfo(userExtInfo.getUsername(), userExtInfo.getPassword(), userExtInfo.isEnabled(), userExtInfo.isAccountNonExpired(), 
							userExtInfo.isCredentialsNonExpired(), userExtInfo.isAccountNonLocked(), userExtInfo.getAuthorities(), userExtInfo.getId(), userExtInfo.getUserDto(), 
							userExtInfo.getDomainDto(), userExtInfo.getPermMap(), userExtInfo.getPermDtoMap()));
		}
		this.allDomainUserExtInfo.addUserDetailIfAbsent(domainDto.getCode(), this.loginDomainUserExtInfo);
	}
	
	/**
	 * @param currentLoginDomainUserInfo can not be null
	 * @param useAllDomainUserInfoShareMode
	 * @param userExtInfos
	 */
	public static UserExtInfo build(UserExtInfoParam currentLoginDomainUserInfo, Map<String, UserExtInfoParam> userExtInfos){
		Assert.notNull(currentLoginDomainUserInfo);
		return new UserExtInfo(currentLoginDomainUserInfo.getUsername(), currentLoginDomainUserInfo.getPassword(), currentLoginDomainUserInfo.isEnabled(), currentLoginDomainUserInfo.isAccountNonExpired(), 
				currentLoginDomainUserInfo.isCredentialsNonExpired(), currentLoginDomainUserInfo.isAccountNonLocked(), currentLoginDomainUserInfo.getAuthorities(), currentLoginDomainUserInfo.getId(), 
				currentLoginDomainUserInfo.getUserDto(), currentLoginDomainUserInfo.getDomainDto(), currentLoginDomainUserInfo.getPermMap(), currentLoginDomainUserInfo.getPermDtoMap(), userExtInfos);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getCurrentDomainUserExtInfo().getAuthorities();
	}

	@Override
	public String getPassword() {
		return getCurrentDomainUserExtInfo().getPassword();
	}

	@Override
	public String getUsername() {
		return getCurrentDomainUserExtInfo().getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return getCurrentDomainUserExtInfo().isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return getCurrentDomainUserExtInfo().isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return getCurrentDomainUserExtInfo().isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return getCurrentDomainUserExtInfo().isEnabled();
	}
	
	// 为了兼容以前的版本
	public Long getId() {
		return getCurrentDomainUserExtInfo().getId();
	}

	public UserExtInfo setId(Long id) {
		getCurrentDomainUserExtInfo().setId(id);
		return this;
	}

	public UserDto getUserDto() {
		return getCurrentDomainUserExtInfo().getUserDto();
	}

	public UserExtInfo setUserDto(UserDto userDto) {
		getCurrentDomainUserExtInfo().setUserDto(userDto);
		return this;
	}

	public DomainDto getDomainDto() {
		return getCurrentDomainUserExtInfo().getDomainDto();
	}

	public UserExtInfo setDomainDto(DomainDto domainDto) {
		getCurrentDomainUserExtInfo().setDomainDto(domainDto);
		return this;
	}

	public Map<String, Set<String>> getPermMap() {
		return getCurrentDomainUserExtInfo().getPermMap();
	}

	public UserExtInfo setPermMap(Map<String, Set<String>> permMap) {
		getCurrentDomainUserExtInfo().setPermMap(permMap);
		return this;
	}

	public Map<String, Set<PermissionDto>> getPermDtoMap() {
		return getCurrentDomainUserExtInfo().getPermDtoMap();
	}

	public UserExtInfo setPermDtoMap(Map<String, Set<PermissionDto>> permDtoMap) {
		getCurrentDomainUserExtInfo().setPermDtoMap(permDtoMap);
		return this;
	}

	public final SingleDomainUserExtInfo getLoginDomainUserExtInfo() {
		return loginDomainUserExtInfo;
	}

	public final AllDomainUserExtInfo getAllDomainUserExtInfo() {
		return allDomainUserExtInfo;
	}
	
	public final UserExtInfo setLoginDomainUserExtInfo(SingleDomainUserExtInfo loginDomainUserExtInfo) {
		this.loginDomainUserExtInfo = loginDomainUserExtInfo;
		return this;
	}

	public final  UserExtInfo setAllDomainUserExtInfo(AllDomainUserExtInfo allDomainUserExtInfo) {
		this.allDomainUserExtInfo = allDomainUserExtInfo;
		return this;
	}
}
