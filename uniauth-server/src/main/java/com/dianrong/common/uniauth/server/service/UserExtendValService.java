package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserExample;
import com.dianrong.common.uniauth.server.data.entity.UserExtend;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample.Criteria;
import com.dianrong.common.uniauth.server.data.entity.ext.UserExtendValExt;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendValMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.datafilter.impl.UserExtendValDataFilter;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.google.common.collect.Lists;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
@Service
public class UserExtendValService extends TenancyBasedService {

    @Autowired
    private UserExtendValMapper userExtendValMapper;

    @Autowired
    private UserExtendMapper userExtendMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Resource(name = "userExtendValDataFilter")
    private UserExtendValDataFilter dataFilter;

    /**
     * 添加一个用户扩展属性值
     * 
     * @param userId
     * @param extendId
     * @param value
     * @param status
     * @return
     */
    public UserExtendValDto add(Long userId, Long extendId, String value, Byte status) {
        CheckEmpty.checkEmpty(userId, "user_id");
        CheckEmpty.checkEmpty(extendId, "extend_id");

        // 数据过滤
        dataFilter.addFieldsCheck(FilterType.FILTER_TYPE_EXSIT_DATA, FilterData.buildFilterData(FieldType.FIELD_TYPE_USER_ID, userId),
                FilterData.buildFilterData(FieldType.FIELD_TYPE_EXTEND_ID, extendId));

        UserExtendVal userExtendVal = new UserExtendVal();
        userExtendVal.setExtendId(extendId);
        userExtendVal.setStatus(status);
        userExtendVal.setUserId(userId);
        userExtendVal.setValue(value);
        userExtendVal.setTenancyId(tenancyService.getTenancyIdWithCheck());

        userExtendValMapper.insertSelective(userExtendVal);

        return BeanConverter.convert(userExtendVal, UserExtendValDto.class);
    }

    /**
     * 根据扩展属性值的主键id删除数据
     * 
     * @return 删除的个数
     */
    public int delById(Long id) {
        CheckEmpty.checkEmpty(id, "id");
        UserExtendVal userExtendVal = new UserExtendVal();
        userExtendVal.setId(id);
        userExtendVal.setStatus(AppConstants.STATUS_DISABLED);
        return userExtendValMapper.updateByPrimaryKeySelective(userExtendVal);
    }

    /**
     * 根据扩展属性值的主键id修改数据
     * 
     * @param id
     * @param userId
     * @param extendId
     * @param value
     * @param status
     * @return
     */
    public int updateById(Long id, Long userId, Long extendId, String value, Byte status) {
        CheckEmpty.checkEmpty(id, "id");
        // 过滤数据
        List<FilterData> filterFileds = new ArrayList<FilterData>();
        if (userId != null) {
            filterFileds.add(FilterData.buildFilterData(FieldType.FIELD_TYPE_USER_ID, userId));
        }
        if (extendId != null) {
            filterFileds.add(FilterData.buildFilterData(FieldType.FIELD_TYPE_EXTEND_ID, extendId));
        }
        if (!filterFileds.isEmpty()) {
            dataFilter.updateFieldsCheck(TypeParseUtil.parseToIntegerFromObject(id), filterFileds.toArray(new FilterData[filterFileds.size()]));
        }

        UserExtendVal userExtendVal = new UserExtendVal();
        userExtendVal.setId(id);
        userExtendVal.setExtendId(extendId);
        userExtendVal.setStatus(status);
        userExtendVal.setUserId(userId);
        userExtendVal.setValue(value);
        return userExtendValMapper.updateByPrimaryKeySelective(userExtendVal);
    }

    /**
     * 根据用户id查询扩展属性值
     * 
     * @param userId
     * @param status 启用禁用状态
     * @return
     */
    public List<UserExtendValDto> searchByUserId(Long userId, Byte status) {
        CheckEmpty.checkEmpty(userId, "userId");

        UserExtendValExample example = new UserExtendValExample();
        Criteria criteria = example.createCriteria().andUserIdEqualTo(userId);
        if (status != null) {
            criteria.andStatusEqualTo(status);
        }
        criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());

