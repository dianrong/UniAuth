package com.dianrong.common.uniauth.cas;

import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.springframework.util.StringUtils;

public class FileToolTest {

  /**
   * 测试读取文件.
   */
  public Map<String, String> readFiles(String path) throws IOException {
    File file = new File(path);
    if (file.exists() && file.isFile()) {
      Map<String, String> nameEmailMap = Maps.newHashMap();
      Reader reader = null;
      BufferedReader breader = null;
      try {
        reader = new FileReader(file);
        breader = new BufferedReader(reader);
        while (true) {
          String line = breader.readLine();
          if (!StringUtils.hasText(line)) {
            break;
          }
          String[] items = line.split("\t");
          if (items != null && items.length == 7) {
            nameEmailMap.put(items[1].trim(), items[6].trim());
          }
        }
      } catch (IOException ex) {
        throw ex;
      } finally {
        if (reader != null) {
          reader.close();
        }
        if (breader != null) {
          breader.close();
        }
      }
      return nameEmailMap;
    }
    return null;
  }
}
