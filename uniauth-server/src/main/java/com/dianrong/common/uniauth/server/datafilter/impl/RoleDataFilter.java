package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Role;
import com.dianrong.common.uniauth.server.data.entity.RoleExample;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

/**
 * . 角色的数据过滤处理实现.
 * 
 * @author wanglin
 */
@Service("roleDataFilter")
public class RoleDataFilter extends CurrentAbstractDataFilter {

	@Autowired
	private RoleMapper roleMapper;

	@Override
	protected boolean dataWithConditionsEqualExist(FilterData... equalsField) {
		// 判空处理
		if (equalsField == null || equalsField.length == 0) {
			return false;
		}
		// 首先根据类型和值获取到对应的model数组
		RoleExample condition = new RoleExample();
		RoleExample.Criteria criteria = condition.createCriteria();

		criteria.andStatusEqualTo(AppConstants.ZERO_Byte);
		// 构造查询条件
		for (FilterData fd : equalsField) {
			switch (fd.getType()) {
			case FIELD_TYPE_DOMAIN_ID:
				criteria.andDomainIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
				break;
			case FIELD_TYPE_NAME:
				criteria.andNameEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
				break;
			case FIELD_TYPE_ROLE_CODE_ID:
				criteria.andRoleCodeIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
				break;
			default:
				break;
			}
		}
		// 查询
		int count = roleMapper.countByExample(condition);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	protected Object getRecordByPrimaryKey(Integer id) {
		CheckEmpty.checkEmpty(id, "roleId");
		RoleExample condition = new RoleExample();
		condition.createCriteria().andIdEqualTo(id);
		List<Role> selectByExample = roleMapper.selectByExample(condition);

		if (selectByExample != null && !selectByExample.isEmpty()) {
			return  selectByExample.get(0);
		}
		return null;
	};
	
	@Override
	protected String getProcessTableName() {
		return UniBundle.getMsg("data.filter.table.name.role");
	}
}
