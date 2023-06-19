package cn.lqs.flink.yarn.admin;

import cn.lqs.flink.yarn.admin.hdfs.ConfigurationLoader;
import cn.lqs.flink.yarn.admin.hdfs.FailLoadConfigurationException;
import io.vertx.core.Vertx;
import org.apache.hadoop.conf.Configuration;

/**
 * 需要在启动前确定应用完成 HADOOP 配置初始化
 */
public class FlinkYarnAdminLauncher {

  protected static final Configuration GlobalConfig;

  static {
    try {
      GlobalConfig = ConfigurationLoader.load();
    } catch (FailLoadConfigurationException e) {
      throw new RuntimeException(e);
    }
  }


  public static void main(String[] args) {
    // int workers = Runtime.getRuntime().availableProcessors();
    // System.out.printf("Start vertx service with [%s] workers.%n", workers);
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new FlinkRestDispatcher());
  }

}
