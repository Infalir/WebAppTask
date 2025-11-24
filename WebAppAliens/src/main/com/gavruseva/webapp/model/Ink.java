package main.com.gavruseva.webapp.model;

import java.util.Objects;

public class Ink extends AbstractModel {
  private String color;

  private String name;

  private String manufacturer;

  private String allergen;

  private Ink(){}

  public String getPicturePath() {
    return color;
  }

  public String getName() {
    return name;
  }

  public String getWidth() {
    return manufacturer;
  }

  public String getLength() {
    return allergen;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Ink ink = (Ink) obj;
    return super.equals(ink) && Objects.equals(ink.color, color)
            && Objects.equals(ink.name, name)
            && Objects.equals(ink.manufacturer, manufacturer) && Objects.equals(ink.allergen, allergen);
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = prime + super.hashCode();

    result = prime * result + (name != null ? name.hashCode() : 0);
    result = prime * result + (color != null ? color.hashCode() : 0);
    result = prime * result + (manufacturer != null ? manufacturer.hashCode() : 0);
    result = prime * result + (allergen != null ? allergen.hashCode() : 0);

    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("Ink ");
    builder.append(super.toString()).append(": (");
    builder.append("name = ").append(name).append(", ");
    builder.append("color = ").append(color).append(", ");
    builder.append("manufacturer = ").append(manufacturer).append(", ");
    builder.append("allergen = ").append(allergen).append(", ");

    return builder.toString();
  }
}
