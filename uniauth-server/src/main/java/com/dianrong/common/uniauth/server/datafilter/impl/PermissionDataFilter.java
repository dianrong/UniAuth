package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Permission;
import com.dianrong.common.uniauth.server.data.entity.PermissionExample;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

/**.
 * 权限的数据过滤处理实现.
 * @author wanglin
 */
@Service("permissionDataFilter")
public class PermissionDataFilter extends CurrentAbstractDataFilter {
	
	@Autowired
	private PermissionMapper permissionMapper;
	
	
	@Override
	protected Object getRecordByPrimaryKey(Integer id){
		CheckEmpty.checkEmpty(id, "permission的ID");
		return  permissionMapper.selectByIdWithStatusEffective(id);
	}
	
	@Override
	protected boolean dataWithConditionsEqualExist(FilterData... equalsField) {
		//判空处理
				if(equalsField == null || equalsField.length == 0) {
					return false;
				}
				//首先根据类型和值获取到对应的model数组
				PermissionExample condition = new PermissionExample();
				PermissionExample.Criteria criteria =  condition.createCriteria();
				
				criteria.andStatusEqualTo(AppConstants.ZERO_Byte);
				//构造查询条件
				for(FilterData fd: equalsField){
					switch(fd.getType()) {
						case FIELD_TYPE_ID:
							criteria.andIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
							break;
						case FIELD_TYPE_VALUE:
							criteria.andValueEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
							break;
						case FIELD_TYPE_PERM_TYPE_ID:
							criteria.andPermTypeIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
							break;
						case FIELD_TYPE_DOMAIN_ID:
							criteria.andDomainIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
							break;
						default:
							break;
					}
				}
				//查询
				List<Permission> permissiones = permissionMapper.selectByExample(condition);
				if(permissiones != null && !permissiones.isEmpty()){
					return true;
				}
				return false;
	}

	@Override
	protected String getProcessTableName() {
		return UniBundle.getMsg("data.filter.table.name.permission");
	}
}
