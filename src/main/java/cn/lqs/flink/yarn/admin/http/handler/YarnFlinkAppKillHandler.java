package cn.lqs.flink.yarn.admin.http.handler;

import cn.lqs.flink.yarn.admin.hdfs.YarnManager;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Function;

public class YarnFlinkAppKillHandler implements Function<RoutingContext, Future<Void>> {

  private final static Logger log = LoggerFactory.getLogger(YarnFlinkAppListHandler.class);

  private final YarnManager yarnManager;

  public YarnFlinkAppKillHandler(YarnManager yarnManager) {
    this.yarnManager = yarnManager;
  }

  @Override
  public Future<Void> apply(RoutingContext routingContext) {
    int id;
    long timestamp;
    try {
      id = Integer.parseInt(routingContext.pathParam("id"));
      timestamp = Long.parseLong(routingContext.pathParam("timestamp"));
    } catch (Exception e) {
      return Future.failedFuture(e);
    }
    try {
      yarnManager.cancelFlinkApplication(timestamp, id);
      return Future.succeededFuture();
    } catch (IOException | YarnException e) {
      log.warn("关闭 flink yarn application fail! [{}_{}]", timestamp, id);
      return Future.failedFuture(e);
    }
  }
}
