package cn.lqs.flink.yarn.admin.http.handler;

import cn.lqs.flink.yarn.admin.hdfs.HdfsJarManager;
import cn.lqs.flink.yarn.admin.http.entity.JarUploadedFileInfo;
import io.vertx.core.Handler;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static cn.lqs.flink.yarn.admin.hdfs.ConfigurationLoader.RUNNING_DIR;


public class JarUploadHandler implements Handler<RoutingContext> {

  private final static Logger log = LoggerFactory.getLogger(JarUploadHandler.class);

  private final HdfsJarManager hdfsJarManager;

  public JarUploadHandler(HdfsJarManager hdfsJarManager) {
    this.hdfsJarManager = hdfsJarManager;
  }

  @Override
  public void handle(RoutingContext event) {
    List<FileUpload> fileUploads = event.fileUploads();
    boolean foundJar = false;
    for (FileUpload fileUpload : fileUploads) {
      if (fileUpload.name().equalsIgnoreCase("jar")) {
        // 上传 jar 到 hdfs
        log.info("accept jar file [{}]. Try upload to hdfs...", fileUpload.fileName());
        File jarF = Path.of(RUNNING_DIR, fileUpload.uploadedFileName()).toFile();
        try {
          String uploadPt = hdfsJarManager.uploadJar(jarF, fileUpload.fileName());
          event.json(JarUploadedFileInfo.createFromFileUpload(fileUpload, uploadPt));
        } catch (IOException e) {
          log.error("upload jar to hdfs fail!", e);
          event.fail(500, e);
        }
        foundJar = true;
        break;
      }
    }
    if (!foundJar) {
      event.fail(404, new FileNotFoundException("not found jar file."));
    }
  }
}
