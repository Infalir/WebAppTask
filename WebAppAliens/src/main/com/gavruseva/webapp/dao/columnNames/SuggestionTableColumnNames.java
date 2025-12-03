package main.com.gavruseva.webapp.dao.columnNames;

public enum SuggestionTableColumnNames {
  ID("id"),
  PICTURE_PATH("picture_path"),
  USER_ID("user_id"),
  LENGTH("length"),
  WIDTH("width"),
  SUGGESTION_STATUS("suggestion_status");

  private String fieldName;

  private SuggestionTableColumnNames(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
