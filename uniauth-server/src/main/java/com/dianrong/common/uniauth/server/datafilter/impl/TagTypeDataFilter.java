package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.TagTypeExample;
import com.dianrong.common.uniauth.server.data.mapper.TagTypeMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Arc on 15/4/2016.
 */

@Service("tagTypeDataFilter")
public class TagTypeDataFilter extends CurrentAbstractDataFilter {
    @Autowired
    TagTypeMapper tagTypeMapper;
    /**.
     * 标示处理的表名
     */
    private String processTableName = "标签类型";
    @Override
    protected void filterStatusEqual0(Map<FieldType, Object> filterMap) {

    }

    @Override
    protected void filterNoStatusEqual0(Map<FieldType, Object> filterMap) {

    }

    @Override
    public void doDataFilterWithConditionsEqual(FilterType ftype, FilterData... equalsField) {
        switch(ftype){
            case FILTER_TYPE_EXSIT_DATA:
                if(dataWithConditionsEqualExist(equalsField)){
                    throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.mutilcondition.exsit.error", processTableName, getFieldTypeKeyAndValue(equalsField)));
                }
                break;
            case FILTER_TYPE_NO_DATA:
                if(!dataWithConditionsEqualExist(equalsField)){
                    throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.mutilcondition.notexsit.error", processTableName, getFieldTypeKeyAndValue(equalsField)));
                }
                break;
            default:
                break;
        }
    }

    /**.
     * 判断某几个字段是否同时存在.
     */
    private boolean dataWithConditionsEqualExist(FilterData... equalsField){
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

    /**.
     * 获取描述符
     * @param equalsField equalsField处理的字段
     * @return 描述符string
     */
    private String getFieldTypeKeyAndValue(FilterData... equalsField){
        if(equalsField == null || equalsField.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String filterKeyVal = "=";
        String filterEle = ";";
        for(FilterData fd: equalsField){
            if(sb.toString().length() > 0) {
                sb.append(filterEle);
            }
            sb.append(FieldType.getTypeDes(fd.getType()));
            sb.append(filterKeyVal);
            sb.append(StringUtil.getObjectStr(fd.getValue()));
        }
        return sb.toString();
    }
}
