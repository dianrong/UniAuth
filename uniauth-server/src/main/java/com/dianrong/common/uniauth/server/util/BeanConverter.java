package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.dto.*;
import com.dianrong.common.uniauth.common.bean.request.*;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.entity.ext.PermissionExt;
import com.dianrong.common.uniauth.server.data.entity.ext.RoleExt;
import com.dianrong.common.uniauth.server.data.entity.ext.UrlRoleMappingExt;

/**
 * Created by Arc on 15/1/16.
 */
public class BeanConverter {

    public static RoleCodeDto convert(RoleCode roleCode) {
        if(roleCode == null) {
            return null;
        } else {
            return new RoleCodeDto().setCode(roleCode.getCode()).setId(roleCode.getId()).setDescription(roleCode.getDescription());
        }
    }

    public static ConfigDto convert(Cfg cfg) {
        if(cfg == null) {
            return null;
        } else {
            return new ConfigDto()
                    .setId(cfg.getId())
                    .setKey(cfg.getKey())
                    .setType(cfg.getType())
                    .setFile(cfg.getFile())
                    .setValue(cfg.getValue());
        }
    }


    public static DomainDto convert(Domain domain) {
        if(domain == null) {
            return null;
        } else {
            return new DomainDto().setCode(domain.getCode()).setCreateDate(domain.getCreateDate()).setDescription(domain.getDescription()).setDisplayName(domain.getDisplayName()).setId(domain.getId()).setLastUpdate(domain.getLastUpdate()).setStatus(domain.getStatus());
        }
    }
    

    public static Domain convert(DomainParam domainParam) {
        if(domainParam == null) {
            return null;
        } else {
        	Domain domain = new Domain();
        	domain.setCode(domainParam.getCode());
        	domain.setDescription(domainParam.getDescription());
        	domain.setDisplayName(domainParam.getDisplayName());
        	domain.setId(domainParam.getId());
        	domain.setStatus(domainParam.getStatus());
        	return domain;
        }
    }
    
    public static Grp convert(GroupParam groupParam) {
        if(groupParam == null) {
            return null;
        } else {
            Grp grp = new Grp();
            grp.setDescription(groupParam.getDescription());
            grp.setCode(groupParam.getCode());
            grp.setId(groupParam.getId());
            grp.setName(groupParam.getName());
            grp.setStatus(groupParam.getStatus());
            return grp;
        }
    }

    public static GroupDto convert(Grp grp) {
        if(grp == null) {
            return null;
        } else {
            GroupDto groupDto = new GroupDto();
            groupDto.setCode(grp.getCode());
            groupDto.setId(grp.getId());
            groupDto.setName(grp.getName());
            groupDto.setDescription(grp.getDescription());
            groupDto.setStatus(grp.getStatus());
            groupDto.setCreateDate(grp.getCreateDate());
            groupDto.setLastUpdate(grp.getLastUpdate());
            return groupDto;
        }
    }

    public static StakeholderDto convert(Stakeholder stakeholder) {
        if(stakeholder == null) {
            return null;
        } else {
            return new StakeholderDto().setDomainId(stakeholder.getDomainId()).setEmail(stakeholder.getEmail()).setId(stakeholder.getId()).setJobtitle(stakeholder.getJobtitle()).setName(stakeholder.getName()).setPhone(stakeholder.getPhone());
        }
    }


    public static RoleDto convert(Role role) {
        if(role == null) {
            return null;
        } else {
            RoleDto roleDto = new RoleDto();
            roleDto.setDescription(role.getDescription()).
                    setId(role.getId()).
                    setStatus(role.getStatus()).
                    setName(role.getName()).
                    setRoleCodeId(role.getRoleCodeId()).
                    setDomainId(role.getDomainId());
            return roleDto;
        }
    }
    
    public static Stakeholder convert(StakeholderParam stakeholderParam, boolean needSetId) {
        if(stakeholderParam == null) {
            return null;
        } else {
        	Stakeholder stakeholder = new Stakeholder();
        	if(needSetId){
            	stakeholder.setId(stakeholderParam.getId());
        	}
        	stakeholder.setDomainId(stakeholderParam.getDomainId());
        	stakeholder.setEmail(stakeholderParam.getEmail());
        	stakeholder.setJobtitle(stakeholderParam.getJobtitle());
        	stakeholder.setName(stakeholderParam.getName());
        	stakeholder.setPhone(stakeholderParam.getPhone());
        	return stakeholder;
        }
    }
    
