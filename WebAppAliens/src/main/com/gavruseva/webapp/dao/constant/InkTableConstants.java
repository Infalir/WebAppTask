package main.com.gavruseva.webapp.dao.constant;

public enum InkTableConstants {
  ID("id"),
  NAME("name"),
  COLOR("color"),
  ALLERGEN("allergen"),
  MANUFACTURER("manufacturer");

  private String fieldName;

  private InkTableConstants(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
