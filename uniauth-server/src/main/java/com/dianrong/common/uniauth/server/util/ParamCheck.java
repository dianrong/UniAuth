package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.exp.AppException;

/**
 * Created by Arc on 27/1/16.
 */
public class ParamCheck {

  private ParamCheck() {
  }

  /**
   * 检查状态.
   */
  public static void checkStatus(Byte b) {
    if (b == null) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.empty", "status"));
    } else if (!AppConstants.ONE_BYTE.equals(b) && !AppConstants.ZERO_BYTE.equals(b)) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.status.invalid"));
    }
  }

  /**
   * @param pageNumber page's number.
   * @param pageSize the size of one page.
   * @param count the query result count.
   */
  public static void checkPageParams(Integer pageNumber, Integer pageSize, int count) {
    if (AppConstants.MIN_PAGE_NUMBER.equals(pageNumber)
        && AppConstants.MAX_PAGE_SIZE.equals(pageSize) && count > AppConstants.MAX_PAGE_SIZE) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("data.query.result.number.exceed", AppConstants.MAX_PAGE_SIZE, count));
    }
  }
}
