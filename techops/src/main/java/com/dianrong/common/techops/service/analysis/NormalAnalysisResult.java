package com.dianrong.common.techops.service.analysis;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 普通的分析结果对象.
 */
public class NormalAnalysisResult {
  
  /**
   * 存放按顺序得到的结果.
   */
  private List<String> info = Lists.newArrayList();
  
  /**
   * 结果在输入文件中的行号.
   */
  private int lineNumber;

  public List<String> getInfo() {
    return info;
  }

  public void setInfo(List<String> info) {
    this.info = info;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  @Override
  public String toString() {
    return "NormalAnalysisResult [info=" + info + ", lineNumber=" + lineNumber + "]";
  }
}
