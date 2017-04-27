package com.dianrong.common.uniauth.client.custom.callback.support;

import java.util.List;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.client.custom.callback.LoadUserSuccessCallBack;
import com.dianrong.common.uniauth.common.util.Assert;

public class MultipleLoadUserSuccessCallBackDelegate implements LoadUserSuccessCallBack {
    
    private List<LoadUserSuccessCallBack>  loadUserSuccessCallBacks;
    
    public MultipleLoadUserSuccessCallBackDelegate(List<LoadUserSuccessCallBack>  loadUserSuccessCallBacks) {
        Assert.notNull(loadUserSuccessCallBacks);
        this.loadUserSuccessCallBacks =loadUserSuccessCallBacks;
    }
    
    @Override
    public void loadUserSuccess(UserExtInfo userDetail) {
        for (LoadUserSuccessCallBack loadUserSuccessCallBack : loadUserSuccessCallBacks) {
            loadUserSuccessCallBack.loadUserSuccess(userDetail);
        }
    }
}
