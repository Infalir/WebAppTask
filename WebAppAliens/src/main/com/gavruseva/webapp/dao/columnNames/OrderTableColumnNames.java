package main.com.gavruseva.webapp.dao.columnNames;

public enum OrderTableColumnNames {
  ID("id"),
  IMAGE_ID("picture_path"),
  USER_ID("user_id"),
  INK_ID("length"),
  BODYPART("width"),
  ORDER_STATUS("order_status"),
  ORDER_PRICE("order_price");

  private String fieldName;

  private OrderTableColumnNames(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
