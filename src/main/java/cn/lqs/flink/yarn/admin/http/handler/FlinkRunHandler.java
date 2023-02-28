package cn.lqs.flink.yarn.admin.http.handler;

import cn.lqs.flink.yarn.admin.hdfs.FlinkRunResult;
import cn.lqs.flink.yarn.admin.hdfs.FlinkScriptManager;
import cn.lqs.flink.yarn.admin.http.entity.FlinkRunRequestBody;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

public class FlinkRunHandler implements Handler<RoutingContext> {

  private final FlinkScriptManager flinkScriptManager;

  public FlinkRunHandler(FlinkScriptManager flinkScriptManager) {
    this.flinkScriptManager = flinkScriptManager;
  }


  @Override
  public void handle(RoutingContext event) {
    FlinkRunRequestBody flinkRunRequestBody = event.get(FlinkRunRequestBody.class.getName());
    if (flinkRunRequestBody == null) {
      event.fail(403, new Exception("illegal flink run request body."));
      return;
    }
    if (!flinkRunRequestBody.getJarPath().startsWith("hdfs")) {
      event.fail(403, new Exception("expect hdfs path, but got " + flinkRunRequestBody.getJarPath()));
      return;
    }
    try {
      FlinkRunResult result = flinkScriptManager.runJar(flinkRunRequestBody, 30);
      event.response()
        .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .end(JsonObject.mapFrom(result).encode());
    } catch (IOException e) {
      event.fail(500, e);
    }

  }


}
