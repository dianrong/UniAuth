package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.*;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.enm.UserActionEnum;
import com.dianrong.common.uniauth.common.util.AuthUtils;
import com.dianrong.common.uniauth.common.util.Base64;
import com.dianrong.common.uniauth.common.util.UniPasswordEncoder;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.mapper.*;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by Arc on 14/1/16.
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleCodeMapper roleCodeMapper;
    @Autowired
    private DomainMapper domainMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private CommonService commonService;
    

    @Transactional
    public UserDto addNewUser(String name, String phone, String email) {
        this.checkPhoneAndEmail(phone, email, null);
        User user = new User();
        user.setEmail(email);
        user.setName(name);

        Date now = new Date();
        user.setFailCount(AppConstants.ZERO_Byte);

        String randomPassword = AuthUtils.randomPassword();
        byte salt[] = AuthUtils.createSalt();
        user.setPassword(Base64.encode(AuthUtils.digest(randomPassword, salt)));
        user.setPasswordSalt(Base64.encode(salt));

        user.setLastUpdate(now);
        user.setCreateDate(now);
        user.setPhone(phone);
        user.setStatus(AppConstants.ZERO_Byte);
        userMapper.insert(user);
        UserDto userDto = BeanConverter.convert(user).setPassword(randomPassword);
        return userDto;
    }

    @Transactional
    public UserDto updateUser(UserActionEnum userActionEnum, Long id, String name, String phone, String email, String password, Byte status) {
        if(userActionEnum == null || id == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "userActionEnum, userId"));
        }
        User user = userMapper.selectByPrimaryKey(id);
        if(user == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", id, User.class.getSimpleName()));
        } else if(user.getStatus().equals(AppConstants.ONE_Byte) && !UserActionEnum.STATUS_CHANGE.equals(userActionEnum)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.status.isone", id, User.class.getSimpleName()));
        }
        switch(userActionEnum) {
            case LOCK:
                user.setFailCount(AppConstants.MAX_AUTH_FAIL_COUNT);
                break;
            case UNLOCK:
                user.setFailCount((byte)0);
                break;
            case RESET_PASSWORD:
                if(password == null) {
                    throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "password"));
                } else if(!AuthUtils.validatePasswordRule(password)) {
                    throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("user.parameter.password.rule"));
                }
                byte salt[] = AuthUtils.createSalt();
                user.setPassword(Base64.encode(AuthUtils.digest(password, salt)));
                user.setPasswordSalt(Base64.encode(salt));
                user.setPasswordDate(new Date());
                break;
            case STATUS_CHANGE:
                user.setStatus(status);
                break;
            case UPDATE_INFO:
                this.checkPhoneAndEmail(phone, email, id);
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                break;
        }
        user.setLastUpdate(new Date());
        userMapper.updateByPrimaryKey(user);
        return BeanConverter.convert(user).setPassword(password);
    }

    public List<RoleDto> getAllRolesToUser(Long userId, Integer domainId) {
        if(userId == null || domainId == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "userId, domainId"));
        }
        // 1. get all roles under the domain
        RoleExample roleExample = new RoleExample();
        roleExample.createCriteria().andDomainIdEqualTo(domainId).andStatusEqualTo(AppConstants.ZERO_Byte);
        List<Role> roles = roleMapper.selectByExample(roleExample);
        if(CollectionUtils.isEmpty(roles)) {
            return null;
        }
        // 2. get the checked roleIds for the user
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUserIdEqualTo(userId);
        List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(userRoleExample);
        Set<Integer> roleIds = null;
        if(!CollectionUtils.isEmpty(userRoleKeys)) {
            roleIds = new TreeSet<>();
            for(UserRoleKey userRoleKey : userRoleKeys) {
                roleIds.add(userRoleKey.getRoleId());
            }
        }

        List<RoleCode> roleCodes = roleCodeMapper.selectByExample(new RoleCodeExample());
        
        // build roleCode index.
        Map<Integer, String> roleCodeIdNamePairs = new TreeMap<>();
        for(RoleCode roleCode : roleCodes) {
            roleCodeIdNamePairs.put(roleCode.getId(), roleCode.getCode());
        }

        // 3. construct all roles under the domain & mark the role checked on the user or not
        List<RoleDto> roleDtos = new ArrayList<>();
        for(Role role : roles) {
            RoleDto roleDto = new RoleDto()
                    .setId(role.getId())
                    .setName(role.getName())
                    .setRoleCode(roleCodeIdNamePairs.get(role.getRoleCodeId()));
            if(roleIds != null && roleIds.contains(role.getId())) {
                roleDto.setChecked(Boolean.TRUE);
            } else {
                roleDto.setChecked(Boolean.FALSE);
            }
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }

    @Transactional
    public void saveRolesToUser(Long userId, List<Integer> roleIds) {

        if(userId == null || CollectionUtils.isEmpty(roleIds)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "userId, roleIds"));
        }

        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUserIdEqualTo(userId);
        List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(userRoleExample);
        Set<Integer> roleIdSet = new TreeSet<>();
        if(!CollectionUtils.isEmpty(userRoleKeys)) {
            for (UserRoleKey userRoleKey : userRoleKeys) {
                roleIdSet.add(userRoleKey.getRoleId());
            }
        }
        for(Integer roleId : roleIds) {
            if(!roleIdSet.contains(roleId)) {
                UserRoleKey userRoleKey = new UserRoleKey();
                userRoleKey.setRoleId(roleId);
                userRoleKey.setUserId(userId);
                userRoleMapper.insert(userRoleKey);
            }
        }
    }

    public PageDto<UserDto> searchUser(String name, String phone, String email, Byte status, Integer pageNumber, Integer pageSize) {
        if(pageNumber == null || pageSize == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "pageNumber, pageSize"));
        }
        UserExample userExample = new UserExample();
        userExample.setOrderByClause("create_date desc");
        userExample.setPageOffSet(pageNumber * pageSize);
        userExample.setPageSize(pageSize);
        UserExample.Criteria criteria = userExample.createCriteria();
        if(name != null) {
            criteria.andNameLike("%" + name + "%");
        }
        if(phone != null) {
            criteria.andPhoneLike("%" + phone + "%");
        }
        if(email != null) {
            criteria.andEmailLike("%" + email + "%");
        }
        if(status != null) {
            criteria.andStatusEqualTo(status);
        }
        List<User> users = userMapper.selectByExample(userExample);
        if(!CollectionUtils.isEmpty(users)) {
            int count = userMapper.countByExample(userExample);
            List<UserDto> userDtos = new ArrayList<>();
            for(User user : users) {
                userDtos.add(BeanConverter.convert(user));
            }
            return new PageDto<>(pageNumber,pageSize,count,userDtos);
        } else {
            return null;
        }
    }

    private void checkPhoneAndEmail(String phone, String email, Long userId) {
        if(email == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "email"));
        }
        if(!email.contains("@")) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("user.parameter.email.invalid", email));
        }
        // check duplicate email
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria1 = userExample.createCriteria().andEmailEqualTo(email);
        if(userId != null) {
            criteria1.andIdNotEqualTo(userId);
        }
        List<User> emailUsers =  userMapper.selectByExample(userExample);
        if(!CollectionUtils.isEmpty(emailUsers)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("user.parameter.email.dup", email));
        }
        if(phone != null) {
            //check duplicate phone
            UserExample userPhoneExample = new UserExample();
            UserExample.Criteria criteria2 = userPhoneExample.createCriteria().andPhoneEqualTo(phone);
            if(userId != null) {
                criteria2.andIdNotEqualTo(userId);
            }
            List<User> phoneUsers = userMapper.selectByExample(userPhoneExample);
            if (!CollectionUtils.isEmpty(phoneUsers)) {
                throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("user.parameter.phone.dup", phone));
            }
        }
    }

	public void login(LoginParam loginParam) {
		String account = loginParam.getAccount();
		String password = loginParam.getPassword();
		String ip = loginParam.getIp();
		CheckEmpty.checkEmpty(account, "账号");
		CheckEmpty.checkEmpty(password, "密码");
		CheckEmpty.checkEmpty(ip, "IP地址");

		User user = getUserByAccount(account, true);
        
		if(AppConstants.ONE_Byte.equals(user.getStatus())){
			throw new AppException(InfoName.LOGIN_ERROR_STATUS_1, UniBundle.getMsg("user.login.status.lock"));
		}
        if(user.getFailCount() >= AppConstants.MAX_AUTH_FAIL_COUNT){
        	throw new AppException(InfoName.LOGIN_ERROR_EXCEED_MAX_FAIL_COUNT, UniBundle.getMsg("user.login.account.lock"));
        }
        if(!UniPasswordEncoder.isPasswordValid(user.getPassword(), password, user.getPasswordSalt())){
        	updateLogin(user.getId(), ip, user.getFailCount() + 1);
            throw new AppException(InfoName.LOGIN_ERROR, UniBundle.getMsg("user.login.error"));
        }
        //successfully loged in
        updateLogin(user.getId(), ip, 0);
        
        Date passwordDate = user.getPasswordDate();
        if(passwordDate == null){
        	throw new AppException(InfoName.LOGIN_ERROR_NEW_USER, UniBundle.getMsg("user.login.newuser"));
        }
        else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getPasswordDate());
            calendar.add(Calendar.MONTH, AppConstants.MAX_PASSWORD_VALID_MONTH);
            Date currentDate = new Date();
            if (currentDate.after(calendar.getTime())) {
            	throw new AppException(InfoName.LOGIN_ERROR_EXCEED_MAX_PASSWORD_VALID_MONTH, UniBundle.getMsg("user.login.password.usetoolong", String.valueOf(AppConstants.MAX_PASSWORD_VALID_MONTH)));
            }
        }
	}
    
	public UserDetailDto getUserDetailInfo(LoginParam loginParam) {
		String account = loginParam.getAccount();
		CheckEmpty.checkEmpty(account, "账号");
		User user = getUserByAccount(account, true);
		
		UserDetailDto userDetailDto = new UserDetailDto();
		UserDto userDto = BeanConverter.convert(user);
		userDetailDto.setUserDto(userDto);
		
		Long userId = user.getId();
		List<DomainDto> domainDtoList = new ArrayList<DomainDto>();
		userDetailDto.setDomainList(domainDtoList);
		List<Domain> domainList = domainMapper.selectUserDomainsByUserId(userId);
		
		Map<Integer, RoleCode> roleCodeMap = commonService.getRoleCodeMap();
		Map<Integer, PermType> permTypeMap = commonService.getPermTypeMap();
		
		if(domainList != null && !domainList.isEmpty()){
			for(Domain domain : domainList){
				Integer domainId = domain.getId();
				Map<String, Object> userAndDomainMap = new HashMap<String, Object>();
				userAndDomainMap.put("userId", userId);
				userAndDomainMap.put("domainId", domainId);
				List<Role> roleList = roleMapper.getRolesByUserAndDomainId(userAndDomainMap);
				List<RoleDto> roleDtoList = new ArrayList<RoleDto>();
				
				DomainDto domainDto = BeanConverter.convert(domain);
				domainDto.setRoleList(roleDtoList);
				domainDtoList.add(domainDto);
				
				if(roleList != null){
					for(Role role: roleList){
						RoleDto roleDto = BeanConverter.convert(role);
						roleDto.setRoleCode(roleCodeMap.get(role.getRoleCodeId()).getCode());
						roleDtoList.add(roleDto);
						
						Map<String, Object> roleAndDomainMap = new HashMap<String, Object>();
						roleAndDomainMap.put("domainId", domainId);
						roleAndDomainMap.put("roleId", roleDto.getId());
						
						List<Permission> permList = permissionMapper.selectByRoleAndDomainId(roleAndDomainMap);
						
						Map<String, Set<String>> permMap = new HashMap<String, Set<String>>();
						
						if(permList != null){
							for(Permission permission: permList){
								Integer permTypeId = permission.getPermTypeId();
								String permType = permTypeMap.get(permTypeId).getType();
								String value = permission.getValue();
								
								if(permMap.containsKey(permType)){
									permMap.get(permType).add(value);
								}
								else{
									Set<String> set = new HashSet<>();
                                    set.add(value);
									permMap.put(permType, set);
								}
							}
						}
						
						roleDto.setPermMap(permMap);
					}
				}
			}
		}
		
		return userDetailDto;
	}

	public UserDto getSingleUser(UserParam userParam) {
		String email = userParam.getEmail();
		CheckEmpty.checkEmpty(email, "邮件");
		
		User user = getUserByAccount(email, false);
		return BeanConverter.convert(user);
	}
	
	@Transactional
	public void resetPassword(UserParam userParam) {
		String email = userParam.getEmail();
		String password = userParam.getPassword();
		CheckEmpty.checkEmpty(email, "邮件");
		CheckEmpty.checkEmpty(password, "密码");
		
		User user = getUserByAccount(email, false);
		if(!AuthUtils.validatePasswordRule(password)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("user.parameter.password.rule"));
        }
        byte salt[] = AuthUtils.createSalt();
        user.setPassword(Base64.encode(AuthUtils.digest(password, salt)));
        user.setPasswordSalt(Base64.encode(salt));
        user.setPasswordDate(new Date());
        user.setFailCount(AppConstants.ZERO_Byte);
        
        userMapper.updateByPrimaryKey(user);
	}

    @Transactional
    public void replaceRolesToUser(Long userId, List<Integer> roleIds) {

        CheckEmpty.checkEmpty(userId, "userId");

        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUserIdEqualTo(userId);
        if(CollectionUtils.isEmpty(roleIds)) {
            userRoleMapper.deleteByExample(userRoleExample);
            return;
        }
        List<UserRoleKey>  userRoleKeys = userRoleMapper.selectByExample(userRoleExample);
        if(!CollectionUtils.isEmpty(userRoleKeys)) {
            ArrayList<Integer> dbRoleIds = new ArrayList<>();
            for(UserRoleKey userRoleKey : userRoleKeys) {
                dbRoleIds.add(userRoleKey.getRoleId());
            }
            ArrayList<Integer> intersections = ((ArrayList<Integer>)dbRoleIds.clone());
            intersections.retainAll(roleIds);
            List<Integer> roleIdsNeedAddToDB = new ArrayList<>();
            List<Integer> roleIdsNeedDeleteFromDB = new ArrayList<>();
            for(Integer roleId : roleIds) {
                if(!intersections.contains(roleId)) {
                    roleIdsNeedAddToDB.add(roleId);
                }
            }
            for(Integer dbRoleId : dbRoleIds) {
                if(!intersections.contains(dbRoleId)) {
                    roleIdsNeedDeleteFromDB.add(dbRoleId);
                }
            }

            if(!CollectionUtils.isEmpty(roleIdsNeedAddToDB)) {
                for(Integer roleIdNeedAddToDB : roleIdsNeedAddToDB) {
                    UserRoleKey userRoleKey = new UserRoleKey();
                    userRoleKey.setRoleId(roleIdNeedAddToDB);
                    userRoleKey.setUserId(userId);
                    userRoleMapper.insert(userRoleKey);
                }
            }
            if(!CollectionUtils.isEmpty(roleIdsNeedDeleteFromDB)) {
                UserRoleExample userRoleDeleteExample = new UserRoleExample();
                userRoleDeleteExample.createCriteria().andUserIdEqualTo(userId).andRoleIdIn(roleIdsNeedDeleteFromDB);
                userRoleMapper.deleteByExample(userRoleDeleteExample);
            }
        } else {
            for(Integer roleId : roleIds) {
                UserRoleKey userRoleKey = new UserRoleKey();
                userRoleKey.setRoleId(roleId);
                userRoleKey.setUserId(userId);
                userRoleMapper.insert(userRoleKey);
            }
        }
    }

    private int updateLogin(Long userId, String ip, int failCount) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(ip);
        user.setFailCount((byte)failCount);
        return userMapper.updateByPrimaryKeySelective(user);
    }
    
    private User getUserByAccount(String account, boolean withPhoneChecked){
    	UserExample example = new UserExample();
    	example.or().andEmailEqualTo(account);
    	if(withPhoneChecked){
        	example.or().andPhoneEqualTo(account);
    	}

        List<User> userList = userMapper.selectByExample(example);
        if(userList == null || userList.isEmpty()){
        	throw new AppException(InfoName.LOGIN_ERROR_USER_NOT_FOUND, UniBundle.getMsg("user.login.notfound", account));
        }
        if(userList.size() > 1){
        	throw new AppException(InfoName.LOGIN_ERROR_MULTI_USER_FOUND, UniBundle.getMsg("user.login.multiuser.found"));
        }
        
        User user = userList.get(0);
        return user;
    }
}
