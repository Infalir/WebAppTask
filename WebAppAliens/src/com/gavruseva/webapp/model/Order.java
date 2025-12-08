package com.gavruseva.webapp.model;

import lombok.Setter;

import java.util.Objects;

@Setter
public class Order extends AbstractModel{
  public enum OrderStatus{
    COMPLETE,
    CREATED,
    CANCELLED
  }

  private long userId;

  private long imageId;

  private long inkId;

  private String bodyPart;

  private double orderPrice;

  private OrderStatus orderStatus;

  public Order(){}

  public long getUserId(){
    return userId;
  }

  public long getImageId(){
    return imageId;
  }

  public long getInkId(){
    return inkId;
  }

  public String getBodyPart(){
    return bodyPart;
  }

  public double getOrderPrice(){
    return orderPrice;
  }

  public OrderStatus getOrderStatus(){
    return orderStatus;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Order order = (Order) obj;
    return super.equals(order) && Objects.equals(order.orderStatus, orderStatus)
            && order.userId == userId && order.imageId == imageId
            && order.inkId == inkId && Objects.equals(order.bodyPart, bodyPart)
            && order.orderPrice == orderPrice;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = prime + super.hashCode();

    result = prime * result + (orderStatus != null ? orderStatus.hashCode() : 0);
    result = prime * result + Long.hashCode(userId);
    result = prime * result + Long.hashCode(imageId);
    result = prime * result + Long.hashCode(inkId);
    result = prime * result + (bodyPart != null ? bodyPart.hashCode() : 0);
    result = prime * result + Double.hashCode(orderPrice);

    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("Order ");
    builder.append(super.toString()).append(": (");
    builder.append("Order status = ").append(orderStatus).append(", ");
    builder.append("Image ID = ").append(imageId).append(", ");
    builder.append("User ID = ").append(userId).append(", ");
    builder.append("Body Part = ").append(bodyPart).append(")");
    builder.append("Ink ID = ").append(inkId).append(", ");
    builder.append("Order price = ").append(orderPrice).append(")");

    return builder.toString();
  }
}
