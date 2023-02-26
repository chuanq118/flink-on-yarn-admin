package cn.lqs.flink.manager.config;

public class FailLoadConfigurationException extends Exception{

  public FailLoadConfigurationException() {
    super();
  }

  public FailLoadConfigurationException(String message) {
    super(message);
  }

  public FailLoadConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  public FailLoadConfigurationException(Throwable cause) {
    super(cause);
  }

  protected FailLoadConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
