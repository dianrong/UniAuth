package com.dianrong.common.uniauth.common.client;

import com.dianrong.common.uniauth.common.util.Assert;

/**
 * 存放uniauth-server Api访问的权限认证的账号信息
 * 
 * @author wanglin
 */
public class SimpleApiCtrlAccountHolder implements ApiCtrlAccountHolder {

    private String account;

    private String password;

    @Override
    public String getAccount() {
        return this.account;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setAccount(String account) {
        Assert.notNull(account);
        this.account = account;
    }

    public void setPassword(String password) {
        Assert.notNull(password);
        this.password = password;
    }
}
