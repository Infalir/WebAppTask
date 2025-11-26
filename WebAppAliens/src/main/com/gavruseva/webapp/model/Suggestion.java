package main.com.gavruseva.webapp.model;

import lombok.Setter;

import java.util.Objects;

@Setter
public class Suggestion extends AbstractModel{
  public enum SuggestionStatus{
    ACCEPTED,
    CREATED,
    REJECTED
  }
  private long userId;
  private String picturePath;
  private double width;
  private double length;
  private SuggestionStatus status;

  public Suggestion() {}

  public long getUserId() {
    return userId;
  }
  public String getPicturePath() {
    return picturePath;
  }
  public double getWidth() {
    return width;
  }
  public double getLength() {
    return length;
  }
  public SuggestionStatus getStatus() {
    return status;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Suggestion suggestion = (Suggestion) obj;
    return super.equals(suggestion) && Objects.equals(suggestion.picturePath, picturePath)
            && Objects.equals(suggestion.status, status)
            && suggestion.width == width && suggestion.length == length
            && suggestion.userId == userId;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = prime + super.hashCode();

    result = prime * result + (status != null ? status.hashCode() : 0);
    result = prime * result + (picturePath != null ? picturePath.hashCode() : 0);
    result = prime * result + Double.hashCode(width);
    result = prime * result + Double.hashCode(length);
    result = prime * result + Long.hashCode(userId);

    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("Suggestion ");
    builder.append(super.toString()).append(": (");
    builder.append("status = ").append(status).append(", ");
    builder.append("picture path = ").append(picturePath).append(", ");
    builder.append("width = ").append(width).append(", ");
    builder.append("length = ").append(length).append(", ");
    builder.append("User Id = ").append(userId).append(", ");

    return builder.toString();
  }
}
