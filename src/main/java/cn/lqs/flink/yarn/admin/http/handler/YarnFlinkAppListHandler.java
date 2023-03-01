package cn.lqs.flink.yarn.admin.http.handler;

import cn.lqs.flink.yarn.admin.hdfs.YarnManager;
import cn.lqs.flink.yarn.admin.http.entity.FlinkYarnAppInfo;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class YarnFlinkAppListHandler implements Function<RoutingContext, Future<List<FlinkYarnAppInfo>>> {
  private final YarnManager yarnManager;

  private final static Logger log = LoggerFactory.getLogger(YarnFlinkAppListHandler.class);

  public YarnFlinkAppListHandler(YarnManager yarnManager) {
    this.yarnManager = yarnManager;
  }

  @Override
  public Future<List<FlinkYarnAppInfo>> apply(RoutingContext routingContext) {
    try {
      long start = System.currentTimeMillis();
      List<ApplicationReport> applicationReports = yarnManager.listFlinkApplication();
      log.info("Got yarn application reports cost [{}]ms.", System.currentTimeMillis() - start);
      if (applicationReports == null) {
        return Future.failedFuture(new YarnException("can not connect to yarn cluster"));
      }
      return Future.succeededFuture(applicationReports.stream().map(FlinkYarnAppInfo::new).collect(Collectors.toList()));
    } catch (IOException | YarnException e) {
      log.error("list flink applications on yarn fail!", e);
      return Future.failedFuture(e);
    }
  }
}
