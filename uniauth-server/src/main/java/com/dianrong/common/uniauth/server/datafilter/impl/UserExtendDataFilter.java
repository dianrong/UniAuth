package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.server.data.entity.UserExtend;
import com.dianrong.common.uniauth.server.data.entity.UserExtendExample;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

/**
 * . 用户扩展字段数据过滤处理
 * 
 * @author wanglin
 */
@Service("userExtendDataFilter")
public class UserExtendDataFilter extends CurrentAbstractDataFilter<UserExtend> {
    @Autowired
    private UserExtendMapper userExtendMapper;

    @Override
    protected String getProcessTableName() {
        return UniBundle.getMsg("data.filter.table.name.userextend");
    }

    @Override
    protected boolean multiFieldsDuplicateCheck(FilterData... equalsField) {
        UserExtendExample condition = new UserExtendExample();
        UserExtendExample.Criteria criteria = condition.createCriteria();
        criteria.andTenancyIdEqualTo(getTenancyId());
        // 构造查询条件
        for (FilterData fd : equalsField) {
            switch (fd.getType()) {
                case FIELD_TYPE_CODE:
                    criteria.andCodeEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
                    break;
                default:
                    break;
            }
        }
        // 查询
        int count = userExtendMapper.countByExample(condition);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    protected UserExtend getEnableRecordByPrimaryKey(Integer id) {
        CheckEmpty.checkEmpty(id, "userExtendId");
        UserExtendExample condition = new UserExtendExample();
        condition.createCriteria().andIdEqualTo(new Long(id.toString()));
        List<UserExtend> selectByExample = userExtendMapper.selectByExample(condition);
        if (selectByExample != null && !selectByExample.isEmpty()) {
            return selectByExample.get(0);
        }
        return null;
    }
}
