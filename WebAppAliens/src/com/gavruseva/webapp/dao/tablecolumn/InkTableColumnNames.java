package com.gavruseva.webapp.dao.tablecolumn;

public enum InkTableColumnNames {
  ID("id"),
  NAME("name"),
  COLOR("color"),
  ALLERGEN("allergen"),
  MANUFACTURER("manufacturer");

  private String fieldName;

  private InkTableColumnNames(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
