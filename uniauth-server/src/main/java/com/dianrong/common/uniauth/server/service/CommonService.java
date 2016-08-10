package com.dianrong.common.uniauth.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.server.data.entity.PermType;
import com.dianrong.common.uniauth.server.data.entity.PermTypeExample;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.RoleCodeExample;
import com.dianrong.common.uniauth.server.data.mapper.PermTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;

@Service
public class CommonService {
	
	@Autowired
	private PermTypeMapper permTypeMapper;
	@Autowired
	private RoleCodeMapper roleCodeMapper;
	
	@Cacheable(value="commonService")
	public Map<Integer, PermType> getPermTypeMap(){
		Map<Integer, PermType>	permTypeMap = new HashMap<Integer, PermType>();
		PermTypeExample example = new PermTypeExample();
		List<PermType> permTypeList = permTypeMapper.selectByExample(example);
		if(permTypeList != null && !permTypeList.isEmpty()){
			for(PermType permType: permTypeList){
				permTypeMap.put(permType.getId(), permType);
			}
		}
		return permTypeMap;
		
	}
	
	public Map<Integer, RoleCode> getRoleCodeMap(){
		Map<Integer, RoleCode>	roleCodeMap = new HashMap<Integer, RoleCode>();
		RoleCodeExample example = new RoleCodeExample();
		List<RoleCode> roleCodeList = roleCodeMapper.selectByExample(example);
		if(roleCodeList != null && !roleCodeList.isEmpty()){
			for(RoleCode rc: roleCodeList){
				roleCodeMap.put(rc.getId(), rc);
			}
		}
		return roleCodeMap;
	}
}
