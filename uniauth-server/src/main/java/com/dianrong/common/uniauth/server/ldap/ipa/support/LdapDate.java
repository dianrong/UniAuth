package com.dianrong.common.uniauth.server.ldap.ipa.support;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于接收ldap的时间类型
 */
@Slf4j
@ToString
public class LdapDate {

    /**
     * 需要特殊处理的时间格式
     */
    public static final String UTC_DATE_STRING= "yyyyMMddHHmmss'Z'";
    public static final String UTC_DATE_STRING_1= "yyyy-MM-dd HH:mm:ss'Z'";
    
    public static final String NORMAL_DATE_STRING_1= "yyyy-MM-dd HH:mm:ss'";
    public static final String NORMAL_DATE_STRING_2= "yyyyMMddHHmmss'";
    public static final String NORMAL_DATE_STRING_3= "yyyyMMdd HHmmss'";
    
    public static final String[] DATE_FORMATS = new String[] {UTC_DATE_STRING, UTC_DATE_STRING_1, NORMAL_DATE_STRING_1, NORMAL_DATE_STRING_2, NORMAL_DATE_STRING_3};
    
    private Date date;
    
    public Date getDate() {
        return date;
    }

    @SuppressWarnings("deprecation")
    public LdapDate(String source) {
        try {
            this.date = DateUtils.parseDate(source, DATE_FORMATS);
        } catch (ParseException e) {
            log.warn("{0} is not a format date time string", source, e);
        }
        
        try {
            this.date = new Date(source);
        } catch(Exception e) {
            log.warn("{0} is not a format date time string", e);
        }
        if (this.date == null) {
            throw new UniauthCommonException(String.format("{} is not a format date String", source));
        }
    }
}
