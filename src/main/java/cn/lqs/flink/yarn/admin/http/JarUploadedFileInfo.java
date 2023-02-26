package cn.lqs.flink.yarn.admin.http;

import io.vertx.ext.web.FileUpload;

public class JarUploadedFileInfo {

  private final String uploadedFilePath;
  private final String filename;
  private final String charset;
  private final long size;
  private final String contentType;

  public static JarUploadedFileInfo createFromFileUpload(FileUpload fileUpload, String uploadPt) {
    return new JarUploadedFileInfo(uploadPt, fileUpload.fileName(),
      fileUpload.charSet(), fileUpload.size(), fileUpload.contentType());
  }

  public JarUploadedFileInfo(String uploadedFilePath, String filename, String charset, long size, String contentType) {
    this.uploadedFilePath = uploadedFilePath;
    this.filename = filename;
    this.charset = charset;
    this.size = size;
    this.contentType = contentType;
  }

  public String getUploadedFilePath() {
    return uploadedFilePath;
  }

  public String getFilename() {
    return filename;
  }

  public String getCharset() {
    return charset;
  }

  public long getSize() {
    return size;
  }

  public String getContentType() {
    return contentType;
  }
}
