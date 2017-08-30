package com.dianrong.common.uniauth.server.synchronous.support;

import com.dianrong.common.uniauth.server.synchronous.exp.InvalidContentException;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.AnaListResult;

import java.io.InputStream;

/**
 * 分析文件的内容.
 * @param <T> 分析的内容类型.
 */
public interface FileContentAnalyzer<T extends AnaListResult> {

  /**
   * 分析传入的内容.
   * @param content 传入的内容字符串.
   * @return 分析结果,不会为空.
   * @throws InvalidContentException 如果传入的内容有问题,则抛出改异常.
   */
  T analyze(String content) throws InvalidContentException;

  /**
   * 分析传入的内容.
   * @param inputStream 内容的输入流.
   * @return 分析结果,不会为空.
   * @throws InvalidContentException 如果传入的内容有问题,则抛出改异常.
   */
  T analyze(InputStream inputStream) throws InvalidContentException;
}
