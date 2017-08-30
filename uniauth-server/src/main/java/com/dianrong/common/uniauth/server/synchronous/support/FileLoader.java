package com.dianrong.common.uniauth.server.synchronous.support;

import com.dianrong.common.uniauth.server.synchronous.exp.FileLoadFailureException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 加载文件内容的加载器.
 */
public interface FileLoader {

  /**
   * 通过文件来导入文件内容.
   * @param file 文件内容.
   * @return 文输入流.
   * @exception FileLoadFailureException 加载文件异常.
   */
  InputStream loadFile(String file) throws FileLoadFailureException;

  /**
   * 通过文件来导入文件内容.
   * @param file 文件内容.
   * @return 文件内容.
   * @exception FileLoadFailureException 加载文件异常.
   */
  String loadFileContent(String file) throws FileLoadFailureException;
}
