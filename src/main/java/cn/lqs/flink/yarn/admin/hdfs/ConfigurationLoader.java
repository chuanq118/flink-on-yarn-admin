package cn.lqs.flink.yarn.admin.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static cn.lqs.flink.yarn.admin.hdfs.ApplicationUtils.hasText;


public final class ConfigurationLoader {

  private final static Logger log = LoggerFactory.getLogger(ConfigurationLoader.class);

  public static final String RUNNING_DIR = System.getProperty("user.dir");

  public static final String SERVER_PORT = "server.port";
  public static final String SERVER_HOST = "server.host";
  public static final String HDFS_ADDRESS = "hdfs.address";
  public static final String HADOOP_HOME = "hadoop.home.dir";
  public static final String HADOOP_USER_NAME = "hadoop.user.name";

  public static Configuration load() throws FailLoadConfigurationException {
    Configuration config = new Configuration();
    // 加载自定义配置
    loadCustomConfig(config);
    // 配置控制权限
    setHadoopHome(config);
    setHadoopUsername(config);
    // 配置 hdfs 访问地址
    setHdfsAddress(config);
    // 配置 web server
    setWebServer(config);
    return config;
  }

  private static void loadCustomConfig(Configuration cfg){
    org.apache.hadoop.fs.Path path = new org.apache.hadoop.fs.Path(RUNNING_DIR, "flink-on-yarn-admin.xml");
    log.info("加载自定义配置文件 -> [{}]", path);
    cfg.addResource(path);

  }

  @Deprecated
  private static String loadCustomConfigXml() {
    File configF = Path.of(RUNNING_DIR, "flink-on-yarn-admin.xml").toFile();
    String xml = "";
    if (configF.exists()) {
      try (FileInputStream fis = new FileInputStream(configF)) {
        xml = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
      } catch (IOException e) {
        log.error("读取配置文件 [flink-on-yarn-admin.xml] 失败! 无法加载配置项!", e);
      }
    }
    return xml;
  }

  private static void  setHadoopHome(Configuration cfg) throws FailLoadConfigurationException {
    String hadoopHome = cfg.get(HADOOP_HOME);
    if (hadoopHome == null) {
      hadoopHome = System.getenv("HADOOP_HOME");
    }
    if (!hasText(hadoopHome)) {
      throw new FailLoadConfigurationException("can't find hadoop home!");
    }
    cfg.set(HADOOP_HOME, hadoopHome);
    Path hadoopEtc = Path.of(hadoopHome, "etc", "hadoop");
    if (hadoopEtc.toFile().exists() && hadoopEtc.toFile().isDirectory()) {
      File[] files = hadoopEtc.toFile().listFiles();
      // 如果 hadoop home 被正确配置,我们读取所有的 XML 配置文件
      if (files != null) {
        for (File file : files) {
          if (file.getName().endsWith(".xml")) {
            cfg.addResource(new org.apache.hadoop.fs.Path(file.toPath().toAbsolutePath().toString()));
          }
        }
      }
    }
    System.setProperty(HADOOP_HOME, hadoopHome);
  }

  private static void setHadoopUsername(Configuration cfg) throws FailLoadConfigurationException {
    String hadoopUsername = cfg.get(HADOOP_USER_NAME);
    if (hadoopUsername == null) {
      hadoopUsername = System.getProperty("user.name");
    }
    if (!hasText(hadoopUsername)) {
      throw new FailLoadConfigurationException("can't find hadoop username!");
    }
    cfg.set(HADOOP_USER_NAME, hadoopUsername);
    System.setProperty("HADOOP_USER_NAME", hadoopUsername);
  }

  private static void setHdfsAddress(Configuration cfg) throws FailLoadConfigurationException {
    try {
      if (cfg.get(HDFS_ADDRESS) == null) {
        if (cfg.get("fs.defaultFS") == null) {
          cfg.set(HDFS_ADDRESS, InetAddress.getLocalHost().getHostName() + ":8020");
        }else {
          cfg.set(HDFS_ADDRESS, cfg.get("fs.defaultFS"));
        }
      }
    } catch (UnknownHostException e) {
      throw new FailLoadConfigurationException(e);
    }
  }

  private static void setWebServer(Configuration cfg) throws FailLoadConfigurationException {
    if (cfg.get(SERVER_PORT) == null) {
      cfg.set(SERVER_PORT, "8888");
    }
    if (cfg.get(SERVER_HOST) == null) {
      try {
        cfg.set(SERVER_HOST, InetAddress.getLocalHost().getHostName());
      } catch (UnknownHostException e) {
        throw new FailLoadConfigurationException(e);
      }
    }
  }
}
