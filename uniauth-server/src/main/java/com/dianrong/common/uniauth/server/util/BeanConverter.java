package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.Role;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.Stakeholder;
import com.dianrong.common.uniauth.server.data.entity.User;

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


    public static DomainDto convert(Domain domain) {
        if(domain == null) {
            return null;
        } else {
            return new DomainDto().setCode(domain.getCode()).setCreateDate(domain.getCreateDate()).setDescription(domain.getDescription()).setDisplayName(domain.getDisplayName()).setId(domain.getId()).setLastUpdate(domain.getLastUpdate()).setStatus(domain.getStatus());
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
                    setName(role.getName());
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
            userDto.setName(user.getName()).
                    setEmail(user.getEmail()).
                    setId(user.getId()).
                    setPhone(user.getPhone());
            return userDto;
        }
    }
    
}
