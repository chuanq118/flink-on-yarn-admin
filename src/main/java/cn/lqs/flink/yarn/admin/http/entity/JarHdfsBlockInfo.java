package cn.lqs.flink.yarn.admin.http.entity;

import org.apache.hadoop.fs.FileStatus;

public class JarHdfsBlockInfo {

  private final String path;
  private final long blockSize;
  private final short replication;
  private final String group;
  private final String owner;
  private final long modificationTime;
  private final long accessTime;
  private final long length;

  public JarHdfsBlockInfo(FileStatus fileStatus) {
    this.path = fileStatus.getPath().toString();
    this.blockSize = fileStatus.getBlockSize();
    this.replication = fileStatus.getReplication();
    this.group = fileStatus.getGroup();
    this.owner = fileStatus.getOwner();
    this.modificationTime = fileStatus.getModificationTime();
    this.length = fileStatus.getLen();
    this.accessTime = fileStatus.getAccessTime();
  }

  public String getPath() {
    return path;
  }

  public long getBlockSize() {
    return blockSize;
  }

  public short getReplication() {
    return replication;
  }

  public String getGroup() {
    return group;
  }

  public String getOwner() {
    return owner;
  }

  public long getModificationTime() {
    return modificationTime;
  }

  public long getAccessTime() {
    return accessTime;
  }

  public long getLength() {
    return length;
  }
}
