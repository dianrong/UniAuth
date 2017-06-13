package com.dianrong.common.uniauth.common.interfaces.readwrite;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.bean.request.AttributeExtendParam;
import com.dianrong.common.uniauth.common.interfaces.read.IAttributeExtendResource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 扩展属性写接口.
 */
public interface IAttributeExtendRWResource extends IAttributeExtendResource {

  @POST
  @Path("add-AttributeExtend")
  Response<AttributeExtendDto> addAttributeExtend(AttributeExtendParam attributeExtendParam);

  @POST
  @Path("update-AttributeExtend")
  Response<Integer> updateAttributeExtend(AttributeExtendParam attributeExtendParam);
}

