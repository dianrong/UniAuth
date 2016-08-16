package com.dianrong.common.uniauth.server.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.TaskInvoker.TaskExecutor;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.DomainExample;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.GrpExample;
import com.dianrong.common.uniauth.server.data.entity.GrpTagKey;
import com.dianrong.common.uniauth.server.data.entity.Role;
import com.dianrong.common.uniauth.server.data.entity.RoleExample;
import com.dianrong.common.uniauth.server.data.entity.Tag;
import com.dianrong.common.uniauth.server.data.entity.TagExample;
import com.dianrong.common.uniauth.server.data.entity.TagType;
import com.dianrong.common.uniauth.server.data.entity.TagTypeExample;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserGrpExample;
import com.dianrong.common.uniauth.server.data.entity.UserGrpKey;
import com.dianrong.common.uniauth.server.data.entity.UserRoleKey;
import com.dianrong.common.uniauth.server.data.entity.UserTagKey;
import com.dianrong.common.uniauth.server.data.entity.transfer.OldNewUser;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaCrmRoleNew;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaDomainNew;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaGroupNew;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaGroupRoleTypeNew;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaUserNew;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaUserRoleCrmNew;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpTagMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserGrpMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserRoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserTagMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.OldNewUserMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaCrmRoleNewMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaDomainNewMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaGroupNewMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaGroupRoleTypeNewMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaUserNewMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaUserRoleCrmNewMapper;
import com.google.common.collect.Maps;

@Service
public class TransferService implements TaskExecutor {
	
	private static final Log log = LogFactory.getLog(TransferService.class);

	@Autowired
	private TempUaDomainNewMapper domainNewMapper;
	@Autowired
	private TempUaCrmRoleNewMapper crmRoleNewMapper;
	@Autowired
	private TempUaGroupNewMapper groupNewMapper;
	@Autowired
	private TempUaGroupRoleTypeNewMapper groupRoleTypeNewMapper;
	@Autowired
	private TempUaUserNewMapper userNewMapper;
	@Autowired
	private TempUaUserRoleCrmNewMapper userRoleCrmNewMapper;
	@Autowired
	private OldNewUserMapper oldNewUserMapper;
	
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private DomainMapper domainMapper;
	@Autowired
	private UserTagMapper userTagMapper;
	@Autowired
	private GrpTagMapper grpTagMapper;
	
	@Autowired
	private GrpMapper grpMapper;
	
	@Autowired
	private TagMapper tagMapper;
	
	@Autowired
	private TagTypeMapper tagTypeMapper;
	
