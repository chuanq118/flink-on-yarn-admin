package cn.lqs.flink.yarn.admin.hdfs;

import cn.lqs.flink.yarn.admin.http.entity.FlinkRunRequestBody;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FlinkScriptManager {

  private final static Logger log = LoggerFactory.getLogger(FlinkScriptManager.class);

  private final String flink;

  private FlinkScriptManager(String flink) {
    this.flink = flink;
  }

  public static FlinkScriptManager parseFrom(Configuration cfg) throws FailLoadConfigurationException {
    String flinkHome = cfg.get("flink.home", System.getProperty("FLINK_HOME"));
    if (flinkHome == null) {
      throw new FailLoadConfigurationException("null flink.home");
    }
    return new FlinkScriptManager(Path.of(flinkHome, "bin", "flink").toAbsolutePath().toString());
  }


  public FlinkRunResult runJar(FlinkRunRequestBody flinkRunRequestBody) throws IOException {
    String[] cmd = new String[]{flink, "run-application", "-t", "yarn-application",
      flinkRunRequestBody.getJarPath().trim(), "--config", "@path/to/json"};
    Process startingProcess = Runtime.getRuntime().exec(cmd);
    ProcessHandle.Info info = startingProcess.info();
    log.info("执行 flink run -> [user:{}, cmd:{}, cmd-line:{}]",
      info.user(), info.command().orElse(""), info.commandLine().orElse(""));
    try {
      startingProcess.getOutputStream().close();
      // output
      log.info("try collect error / std output...");
      FutureTask<String> errorF = getOutput(startingProcess.getErrorStream());
      errorF.run();
      FutureTask<String> stdF = getOutput(startingProcess.getInputStream());
      stdF.run();

      FlinkRunResult result = new FlinkRunResult();
      try {
        log.info("waiting err output...");
        String errStr = errorF.get().trim();
        if (ApplicationUtils.hasText(errStr)) {
          log.error("{}", errStr);
          result.setHasError(true);
          result.setErrOutput(errStr);
        }
      } catch (ExecutionException | InterruptedException e) {
        log.error("get error stream error!", e);
      }

      try {
        log.info("waiting std output...");
        String stdStr = stdF.get().trim();
        log.info("{}", stdStr);
        result.setStdOutput(stdStr);
      } catch (ExecutionException | InterruptedException e) {
        log.error("get std stream error!", e);
      }

      try {
        startingProcess.waitFor();
      } catch (InterruptedException e) {
        log.error("process is killed!", e);
      }

      return result;
    }finally {
      if (startingProcess.isAlive()) {
        startingProcess.destroy();
      }
    }
  }

  private FutureTask<String> getOutput(InputStream is) {
    return new FutureTask<String>(() -> {
      try {
        return new String(is.readAllBytes());
      } catch (IOException e) {
        log.error("读取 flink run error stream fail!", e);
      }
      return null;
    });
  }
}
