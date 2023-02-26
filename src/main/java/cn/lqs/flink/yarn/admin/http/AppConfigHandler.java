package cn.lqs.flink.yarn.admin.http;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.hadoop.conf.Configuration;

import java.util.function.Function;

import static cn.lqs.flink.yarn.admin.hdfs.ConfigurationLoader.*;

public class AppConfigHandler implements Function<RoutingContext, Future<JsonObject>> {

  private final Configuration conf;

  public AppConfigHandler(Configuration conf) {
    this.conf = conf;
  }

  @Override
  public Future<JsonObject> apply(RoutingContext routingContext) {
    JsonObject json = new JsonObject();
    json.put(SERVER_PORT, conf.get(SERVER_PORT));
    json.put(SERVER_HOST, conf.get(SERVER_HOST));
    json.put(HDFS_ADDRESS, conf.get(HDFS_ADDRESS));
    json.put(HADOOP_HOME, conf.get(HADOOP_HOME));
    json.put(HADOOP_USER_NAME, conf.get(HADOOP_USER_NAME));
    return Future.succeededFuture(json);
  }

}
