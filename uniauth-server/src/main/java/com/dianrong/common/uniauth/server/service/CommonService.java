package com.dianrong.common.uniauth.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.cache.UniauthCommonCache;
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
	
	public Map<Integer, PermType> getPermTypeMap(){
		
		try {
			return UniauthCommonCache.getInstance().get("permType", 1000*60*60, new Callable<Map<Integer, PermType>>(){

				@Override
				public Map<Integer, PermType> call() throws Exception {
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
				
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public Map<Integer, RoleCode> getRoleCodeMap(){
		try {
			return UniauthCommonCache.getInstance().get("roleCode", 1000*60*60, new Callable<Map<Integer, RoleCode>>() {

				@Override
				public Map<Integer, RoleCode> call() throws Exception {
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
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
