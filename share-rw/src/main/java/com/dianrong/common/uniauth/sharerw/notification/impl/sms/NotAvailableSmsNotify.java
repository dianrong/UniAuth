package com.dianrong.common.uniauth.sharerw.notification.impl.sms;

import com.dianrong.common.uniauth.sharerw.notification.SmsNotify;
import com.dianrong.common.uniauth.sharerw.notification.exp.NotificationNotAvailableException;

/**
 * Uniauth提供的一个默认短信发送实现
 * 
 * @author wanglin
 */
public class NotAvailableSmsNotify implements SmsNotify {

    @Override
    public void send(String phoneNumber, String notification) {
        throw new NotificationNotAvailableException();
    }

}