    public static UserDto convert(User user) {
        if(user == null) {
            return null;
        } else {
            UserDto userDto = new UserDto();
            userDto.setName(user.getName())
                    .setEmail(user.getEmail())
                    .setId(user.getId())
                    .setPhone(user.getPhone())
                    .setCreateDate(user.getCreateDate())
                    .setStatus(user.getStatus())
                    .setLastUpdate(user.getLastUpdate());
            return userDto;
        }
    }

    public static PermTypeDto convert(PermType pt) {
        if(pt == null) {
            return null;
        } else {
            return new PermTypeDto().setId(pt.getId()).setType(pt.getType());
        }
    }
    
    public static Permission convert(PermissionParam permissionParam, boolean needSetId) {
        if(permissionParam == null) {
            return null;
        } else {
        	Permission permission = new Permission();
        	if(needSetId){
            	permission.setId(permissionParam.getId());
        	}
        	permission.setDescription(permissionParam.getDescription());
        	permission.setDomainId(permissionParam.getDomainId());
        	permission.setPermTypeId(permissionParam.getPermTypeId());
        	permission.setStatus(permissionParam.getStatus());
        	permission.setValue(permissionParam.getValue());
        	return permission;
        }
    }
    

    public static PermissionDto convert(Permission permission) {
        if(permission == null) {
            return null;
        } else {
        	return new PermissionDto().setDescription(permission.getDescription()).
                    setDomainId(permission.getDomainId()).setId(permission.getId())
                    .setPermTypeId(permission.getPermTypeId()).
                            setStatus(permission.getStatus()).
                            setValue(permission.getValue());
        }
    }
    
    
    public static RoleDto convert(RoleExt roleExt) {
        if(roleExt == null) {
            return null;
        } else {
            RoleDto roleDto = new RoleDto();
            roleDto.setDescription(roleExt.getDescription()).
                    setId(roleExt.getId()).
                    setStatus(roleExt.getStatus()).
                    setName(roleExt.getName()).setRoleCodeId(roleExt.getRoleCodeId()).setDomainId(roleExt.getDomainId());
            
            roleDto.setRoleCodeId(roleExt.getRoleCodeId());
            roleDto.setRoleCode(roleExt.getRoleCode());
            
            roleDto.setPermissionId(roleExt.getPermissionId());
            return roleDto;
        }
    }
    
    public static PermissionExt convert(PermissionQuery permissionQuery) {
        if(permissionQuery == null) {
            return null;
        } else {
        	PermissionExt permissionExt = new PermissionExt();
        	permissionExt.setDomainId(permissionQuery.getDomainId());
        	permissionExt.setStatus(permissionQuery.getStatus());
        	permissionExt.setValue(permissionQuery.getValue());
        	permissionExt.setPermTypeId(permissionQuery.getPermTypeId());
        	return permissionExt;
        }
    }
    
    public static UrlRoleMappingDto convert(UrlRoleMappingExt urlRoleMappingExt){
    	if(urlRoleMappingExt == null){
    		return null;
    	}
    	else{
    		UrlRoleMappingDto urlRoleMappingDto = new UrlRoleMappingDto();
    		urlRoleMappingDto.setPermType(urlRoleMappingExt.getPermType());
    		urlRoleMappingDto.setPermUrl(urlRoleMappingExt.getPermUrl());
    		urlRoleMappingDto.setRoleCode(urlRoleMappingExt.getRoleCode());
    		
    		return urlRoleMappingDto;
    	}
    }

    public static AuditDto convert(Audit audit){
        if(audit == null){
            return null;
        }
        else{
            AuditDto auditDto = new AuditDto();
            auditDto.setDomainId(audit.getDomainId());
            auditDto.setReqElapse(audit.getReqElapse());
            auditDto.setReqClass(audit.getReqClass());
            auditDto.setReqException(audit.getReqExp());
            auditDto.setReqMethod(audit.getReqMethod())
                    .setReqSuccess(audit.getReqSuccess())
                    .setReqUuid(audit.getReqUuid())
                    .setReqResult(audit.getReqResult())
                    .setReqIp(audit.getReqIp())
                    .setReqUrl(audit.getReqUrl())
                    .setReqParam(audit.getReqParam())
                    .setRequestDate(audit.getReqDate())
                    .setReqClass(audit.getReqClass())
                    .setUserId(audit.getUserId())
                    .setId(audit.getId())
                    .setReqSequence(audit.getReqSeq());
            return auditDto;
        }
    }
}
