package main.com.gavruseva.webapp.model;

import java.util.Objects;

public class BodyPart extends AbstractModel{
  private String name;

  public String getName(){
    return name;
  }

  private BodyPart(){}

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    BodyPart part = (BodyPart) obj;
    return super.equals(part) && Objects.equals(part.name, name);
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = prime + super.hashCode();

    result = prime * result + (name != null ? name.hashCode() : 0);

    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("BodyPart ");
    builder.append(super.toString()).append(": (");
    builder.append("name = ").append(name).append(", ");

    return builder.toString();
  }
}