	@Autowired
	private UserGrpMapper userGrpMapper;
	
	
	@Override
	public void execut(String cmd) {
		List<TempUaUserNew> uaUser = null;
		Map<String,Object> m = Maps.newHashMap();
		m.put("startNum", cmd.split("\\-")[0]);
		m.put("endNum", cmd.split("\\-")[1]);
		if(cmd!=null && cmd.contains("-")){
			uaUser = userNewMapper.selectByRange(m);
		}else{
			log.error("cmd format error!cmd="+cmd);
			return;
		}
		//用户迁移
		for(TempUaUserNew uUser:uaUser){
			User user = new User();
	        user.setEmail(uUser.getEmail());
	        user.setName(uUser.getName());

	        Date now = new Date();
	        user.setFailCount(AppConstants.ZERO_Byte);

	        user.setPassword(uUser.getPassword());
	        user.setPasswordSalt(uUser.getPasswordSalt());

	        user.setLastUpdate(now);
	        user.setCreateDate(now);
	        user.setPhone(uUser.getPhone());
	        user.setStatus(AppConstants.ZERO_Byte);
	        user.setId(uUser.getId());
	        user.setPasswordDate(uUser.getPasswordDate());
	        Map<String,String> phone = Maps.newHashMap();
	        phone.put("email", uUser.getEmail());
	        try{
	        	List<User> selByEmail = userMapper.selectByEmailOrPhone(phone);
	        	User selUser = userMapper.selectByPrimaryKey(uUser.getId());
		        if(selByEmail != null && selByEmail.size() >0){
		        	OldNewUser oldNewUser = new OldNewUser();
		        	if(uUser.getId().equals(selByEmail.get(0).getId())){
		        		continue;
		        	}
		        	oldNewUser.setEmail(uUser.getEmail());
		        	oldNewUser.setOldUserId(uUser.getId());
		        	oldNewUser.setNewUserId(selByEmail.get(0).getId());
		        	oldNewUserMapper.insertSelective(oldNewUser);
		        }else if(selUser != null){
		        	//id重复的情况
		        	user.setId(null);
		        	userMapper.insert(user);
		        	phone.put("email", uUser.getEmail());
		        	List<User> selMail = userMapper.selectByEmailOrPhone(phone);
		        	if(selMail != null && selMail.size() >0){
			        	OldNewUser oldNewUser = new OldNewUser();
			        	oldNewUser.setEmail(uUser.getEmail());
			        	oldNewUser.setOldUserId(uUser.getId());
			        	oldNewUser.setNewUserId(selMail.get(0).getId());
			        	oldNewUserMapper.insertSelective(oldNewUser);
			        }
		        }else{
		        	userMapper.insert(user);
		        }
				userNewMapper.updateSuccess(user.getId());
	        }catch(Exception e){
	        	log.error("transfer user error,userid="+uUser.getId(), e);
	        }
			
		}
		
		Map<Integer,Integer> domainIdMappings = Maps.newConcurrentMap();
		List<TempUaDomainNew> allUaDomains = domainNewMapper.selectAll();
		if(allUaDomains != null){
			for(TempUaDomainNew uaDomain : allUaDomains){
				DomainExample domainExa = new DomainExample();
				domainExa.createCriteria().andCodeEqualTo(uaDomain.getName());
				List<Domain> doms = domainMapper.selectByExample(domainExa);
				if(doms != null && doms.size()>0){
					domainIdMappings.put(uaDomain.getId(), doms.get(0).getId());
				}
			}
		}
		
		List<TempUaCrmRoleNew> allRole = crmRoleNewMapper.selectAll();
		Map<Integer,Integer> roleMapping = Maps.newHashMap();
		for(TempUaCrmRoleNew crmRole : allRole){
			RoleExample roleExa = new RoleExample();
			roleExa.createCriteria().andNameEqualTo(crmRole.getName());
			List<Role> selRoles = roleMapper.selectByExample(roleExa);
			if(selRoles != null && selRoles.size()>0){
				roleMapping.put(crmRole.getId(), selRoles.get(0).getId());
			}
		}
		
		List<TempUaUserRoleCrmNew> uaUserRoles = userRoleCrmNewMapper.selectByRange(m);
		Map<Long,Long> oldNewUserMapping = Maps.newHashMap();
		if(uaUserRoles != null){
			
			List<OldNewUser> allOldNewUsers = oldNewUserMapper.selectAll();
			
			if(allOldNewUsers != null){
				for(OldNewUser ol:allOldNewUsers){
					oldNewUserMapping.put(ol.getOldUserId(), ol.getNewUserId());
				}
			}
			for(TempUaUserRoleCrmNew uaUserRole : uaUserRoles){
				 UserRoleKey userRoleKey = new UserRoleKey();
                userRoleKey.setRoleId(roleMapping.get(uaUserRole.getRoleId()));
                if(oldNewUserMapping.containsKey(uaUserRole.getUserId())){
                	userRoleKey.setUserId(oldNewUserMapping.get(uaUserRole.getUserId()));
                }else{
                	userRoleKey.setUserId(uaUserRole.getUserId());
                }
                try{
                	userRoleMapper.insert(userRoleKey);
                    Map<String,Object> mm = Maps.newHashMap();
                    mm.put("userId", uaUserRole.getUserId());
                    mm.put("roleId",uaUserRole.getRoleId());
                    userRoleCrmNewMapper.updateSuccess(mm);
                }catch(Exception e){
                	log.error("transfer user role error!userid="+uaUserRole.getUserId()+",roleid="+uaUserRole.getRoleId(), e);
                }
                
			}
		}
		
		//组与用户的关系
		List<TempUaGroupNew> allGroup = groupNewMapper.selectAll();
		Map<Integer,Integer> grpMapping = Maps.newHashMap();
		if(allGroup != null){
			for(TempUaGroupNew groupNew : allGroup){
				GrpExample grpExa = new GrpExample();
				grpExa.createCriteria().andCodeEqualTo("CRM_"+groupNew.getId()).andStatusEqualTo(AppConstants.ZERO_Byte);
				List<Grp> selGrp = grpMapper.selectByExample(grpExa);
				if(selGrp != null && selGrp.size()>0){
					grpMapping.put(groupNew.getId(), selGrp.get(0).getId());
				}
			}
		}
		
		if(uaUser !=null){
			for(TempUaUserNew uUser : uaUser){
				UserGrpKey userGrp = new UserGrpKey();
				userGrp.setType(AppConstants.ZERO_Byte);
				if(oldNewUserMapping.containsKey(uUser.getId())){
					userGrp.setUserId(oldNewUserMapping.get(uUser.getId()));
				}else{
					userGrp.setUserId(uUser.getId());
				}
				UserGrpExample usergrpExa = new UserGrpExample();
				usergrpExa.createCriteria().andUserIdEqualTo(userGrp.getUserId());
				if(userGrpMapper.countByExample(usergrpExa)>0){
					continue;
				}
				userGrp.setGrpId(grpMapping.get(uUser.getGroupId()));
				try{
					userGrpMapper.insert(userGrp);
				}catch(Exception e){
					log.error("transfer usergrp error!,userid:"+userGrp.getUserId(), e);
				}
			}
		}
		
		//tag迁移
		if(allGroup != null){
			TagTypeExample ttExa = new TagTypeExample();
			ttExa.createCriteria().andCodeEqualTo("GRP_TYPE");
			List<TagType> selTagTypes = tagTypeMapper.selectByExample(ttExa);
			for(TempUaGroupNew grp : allGroup){
				TagExample tagExa = new TagExample();
				if(grp.getGrpType() == null) continue;
				tagExa.createCriteria().andCodeEqualTo(grp.getGrpType()).andTagTypeIdEqualTo(selTagTypes.get(0).getId());
				List<Tag> tag = tagMapper.selectByExample(tagExa);
				if(tag != null && tag.size()>0){
					GrpTagKey grpTagKey = new GrpTagKey();
					grpTagKey.setGrpId(grpMapping.get(grp.getId()));
					grpTagKey.setTagId(tag.get(0).getId());
					try{
						grpTagMapper.insert(grpTagKey);
					}catch(Exception e){
						log.error("transfer tag error!grp_id:"+grp.getId(), e);
					}
				}
			}
		}
		
		if(uaUser != null){
			TagTypeExample ttExa = new TagTypeExample();
			ttExa.createCriteria().andCodeEqualTo("GRP_ROLE_TYPE");
			List<TagType> selTagTypes = tagTypeMapper.selectByExample(ttExa);
			List<TempUaGroupRoleTypeNew> allRoleTypes = groupRoleTypeNewMapper.select();
			Map<Integer,Integer> groupRoleMapping = Maps.newHashMap();
			if(allRoleTypes != null){
				for(TempUaGroupRoleTypeNew groupRoleType : allRoleTypes){
					if(groupRoleType.getGrtCode() == null) continue;
					TagExample tagExa = new TagExample();
					tagExa.createCriteria().andCodeEqualTo(groupRoleType.getGrtCode()).andTagTypeIdEqualTo(selTagTypes.get(0).getId());
					List<Tag> tag = tagMapper.selectByExample(tagExa);
					if(tag != null && tag.size()>0){
						groupRoleMapping.put(groupRoleType.getId(), tag.get(0).getId());
					}
				}
			}
			for(TempUaUserNew uUser : uaUser){
				UserTagKey userTagKey = new UserTagKey();
				userTagKey.setTagId(groupRoleMapping.get(uUser.getGroupRole()));
				if(oldNewUserMapping.containsKey(uUser.getId())){
					userTagKey.setUserId(oldNewUserMapping.get(uUser.getId()));
				}else{
					userTagKey.setUserId(uUser.getId());
				}
				try{
					userTagMapper.insert(userTagKey);
				}catch(Exception e){
					log.error("transfer userTag error!userid="+uUser.getId(), e);
				}
			}
		}
	}
}