        List<UserExtendVal> userExtendVals = userExtendValMapper.selectByExample(example);
        List<UserExtendValDto> userExtendValDtos = new ArrayList<UserExtendValDto>();
        UserExtendValDto userExtendValDto;
        UserExtend userExtend;
        for (UserExtendVal userExtendVal : userExtendVals) {
            userExtendValDto = BeanConverter.convert(userExtendVal, UserExtendValDto.class);
            userExtend = userExtendMapper.selectByPrimaryKey(userExtendValDto.getExtendId());
            userExtendValDto.setExtendCode(userExtend.getCode());
            userExtendValDto.setExtendDescription(userExtend.getDescription());
            userExtendValDtos.add(userExtendValDto);
        }

        return userExtendValDtos;
    }

    /**
     * 根据用户id和code分页查询数据
     * 
     * @param userId 必填
     * @param code 可选
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageDto<UserExtendValDto> searchByUserIdAndCode(Long userId, String code, Integer pageNumber, Integer pageSize, boolean queryOnlyUsed) {
        CheckEmpty.checkEmpty(userId, "userId");
        CheckEmpty.checkEmpty(pageNumber, "pageNumber");
        CheckEmpty.checkEmpty(pageSize, "pageSize");

        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId.toString());
        params.put("extendCode", code == null ? null : '%' + code + '%');
        params.put("tenancyId", tenancyService.getTenancyIdWithCheck().toString());

        int count = queryOnlyUsed ? userExtendValMapper.countByUserExtend(params) : userExtendValMapper.countByCode(params);
        ParamCheck.checkPageParams(pageNumber, pageSize, count);
        params.put("queryOnlyUsed", String.valueOf(queryOnlyUsed));
        params.put("startIndex", String.valueOf(pageNumber * pageSize));
        params.put("pageSize", pageSize.toString());
        List<UserExtendValExt> userExtendValExts = userExtendValMapper.selectByUserIdAndCode(params);
        // 转换
        List<UserExtendValDto> userExtendDtos = new ArrayList<UserExtendValDto>();

        for (UserExtendValExt userExtendValExt : userExtendValExts) {
            userExtendDtos.add(BeanConverter.convert(userExtendValExt, UserExtendValDto.class));
        }
        // 生成分页对象
        PageDto<UserExtendValDto> pageDto = new PageDto<UserExtendValDto>(pageNumber, pageSize, count, userExtendDtos);
        return pageDto;
    }

    /**
     * 根据条件查询用户扩展属性值列表
     * 
     * @param extendId 扩展属性id
     * @param value 扩展属性值
     * @param status 状态
     * @param includeDisableUserRelatedExtendVal 是否包含禁用用户关联的扩展属性值
     * @return 符合条件的扩展属性值列表
     */
    public List<UserExtendValDto> search(Long extendId, String value, Byte status, Boolean includeDisableUserRelatedExtendVal) {
        CheckEmpty.checkEmpty(extendId, "extendId");
        CheckEmpty.checkEmpty(value, "value");

        UserExtendValExample example = new UserExtendValExample();
        Criteria criteria = example.createCriteria();
        criteria.andExtendIdEqualTo(extendId).andValueEqualTo(value);
        if (status != null) {
            criteria.andStatusEqualTo(status);
        }
        criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
        List<UserExtendVal> userExtendVals = userExtendValMapper.selectByExample(example);

        List<UserExtendValDto> userExtendValDtos = Lists.newArrayList();
        if (userExtendVals == null || userExtendVals.isEmpty()) {
            return userExtendValDtos;
        }
        
        // 过滤禁用用户关联的属性值信息
        if (!(includeDisableUserRelatedExtendVal !=null && includeDisableUserRelatedExtendVal)) {
            UserExample userExample = new UserExample();
            List<User> selectByExample = userMapper.selectByExample(userExample);
        }

        UserExtend extend = userExtendMapper.selectByPrimaryKey(extendId);
        if (extend == null) {
            return userExtendValDtos;
        }

        for (UserExtendVal extendVal : userExtendVals) {
            UserExtendValDto userExtendValDto = BeanConverter.convert(extendVal, UserExtendValDto.class);
            userExtendValDto.setExtendCode(extend.getCode());
            userExtendValDto.setExtendDescription(extend.getDescription());
            userExtendValDtos.add(userExtendValDto);
        }

        return userExtendValDtos;
    }
}

