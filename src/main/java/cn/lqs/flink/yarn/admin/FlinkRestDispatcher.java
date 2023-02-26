package cn.lqs.flink.yarn.admin;


import cn.lqs.flink.yarn.admin.hdfs.FailLoadConfigurationException;
import cn.lqs.flink.yarn.admin.http.ApplicationRoutes;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static cn.lqs.flink.yarn.admin.FlinkYarnAdminLauncher.GlobalConfig;
import static cn.lqs.flink.yarn.admin.hdfs.ConfigurationLoader.SERVER_HOST;
import static cn.lqs.flink.yarn.admin.hdfs.ConfigurationLoader.SERVER_PORT;

public class FlinkRestDispatcher extends AbstractVerticle {

  private final static Logger log = LoggerFactory.getLogger(FlinkRestDispatcher.class);


  @Override
  public void start(Promise<Void> promise) throws FailLoadConfigurationException, IOException {
    // 创建并启动 web server
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(ApplicationRoutes.build(vertx, GlobalConfig))
      .listen(Integer.parseInt(GlobalConfig.get(SERVER_PORT)), GlobalConfig.get(SERVER_HOST),
        http -> {
          if (http.succeeded()) {
            log.info("Server start successfully! -> [{}:{}]", GlobalConfig.get(SERVER_HOST), GlobalConfig.get(SERVER_PORT));
            promise.complete();
          } else {
            log.error("Server start failed!", http.cause());
            promise.fail(http.cause());
          }
        });
  }

}
