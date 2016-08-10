package com.dianrong.common.uniauth.common.switches;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListReceiver implements SwitchReceiver {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void receive(Field f, String config) throws Exception {
		if(f.getType() == List.class && f.getGenericType() instanceof ParameterizedType){
			Type geneicType = ((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
			if(geneicType instanceof Class){
				List list = new ArrayList();
				String[] configs = config.split(",");
				for(String conf : configs){
					Object value = TypeConverter.convert((Class<?>)geneicType, conf);
					list.add(value);
				}
				List ovalue = (List)f.get(null);
				if(ovalue==null){
					f.set(null, list);
				}else{
					ovalue.clear();
					ovalue.addAll(list);
				}
			}
		}
	}

}
