package com.dianrong.common.uniauth.server.ldap.ipa.support;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import java.text.ParseException;
import java.util.Date;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.StringUtils;


/**
 * 用于接收ldap的时间类型.
 */
@Slf4j
@ToString
public class LdapDate {

  /**
   * 需要特殊处理的时间格式.
   */
  public static final String UTC_DATE_STRING = "yyyyMMddHHmmss'Z'";
  public static final String UTC_DATE_STRING_1 = "yyyy-MM-dd HH:mm:ss'Z'";

  public static final String NORMAL_DATE_STRING_1 = "yyyy-MM-dd HH:mm:ss'";
  public static final String NORMAL_DATE_STRING_2 = "yyyyMMddHHmmss'";
  public static final String NORMAL_DATE_STRING_3 = "yyyyMMdd HHmmss'";

  public static final String[] DATE_FORMATS = new String[]{UTC_DATE_STRING, UTC_DATE_STRING_1,
      NORMAL_DATE_STRING_1, NORMAL_DATE_STRING_2, NORMAL_DATE_STRING_3};

  private Date date;

  public Date getDate() {
    return date;
  }

  /**
   * 封装Date对象,支持时间格式: yyyyMMddHHmmss'Z'.
   */
  @SuppressWarnings("deprecation")
  public LdapDate(String source) {
    if (!StringUtils.hasText(source)) {
      log.warn("empty string can not cast to Date");
      return;
    }
    try {
      this.date = DateUtils.parseDate(source, DATE_FORMATS);
      return;
    } catch (ParseException e) {
      log.warn("'{}' is not a format date time string", source, e);
    }

    try {
      this.date = new Date(source);
    } catch (Exception e) {
      log.warn("can not cast string '{}' to a Date", source, e);
    }
    if (this.date == null) {
      throw new UniauthCommonException(String.format("%s is not a format date String", source));
    }
  }
}
