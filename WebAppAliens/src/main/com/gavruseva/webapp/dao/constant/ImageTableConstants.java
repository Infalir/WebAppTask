package main.com.gavruseva.webapp.dao.constant;

public enum ImageTableConstants {
  ID("id"),
  PICTURE_PATH("picture_path"),
  NAME("name"),
  LENGTH("length"),
  WIDTH("width"),
  PRICE("price");

  private String fieldName;

  private ImageTableConstants(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
