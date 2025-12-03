package main.com.gavruseva.webapp.dao.columnNames;

public enum UserTableColumnNames {
  ID("id"),
  EMAIL("email"),
  LOGIN("login"),
  PASSWORD_HASH("password_hash"),
  ROLE("role"),
  STATUS("status");

  private String fieldName;

  private UserTableColumnNames(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
