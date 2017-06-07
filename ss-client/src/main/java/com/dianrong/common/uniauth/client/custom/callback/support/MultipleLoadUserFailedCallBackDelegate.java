package com.dianrong.common.uniauth.client.custom.callback.support;

import com.dianrong.common.uniauth.client.custom.callback.LoadUserFailedCallBack;
import java.util.List;

public class MultipleLoadUserFailedCallBackDelegate implements LoadUserFailedCallBack {

  private List<LoadUserFailedCallBack> loadUserFailedCallBacks;

  public MultipleLoadUserFailedCallBackDelegate(
      List<LoadUserFailedCallBack> loadUserFailedCallBacks) {
    this.loadUserFailedCallBacks = loadUserFailedCallBacks;
  }

  @Override
  public void loadUserFailed(String identity, Long tenancyId, Throwable cause) {
    for (LoadUserFailedCallBack loadUserFailedCallBack : loadUserFailedCallBacks) {
      loadUserFailedCallBack.loadUserFailed(identity, tenancyId, cause);
    }
  }
}