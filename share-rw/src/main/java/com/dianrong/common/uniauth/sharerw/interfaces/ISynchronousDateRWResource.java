package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.request.HrSynchronousLogParam;
import com.dianrong.common.uniauth.common.bean.request.TagParam;
import com.dianrong.common.uniauth.common.interfaces.read.ISynchronousDataResource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 同步操作的写接口.
 */
public interface ISynchronousDateRWResource extends ISynchronousDataResource {

  /**
   * 同步HR系统的数据.
   */
  @POST
  @Path("synchronous-hr-data") Response<Void> synchronousHrData(HrSynchronousLogParam param);

  /**
   * 删除过期文件.
   */
  @POST
  @Path("delete-expired-file") Response<Void> deleteExpiredFile(HrSynchronousLogParam param);
}
