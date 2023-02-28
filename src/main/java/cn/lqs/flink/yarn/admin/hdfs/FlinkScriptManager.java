package cn.lqs.flink.yarn.admin.hdfs;

import cn.lqs.flink.yarn.admin.http.entity.FlinkRunRequestBody;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.lqs.flink.yarn.admin.hdfs.ConfigurationLoader.FLINK_HOME;

public class FlinkScriptManager {

  private final static Logger log = LoggerFactory.getLogger(FlinkScriptManager.class);

  private final static Pattern APPLICATION_INFO_PAT = Pattern.compile("Found Web Interface (.*?) of application '(.*?)'\\.", Pattern.DOTALL);


  private final String flink;

  private FlinkScriptManager(String flink) {
    this.flink = flink;
  }

  public static FlinkScriptManager parseFrom(Configuration cfg)  {
    return new FlinkScriptManager(Path.of(cfg.get(FLINK_HOME), "bin", "flink").toAbsolutePath().toString());
  }


  public FlinkRunResult runJar(FlinkRunRequestBody flinkRunRequestBody, int maxSeconds) throws IOException {
    String[] cmd = new String[]{flink, "run-application", "-t", "yarn-application",
      flinkRunRequestBody.getJarPath().trim(), "--config", "@path/to/json"};
    Process startingProcess = Runtime.getRuntime().exec(cmd);
    ProcessHandle.Info info = startingProcess.info();
    log.info("执行 flink run -> [user:{}, cmd:{}, cmd-line:{}]",
      info.user(), info.command().orElse(""), info.commandLine().orElse(""));
    try {
      startingProcess.getOutputStream().close();
      final FlinkRunResult result = new FlinkRunResult();
      // output
      log.info("try collect error / std output...");

      new Thread(() -> {
        log.info("waiting std output...");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(startingProcess.getInputStream()))) {
          String line;
          ArrayList<String> infos = new ArrayList<>(1 << 5);
          while ((line = br.readLine()) != null) {
            infos.add(line);
            log.info("{}", line);
            Matcher mat = APPLICATION_INFO_PAT.matcher(line);
            if (mat.find()) {
              result.setWebInterface(mat.group(1));
              result.setApplicationId(mat.group(2));
            }
          }
          result.setStdOutput(infos);
        } catch (IOException e) {
          log.error("read input stream error!", e);
        }
      }).start();

      new Thread(() -> {
        log.info("waiting err output...");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(startingProcess.getErrorStream()))) {
          String line;
          ArrayList<String> errs = new ArrayList<>(1 << 5);
          while ((line = br.readLine()) != null) {
            log.warn("{}", line);
            errs.add(line);
          }
          result.setErrOutput(errs);
        } catch (IOException e) {
          log.error("read error stream error!", e);
        }
      }).start();

      try {
        boolean ok = startingProcess.waitFor(maxSeconds, TimeUnit.SECONDS);
        if (!ok) {
          result.setOvertime(true);
        }
      } catch (InterruptedException e) {
        log.error("process is killed!", e);
      }
      log.info("return running result!");
      return result;
    }finally {
      if (startingProcess.isAlive()) {
        log.warn("kill process [{}]", startingProcess.pid());
        startingProcess.destroy();
      }
    }
  }

}
