package cn.lqs.flink.yarn.admin.http;

import cn.lqs.flink.yarn.admin.hdfs.FailLoadConfigurationException;
import cn.lqs.flink.yarn.admin.hdfs.FlinkScriptManager;
import cn.lqs.flink.yarn.admin.hdfs.HdfsJarManager;
import cn.lqs.flink.yarn.admin.hdfs.YarnManager;
import cn.lqs.flink.yarn.admin.http.entity.FlinkRunRequestBody;
import cn.lqs.flink.yarn.admin.http.handler.*;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class ApplicationRoutes {

  private final static Logger log = LoggerFactory.getLogger(ApplicationRoutes.class);

  private final static String PREFIX = "/api";
  private final static String PREFIX_V2 = "/api/v2";

  public static Router build(Vertx vertx, Configuration conf) throws IOException, FailLoadConfigurationException {
    Router router = Router.router(vertx);
    // 允许 GET POST 所有的跨域
    handleCors(router);
    // 配置静态 web ui
    handleWebui(router);
    // XFrame 允许
    // router.route().handler(XFrameHandler.create(XFrameHandler.SAMEORIGIN));

    // hdfs-jar / flink manager
    HdfsJarManager hdfsJarManager = HdfsJarManager.create(conf);
    FlinkScriptManager flinkScriptManager = FlinkScriptManager.parseFrom(conf);
    YarnManager yarnManager = new YarnManager(conf);

    router.get("/").respond(ctx -> ctx.redirect("/ui"));

    // 打印配置信息
    router.get(PREFIX + "/app/config")
      .respond(new AppConfigHandler(conf));
    router.get(PREFIX + "/jar/list")
      .respond(new JarListHandler(hdfsJarManager));
    router.get(PREFIX + "/flink/applications")
      .respond(new YarnFlinkAppListHandler(yarnManager));
    router.get(PREFIX + "/flink/application/kill/:id/:timestamp")
      .respond(new YarnFlinkAppKillHandler(yarnManager));

    // 在 post 请求前安装对请求体的处理器
    router.post(PREFIX + "/*").handler(BodyHandler.create());
    // flink run jar
    router.post(PREFIX + "/flink/run")
      .consumes(HttpHeaderValues.APPLICATION_JSON.toString())
      .handler(ctx -> {
        ctx.put(FlinkRunRequestBody.class.getName(), ctx.body().asPojo(FlinkRunRequestBody.class));
        ctx.next();
      })
      .blockingHandler(new FlinkRunHandler(flinkScriptManager));
    // add jar file upload handler
    router.post(PREFIX + "/jar/upload")
      .blockingHandler(new JarUploadHandler(hdfsJarManager));

    // 默认此处返回所有的错误信息 -> 可更换到 FailureHandler(自定义)
    router.route(PREFIX + "/*").failureHandler(ErrorHandler.create(vertx, true));

    log.info("Routes build successfully!");
    return router;
  }

  private static void handleCors(Router router) {
    Set<HttpMethod> allowedHttpMethods = new HashSet<>(2);
    allowedHttpMethods.add(HttpMethod.GET);
    allowedHttpMethods.add(HttpMethod.POST);
    router.route("/*").handler(CorsHandler.create()
      .addOrigin("*")
      .allowedHeader("*")
      .allowedMethods(allowedHttpMethods)
      .exposedHeader("*").maxAgeSeconds(3600));
  }

  private static void handleWebui(Router router) {
    router.route("/ui/*").handler(StaticHandler.create("ui"));
  }

}
