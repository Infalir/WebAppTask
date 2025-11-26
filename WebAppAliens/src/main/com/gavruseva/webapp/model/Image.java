package main.com.gavruseva.webapp.model;

import lombok.Setter;

import java.util.Objects;

@Setter
public class Image extends AbstractModel {
  private String picturePath;

  private String name;

  private double width;

  private double length;

  private double price;

  public Image(){}

  public String getPicturePath() {
    return picturePath;
  }

  public String getName() {
    return name;
  }

  public double getWidth() {
    return width;
  }

  public double getLength() {
    return length;
  }

  public double getPrice() {
    return price;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Image image = (Image) obj;
    return super.equals(image) && Objects.equals(image.picturePath, picturePath)
            && Objects.equals(image.name, name)
            && image.width == width && image.length == length
            && image.price == price ;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = prime + super.hashCode();

    result = prime * result + (name != null ? name.hashCode() : 0);
    result = prime * result + (picturePath != null ? picturePath.hashCode() : 0);
    result = prime * result + Double.hashCode(width);
    result = prime * result + Double.hashCode(length);
    result = prime * result + Double.hashCode(price);

    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("Image ");
    builder.append(super.toString()).append(": (");
    builder.append("name = ").append(name).append(", ");
    builder.append("picture path = ").append(picturePath).append(", ");
    builder.append("width = ").append(width).append(", ");
    builder.append("length = ").append(length).append(", ");
    builder.append("price = ").append(price).append(", ");

    return builder.toString();
  }
}
