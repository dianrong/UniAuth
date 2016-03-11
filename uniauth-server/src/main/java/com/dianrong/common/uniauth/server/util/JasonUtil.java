package com.dianrong.common.uniauth.server.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JasonUtil {
	
	private static ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
	
	private JasonUtil() {
		
	}

	public static String object2Jason(Object obj){
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
}
