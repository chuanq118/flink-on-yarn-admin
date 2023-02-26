package cn.lqs.flink.yarn.admin.http;

import cn.lqs.flink.yarn.admin.hdfs.HdfsJarManager;
import cn.lqs.flink.yarn.admin.http.handler.AppConfigHandler;
import cn.lqs.flink.yarn.admin.http.handler.JarListHandler;
import cn.lqs.flink.yarn.admin.http.handler.JarUploadHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ApplicationRoutes {

  private final static String PREFIX = "/api";

  public static Router build(Vertx vertx, Configuration conf) throws IOException {
    Router router = Router.router(vertx);
    // 允许 GET POST 所有的跨域
    handleCors(router);
    // 配置静态 web ui
    handleWebui(router);

    // hdfs jar manager
    HdfsJarManager hdfsJarManager = HdfsJarManager.create(conf);
    // 打印配置信息
    router.get(PREFIX + "/app/config")
      .respond(new AppConfigHandler(conf));
    router.get(PREFIX + "/jar/list")
      .respond(new JarListHandler(hdfsJarManager));

    // 在 post 请求前安装对请求体的处理器
    router.post(PREFIX + "/*").handler(BodyHandler.create());
    // add jar file upload handler
    router.post(PREFIX + "/jar/upload")
      .blockingHandler(new JarUploadHandler(hdfsJarManager));

    // 默认此处返回所有的错误信息 -> 可更换到 FailureHandler(自定义)
    router.route(PREFIX + "/*").failureHandler(ErrorHandler.create(vertx, true));

    return router;
  }

  private static void handleCors(Router router) {
    Set<HttpMethod> allowedHttpMethods = new HashSet<>(2);
    allowedHttpMethods.add(HttpMethod.GET);
    allowedHttpMethods.add(HttpMethod.POST);
    router.route().handler(CorsHandler.create()
      .addOrigin("*")
      .allowedHeader("*")
      .allowedMethods(allowedHttpMethods)
      .exposedHeader("*").maxAgeSeconds(3600));
  }

  private static void handleWebui(Router router) {
    router.route("/ui/*").handler(StaticHandler.create("ui"));
  }

}
