package com.gavruseva.webapp.connection;

public enum ConnectionParameters {
  URL("db.url"),
  POOLSIZE("db.poolsize"),
  USER("db.user"),
  PASSWORD("db.password");

  private String propertyName;

  private ConnectionParameters(String propertyName) {
    this.propertyName = propertyName;
  }

  public String getPropertyName() {
    return propertyName;
  }
}
