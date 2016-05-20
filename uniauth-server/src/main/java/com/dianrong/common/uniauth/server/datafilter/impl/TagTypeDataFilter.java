package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.server.data.entity.TagType;
import com.dianrong.common.uniauth.server.data.entity.TagTypeExample;
import com.dianrong.common.uniauth.server.data.mapper.TagTypeMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;

/**
 * Created by Arc on 15/4/2016.
 */

@Service("tagTypeDataFilter")
public class TagTypeDataFilter extends CurrentAbstractDataFilter {
	/**.
	 * tagType处理的mapper
	 */
	@Autowired
    private TagTypeMapper tagTypeMapper;

    /**.
     * 判断某几个字段是否同时存在.
     */
    @Override
    protected boolean dataWithConditionsEqualExist(FilterData... equalsField){
        //判空处理
        if(equalsField == null || equalsField.length == 0) {
            return false;
        }
        //首先根据类型和值获取到对应的model数组
        TagTypeExample condition = new TagTypeExample();
        TagTypeExample.Criteria criteria =  condition.createCriteria();
        //构造查询条件
        for(FilterData fd: equalsField){
            switch(fd.getType()) {
                case FIELD_TYPE_CODE:
                    criteria.andCodeEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
                    break;
                case FIELD_TYPE_DOMAIN_ID:
                    criteria.andDomainIdEqualTo(Integer.parseInt(TypeParseUtil.parseToLongFromObject(fd.getValue()).toString()));
                    break;
                default:
                    break;
            }
        }
        //查询
        int count = tagTypeMapper.countByExample(condition);
        if(count > 0){
            return true;
        }
        return false;
    }

	@Override
	protected String getProcessTableName() {
		return  "标签类型";
	}
	
	@Override
	protected void doFilterFieldValueIsExistWithConditionsEqual(Integer tagId, FilterData... equalsField){
		CheckEmpty.checkEmpty(tagId, "tagId");
		//不处理
		if(equalsField ==  null || equalsField.length == 0) {
			return;
		}
		
		TagTypeExample condition = new TagTypeExample();
		condition.createCriteria().andIdEqualTo(tagId);
		List<TagType> selectByExample = tagTypeMapper.selectByExample(condition);
		
		if(selectByExample != null && !selectByExample.isEmpty()){
			TagType currentTag = selectByExample.get(0);
			// 默认是全部相等的
			boolean isEqual = true;
			// 如果数据信息没有改变  则不管
			for(FilterData fd: equalsField){
				Object  v1 =  getObjectValue(currentTag, fd.getType());
				Object v2 = fd.getValue();
				if(v1 == null && v2== null){
					continue;
				}
				// 其中某个字段为空了 都不能认为是相等的
				if(v1== null || v2 == null){
					isEqual = false;
					break;
				}
				if(!v1.equals(v2)){
					isEqual = false;
					break;
				}
			}
			if(isEqual){
				return;
			}
		}
		//查看是否存在其他的记录
		doDataFilterWithConditionsEqual(FilterType.FILTER_TYPE_EXSIT_DATA,  equalsField) ;
	}
	
	/**.
	 * 从Tag中获取数据
	 * @param obj Tag
	 * @param type 结果
	 * @return 结果
	 */
	private Object getObjectValue(TagType obj, FieldType type){
		if(obj == null){
			return null;
		}
		//根据自身情况来实现
		switch(type){
		  case FIELD_TYPE_CODE:
			  return obj.getCode();
          case FIELD_TYPE_DOMAIN_ID:
        	  return obj.getDomainId();
			default:
				break;
		}
		return null;
	}
}
