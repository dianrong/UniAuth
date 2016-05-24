package com.dianrong.common.auth.server.util;

import org.springframework.beans.BeanUtils;

import com.dianrong.common.auth.server.data.entity.SlActorWithBLOBs;
import com.dianrong.common.auth.server.data.entity.SlIp;
import com.dianrong.common.auth.server.data.entity.SlThirdLogin;
import com.dianrong.common.uniauth.common.bean.dto.SlActorDto;
import com.dianrong.common.uniauth.common.bean.dto.SlActorEnum;
import com.dianrong.common.uniauth.common.bean.dto.SlIpDto;
import com.dianrong.common.uniauth.common.bean.dto.SlThirdLoginDto;

/**.
 * covert from model to dto
 * @author wanglin
 */
public final class BeanConverter {
	
	/**.
	 * covert from SlActorWithBLOBs to SlActorDto
	 * @param bean SlActorWithBLOBs
	 * @return SlActorDto
	 */
	public static SlActorDto convert(SlActorWithBLOBs bean){
		if(bean == null) {
			return null;
		}
		// copy properties 
		SlActorDto dto = new SlActorDto();
		BeanUtils.copyProperties(bean, dto);
		dto.setId(bean.getId() == null ? null : bean.getId().longValueExact())
				.setType(SlActorEnum.Type.fromShort(bean.getType()))
				.setSubtype(SlActorEnum.SubType.fromShort(bean.getSubtype()))
				.setaStatus(SlActorEnum.Status.fromShort(bean.getaStatus()))
				.setbStatus(SlActorEnum.Status.fromShort(bean.getbStatus()))
				.setlStatus(SlActorEnum.Status.fromShort(bean.getlStatus()))
				.settStatus(SlActorEnum.Status.fromShort(bean.gettStatus()))
				.setuStatus(SlActorEnum.Status.fromShort(bean.getuStatus()));
		return dto;
	}
	
	/**.
	 * covert from SlIp to SlIpDto
	 * @param bean SlIp
	 * @return SlIpDto
	 */
	public static SlIpDto convert(SlIp bean){
		if(bean == null) {
			return null;
		}
		// copy properties 
		SlIpDto dto = new SlIpDto();
		BeanUtils.copyProperties(bean, dto);
		return dto;
	}
	
	/**.
	 * covert from SlThirdLogin to SlThirdLoginDto
	 * @param bean SlThirdLogin
	 * @return SlThirdLoginDto
	 */
	public static SlThirdLoginDto convert(SlThirdLogin bean){
		if(bean == null) {
			return null;
		}
		// copy properties 
		SlThirdLoginDto dto = new SlThirdLoginDto();
		BeanUtils.copyProperties(bean, dto);
		return dto;
	}
}
