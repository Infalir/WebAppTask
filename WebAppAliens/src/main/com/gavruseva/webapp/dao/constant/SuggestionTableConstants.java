package main.com.gavruseva.webapp.dao.constant;

public enum SuggestionTableConstants {
  ID("id"),
  PICTURE_PATH("picture_path"),
  USER_ID("user_id"),
  LENGTH("length"),
  WIDTH("width"),
  SUGGESTION_STATUS("suggestion_status");

  private String fieldName;

  private SuggestionTableConstants(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
