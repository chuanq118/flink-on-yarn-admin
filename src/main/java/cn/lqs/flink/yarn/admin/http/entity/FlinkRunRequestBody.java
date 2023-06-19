package cn.lqs.flink.yarn.admin.http.entity;

public class FlinkRunRequestBody {

  private String source;
  private String sink;
  private String configId;
  private String jarPath;

  public FlinkRunRequestBody() {
  }

  public FlinkRunRequestBody(String source, String sink, String configId, String jarPath) {
    this.source = source;
    this.sink = sink;
    this.configId = configId;
    this.jarPath = jarPath;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public void setSink(String sink) {
    this.sink = sink;
  }



  public void setJarPath(String jarPath) {
    this.jarPath = jarPath;
  }

  public String getSource() {
    return source;
  }

  public String getSink() {
    return sink;
  }

  public String getConfigId() {
    return configId;
  }

  public void setConfigId(String configId) {
    this.configId = configId;
  }

  public String getJarPath() {
    return jarPath;
  }
}
