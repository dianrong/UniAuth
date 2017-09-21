package com.dianrong.common.uniauth.server.service.attribute.transalate;

import com.dianrong.common.uniauth.server.service.attribute.exp.InvalidPropertyValueException;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

@Slf4j
public class DateTranslator extends AbstractAttributeTypeTranslator {

  /**
   * 由于DateFormatter有线程安全,所以采用ThreadLocal来封装一下.
   */
  private final ThreadLocal<DateFormatter> dateFormatter = new ThreadLocal<DateFormatter>() {
    protected DateFormatter initialValue() {
      return new DateFormatter();
    }
  };

  @Override public Object doToRealType(String attribute) {
    return dateFormatter.get().toDate(attribute);
  }

  @Override
  public String doToString(Object obj) {
    if (obj instanceof Date) {
      return dateFormatter.get().toString((Date)obj);
    }
    return null;
  }

  private class DateFormatter {
    private static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_2 = "yyyy/MM/dd HH:mm:ss";
    private static final String DATE_FORMAT_3 = "yyyy-MM-dd";
    private static final String DATE_FORMAT_4 = "yyyy/MM/dd";
    private final Pattern dateTimePattern = Pattern.compile("^\\d+$");

    private Map<String, DateFormat> dateFormatMap;

    public DateFormatter() {
      this.dateFormatMap = new LinkedHashMap<>();
      dateFormatMap.put(DATE_FORMAT_1, null);
      dateFormatMap.put(DATE_FORMAT_2, null);
      dateFormatMap.put(DATE_FORMAT_3, null);
      dateFormatMap.put(DATE_FORMAT_4, null);
    }

    public Date toDate(String str) {
      if (dateTimePattern.matcher(str).matches()) {
        return new Date(Long.parseLong(str));
      }
      Date result = null;
      for (Entry<String, DateFormat> entry : this.dateFormatMap.entrySet()) {
        DateFormat dateFormat = entry.getValue();
        if (dateFormat == null) {
          dateFormat = new SimpleDateFormat(entry.getKey());
          this.dateFormatMap.put(entry.getKey(), dateFormat);
        }
        try {
          result = dateFormat.parse(str);
        } catch (ParseException e) {
          log.debug("{} failed translate to java.util.Date, the format is {}", str, entry.getKey());
        }
        if (result != null) {
          return result;
        }
      }
      throw new InvalidPropertyValueException(str + " is a invalid date string", "Date", str);
    }

    public String toString(Date date) {
      DateFormat dateFormat = this.dateFormatMap.get(DATE_FORMAT_1);
      if (dateFormat == null) {
        dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        this.dateFormatMap.put(DATE_FORMAT_1, dateFormat);
      }
      return dateFormat.format(date);
    }
  }
}
