package main.com.gavruseva.webapp.dao.constant;

public enum UserTableConstants {
  ID("id"),
  EMAIL("email"),
  LOGIN("login"),
  PASSWORD_HASH("password_hash"),
  ROLE("role"),
  STATUS("status");

  private String fieldName;

  private UserTableConstants(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
