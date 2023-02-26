package cn.lqs.flink.yarn.admin.http.handler;

import cn.lqs.flink.yarn.admin.hdfs.HdfsJarManager;
import cn.lqs.flink.yarn.admin.http.entity.JarHdfsBlockInfo;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JarListHandler implements Function<RoutingContext, Future<List<JarHdfsBlockInfo>>> {

  private final static Logger log = LoggerFactory.getLogger(JarListHandler.class);

  private final HdfsJarManager hdfsJarManager;

  public JarListHandler(HdfsJarManager hdfsJarManager) {
    this.hdfsJarManager = hdfsJarManager;
  }

  @Override
  public Future<List<JarHdfsBlockInfo>> apply(RoutingContext routingContext) {
    List<JarHdfsBlockInfo> jarList = new ArrayList<>(1 << 4);
    try {
      for (FileStatus status : hdfsJarManager.listJar()) {
        jarList.add(new JarHdfsBlockInfo(status));
      }
      return Future.succeededFuture(jarList);
    } catch (IOException e) {
      log.error("list jar fail!", e);
      return Future.failedFuture(e);
    }
  }
}
