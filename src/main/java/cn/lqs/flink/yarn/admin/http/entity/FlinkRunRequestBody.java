package cn.lqs.flink.yarn.admin.http.entity;

import java.util.Map;

public class FlinkRunRequestBody {

  private String source;
  private String sink;
  private Map<String, Object> jsonConfig;
  private String jarPath;

  public FlinkRunRequestBody() {
  }

  public FlinkRunRequestBody(String source, String sink, Map<String, Object> jsonConfig, String jarPath) {
    this.source = source;
    this.sink = sink;
    this.jsonConfig = jsonConfig;
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

  public Map<String, Object> getJsonConfig() {
    return jsonConfig;
  }

  public void setJsonConfig(Map<String, Object> jsonConfig) {
    this.jsonConfig = jsonConfig;
  }

  public String getJarPath() {
    return jarPath;
  }

  public String jsonConfigString() {
    if (jsonConfig != null) {
      return jsonConfig.toString();
    }
    return "";
  }
}
