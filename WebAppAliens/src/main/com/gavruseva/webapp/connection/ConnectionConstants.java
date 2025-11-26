package main.com.gavruseva.webapp.connection;

public enum ConnectionConstants {
  URL("db.url"),
  POOLSIZE("db.poolsize"),
  USER("db.user"),
  PASSWORD("db.password");

  private String propertyName;

  private ConnectionConstants(String propertyName) {
    this.propertyName = propertyName;
  }

  public String getPropertyName() {
    return propertyName;
  }
}
