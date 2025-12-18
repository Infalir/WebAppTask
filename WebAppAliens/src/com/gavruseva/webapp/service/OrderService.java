package com.gavruseva.webapp.service;

import com.gavruseva.webapp.exception.ServiceException;
import com.gavruseva.webapp.model.Order;

public interface OrderService {
  Integer saveOrder(Order order) throws ServiceException;
}
