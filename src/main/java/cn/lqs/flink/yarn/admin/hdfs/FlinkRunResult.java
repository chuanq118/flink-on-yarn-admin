package cn.lqs.flink.yarn.admin.hdfs;

public class FlinkRunResult {
  private boolean hasError;
  private String errOutput;
  private String stdOutput;

  private String applicationId;
  private String webInterface;

  public FlinkRunResult() {
  }

  public FlinkRunResult(boolean hasError, String errOutput, String stdOutput, String applicationId, String webInterface) {
    this.hasError = hasError;
    this.errOutput = errOutput;
    this.stdOutput = stdOutput;
    this.applicationId = applicationId;
    this.webInterface = webInterface;
  }

  public boolean isHasError() {
    return hasError;
  }

  public void setHasError(boolean hasError) {
    this.hasError = hasError;
  }

  public String getErrOutput() {
    return errOutput;
  }

  public void setErrOutput(String errOutput) {
    this.errOutput = errOutput;
  }

  public String getStdOutput() {
    return stdOutput;
  }

  public void setStdOutput(String stdOutput) {
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
