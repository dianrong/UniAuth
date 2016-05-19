package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.UserExtend;
import com.dianrong.common.uniauth.server.data.entity.UserExtendExample;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.datafilter.impl.UserExtendDataFilter;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
@Service
public class UserExtendService {
    
//    private Logger logger=Logger.getLogger(UserExtendService.class);
    
    @Autowired
    private UserExtendMapper userExtendMapper;
    
    @Resource(name="userExtendDataFilter")
    private UserExtendDataFilter dataFilter;
    
    /**
     * 新增扩展数据
     * @param code
     * @param description
     * @return
     */
    public UserExtendDto add(String code,String description){
    	CheckEmpty.checkEmpty(code, "eav_code");
    	
    	// 过滤数据
    	dataFilter.dataFilter(FieldType.FIELD_TYPE_CODE, code.trim(), FilterType.FILTER_TYPE_EXSIT_DATA);
    	
        UserExtend userExtend=new UserExtend();
        userExtend.setCode(code);
        userExtend.setDescription(description);
        userExtendMapper.insertSelective(userExtend);
        
        UserExtendDto userExtendDto = BeanConverter.convert(userExtend,UserExtendDto.class);
        
        return userExtendDto;
    }
    
    /**
     * 根据id获取数据
     * @param id
     * @return
     */
    public UserExtendDto getById(Long id){
        UserExtend userExtend=userExtendMapper.selectByPrimaryKey(id);
        UserExtendDto userExtendDto = BeanConverter.convert(userExtend,UserExtendDto.class);
        return userExtendDto;
    }
    
    /**
     * 根据id修改扩展数据
     * @param id
     * @param code
     * @param description
     * @return
     */
    public int updateByKey(Long id,String code,String description){
        CheckEmpty.checkEmpty(id,"id");
        if(!StringUtil.strIsNullOrEmpty(code)) {
        	// 过滤数据
        	dataFilter.filterFieldValueIsExist(FieldType.FIELD_TYPE_CODE, TypeParseUtil.parseToIntegerFromObject(id), code.trim());
        }
        
        UserExtend userExtend=new UserExtend();
        userExtend.setCode(code);
        userExtend.setDescription(description);
        userExtend.setId(id);
        
        return userExtendMapper.updateByPrimaryKeySelective(userExtend);
    }
    
    /**
     * 根据code模糊分页查询数据
     * @param code
     * @param pageNumber 从0开始
     * @param pageSize
     * @return
     */
    public PageDto<UserExtendDto> search(String code,Integer pageNumber,Integer pageSize){
        CheckEmpty.checkEmpty(pageNumber, "pageNumber");
        CheckEmpty.checkEmpty(pageSize, "pageSize");
        
        UserExtendExample example=new UserExtendExample();
        example.setPageOffSet(pageNumber*pageSize);
        example.setPageSize(pageSize);
        example.setOrderByClause("id desc");
        if(StringUtils.isNotBlank(code)){
            example.createCriteria().andCodeLike('%'+code+'%');
        }
        //查询
        int count=userExtendMapper.countByExample(example);
        ParamCheck.checkPageParams(pageNumber, pageSize, count);
        List<UserExtend> userExtends=userExtendMapper.selectByExample(example);
        //转换
        List<UserExtendDto> userExtendDtos=new ArrayList<UserExtendDto>();
        for(UserExtend userExtend:userExtends){
            userExtendDtos.add(BeanConverter.convert(userExtend,UserExtendDto.class));
        }
        //生成分页对象
        PageDto<UserExtendDto> pageDto=new PageDto<UserExtendDto>(pageNumber,pageSize, count, userExtendDtos);
        
        return pageDto;
    }
}

