package cn.lqs.flink.yarn.admin.http;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class FailureHandler implements Handler<RoutingContext> {
  @Override
  public void handle(RoutingContext event) {
    if (event.failed()) {
      // TODO 自定义的错误处理
    }
  }
}
