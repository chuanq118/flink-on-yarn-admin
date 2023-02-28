package cn.lqs.flink.yarn.admin.http.entity;

import org.apache.hadoop.yarn.api.records.ApplicationReport;

public class FlinkYarnAppInfo {

  private int id;
  private long timestamp;
  private String name;
  private String type;
  private String trackingUrl;
  private String queue;
  private long launchTime;
  private String state;
  private String user;
  private String host;

  public FlinkYarnAppInfo(ApplicationReport report) {
    this.id = report.getApplicationId().getId();
    this.timestamp = report.getApplicationId().getClusterTimestamp();
    this.trackingUrl = report.getTrackingUrl();
    this.name = report.getName();
    this.type = report.getApplicationType();
    this.launchTime = report.getLaunchTime();
    this.queue = report.getQueue();
    this.state = report.getYarnApplicationState().name();
    this.user = report.getUser();
    this.host = report.getHost();
  }

  public FlinkYarnAppInfo() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTrackingUrl() {
    return trackingUrl;
  }

  public void setTrackingUrl(String trackingUrl) {
    this.trackingUrl = trackingUrl;
  }

  public String getQueue() {
    return queue;
  }

  public void setQueue(String queue) {
    this.queue = queue;
  }

  public long getLaunchTime() {
    return launchTime;
  }

  public void setLaunchTime(long launchTime) {
    this.launchTime = launchTime;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }
}
