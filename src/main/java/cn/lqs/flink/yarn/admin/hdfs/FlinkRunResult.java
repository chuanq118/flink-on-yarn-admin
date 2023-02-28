package cn.lqs.flink.yarn.admin.hdfs;

import java.util.List;

public class FlinkRunResult {
  private boolean overtime;
  private List<String> errOutput;
  private List<String> stdOutput;

  private String applicationId;
  private String webInterface;

  public FlinkRunResult() {
  }

  public FlinkRunResult(boolean overtime, List<String> errOutput, List<String> stdOutput, String applicationId, String webInterface) {
    this.overtime = overtime;
    this.errOutput = errOutput;
    this.stdOutput = stdOutput;
    this.applicationId = applicationId;
    this.webInterface = webInterface;
  }

  public boolean isOvertime() {
    return overtime;
  }

  public void setOvertime(boolean overtime) {
    this.overtime = overtime;
  }

  public List<String> getErrOutput() {
    return errOutput;
  }

  public void setErrOutput(List<String> errOutput) {
    this.errOutput = errOutput;
  }

  public List<String> getStdOutput() {
    return stdOutput;
  }

  public void setStdOutput(List<String> stdOutput) {
    this.stdOutput = stdOutput;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getWebInterface() {
    return webInterface;
  }

  public void setWebInterface(String webInterface) {
    this.webInterface = webInterface;
  }
}
