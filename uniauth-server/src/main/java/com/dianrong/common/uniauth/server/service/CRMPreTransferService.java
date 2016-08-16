package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.TaskInvoker.TaskExecutor;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.DomainExample;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.GrpExample;
import com.dianrong.common.uniauth.server.data.entity.GrpTagKey;
import com.dianrong.common.uniauth.server.data.entity.Permission;
import com.dianrong.common.uniauth.server.data.entity.PermissionExample;
import com.dianrong.common.uniauth.server.data.entity.Role;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.RoleExample;
import com.dianrong.common.uniauth.server.data.entity.RolePermissionKey;
import com.dianrong.common.uniauth.server.data.entity.Tag;
import com.dianrong.common.uniauth.server.data.entity.TagExample;
import com.dianrong.common.uniauth.server.data.entity.TagType;
import com.dianrong.common.uniauth.server.data.entity.TagTypeExample;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaCrmRoleNew;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaDomainNew;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaGroupNew;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaRoleUriPatternNew;
import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaUriPatternNew;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpTagMapper;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.RolePermissionMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaCrmRoleNewMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaDomainNewMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaGroupNewMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaRoleUriPatternNewMapper;
import com.dianrong.common.uniauth.server.data.mapper.transfer.TempUaUriPatternNewMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class CRMPreTransferService implements TaskExecutor {

	private static final Log log = LogFactory.getLog(TransferService.class);

	@Autowired
	private TempUaDomainNewMapper domainNewMapper;
	@Autowired
	private TempUaCrmRoleNewMapper crmRoleNewMapper;
	@Autowired
	private TempUaGroupNewMapper groupNewMapper;
	@Autowired
	private TempUaUriPatternNewMapper uriPatternNewMapper;
	@Autowired
	private TempUaRoleUriPatternNewMapper tempUaRoleUriMapper;

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
	@Autowired
	private GrpTagMapper grpTagMapper;

	@Autowired
	private GrpMapper grpMapper;

	@Autowired
	private GroupService groupService;

	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	@Autowired
	private TagMapper tagMapper;

	@Autowired
	private TagTypeMapper tagTypeMapper;

	@Override
	public void execut(String cmd) {

		// domain迁移
		List<TempUaDomainNew> uaDomains = domainNewMapper.select();
		if (uaDomains != null) {
			for (TempUaDomainNew uaDomain : uaDomains) {
				int cnt = domainMapper.countDomainByCodeWithStatusEffective(uaDomain.getName());
				if (cnt == 0) {
					Domain d = new Domain();
					d.setCode(uaDomain.getName());
					d.setDescription(uaDomain.getDescription());
					d.setDisplayName(uaDomain.getDisplayName());
					d.setStatus(AppConstants.ZERO_Byte);
					d.setCreateDate(new Date());
					d.setLastUpdate(new Date());
					domainMapper.insert(d);
				}
				domainNewMapper.updateSuccess(uaDomain.getId());
			}
		}

		DomainExample domExa = new DomainExample();
		domExa.createCriteria().andCodeEqualTo("crm");
		List<Domain> crmDomain = domainMapper.selectByExample(domExa);
		Integer crmDomainId = null;
		if (crmDomain != null && crmDomain.size() > 0) {
			crmDomainId = crmDomain.get(0).getId();
		}else{
			log.error("没有找到CRM对应 的doamin!");
		}

		GrpExample grpExaCrm = new GrpExample();
		grpExaCrm.createCriteria().andCodeEqualTo("CRM_ROOT_GROUP");
		List<Grp> crmGrpCode = grpMapper.selectByExample(grpExaCrm);
		Integer crmGrpId = null;
		if (crmGrpCode != null && crmGrpCode.size() > 0) {
			crmGrpId = crmGrpCode.get(0).getId();
		} else {
			log.error("没有找到CRM_ROOT_GROUP的组!");
		}

		// 组迁移
		List<TempUaGroupNew> uaGroups = groupNewMapper.select();
		if (uaGroups != null && crmGrpId != null) {
			List<GroupTree> groups = Lists.newArrayList();
			for (TempUaGroupNew uaGroup : uaGroups) {
				if (uaGroup.getParent() == null) {
					groups.add(new GroupTree(new ArrayList<GroupTree>(), uaGroup));
				}
			}

			for (GroupTree gTree : groups) {
				initGroupTree(gTree, uaGroups);
			}

			for (GroupTree gTree : groups) {
				insertGroup(gTree, crmGrpId);
			}

		}

		// role迁移
		List<TempUaCrmRoleNew> crmRoles = crmRoleNewMapper.select();
		if (crmRoles != null) {

			for (TempUaCrmRoleNew uaRole : crmRoles) {
				RoleCode rc = new RoleCode();
				rc.setCode(uaRole.getRoleCode());
				rc.setDescription(uaRole.getDescription());
				try {
					roleCodeMapper.insert(rc);
				} catch (Exception e) {
					log.error("transfer role error!,role:"+uaRole.getId(), e);
				}
			}

			Map<Integer, RoleCode> roleCodeMap = commonService.getRoleCodeMap();
			if(crmDomainId != null){
				for (TempUaCrmRoleNew uaRole : crmRoles) {
					Role role = new Role();
					role.setDomainId(crmDomainId);
					role.setName(uaRole.getName());
					role.setStatus(uaRole.getStatus());
					RoleCode rrc = null;
					for (Entry<Integer, RoleCode> r : roleCodeMap.entrySet()) {
						if (r.getValue().getCode().equals(uaRole.getRoleCode())) {
							rrc = r.getValue();
							break;
						}
					}
					role.setRoleCodeId(rrc == null ? null : rrc.getId());
					role.setStatus(AppConstants.ZERO_Byte);
					role.setDescription(uaRole.getDescription());
					roleMapper.insert(role);
					crmRoleNewMapper.updateSuccess(uaRole.getId());
				}
			}
		}

		Map<Integer, Integer> domainIdMappings = Maps.newConcurrentMap();
		List<TempUaDomainNew> allUaDomains = domainNewMapper.selectAll();
		if (allUaDomains != null) {
			for (TempUaDomainNew uaDomain : allUaDomains) {
				DomainExample domainExa = new DomainExample();
				domainExa.createCriteria().andCodeEqualTo(uaDomain.getName());
				List<Domain> doms = domainMapper.selectByExample(domainExa);
				if (doms != null && doms.size() > 0) {
					domainIdMappings.put(uaDomain.getId(), doms.get(0).getId());
				}
			}
		}
		// perm迁移
		List<TempUaUriPatternNew> uaUriPattern = uriPatternNewMapper.select();
		if (uaUriPattern != null) {
			for (TempUaUriPatternNew uriPattern : uaUriPattern) {
				try{
					PermissionExample permExa = new PermissionExample();
					permExa.createCriteria().andValueEqualTo(uriPattern.getPattern()).andPermTypeIdEqualTo(2)// FIXME 修改为正式URL-PATTERN的id
							.andDomainIdEqualTo(domainIdMappings.get(uriPattern.getDomainId()));
					List<Permission> value = permissionMapper.selectByExample(permExa);
					if(value!= null && !value.isEmpty()) continue;
					Permission p = new Permission();
					p.setDescription(uriPattern.getDescription());
					p.setValue(uriPattern.getPattern());
					p.setPermTypeId(2);// FIXME 修改为正式URL-PATTERN的id
					p.setDomainId(domainIdMappings.get(uriPattern.getDomainId()));
					p.setStatus(AppConstants.ZERO_Byte);
					permissionMapper.insert(p);
					uriPatternNewMapper.updateSuccess(uriPattern.getId());
				}catch(Exception e){
					log.error("transfer permission error,pattern="+uriPattern.getPattern(), e);
				}
				
			}
		}

		// rolePermission
		List<TempUaRoleUriPatternNew> roleUris = tempUaRoleUriMapper.select();
		if (roleUris != null) {
			Map<Integer, Integer> permMapping = Maps.newHashMap();
			List<TempUaUriPatternNew> allUris = uriPatternNewMapper.selectAll();
			if (allUris != null) {
				for (TempUaUriPatternNew uri : allUris) {
					PermissionExample permExa = new PermissionExample();
					permExa.createCriteria().andDomainIdEqualTo(domainIdMappings.get(uri.getDomainId()))
							.andValueEqualTo(uri.getPattern());
					List<Permission> selPerms = permissionMapper.selectByExample(permExa);
					if (selPerms != null && selPerms.size() > 0) {
						permMapping.put(uri.getId(), selPerms.get(0).getId());
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
			
			for (TempUaRoleUriPatternNew roleUri : roleUris) {
				RolePermissionKey rolePerKey = new RolePermissionKey();
				rolePerKey.setPermissionId(permMapping.get(roleUri.getUriPatternId()));
				rolePerKey.setRoleId(roleMapping.get(roleUri.getRoleId()));
				try {
					rolePermissionMapper.insert(rolePerKey);
					Map<String, Object> map = Maps.newHashMap();
					map.put("roleId", roleUri.getRoleId());
					map.put("uriId", roleUri.getUriPatternId());
					tempUaRoleUriMapper.updateSuccess(map);
				} catch (Exception e) {
					log.error("transfer rolePermission error,data="+roleUri.getRoleId()+","+roleUri.getUriPatternId(), e);
				}
			}
		}
		
		
		
		//tag迁移
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

	}

	private void insertGroup(GroupTree gTree, Integer parentId) {
		GroupParam groupParam = new GroupParam();
		groupParam.setTargetGroupId(parentId);
		groupParam.setCode("CRM_" + gTree.getRelGroup().getId());
		groupParam.setName(gTree.getRelGroup().getName());
		groupParam.setDescription(gTree.getRelGroup().getDescription());
		groupParam.setStatus(gTree.getRelGroup().getStatus());
		GroupDto cG = groupService.createDescendantGroup(groupParam);
		if (gTree.getChildren().size() > 0) {
			for (GroupTree gChild : gTree.getChildren()) {
				insertGroup(gChild, cG.getId());
			}
		}
		groupNewMapper.updateSuccess(gTree.getRelGroup().getId());
	}

	private void initGroupTree(GroupTree tree, List<TempUaGroupNew> groups) {
		for (TempUaGroupNew g : groups) {
			if (g.getParent() != null && g.getParent().equals(tree.getRelGroup().getId())) {
				GroupTree t = new GroupTree(new ArrayList<GroupTree>(), g);
				initGroupTree(t, groups);
				tree.getChildren().add(t);
			}
		}
	}

	private static class GroupTree {
		private List<GroupTree> children;
		private TempUaGroupNew relGroup;

		public GroupTree(List<GroupTree> children, TempUaGroupNew relGroup) {
			super();
			this.children = children;
			this.relGroup = relGroup;
		}

		public List<GroupTree> getChildren() {
			return children;
		}

		public TempUaGroupNew getRelGroup() {
			return relGroup;
		}

	}

}
