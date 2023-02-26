package cn.lqs.flink.yarn.admin.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import static cn.lqs.flink.yarn.admin.hdfs.ConfigurationLoader.HDFS_ADDRESS;

public class HdfsJarManager {

  public final static String JAR_UPLOAD_PATH = "/flink-jars";
  public final static int BUFFER_SIZE = 64 * 1024 * 8;

  private final DistributedFileSystem hdfs;
  private final String hdfsAddr;

  private HdfsJarManager(DistributedFileSystem hdfs, String hdfsAddr) {
    this.hdfs = hdfs;
    this.hdfsAddr = hdfsAddr;
  }

  public static HdfsJarManager create(Configuration hadoopCfg) throws IOException {
    // 注意此处我们 new 了一个 Configuration, 这是避免我们定义的配置影响到主机上的 hadoop 配置.
    String hdfsAddress = hadoopCfg.get(HDFS_ADDRESS);
    return new HdfsJarManager((DistributedFileSystem) FileSystem.get(URI
      .create(hdfsAddress), new Configuration()), hdfsAddress);
  }

  public DistributedFileSystem getHdfs() {
    return hdfs;
  }

  /**
   * upload jar to hdfs
   * @param jarF jar file
   * @param resolvePath jar pt
   * @return abs path
   * @throws IOException io exception
   */
  public String uploadJar(File jarF, String resolvePath) throws IOException {
    resolvePath = resolvePath.startsWith("/") ? resolvePath : "/" + resolvePath;
    Path uploadPt = new Path(JAR_UPLOAD_PATH + resolvePath);
    try (FSDataOutputStream fsDos = hdfs.create(uploadPt, true);
         FileInputStream fis = new FileInputStream(jarF);) {
      byte[] buff = new byte[BUFFER_SIZE];
      int len;
      while ((len = fis.read(buff)) != -1) {
        fsDos.write(buff, 0, len);
        fsDos.flush();
      }
    }
    return hdfsAddr + uploadPt.toUri().toString();
  }

  public FileStatus[] listJar() throws IOException {
     return hdfs.listStatus(new Path(JAR_UPLOAD_PATH));
  }
}
