package com.dianrong.common.uniauth.server.synchronous.support;

import com.dianrong.common.uniauth.server.synchronous.exp.FileLoadFailureException;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.LoadContent;

import java.io.IOException;
import java.io.InputStream;

/**
 * 加载文件内容的加载器.
 */
public interface FileLoader {

  /**
   * 通过文件来导入文件内容.
   * @param file 文件内容.
   * @return 文输入流.外部需要自行处理输入流.
   * @exception FileLoadFailureException 加载文件异常.
   */
  LoadContent<InputStream> loadFile(String file) throws FileLoadFailureException;

  /**
   * 通过文件来导入文件内容.
   * @param file 文件内容.
   * @return 文件内容.
   * @exception FileLoadFailureException 加载文件异常.
   */
  LoadContent<String> loadFileContent(String file) throws FileLoadFailureException;
}
