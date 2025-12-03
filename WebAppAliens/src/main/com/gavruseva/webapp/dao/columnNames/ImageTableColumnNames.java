package main.com.gavruseva.webapp.dao.columnNames;

public enum ImageTableColumnNames {
  ID("id"),
  PICTURE_PATH("picture_path"),
  NAME("name"),
  LENGTH("length"),
  WIDTH("width"),
  PRICE("price");

  private String fieldName;

  private ImageTableColumnNames(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
