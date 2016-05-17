package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.server.data.entity.UserExtend;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample.Criteria;
import com.dianrong.common.uniauth.server.data.entity.ext.UserExtendValExt;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendValMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
@Service
public class UserExtendValService {
    
    @Autowired
    private UserExtendValMapper userExtendValMapper;
    
    @Autowired 
    private UserExtendMapper userExtendMapper;
    
    /**
     * 添加一个用户扩展属性值
     * @param userId
     * @param extendId
     * @param value
     * @param status
     * @return
     */
    public UserExtendValDto add(Long userId,Long extendId,String value,Byte status){
        UserExtendVal userExtendVal=new UserExtendVal();
        userExtendVal.setExtendId(extendId);
        userExtendVal.setStatus(status);
        userExtendVal.setUserId(userId);
        userExtendVal.setValue(value);
        
        userExtendValMapper.insertSelective(userExtendVal);
        
        return BeanConverter.convert(userExtendVal,UserExtendValDto.class);
    }
    
    /**
     * 根据扩展属性值的主键id删除数据
     * @return 删除的个数
     */
    public int delById(Long id){
        CheckEmpty.checkEmpty(id,"id");
        
        UserExtendVal userExtendVal=new UserExtendVal();
        userExtendVal.setId(id);
        userExtendVal.setStatus((byte)1);
        
        return userExtendValMapper.updateByPrimaryKeySelective(userExtendVal);
    }
    
    /**
     * 根据扩展属性值的主键id修改数据
     * @param id
     * @param userId
     * @param extendId
     * @param value
     * @param status
     * @return
     */
    public int updateById(Long id,Long userId,Long extendId,String value,Byte status){
        CheckEmpty.checkEmpty(id,"id");
        
        UserExtendVal userExtendVal=new UserExtendVal();
        userExtendVal.setId(id);
        userExtendVal.setExtendId(extendId);
        userExtendVal.setStatus(status);
        userExtendVal.setUserId(userId);
        userExtendVal.setValue(value);
        
        return userExtendValMapper.updateByPrimaryKeySelective(userExtendVal);
    }
    
    /**
     * 根据用户id查询扩展属性值
     * @param userId
     * @param status 启用禁用状态
     * @return
     */
    public List<UserExtendValDto> searchByUserId(Long userId,Byte status){
        CheckEmpty.checkEmpty(userId,"userId");
        
        UserExtendValExample example=new UserExtendValExample();
        Criteria criteria=example.createCriteria().andUserIdEqualTo(userId);
        if(status!=null){
            criteria.andStatusEqualTo(status);
        }
        
        List<UserExtendVal> userExtendVals=userExtendValMapper.selectByExample(example);
        
        List<UserExtendValDto> userExtendValDtos=new ArrayList<UserExtendValDto>();
        UserExtendValDto userExtendValDto;
        UserExtend userExtend;
        for(UserExtendVal userExtendVal:userExtendVals){
            userExtendValDto=BeanConverter.convert(userExtendVal,UserExtendValDto.class);
            userExtend=userExtendMapper.selectByPrimaryKey(userExtendValDto.getExtendId());
            userExtendValDto.setExtendCode(userExtend.getCode());
            userExtendValDto.setExtendDescription(userExtend.getDescription());
            userExtendValDtos.add(userExtendValDto);
        }
        
        return userExtendValDtos;
    }

    /**
     * 根据用户id和code分页查询数据
     * @param userId 必填
     * @param code 可选
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageDto<UserExtendValDto> searchByUserIdAndCode(Long userId,String code,
            Integer pageNumber,Integer pageSize){
        CheckEmpty.checkEmpty(userId,"userId");
        CheckEmpty.checkEmpty(pageNumber, "pageNumber");
        CheckEmpty.checkEmpty(pageSize, "pageSize");
        
        Map<String,String> params=new HashMap<String,String>();
        params.put("userId",userId.toString());
        params.put("extendCode", code==null?null:'%'+code+'%');
        
        int count=userExtendValMapper.countByCode(params);
        ParamCheck.checkPageParams(pageNumber, pageSize, count);
        params.put("pageNumber",pageNumber.toString());
        params.put("pageSize",pageSize.toString());
        List<UserExtendValExt> userExtendValExts=userExtendValMapper.selectByUserIdAndCode(params);
        
        //转换
        List<UserExtendValDto> userExtendDtos=new ArrayList<UserExtendValDto>();
        
        for(UserExtendValExt userExtendValExt:userExtendValExts){
            userExtendDtos.add(BeanConverter.convert(userExtendValExt,UserExtendValDto.class));
        }
        //生成分页对象
        PageDto<UserExtendValDto> pageDto=new PageDto<UserExtendValDto>(pageNumber,pageSize, count, userExtendDtos);
        
        return pageDto;
    }
}

