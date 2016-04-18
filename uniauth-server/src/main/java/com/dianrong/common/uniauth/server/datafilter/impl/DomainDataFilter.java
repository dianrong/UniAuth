package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

/**.
 * 域名的数据过滤处理实现.
 * @author wanglin
 */
@Service("domainDataFilter")
public class DomainDataFilter extends CurrentAbstractDataFilter {
	
	@Autowired
	private DomainMapper domainMapper;
	
	/**.
	 * 标示处理的表名
	 */
	private String processTalbeName = "域";
	
	/**.
	 * 处理过滤status=0的情况
	 * @param filterMap 过滤条件字段
	 */
	@Override
	public void filterStatusEqual0(Map<FieldType, Object> filterMap){
		Set<Entry<FieldType, Object>> entrySet = filterMap.entrySet();
		//遍历
		for(Entry<FieldType, Object> kv : entrySet){
			switch(kv.getKey()){
			case FIELD_TYPE_ID:
				int countById = domainMapper.countDomainByIdWithStatusEffective(TypeParseUtil.parseToLongFromObject(kv.getValue()));
				//有数据  就要报错
				if(countById > 0){
					throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.exsit.error", processTalbeName , "id" , TypeParseUtil.parseToLongFromObject(kv.getValue())));
				}
				break;
			case FIELD_TYPE_CODE:
				int countByCode = domainMapper.countDomainByCodeWithStatusEffective(TypeParseUtil.parseToStringFromObject(kv.getValue()));
				if(countByCode > 0){
					throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.exsit.error", processTalbeName , "code" , TypeParseUtil.parseToStringFromObject(kv.getValue())));
				}
				break;
				default:
					break;
			}
		}
	}
	
	/**.
	 * 处理过滤不能出现status=0的情况
	 * @param filterMap 入参数据
	 */
	@Override
	public void filterNoStatusEqual0(Map<FieldType, Object> filterMap){
		Set<Entry<FieldType, Object>> entrySet = filterMap.entrySet();
		//遍历
		for(Entry<FieldType, Object> kv : entrySet){
			switch(kv.getKey()){
			case FIELD_TYPE_ID:
				int countById = domainMapper.countDomainByIdWithStatusEffective(TypeParseUtil.parseToLongFromObject(kv.getValue()));
				//有数据  就要报错
				if(countById <= 0){
					throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.notexsit.error", processTalbeName , "id" , TypeParseUtil.parseToLongFromObject(kv.getValue())));
				}
				break;
			case FIELD_TYPE_CODE:
				int countByCode = domainMapper.countDomainByCodeWithStatusEffective(TypeParseUtil.parseToStringFromObject(kv.getValue()));
				if(countByCode <= 0){
					throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.notexsit.error", processTalbeName , "code" , TypeParseUtil.parseToStringFromObject(kv.getValue())));
				}
				break;
			default:
				break;
			}
		}
	}
	
	/**.
	 * 判断数据是否已经重复了
	 */
	@Override
	public void doFilterFieldValueIsExist(FieldType type, Integer id, Object fieldValue){
		switch(type){
			case FIELD_TYPE_CODE:
				String newCode = TypeParseUtil.parseToStringFromObject(fieldValue);
				Domain domainInfo = domainMapper.selectByIdWithStatusEffective(id);
				if(domainInfo != null){
					//如果数据信息没有改变  则不管
					if(newCode.equals(domainInfo.getCode())){
						break;
					}
				}
				//查看是否存在其他的记录是该信息
				Map<FieldType, Object> tmap = new HashMap<FieldType, Object>();
				tmap.put(FieldType.FIELD_TYPE_CODE, newCode);
				//进行判断
				this.filterStatusEqual0(tmap);
				break;
			default:
				break;
			}
	}
}
