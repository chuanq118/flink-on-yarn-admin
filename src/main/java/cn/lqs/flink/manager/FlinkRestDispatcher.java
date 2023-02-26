package cn.lqs.flink.manager;

import cn.lqs.flink.manager.config.ApplicationConf;
import cn.lqs.flink.manager.config.FailLoadConfigurationException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;

public class FlinkRestDispatcher extends AbstractVerticle {

  private final static Logger log = LoggerFactory.getLogger(FlinkRestDispatcher.class);

  @Override
  public void start() throws FailLoadConfigurationException {
    ApplicationConf conf = ApplicationConf.load();
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(request -> {
      request.response()
        .putHeader(CONTENT_TYPE, APPLICATION_JSON)
        .end(JsonObject.mapFrom(conf).toString());
    });

    server.listen(8888, http ->{
      if (http.succeeded()) {
        log.info("Server start successfully! -> [{}:{}]", conf.getHost(), conf.getPort());
      }else{
        log.error("Server start failed!", http.cause());
      }
    });
  }

}
