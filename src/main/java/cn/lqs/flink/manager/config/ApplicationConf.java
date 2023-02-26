package cn.lqs.flink.manager.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class ApplicationConf {

  private final int port;
  private final String host;
  private final String hadoopHome;

  private ApplicationConf(int port, String host, String hadoopHome){
    this.port = port;
    this.host = host;
    this.hadoopHome = hadoopHome;
  }

  public static ApplicationConf load() throws FailLoadConfigurationException {
    File configF = Path.of(System.getProperty("user.dir"), "flink-rest-controller.conf").toFile();
    Map<String, String> params = new HashMap<>(1 << 3);
    try (BufferedReader br = new BufferedReader(new FileReader(configF, StandardCharsets.UTF_8))){
      String line;
      while ((line = br.readLine()) != null){
        line = line.trim();
        if (line.startsWith("#")) {
          continue;
        }
        String[] pieces = line.split("=");
        if (pieces.length == 2) {
          String value = pieces[1].trim();
          if (value.length() > 0) {
            params.put(pieces[0].trim(), value);
          }
        }
      }
      return new ApplicationConf(
        Integer.parseInt(params.getOrDefault("port", "1234")),
        params.getOrDefault("host", InetAddress.getLocalHost().getHostName()),
        params.getOrDefault("HADOOP_HOME", System.getenv("HADOOP_HOME")));
    }catch (Exception e) {
      throw new FailLoadConfigurationException(e);
    }
  }

  public int getPort() {
    return port;
  }

  public String getHost() {
    return host;
  }

  public String getHadoopHome() {
    return hadoopHome;
  }
}
