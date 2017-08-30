package com.dianrong.common.uniauth.server.synchronous.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.synchronous.exp.InvalidContentException;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.AnaListResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 分析文件的内容.
 *
 * @param <T> 分析的内容类型.
 */
@Slf4j public abstract class AbstractFileContentAnalyzer<T extends AnaListResult>
    implements FileContentAnalyzer<T> {

  private static final String SIMPLE_DATE_FORMAT = "yyyy/MM/dd";

  /**
   * 列分割符.
   */
  protected String rowDelimiter = ",";

  @Override public T analyze(InputStream inputStream) throws InvalidContentException {
    Assert.notNull(inputStream, "inputStream can not be null!");
    BufferedReader in = null;
    InputStreamReader reader = null;
    StringBuilder sb = new StringBuilder();
    try {
      String line = "";
      reader = new InputStreamReader(inputStream);
      in = new BufferedReader(reader);
      while ((line = in.readLine()) != null) {
        sb.append(line);
      }
    } catch (IOException e) {
      throw new InvalidContentException("Failed read content from InputStream", e);
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        log.warn("Failed close Stream", e);
      }
    }
    return this.analyze(sb.toString());
  }

  /**
   * 将内容解析成list.
   *
   * @param content 内容字符串.
   * @return 解析结果.
   */
  protected List<String> anaToList(String content) {
    if (!StringUtils.hasText(content)) {
      return Collections.emptyList();
    }
    String[] strs = content.split("\r\n");
    List<String> strList = Lists.newArrayList();
    for (String str : strs) {
      strList.add(str.trim());
    }
    return strList;
  }

  /**
   * Check实际列数是否与预期一致.
   *
   * @param items  实际内容.
   * @param length 预期的列数.
   * @throws InvalidContentException 如果内容与预期不一致.
   */
  protected void itemLengthCheck(String[] items, int length) throws InvalidContentException {
    if (items == null || items.length != length) {
      throw new InvalidContentException(
          this.getClass().getSimpleName() + ".The content is invalid, Each row need " + length
              + " columns, but the real content is " + (items == null ? 0 : items.length));
    }
  }

  public void setRowDelimiter(String rowDelimiter) {
    Assert.notNull(rowDelimiter);
    this.rowDelimiter = rowDelimiter;
  }

  /**
   * 将字符串转化为Long.
   *
   * @param item 字符串
   * @return Long
   * @throws InvalidContentException 如果不能正常转为为Long,则抛出该异常.
   */
  protected Long strToLong(String item) throws InvalidContentException {
    if (!StringUtils.hasText(item)) {
      return null;
    }
    try {
      return Long.parseLong(item);
    } catch (NumberFormatException nfe) {
      log.error("Analysis ftp file, failed to translate string to long", nfe);
      throw new InvalidContentException(
          this.getClass().getSimpleName() + "." + item + " is not a valid Long string");
    }
  }

  /**
   * 将字符串转化为Date.
   *
   * @param item 字符串
   * @return Date
   * @throws InvalidContentException 如果不能正常转为为Date,则抛出该异常.
   */
  protected Date strToDate(String item) {
    if (!StringUtils.hasText(item)) {
      return null;
    }
    Date date = DateUtils.parseDate(item, new String[] {SIMPLE_DATE_FORMAT});
    if (date == null) {
      log.error(item + " is not a valid Date string.");
      throw new InvalidContentException(
          this.getClass().getSimpleName() + "." + item + " is not a valid Date string");
    }
    return date;
  }

  /**
   * 将字符串的前后的"去掉.
   */
  protected String clearItem(String item) {
    if (!StringUtils.hasText(item)) {
      return item;
    }
    String processItem = item.trim();
    if (processItem.startsWith("\"")) {
      processItem = processItem.substring(1);
    }
    if (processItem.endsWith("\"")) {
      processItem = processItem.substring(0, processItem.length() - 1);
    }
    return processItem;
  }
}
