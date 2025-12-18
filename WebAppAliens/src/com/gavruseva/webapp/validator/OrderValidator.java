package com.gavruseva.webapp.validator;

import com.gavruseva.webapp.model.Order;

public interface OrderValidator {
  boolean isValid(Order order);
}
