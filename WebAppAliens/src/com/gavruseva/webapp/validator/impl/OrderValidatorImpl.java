package com.gavruseva.webapp.validator.impl;

import com.gavruseva.webapp.model.Order;
import com.gavruseva.webapp.validator.OrderValidator;

public class OrderValidatorImpl implements OrderValidator {
  @Override
  public boolean isValid(Order order){
    return order.getUserId() > 1 && order.getImageId() > 1 && order.getInkId() > 1 && !order.getBodyPart().isEmpty();
  }
}
