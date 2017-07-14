package com.dianrong.common.techops.service.analysis;

import com.dianrong.common.techops.exp.BatchProcessException;
import com.dianrong.common.techops.util.UniBundle;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 分析输入流的辅助类.
 * 
 * @author wanglin
 *
 */
public final class InputAnalyzer {

  // 身份识别的内容有多少列.
  public static final int USER_IDENTITY_LINE_NUM = 2;

  /**
   * 相当于analysisInputForIdentity(inputStream, 2).
   */
  public static List<IdentityAnalysisResult> analysisInputForIdentity(InputStream inputStream)
      throws IOException {
    return analysisInputForIdentity(inputStream, USER_IDENTITY_LINE_NUM);
  }

  /**
   * 解析输入流,将其解析为带用户信息识别的对象.
   * 
   * @param inputStream 输入流
   * @param lineNum 每一行有多少列需要解析,不能为负数.
   */
  public static List<IdentityAnalysisResult> analysisInputForIdentity(InputStream inputStream,
      int lineNum) throws IOException {
    List<String> content = readContentFromInputStream(inputStream);
    // 解析文件内容
    List<IdentityAnalysisResult> results = Lists.newArrayList();
    for (int i = 0; i < content.size(); i++) {
      String paramStr = content.get(i);
      String[] params = split(paramStr, ",");
      String email = getItem(params, 0);
      String phone = getItem(params, 1);
      if (StringUtils.isBlank(email) && StringUtils.isBlank(phone)) {
        throw new BatchProcessException(
            UniBundle.getMsg("service.batch.process.file.analysis.fail", i + 1, paramStr));
      }
      IdentityAnalysisResult result = new IdentityAnalysisResult();
      UserParam userParam = new UserParam();
      userParam.setEmail(email);
      userParam.setPhone(phone);
      result.setUserParam(userParam);
      if (lineNum > USER_IDENTITY_LINE_NUM) {
        List<String> appendInfo = Lists.newArrayList();
        for (int j = USER_IDENTITY_LINE_NUM; j < lineNum; j++) {
          appendInfo.add(getItem(params, j));
        }
        result.setAppendInfo(appendInfo);
      }
      results.add(result);
    }
    return results;
  }

  /**
   * 解析输入流,解析成普通的解析结果对象.
   * 
   * @param inputStream 输入流
   * @param lineNum 每一行有多少列需要解析,不能为负数.
   */
  public static List<NormalAnalysisResult> analysisInput(InputStream inputStream, int lineNum)
      throws IOException {
    List<String> contents = readContentFromInputStream(inputStream);
    // 解析文件内容
    List<NormalAnalysisResult> results = Lists.newArrayList();
    int lineNumber = 1;
    for (String content : contents) {
      String[] params = split(content, ",");
      NormalAnalysisResult result = new NormalAnalysisResult();
      for (int i = 0; i < lineNum; i++) {
        result.getInfo().add(getItem(params, i));
      }
      result.setLineNumber(lineNumber++);
      results.add(result);
    }
    return results;
  }

  /**
   * 解析字符串.处理最后一个来分隔符的情况.
   */
  private static String[] split(String str, String delimiter) {
    if (StringUtils.isBlank(str)) {
      return new String[] {};
    }
    String[] items = str.split(delimiter);
    if (str.endsWith(delimiter)) {
      String[] newItmes = new String[items.length + 1];
      System.arraycopy(items, 0, newItmes, 0, items.length);
      newItmes[newItmes.length - 1] = "";
      items = newItmes;
    }
    // 对所有的item进行trim处理
    for (int i = 0; i < items.length; i++) {
      items[i] = items[i].trim();
    }
    return items;
  }

  /**
   * 从数组中获取对应index的数据,不报数组越界错误.
   */
  private static <T> T getItem(T[] items, int index) {
    int length = items.length;
    if (index >= length) {
      return null;
    }
    return items[index];
  }

  /**
   * 从inputStream读取上传文件的内容.
   */
  private static List<String> readContentFromInputStream(InputStream inputStream)
      throws IOException {
    List<String> content = Lists.newArrayList();
    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
    String line = null;
    while ((line = in.readLine()) != null) {
      content.add(line);
    }
    return content;
  }
}
