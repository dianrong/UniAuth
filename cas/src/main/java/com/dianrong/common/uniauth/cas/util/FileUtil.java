package com.dianrong.common.uniauth.cas.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 文件处理相关util.主要用于cas的子系统.
 *
 * @author wanglin
 */
@Slf4j
public final class FileUtil {

  /**
   * 返回webApp文件夹所在路径.
   */
  public static String getWebAppLocation() {
    String classesLocation = FileUtil.class.getClassLoader().getResource("").getPath();
    File tclasses = new File(classesLocation);

    // ../../classes
    return tclasses.getParentFile().getParent();
  }

  /**
   *读取文件内容.
   * @param relativePathForWebApp 相对于webapp的相对路径
   * @return 读取到的文件流数据
   * @throws IOException 文件读取io异常
   */
  public static byte[] readFiles(String relativePathForWebApp) throws IOException {
    if (relativePathForWebApp == null) {
      relativePathForWebApp = "";
    }
    if (relativePathForWebApp.startsWith(File.separator)) {
      relativePathForWebApp = relativePathForWebApp.substring(1);
    }
    String realFilePath = getWebAppLocation() + File.separator + relativePathForWebApp;
    File file = new File(realFilePath);
    if (file.exists() && file.isFile()) {
      byte[] bytes = new byte[(int) file.length()];
      try (FileInputStream fileInputStream = new FileInputStream(file);
          BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
        int r = bufferedInputStream.read(bytes);
        if (r != file.length()) {
          throw new IOException("Read file " + realFilePath + " error!");
        }
      } catch (IOException ex) {
        throw ex;
      }
      return bytes;
    }
    return null;
  }


  /**
   * 等同于loadProperties(filePath , null).
   * @param filePath filePath
   */
  public static Map<String, String> loadProperties(String filePath) {
    return loadProperties(filePath, null);
  }

  /**
   * Load map from properties file. if defaultMap is null, new a LinkedHashMap
   * @param filePath filePath
   */
  public static Map<String, String> loadProperties(String filePath,
      Map<String, String> defaultMap) {
    Assert.notNull(filePath, "properties filePath");
    // 读取属性文件a.properties
    Properties prop = new Properties();
    try (InputStream in = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream(filePath);) {
      prop.load(in);
      Map<String, String> values = defaultMap;
      if (values == null) {
        values = new HashMap<String, String>();
      }
      Iterator<String> it = prop.stringPropertyNames().iterator();
      while (it.hasNext()) {
        String key = it.next();
        values.put(key, prop.getProperty(key));
      }
      return values;
    } catch (IOException e) {
      log.warn("failed to read properties from " + filePath, e);
      throw new RuntimeException(e);
    }
  }
}
