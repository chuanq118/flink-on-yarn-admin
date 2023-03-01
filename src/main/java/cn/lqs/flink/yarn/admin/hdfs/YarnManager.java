package cn.lqs.flink.yarn.admin.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class YarnManager implements Closeable {

  private final static Logger log = LoggerFactory.getLogger(YarnManager.class);

  private final YarnClient yarn;
  private final Set<String> flinkApplicationTypeSet;

  public YarnManager(Configuration configuration) {
    this.yarn = YarnClient.createYarnClient();
    this.yarn.init(configuration);
    log.info("yarn client start connecting...");
    // 这是一个异步方法,执行不成功则 yarn 为 null
    this.yarn.start();
    // log.info("got yarn cluster name:[{}] start-time:[{}]", this.yarn.getName(), this.yarn.getStartTime());
    flinkApplicationTypeSet = new HashSet<>();
    flinkApplicationTypeSet.add("Apache Flink");
  }

  @Override
  public void close() throws IOException {
    yarn.close();
  }

  public List<ApplicationReport> listFlinkApplication() throws IOException, YarnException {
    if (yarn == null) {
      return null;
    }

    return yarn.getApplications(flinkApplicationTypeSet);
  }

  public void cancelFlinkApplication(long clusterTimestamp, int id) throws IOException, YarnException {
    yarn.killApplication(ApplicationId.newInstance(clusterTimestamp, id));
  }

}
